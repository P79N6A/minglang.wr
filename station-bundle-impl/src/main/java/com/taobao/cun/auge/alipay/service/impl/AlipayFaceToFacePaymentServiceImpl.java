package com.taobao.cun.auge.alipay.service.impl;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.FileItem;
import com.alipay.api.domain.AddressInfo;
import com.alipay.api.domain.ContactInfo;
import com.alipay.api.request.AntMerchantExpandIndirectImageUploadRequest;
import com.alipay.api.request.AntMerchantExpandIndirectZftCreateRequest;
import com.alipay.api.request.AntMerchantExpandOrderQueryRequest;
import com.alipay.api.response.AntMerchantExpandIndirectImageUploadResponse;
import com.alipay.api.response.AntMerchantExpandIndirectZftCreateResponse;
import com.alipay.api.response.AntMerchantExpandOrderQueryResponse;
import com.taobao.common.tfs.TfsManager;
import com.taobao.cun.attachment.dto.AttachmentDto;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.alipay.dto.AlipayFaceToFaceOrderInfo;
import com.taobao.cun.auge.alipay.dto.AlipayFaceToFacePaymentResult;
import com.taobao.cun.auge.alipay.dto.StationAlipayInfoDto;
import com.taobao.cun.auge.alipay.process.AlipayClientProcessor;
import com.taobao.cun.auge.alipay.service.AlipayFaceToFacePaymentService;
import com.taobao.cun.auge.alipay.service.StationAlipayInfoService;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.CuntaoQualificationBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.recruit.partner.dto.PartnerApplyDto;
import com.taobao.cun.recruit.partner.service.PartnerApplyService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service("alipayFaceToFacePaymentService")
@HSFProvider(serviceInterface= AlipayFaceToFacePaymentService.class, clientTimeout = 10000)
public class AlipayFaceToFacePaymentServiceImpl implements AlipayFaceToFacePaymentService {

    private static final Logger logger = LoggerFactory.getLogger(AlipayFaceToFacePaymentServiceImpl.class);

    public static final String DOLWAN_IMAGE_URI = "/image/doDownLoadImage.json";

    public static final String DOLWAN_QUAN_IMAGE_URI="http://sellercenter.cn-hangzhou.oss-pub.aliyun-inc.com/";

    public static final String OUT_DOOR_IMAGE_URL="http://img.alicdn.com/imgextra/i3/3405569954/O1CN01fNR9HU2NOyxVAeRUz_!!3405569954.png";

    public static final String INDIVIDUAL_BUSINESS="1";

    public static final String ENTERPRISE="2";

    public static final AlipayClient alipayClient = AlipayClientProcessor.getAlipayClient();
    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private TfsManager tfsManager;

    @Autowired
    private PartnerApplyService partnerApplyService;

    @Autowired
    private StationAlipayInfoService  stationAlipayInfoService;

    @Autowired
    private CuntaoQualificationBO cuntaoQualificationBO;

    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

    @Autowired
    private AccountMoneyBO accountMoneyBO;

    @Autowired
    private StationBO stationBO;

    @Resource
    private UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;



