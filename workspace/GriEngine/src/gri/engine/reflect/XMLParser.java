package gri.engine.reflect;


import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import gri.engine.util.Constant;

public class XMLParser {

	public XMLParser() {
		// TODO Auto-generated constructor stub
	}
	public static Map<String, String> getMapFromXML() {
		Map<String,String> methodMap=new HashMap<String,String>();
		String filePath=Constant.reflect_file_path;
//		String filePath="/methodReflect.xml";
		SAXReader reader=new SAXReader();
		String sourcePath=null;
		String destPath=null;
		String methodClass=null;
		String methodName=null;
		try {
			Document document=reader.read(new File(filePath));
			Element methodCaller=document.getRootElement();
			Iterator<Element> methodCallerIt=methodCaller.elementIterator();
			for(;methodCallerIt.hasNext();) {
				Element element=methodCallerIt.next();
				Iterator<Element> methodsIt=element.elementIterator();
			    for(;methodsIt.hasNext();) {
			    	Element child=(Element)methodsIt.next();
			    	String nodeName=child.getName();
			    	if(nodeName.equals("sourcePath")) {
			    		sourcePath=child.getStringValue();
			    	}else if(nodeName.equals("destPath")) {
			    		destPath=child.getStringValue();
			    	}else if(nodeName.equals("methodClass")) {
			    		methodClass=child.getStringValue();
			    	}else if(nodeName.equals("methodName")) {
			    		methodName=child.getStringValue();
			    	}
			    }
			    methodMap.put(sourcePath+"!!!"+destPath, methodClass+"!!!"+methodName);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return methodMap;
	}
}
