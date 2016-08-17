package czb.tools.wordsegment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import czb.tools.filereader.CommonContentFilter;
import czb.tools.filereader.ContentFilter;
import czb.tools.filereader.FileReader;
import czb.tools.filereader.TxtFileReader;

public class TFIDF {

	private Nlpir nlpir = new Nlpir();
	private String userdict = System.getProperty("user.dir") + "\\dict\\Userdict.txt";
	private String blaclistdict = System.getProperty("user.dir") + "\\dict\\BlackListDict.txt";
	private FileReader reader = new FileReader();

	public void enableUserDict() {
		int er = nlpir.NLPIR_ImportUserDict(userdict, true);
		if (er < 0) {
			System.out.println("用户词典导入失败");
		}
	}

	public void enableBlackListDict() {
		int er = nlpir.NLPIR_ImportKeyBlackList(blaclistdict, "utf-8");
		if (er < 0) {
			System.out.println("停用词典导入失败");
		}
	}

	public Map<String, Integer> statTf(String filepath) throws IOException {
		Filter filter = new Filter();
		String content = reader.readContent(filepath, "utf-8");
		content = filter.filter(content);
		Map<String, Integer> result = nlpir.NLPIR_WordFreqStat(content, true);
		return result;
	}

	public Map<String, Integer> statIDf(Collection<String> fileNames) throws IOException {
		Map<String, Integer> tMap = null;
		Map<String, Integer> result = new HashMap<String, Integer>();
		int tmp = 0;
		for (String string : fileNames) {
			tMap =null;
			tMap = statTf(string);
			for (String string2 : tMap.keySet()) {
				if (result.containsKey(string2)) {
					tmp = result.get(string2);
					result.put(string2, ++tmp);
				} else {
					result.put(string2, 1);
				}
			}

		}
		return result;
	}

	/**
	 * 统计一篇文章在一份语料库中词语的tf-idf
	 * 
	 * @param filepath
	 *            待统计文章
	 * @param fileNames
	 *            语料库集合
	 * @return 词语及其tf-idf值
	 */
	public Map<String, Double> statTF_IDF(String filepath, Collection<String> fileNames, CommonContentFilter filter) {
		Map<String, Double> tf_map = new HashMap<String, Double>();
		Map<String, Integer> idf_map = new HashMap<String, Integer>();
		Map<String, Double> tf_idf_map = new HashMap<String, Double>();
		enableBlackListDict();
		enableUserDict();
		// 统计tf
		try {
			Map<String,Integer>tf_map1 = statTf(filepath);
			for(String string: tf_map1.keySet()){
				tf_map.put(string, tf_map1.get(string)/(double)tf_map1.keySet().size());
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		// 统计idf
		try {
			idf_map = statIDf(fileNames);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 统计tf-idf
		int filesNum = fileNames.size();
		double tf_idf =0;
		for (String key : tf_map.keySet()) {
			double tm =filesNum / (double)(idf_map.get(key));
			tf_idf = tf_map.get(key) * Math.log(tm);
			tf_idf_map.put(key, tf_idf);
			System.out.println(key+" "+tf_idf);
		}
		return tf_idf_map;
	}

	public void statTF_IDF_multi(Collection<String> files, Collection<String> fileNames){
		Map<String,Map<String, Double>> tf_map = new HashMap<String,Map<String, Double>>();
		Map<String, Integer> idf_map = new HashMap<String, Integer>();
		Map<String,Map<String, Double>> tf_idf_map = new HashMap<String,Map<String, Double>>();
		enableBlackListDict();
		enableUserDict();
		// 统计tf
		try {
			for(String file: files){
				Map<String, Double> tt = new HashMap<String, Double>();
				Map<String,Integer>tf_map1 = statTf(file);
				for(String string: tf_map1.keySet()){
					tt.put(string, tf_map1.get(string)/(double)tf_map1.keySet().size());
				}
				tf_map.put(file, tt);
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		// 统计idf
		try {
			idf_map = statIDf(fileNames);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 统计tf-idf
		int filesNum = fileNames.size();
		double tf_idf =0;
		for (String key : tf_map.keySet()) {
			Map<String, Double> tmp_tf_idf = new HashMap<String, Double>();
			for(String word: tf_map.get(key).keySet()){
				double tm =filesNum / (double)(idf_map.get(word));
				tf_idf = tf_map.get(key).get(word) * Math.log(tm);
				tmp_tf_idf.put(word, tf_idf);
			}
			tf_idf_map.put(key, tmp_tf_idf);
		}
		StringBuffer stringBuffer = new StringBuffer();
		int counter =0;
		TxtFileReader txtFileReader = new TxtFileReader();
		System.out.println("start");
		stringBuffer.append("id ");
		for(String key: idf_map.keySet()){
			stringBuffer.append(key+" ");
		}
		stringBuffer.append("\n");
		for(String key: tf_idf_map.keySet()){
			stringBuffer.append(key.substring(key.lastIndexOf("\\")+1,key.lastIndexOf("."))+",");
			for(String string: idf_map.keySet()){
				if(!tf_idf_map.get(key).containsKey(string)){
					stringBuffer.append(0+",");
				}else{
					stringBuffer.append(tf_idf_map.get(key).get(string)+",");
				}
			}
			stringBuffer.append("\n");
			counter++;
			if(counter%1000 == 0){
				System.out.println("write into file");
				String filepath = "E:\\result.csv";
				
				try {
					txtFileReader.write(filepath, stringBuffer.toString(), "utf-8", true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stringBuffer = new StringBuffer();
				System.out.println(counter);
				System.out.println("complete");
			}
			
		}
		System.out.println("write into file");
		String filepath = "E:\\result.csv";
		
		try {
			txtFileReader.write(filepath, stringBuffer.toString(), "utf-8", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stringBuffer = new StringBuffer();
		System.out.println(counter);
		System.out.println("complete");
		
	}
	
	
	
	public class Filter extends ContentFilter {
		public String filter(String content) {
			String result = "";
			result = content.substring(content.indexOf("# content #") + 12, content.indexOf("# / content #"));
			return result;
		}
	}

	public static void main(String[] args) {
		TFIDF tfidf = new TFIDF();
		String filepath = "E:\\DownLoad\\FireFox_Download\\dataset_617543\\emotion语料\\corpus\\";
		List<String> fileNames = new ArrayList<String>();
		for (int i = 1; i < 20001; i++) {
			fileNames.add(filepath + i + ".txt");
		}
//		tfidf.statTF_IDF(filepath + "1.txt", fileNames, tfidf.new Filter());
		tfidf.statTF_IDF_multi(fileNames, fileNames);
	}
}
