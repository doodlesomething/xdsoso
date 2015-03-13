/*
 * @author:doodlesomething@163.com	
 * @date:2015-1-7
 * @version:1.0
 * @description:to get the text from html page
 * 
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



public class GetText {
	
	//测试使用
	final static String filename= "/home/doodle/Code/Java/Search/doc/index.html.1";
	
	
	
	/*
	 * @description:load content from html
	 */
	public static InputStream loadHtml() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"UTF-8"));
		StringBuilder html = new StringBuilder();
		String line = null;
		
		while ((line = br.readLine()) != null) {
			html.append(line);
			html.append("\n");
		}
		String content = null;
		content = html.toString();
		
		ByteArrayInputStream inputstream = new ByteArrayInputStream(content.getBytes());
		return inputstream;
	}
	
	

	public static String TextExtractor(Node root) {
		// 若是文本节点的话，直接返回
		if (root.getNodeType() == Node.TEXT_NODE) {
			return root.getNodeValue().trim();
		}
		if (root.getNodeType() == Node.ELEMENT_NODE) {
			Element elmt = (Element) root;

			// 抛弃脚本
			if (elmt.getTagName().equals("STYLE")
					|| elmt.getTagName().equals("SCRIPT")
					|| elmt.getTagName().equals("FORM")
					|| elmt.getTagName().equals("SELECT"))
				return "";

			NodeList children = elmt.getChildNodes();
			StringBuilder text = new StringBuilder();
			for (int i = 0; i < children.getLength(); i++) {
				text.append(TextExtractor(children.item(i)));
			}
			
			return text.toString();
		}
		// 对其它类型的节点，返回空值
		return "";
	}
	
	public String ExtractText(InputStream in) throws SAXException, IOException {
		DOMParser parser = new DOMParser();
		parser.setProperty("http://cyberneko.org/html/properties/default-encoding","UTF-8");
		parser.parse(new InputSource(in));
		
		Document doc = parser.getDocument();
		Node body = doc.getElementsByTagName("BODY").item(0);
		String text = TextExtractor(body);
		
		return text;
	}
	
	//测试
	/*
	public static void main(String args[]) throws SAXException, IOException {
		DOMParser parser = new DOMParser();
		
		parser.parse(new InputSource(loadHtml()));
		
		Document doc = parser.getDocument();
		
		
		Node body = doc.getElementsByTagName("BODY").item(0);
		
		System.out.println(TextExtractor(body));
		
	}
	*/
}
