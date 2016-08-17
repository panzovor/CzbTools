package czb.tools.filereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class TxtFileReader implements CommonReader{

	public String readContent(String filepath) throws IOException {
		Reader reader = null;
		reader = new InputStreamReader(new FileInputStream(FileCheck.getInstance().checkFile(filepath)));
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

	public List<String> readLines(String filepath, String charSet) throws IOException {
		Reader reader = null;
		if (charSet.equals(""))
			reader = new FileReader(FileCheck.getInstance().checkFile(filepath));
		else
			reader = new InputStreamReader(new FileInputStream(FileCheck.getInstance().checkFile(filepath)),charSet);
		BufferedReader br = new BufferedReader(reader);
		String line = "";
		List<String> lines = new ArrayList<String>();
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		return lines;
	}

	public List<String[]> readWordsWithSeperator(String filepath, String seperator, String charSet) throws IOException {
		Reader reader = null;
		if (charSet.equals(""))
			reader = new FileReader(FileCheck.getInstance().checkFile(filepath));
		else
			reader = new InputStreamReader(new FileInputStream(FileCheck.getInstance().checkFile(filepath)),charSet);
		if (seperator != null && !seperator.equals("")) {
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			List<String[]> lines = new ArrayList<String[]>();
			String[] tmp;
			while ((line = br.readLine()) != null) {
				if(!line.equals("\n") && !line.trim().equals("")){
					tmp = line.split(seperator);
					lines.add(tmp);
				}
				
			}
			return lines;
		} else {
			return null;
		}
	}
	
	public List<String[]> readWordsWithSeperatorWithLength(String filepath, String seperator, String charSet,int length) throws IOException {
		Reader reader = null;
		boolean first =true;
		if (charSet.equals(""))
			reader = new FileReader(FileCheck.getInstance().checkFile(filepath));
		else
			reader = new InputStreamReader(new FileInputStream(FileCheck.getInstance().checkFile(filepath)),charSet);
		if (seperator != null && !seperator.equals("")) {
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			List<String[]> lines = new ArrayList<String[]>();
			String[] tmp;
			while ((line = br.readLine()) != null) {
				if(!line.equals("\n") && !line.trim().equals("")){
					
					
					tmp = line.split(seperator);
					if(first&& length ==0){
						length = tmp.length;
						first = false;
					}
					if(tmp.length>length){
						String [] tt = new String[length];
						for(int i=0;i<tmp.length;i++){
							if(i<length){
								tt[i] = tmp[i];
							}else{
								tt[length-1]+= tmp[i];
							}
							lines.add(tt);
						}
					}else{
						lines.add(tmp);
					}
					
				}
				
			}
			return lines;
		} else {
			return null;
		}
	}
	
	public List<String[]> readWordsWithSeperator_filter(String filepath, String seperator, String charSet,String[] regex,String[]newstring) throws IOException {
		Reader reader = null;
		if (charSet.equals(""))
			reader = new FileReader(FileCheck.getInstance().checkFile(filepath));
		else
			reader = new InputStreamReader(new FileInputStream(FileCheck.getInstance().checkFile(filepath)),charSet);
		if (seperator != null && !seperator.equals("")) {
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			List<String[]> lines = new ArrayList<String[]>();
			String[] tmp;
			while ((line = br.readLine()) != null) {
				if(regex.length>0){
					for(int i=0;i< regex.length;i++){
						line = line.replaceAll(regex[i], newstring[i]);
					}
				}
				tmp = line.split(seperator);
				lines.add(tmp);
			}
			return lines;
		} else {
			return null;
		}
	}

	public void write(String filePath, List<String> content,String charSet, boolean append) throws IOException {
		File file = null;
		try {
			file = FileCheck.getInstance().checkFile(filePath);
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			if (e.getLocalizedMessage().contains("not exit")) {
				file = new File(filePath);
				file.createNewFile();
			}
		}
		if(charSet.equals("")){
			charSet = "GBK";
		}
		OutputStreamWriter writer = null;
		writer = new OutputStreamWriter(new FileOutputStream(new File(filePath), append),charSet);
		for (String line : content)
			writer.write(line + "\n");

		writer.flush();
		writer.close();
	}

	public void write(String filePath, String content,String charSet, boolean append) throws IOException {
		File file = null;
		try {
			file = FileCheck.getInstance().checkFile(filePath);
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			if (e.getLocalizedMessage().contains("not exit")) {
				file = new File(filePath);
                System.out.println(file.getParent());
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
				file.createNewFile();
			}
		}
		if(charSet.equals("")){
			charSet = "GBK";
		}
		OutputStreamWriter writer = null;
		writer = new OutputStreamWriter(new FileOutputStream(new File(filePath), append),charSet);
		writer.write(content + "\n");

		writer.flush();
		writer.close();
	}

	public void writeWithSperator(String filePath, List<String[]> content, String seperator,String charSet, boolean append)
			throws IOException {
		File file = null;
		try {
			file = FileCheck.getInstance().checkFile(filePath);
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			if (e.getLocalizedMessage().contains("not exit")) {
				file = new File(filePath);
				file.createNewFile();
			}
		}
		if(charSet.equals("")){
			charSet = "GBK";
		}
		OutputStreamWriter writer = null;
		writer = new OutputStreamWriter(new FileOutputStream(new File(filePath), append),charSet);
		String con = "";
		for (int i = 0; i < content.size(); i++) {
			String[] line = content.get(i);
			for (String string : line) {
				con += seperator;
			}
			if (i != content.size() - 1)
				con += "\n";
		}
		writer.flush();
		writer.close();
	}

	public static void main(String[] args) {
		// test
		String filepath = "E:\\t.txt";
		TxtFileReader txtFileReader = new TxtFileReader();
		try {
			// String content = txtFileReader.readContent(filepath);
			// System.out.println(content);
			//
			// List<String> contentL = txtFileReader.readLines(filepath);
			// System.out.println(contentL.toString());
			//
			// List<String[]> contentW =
			// txtFileReader.readWordsWithSeperator(filepath," ");
			// for(String[] strings: contentW){
			// for(String string: strings){
			// System.out.print(string+" ");
			// }
			// System.out.println();
			// }

			filepath = "E:\\DigPro2\\fc_all.arff";
//			String content = "ddd";
//			List<String> cList = new ArrayList<String>();
//			cList.add(content);
//			txtFileReader.write(filepath, cList,"", true);
			System.out.println(txtFileReader.readContent(filepath));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
