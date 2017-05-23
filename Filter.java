import java.io.*;
import java.util.*;

class Filter implements FilenameFilter {
	ArrayList<String> exts;
	boolean isRecursive;
	Filter(ArrayList<String> exts,boolean rec) {
		this.exts = exts;
		isRecursive = rec;
	}

	public boolean accept(File dir,String name) {
		if(isRecursive) {
			File f = new File(dir.getAbsolutePath() + "\\" + name);
			if(f.isDirectory())
				return true;
		}

		for(String ext : exts) {
			if(name.endsWith(ext)) {
				return true;
			}
		}

		return false;
	}
}