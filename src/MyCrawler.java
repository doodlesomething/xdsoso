/*
 * @author doodlesomething@163.com
 * @date 2014-12-30
 * @version 1.0
 * @description 搜索引擎的爬虫部分主程序，该部分主要功能包括：
 				1.从指定种子url文件中读取种子url到ToDo优先队列中赋予一定的优先级，并读取bloomfilter文件判断种子url是否有访问过，并优先爬取
 				2.爬取指定url时调用DownLoader类下下载网页文件并存储到指定存储文件，更新BloomFilter的相应位
 				3.爬虫还需调用HtmlParserTool类来解析当前网页中所有的url，如果该url经过了SimpleBloomFilter的过滤
 				则将该url加入ToDo优先，根据不同情况来确定其优先级(当前程序采用的是网页url的深度--囧)，等待下来的爬取
 				4.如果程序在运行中出现问题，应该进行错误处理，包括：将当前ToDo优先队列中的url和优先级写入种子url文件中以便下次
 				启动的不重复爬取；调用Log类将错误写入指定的日志文件中；将最新的BloomFilter写入相应的文件中更新
 */

import java.io.BufferedReader;
import java.util.Vector;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Set;

import org.htmlparser.util.ParserException;




public class MyCrawler {
	
	private ToDoQueue todoQueue = new ToDoQueue();
	public SimpleBloomFilter bloomFilter = new SimpleBloomFilter();
	private final String bloomFilterFile = "/home/doodle/Code/Java/Search/doc/bloomFilterFile";
	final int DEFAULT_PRIORITY = 10;
	public boolean flag = true;
	private Vector visited = new Vector();
	public int counter = 0;

	
	
	/*
	 * @description Load the seed urls from the seedsFile
	 * 				and then add url to todoQueue
	 */

	private void initCrawlerWithSeeds(String seedsFileName) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(seedsFileName));
		
		String line = null;
		while ((line = br.readLine()) != null) {
			String	stringArray[] = line.replaceAll("/r/n", "").split(" ");
			String url = stringArray[0];
			int priority =Integer.parseInt(stringArray[1]);
			UrlObject urlObj = new UrlObject(url,priority);
			todoQueue.add(urlObj);
		}
		br.close();
		
	}
	
	
	/*
	 * @description Backup the todoQueue if there something wrong
	 */
	public void errorBackup(String backupFileName)  throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(backupFileName));
		while (!todoQueue.isEmpty()) {
			UrlObject obj = todoQueue.get();
			String url = obj.url;
			int priority = obj.priority;
			String line = url + " " + priority;
			writer.write(line);
			writer.newLine();
		}
		writer.close();
	}
	
	/*
	 * @desription:save bloomfilter to file if there something wrong
	 */
	public void saveBloomFilter() {
		bloomFilter.saveBit(bloomFilterFile);
	}
	
	/*
	* @desription:read bloomfilter from file If the program have stop
	 */
	private void readBloomFilter() {
		bloomFilter.readBit(bloomFilterFile);
	}
	
	/*
	 * @description Get the depth of url
	 */
	private int getDepth(String url) {
		String subUrl = url.substring(7);
		String strArray[] = subUrl.split("/");
		
		return strArray.length;
	}
	
	
	/*
	 * @description start the crawler
	 */
	public void startCrawl(String filename) throws IOException, ParserException {
	

		HtmlParserTool htmlParser = new HtmlParserTool();
		LinkFilter filter = new LinkFilter();
	
		//load seed urls from file
		initCrawlerWithSeeds(filename);
		
		
		//saveBloomFilter();
		readBloomFilter();
		
		
		
		while (!todoQueue.isEmpty()) { 
			String currentUrl = todoQueue.getUrl();

			if (!bloomFilter.contains(currentUrl)) {
				//get all links from the url
				Set<String> links = null;
				
				try {
					
					//download file and then save it
					DownLoader downloader = new DownLoader();	
					downloader.downloadFile(currentUrl);
					links = htmlParser.extractAllLinks(currentUrl,filter);
					
					
				}catch(Exception e) {
					e.printStackTrace();
					continue;
					//in fact I want to log the information 
				}
				
				bloomFilter.add(currentUrl);

				
				counter++;
			
				System.out.println(currentUrl);
				
				
				for (String link:links) {
					if (!bloomFilter.contains(link)) {
						
						int depth = getDepth(link);
						int priority = DEFAULT_PRIORITY / depth;
						UrlObject urlObj = new UrlObject(link,priority);
				
						//add new urls to todoQueue
						todoQueue.add(urlObj);
					}
				}
				
			}
		}
		
	}
	
	

	
	
	//测试案例
	/*
	public static void main(String args[]) throws IOException {
		String seedsFileName = "/home/doodle/Code/Java/Search/doc/seedsFile.url";
		MyCrawler crawler = new MyCrawler();
		System.out.println("starting");
		
		//String seeds[] = {"http://dp.xidian.edu.cn/"};
		try {
			crawler.startCrawl(seedsFileName);
			
		}catch(Exception e) {
			e.printStackTrace();	//I want to logging it
		} finally {
			crawler.saveBloomFilter();
			crawler.errorBackup(seedsFileName);
		
			System.out.println("stopped");
		}
		
		
	}
	*/
	
	
}
