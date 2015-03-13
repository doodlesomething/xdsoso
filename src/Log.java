/*
 * @author:doodlesomething@163.com
 * @date:2015-1-3
 * @version:1.0
 * @description:爬虫错误处理日志记录程序，负责将错误时间、原因等记录到指定日志文件中
 */

import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Date;
import java.util.logging.Formatter;



/*
 * @description:the class to keep log message
 */
public class Log {
	
	final static String filePattern = "/home/doodle/Code/Java/Search/doc/soso.%g.log";
	final static int sizeLimit = 2 << 20;	//size of single log file
	final static int fileNumLimit = 50;	//the count of all log file
	
	private static StackTraceElement traceElement;
	private String errorMsg;	//错误信息
	private Level errorLevel;	//错误级别
	
	
	public Log(StackTraceElement elment,String msg,Level level) {
		traceElement = elment;
		errorMsg = msg;
		errorLevel = level;
	}
	
	
	
	public  void setLog()  {
		Logger log = Logger.getLogger(traceElement.getClassName());
		try {
		
			FileHandler logFileHandler = new FileHandler(filePattern,sizeLimit,fileNumLimit,true);
			log.setLevel(Level.INFO);
			logFileHandler.setFormatter(new MyFormatter(getParam()));
			log.addHandler(logFileHandler);
			log.log(errorLevel,errorMsg);		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	@description:获取各种日志需记录的参数
	*/
	public static String getParam() {
		StringBuffer buff = new StringBuffer();
		String runPath = System.getProperty("java.class.path");
		String className = traceElement.getClassName();
		String fileName = traceElement.getFileName();
		int lineNumber = traceElement.getLineNumber();
		String methodName = traceElement.getMethodName();
		
		buff.append(runPath + " ");
		buff.append(className + " ");
		buff.append(fileName + " ");
		buff.append(lineNumber + " ");
		buff.append(methodName + " ");
		
		return buff.toString();
	}
	
	
}


/*
 * @description:class of my log file formatter 
 * @param String proMsg 	something about the program such as className / methodName / lineNumber / program run path
 */
class MyFormatter extends Formatter {
	
	
	private String Msg;
	
	public  MyFormatter(String proMsg) {
		Msg = proMsg;
		System.out.println(Msg);
	}
	

	@Override
	public String format(LogRecord record) {
		// TODO Auto-generated method stub
		
		
		
		StringBuffer buff = new StringBuffer();
		String date = new Date().toString();
		
		
		buff.append(date + " | ");
		buff.append(record.getLoggerName() + " | ");
		buff.append(record.getLevel() + " | ");
		buff.append(record.getMillis() + " | ");
		buff.append(record.getMessage() + " | " + Msg + "\n");
		return buff.toString();
		
	}
	
}
