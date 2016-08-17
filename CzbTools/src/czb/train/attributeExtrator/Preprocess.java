package czb.train.attributeExtrator;

import czb.dataStructure.Table;
import czb.tools.filereader.TxtFileReader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by E440 on 16-8-15.
 */
public class Preprocess {
	
	private Tfidf tfidf = new Tfidf();

    private String charset="utf-8";

	private TxtFileReader txtFileReader = new TxtFileReader();
	
    public List<String[]> seperate(List<String> content,String seperator){
        List<String[]> result =new ArrayList<String[]>();
        for(String string :    content){
            result.add(string.split(seperator));
        }
        return result;
    }
    
    public void preprocess_label(String file,String save){
    	
    	try {
    		String[] regex ={"(辽宁|吉林|黑龙江).+\n","(河北|山西|内蒙古|北京|天津).+\n","(山东|江苏|安徽|浙江|台湾|福建|江西|上海).+\n","(河南|湖北|湖南).+\n","(广东|广西|海南|香港|澳门).+\n","(云南|重庆|贵州|四川|西藏).+\n","(新疆|陕西|宁夏|青海|甘肃).+\n","((海外|None).+\n)|((海外|None).+)"};
    		String[] newString ={"东北\n","华北\n","华东\n","华中\n","华南\n","西南\n","西北\n","境外\n"};
    		String content = txtFileReader.readContent(file);
    		for(int i=0;i< regex.length;i++){
    			content = content.replaceAll(regex[i], newString[i]);
    		}
			txtFileReader.write(save, content, "utf-8", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    public void preprocess_status(String file,String save){
    	try {
    		String[] regex ={"http(s)?://[^\\s]*","\\d{3}-\\d{8}|\\d{4}-\\{7,8}","\\d{11}"};
    		String[] newString ={"","",""};
    		String content = txtFileReader.readContent(file);
    		for(int i=0;i< regex.length;i++){
    			content = content.replaceAll(regex[i], newString[i]);
    		}
			txtFileReader.write(save, content, "utf-8", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }

    public void preprocess_label_status(){
        String file1 ="G:\\Czb\\smpCup\\train\\train_status.txt";
        String file2 ="G:\\Czb\\smpCup\\train\\train_labels.txt";
        String save_label ="G:\\Czb\\smpCup\\train\\train_labels_filter.txt";
        String save_status="G:\\Czb\\smpCup\\train\\train_status_filter.txt";

        preprocess_label(file2, save_label);
        preprocess_status(file1, save_status);
    }

    public void combineLableAndStatus(String labelfile,String statusfile,String savefile){
        String[] title1 ={"id","retweet count","retweet count","source","time","content"};
        String[] title2 ={"id","gender","age","location"};

        Table table = new Table(statusfile,charset,",",title1);
        Table table1 = new Table(labelfile,charset,"\\|\\|",title2);
        Table result =  table.leftJoin(table,table1,0,0);
        result.saveTable(savefile,charset);
    }

    public Map<String,Double> calculateTfidf(String file1,String savefile){
        Table result = new Table(file1,charset,",");
        Map<String, List<String[]>> map = result.buildMap(result.titleMap.get("location"),result.titleMap.get("content")," ");
        List<String[]> tmp = result.converMap2List(map);
        tfidf.tfidf(tmp);
        tfidf.getTf();
        tfidf.getIdf();
        tfidf.getWordBag();
        Map<String, String[]> map2 = result.buildMap(result.titleMap.get("id"),result.titleMap.get("content"));
        List<String[]> tmp11 = result.converMap2List_(map2);
        Map<String,Double> map_ = tfidf.tfidfFilterFirstN(5,0.5);
        String tfidfinfo = tfidf.tfidfToString(map_);
//        System.out.println(tfidfinfo);
        TxtFileReader fileReader= new TxtFileReader();
        try {
            fileReader.write(savefile, tfidfinfo, "utf-8", false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map_;
    }

    public void makeArffFileWith(String file)

    public static void main(String[] args){
    	Preprocess preprocess =new Preprocess();
    	String save_label ="G:\\Czb\\smpCup\\train\\train_labels_filter.txt";
    	String save_status="G:\\Czb\\smpCup\\train\\train_status_filter.txt";
    	String resultsave ="G:\\Czb\\smpCup\\train\\train_labels_status.txt";
    	String filePath ="G:\\Czb\\smpCup\\train\\tfidf_info_label_status.txt";

        preprocess.combineLableAndStatus(save_status, save_label, resultsave);

        String tfidfFile = "G:\\Czb\\smpCup\\train\\tfidf_info_label_status.txt";;
        Map<String,Double> tfidf = preprocess.calculateTfidf(resultsave,tfidfFile);

    }



}
