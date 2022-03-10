package com.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PushbackInputStream;

public class LicenseHelper {
	public static void main(String[] args) {
		String license = "F:/SVNCheckout/湖北银行ESB/工程管理/09code/SmartESB/bin/esblicense";
		// byte[] bytes = FileTools.readContent(license);
		PushbackInputStream pis = null;
		try {
			pis = new PushbackInputStream(new FileInputStream(license));
			String userID = newString(readFromFile(pis));
			String licenseKey = newString(readFromFile(pis));
			String companyName = newString(readFromFile(pis));
			String installRoot = newString(readFromFile(pis));
			boolean flag = Boolean.valueOf(newString(readFromFile(pis)));
			String sMacAddress = null;
			try {
				if (pis.available() > 0) {
					sMacAddress = newString(readFromFile(pis));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("userID:" + userID);
			System.out.println("licenseKey:" + licenseKey);
			System.out.println("companyName:" + companyName);
			System.out.println("installRoot:" + installRoot);
			System.out.println("flag:" + flag);
			System.out.println("sMacAddress:" + sMacAddress);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static String newString(byte[] b) {
		try {
			return new String(b, "8859_1");
		} catch (Exception e) {
			e.printStackTrace();
			return new String(b);
		}
	}

	private static byte[] readFromFile(PushbackInputStream pis) {
		int size = 0;
		try {
			size = pis.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] b = new byte[size];

		for (int i = 0; i < size; ++i) {
			try {
				b[i] = (byte) (pis.read() ^ '\uf02a');
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return b;
	}
}
