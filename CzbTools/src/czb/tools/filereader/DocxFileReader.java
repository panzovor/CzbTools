package czb.tools.filereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class DocxFileReader implements CommonReader{

	public String readContent(String filepath) throws FileNotFoundException, IOException{
		File file  =null;
		if((file=FileCheck.getInstance().checkFile(filepath))!= null){
			OPCPackage oPCPackage = POIXMLDocument.openPackage(filepath);
            XWPFDocument xwpf = new XWPFDocument(oPCPackage);
            POIXMLTextExtractor ex = new XWPFWordExtractor(xwpf);
            String content = ex.getText();
            return content;
		}else
			return "null";
		
	}
	
	public static void main(String[] args) {
		String filepath="E:\\test\\2.docx";
		DocxFileReader fileReader = new DocxFileReader();
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
