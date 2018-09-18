package ucomp.twitter.basic_retriever;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class TweetsRetriever {
	private String m_sPath;
	private String m_sRetrieveMode;

	private String m_sConsumerKey;
	private String m_sSecretKey;
	private String m_sAccessToken;
	private String m_sAccessTokenSecret;

	private static final Logger logger = Logger.getLogger(TweetsRetriever.class);

	public TweetsRetriever(String sPath, String sRetrieveMode, String sConsumerKey, String sSecretKey, String sAccessToken, String sAccessTokenSecret) {
		this.m_sPath = sPath;
		this.m_sRetrieveMode = sRetrieveMode;
		this.m_sConsumerKey = sConsumerKey;
		this.m_sSecretKey = sSecretKey;
		this.m_sAccessToken = sAccessToken;
		this.m_sAccessTokenSecret = sAccessTokenSecret;
	}



	//Root method to retrieve tweets by ID. Limited to 100 tweets as per lookup api limitation
	//Return the list of Ids retrived
	public List<Long> retrieve(long[] lstIds) {
		ArrayList<Long> lstRetrievedIds = new ArrayList<Long>();
		if (lstIds.length <= 0) {
			logger.info("No IDs specified");
			return lstRetrievedIds;
		}
		if (new File(this.m_sPath).exists() == false) {
			logger.error("Please first create output directory: " + this.m_sPath);
			return lstRetrievedIds;
		}

		ConfigurationBuilder cb = new ConfigurationBuilder();

		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(m_sConsumerKey)
		.setOAuthConsumerSecret(m_sSecretKey)
		.setOAuthAccessToken(m_sAccessToken)
		.setOAuthAccessTokenSecret(m_sAccessTokenSecret);
		cb.setJSONStoreEnabled(true);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		HashSet<Long> mapIdTweetsRetrieved = new HashSet<Long>();
		int i = 0;
		while  (i < lstIds.length) {
			long [] lstIds4Request;
			if (i + 100 < lstIds.length) {
				long[] lsttemp = new long[100];
				System.arraycopy( lstIds, i, lsttemp, 0, 100 );
				lstIds4Request = lsttemp;
				i +=100;
			}
			else {
				long[] lsttemp = new long[lstIds.length-i];
				System.arraycopy( lstIds, i, lsttemp, 0, lstIds.length-i );
				lstIds4Request = lsttemp;
				i = lstIds.length;
			}

			ResponseList<Status> tweets = null;
			try {
				tweets = twitter.lookup(lstIds4Request);
				logger.debug("#Tweets retrieved: " + tweets.size());
			} 
			catch (TwitterException te) {
				logger.error(te);
			}

			
			for (Status tweet : tweets) {
				String sTxt = tweet.getText().replaceAll("(\\r|\\n)", " ");
				long id = tweet.getId();
				mapIdTweetsRetrieved.add(id);
				logger.debug("Tweet retrieved - ID: " + id + " - Text: " + sTxt);
				String sRetrieveMode = this.m_sRetrieveMode;
				File file = new File(this.m_sPath + id + ".txt");
				File fileJson = new File(this.m_sPath + id + ".json");
				//file.getParentFile().mkdirs();
				try {
					PrintWriter writer = new PrintWriter(new FileWriter(file));
					writer.println(sTxt);
					writer.close();
				} 
				catch (IOException e) {
					logger.error("Error while writing tweet Id: " + tweet.getId() + " on file: " + file.getPath() +". Make sure folder exists");
					logger.debug(e);
				}
				try {
					logger.debug("Tweet written : " + file.toString());

					if ("mixte".equalsIgnoreCase(sRetrieveMode)) {
						PrintWriter writerJson = new PrintWriter(new FileWriter(fileJson));
						String json = DataObjectFactory.getRawJSON(tweet);
						writerJson.println(json);
						writerJson.close();
					}
				}
				catch (IOException e) {
					logger.error("Error while writing tweet Id: " + tweet.getId() + " on file: " + fileJson.getPath());
					logger.debug(e);
				}
			}
		}
		//Compare retrieved tweets to desired tweets
		for (long id_desired : lstIds) {
			if(mapIdTweetsRetrieved.contains(id_desired)) {
				lstRetrievedIds.add(id_desired);
				logger.info("Tweet " + id_desired + " successfully retrieved");
			}
			else
				logger.info("Tweet " + id_desired + " not retrieved");
		}
		
		return lstRetrievedIds;
	}
}
