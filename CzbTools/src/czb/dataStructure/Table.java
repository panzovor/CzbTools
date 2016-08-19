package czb.dataStructure;

import czb.tools.filereader.TxtFileReader;
import utils.CommonTools;

import java.io.IOException;
import java.util.*;

/**
 * Created by E440 on 16-8-15.
 */
public class Table {

    private TxtFileReader  reader = new TxtFileReader();
    public String[] tableTitle = null;

    public Map<String,Integer> titleMap = new HashMap<String, Integer>();

    public List<String[]> tableContent = null;

    public Table(){

    }


    public Table(String[] tableTitle,List<String[]> content){
        this.tableTitle = tableTitle;
        this.tableContent = content;
    }

    public Table(String file,String charset,String seperator){
        buildTable_withTitle(file,charset,seperator);
    }

    public Table(String file,String charset,String seperator,String[] title){
        buildTable_withoutTitle(title,file,charset,seperator);
    }
    
    public Table(String file,String charset,String seperator,String[] title,String[] regex,String[] newstring){
        buildTable_withoutTitle(title,file,charset,seperator,regex,newstring);
    }

    public String[] getTableTitle() {
        return tableTitle;
    }

    public void setTableTitle(String[] tableTitle) {
        this.tableTitle = tableTitle;
        for(int i =0;i< tableTitle.length;i++){
            titleMap.put(tableTitle[i],i);
        }
    }

    public List<String[]> getTableContent() {
        return tableContent;
    }

    public void setTableContent(List<String[]> tableContent) {
        this.tableContent = tableContent;
    }

    /**
     * build the index for the input collum
     * @param index the location of the collum
     * @return the index map
     */
    public Map<String,String[]> buildMap_duplicate(int index){
        Map<String,String[]> result = new HashMap<String,String[]>();
        for(String []tmp: tableContent){
            if(!result.containsKey(tmp[index])){
                result.put(tmp[index],tmp);
            }
        }
        return result;
    }

    public Table duplicateCombineTheIndexCollum(int uniqueId,List<Integer> collumIndexs,String combineSeperator){
        Map<String,List<String[]>> map = buildMap(uniqueId);
        List<String[]> content = new ArrayList<String[]>();
        for(String string: map.keySet()){
            String[] newLine = new String[tableTitle.length];
            for(String[] tmp: map.get(string)){
                for(int i =0;i< tmp.length;i++){
                    if(collumIndexs.contains(i)){
                        newLine[i]+= tmp[i]+combineSeperator;
                    }else{
                        newLine[i] = tmp[i];
                    }
                }
            }
            content.add(newLine);
        }
        this.tableContent = content;
        return this;
    }

    /**
     *build a view(dataStructure) from this dataStructure with selected collums
     * @param collums selected collums
     * @return
     */
    public Table buildView(String[] collums ){
//        String[] title = (String[])collums.toArray();
        List<String[]>content  =new ArrayList<String[]>();
        for(String[] tmp: tableContent){
            String[] mid = new String[collums.length];
            for(int i =0;i<mid.length;i++){
                mid[i] = tmp[titleMap.get(collums[i])];
            }
            content.add(mid);
        }
        Table table = new Table();
        table.setTableTitle(collums);
        table.setTableContent(content);
        return  table;
    }


    public Map<String,List<String[]>> buildMap(int index){
        Map<String,List<String[]>> result = new HashMap<String,List<String[]>>();
        for(String []tmp: tableContent){
            if(!result.containsKey(tmp[index])){
                result.put(tmp[index],new ArrayList<String[]>());
            }
            result.get(tmp[index]).add(tmp);
        }
        return result;
    }
    
    public Map<String,String[]> buildMap(int index,int collums){
        Map<String,String[]> result = new HashMap<String,String[]>();
        for(String []tmp: tableContent){
            if(!result.containsKey(tmp[index])){
                result.put(tmp[index],tmp);
            }else{
            	result.get(tmp[index])[collums]+=tmp[collums];
            }
        }
        return result;
    }

    /**
     * build the map with select collums(index)
     * value of the map are the split result of the selected collums(collums) seperate by seperator
     * @param index
     * @param collums
     * @param seperator
     * @return
     */
    public Map<String,List<String[]>> buildMap(int index,int collums,String seperator){
        Map<String,List<String[]>> result = new HashMap<String,List<String[]>>();
        for(String []tmp: tableContent){
            if(!result.containsKey(tmp[index])){
                result.put(tmp[index],new ArrayList<String[]>());
            }
            result.get(tmp[index]).add(tmp[collums].split(seperator));
        }
        return result;
    }
    
    public String[] combine(List<String[]> list){
    	List<String> list2 = new ArrayList<String>();
    	for(String[] strings : list){
    		for(String string: strings)
    			list2.add(string);
    	}
    	
    	String[] result = new String[list2.size()];
    	for(int i=0;i< list2.size();i++){
    		result[i] = list2.get(i);
    	}
    	return result;
    }

