package czb.tools.wordsegment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sun.jna.Library;
import com.sun.jna.Native;

import czb.tools.filereader.TxtFileReader;

public class Nlpir {
	
	public Nlpir(){
		String argu = "";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "GBK";
		
		int charset_type = 1;
		// int charset_type = 0;
		// 调用printf打印信息
		int init_flag =0;
		try {
			init_flag = CLibrary.Instance.NLPIR_Init(argu
					.getBytes(system_charset), charset_type, "0"
					.getBytes(system_charset));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (0 == init_flag) {
			System.err.println("初始化失败！");
			return;
		}
	}
	
	private List<String > stopWords = new ArrayList<String>();

	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				System.getProperty("user.dir")+"\\NLPIR", CLibrary.class);

		// printf函数声明
		public int NLPIR_Init(byte[] sDataPath, int encoding,
                              byte[] sLicenceCode);

		/**
		 * 语句分词
		 * @param sSrc 待分词语句
		 * @param bPOSTagged 是否返回词性0为不返回，非0为返回
		 * @return
		 */
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		/**
		 * 提取关键词
		 * @param sLine （待分词语段）
		 * @param nMaxKeyLimit 关键词最大数量
		 * @param bWeightOut 是否返回权重
		 * @return
		 */
		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
                                        boolean bWeightOut);

		/**
		 * 统计词语数量（词语的总数量而非单个词语出现的数量）
		 * @param aParagraph
		 * @return
		 */
		public int NLPIR_GetParagraphProcessAWordCount(String aParagraph);
		
		/**
		 * 导入用户自定义词典
		 * @param aFileName 用户自定义词典路径
		 * @return
		 */
		public int NLPIR_ImportUserDict(String aFileName, boolean bOverwrite);
		
		/**
		 * 分词包含词语出现的次序以及位置(返回为一个结构体，不确定能用)
		 * @param aParagraph 待分词语句
		 * @param nCount 总词语数
		 * @param userDict 是否导入用户词典
		 */
