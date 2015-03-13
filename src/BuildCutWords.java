/*
 * @author:doodlesomething@163.com
 * @date:2015-1-7
 * @version:1.0
 * @description:build the cut words file according to the dic file and orign page files
 				中文分词主程序
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xml.sax.SAXException;



public class BuildCutWords {

	private final static String orignFilesPath = "/home/doodle/Code/Java/Search/doc/xdsoso.1.fuck";
	private final static String cutWordsFilePath = "/home/doodle/Code/Java/Search/doc/xdsoso.1.seg";
	private final static String docIdxFile = "/home/doodle/Code/Java/Search/doc/xdsoso.doc.idx.1";
	private final static String urlIdxFile = "/home/doodle/Code/Java/Search/doc/xdsoso.url.idx.1";
	

	// get all orign page files
	public  static void cutWords() throws IOException, SAXException {

		BufferedReader br = new BufferedReader(new FileReader(orignFilesPath));
		BufferedWriter wr = new BufferedWriter(new FileWriter(cutWordsFilePath,true));
		BufferedWriter wrDocIdx = new BufferedWriter(new FileWriter(docIdxFile,true));
		BufferedWriter wrUrlIdx = new BufferedWriter(new FileWriter(urlIdxFile,true));
		
		MD5 md5 = new MD5();
		
		
		String line = null;
		int docId = 0;
		
		CutWords cutter = new CutWords();
		cutter.loadDic();

		while ((line = br.readLine()) != null) {
			if (line.equals("\n") || line.equals("\0")) {
				continue;
			}

			if (line.equals("version:1.0")) {
				docId++;
				line = br.readLine();
				String url = line.substring(4,line.length());

				while ((line = br.readLine()) != null) {
					if (line.substring(0, 7).equals("length:")) {
						break;
					}
				}

				int contentLength = Integer.parseInt(line.substring(7,
						line.length()));

				// skip the header
				line = br.readLine();

				while ((line = br.readLine()) != null) {
					if (line.equals(""))
						break;
				}

				char[] htmlContent = new char[contentLength];
				br.read(htmlContent, 0, contentLength);
				String content = String.valueOf(htmlContent);
				
				
				/*
				 * build doc.idx
				 * the format such as: 
				 * 	docId	offset 	MD5 value of content
				 */
				int offset = 0;
				String contentMD5Value = md5.getMD5(content);
				wrDocIdx.write(docId + "  " + offset + "  " + contentMD5Value);
				wrDocIdx.newLine();
				
				/*
				 * build url.idx
				 * the format of file such as:
				 * 	docId  MD5 value of url
				 */
				String urlMD5Value = md5.getMD5(url);
				wrUrlIdx.write(urlMD5Value + "  " + docId);
				wrUrlIdx.newLine();
				
				
				
				ByteArrayInputStream inputstream = new ByteArrayInputStream(content.getBytes());
				GetText textGetter = new GetText();
				String text = textGetter.ExtractText(inputstream);
				ArrayList result = cutter.cutAll(text.replaceAll("\n", ""));
				wr.write(String.valueOf(docId));
				wr.newLine();
				Iterator<String> iter = result.iterator();
				while (iter.hasNext()) {
					String test = iter.next();
					
					wr.write(test.trim() + "/ ");
				}
				wr.newLine();
				
				
				

			}
		}
		
		
		br.close();
		wr.close();
		wrDocIdx.close();
		wrUrlIdx.close();

	}

	

	//测试
	public static void main(String args[]) throws IOException, SAXException {
		cutWords();
		System.out.println("done");
	}
}
