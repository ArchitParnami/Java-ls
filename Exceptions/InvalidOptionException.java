package Exceptions;

 public class InvalidOptionException extends Exception {
 	String opt;
 	public InvalidOptionException(String str) {
 		opt = str;
 	}
	public String toString() {
		return "ls : " + "'" + opt + "'" + " - " + "No such option\n" + 
				"command : ls [option] [extension] [path]\n" + 
				"For more help type  : ls --man " ;
	}
}