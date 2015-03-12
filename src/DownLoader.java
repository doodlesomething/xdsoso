/*
 * @author doodlesomething@163.com
 * @date:2014-12-30
 * @version 1.0
 * @description Download web page from Internet	and save them as local file 
 	使用第三方包cpdetector来做网页内容的编码预检测
 */

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.ByteOrderMarkDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.BufferedReader;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.nio.charset.Charset;



public class DownLoader {

	/*
	 * @description:download page and save it
	 */
	final static int TIMEOUT = 10000;
	//网页文件储存路径后期应实现动态化
	private static String downloaFilePathName = "/home/doodle/Code/Java/Search/doc/";


	/*
	@description:下载特定url的网页并按指定方式来存储为本地文件
	@param：String url  需下载网页的url
	*/
	public void downloadFile(String url) throws IOException {

		String version;
		String trueUrl;
		String orignUrl = null; // when the url redict
		String lastUpdateTime;
		String ip;
		int length; // the length of data

		StringBuffer pageContent = new StringBuffer();

		URL pageUrl = new URL(url);
		HttpURLConnection uc = (HttpURLConnection) pageUrl.openConnection();
		uc.setReadTimeout(TIMEOUT);
		uc.setConnectTimeout(TIMEOUT);

		String encoding = encodingDetector(url);
		System.out.println(encoding);

		// get all information
		version = "1.0";

		if (uc.getURL().toString().equals(url)) {
			trueUrl = url;
		} else {
			trueUrl = uc.getURL().toString();
			orignUrl = url;
		}

		ip = getIpByUrl(trueUrl);
		lastUpdateTime = getDate();

		// write data
		InputStream in = uc.getInputStream();
		InputStreamReader inr = new InputStreamReader(in, encoding);
		BufferedReader br = new BufferedReader(inr);
		String line = null;

		while ((line = br.readLine()) != null) {
			// I want to delete blank line,but it did not work
			if (!line.equals("") && !line.contains("charset=gb2312")) {
				pageContent.append(line);
				pageContent.append('\n');
			}
		}

		length = pageContent.length();

		// write all above information to file in specailly format
		String fileToWrite = downloaFilePathName;
		String name = getFileName(length);

		fileToWrite = fileToWrite + name;

		BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite,
				true));

		// version
		writer.write("version:" + version);
		writer.newLine();

		// url
		writer.write("url:" + trueUrl);
		writer.newLine();

		if (orignUrl != null) {
			writer.write("orign:" + orignUrl);
		}

		// date
		writer.write("date:" + lastUpdateTime);
		writer.newLine();

		// ip
		writer.write("ip:" + ip);
		writer.newLine();

		// data length
		writer.write("length:" + length);
		writer.newLine();

		// write header information
		writer.newLine();
		Map header = uc.getHeaderFields();
		Set<String> keys = header.keySet();
		for (String key : keys) {
			String value = uc.getHeaderField(key);
			if (key != null) {
				writer.write(key + ":" + value);
			} else {
				writer.write(value);
			}
			writer.newLine();
		}

		String content = pageContent.toString();
		// write data
		writer.newLine();
		writer.write(content);
		writer.newLine();
		writer.newLine();

		// close
		in.close();
		inr.close();
		br.close();
		writer.close();

	}


	/*
	@description:网页编码预检测 使用第三方包cpdetector来做网页内容的编码预检测
	@param:String  url  指定url
	@return 编码类型
	*/
	public static String encodingDetector(String url) throws IOException {
		URL des = new URL(url);
		CodepageDetectorProxy codepageDetectorProxy = CodepageDetectorProxy
				.getInstance();

		codepageDetectorProxy.add(JChardetFacade.getInstance());
		codepageDetectorProxy.add(ASCIIDetector.getInstance());
		codepageDetectorProxy.add(UnicodeDetector.getInstance());
		codepageDetectorProxy.add(new ParsingDetector(false));
		codepageDetectorProxy.add(new ByteOrderMarkDetector());

		Charset charset = codepageDetectorProxy.detectCodepage(des);
		return charset.name();
	}

	/*
	@description:将网页的非特定编码转换为统一utf编码
	*/
	public static String convert(String baCardHolderName, String charset)
			throws UnsupportedEncodingException {

		String test = new String(baCardHolderName.getBytes("utf-8"), "utf-8");
		return test;
	}

	/*
	 * @description:每个存储网页内容的文件有大小限制，根据情况判断是否需要增加新文件
	 * @param:int length 当前网页的组成的网页内容的大小
	 * @return :String  filename  将存储当前网页内容的文件名
	 */
	private static String getFileName(int length) {
		String filename = null;
		File file = new File(downloaFilePathName);
		File[] fuckFiles = file.listFiles(new FuckFileFilter());
		if (fuckFiles.length <= 0) {
			filename = "xdsoso.1.fuck";
		} else {
			int max = 1;
			int index = 0;
			for (int i = 0; i < fuckFiles.length; i++) {
				String subStr = fuckFiles[i].getName().toString();
				subStr = subStr.substring(7, subStr.length() - 5);
				int num = Integer.parseInt(subStr);
				if (num > max) {
					max = num;
					index = i;
				}
			}

			filename = "xdsoso." + max + ".fuck";
			int fileSize = (int) fuckFiles[index].length();
			// we should limit the size of file to write
			if ((fileSize + length + 500) > (2 << 24)) {
				max += 1;
				filename = "xdsoso." + max + ".fuck";
			}

		}
		return filename;

	}

	/*
	 * @description:get now date
	 */
	private static String getHeaderFieldate() {
		Date date = new Date();
		return date.toString();
	}

	/*
	 * @description:get ip address by url
	 */
	private static String getIpByUrl(String url) throws UnknownHostException {
		String hostName = getHostName(url);
		InetAddress iAddress = InetAddress.getByName(hostName);
		return iAddress.getHostAddress();
	}

	/*
	 * @description:get title
	 */

	/*
	 * @description:get hostname by url
	 */
	private static String getHostName(String url) {
		Matcher m = Pattern.compile("^http://[^/]+").matcher(url);
		String subStr = null;
		while (m.find()) {
			subStr = m.group().substring(7);
			int index = subStr.indexOf(":");
			if (index != -1) {
				subStr = subStr.substring(0, index);
			}
		}
		return subStr;
	}

	//测试程序
	public static void main(String args[]) throws IOException {
		DownLoader download = new DownLoader();
		download.downloadFile("http://job.xidian.edu.cn");
	}

}


/*
 * @description:to get the extend filename is .fuck
   网页的存储文件名形式为:xdsoso.数字.fuck
   过滤程序
 */
class FuckFileFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		// TODO Auto-generated method stub
		if (pathname.getName().endsWith(".fuck")
				&& pathname.getName().startsWith("xdsoso"))
			return true;

		return false;
	}

}
