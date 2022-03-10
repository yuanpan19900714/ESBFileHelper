package www.global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.entry.Case;
import com.entry.Configs;
import com.entry.Switch;

public class FileCheckTools {
	private final static String FILENAME = "checkResult.txt";

	/**
	 * 检查服务识别文件
	 * 
	 * @param flag
	 *            是否重新生成服务识别文件（去重排序）
	 */
	@SuppressWarnings("unchecked")
	public static void checkServiceIdentify(boolean flag) {
		String serviceIdentify = Configs.inConf.SERVICE_INDENTIFY;
		FileInputStream fis = null;
		SAXReader reader = new SAXReader();
		Document document;
		try {
			fis = new FileInputStream(serviceIdentify);
			document = reader.read(fis);
		} catch (DocumentException | FileNotFoundException e) {
			e.printStackTrace();
			FileTools.write(FILENAME, "读取服务识别文件出错！请检查文件[" + serviceIdentify + "]\n", true);
			return;
		}
		FileTools.write(FILENAME, "检查服务识别文件开始！\n", true);
		Element root = document.getRootElement();
		List<Element> channels = root.elements("channel");
		String[] channelArray = checkRepeated(channels, "channel", flag);
		System.out.println("channels:" + Arrays.toString(channelArray));
		Map<String, List<Switch>> map = new HashMap<String, List<Switch>>();
		for (Element channel : channels) {
			String channelId = channel.attributeValue("id");
			List<Element> switchCases = channel.elements();
			String[] serviceArray = {};
			List<Element> services = new ArrayList<Element>();
			List<Switch> switchList = new ArrayList<Switch>();
			for (Element switchCase : switchCases) {
				List<Case> caseList = new ArrayList<Case>();
				String mode = switchCase.attributeValue("mode");
				String expression = switchCase.attributeValue("expression");
				String encode = switchCase.attributeValue("encode");
				String switchKey = channelId + "_" + mode + "_" + expression;
				checkSwitch(switchCase, channelId);
				List<Element> cases = switchCase.elements("case");
				for (Element ele : cases) {
					String value = ele.attributeValue("value");
					String isThrough = ele.attributeValue("isThrough");
					String isConvert = ele.attributeValue("isConvert");
					String serviceId = ele.getText();
					caseList.add(new Case(switchKey, value, isThrough, isConvert, serviceId));
				}
				switchList.add(new Switch(channelId, mode, expression, encode, caseList));
				services.addAll(cases);
			}
			String[] repeatedServices = {};
			for (Element service : services) {
				String serviceId = service.getText();
				if (Arrays.asList(serviceArray).contains(serviceId)) {
					repeatedServices = Arrays.copyOf(repeatedServices, repeatedServices.length + 1);
					repeatedServices[repeatedServices.length - 1] = serviceId;
				} else {
					serviceArray = Arrays.copyOf(serviceArray, serviceArray.length + 1);
					serviceArray[serviceArray.length - 1] = serviceId;
				}
			}
			FileTools.write(FILENAME, "渠道[" + channelId + "]共有交易" + services.size() + "个\n", true);
			if (0 == repeatedServices.length) {
				FileTools.write(FILENAME, "其中没有重复交易！\n\n", true);
			} else {
				FileTools.write(FILENAME, "其中重复交易为:" + Arrays.toString(repeatedServices) + "\n\n",
						true);
			}
			map.put(channelId, switchList);
		}
		if (flag) {
			reWriteServiceIdentify(channelArray, map);
		}
	}

