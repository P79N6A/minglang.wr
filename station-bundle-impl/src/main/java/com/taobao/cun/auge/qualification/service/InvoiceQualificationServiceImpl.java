package com.taobao.cun.auge.qualification.service;

import java.util.Date;

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
	public InvoiceQualification queryInvoiceQualification(Long taobaoUserId) {
		Assert.notNull(taobaoUserId);
		CuntaoInvoiceQualificationExample example = new  CuntaoInvoiceQualificationExample();
		example.createCriteria().andIsDeletedEqualTo("n").andTaobaoUserIdEqualTo(taobaoUserId);
		CuntaoInvoiceQualification cuntaoInvoiceQualification = ResultUtils.selectOne(cuntaoInvoiceQualificationMapper.selectByExample(example));
		if(cuntaoInvoiceQualification != null){
			InvoiceQualification invoiceQualification = new InvoiceQualification();
			reverseCuntaoQualificationCopier.copy(cuntaoInvoiceQualification, invoiceQualification, null);
			return invoiceQualification;
		}
		return  null;
	}

	
}
