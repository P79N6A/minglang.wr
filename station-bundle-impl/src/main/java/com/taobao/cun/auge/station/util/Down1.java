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
			@Override
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
				connection.setRequestProperty("cookie", "cna=2DJYFAghAAkCAXkAHcMFJ++O; l=aB95WEWGysceQEsBDMa2wXl99m8vEJ5P0wAq1Mw_Iin767hK2jq70rtx-OzIRVAtxq4O_0m_505j0; SSO_LANG_V2=ZH-CN; JSESSIONID=VFYJCX0V-JOK27WTPZINUSDOGTV9O1-NVFWD1SJ-N4; _ga=GA1.2.267150292.1550731795; cuntaoBopsGroup_USER_COOKIE=2D02F26121D0B5B2FE3E17A3B5F6111427AA19D5B3377598AB20534A2EB86191251F21036D63DC96CD5F6A7E6F6EF469A7A8E8718902B35C815AF6C0EF5146CE0F86AF1DAB45D168F67AD655EF828136E5E24551A1C6B18E2901DE5B7AAD25D629B8B78F9BB153E4109E5F0DBF24F3193994EDCA3F01DE5E037676B3856E95CB1E9E0695F07CB63A06CC86CA3EC185AE42A33274C8E08FC482A799E37340EE33B37C47AF858727633A43C263D229698D5F384F645F76BBDA0BCCEB2E5E276F819F3DE02F8139940B9F6B7E458D0A37AC5D06FA6CD7D276077C94578F47AAE226CDDE22645CA3AA1C4BFAE5E190B5D42A0FFD0A4DC310BD9DFAF3A123C47636AF5CE91DA754C2132DE012EA7580A2C065FA0A000695753BDEA5DFF2F87AE772F3ABB29979A82BB947555B70FA4C9E06BC62263E8F8FF15DDE0A63764C84311C217223684A685466B938F4E48A585AFBDE6E87388A2C50612DAFE72A4E423A0DA0CC5347EF953E3F8628745D1D8C500A9E4DFB1FC910CEE8D23B3D2B1C8502C5CB3ACD8805ABB2001D401DDBDCBED0345359E355C51915D8F522D3C8F517E50CB7F96B6C7BF4F35736F53EF029C88D2EE12BAC62D14E591A7E0CC22DDF9391E72E6738AB95DCC8B46F192C741119F6CFD9CE4879DC2381008C95C0C9BF0C36F2BBF8BCF0715F9D783562931C51B763AE96; cuntaoBopsGroup_USER_ORG=938AA253C68CB9B45B794C764C95C61C; cuntaoBopsGroup_ROLE_NAME=BDE258838844E55F5FFD175BD9EEAF79; cuntaoBopsGroup_USER_ORG_PATH=40632599BBA7D5B20F8A8B9D202BC337; cuntaoBopsGroup_USER_ORG_NAME=6CC35EB133B4A288B0685D1D976CC602; cuntaoBopsGroup_U_R_ID=73D7D2E939356B6635673A2A2A934C00; cuntaoBopsGroup_SSO_TOKEN_V2=16791CBA682107A9743E5B4C77AABD2FDD98FA42BDFFDC25547BA0D37705E3856BCCC990D3650BE2DD514A444E0AEB30EF0B98121CE38FA99CA63B3EBCAA38CE; SSO_EMPID_HASH_V2=52efcec036dba7019af6165d243702d8; tmp10=X6Q5%2BzNdZZDqS3Rq3dMX8zBQu0XagL5CAiR6vhwtVK1lGqhTid5SqsUMRrxxIZhcqldsmqlgyzSu%2FvxUCLc3%2BYrD2Bg%2BgEtJJH5dshuH9KuzNJaqiHxj5bqkI%2BFSzSlcNNUp3cjWRdQXyA65%2B58zLQ%3D%3D; isg=BIaGb-BUJu07QfQc7Z0YkgMI13oWwvqMFaLyF3CvcqmEcyaN2HcasWwFS-8aW8K5");
		 	    byte[] bytes = IOUtils.toByteArray(connection.getInputStream());
		 	   IOUtils.write(bytes, out);
		 	   Thread.sleep(500);
			} catch (Exception e) {
				System.out.println(line);
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

		@Override
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

        @Override
        public void checkServerTrusted(  
                java.security.cert.X509Certificate[] certs, String authType)  
                throws java.security.cert.CertificateException {  
            return;  
        }  

        @Override
        public void checkClientTrusted(  
                java.security.cert.X509Certificate[] certs, String authType)  
                throws java.security.cert.CertificateException {  
            return;  
        }  }
}

