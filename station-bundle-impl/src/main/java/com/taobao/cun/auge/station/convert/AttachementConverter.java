package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.List;

import com.taobao.cun.auge.dal.domain.Attachement;
import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementTypeIdEnum;

/**
 * 附件转换
 * @author quanzhu.wangqz
 *
 */
public class AttachementConverter {

	public static AttachementDto toAttachementDto(Attachement attachement) {
		if (attachement == null) {
			return null;
		}

		AttachementDto attachementDto = new AttachementDto();

		attachementDto.setId(attachement.getId());
		attachementDto.setAttachementTypeId(AttachementTypeIdEnum.valueof(attachement.getAttachementTypeId()));
		attachementDto.setBizType(AttachementBizTypeEnum.valueof(attachement.getBizType()));
		attachementDto.setDescription(attachement.getDescription());
		attachementDto.setFileType(attachement.getFileType());
		attachementDto.setFsId(attachement.getFsId());
		attachementDto.setObjectId(attachement.getObjectId());
		attachementDto.setTitle(attachement.getTitle());

		return attachementDto;
	}

	public static Attachement toAttachement(AttachementDto attachementDto) {
		if (attachementDto == null) {
			return null;
		}
		Attachement attachement = new Attachement();
		attachement.setAttachementTypeId(attachementDto.getAttachementTypeId().getCode());
		attachement.setBizType(attachementDto.getBizType().getCode());
		attachement.setDescription(attachementDto.getDescription());
		attachement.setFileType(attachementDto.getFileType());
		attachement.setFsId(attachementDto.getFsId());
		attachement.setObjectId(attachementDto.getObjectId());
		attachement.setTitle(attachementDto.getTitle());
		return attachement;
	}

	public static List<AttachementDto> toAttachementDtos(List<Attachement> attachements) {
		if (attachements == null) {
			return null;
		}

		List<AttachementDto> list = new ArrayList<AttachementDto>();
		for (Attachement attachement : attachements) {
			list.add(toAttachementDto(attachement));
		}

		return list;
	}

	public static List<Attachement> toAttachements(List<AttachementDto> attachementDtos) {
		if (attachementDtos == null) {
			return null;
		}

		List<Attachement> list = new ArrayList<Attachement>();
		for (AttachementDto AttachementDto : attachementDtos) {
			list.add(toAttachement(AttachementDto));
		}

		return list;
	}
}
