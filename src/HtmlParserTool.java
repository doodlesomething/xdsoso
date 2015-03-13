/*
 * @author doodlesomething
 * @date 2014-12-30
 * @version 1.0
 * @description To get all links 
 * 使用了第三方包HtmlParser来完成对页面的解析，获取网页中的所有链接

 */

import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;

import org.htmlparser.*;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.ParserException;
import org.htmlparser.NodeFilter;
import org.htmlparser.Node;
import org.htmlparser.util.NodeList;
import java.util.Iterator;



public class HtmlParserTool {
	/* @description:这里的链接主要有<a href>类型的和<frame src >类型的
		@param String url 网页url
		@param  LinkFilter filter  特殊过滤条件
		@return 返回链接集
	*/
	public Set<String> extractAllLinks(String url,LinkFilter filter) throws ParserException, IOException {
		Set<String> linksSet = new HashSet<String>();
		URL urlPage = new URL(url);
	    HttpURLConnection conn = (HttpURLConnection) urlPage.openConnection();  
	    conn.setConnectTimeout(1000);  
	    conn.setReadTimeout(1000);  
	    Parser parser = new Parser(conn);	
		parser.setEncoding("UTF-8");
		
		
		NodeFilter frameFilter = new NodeFilter() {
			public boolean accept(Node node) {
				if (node.getText().startsWith("<frame src=")) {
					return true;
				}
				else {
					return false;
				}
			}
		};
		
		
		NodeFilter aFilter = new TagNameFilter("a");
		OrFilter linksFilter = new OrFilter(aFilter,frameFilter);
		
		NodeList list = parser.extractAllNodesThatMatch(linksFilter);
		
		for (int i = 0; i < list.size(); i++) {
			Node tag = list.elementAt(i);
			
			if (tag instanceof LinkTag) {
				LinkTag link = (LinkTag) tag;
				String linkUrl = link.getAttribute("href");
				//<a href的情况
				if (linkUrl != null && !linkUrl.startsWith("#")) {
				
					try {
						URL absoluteUrl = new URL(url);
						URL urlParser = new URL(absoluteUrl,linkUrl);
						linkUrl = urlParser.toString();
						if(filter.accept(linkUrl)) {
							int length = linkUrl.length();
							String substr = linkUrl.substring(length -1 ,length);
							
							if (substr.equals("/") | substr.equals("#")) {
								linkUrl = linkUrl.substring(0,length - 1);
							}
							
							linksSet.add(linkUrl);
						}
					}catch(MalformedURLException e) {
						continue;
					}
				}
				
				
			}
			//<frame str >的情况
			else {
				String frame = tag.getText();
				int start = frame.indexOf("src=");
				frame = frame.substring(start);
				int end = frame.indexOf(" ");
				if (end == -1) {
					end = frame.indexOf(">");
				}
				String frameUrl = frame.substring(5, end);
				try {
					URL absoluteUrl = new URL(url);
					URL urlParser = new URL(absoluteUrl,frameUrl);
					frameUrl = urlParser.toString();
					if(filter.accept(frameUrl)) {
						linksSet.add(frameUrl);
					}
				}catch(MalformedURLException e) {
					continue;
				}
				if (filter.accept(frameUrl)) {
					linksSet.add(frameUrl);
				}
			}
		}
		
		 
		return linksSet;
	}

	
	//测试案例
	/*
	public static void main(String args[]) throws ParserException, MalformedURLException, IOException{
		String currentUrl = "http://www.xidian.edu.cn/";
		LinkFilter filter = new LinkFilter();
		Set<String> links = extractAllLinks(currentUrl,filter);
		Iterator iter = links.iterator();
		
		while (iter.hasNext()) {
			System.out.println(iter.next());
			System.out.println();
		}
		
		System.out.println(links.size());
	}
	*/
	
}
