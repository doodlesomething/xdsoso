/*
 * @author:doodlesomething@163.com
 * @date:2015-1-7
 * @version:1.0
 * @description:cut words according to the dic
 					中文分词的核心程序，这里采用的是较为简单的机械匹配分词，分词的效果
 					主要取决于字典的好与坏
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;



public class CutWords {
	
	final static String filename = "/home/doodle/Code/Java/Search/doc/2.bak";

	static TreeNode trie = null;
	
	public void loadDic() throws NumberFormatException, IOException {
		trie = new TreeNode();
		
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = null;
		
		while ((line = br.readLine()) != null) {
			TreeNode root = trie;
			
			String[] seg = line.split(" ");
			String words = seg[0];
			int frequency = 1;
			double antilog = 0;
			
			for (int i = 0, len = words.length(); i < len ; i++) {
				String character = "" +  words.charAt(i);
				TreeNode node = root.getChild(character);
				if (node == null) {
					node = new TreeNode();
					node.setCharacter(character);
					root.addChild(node);
				}
				
				root = node;
			}
			
			root.setAntilog(antilog);
			root.setFrequency(frequency);
		}
		
		br.close();
	}
	
	
	//get node by given word
	public static TreeNode getNodeByWord(String word) throws NumberFormatException, IOException {
		if (trie == null) {
			//log
			return null;
		}
		
		TreeNode node = trie;
		
		for (int i = 0; i < word.length(); i++) {
			String ch = word.charAt(i) + "";
			if (node == null) {
				break;
			}
			else {
				node = node.getChild(ch);
			}
		}
		
		return node;
	}
	
	
   
    
    /*
     * @param sentence		the string to be cut
     * @description:left longest match method to cut words -- 最大正向匹配
     */
    public static ArrayList<String> MM(String sentence) throws NumberFormatException, IOException {
    	int MAX_LEN = 10;
    	
    	ArrayList<String> result = new ArrayList<String>();
    	int end;
    	sentence = sentence.trim();
    	while (!sentence.equals("")) {
    		if (sentence.length() < MAX_LEN)
    			end = sentence.length();
    		else
    			end = MAX_LEN;
    		
    		String segment = sentence.substring(0, end);
    		
    		while (segment.length() > 1) {
    			if (getNodeByWord(segment) == null) {
    				segment = segment.substring(0,segment.length() - 1);
    			}
    			else {
    				break;
    			}
    		}
    		
    		sentence = sentence.substring(segment.length(),sentence.length());
    		result.add(segment);
    		
    	}
    	
    	return result;
    }
    
    
    /*
     * @param	String sentence 	the string to be cut
     * @description:reverse MM method --最大反向匹配
     */
    public static ArrayList<String> RMM(String sentence) throws NumberFormatException, IOException {
    	ArrayList<String> result = new ArrayList<String>();
    	int	MAX_LEN = 10;
    	int start;
    	sentence = sentence.trim();
    	while (!sentence.equals("")) {
    		int len = sentence.length();
    		if ( len <= MAX_LEN) {
    			start = 0;
    		}
    		else {
    			start = len - MAX_LEN;
    		}
    		
    		String segment = sentence.substring(start,len);
    		
    		
    		while (segment.length() > 1) {
    			if (getNodeByWord(segment) == null) {
    				segment = segment.substring(1,segment.length());
    			}
    			else {
    				break;
    			}
    		}

    		sentence = sentence.substring(0,sentence.length() - segment.length());
    		result.add(segment);
    	}
    	//reverser the result to get the correct words
    	Collections.reverse(result);
    	return result;
    }
	
    
    /*
     * @description:get the number of single  word in list
     */
    public static int getSingleWordNum(ArrayList<String> list) {
    	int single = 0;
    	
    	for (int i = 0; i < list.size(); i++) {
    		if (list.get(i).toString().length() == 1) {
    			single++;
    		}
    	}
    	return single;
    }
    
    public static int getSingleWordInTree(ArrayList<String> list) throws NumberFormatException, IOException {
    	int result = 0;
    	for (int i = 0; i < list.size(); i++) {
    		if (getNodeByWord(list.get(i).toString()) != null) {
    			result++;
    		}
    	}
    	
    	return result;
    }
    
    
    
    /*
     * @description:decide which result to get  --多策略决定最后的匹配结果
     */
    public static ArrayList<String> getFinalResult(String sentence) throws NumberFormatException, IOException {
    	ArrayList<String> mmResult = new ArrayList<String>();
    	ArrayList<String> rmmResult = new ArrayList<String>();
    	mmResult = MM(sentence);
    	rmmResult = RMM(sentence);
    	
    	
    	
    	if (mmResult.equals(rmmResult)) {
    		return mmResult;
    	}
    	else {
    		if (mmResult.size() != rmmResult.size()) {
    			return mmResult.size() > rmmResult.size() ? rmmResult : mmResult;
    		}
    		else {
    			if (getSingleWordNum(mmResult) != getSingleWordNum(rmmResult)) {
    				return getSingleWordNum(mmResult) > getSingleWordNum(rmmResult) ? rmmResult : mmResult;
    			}
    			else {
    				return getSingleWordInTree(mmResult) > getSingleWordInTree(rmmResult) ? mmResult : rmmResult;
    				
    			}
    		}
    	}
    }
    
    public void outputArrayList (ArrayList<String> list) {
    	Iterator iter = list.iterator();
    	
    	while (iter.hasNext()) {
    		System.out.print(((String) iter.next()).replaceAll(" +","") + " ");
    	}
    	System.out.println();
    }
    
    /*
     * @description:split the text by the symbol character an cut them
     */
    public static ArrayList<String> cutAll(String text) throws NumberFormatException, IOException {
    	ArrayList<String> result = new ArrayList<String>();
    	String[] keys = text.split("[,。；\\.]");
		for (String key:keys) {
			if (!key.equals("")) {
				key = key.trim();
				result.addAll(cut(key));
			}
		}
		
		return result;
    }
    
    /*
     * @description:should pay attention to the situtaion when there are chinese/number/english in a sentence
     */
    public static ArrayList<String> cut(String key) throws NumberFormatException, IOException {
		int offset = 0;
		
		ArrayList<String> result = new ArrayList<String>();
		key = key.trim();
		while (offset < key.length()) {
			String fuck = key.substring(offset,offset + 1);
			//this code is very strange:pay attention the chinese " " and english " "
			if (fuck.equals(" ") || fuck.equals("　")) {
				offset++;
				continue;
			}
			
			int numEnd = matchNum(key,offset);
			if (numEnd > offset) {
				result.add(key.substring(offset,numEnd));
				offset = numEnd;
				continue;
			}
			
			int englishEnd = matchEnglish(key,offset);
			if (englishEnd > offset) {
				result.add(key.substring(offset,englishEnd));
				offset = englishEnd;
				continue;
			}
			
			int numStart = startNum(key,offset);
			int englishStart = startEnglish(key,offset);
			
			int chineseEnd = numStart > englishStart ? englishStart : numStart;
			if (chineseEnd > offset ) {
				
				String sentence = key.substring(offset,chineseEnd);
				if (!sentence.equals("")) {
					result.addAll(getFinalResult(sentence));
					offset = chineseEnd;
				}
				
			}
			else {
				String sentence = key.substring(offset,key.length());
				if (!sentence.equals("")) {
					result.addAll(getFinalResult(sentence));
					offset = key.length();
				}
				
			}
		}
		
		return result;
	}
	

	
	/*
	 * @descrition:get the end postion of number
	 */
	public static int matchNum(String key,int start) {
		int i = start;
		while (i < key.length()) {
			char ch = key.charAt(i);
			if (isDigit(ch)) {
				i++;
			}
			else {
				break;
			}
		}
		return i;
	}
	
	
	/*
	 * @description:get the end postion of english
	 */
	public static int matchEnglish(String key,int start) {
		int i = start;
		while (i < key.length()) {
			char ch = key.charAt(i);
			if (isEnglish(ch)) {
				i++;
			}
			else {
				break;
			}
		}
		return i;
	}
	
	
	/*
	 * @description:get the start postion of number
	 */
	public static int startNum(String key,int offset) {
		int i = offset;
		while (i < key.length()) {
			char ch = key.charAt(i);
			String test = key.substring(i,i + 1);
			if ( test.equals(" ") || test.equals("　") || isDigit(ch)) {
				break;
			}
			i++;
		}
		return i;
	}
	
	
	/*
	 * @description:get the start postion of english
	 */
	public static int startEnglish(String key,int offset) {
		int i = offset;
		while (i < key.length()) {
			char ch = key.charAt(i);
			String test = key.substring(i,i + 1);
			if (isEnglish(ch) ||test.equals(" ") || test.equals("　")) {
				break;
			}
			i++;
		}
		return i;
	}
	
	
	
	
	/*
	 * @description:jude the character is english or not
	 */
	public static boolean isEnglish(char ch) {
		if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/*
	 * @description:make the isDigit simple
	 */
	public static boolean isDigit(char ch) {
		return Character.isDigit(ch);
	}
    
    /*
	public static void main(String args[]) throws NumberFormatException, IOException, SAXException {
		GetText textGetter = new GetText();
		String text = textGetter.ExtractText();
		
		System.out.println(text);
		
		CutWords cutter = new CutWords();
		cutter.loadDic();
		String sentence = "里面有我多年上网形成的无数错别字词路柳墙花c#";
		
		
		
		
		ArrayList<String> result = cutter.getFinalResult(text);
		
		
		cutter.outputArrayList<String>(result);
		
	}
	*/
	
}
