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
		SCXMLReader.Configuration config =  new SCXMLReader.Configuration(null, null, null);
		URL url = Thread.currentThread().getContextClassLoader().getResource("statemachine/statemachine-tp.xml");
		 SCXML scxml = SCXMLReader.read(url,config);
		 System.out.println(scxml);

	}

}
