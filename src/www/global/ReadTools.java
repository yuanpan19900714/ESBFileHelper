package www.global;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadTools {
	private static final String FILE_TYPE = ".xml";

	public static List<String> getAllFileName(String path) {
		List<String> allFileName = getAll(checkPath(path), checkPath(path));
		return allFileName;
	}

	private static List<String> getAll(String path, String name) {
		List<String> allFileName = new ArrayList<String>();
		File file = null;
		try {
			file = new File(path);
		} catch (Exception e) {
			return null;
		}

		if (!file.isDirectory()) {
			return allFileName;
		} else {
			String[] names = file.list();
			for (int i = 0; i < names.length; i++) {
				File childFile = new File(path + names[i]);
				if (childFile.isDirectory()) {
					// 当有子文件夹，则递归，暂时由错误，虽能获得文件名，但不能获得其路径
					allFileName.addAll(getAll(path + names[i], names[i]));
				} else if ((names[i].length() - 4) == names[i].indexOf(FILE_TYPE)) {
					// 当读到的文件名称是以“.xml”结尾时，即文件是xml格式的，则存入list中
					if (null != name && !"".equals(name)) {
						allFileName.add(name + "/" + names[i]);
					} else {
						allFileName.add(names[i]);
					}
				}
			}
		}
		return allFileName;
	}

	// 当输入的路径不带“/”时，为其加上
	private static String checkPath(String path) {
		if (!path.contains("/")) {
			path = new StringBuilder(path).append("/").toString();
		}
		return path;
	}

}
