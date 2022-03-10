package www.global;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlTools 
{
	public static String address = "19.3.22.61";
	public static int port = 9001;
	public static int headLenght = 8;
	public static boolean isInclude = false;
	public static String encode = "UTF-8";
	public static boolean isNewPath = false;//是否输入新路径
	public static String fileName = "1269-req.xml";
	public static String serviceName = "";//jolt协议的服务名
	
	
	public static void readPropertiesXml()
	{
		SAXReader reader = new SAXReader();
		System.out.println(new File("property.xml").getAbsolutePath());
		File file = new File("property.xml");
		Document document;
		try 
		{
			document = reader.read(file);
		} 
		catch (DocumentException e)
		{
			e.printStackTrace();
			System.out.println("读文件时出错");
			return;
		}
		
		Element root = document.getRootElement();
		address = getValue(root,"address");
		port = Integer.valueOf(getValue(root,"port"));
		headLenght = Integer.valueOf(getValue(root, "headlength"));
		
		String include = getValue(root, "isInclude");
		if("true".equals(include))
		{		isInclude = true;		}
		else{ isInclude = false;}
		
		encode = getValue(root, "encode");
		fileName = getValue(root, "filename");
		
		String newPath = getValue(root, "isNewPath");
		if("true".equals(newPath))  isNewPath = true;
		else isNewPath = false;
		
		serviceName = getValue(root, "servicename");
	}
	
	private static String getValue(Element root, String name)
	{
		String value = null;
		Element element = root.element(name);
		value = (null == element.attributeValue("value")?"": element.attributeValue("value"));
		return value;
	}
	
}
