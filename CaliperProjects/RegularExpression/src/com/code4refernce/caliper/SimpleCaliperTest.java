package com.code4refernce.caliper;

import java.util.Random;
import java.util.regex.Pattern;

import com.google.caliper.Param;
import com.google.caliper.SimpleBenchmark;

public class SimpleCaliperTest extends SimpleBenchmark {
	String extensiveregex = "^\\d?(?:(?:[\\+]?(?:[\\d]{1,3}(?:[ ]+|[\\-.])))?[(]?(?:[\\d]{3})[\\-/)]?(?:[ ]+)?)?(?:[a-zA-Z2-9][a-zA-Z0-9 \\-.]{6,})(?:(?:[ ]+|[xX]|(i:ext[\\.]?)){1,2}(?:[\\d]{1,5}))?$";
	Pattern EXTENSIVE_REGEX_PATTERN =  Pattern.compile(extensiveregex);
	
	String mdn[][];
	Random random;
	@Param
	int index;

	@Override 
	protected void setUp() {
		 random = new Random(0);
		mdn = new String[11][1<<16];
		for (int i=0; i<mdn.length; ++i) {
		    mdn[0][i] = String.format("%03ddsfasdf00000", random.nextInt(1000));
		    mdn[1][i] = String.format("%04d", random.nextInt(10000));
		    mdn[2][i] = String.format("%10d", random.nextInt((int) 1e10));
		    mdn[3][i] = String.format("-%10d", random.nextInt((int) 1e10));
		    mdn[4][i] = String.format("%10d-", random.nextInt((int) 1e10));
		    mdn[5][i] = String.format("%03d-%03d-%03d", random.nextInt(1000), random.nextInt(1000), random.nextInt(1000));
		    mdn[6][i] = String.format("-%03d-%03d-%03d-", random.nextInt(1000), random.nextInt(1000), random.nextInt(1000));
		    mdn[7][i] = String.format("%03d-%03d-%03d-", random.nextInt(1000), random.nextInt(1000), random.nextInt(1000));
		    mdn[8][i] = String.format("%03d-%03d-%03d ext %04d", random.nextInt(1000), random.nextInt(1000), random.nextInt(1000), random.nextInt(10000));
		    mdn[9][i] = String.format("%03d-%03d-%03d ext %04d-", random.nextInt(1000), random.nextInt(1000), random.nextInt(1000), random.nextInt(10000));
		    mdn[10][i] = "123456789012345677890";
		}
	}

	public boolean timeExtensiveSimpleMDNCheck(int reps){
		boolean results = false;
		for(int i = 0; i<reps; i ++){
			for(int index2=0; index2<mdn.length; index2++)
				//Use simple method to check the phone number in string.
			results ^= extensiveMDNCheckRegularMethod(mdn[index][index2]);
		}
		return results;
	}
	public boolean timeExtensiveMDNRegexCheck(int reps){
		boolean results = false;
		for(int i = 0; i<reps; i ++){
			for(int index2=0; index2<mdn.length; index2++)
				//user Regular expression to check the phone number in string.
			results ^=mdnExtensiveCheckRegEx(mdn[index][index2]);
		}
		return results;
	}
	
	public boolean extensiveMDNCheckRegularMethod(String mdn){
		//strip the character which not numeric or 'x' character.
		String stripedmdn = stripString(mdn);
		
		
		if(stripedmdn.length() >= 10 && stripedmdn.length() <= 11 && (!stripedmdn.contains("x") || !stripedmdn.contains("X"))){
			//For following condition 
			//1-123-456-7868 or  123-456-7868
			return true;
		}else if ( stripedmdn.length() >= 15 && stripedmdn.length() <= 16  ) {
			//1-123-456-7868 ext 2345 or  123-456-7868 ext 2345
			//
			if ( stripedmdn.contains("x") ) {
				int index = stripedmdn.indexOf("x");
				if(index >= 9 && index <= 10){
					return true;
				}
			}else if( stripedmdn.contains("X") ) {
				int index = stripedmdn.indexOf("X");
				if(index >= 9 && index <= 10){
					return true;
				}
			}	
		}
		return false;
	}
	/**
	 * Strip the other character and leave only x and numeric values.
	 * @param extendedMdn
	 * @return
	 */
	public String stripString(String extendedMdn){
		byte mdn[] = new byte[extendedMdn.length()];
		int index = 0;
		for(byte b : extendedMdn.getBytes()){
			if((b >= '0' && b <='9') || b == 'x'){
				mdn[index++] = b;
			}
		}
		return new String(mdn);
	}
	
	private boolean mdnExtensiveCheckRegEx(String mdn){
		return EXTENSIVE_REGEX_PATTERN.matcher(mdn).matches();
	}
}