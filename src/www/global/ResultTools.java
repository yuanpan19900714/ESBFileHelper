package www.global;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class ResultTools {
	private final static String FILENAME = "result.txt";
	/*
	 * 结果输出方法
	 */
	public static void write(String context, boolean flag) {
		
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
	
	public static void delete(){
		File file = new File("result.txt");
		file.delete();
	}
}
