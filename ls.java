import Exceptions.*;

class ls {
	public static void main(String[] args) {
		try {
			Jls list = new Jls(args);
		} 
		catch (InvalidOptionException | NoSuchFileException e) {
			System.out.println(e);			
		}
	}
}