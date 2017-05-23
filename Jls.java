import java.io.*;
import java.util.*;
import java.util.Date;
import Exceptions.*;

class Jls {
	
	String currDir;
	ArrayList<String> exts;
	ArrayList<File> files;
	String args[];
	boolean listAll, listOnlyFiles, listOnlyDirs, longList, recursiveList, 
			listReadable, listWritable, listExecutable, listSize, listTime, 
			noExts, numbering; 
    
    FilenameFilter filter;
     
	Jls(String[] args) throws InvalidOptionException, NoSuchFileException {

		if(args.length > 0 && args[0].equals("--man")) {
			 printMan();
		}
		
		else {
			this.args = args;
			exts = new ArrayList<String>();
			files = new ArrayList<File>();
			scanArguments();
			display();
		}
	}

	void printMan() {
	
		//String file = "E:\\MEDIA\\DOCUMENTS\\JAVA\\Practice\\ls\\man.txt";
		String file = System.getProperty("user.dir") + "\\man.txt";
		try(FileInputStream fin = new FileInputStream(file)) {
			
			int i;
			do {
				i = fin.read();
				if(i!=-1)
					System.out.print((char)i);
			}while(i!=-1);
		}
		catch(FileNotFoundException e) {
			System.out.println("File " + file + " not found");
		}
		catch(IOException e) {
			System.out.println("An I/O Error occured");
		}
	
     }

	void scanArguments() throws InvalidOptionException, NoSuchFileException {

     	boolean scanOptions = true;
		boolean scanExts = true;

		int n = args.length;
		int index = 0;
		String arg;

		while(scanOptions && index < n) {
			
			arg = args[index];
			
			if(arg.charAt(0) == '-') {
				switch(arg.substring(1)) {
					case "a" : listAll = true; break;
					case "f" : listOnlyFiles = true; break;
					case "d" : listOnlyDirs = true; break;
					case "l" : longList = true; break;
					case "R" : recursiveList = true; break;
					case "r" : listReadable = true; break;
					case "w" : listWritable = true; break;
					case "x" : listExecutable = true; break;
					case "s" : listSize = true; break;
					case "t" : listTime = true; break;
					case "n" : numbering = true; break;
					default : throw new InvalidOptionException(arg.substring(1));
				}
				
				index++;
			}

			else {
				scanOptions = false;
			}		
		}

		while(scanExts && index < n) {
			
			arg = args[index]; 

			if(arg.charAt(0) == '.') {
			
				if(arg.length() ==1 ) {
					scanExts = false;
				}

				else {
					char ch = arg.charAt(1);
			
					if(ch == '.' ||  ch == '/')
						scanExts = false;
			
					else {
						exts.add(arg);
						index++;
					}
				}

			}

			else {
				scanExts = false;
			}
		}

		if(exts.size()==0) {
			noExts = true;
		}
		
		File f;
		while(index < n) {

			arg = args[index];
			f = new File(arg);
			
			if(f.exists()) {
				files.add(f);
				index++;
			}

			else {
				throw new NoSuchFileException(arg);
			}

		}

		if(files.size() == 0) {
				files.add(new File(System.getProperty("user.dir")));
		}
	}

	void display() {

		if(!noExts){
			filter = new Filter(exts,recursiveList);
		}

		File[] contents;
		if(files.size()==1) {
			contents = setContents(files.get(0));
			printContents(contents,0);
		}

		else {
			for(File f : files) {
				contents = setContents(f);	
				System.out.println(f.getPath());
				printContents(contents,0);
				System.out.println("\n");
			}
		}
	}

	File[] setContents(File f) {
		if(f.isDirectory()) {
			return getContentList(f,filter);
		}
		else {
			if(filter == null || filter.accept(f,f.getName())) {
				File[] temp;
				temp = new File[1];
				temp[0] = f;
				return temp;
			}

			else {
				return null;
			}
		}
	}

	File[] getContentList(File f, FilenameFilter fil) {

			if(fil == null) {
				return f.listFiles();
			}
			else {
				return f.listFiles(fil);
			}
	}

	void printContents(File[] cont, int indent) {
		int n=1;
		
		if(cont == null)
			return;
				
		for(File f : cont) {

			if(!listAll && f.isHidden())
				continue;
			
			if(f.isFile()) {
				if(listOnlyDirs && !listOnlyFiles)
					continue;
			}
			else {
				if(listOnlyFiles && !listOnlyDirs)
					continue;
			}

			if(listReadable && !f.canRead())
				continue;
			if(listWritable && !f.canWrite())
				continue;
			if(listExecutable && !f.canExecute())
				continue;

			long size = f.length();
			Date date = new Date(f.lastModified());
			
			if(recursiveList) {
				for(int i=0;i<indent;i++)
					System.out.print("\t");
			}

			if(numbering){
				System.out.printf("%5d\t",n);
				++n;
			}
			

			if(longList) {
				char r,w,x,t='f';
				r=w=x='-';
				if(f.canRead())
					r='r';
				if(f.canWrite())
					w='w';
				if(f.canExecute())
					x='x';
				if(f.isDirectory())
					t='d';
							
				System.out.printf(" %c%c%c%c\t%10d\t%ta %<tb %<td %<tR\t%s\n",t,r,w,x,size,date,f.getName());
			}
			else if(listSize && !listTime) {
				System.out.printf("  %10d\t%s\n",size,f.getName());
			}
			
			else if(listTime && !listSize) {
				System.out.printf(" %ta %<tb %<td %<tR\t%s\n",date,f.getName());	
			}
			
			else if(listSize && listTime) {
				System.out.printf(" %10d\t%ta %<tb %<td %<tR\t%s\n",size,date,f.getName());	
			}
			else {
					System.out.println(" "+ f.getName());
			}
		
		 if(recursiveList) {
		 	if(f.isDirectory()) {
		 		printContents(getContentList(f,filter),indent+1);
		 		System.out.println();
		 	}
		 }	
		
		}
	}

}