    /**
     * convert the map to a list
     * the value(a list of string array) will be combined as a string array
     * @param map
     * @return
     */
    public List<String[]> converMap2List(Map<String, List<String[]>> map){
    	List<String[]> result = new ArrayList<String[]>();
    	for(String string: map.keySet()){
    		result.add(combine(map.get(string)));
    	}
    	return result;
    }

    /**
     *convert the map to a list
     * the value is an array of string
     * @param map
     * @return
     */
    public List<String[]> converMap2List_(Map<String, String[]> map){
    	List<String[]> result = new ArrayList<String[]>();
    	for(String string: map.keySet()){
    		result.add(map.get(string));
    	}
    	return result;
    }

    /**
     *left join the left dataStructure and right dataStructure and save it into a new dataStructure
     * @param left left dataStructure
     * @param right right dataStructure(right dataStructure should be duplicated)
     * @param combineIndex1 index of table1's attributes to be compared
     * @param combineIndex2 index of table2's attributes to be compared
     * @return
     */
    public Table leftJoin(Table left,Table right,int combineIndex1,int combineIndex2){
        Table table = new Table();
        String[] title = leftJoinArray(left.getTableTitle(),right.getTableTitle(),combineIndex2);
        List<String[]> newContent = new ArrayList<String[]>();
        Map<String,String[]> rightTable = right.buildMap_duplicate(combineIndex2);
        
//        System.out.println(rightTable.size());
        for(String[] content: left.getTableContent()){
            if(!rightTable.keySet().contains(content[combineIndex1])){
                continue;
            }

            String [] tmp = new String[title.length];
            for(int i =0;i< content.length;i++){
                tmp[i] = content[i];
            }
            String[] tmp_right = rightTable.get(tmp[combineIndex1]);
            if(tmp_right == null){
            	int count=0;
                for(int i=0;i< right.tableTitle.length-2;i++,count++){
                    tmp[content.length+count] = "null,";
                }
                tmp[tmp.length-1] = "null\n";
            }else{
            	int count=0;
                for(int i=0;i< tmp_right.length;i++){
                    if(i == combineIndex2) {
                    	continue;
                    }
                    else {
//                    	if(content.length ==7)
//                    		System.out.println(printArray(content));
                        tmp[content.length+count] = tmp_right[i];
                        count++;
                    }
                }
            }

            newContent.add(tmp);
        }
        table.setTableTitle(title);
        table.setTableContent(newContent);
        return table;
    }

    /**
     *left join the left dataStructure and right dataStructure and save it into this dataStructure
     * @param left left dataStructure
     * @param right right dataStructure(right dataStructure should be duplicated)
     * @param combineIndex1 index of table1's attributes to be compared
     * @param combineIndex2 index of table2's attributes to be compared
     * @return
     */
    public Table leftJoinIntoItself(Table left,Table right,int combineIndex1,int combineIndex2){
        String[] title = leftJoinArray(left.getTableTitle(),right.getTableTitle(),combineIndex2);
        List<String[]> newContent = new ArrayList<String[]>();
        Map<String,String[]> rightTable = right.buildMap_duplicate(combineIndex2);
        for(String[] content: left.getTableContent()){
            String [] tmp = new String[title.length];
            for(int i =0;i< content.length;i++){
                tmp[i] = content[i];
            }
            String[] tmp_right = rightTable.get(tmp[combineIndex1]);
            if(tmp_right == null){
                for(int i=0;i< tmp_right.length-1;i++){
                    tmp[content.length+i] = "null";
                }
            }else{
            	int count=0;
                for(int i=0;i< tmp_right.length;i++){
                    if(i == combineIndex2) continue;
                    else {
                        tmp[content.length+count] = tmp_right[i];
                        count++;
                    }
                }
            }

            newContent.add(tmp);
        }
        this.setTableTitle(title);
        this.setTableContent(newContent);
        return this;
    }

    public String[] leftJoinArray(String[] left,String[] right,int combineIndex2){
        String[] result = new String[left.length+right.length-1];
        for(int i=0;i<left.length;i++){
            result[i] = left[i];
        }
        int count=0;
        for(int i=0;i<right.length;i++,count++){
            if(i == combineIndex2){
            	count--;
                continue;
            }
            result[count+left.length] = right[i];
        }
        return result;
    }

    /**
     *
     * @param left left dataStructure(left dataStructure should be duplicated)
     * @param right right dataStructure
     * @param combineIndex1 index of table1's attributes to be compared
     * @param combineIndex2 index of table2's attributes to be compared
     * @return
     */
    public Table rightJoin(Table left,Table right,int combineIndex1,int combineIndex2){
        return leftJoin(right,left,combineIndex2,combineIndex1);
    }