    @Override
    public AlipayFaceToFacePaymentResult<String> createTwoLevelMerchants(Long taobaoUserId) {

        AlipayFaceToFacePaymentResult<String> result=new AlipayFaceToFacePaymentResult<String>();
        try {
            PartnerApplyDto apply = partnerApplyService.getPartnerApplyByTaobaoUserId(taobaoUserId);
            if(apply!=null){
                AntMerchantExpandIndirectImageUploadResponse idcardImgsFaceResponse=  uploadIdcardImgsFace(apply.getId());
                logger.info("taobaoUserId="+taobaoUserId+",uploadIdCardImgsFace="+idcardImgsFaceResponse.isSuccess());
                if(!idcardImgsFaceResponse.isSuccess()){
                    result.setSuccess(false);
                    result.setResultCode("UPLOAD_IMAGE_ERROR");
                    result.setErrorMsg("uploadIdCardImgsFace erroe");
                    return result;
                }
                AntMerchantExpandIndirectImageUploadResponse idcardImgsBackResponse= uploadIdcardImgsBack(apply.getId());
                logger.info("taobaoUserId="+taobaoUserId+",uploadIdCardImgsBack="+idcardImgsBackResponse.isSuccess());
                if(!idcardImgsBackResponse.isSuccess()){
                    result.setSuccess(false);
                    result.setResultCode("UPLOAD_IMAGE_ERROR");
                    result.setErrorMsg("uploadIdCardImgsBack erroe");
                    return result;
                }

                CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
                PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
                if(instance!=null){
                    Station station=stationBO.getStationById(instance.getStationId());
                    AccountMoneyDto accountMoneyDto= accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND, AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
//                    AntMerchantExpandIndirectImageUploadResponse certImageResponse= uploadCertImage(cuntaoQualification);
//                    logger.info("taobaoUserId="+taobaoUserId+",certImageResponse="+certImageResponse.isSuccess());
//                    if(!certImageResponse.isSuccess()){
//                        result.setSuccess(false);
//                        result.setResultCode("UPLOAD_IMAGE_ERROR");
//                        result.setErrorMsg("uploadCertImgs erroe");
//                        return result;
//                    }
                    ResultDO<BasePaymentAccountDO>paymentAccountDOResultDO= uicPaymentAccountReadServiceClient.getAccountByUserId(apply.getTaobaoUserId());
                    AntMerchantExpandIndirectZftCreateResponse response= createZft(instance,apply, paymentAccountDOResultDO,station,accountMoneyDto,cuntaoQualification,idcardImgsFaceResponse,idcardImgsBackResponse);
                    if(response.isSuccess()){
                        result.setSuccess(true);
                        result.setData(response.getOrderId());
                        logger.info("taobaoUserId="+taobaoUserId+",orderId="+response.getOrderId());
                        StationAlipayInfoDto oldInfo= stationAlipayInfoService.getStationAlipayInfoByTaobaoUserId(taobaoUserId.toString());
                        if(oldInfo==null){
                            StationAlipayInfoDto stationAlipayInfoDto=new StationAlipayInfoDto();
                            stationAlipayInfoDto.setAlipayAccount(paymentAccountDOResultDO.getModule().getOutUser());
                            stationAlipayInfoDto.setAlipayOrderId(response.getOrderId());
                            stationAlipayInfoDto.setStationId(station.getId().toString());
                            stationAlipayInfoDto.setTaobaoUserId(taobaoUserId.toString());
                            stationAlipayInfoDto.setIsDeleted("n");
                            stationAlipayInfoDto.setGmtCreate(new Date());
                            stationAlipayInfoDto.setGmtModified(new Date());
                            Long id=stationAlipayInfoService.saveStationAlipayInfo(stationAlipayInfoDto);
                            logger.info("taobaoUserId="+taobaoUserId+",saveStationAlipayInfo=success,saveStationAlipayInfoId="+id.toString());
                        }
                        logger.info("taobaoUserId="+taobaoUserId+",createTwoLevelMerchants=success");
                    }
                    else{
                        result.setSuccess(false);
                        result.setResultCode(response.getCode());
                        result.setErrorMsg(response.getMsg());
                        logger.info("taobaoUserId="+taobaoUserId+",createTwoLevelMerchants=false"+",errorMsg="+response.getMsg());
                    }
                 }
              }
           }  catch (Exception e) {
            logger.error("taobaoUserId="+taobaoUserId+",createTwoLevelMerchants error", e);
            result.setSuccess(false);
            result.setResultCode("EXCEPTION");
            result.setErrorMsg(e.getMessage());
         }
        return result;
    }

