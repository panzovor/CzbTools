package czb.tools.filereader;

import java.io.File;
import java.io.FileNotFoundException;

public class FileCheck {
	
	private final static FileCheck fileCheck = new FileCheck();
	
	private FileCheck(){
		
	}
	
	public static final FileCheck getInstance(){
		return fileCheck;
	}

	public File checkFile(String filepath) throws FileNotFoundException{
		if(filepath == null){
			throw new FileNotFoundException("file name is null");
		}
		if(filepath.trim().equals("")){
			throw new FileNotFoundException("file name is empty");
		}
		File file = new File(filepath);
		if(file.exists()){
			return file; 
		}else{
			throw new FileNotFoundException("file does not exit");
		}
	}
	
	
	
}
