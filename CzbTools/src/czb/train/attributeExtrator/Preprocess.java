package czb.train.attributeExtrator;

import czb.dataStructure.Table;
import czb.tools.filereader.TxtFileReader;
import utils.CommonTools;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by E440 on 16-8-15.
 */
public class Preprocess {

    private Tfidf tfidf = new Tfidf();

    private String charset = "utf-8";

    public List<String[]> seperate(List<String> content, String seperator) {
        List<String[]> result = new ArrayList<String[]>();
        for (String string : content) {
            result.add(string.split(seperator));
        }
        return result;
    }

    public void preprocess_label(String file, String save) {

        try {
            String[] regex = {"(辽宁|吉林|黑龙江).+\n", "(河北|山西|内蒙古|北京|天津).+\n", "(山东|江苏|安徽|浙江|台湾|福建|江西|上海).+\n", "(河南|湖北|湖南).+\n", "(广东|广西|海南|香港|澳门).+\n", "(云南|重庆|贵州|四川|西藏).+\n", "(新疆|陕西|宁夏|青海|甘肃).+\n", "((海外|None).+\n)|((海外|None).+)"};
            String[] newString = {"东北\n", "华北\n", "华东\n", "华中\n", "华南\n", "西南\n", "西北\n", "境外\n"};
            String content = CommonTools.fileReader.readContent(file);
            for (int i = 0; i < regex.length; i++) {
                content = content.replaceAll(regex[i], newString[i]);
            }
            CommonTools.fileReader.write(save, content, "utf-8", false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void preprocess_status(String file, String save) {
        try {
            String[] regex = {"http(s)?://[^\\s]*", "\\d{3}-\\d{8}|\\d{4}-\\{7,8}", "\\d{11}"};
            String[] newString = {"", "", ""};
            String content = CommonTools.fileReader.readContent(file);
            for (int i = 0; i < regex.length; i++) {
                content = content.replaceAll(regex[i], newString[i]);
            }
            CommonTools.fileReader.write(save, content, "utf-8", false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void preprocess_label_status(String status, String label, String saveStatus, String saveLabel) {
        preprocess_label(label, saveLabel);
        preprocess_status(status, saveStatus);
    }

    public void combineLableAndStatus(String labelfile, String statusfile, String savefile) {
        String[] title1 = {"id", "retweet count", "retweet count", "source", "time", "content"};
        String[] title2 = {"id", "gender", "age", "location"};

        Table table = new Table(statusfile, charset, ",", title1);
        Table table1 = new Table(labelfile, charset, "\\|\\|", title2);
        Table result = table.leftJoin(table, table1, 0, 0);
        result.saveTable(savefile, charset);
    }

    public Map<String, Double> calculateTfidf(String file1, String savefile) {
        Table result = new Table(file1, charset, ",");
        Map<String, List<String[]>> map = result.buildMap(result.titleMap.get("location"), result.titleMap.get("content"), " ");
        List<String[]> tmp = result.converMap2List(map);
        tfidf.tfidf(tmp);
        tfidf.getTf();
        tfidf.getIdf();
        tfidf.getWordBag();
        Map<String, String[]> map2 = result.buildMap(result.titleMap.get("id"), result.titleMap.get("content"));
        List<String[]> tmp11 = result.converMap2List_(map2);
        Map<String, Double> map_ = tfidf.tfidfFilterFirstN(5, 0.5);
        String tfidfinfo = tfidf.tfidfToString(map_);
//        System.out.println(tfidfinfo);
        try {
            CommonTools.fileReader.write(savefile, tfidfinfo, "utf-8", false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map_;
    }

    public String makeArffFileWithTfidf(String file, Map<String, Double> tfidf, String[] title, String saveFile) {
        String seperator = ",";
        Table table = new Table(file, charset, seperator);
        if (title == null) {
            title = table.getTableTitle();
        }
        StringBuffer stringBuffer = new StringBuffer();
        String titleString = CommonTools.arrayToString(title, seperator);
        StringBuffer replaceString = new StringBuffer();
        for (int i = 0; i < tfidf.size(); i++) {
            replaceString.append("att" + i + seperator);
        }
        titleString = titleString.replace("content,", replaceString.toString());
        stringBuffer.append(titleString);
        for (String[] tmp : table.getTableContent()) {
            for (int i = 0; i < title.length - 1; i++) {
                String string = title[i];
                if (!string.equals("content"))
                    stringBuffer.append(tmp[table.titleMap.get(string)] + seperator);
                else {
                    String[] words = string.split(" ");
                    for (String word : tfidf.keySet()) {
                        if (CommonTools.contains(words, word)) {
                            stringBuffer.append(tfidf.get(word) + seperator);
                        } else {
                            stringBuffer.append("0,");
                        }
                    }
                }
            }
            stringBuffer.append(tmp[table.titleMap.get(title[title.length - 1])] + "\n");
        }
        try {
            CommonTools.fileReader.write(saveFile, stringBuffer.toString(), charset, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    static Map<String, Double> tfidf_map = new HashMap<String, Double>();

    public static void train() {
        Preprocess preprocess = new Preprocess();
        String fileRoot = "G:\\Czb\\smpCup\\train\\";
        String file1 = fileRoot + "train_status.txt";
        String file2 = fileRoot + "train_labels.txt";
        String save_label = fileRoot + "train_labels_filter.txt";
        String save_status = fileRoot + "train_status_filter.txt";
        preprocess.preprocess_label_status(file1, file2, save_status, save_label);

        String resultsave = fileRoot + "train_labels_status.txt";
        preprocess.combineLableAndStatus(save_status, save_label, resultsave);

        String tfidfFile = fileRoot + "tfidf_info_label_status.txt";
        tfidf_map = preprocess.calculateTfidf(resultsave, tfidfFile);

        String arffFile_train = fileRoot + "arffTrain_tfidf_label_status.txt";
        preprocess.makeArffFileWithTfidf(resultsave, tfidf_map, null, arffFile_train);
    }

    public static void test() {
        Preprocess preprocess = new Preprocess();
        String fileRoot = "G:\\Czb\\smpCup\\test\\";
        String file1 = fileRoot + "test_status.txt";
        String file2 = fileRoot + "test_labels.txt";
        String save_label = fileRoot + "test_labels_filter.txt";
        String save_status = fileRoot + "test_status_filter.txt";
        preprocess.preprocess_label_status(file1, file2, save_status, save_label);

        String resultsave = fileRoot + "train_labels_status.txt";
        preprocess.combineLableAndStatus(save_status, save_label, resultsave);

//        String tfidfFile = fileRoot+"tfidf_info_label_status.txt";
//        Map<String,Double> tfidf = preprocess.calculateTfidf(resultsave,tfidfFile);

        String arffFile_train = fileRoot + "arffTrain_tfidf_label_status.txt";
        preprocess.makeArffFileWithTfidf(resultsave, tfidf_map, null, arffFile_train);
    }


    public static void main(String[] args) {

        train();
        test();
    }


}
