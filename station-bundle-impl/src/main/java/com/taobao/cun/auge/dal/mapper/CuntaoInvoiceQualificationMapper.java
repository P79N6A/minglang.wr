package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.dal.domain.CuntaoInvoiceQualification;
import com.taobao.cun.auge.dal.domain.CuntaoInvoiceQualificationExample;
import org.apache.ibatis.annotations.Param;

public interface CuntaoInvoiceQualificationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_invoice_qualification
     *
     * @mbggenerated
     */
    int countByExample(CuntaoInvoiceQualificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_invoice_qualification
     *
     * @mbggenerated
     */
    int deleteByExample(CuntaoInvoiceQualificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_invoice_qualification
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_invoice_qualification
     *
     * @mbggenerated
     */
    int insert(CuntaoInvoiceQualification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_invoice_qualification
     *
     * @mbggenerated
     */
    int insertSelective(CuntaoInvoiceQualification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_invoice_qualification
     *
     * @mbggenerated
     */
    List<CuntaoInvoiceQualification> selectByExample(CuntaoInvoiceQualificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_invoice_qualification
     *
     * @mbggenerated
     */
    CuntaoInvoiceQualification selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_invoice_qualification
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CuntaoInvoiceQualification record, @Param("example") CuntaoInvoiceQualificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_invoice_qualification
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CuntaoInvoiceQualification record, @Param("example") CuntaoInvoiceQualificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_invoice_qualification
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CuntaoInvoiceQualification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_invoice_qualification
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CuntaoInvoiceQualification record);
}