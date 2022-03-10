package www.global;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class FileTools {
	/**
	 * 读取文件内容
	 * 
	 * @param fileName
	 * @return
	 */
	public static byte[] readContent(String fileName) {
		byte[] value = null;
		try {
			InputStream input = new FileInputStream(fileName);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] cache = new byte[100];
			int len = -1;
			while ((len = input.read(cache)) != -1) {
				baos.write(cache, 0, len);
			}
			value = baos.toByteArray();
			input.close();
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 结果输出方法
	 */
	public static void write(String FILENAME, String context, boolean flag) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(FILENAME, flag);
			writer.write(context);
		} catch (IOException e) {
			System.out.println("输出结果异常");
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("关闭Writer异常");
			}
		}
	}

}
