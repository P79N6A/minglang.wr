package com.taobao.cun.auge.qualification.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.CuntaoInvoiceQualification;
import com.taobao.cun.auge.dal.domain.CuntaoInvoiceQualificationExample;
import com.taobao.cun.auge.dal.mapper.CuntaoInvoiceQualificationMapper;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("invoiceQualificationService")
@HSFProvider(serviceInterface= InvoiceQualificationService.class)
public class InvoiceQualificationServiceImpl implements InvoiceQualificationService{
	private static final Logger logger = LoggerFactory.getLogger(InvoiceQualificationServiceImpl.class);
	@Autowired
	private CuntaoInvoiceQualificationMapper cuntaoInvoiceQualificationMapper;
	
	private static BeanCopier cuntaoQualificationCopier = BeanCopier.create(InvoiceQualification.class, CuntaoInvoiceQualification.class, false);

	private static BeanCopier reverseCuntaoQualificationCopier = BeanCopier.create(CuntaoInvoiceQualification.class, InvoiceQualification.class, false);

	
	@Override
	public boolean saveSettleInvoiceInfo(InvoiceQualification invoiceQualification){
		try {
			BeanValidator.validateWithThrowable(invoiceQualification);
			CuntaoInvoiceQualificationExample example = new  CuntaoInvoiceQualificationExample();
			example.createCriteria().andIsDeletedEqualTo("n").andTaobaoUserIdEqualTo(invoiceQualification.getTaobaoUserId()).andInvoiceFileTypeEqualTo(invoiceQualification.getInvoiceFileType());
			CuntaoInvoiceQualification cuntaoInvoiceQualification = ResultUtils.selectOne(cuntaoInvoiceQualificationMapper.selectByExample(example));
			if(cuntaoInvoiceQualification == null){
				cuntaoInvoiceQualification = new CuntaoInvoiceQualification();
				DomainUtils.beforeInsert(cuntaoInvoiceQualification, "system");
				cuntaoInvoiceQualification.setUploadTime(new Date());
				cuntaoQualificationCopier.copy(invoiceQualification, cuntaoInvoiceQualification, null);
				cuntaoInvoiceQualificationMapper.insertSelective(cuntaoInvoiceQualification);
			}else{
				DomainUtils.beforeUpdate(cuntaoInvoiceQualification, "system");
				cuntaoQualificationCopier.copy(invoiceQualification, cuntaoInvoiceQualification, null);
				cuntaoInvoiceQualificationMapper.updateByPrimaryKeySelective(cuntaoInvoiceQualification);
			}
			return true;
		} catch (Exception e) {
			logger.error("saveSettleInvoiceInfo error!taobaoUserId["+invoiceQualification.getTaobaoUserId()+"]",e);
			return false;
		}
		
	}

	@Override
	public List<InvoiceQualification> queryInvoiceQualification(Long taobaoUserId) {
		Assert.notNull(taobaoUserId);
		CuntaoInvoiceQualificationExample example = new  CuntaoInvoiceQualificationExample();
		example.createCriteria().andIsDeletedEqualTo("n").andTaobaoUserIdEqualTo(taobaoUserId);
		List<CuntaoInvoiceQualification> cuntaoInvoiceQualifications = cuntaoInvoiceQualificationMapper.selectByExample(example);
		if(cuntaoInvoiceQualifications != null){
			return cuntaoInvoiceQualifications.stream().map(cuntaoInvoiceQualification -> {
				InvoiceQualification invoiceQualification = new InvoiceQualification();
				reverseCuntaoQualificationCopier.copy(cuntaoInvoiceQualification, invoiceQualification, null);
				return invoiceQualification;
			}).collect(Collectors.toList());
			
		}
		return  null;
	}

	
}
