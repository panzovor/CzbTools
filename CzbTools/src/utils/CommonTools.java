package utils;

import czb.tools.filereader.TxtFileReader;

import java.util.*;

/**
 * Created by E440 on 16-8-17.
 */
public class CommonTools {

    public static TxtFileReader fileReader = new TxtFileReader();

    public static String arrayToString(String[] strings,String seperator){
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i< strings.length-1;i++){
            String string  = strings[i];
            stringBuffer.append(string+seperator);
        }
        stringBuffer.append(strings[strings.length-1]);
        return stringBuffer.toString();
    }

    public static boolean contains(String[] string,String word){
        for(String s: string){
            if(s.equals(word))
                return true;
        }
        return false;
    }

    public static Map sortMap(Map oldMap) {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(oldMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(Map.Entry<String, Integer> arg0,
                               Map.Entry<String, Integer> arg1) {
                return arg0.getValue() - arg1.getValue();
            }
        });
        Map newMap = new LinkedHashMap();
        for (int i = 0; i < list.size(); i++) {
            newMap.put(list.get(i).getKey(), list.get(i).getValue());
        }
        return newMap;
    }

    public static Map sortMap(Map oldMap,int fisrtN) {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(oldMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(Map.Entry<String, Integer> arg0,
                               Map.Entry<String, Integer> arg1) {
                return arg0.getValue() - arg1.getValue();
            }
        });
        Map newMap = new LinkedHashMap();
        int num = fisrtN;
        if(list.size()< fisrtN){
            num = list.size();
        }
        for (int i = 0; i < num; i++) {
            newMap.put(list.get(i).getKey(), list.get(i).getValue());
        }
        return newMap;
    }

}
