package common;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

//Copied from a class task
public class LoggerManager {

	public final static String NAME = "common.logging";
	private static String SPACE = " ";
	private static String LF = "\n";
	private static final Logger logger = LogManager.getLogger(NAME);
	private static final LoggerManager loggerManager = new LoggerManager();
 
 public static Logger currentLogger() {
	 return logger;
 }
 
 public static LoggerManager current() {
	 return loggerManager;
 }
 
 private String format(Object object, String method, String message, Exception exception) {
		
		StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
		StringBuffer buffer = new StringBuffer();
		
		if (object == null) {
			buffer.append(method + SPACE + message + SPACE);
		} else {
			buffer.append(object.getClass().getName() + SPACE + method + SPACE + message + SPACE);
		}
		if (exception != null) {
			 exception.printStackTrace(printWriter);
			 printWriter.close();
		     buffer.append(stringWriter.toString());
		}
		buffer.append(LF);
		return buffer.toString();
	}
	
	public void warn(Object object, String method, String message, Exception exception) {	
		String displayMessage = format (object, method, message, exception);
		LoggerManager.currentLogger().warn(displayMessage);
	}
	
	public void info(Object object, String method, String message, Exception exception) {		
		String displayMessage = format (object, method, message, exception);
		LoggerManager.currentLogger().info(displayMessage);
	}
	
	public void error(Object object, String method, String message, Exception exception) {		
		String displayMessage = format (object, method, message, exception);
		LoggerManager.currentLogger().error(displayMessage);
	}

}
