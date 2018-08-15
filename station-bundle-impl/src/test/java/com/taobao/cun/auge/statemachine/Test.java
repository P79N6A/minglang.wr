package com.taobao.cun.auge.statemachine;

import java.io.IOException;
import java.net.URL;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;

public class Test {

	public static void main(String[] args) throws IOException, ModelException, XMLStreamException {
		// TODO Auto-generated method stub
		String s = "insert into `service_ability_person_info`( gmt_create,  gmt_modified,  creator,  modifier,  is_deleted,  mobile,  name,  taobao_user_id,  taobao_nick,  post,  sad_id,  install_ability_category)"
				+ " values(now(), now(), '1714147191', 'system', 'n', '$1', '$2', $3, '$4', 'SONGHUO', $5, '');";
		
		String[][] ss = new String[][]{{"18196727979","刘洪发","2858394170","刘洪发8601","605"},
			{"18516763498","李阿飞","2114542749","油菜籽油菜花66","604"},
			{"18305586199","张斐斐","410861941","feifeiabc2626","603"},
			{"18156856556","李锐","73114169","594lirui","585"},
			{"15178142976","徐君君","2851187895","君君君君悦1521","583"},
			{"18756880023","赵洪武","3326185073","t_1503884691752_0472","602"},
			{"18267302966","张慧敏","823506294","tb_7280996","600"},
			{"18325978081","叶兰玉","3335112330","谭棚谭亮","601"},
			{"18605584588","林洪宝","856165244","diveline","599"},
			{"15056855113","王建侠","3127144815","t_1496274463869_0913","592"},
			{"15391706976","王园园","916247028","子恒1110","597"},
			{"15156664209","韩素侠","3124859032","小小的短腿齐","598"},
			{"15655808539","马荣耀","2535920913","王者15845679","587"},
			{"17355871885","姚萍","2140223006","姚萍1888","588"},
			{"15955858018","胡玲","674395682","生活的朦胧","594"},
			{"18949056114","田明","329377685","mingm00007","596"},
			{"15555809210","卢震","674010711","tb6999155","593"},
			{"15655810013","陈媛媛","911508671","chenyuanyuan1952","589"},
			{"18855816535","杨玲","3028794231","t_1492412874532_0888","595"},
			{"18655802300","张震","2246235727","zhangzhencpicah","590"},
			{"13955892092","郭静静","2076865051","miss静静666","586"},
			{"13470800038","张松","34946110","zhangsong888","396"},
			{"18258736986","肖庆红","3129530076","t_1486688678097_0265","584"}};

			for(String[] s1 : ss){
				System.out.println(s.replaceAll("\\$1", s1[0]).replaceAll("\\$2", s1[1]).replaceAll("\\$3", s1[2]).replaceAll("\\$4", s1[3]).replaceAll("\\$5", s1[4]));
			}
	}

}
