package ucomp.twitter.basic_retriever;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * Hello world!
 *
 */
public class App {
	private static final Logger logger = Logger.getLogger(App.class);
	private static Properties s_Properties;
	static {
		s_Properties = new Properties();
		try {
			s_Properties.load(new InputStreamReader(new FileInputStream("App.properties"), "UTF-8"));
		} 
		catch (FileNotFoundException e) {
			logger.error("Could not find App.properties file");
			System.exit(-1);
		}
		catch (IOException e) {
			logger.error("Can not acces to App.properties file. Make sure user that is running current jar has acces to the previous file.");
			System.exit(-1);
		}
	}
	
	public void run() {
		String sInputFile = s_Properties.getProperty("App.InputFile");
		String sOuputPath = s_Properties.getProperty("App.OutputPath");
		String sRetrievMode = s_Properties.getProperty("App.retrievemode");
		
		String sConsumerKey = s_Properties.getProperty("App.TwitterConsumerKey");
		String sSecretKey = s_Properties.getProperty("App.TwitterSecretKey");
		String sAccessToken = s_Properties.getProperty("App.TwitterAccessToken");
		String sAccessTokenSecret = s_Properties.getProperty("App.TwitterAccessTokenSecret");
		
		TweetsRetrieverFromIDFromFile retriever = new TweetsRetrieverFromIDFromFile(sInputFile, sOuputPath, sRetrievMode, sConsumerKey, sSecretKey, sAccessToken, sAccessTokenSecret);
		retriever.retrieveTweets();
	}
	
	public static void main( String[] args) {
		App myApp = new App();
		myApp.run();
	}
}
