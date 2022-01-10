package com.pgmate.lib.util.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.pgmate.lib.util.lang.CommonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class XmlUtil {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.util.xml.XmlUtil.class );

	public XmlUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static String toXml(Object xml){
		return toXml(xml, true, "");
       
	}
	
	public static String toXml(Object xml,boolean pretty,String charSet){
		StringWriter str = new StringWriter();
        try {
            JAXBContext context = JAXBContext.newInstance(xml.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, pretty);
            if(CommonUtil.isNullOrSpace(charSet)){
            	m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            }else{
            	m.setProperty(Marshaller.JAXB_ENCODING, charSet);
            }

            m.marshal(xml,str);
        } catch (Exception e) {
        	logger.debug("JAXBException : {}",CommonUtil.getExceptionMessage(e));
        }
        return str.toString();
       
	}
	
	
	public static Object fromXml(Object xmlObject,String xml ){
		
		try {
			 StringReader reader = new StringReader(xml);
			 JAXBContext context = JAXBContext.newInstance(xmlObject.getClass());
			 Unmarshaller un = context.createUnmarshaller();
			 xmlObject = un.unmarshal(reader);
          
		}catch (JAXBException e) {
      	 	logger.debug("JAXBException : {}",CommonUtil.getExceptionMessage(e));
		}
		return xmlObject;
	}
	
	

}
