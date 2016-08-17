package czb.train.attributeExtrator;

import czb.tools.filereader.TxtFileReader;
import utils.CommonTools;

import java.io.IOException;
import java.util.*;

/**
 * Created by E440 on 16-8-15.
 */
public class Tfidf {

    private Map<String,Integer> wordBag = new HashMap<String, Integer>();

    private Map<Integer,Integer> tf = new HashMap<Integer,Integer>();

    private Map<Integer,Double> idf = new HashMap<Integer, Double>();

    private Map<String,Double> tfidf = new HashMap<String, Double>();

    public Map<String, Integer> getWordBag() {
        return wordBag;
    }

    public void setWordBag(Map<String, Integer> wordBag) {
        this.wordBag = wordBag;
    }

    public Map<Integer, Integer> getTf() {
        return tf;
    }

    public void setTf(Map<Integer, Integer> tf) {
        this.tf = tf;
    }

    public Map<Integer, Double> getIdf() {
        return idf;
    }

    public void setIdf(Map<Integer, Double> idf) {
        this.idf = idf;
    }

    public void tfidf(List<String[]> datas){
        int i=0;
        Set<String> appeace = new HashSet<String>();
        for(String[] data: datas){
            for(String word:data){
            	if(word.trim().equals("")){
            		continue;
            	}
                if(wordBag.containsKey(word)){
                    int tmp = tf.get(i);
                    tf.put(i,++tmp);
                }else{
                    i++;
                    wordBag.put(word,i);
                    tf.put(i,1);
                }
                if(!idf.containsKey(word)){
                    idf.put(wordBag.get(word),1.0);
                }else if(!appeace.contains(word)){
                    double tmmp = idf.get(wordBag.get(word));
                    idf.put(wordBag.get(word),++tmmp);
                }
                appeace.add(word);
            }
            appeace.clear();
        }
        for(Integer index : idf.keySet()){
            idf.put(index,Math.abs(Math.log((double) datas.size() / idf.get(index))));
        }
        getTfidf();
    }

    public void clear(){
        wordBag.clear();
        tf.clear();
        idf.clear();
        tfidf.clear();
    }

    public void loadTfIdfData(String file){
        TxtFileReader txtFileReader = new TxtFileReader();
        try {
            clear();
            List<String[]> content = txtFileReader.readWordsWithSeperatorWithLength(file,",","utf-8",4);
            for(int i=0;i< content.size();i++){
                String[] tmp = content.get(i);
                wordBag.put(tmp[0],i);
                tf.put(i,Integer.parseInt(tmp[1]));
                idf.put(i,Double.parseDouble(tmp[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String,Double> getTfidf(){
        tfidf = new HashMap<String, Double>();
        for(String string: wordBag.keySet()){
            tfidf.put(string,tf.get(wordBag.get(string))*idf.get(wordBag.get(string)));
        }
        return tfidf;
    }

    public Map<String,Double> tfidfFilter(int minTf,double mintfidf){
        Map<String,Double> tfidf = getTfidf();
        for(String string : wordBag.keySet()){
            if(tf.get(wordBag.get(string))< minTf && tfidf.get(string)<mintfidf){
                if(tfidf.containsKey(string)){
                    tfidf.remove(string);
                }
            }
        }
        return tfidf;
    }

    public Map<String,Double> tfidfFilterFirstN(int minTf,int firstN){
        Map<String,Double> tfidf = tfidfFilter(minTf,0);
        tfidf = CommonTools.sortMap(tfidf,firstN);
        return tfidf;
    }

    public Map<String,Double> tfidfFilterFirstN(int minTf,double firstN){
        int num = (int)(tfidf.size()* firstN);
        return tfidfFilterFirstN(minTf,num);
    }

    public String tfidfToString(Map<String,Double> map){
        StringBuffer stringBuffer = new StringBuffer();
        for(String string : map.keySet() ){
                stringBuffer.append(string+",");
                stringBuffer.append(tfidf.get(wordBag.get(string))+"\n");
        }
        return stringBuffer.toString();
    }

    public String printTfidf(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("word,tf,idf,tf-idf\n");
        for(String word : wordBag.keySet()){
            stringBuffer.append(word+",");
            stringBuffer.append(tf.get(wordBag.get(word))+",");
            stringBuffer.append(idf.get(wordBag.get(word))+",");
            stringBuffer.append(tf.get(wordBag.get(word))* idf.get(wordBag.get(word))+"\n");
        }
        return stringBuffer.toString();
    }




}


