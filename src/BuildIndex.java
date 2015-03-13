/*
 * @author:doodlesomething@163.com
 * @date:2015-1-15
 * @version:1.0
 * @description:build index file
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;
import java.util.Iterator;




public class BuildIndex {
	final static String toRead = "/home/doodle/Code/Java/Search/doc/xdsoso.fidx.1.sort";
	final static String toWrite = "/home/doodle/Code/Java/Search/doc/xdsoso.index.1";
	
	public static  void buildIndex() throws IOException, FileNotFoundException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(toRead),"UTF-8"));
		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toWrite),"UTF-8"));
		
		String line = null;
		String preWord,currentWord;
		String preDocId,currentDocId;
		Vector<String> vector = new Vector<String>();
		
		line = br.readLine();
		preWord =  line.split("  ")[0];
		preDocId =  line.split("  ")[1];
		vector.add(preDocId);
		
		while ((line = br.readLine()) != null) {
			currentWord = line.split("  ")[0];
			currentDocId = line.split("  ")[1];
			
			if (currentWord.equals(preWord)) {
				vector.add(currentDocId);
			}
			else {
				wr.write(preWord + "    ");
				Iterator<String> iter = vector.iterator();
				while (iter.hasNext()) {
					if (vector.size() > 1) {
						wr.write(iter.next() + " ");
					}
					else {
						wr.write(iter.next());
					}
					
				}
				vector.removeAllElements();
				preWord = currentWord;
				vector.add(currentDocId);
				
				wr.newLine();
			}
			
		}
		
		br.close();
		wr.close();
		
		
		System.out.println("done");
		
	}
	
	
	
	public static void main(String args[]) {
		try {
			buildIndex();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
