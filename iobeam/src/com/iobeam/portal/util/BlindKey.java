package com.iobeam.portal.util;

import javax.naming.*;
import java.util.Properties;
import javax.rmi.*;
import java.rmi.*;
import java.security.SecureRandom;

import com.iobeam.portal.util.sequence.*;


public class BlindKey {
	static private BlindKey cBlindKey = null;

	private Sequence mSequence;

	static private Properties cEnvironment = null;
	private SecureRandom mRandom;
	
	private static final int DEFAULT_RANDOM_STRING_LENGTH = 60;
	
	private static final char[] ALPHABET = { 
	        'A','B','C','D','E','F','G','H',
	        'J','K','M','N','P','Q','R','S',
	        'T','W','X','Y','Z','a','b','c',
	        'd','e','f','g','h','j','k','m',
	        'n','p','q','r','s','t','w','x',
	        'y','z','2','3','4','5','6','7',
	        '8','9',};
    
	private BlindKey() throws Exception {
		InitialContext initialContext = null;

		try {
			initialContext = createInitialContext();
			SequenceHome sh = (SequenceHome) PortableRemoteObject.narrow(
				initialContext.lookup( SequenceHome.JNDI_NAME ), SequenceHome.class);
			
			mSequence = sh.create();
			mRandom = new SecureRandom();
			
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			try {
				if (initialContext != null) {
					initialContext.close();
				}
			}
			catch (NamingException ne) {
				throw new Exception(ne.toString());
			}
		}
	}
	private InitialContext createInitialContext() throws NamingException {
		if (cEnvironment == null) {
			return new InitialContext();
		}
		else {
			return new InitialContext( cEnvironment );
		}
	}

	static public long getNextKey(String targetSequence) {
		try {
			return getBlindKey().mSequence.getNextSequenceNumber(
					targetSequence);
		}
		catch (Exception e) {
			throw new Error(e.toString());
		}
	}
	
	public static String getRandomSequenceString() {
		
		return getRandomSequenceString(DEFAULT_RANDOM_STRING_LENGTH);
	
	}
		
	public static String getRandomSequenceString(int length) {
		
		StringBuffer buff = new StringBuffer(length);
		try {
			for (int i=0; i<length; i++){
		    buff.append(ALPHABET[Math.abs(getBlindKey().mRandom.nextInt() % 
					ALPHABET.length)]); 
	    }
		}
		catch (Exception e) {
		}
		
    return(buff.toString());
  }
    

	static private BlindKey getBlindKey() throws Exception {
		if (cBlindKey == null) {
			synchronized ( BlindKey.class ) {
				if (cBlindKey == null) {
					cBlindKey = new BlindKey();
				}
			}
		}
		return cBlindKey;
	}

}
