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
				connection.setRequestProperty("cookie", "SSO_LANG_V2=ZH-CN; JSESSIONID=FFYJG4OV-31RZUIJ8T4TLW9EPNVLT1-HVXV5ONJ-56; cna=2DJYFAghAAkCAXkAHcMFJ++O; __cuntaoworkmob_=ruQkjksa+shL5I0iRN9xZ7QQI0CARGU4C5ySfD6egHTe3BV2hjGZaNzOVJqrCGV7J6KGcbH2DHOBeaonsehRw3eho0mACHT5IBGVoGbc78hecZkMULBUSQ==; cuntaoBopsGroup_USER_COOKIE=2D02F26121D0B5B2FE3E17A3B5F6111427AA19D5B3377598AB20534A2EB86191251F21036D63DC96CD5F6A7E6F6EF469A7A8E8718902B35C815AF6C0EF5146CE0F86AF1DAB45D168F67AD655EF828136E5E24551A1C6B18E2901DE5B7AAD25D629B8B78F9BB153E4109E5F0DBF24F3193994EDCA3F01DE5E037676B3856E95CBA6CF0905F5D5B0BEA1AE0C5A6B09283EAFE6EB9F916203148B748F473FE989145AFE43D916EA6342C74508B54938F8435F384F645F76BBDA0BCCEB2E5E276F810C94528E8CE96005BDD97A5D3DB03484D244B24A5BF21E86708F896282CD74B80EF1AE192247301DB90889A26599F4A548921D54B5B83C607B171F2B7FC6A23D18304CDC3D33035DE7CEA4B922F6B49AA5A5AE2D80321A19A6E9D158B6684098D37415985844C31B17DACC4A9EE61F1160F3F6324A27D4A7093620CC39B69DE045D5F5CDF49D9A62C4E5DB7C995AF724EC464604D15EA928A87A489706E29D20EA83E5807DC39F058F154976E4BC55C529B8B78F9BB153E4109E5F0DBF24F3193994EDCA3F01DE5E037676B3856E95CB222B1681A01601B760C7B7DA89D57169CAE7767CE78F9FDA677061A775AA05E9B19AA2BECBCF2BE489A469803F4013B1; cuntaoBopsGroup_USER_ORG=938AA253C68CB9B45B794C764C95C61C; cuntaoBopsGroup_ROLE_NAME=BDE258838844E55F5FFD175BD9EEAF79; cuntaoBopsGroup_USER_ORG_PATH=40632599BBA7D5B20F8A8B9D202BC337; cuntaoBopsGroup_USER_ORG_NAME=6CC35EB133B4A288B0685D1D976CC602; cuntaoBopsGroup_U_R_ID=73D7D2E939356B6635673A2A2A934C00; SSO_EMPID_HASH_V2=fe8e1740278889f76fd88b14346c527a; tmp10=X6Q5%2BzNdZZDqS3Rq3dMX8%2FTXxiqbrpiObiXv9L5RTXeu%2FnvknUCC4hqmDqItdOiqGqvaiXgjHpN2WGykeNDviAk4BpYqtLDq0ghQZXyxzPIw65QQE2Ym7eCeneDN%2BrWgnJcG6jLK3lIuAjIgUbXfIg%3D%3D; cuntaoBopsGroup_SSO_TOKEN_V2=D22F9A303BC975567A2FCFA091D4FD9A774233BD55AAE7815D8B12229C232A92FD5E0F381EB30E386BDAE734F36DBF36155DBF7E2B2C2314F792279DCD0D8DBD; isg=BNPTDp9ri9ZkXUHHOKKlqa7fYlHjrVcKqJWnbIXwBPIpBPKmDlhbmoEWOjTPpL9C; l=aB95WEWGysceQlbBEMa4AVhgom8vEiBP_1SC1MazVin767a92jq70AtoWMzOE_kOJi7gP3vCoUEw.");
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