    @Override
    public AlipayFaceToFaceOrderInfo getAlipayFaceToFaceOrderInfoByTaobaoUserId(Long taobaoUserId) throws Exception {

        AlipayFaceToFaceOrderInfo alipayFaceToFaceOrderInfo=new AlipayFaceToFaceOrderInfo();
        StationAlipayInfoDto stationAlipayInfoDto= stationAlipayInfoService.getStationAlipayInfoByTaobaoUserId(taobaoUserId.toString());
        if(stationAlipayInfoDto!=null){
            AntMerchantExpandOrderQueryRequest request = new AntMerchantExpandOrderQueryRequest();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("order_id",stationAlipayInfoDto.getAlipayOrderId());
            request.setBizContent(jsonObject.toString());
            AntMerchantExpandOrderQueryResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                alipayFaceToFaceOrderInfo.setSmid(response.getIpRoleId().get(0));
                alipayFaceToFaceOrderInfo.setMerchantName(response.getMerchantName());
                alipayFaceToFaceOrderInfo.setApplyTime(response.getApplyTime());
                alipayFaceToFaceOrderInfo.setStatus(response.getStatus());
                alipayFaceToFaceOrderInfo.setExtInfo(response.getExtInfo());
            }
        }
        return alipayFaceToFaceOrderInfo;
    }

    @Override
    public void updateAlipayFaceToFaceOrderInfoByTaobaoUserId(Long taobaoUserId) throws Exception {

        AlipayFaceToFaceOrderInfo alipayFaceToFaceOrderInfo= getAlipayFaceToFaceOrderInfoByTaobaoUserId(taobaoUserId);
        if(alipayFaceToFaceOrderInfo!=null&&StringUtil.isNotBlank(alipayFaceToFaceOrderInfo.getSmid())){
            StationAlipayInfoDto stationAlipayInfoDto= stationAlipayInfoService.getStationAlipayInfoByTaobaoUserId(taobaoUserId.toString());
            stationAlipayInfoDto.setAuditStatus("passed");
            stationAlipayInfoDto.setAuditMemo(alipayFaceToFaceOrderInfo.getExtInfo());
            stationAlipayInfoDto.setGmtModified(new Date());
            stationAlipayInfoDto.setSmid(alipayFaceToFaceOrderInfo.getSmid());
            stationAlipayInfoService.updateStationAlipayInfo(stationAlipayInfoDto);
        }
    }

    private AntMerchantExpandIndirectImageUploadResponse uploadIdcardImgsFace(Long applyId) throws AlipayApiException {

        AttachmentDto idcardImgsFace = attachmentService.getAttachment(applyId,
                AttachmentBizTypeEnum.IDCARD_IMGS_FACE_BIZTYPE);

        AntMerchantExpandIndirectImageUploadRequest request = new AntMerchantExpandIndirectImageUploadRequest();

        byte[] bt = doGetFileFromTfs(idcardImgsFace.getFsId(), idcardImgsFace.getFileType());

        FileItem imageContent = new FileItem("/home/admin/"+idcardImgsFace.getTitle(),bt);
        request.setImageContent(imageContent);
        request.setImageType(idcardImgsFace.getFileType());
        AntMerchantExpandIndirectImageUploadResponse response = alipayClient.execute(request);

        return response;
    }

    private AntMerchantExpandIndirectImageUploadResponse uploadIdcardImgsBack(Long applyId) throws AlipayApiException {

        AttachmentDto idcardImgsBack = attachmentService.getAttachment(applyId,
                AttachmentBizTypeEnum.IDCARD_IMGS_BACK_BIZTYPE);

        AntMerchantExpandIndirectImageUploadRequest request = new AntMerchantExpandIndirectImageUploadRequest();

        byte[] bt = doGetFileFromTfs(idcardImgsBack.getFsId(), idcardImgsBack.getFileType());

        FileItem imageContent = new FileItem("/home/admin/"+idcardImgsBack.getTitle(),bt);
        request.setImageContent(imageContent);
        request.setImageType(idcardImgsBack.getFileType());

        AntMerchantExpandIndirectImageUploadResponse response = alipayClient.execute(request);

        return response;

    }

    private AntMerchantExpandIndirectImageUploadResponse uploadCertImage(CuntaoQualification cuntaoQualification ) throws Exception {

        AntMerchantExpandIndirectImageUploadResponse response=null;
        if(cuntaoQualification!=null){
            String certUrl=DOLWAN_QUAN_IMAGE_URI+cuntaoQualification.getQualiPic();
            AntMerchantExpandIndirectImageUploadRequest request = new AntMerchantExpandIndirectImageUploadRequest();

            byte[]bt=getUrlFileData(certUrl);

            FileItem imageContent = new FileItem("/home/admin/"+cuntaoQualification.getQualiPic(),bt);
            request.setImageContent(imageContent);
            String imageType=imageContent.getMimeType();
            if("image/jpeg".equals(imageType)){
                request.setImageType("jpeg");
            }
            else if("image/gif".equals(imageType)){
                request.setImageType("gif");
            }
            else if("image/png".equals(imageType)){
                request.setImageType("png");
            }
            else if("image/bmp".equals(imageType)){
                request.setImageType("bmp");
            }
            else{
                request.setImageType("application/octet-stream");
            }

            response = alipayClient.execute(request);
        }
        return response;
    }

    private AntMerchantExpandIndirectImageUploadResponse uploadOutDoorImage(String url) throws Exception {

        AntMerchantExpandIndirectImageUploadRequest request = new AntMerchantExpandIndirectImageUploadRequest();

        byte[]bt=getUrlFileData(url);

        FileItem imageContent = new FileItem("/home/admin/outDoorImage",bt);
        request.setImageContent(imageContent);

        request.setImageType("png");

        AntMerchantExpandIndirectImageUploadResponse response  = alipayClient.execute(request);

        return response;
    }

    private AntMerchantExpandIndirectZftCreateResponse createZft(PartnerStationRel instance,PartnerApplyDto apply,ResultDO<BasePaymentAccountDO>paymentAccountDOResultDO,Station station,
                                                                 AccountMoneyDto accountMoneyDto, CuntaoQualification cuntaoQualification,
                                                                 AntMerchantExpandIndirectImageUploadResponse idcardImgsFaceResponse, AntMerchantExpandIndirectImageUploadResponse idcardImgsBackResponse
   ) throws AlipayApiException {
        AntMerchantExpandIndirectZftCreateResponse response=null;
        if(instance!=null&&apply!=null&&accountMoneyDto!=null&&cuntaoQualification!=null){
            AntMerchantExpandIndirectZftCreateRequest request = new AntMerchantExpandIndirectZftCreateRequest();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("external_id","cuntao_"+apply.getTaobaoUserId());
            jsonObject.put("name",cuntaoQualification.getLegalPerson());
            jsonObject.put("alias_name",cuntaoQualification.getCompanyName());
//            if(INDIVIDUAL_BUSINESS.equals(cuntaoQualification.getEnterpriceType().toString())){
//                jsonObject.put("merchant_type","07");
//                jsonObject.put("cert_name",cuntaoQualification.getCompanyName());
//            }
//            else if(ENTERPRISE.equals(cuntaoQualification.getEnterpriceType().toString())){
//                jsonObject.put("merchant_type","01");
//            }
            jsonObject.put("merchant_type","06");
            jsonObject.put("mcc","5722");
            jsonObject.put("cert_no",apply.getIdenNum());
            jsonObject.put("cert_type","100");
           // jsonObject.put("cert_image",certImageResponse.getImageId());
            jsonObject.put("legal_name",cuntaoQualification.getLegalPerson());
            jsonObject.put("legal_cert_no",apply.getIdenNum());
            jsonObject.put("legal_cert_front_image",idcardImgsFaceResponse.getImageId());
            jsonObject.put("legal_cert_back_image",idcardImgsBackResponse.getImageId());

            String[]outDoorImages=new String[1];
            outDoorImages[0]=idcardImgsFaceResponse.getImageId();
            jsonObject.put("out_door_images",outDoorImages);

            AddressInfo addressInfo=new AddressInfo();
            addressInfo.setAddress(station.getAddress());
            addressInfo.setCityCode(station.getCity());
            addressInfo.setDistrictCode(station.getCounty());
            addressInfo.setProvinceCode(station.getProvince());
            addressInfo.setLatitude(station.getLat());
            addressInfo.setLongitude(station.getLng());
            addressInfo.setType("BUSINESS_ADDRESS");

            jsonObject.put("business_address",addressInfo);
            jsonObject.put("service_phone",apply.getPhone());

            ContactInfo contactInfo=new ContactInfo();
            contactInfo.setName(cuntaoQualification.getLegalPerson());
            contactInfo.setType("LEGAL_PERSON");
            contactInfo.setMobile(apply.getPhone());
            jsonObject.put("contact_infos",contactInfo);

            String[]services=new String[1];
            services[0]="当面付";
            jsonObject.put("service",services);
            DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd"); //HH表示24小时制；
            jsonObject.put("sign_time_with_isv",dFormat.format(instance.getOpenDate()));

            if(paymentAccountDOResultDO.isSuccess()&&paymentAccountDOResultDO.getModule()!=null){
                jsonObject.put("alipay_logon_id",paymentAccountDOResultDO.getModule().getOutUser());
                jsonObject.put("binding_alipay_logon_id",paymentAccountDOResultDO.getModule().getOutUser());
            }
            request.setBizContent(jsonObject.toString());
            response = alipayClient.execute(request);
        }


        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

        return response;
    }

    private void saveToFile(String destUrl,String destFileName) {

        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        try {
            url = new URL(destUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            fos = new FileOutputStream(destFileName);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
        } catch (IOException e) {
        } catch (ClassCastException e) {
        } finally {
            try {
                fos.close();
                bis.close();
                httpUrl.disconnect();
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }
        }
    }

    public static byte[] getUrlFileData(String fileUrl) throws Exception
    {
        URL url = new URL(fileUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.connect();
        InputStream cin = httpConn.getInputStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = cin.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        cin.close();
        byte[] fileData = outStream.toByteArray();
        outStream.close();
        return fileData;
    }

    private byte[] doGetFileFromTfs(String fileName, String fileType) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
        String tfsSuffix = null;
        if(fileName != null && fileName.endsWith(".tfsprivate")){
            tfsSuffix = ".tfsprivate";
        }
        boolean flag = tfsManager.fetchFile(fileName, tfsSuffix,byteArrayOutputStream);
        if (flag) {
            return byteArrayOutputStream.toByteArray();
        } else {
            return null;
        }
    }

}