    public String getTitle(String seperator){
        StringBuffer stringBuffer = new StringBuffer();
        for(String string: tableTitle){
            stringBuffer.append(string+seperator);
        }
        stringBuffer.deleteCharAt(stringBuffer.length()-1);
        return stringBuffer.toString();
    }

    public void setTableTitle(String title,String seperator){
        tableTitle = title.split(seperator);
    }

    public String getContent(String seperator){
        StringBuffer stringBuffer = new StringBuffer();
        for(String[] strings : tableContent){
            for(String string : strings){
                stringBuffer.append(string+seperator);
            }
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
            stringBuffer.append("/n");
        }
        return stringBuffer.toString();
    }

    public void saveTable(String file,String charset){
        String seperator = ",";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getTitle(seperator)+"\n");
        stringBuffer.append(getContent(seperator));
        try {
            CommonTools.fileReader.write(file, stringBuffer.toString(), charset, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildTable_withTitle(String file,String charset,String seperator){
        try {
            List<String[]> list= reader.readWordsWithSeperatorWithLength(file,seperator,charset,0);
            System.out.println(list.size());
            setTableTitle(list.get(0));
            list.remove(0);
            setTableContent(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildTable_withoutTitle(String[] title,String file,String charset,String seperator){
        List<String[]> list= null;
        try {
            list = reader.readWordsWithSeperatorWithLength(file,seperator,charset,title.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTableTitle(title);
        setTableContent(list);
    }
    
    public void buildTable_withoutTitle(String[] title,String file,String charset,String seperator,String[] regex,String[]newstring){
        List<String[]> list= null;
        try {
            list = reader.readWordsWithSeperator_filter(file,seperator,charset,regex,newstring);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTableTitle(title);
        setTableContent(list);
    }

    public List<String> selectSingleCollum(String collum){
        List<String> result = new ArrayList<String>();
        for(String[] string: tableContent){
            result.add(string[titleMap.get(collum)]);
        }
        return result;
    }
    
    public String printTable(){
    	StringBuffer stringBuffer= new StringBuffer();
    	for(int i=0;i< tableTitle.length-1;i++){
    		stringBuffer.append(tableTitle[i]+",");
    	}
    	stringBuffer.append(tableTitle[tableTitle.length-1]+"\n");
    	for(String[] strings : tableContent){
    		for(int i=0;i< strings.length-1;i++){
        		stringBuffer.append(strings[i]+",");
        	}
    		stringBuffer.append(strings[strings.length-1]+"\n");
    	}
    	return stringBuffer.toString();
    	
    }
    
    public String printArray(String[] temp){
    	StringBuffer stringBuffer= new StringBuffer();
    	for(int i=0;i< temp.length-1;i++){
    		stringBuffer.append(temp[i]+",");
    	}
    	stringBuffer.append(temp[temp.length-1]+"\n");
    	return stringBuffer.toString();
    }
    
    public String printTitleAndLine(int line){
    	StringBuffer stringBuffer= new StringBuffer();
    	for(int i=0;i< tableTitle.length-1;i++){
    		stringBuffer.append(tableTitle[i]+",");
    	}
    	stringBuffer.append(tableTitle[tableTitle.length-1]+"\n");
    	int counter=0;
    	for(String[] strings : tableContent){
    		for(int i=0;i< strings.length-1;i++){
        		stringBuffer.append(strings[i]+",");
        	}
    		stringBuffer.append(strings[strings.length-1]+"\n");
    		counter++;
    		if(counter>= line)
    			break;
    	}
    	return stringBuffer.toString();
    }

    public String printTitleAndLastNLine(int line){
    	StringBuffer stringBuffer= new StringBuffer();
    	for(int i=0;i< tableTitle.length-1;i++){
    		stringBuffer.append(tableTitle[i]+",");
    	}
    	stringBuffer.append(tableTitle[tableTitle.length-1]+"\n");
    	int counter=0;
    	for(int k=tableContent.size()-1;k>=0;k--){
    		String[] strings = tableContent.get(k);
    		for(int i=0;i< strings.length-1;i++){
        		stringBuffer.append(strings[i]+",");
        	}
    		stringBuffer.append(strings[strings.length-1]+"\n");
    		counter++;
    		if(counter>= line)
    			break;
    	}
    	return stringBuffer.toString();
    }
    
    public void demo(String file1,String file2,String resultsave){
        String[] title1 ={};
        String[] title2 ={};
        String charset="utf-8";
        String seperateor=",";
        Table table = new Table(file1,charset,seperateor,title1);
        Table table1 = new Table(file2,charset,seperateor,title2);
        Table result =  table.leftJoin(table,table1,0,0);
        String[] collums ={"id","content","stats","so_on"};
        result = result.buildView(collums);
        result.saveTable(resultsave,charset);
    }

}
