package czb.tools.filereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfFileReader implements CommonReader{

	public String readContent(String filepath) throws FileNotFoundException, IOException{
		File file  =null;
		if((file=FileCheck.getInstance().checkFile(filepath))!= null){
			PDDocument document = PDDocument.load(file);
			PDFTextStripper stripper = new PDFTextStripper();
			String content = stripper.getText(document);
			document.close();
			return content;
		}else
			return "null";
	}
	
	public static void main(String[] args) {
		String filepath ="E:\\����\\��������\\deep learning.pdf";
		System.out.println(filepath);
		PdfFileReader fileReader = new PdfFileReader();
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
