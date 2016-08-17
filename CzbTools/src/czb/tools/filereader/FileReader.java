package czb.tools.filereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class FileReader {

	public String readContent(String filepath) throws IOException{
		if(filepath!=null && !filepath.equals("")){
			String type = filepath.substring(filepath.lastIndexOf(".")+1);
			type = type.toLowerCase();
			CommonReader reader = null;
            if(type.equals("doc")){
                reader = new DocFileReader();
            }else if(type.equals("docx")){
                reader = new DocxFileReader();
            }else if(type.equals("pdf")){
                reader = new PdfFileReader();
            }else if(type.equals("xls")){
                reader = new ExcelFileReader();
            }else if(type.equals("xlsx")){
                reader = new ExcelFileReader();
            }else{
                reader = new TxtFileReader();
            }
			String content= reader.readContent(filepath);
			return content;
		}else{
			throw new FileNotFoundException("file path is null or empty");
		}
	}
	
	public String readContent(String filepath,String charSet) throws IOException{
		if(filepath!=null && !filepath.equals("")){
			String type = filepath.substring(filepath.lastIndexOf(".")+1);
			type = type.toLowerCase();
			CommonReader reader = null;
            if(type.equals("doc")){
                reader = new DocFileReader();
            }else if(type.equals("docx")){
                reader = new DocxFileReader();
            }else if(type.equals("pdf")){
                reader = new PdfFileReader();
            }else if(type.equals("xls")){
                reader = new ExcelFileReader();
            }else if(type.equals("xlsx")){
                reader = new ExcelFileReader();
            }else{
                reader = new TxtFileReader();
            }
			String content= reader.readContent(filepath,charSet);
			return content;
		}else{
			throw new FileNotFoundException("file path is null or empty");
		}
	}
	
	public void write(File file, String line, boolean append) throws IOException {
//		if (!check(filePath)) {
//			return;
//		}
		FileWriter writer = null;
		if (append) {
			writer = new FileWriter(file, true);
		} else {
			writer = new FileWriter(file);
		}
		writer.write(line);
		writer.flush();
		writer.close();
		saveAsCSV(file,line,append);
	}
	
	public void saveAsCSV(File file, String line, boolean append)throws IOException{
		String filename = file.getAbsolutePath();
		filename = filename.substring(0,filename.lastIndexOf(".")+1)+"csv";
		System.out.println(filename);
		file = new File(filename);
		String tmp = line.replaceAll("\t", ",");
		FileWriter writer = null;
		if (append) {
			writer = new FileWriter(file, true);
		} else {
			writer = new FileWriter(file);
		}
		writer.write(tmp);
		writer.flush();
		writer.close();
	}

	
	public static void main(String[] args) throws IOException{
		String filepath ="E:\\test\\1.doc";
		FileReader reader = new FileReader();
//		System.out.println(reader.readContent(filepath));
//		filepath ="E:\\test\\2.docx";
//		System.out.println(reader.readContent(filepath));
//		filepath ="E:\\test\\3.txt";
//		System.out.println(reader.readContent(filepath));
//		filepath="E:\\test\\4.pdf";
//		System.out.println(reader.readContent(filepath));
		filepath = "E:\\test\\test.xls";
//		reader.readContent(filepath);
		System.out.println(reader.readContent(filepath));
		
	}
	
}
