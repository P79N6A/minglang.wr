package com.taobao.cun.auge.station.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;

public class Down1 {
	public static void main(String[] args)throws Exception {
		//HttpURLConnection connection = null;  
		try {
			trustAllHttpsCertificates();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		
		HostnameVerifier hv = new HostnameVerifier() {  
	        public boolean verify(String urlHostName, SSLSession session) {  
	            System.out.println("Warning: URL Host: " + urlHostName + " vs. "  
	                               + session.getPeerHost());  
	            return true;  
	        }  
	    };  
		HttpsURLConnection.setDefaultHostnameVerifier(hv);  
		
		try {
		
	    final Map<String,Integer> map = Maps.newHashMap();
	    IOUtils.readLines(new FileInputStream("/Users/quanzhu.wangqz/zhangsan.txt"), "UTF-8").forEach(line ->{
	    	String filename = null;
	    	String name = line.split("\\|")[0];
	    	String fileUrl = line.split("\\|")[1];
	    	Integer index = map.get(name);
	    	if(index != null){
	    		index = index+1;
	    		map.put(name,index);
	    		 filename = name+index;
	    	}else{
	    		map.put(name, 1);
	    		filename = name;
	    	}
	    	String type =fileUrl.replaceAll(".*&fileType=" ,"").replaceAll("&title=.*", "");
	    	System.out.println(filename+"."+type);
	    	//name = name+idx;
	    	//idx = idx+1;
	    	 FileOutputStream out = null;
			try {
				out = new FileOutputStream("/Users/quanzhu.wangqz/baoxian/"+filename+"."+type);
				URL url = new URL(fileUrl);
		 		HttpURLConnection connection = (HttpURLConnection)  url.openConnection();
				 connection.setRequestProperty("referer", "https://cun.taobao.org");
				connection.setRequestProperty("cookie", "cookie: cna=IM1qE0JHKE8CAXkAHcHNLkSX; JSESSIONID=FF6Y8XP0P2-PG0XK38WLVU732UVPWZL2-316AAZJJ-VF; __cuntaoworkmob_=ruQkjksa+shL5I0iRN9xZ7QQI0CARGU4C5ySfD6egHSpSlwTaPV0yHmI01HRCkHc0okitYi0TbzvYR/aascxbHeho0mACHT5IBGVoGbc78hecZkMULBUSQ==; SSO_LANG_V2=ZH-CN; cuntaoBopsGroup_USER_ORG=938AA253C68CB9B45B794C764C95C61C; cuntaoBopsGroup_ROLE_NAME=BDE258838844E55F5FFD175BD9EEAF79; cuntaoBopsGroup_USER_ORG_PATH=40632599BBA7D5B20F8A8B9D202BC337; cuntaoBopsGroup_USER_ORG_NAME=6CC35EB133B4A288B0685D1D976CC602; cuntaoBopsGroup_U_R_ID=73D7D2E939356B6635673A2A2A934C00; cuntaoBopsGroup_SSO_TOKEN=9FFFA5F714317A2F0FAD0B818C0CECD3A98A5FEF771F21C4130748707F90E725100CCA4524D6E73CE1ABB660CA29BEBD; cuntaoBopsGroup_LAST_HEART_BEAT_TIME=6B33AA1EC47726CFFB79F1798F10D8CD; cuntaoBopsGroup_USER_COOKIE=2D02F26121D0B5B2FE3E17A3B5F6111427AA19D5B3377598AB20534A2EB86191251F21036D63DC96CD5F6A7E6F6EF469A7A8E8718902B35C815AF6C0EF5146CE0F86AF1DAB45D168F67AD655EF828136E5E24551A1C6B18E2901DE5B7AAD25D629B8B78F9BB153E4109E5F0DBF24F3193994EDCA3F01DE5E037676B3856E95CB614AECB39160718EE03559D33254F6B75F63628D0F6814C2CE566538C467A3020FFD0A4DC310BD9DFAF3A123C47636AF5CE91DA754C2132DE012EA7580A2C065FA0A000695753BDEA5DFF2F87AE772F3ABB29979A82BB947555B70FA4C9E06BC62263E8F8FF15DDE0A63764C84311C217223684A685466B938F4E48A585AFBDE6E87388A2C50612DAFE72A4E423A0DA0CC5347EF953E3F8628745D1D8C500A9E4DFB1FC910CEE8D23B3D2B1C8502C5CB3ACD8805ABB2001D401DDBDCBED0345359E355C51915D8F522D3C8F517E50CB7F96B6C7BF4F35736F53EF029C88D2EE12BAC62D14E591A7E0CC22DDF9391E72E6738AB95DCC8B46F192C741119F6CFD9CE4879DC2381008C95C0C9BF0C36F2BBF8BCF0715F9D783562931C51B763AE96; SSO_EMPID_HASH_V2=1111f34c1621b9892e29e03e37d562a7; tmp10=X6Q5%2BzNdZZDqS3Rq3dMX82UaDPNI6qx8p4qSoZYhp4P1nagZohJvtAmwUMyudymkUEKonC%2FDGtHphMH%2BjhBzCiPTR7sFURVtMmGdJuHZn0BvJgZcb5bryVzhzW9HKXMUU5jA0sBbcBWo%2Bl1dnRqeoW4XJoeAO%2BKCDeIa4ADc3w5ZBWed8KRFIReLyxz3uYS5; cuntaoBopsGroup_SSO_TOKEN_V2=5678B3645CD41D156C58D3ADBBFBE786578EFE3A65F52CF697DA5D6BB8C4BEB5551A176A6E84F395558D74EFD2377F3AA4442E5633ADB343A49541EB06429C6D; isg=BHBwrLhO37tX94KeVzNWsPn-QTjcnBQdEE_oemrBPEueJRDPEskkk8YXeW3gtQzb");
		 	    byte[] bytes = IOUtils.toByteArray(connection.getInputStream());
		 	   IOUtils.write(bytes, out);
		 	   Thread.sleep(500);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	 	
	 	   
	    });;
	    //IOUtils.write(bytes, out);
	   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
      
    private static void trustAllHttpsCertificates() throws Exception {  
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];  
        javax.net.ssl.TrustManager tm = new miTM();  
        trustAllCerts[0] = tm;  
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext  
                .getInstance("SSL");  
        sc.init(null, trustAllCerts, null);  
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc  
                .getSocketFactory());  
    }  
  
    static class miTM implements javax.net.ssl.TrustManager,  
            javax.net.ssl.X509TrustManager {  
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
            return null;  
        }  
  
        public boolean isServerTrusted(  
                java.security.cert.X509Certificate[] certs) {  
            return true;  
        }  
  
        public boolean isClientTrusted(  
                java.security.cert.X509Certificate[] certs) {  
            return true;  
        }  
  
        public void checkServerTrusted(  
                java.security.cert.X509Certificate[] certs, String authType)  
                throws java.security.cert.CertificateException {  
            return;  
        }  
  
        public void checkClientTrusted(  
                java.security.cert.X509Certificate[] certs, String authType)  
                throws java.security.cert.CertificateException {  
            return;  
        }  }
}

