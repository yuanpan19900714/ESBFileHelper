package com.helper;

import com.entry.Configs;

import www.global.FileListTools;

/**
 * 文件清单生成辅助类
 * 
 * @author Administrator
 * 
 */
public class FileListHelper {

	public static void main(String[] args) {
		String metadataPath_in = Configs.inConf.METADATA;
		String metadataPath_out = Configs.outConf.METADATA;
		String serviceFieldPath_in = Configs.inConf.SERVICE_FIELD;
		String serviceFieldPath_out = Configs.outConf.SERVICE_FIELD;
		// PrintStream out = System.out;
		// out.println("metadataPath_in:[" + metadataPath_in + "]");
		// out.println("metadataPath_out:[" + metadataPath_out + "]");
		// out.println("serviceFieldPath_in:[" + serviceFieldPath_in + "]");
		// out.println("serviceFieldPath_out:[" + serviceFieldPath_out + "]");
		FileListTools.delete();
//		FileListTools.writeCommonFile();
		FileListTools.writeMetadataList(metadataPath_in, metadataPath_out);
		FileListTools.writeServiceFieldList(serviceFieldPath_in, serviceFieldPath_out);
	}
}