//		public result_t[] NLPIR_ParagraphProcessA(String aParagraph, int nCount, boolean userDict);
//
		/**
		 * 统计文本中词语的词频
		 * @param sText 文本
		 * @return
		 */
		public String NLPIR_WordFreqStat(String sText);
		
		/**
		 * 统计文件中词语的词频
		 * @param sFilename 文件路径
		 * @return
		 */
		public String NLPIR_FileWordFreqStat(String sFilename);
		
		/**
		 * 对过大的分词进行更细粒度的切分（如中华人民共和国 切分为 中华；人民；共和国）
		 * @param sLine 待切分的词
		 * @return
		 */
		public String NLPIR_FinerSegment(String sLine);
		
		/**
		 * 获取英文单词原型
		 * @param sWord 英文单词
		 * @return 英文单词原型
		 */
		public String NLPIR_GetEngWordOrign(String sWord);
		
		/**
		 * 选择标注集
		 * ICT_POS_MAP_FIRST 计算所一级标注集
		 * ICT_POS_MAP_SECOND 计算所二级标注集
		 * PKU_POS_MAP_SECOND 北大二级标注集
		 * PKU_POS_MAP_FIRST 北大一级标注集
		 * @param nPOSmap
		 * @return
		 */
		public int NLPIR_SetPOSmap(int nPOSmap);
		
		/**
		 * 提取文本的指纹（类似md5）
		 * @param sLine
		 * @return
		 */
		public long NLPIR_FingerPrint(String sLine);
		
		/**
		 * 从文本中发现新词
		 * @param sTextFile 文本
		 * @param nMaxKeyLimit 新词最大数量
		 * @return
		 */
		public String NLPIR_GetFileNewWords(String sTextFile, int nMaxKeyLimit, boolean bWeightOut);
		
		/**
		 * 导入黑名单词典（即不会出现的词）
		 * @param sFilename 词典路径
		 * @return
		 */
		public int NLPIR_ImportKeyBlackList(String sFilename);
		
		/**
		 * 从文本中获取关键词
		 * @param sTextFile 文本
		 * @param nMaxKeyLimit 关键词最大数量
		 * @param bWeightOut 是否输出权重
		 * @return 例："科学发展观/23.80/12#宏观经济/12.20/21"
		 */
		public String NLPIR_GetFileKeyWords(String sTextFile, int nMaxKeyLimit, boolean bWeightOut);
		
		/**
		 * 从词典中删除某个词
		 * @param sWord 词
		 * @return 失败返回-1 否则成功
		 */
		public int NLPIR_DelUsrWord(String sWord);
		
		/**
		 * 保存用户词典到磁盘中
		 * @return 成功返回1 否者返回0
		 */
		public int NLPIR_SaveTheUsrDic();
		
		/**
		 * 保存新词到用户词典中
		 * @param sWord 新词
		 * @return 成功返回1 ， 失败返回0
		 */
		public int NLPIR_AddUserWord(String sWord);
		
		
		/**
		 * 退出并释放所有资源
		 */
		public void NLPIR_Exit();
	}

	public static String transString(String aidString, String ori_encoding,
			String new_encoding) {
		try {
			return new String(aidString.getBytes(ori_encoding), new_encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 语句分词
	 * @param sSrc 待分词语句
	 * @param bPOSTagged 是否返回词性0为不返回，非0为返回
	 * @return
	 */
	public List<String> NLPIR_ParagraphProcess(String sSrc, int bPOSTagged,boolean stopWord){
		String result =CLibrary.Instance.NLPIR_ParagraphProcess(sSrc, bPOSTagged);
		result = result.replace("\n", "");
		String[] tmp = result.split(" ");
		List<String> res = new ArrayList<String>();
        String string ="";
		for(int i=0;i< tmp.length;i++){
            string =  tmp[i];
			if(stopWord &&stopWords.contains(string)){
				continue;
			}
			if(string.equals("")){
				continue;
			}
            if(string.contains("[") && tmp[i+1].contains("]")){
                string = string.substring(1);
                string +=" "+tmp[i+1].substring(0,tmp[i+1].length()-1);
                i+=2;
            }
			res.add(string.trim());
		}
		return res; 
	}

	/**
	 * 提取关键词
	 * @param sLine （待分词语段）
	 * @param nMaxKeyLimit 关键词最大数量
	 * @param bWeightOut 是否返回权重
	 * @return
	 */
	public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
			boolean bWeightOut){
		return CLibrary.Instance.NLPIR_GetKeyWords(sLine, nMaxKeyLimit, bWeightOut);
	}

	/**
	 * 统计词语数量（词语的总数量而非单个词语出现的数量）
	 * @param aParagraph
	 * @return
	 */
	public int NLPIR_GetParagraphProcessAWordCount(String aParagraph){
		return CLibrary.Instance.NLPIR_GetParagraphProcessAWordCount(aParagraph);
	}
	
	/**
	 * 导入用户自定义词典
	 * @param aFileName 用户自定义词典路径
	 * @return
	 */
	public int NLPIR_ImportUserDict(String aFileName,boolean overwrite){
		return  CLibrary.Instance.NLPIR_ImportUserDict(aFileName,overwrite);
	}
	
	/**
	 * 分词包含词语出现的次序以及位置(返回为一个结构体，不确定能用)
	 * @param aParagraph 待分词语句
	 * @param nCount 总词语数
	 * @param userDict 是否导入用户词典
	 */
//	public result_t[] NLPIR_ParagraphProcessA(String aParagraph, int nCount, boolean userDict){
//		return CLibrary.Instance.NLPIR_ParagraphProcessA(aParagraph, nCount, userDict);
//	}
//
	
	/**
	 * 统计文本中词语的词频
	 * @param sText 文本
	 * @return
	 */
	public Map<String, Integer> NLPIR_WordFreqStat(String sText,boolean stopword){
//        System.out.println("统计词频");
		List<String> result = NLPIR_ParagraphProcess(sText,0,stopword);
//        System.out.println("开始统计："+result.size());
		Map<String, Integer> map = new HashMap<String, Integer>();
		int k =0;
		for(String string: result){
			if(map.containsKey(string)){
				k= map.get(string);
				map.put(string,++k);
			}else{
				map.put(string, 1);
			}
		}
		return map;
	}

    /**
     * 统计分词结果的词频
     * @param list 分词结果
     * @return 分词结果词频的键值对
     */
    public Map<String,Integer> countWords(List<String> list){
        Map<String, Integer> map = new HashMap<String, Integer>();
        int k =0;
        for(String string: list){
            if(map.containsKey(string)){
                k= map.get(string);
                map.put(string,++k);
            }else{
                map.put(string, 1);
            }
        }
        return map;
    }


	/**
	 * 统计文件中词语的词频
	 * @param sFilename 文件路径
	 * @return
	 */
	public String NLPIR_FileWordFreqStat(String sFilename){
		return CLibrary.Instance.NLPIR_FileWordFreqStat(sFilename);
	}

	/**
	 * 对过大的分词进行更细粒度的切分（如中华人民共和国 切分为 中华；人民；共和国）
	 * @param sLine 待切分的词
	 * @return
	 */
	public String NLPIR_FinerSegment(String sLine){
		return CLibrary.Instance.NLPIR_FinerSegment(sLine);
	}
	
	/**
	 * 获取英文单词原型
	 * @param sWord 英文单词
	 * @return 英文单词原型
	 */
	public String NLPIR_GetEngWordOrign(String sWord){
		return CLibrary.Instance.NLPIR_GetEngWordOrign(sWord);
	}
	
	/**
	 * 选择标注集
	 * ICT_POS_MAP_FIRST 计算所一级标注集
	 * ICT_POS_MAP_SECOND 计算所二级标注集
	 * PKU_POS_MAP_SECOND 北大二级标注集
	 * PKU_POS_MAP_FIRST 北大一级标注集
	 * @param nPOSmap
	 * @return
	 */
	public int NLPIR_SetPOSmap(int nPOSmap){
		return CLibrary.Instance.NLPIR_SetPOSmap(nPOSmap);
	}
	
	/**
	 * 提取文本的指纹（类似md5）
	 * @param sLine
	 * @return
	 */
	public long NLPIR_FingerPrint(String sLine){
		return CLibrary.Instance.NLPIR_FingerPrint(sLine);
	}
	
	/**
	 * 从文本中发现新词
	 * @param sTextFile 文本
	 * @param nMaxKeyLimit 新词最大数量
	 * @return
	 */
	public String NLPIR_GetFileNewWords(String sTextFile,int nMaxKeyLimit, boolean bWeightOut){
		return CLibrary.Instance.NLPIR_GetFileNewWords(sTextFile,nMaxKeyLimit, bWeightOut);
	}
	
	/**
	 * 导入黑名单词典（即不会出现的词）
	 * @param sFilename 词典路径
	 * @return
	 */
	public int NLPIR_ImportKeyBlackList(String sFilename,String charset){
		TxtFileReader reader = new TxtFileReader();
		try {
			stopWords = reader.readLines(sFilename,charset);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CLibrary.Instance.NLPIR_ImportKeyBlackList(sFilename);
	}

    /**
     * 导入黑名单词典（即不会出现的词）
     * @param sFilename 词典路径
     * @param overwrite 是否覆盖
     * @return
     */
    public int NLPIR_ImportKeyBlackList(String sFilename,String charset,boolean overwrite){
        TxtFileReader reader = new TxtFileReader();
        try {
            if(overwrite)
            stopWords = reader.readLines(sFilename,charset);
            else
                stopWords.addAll( reader.readLines(sFilename,charset));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return CLibrary.Instance.NLPIR_ImportKeyBlackList(sFilename);
    }


	/**
	 * 从文本中获取关键词
	 * @return 例："科学发展观/23.80/12#宏观经济/12.20/21"
	 */
	public String NLPIR_GetFileKeyWords(String sFilename, int nMaxKeyLimit, boolean bWeigtOut){
		return CLibrary.Instance.NLPIR_GetFileKeyWords(sFilename, nMaxKeyLimit,bWeigtOut);
	}
	
	/**
	 * 从词典中删除某个词
	 * @param sWord 词
	 * @return 失败返回-1 否则成功
	 */
	public int NLPIR_DelUsrWord(String sWord){
		return CLibrary.Instance.NLPIR_DelUsrWord(sWord);
	}
	
	/**
	 * 保存用户词典到磁盘中
	 * @return 成功返回1 否者返回0
	 */
	public int NLPIR_SaveTheUsrDic(){
		return CLibrary.Instance.NLPIR_SaveTheUsrDic();
	}
	
	/**
	 * 保存新词到用户词典中
	 * @param sWord 新词
	 * @return 成功返回1 ， 失败返回0
	 */
	public int NLPIR_AddUserWord(String sWord){
		return CLibrary.Instance.NLPIR_AddUserWord(sWord);
	}
	
	
	/**
	 * 退出并释放所有资源
	 */
	public void NLPIR_Exit(){
		CLibrary.Instance.NLPIR_Exit();
	}
	
	/**
	 * 去掉包含在停用词典里的词
	 * 正则式
	 * @param content
	 * @return
	 */
	private String stopStopWord(String content){
		String regrex = "";
		String replacement="#";
		String regrexW="$#(#)#*#+#[#]#?#\\#^#{#}#|";
		if(stopWords.size() == 0){
			return content;
		}else{
			String tmp = "(";
			for(int i=0;i< stopWords.size();i++){
				String tmp1=  stopWords.get(i);
				if(regrexW.contains(tmp1)){
					tmp1 = "\\"+tmp1;
				}if(tmp1.equals("-")){
					tmp1 = "(-~-#)";
				}if(tmp1.contains(".")){
					System.out.println(tmp1);
					tmp1 = tmp1.replaceAll("\\.", "\\\\.");
					System.out.println(tmp1);
				}
				if(i<stopWords.size()-1){
					tmp+=tmp1+"|";
				}else{
					tmp+=tmp1+")";
				}
				
			}
			regrex = tmp+".*?-\\d.*?#";
		}
		System.out.println(regrex);
		System.out.println(content);
		content=content.replaceAll(regrex, replacement);
		System.out.println("==============================================");
		if(content.startsWith("#")){
			content = content.substring(1);
		}
		System.out.println(content);
		return content;
	}
	
	

	public static void main(String[] args) throws Exception {
		Nlpir nl = new Nlpir();
        nl.NLPIR_ImportUserDict(System.getProperty("user.dir")+"\\dict\\car_brand.txt", true);
        nl.NLPIR_ImportUserDict(System.getProperty("user.dir")+"\\dict\\car_type.txt", false);
        nl.NLPIR_ImportUserDict(System.getProperty("user.dir")+"\\dict\\Userdict.txt", false);
        nl.NLPIR_ImportKeyBlackList(System.getProperty("user.dir")+"\\dict\\BlackListDict.txt", "utf-8");
		String sWord = "锐的内饰有浓浓的#大众#品牌烙印，简洁大";
		System.out.println(nl.NLPIR_ParagraphProcess(sWord,0,true));
        System.out.println(nl.NLPIR_GetKeyWords(sWord, 50, false));
	}
}
