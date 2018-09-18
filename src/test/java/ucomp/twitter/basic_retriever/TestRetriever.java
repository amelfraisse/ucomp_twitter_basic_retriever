package ucomp.twitter.basic_retriever;

import java.util.ArrayList;
import org.junit.Test;

import ucomp.twitter.basic_retriever.TweetsRetriever.BadParametersException;

public class TestRetriever {

    @Test(expected = BadParametersException.class) 
    public void retrieve1Tweet() throws BadParametersException{
        TweetsRetriever retriever = new TweetsRetriever(null, null, null, null, null, null);
        retriever.retrieve(new long[]{0, 1});
    }
}