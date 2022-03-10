package com.helper;

import www.global.FileCheckTools;

/**
 * 文件检查辅助类
 * @author Administrator
 *
 */
public class FileCheckHelper {

	public static void main(String[] args) {
		FileCheckTools.delete();
		FileCheckTools.checkServiceIdentify(true);
		FileCheckTools.checkSystemIdentify(true);
		FileCheckTools.checkServiceField();

	}

}
