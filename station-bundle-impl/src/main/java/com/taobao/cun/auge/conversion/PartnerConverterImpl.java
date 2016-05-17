package com.taobao.cun.auge.conversion;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.station.dto.PartnerDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2016-05-16T13:40:03+0800",
    comments = "version: 1.0.0.Final, compiler: javac, environment: Java 1.8.0_91 (Oracle Corporation)"
)
public class PartnerConverterImpl implements PartnerConverter {

    @Override
    public PartnerDto toPartnerDto(Partner partner) {
        if ( partner == null ) {
            return null;
        }

        PartnerDto partnerDto = new PartnerDto();

        partnerDto.setId( partner.getId() );
        partnerDto.setName( partner.getName() );
        partnerDto.setAlipayAccount( partner.getAlipayAccount() );
        partnerDto.setTaobaoUserId( partner.getTaobaoUserId() );
        partnerDto.setTaobaoNick( partner.getTaobaoNick() );
        partnerDto.setIdenNum( partner.getIdenNum() );
        partnerDto.setMobile( partner.getMobile() );
        partnerDto.setEmail( partner.getEmail() );
        partnerDto.setBusinessType( partner.getBusinessType() );
        partnerDto.setDescription( partner.getDescription() );
        partnerDto.setState( partner.getState() );

        return partnerDto;
    }

    @Override
    public Partner toParnter(PartnerDto parnterDto) {
        if ( parnterDto == null ) {
            return null;
        }

        Partner partner = new Partner();

        partner.setId( parnterDto.getId() );
        partner.setName( parnterDto.getName() );
        partner.setAlipayAccount( parnterDto.getAlipayAccount() );
        partner.setTaobaoUserId( parnterDto.getTaobaoUserId() );
        partner.setTaobaoNick( parnterDto.getTaobaoNick() );
        partner.setIdenNum( parnterDto.getIdenNum() );
        partner.setMobile( parnterDto.getMobile() );
        partner.setEmail( parnterDto.getEmail() );
        partner.setBusinessType( parnterDto.getBusinessType() );
        partner.setDescription( parnterDto.getDescription() );
        partner.setState( parnterDto.getState() );

        return partner;
    }

    @Override
    public List<PartnerDto> toPartnerDtos(List<Partner> partner) {
        if ( partner == null ) {
            return null;
        }

        List<PartnerDto> list = new ArrayList<PartnerDto>();
        for ( Partner partner_ : partner ) {
            list.add( toPartnerDto( partner_ ) );
        }

        return list;
    }

    @Override
    public List<Partner> toPartners(List<PartnerDto> partner) {
        if ( partner == null ) {
            return null;
        }

        List<Partner> list = new ArrayList<Partner>();
        for ( PartnerDto partnerDto : partner ) {
            list.add( toParnter( partnerDto ) );
        }

        return list;
    }
}
