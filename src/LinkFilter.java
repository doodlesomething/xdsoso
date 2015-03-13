/*
 * @author:doodlesomething@163.com
 * @date:2015-1-3
 * @version:1.0
 * @description:将爬虫的爬取范围限制在西电的域名下，更好的限制方法应该是采用文件的形式可自由添加，后期加上
 */


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LinkFilter {
	
	public boolean accept(String url) {
		Pattern p = Pattern.compile(".*xidian.edu.cn.*");
		Matcher m = p.matcher(url);
		boolean b = m.matches();
		
		return b && !url.startsWith("mailto:");
	}
	
}