	/**
	 * 重新生成服务识别文件
	 * 
	 * @param channelArray
	 * @param map
	 */
	private static void reWriteServiceIdentify(String[] channelArray, Map<String, List<Switch>> map) {
		String serviceNew = "serviceIdentify_new.xml";
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("channels");
		for (String channelId : channelArray) {
			Element channelEle = root.addElement("channel");
			channelEle.addAttribute("id", channelId);
			channelEle.addAttribute("type", "dynamic");
			List<Switch> switchList = map.get(channelId);
			Collections.sort(switchList, new Comparator<Switch>() {
				@Override
				public int compare(Switch o1, Switch o2) {
					String mode1 = o1.getMode();
					String exp1 = o1.getExpression();
					String mode2 = o2.getMode();
					String exp2 = o2.getExpression();
					return (mode1 + "_" + exp1).compareToIgnoreCase(mode2 + "_" + exp2);
				}
			});
			for (Switch switchEntry : switchList) {
				if (switchEntry.getChannelId().equals(channelId)) {
					Element switchEle = channelEle.addElement("switch");
					switchEle.addAttribute("mode", switchEntry.getMode());
					switchEle.addAttribute("expression", switchEntry.getExpression());
					switchEle.addAttribute("encode", switchEntry.getEncode());
					List<Case> caseList = switchEntry.getCaseList();
					Collections.sort(caseList, new Comparator<Case>() {
						@Override
						public int compare(Case o1, Case o2) {
							String text1 = o1.getText();
							String text2 = o2.getText();
							return text1.compareTo(text2);
						}
					});
					String switchKey = channelId + "_" + switchEntry.getMode() + "_"
							+ switchEntry.getExpression();
					for (Case caseEntry : caseList) {
						if (caseEntry.getSwitchKey().equals(switchKey)) {
							Element caseEle = switchEle.addElement("case");
							caseEle.addAttribute("value", caseEntry.getValue());
							caseEle.addAttribute("isThrough", caseEntry.getIsThrough());
							caseEle.addAttribute("isConvert", caseEntry.getIsConvert());
							caseEle.addText(caseEntry.getText());
						}
					}
				}
			}
		}
		OutputFormat of = OutputFormat.createPrettyPrint();
		of.setEncoding("UTF-8");
		try {
			Writer write = new FileWriter(serviceNew);
			XMLWriter xmlWriter = new XMLWriter(write, of);
			xmlWriter.write(doc);
			xmlWriter.close();
			System.out.println("重新生成服务识别文件成功！");
		} catch (IOException e) {
			System.out.println("重新生成服务识别文件失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 检查渠道是否重复
	 * 
	 * @param channels
	 * @param flag
	 * @return
	 */
	private static String[] checkRepeated(List<Element> elements, String type, boolean flag) {
		String text = "";
		if ("channel".equals(type)) {
			text = "渠道";
		} else if ("system".equals(type)) {
			text = "系统";
		}
		String[] elementArray = {};
		for (Element element : elements) {
			String id = element.attributeValue("id");
			if (Arrays.asList(elementArray).contains(id)) {
				FileTools.write(FILENAME, text + "[" + id + "]重复！\n\n", true);
			} else {
				elementArray = Arrays.copyOf(elementArray, elementArray.length + 1);
				elementArray[elementArray.length - 1] = id;
			}
		}
		if (elements.size() == elementArray.length) {
			FileTools.write(FILENAME, "没有重复" + text + "！\n", true);
			if (flag) {
				Arrays.sort(elementArray);
			}
			FileTools.write(FILENAME, Arrays.toString(elementArray) + "\n\n", true);
		}
		return elementArray;
	}

	/**
	 * 检查每个渠道下switch节点mode属性值与expression属性值是否匹配
	 * 
	 * @param switchCase
	 * @param channelId
	 */
	private static void checkSwitch(Element switchCase, String channelId) {
		String mode = switchCase.attributeValue("mode");
		String expression = switchCase.attributeValue("expression");
		if (Configs.msgMode.STN.equals(mode)) {
			if (expression == null || expression.indexOf(Configs.msgExp.STN_LENGTH) < 0
					|| expression.indexOf(Configs.msgExp.STN_LENGTH) < 0) {
				FileTools.write(FILENAME, "渠道[" + channelId + "],mode[" + mode + "]与expression["
						+ expression + "]不匹配！\n", true);
			} else {
				FileTools.write(FILENAME, "渠道[" + channelId + "],mode[" + mode + "]与expression["
						+ expression + "]匹配！\n", true);
			}
		}
		if (Configs.msgMode.STN_XML.equals(mode)) {
			if (!(Configs.msgExp.STN_XML_SVCCOD + "+" + Configs.msgExp.STN_XML_BSICOD)
					.equals(expression)) {
				FileTools.write(FILENAME, "渠道[" + channelId + "],mode[" + mode + "]与expression["
						+ expression + "]不匹配！\n", true);
			} else {
				FileTools.write(FILENAME, "渠道[" + channelId + "],mode[" + mode + "]与expression["
						+ expression + "]匹配！\n", true);
			}
		}
		if (Configs.msgMode.TRA_XML.equals(mode)) {
			if (expression == null) {
				FileTools.write(FILENAME, "渠道[" + channelId + "],mode[" + mode + "]与expression["
						+ expression + "]不匹配！\n", true);
			} else if (expression.indexOf(Configs.msgExp.TRA_XML_DH) < 0
					&& expression.indexOf(Configs.msgExp.TRA_XML_RH) < 0
					&& expression.indexOf(Configs.msgExp.TRA_XML_IH) < 0
					&& expression.indexOf(Configs.msgExp.TRA_XML_MH) < 0) {
				FileTools.write(FILENAME, "渠道[" + channelId + "],mode[" + mode + "]与expression["
						+ expression + "]不匹配！\n", true);
			} else {
				FileTools.write(FILENAME, "渠道[" + channelId + "],mode[" + mode + "]与expression["
						+ expression + "]匹配！\n", true);
			}
		}
	}

	/**
	 * 删除结果文件
	 */
	public static void delete() {
		File file = new File(FILENAME);
		file.delete();
	}

	/**
	 * 检查系统识别文件
	 * 
	 * @param flag
	 *            是否重新生成系统识别文件（去重排序）
	 */
	@SuppressWarnings("unchecked")
	public static void checkSystemIdentify(boolean flag) {
		String systemIdentify = Configs.outConf.SYSTEM_INDENTIFY;
		// String serviceFieldFile_in = Configs.inConf.SERVICE_FIELD +
		// "/serviceFieldCfg.xml";
		// String serviceFieldFile_out = Configs.outConf.SERVICE_FIELD +
		// "/serviceFieldCfg.xml";
		FileInputStream fis = null;
		SAXReader reader = new SAXReader();
		Document document;
		try {
			fis = new FileInputStream(systemIdentify);
			document = reader.read(fis);
		} catch (DocumentException | FileNotFoundException e) {
			e.printStackTrace();
			FileTools.write(FILENAME, "读取系统识别文件出错！请检查文件[" + systemIdentify + "]\n", true);
			return;
		}
		FileTools.write(FILENAME, "检查系统识别文件开始！\n", true);
		Element root = document.getRootElement();
		List<Element> systems = root.elements("system");
		String[] systemArray = checkRepeated(systems, "system", flag);
		System.out.println("systems:" + Arrays.toString(systemArray));
		int total = 0;
		String[] allServiceArray = {};
		String[] allRepeatedServices = {};
		String[] systemRepeatedArray = {};
		Map<String, String[]> systemMap = new HashMap<String, String[]>();
		for (Element system : systems) {
			String systemId = system.attributeValue("id");
			List<Element> services = system.elements();
			String[] serviceArray = {};
			String[] repeatedServices = {};
			for (Element service : services) {
				String serviceId = service.getText();
				if (Arrays.asList(serviceArray).contains(serviceId)) {
					repeatedServices = Arrays.copyOf(repeatedServices, repeatedServices.length + 1);
					repeatedServices[repeatedServices.length - 1] = serviceId;
				} else {
					serviceArray = Arrays.copyOf(serviceArray, serviceArray.length + 1);
					serviceArray[serviceArray.length - 1] = serviceId;
				}
				if (Arrays.asList(allServiceArray).contains(serviceId)) {
					allRepeatedServices = Arrays.copyOf(allRepeatedServices,
							allRepeatedServices.length + 1);
					allRepeatedServices[allRepeatedServices.length - 1] = serviceId;
				} else {
					allServiceArray = Arrays.copyOf(allServiceArray, allServiceArray.length + 1);
					allServiceArray[allServiceArray.length - 1] = serviceId;
				}
			}
			FileTools.write(FILENAME, "系统[" + systemId + "]共有交易" + services.size() + "个\n", true);
			total += services.size();
			if (0 == repeatedServices.length) {
				FileTools.write(FILENAME, "其中没有重复交易！\n\n", true);
			} else {
				FileTools.write(FILENAME, "其中重复交易为:" + Arrays.toString(repeatedServices) + "\n\n",
						true);
				systemRepeatedArray = union(systemRepeatedArray, repeatedServices);
			}
			if (flag) {
				Arrays.sort(serviceArray);
			}
			systemMap.put(systemId, serviceArray);
		}
		FileTools.write(FILENAME, "全局（不同系统）共有交易" + total + "个\n", true);
		allRepeatedServices = minus(allRepeatedServices, systemRepeatedArray);
		if (0 == allRepeatedServices.length) {
			FileTools.write(FILENAME, "其中没有重复交易！\n\n", true);
		} else {
			FileTools.write(FILENAME, "其中重复交易为:" + Arrays.toString(allRepeatedServices) + "\n\n",
					true);
		}
		if (flag) {
			reWriteSystemIdentify(systemArray, systemMap);
		}
	}

	private static void reWriteSystemIdentify(String[] systemArray, Map<String, String[]> systemMap) {
		String sysNew = "systemIdentify_new.xml";
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("systems");
		for (String systemId : systemArray) {
			Element systemEle = root.addElement("system");
			systemEle.addAttribute("id", systemId);
			String[] services = systemMap.get(systemId);
			for (String serviceId : services) {
				Element serviceEle = systemEle.addElement("service");
				serviceEle.setText(serviceId);
			}
		}
		OutputFormat of = OutputFormat.createPrettyPrint();
		of.setEncoding("UTF-8");
		try {
			Writer write = new FileWriter(sysNew);
			XMLWriter xmlWriter = new XMLWriter(write, of);
			xmlWriter.write(doc);
			xmlWriter.close();
			System.out.println("重新生成系统识别文件成功！");
		} catch (IOException e) {
			System.out.println("重新生成系统识别文件失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 求两字符串数组并集
	 * 
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	private static String[] union(String[] arr1, String[] arr2) {
		Set<String> set = new HashSet<String>();
		for (String str : arr1) {
			set.add(str);
		}
		for (String str : arr2) {
			set.add(str);
		}
		String[] result = {};
		return set.toArray(result);
	}

	/**
	 * 求两字符串数组差集
	 * 
	 * @param longArr
	 * @param shortArr
	 * @return
	 */
	private static String[] minus(String[] longArr, String[] shortArr) {
		LinkedList<String> list = new LinkedList<String>();
		LinkedList<String> history = new LinkedList<String>();
		for (String str : longArr) {
			if (!list.contains(str)) {
				list.add(str);
			}
		}
		for (String str : shortArr) {
			if (list.contains(str)) {
				history.add(str);
				list.remove(str);
			} else {
				if (!history.contains(str)) {
					list.add(str);
				}
			}
		}
		String[] result = {};
		return list.toArray(result);
	}

	/**
	 * 检查字段校验：in、out端字段校验服务和配置文件中是否一致
	 */
	public static void checkServiceField() {
		// TODO Auto-generated method stub

	}
}
