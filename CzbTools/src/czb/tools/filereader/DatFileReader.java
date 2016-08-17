package czb.tools.filereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class DatFileReader {
	
	public String readContent(String filepath) throws IOException{
		File file =null;
		if((file = FileCheck.getInstance().checkFile(filepath)) !=null){
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tmp="";
			StringBuffer stringBuffer = new StringBuffer();
			while((tmp = reader.readLine())!=null){
				stringBuffer.append(tmp+"\n");
			}
			return stringBuffer.toString();
		}
		return null;
	}
	
	public String readContent(String filepath, String charSet) throws IOException {
		Reader reader = null;
		if (charSet.equals(""))
			reader = new FileReader(FileCheck.getInstance().checkFile(filepath));
		else
			reader = new InputStreamReader(new FileInputStream(FileCheck.getInstance().checkFile(filepath)),charSet);
		BufferedReader br = new BufferedReader(reader);
		String line = "";
		StringBuffer content = new StringBuffer();
		while ((line = br.readLine()) != null) {
			content.append(line + "\n");
		}
		if (content != null)
			return content.substring(0, content.length() - 1);
		return null;
	}
	
	public static void main(String[] args) {
		DatFileReader fileReader = new DatFileReader();
		try {
			System.out.println(fileReader.readContent("E:\\DownLoad\\FireFox_Download\\dataset_617543\\emotion����\\corpus\\"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
