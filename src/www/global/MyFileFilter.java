package www.global;

import java.io.File;
import java.io.FileFilter;

public class MyFileFilter implements FileFilter {
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		} else {
			String name = file.getName();
			if (name.endsWith(".xml")) {
				if (name.indexOf("functions") > -1) {
					return false;
				}
				if (name.indexOf("metadata") > -1) {
					return false;
				}
				if (name.indexOf("mode") > -1) {
					return false;
				}
				if (name.indexOf("preload") > -1) {
					return false;
				}
				if (name.indexOf("transform") > -1) {
					return false;
				}
				if (name.indexOf("sopServiceMapping") > -1) {
					return false;
				}
				if (name.indexOf("serviceField") > -1) {
					return false;
				}
				return true;
			}
			return false;
		}
	}

}