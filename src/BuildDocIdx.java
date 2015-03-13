

/*
 * @author:doodlesomething@163.com
 * @version:1.0
 * @date:2015-1-15
 * @description:build doc.idx
 */


import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;




public class BuildDocIdx {

	final static String toRead = "/home/doodle/Code/Java/Search/doc/xdsoso.1.fuck";
	final static String toWrite = "/home/doodle/Code/Java/Search/doc/xdsoso.doc.idx.1";
	
	
	private static void buildDocIdx() throws IOException {
		BufferedReader br = new  BufferedReader(new InputStreamReader(new FileInputStream(toRead),"UTF-8"));
		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toWrite),"UTF-8"));
		
		String line = null;
		
		while ((line = br.readLine()) != null) {
			
		}
		
		
		
	}
	
	

	
	public static void main(String args[]) {
		try {
			buildDocIdx();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} 
	}
	
	
}
