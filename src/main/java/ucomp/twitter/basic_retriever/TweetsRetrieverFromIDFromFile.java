package ucomp.twitter.basic_retriever;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

import ucomp.twitter.basic_retriever.TweetsRetriever.BadParametersException;

public class TweetsRetrieverFromIDFromFile {
	private String m_sInputFile;
	private String m_sOutputPath;
	private String m_sRetrieveMode;

	private String m_sConsumerKey;
	private String m_sSecretKey;
	private String m_sAccessToken;
	private String m_sAccessTokenSecret;

	private static final Logger logger = Logger.getLogger(TweetsRetrieverFromIDFromFile.class);
	
	public TweetsRetrieverFromIDFromFile(String s_InputFiles, String m_sOutputPath,
			String m_sRetrieveMode, String m_sConsumerKey, String m_sSecretKey,
			String m_sAccessToken, String m_sAccessTokenSecret) {
		super();
		this.m_sInputFile = s_InputFiles;
		this.m_sOutputPath = m_sOutputPath;
		this.m_sRetrieveMode = m_sRetrieveMode;
		this.m_sConsumerKey = m_sConsumerKey;
		this.m_sSecretKey = m_sSecretKey;
		this.m_sAccessToken = m_sAccessToken;
		this.m_sAccessTokenSecret = m_sAccessTokenSecret;
	}

	public void retrieveTweets() throws BadParametersException {
		try {
			//Get IDs
			Scanner s = new Scanner(new File(this.m_sInputFile));
			ArrayList<Long> listIds = new ArrayList<Long>();
			while (s.hasNextLong()) {
				listIds.add(s.nextLong());
			}
			if (listIds.size() == 0) {
				logger.info("No ID found in " + m_sInputFile);
				return;
			}
			long[] lstIds = new long[listIds.size()];
			for (int i = 0; i < listIds.size(); i++)
				lstIds[i] = listIds.get(i);

			TweetsRetriever retriever = new TweetsRetriever(this.m_sOutputPath, this.m_sRetrieveMode, this.m_sConsumerKey, this.m_sSecretKey, this.m_sAccessToken, this.m_sAccessTokenSecret);
			retriever.retrieve(lstIds);
			
		} catch (FileNotFoundException e) {
			logger.error("Cannot access file containing tweets id: " + this.m_sInputFile);
			logger.debug(e);
		}
	}

}
