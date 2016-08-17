package czb.tools.filereader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hwpf.extractor.WordExtractor;

public class DocFileReader implements CommonReader{


	public String readContent(String filepath) throws FileNotFoundException, IOException{
		File file  =null;
		if((file=FileCheck.getInstance().checkFile(filepath))!= null){
			FileInputStream  inputStream  = new FileInputStream(file);
			WordExtractor extractor= new WordExtractor(inputStream);
			String content =  extractor.getText();
			extractor.close();
			return content;
		}else
			return "null";
		
	}
	
	public static void main(String[] args) {
		String filepath="E:\\test\\1.doc";
		DocFileReader fileReader = new DocFileReader();
		try {
			System.out.println(fileReader.readContent(filepath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String readContent(String filepath, String charset) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		return readContent(filepath);
	}

	
}
