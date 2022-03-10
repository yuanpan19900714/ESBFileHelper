package www.global;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.entry.Configs;

/**
 * 文件清单工具类
 * 
 * @author Administrator
 * 
 */
public class FileListTools {
	private final static String FILENAME = "fileList.txt";

	/**
	 * 删除结果文件
	 */
	public static void delete() {
		File file = new File(FILENAME);
		file.delete();
	}

	/**
	 * 公共文件清单
	 */
	public static void writeCommonFile() {
		String serviceIdentify = Configs.inConf.SERVICE_INDENTIFY;
		String systemIdentify = Configs.outConf.SYSTEM_INDENTIFY;
		String metadataFile_in = Configs.inConf.METADATA + "/metadata_add.xml";
		String metadataFile_out = Configs.outConf.METADATA + "/metadata_add.xml";
		String serviceFieldFile_in = Configs.inConf.SERVICE_FIELD + "/serviceFieldCfg_add.xml";
		String serviceFieldFile_out = Configs.outConf.SERVICE_FIELD + "/serviceFieldCfg_add.xml";
		List<String> commonFileList = new ArrayList<String>();
		commonFileList.add(serviceIdentify);
		commonFileList.add(systemIdentify);
		commonFileList.add(metadataFile_in);
		commonFileList.add(metadataFile_out);
		commonFileList.add(serviceFieldFile_in);
		commonFileList.add(serviceFieldFile_out);
		writeList2File(commonFileList);
	}

	/**
	 * 拆组包配置文件清单
	 * 
	 * @param metadataPath_in
	 * @param metadataPath_out
	 */
	public static void writeMetadataList(String metadataPath_in, String metadataPath_out) {
		writeList2File(getMetadataList(metadataPath_in, metadataPath_out));
	}

	/**
	 * 字段校验配置文件清单
	 * 
	 * @param serviceFieldPath_in
	 * @param serviceFieldPath_out
	 */
	public static void writeServiceFieldList(String serviceFieldPath_in, String serviceFieldPath_out) {
		writeList2File(getServiceFieldList(serviceFieldPath_in, serviceFieldPath_out));
	}

	/**
	 * 获取拆组包文件路径列表
	 * 
	 * @param metadataPath_in
	 * @param metadataPath_out
	 * @return
	 */
	private static List<String> getMetadataList(String metadataPath_in, String metadataPath_out) {
		List<String> metadataList = new ArrayList<String>();
		if (!metadataPath_in.isEmpty()) {
			metadataList = getAllFileName(new File(metadataPath_in), metadataList);
		}
		if (!metadataPath_out.isEmpty()) {
			metadataList = getAllFileName(new File(metadataPath_out), metadataList);
		}
		return metadataList;
	}

	/**
	 * 获取字段校验文件路径列表
	 * 
	 * @param serviceFieldPath_in
	 * @param serviceFieldPath_out
	 * @return
	 */
	private static List<String> getServiceFieldList(String serviceFieldPath_in,
			String serviceFieldPath_out) {
		List<String> serviceFieldList = new ArrayList<String>();
		if (!serviceFieldPath_in.isEmpty()) {
			serviceFieldList = getAllFileName(new File(serviceFieldPath_in), serviceFieldList);
		}
		if (!serviceFieldPath_out.isEmpty()) {
			serviceFieldList = getAllFileName(new File(serviceFieldPath_out), serviceFieldList);
		}
		return serviceFieldList;
	}

	/**
	 * 使用递归获取某个目录下所有文件路径的列表,然后与原列表合并
	 * 
	 * @param file
	 * @param resultFileName
	 * @return
	 */
	private static List<String> getAllFileName(File file, List<String> resultFileName) {
		File[] files = file.listFiles(new MyFileFilter());
		if (files == null) {
			return resultFileName;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				resultFileName.add(f.getPath());
				getAllFileName(f, resultFileName);
			} else {
				resultFileName.add(f.getPath());
			}
		}
		return resultFileName;
	}

	/**
	 * 将文件清单写入结果文件
	 * 
	 * @param list
	 */
	private static void writeList2File(List<String> list) {
		for (String fileName : list) {
			fileName = fileName.substring(fileName.indexOf("SmartESB"));
			if(fileName.endsWith(".xml")){
				FileTools.write(FILENAME, fileName + "\n", true);
			}
		}
	}

}
