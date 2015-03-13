/*
 * @description:build xdsoso.fidx.1
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;



public class BuildFidx {
	final static String toRead = "/home/doodle/Code/Java/Search/doc/xdsoso.1.seg";
	final static String toWrite = "/home/doodle/Code/Java/Search/doc/xdsoso.fidx.1";
	
	
	public static void buildFidx() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(toRead),"UTF-8"));
		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toWrite),"UTF-8"));
		
		
		
		
		String line = null;
		while ((line = br.readLine()) != null) {
			int docId = Integer.parseInt(line);
			if ((line = br.readLine()) != null) {
				String words = line;
				String[] wordArr = words.split("/ ");
				for (String word:wordArr) {
					if (!word.equals("")) {
						wr.write(word + "  " + docId);
						wr.newLine();
					}
				}
			}
			
		}
		
		br.close();
		wr.close();
		
		System.out.println("done");
	}
	
	public static void main(String args[]) {
		try {
			buildFidx();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
