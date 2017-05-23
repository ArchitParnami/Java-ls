package Exceptions;

public class NoSuchFileException extends Exception {
	
	String file;
	
	 public NoSuchFileException(String file) {
		this.file = file;
	}

	public String toString() {
		return "ls : Can not access  " + "\"" + file + "\"" + " : No such file or directory \n" + 
				"command : ls [option] [extension] [path]\n" + 
				"For more help type  : ls --man " ; 
	}
}