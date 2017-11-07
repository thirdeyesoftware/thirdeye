/* Results Class */
/* used to return results from Malden JNI Library Wrapper */

package telesync.qov;

import java.io.Serializable;
import java.util.Hashtable;

public class Results implements Serializable {

	static String[] fields = { "PAMSlisteningEffort", "PAMSlisteningQuality", "notSpecified", "PAMSavgUtteranceTimeOffset",
											"PAMSoffsetConfidence", "referenceActivityLevel", "referenceActivity",
											"referenceNoise", "referencePeak", "degradedActivityLevel", "degradedActivity",
											"degradedNoise", "degradedPeak", "PAMSmaxUtteranceTimeOffset", "PAMSminUtteranceTimeOffset", 
											"PAMSstdDeviationOfOffsets", "PAMSnumOfUtterances", "PAMSpercentOfDegFileMuted", "PAMSlongestMuteSection",
											
											"PSQMvalue", "PSQM+Value", "PSQMvalueSilentIntervalWeightingFactor", 
											
											"PESQscore", "PESQavgUtteranceTimeOffset", "PESQoffsetConfidence",
											"PESQmaxUtteranceTimeOffset", "PESQminUtteranceTimeOffset", "PESQstdDeviationOfOffsets", 
											"PESQnumOfUtterances", };
											
											
	private float[] mResults;
	
	public Results(float[] results) {
		mResults = results;
	}
	
	public float[] getRawResults() {
		return mResults;
	}
	
	public Hashtable getHashResults() {
		int i = 0, j = 0;
		Hashtable results = new Hashtable();
		
		
		
			for (i=0;i<19;i++) {
				
				results.put( fields[i], new Float(mResults[i]));
			}
			
			for (i=31;i<34;i++) {
				j=19;
				results.put( fields[j], new Float(mResults[i]));
				j++;
			}
			
			for (i=50;i<57;i++) { 
				j=22;
				results.put( fields[j], new Float(mResults[i]));
				j++;
			}
			
	
		return results;
	}
	
	public float getPAMSlisteningEffort() {
		return mResults[0];
	}
	public float getPAMSlisteningQuality() {
		return mResults[1];
	}
	public float getPAMSavgUtteranceTimeOffset() {
		return mResults[3];
	}
	public float getPAMSoffsetConfidence() {
		return mResults[4];
	}
	public float getReferenceActivityLevel() {
		return mResults[5];
	}
	public float getReferenceActivity() {
		return mResults[6];
	}
	public float getReferenceNoise() {
		return mResults[7];
	}
	public float getReferencePeak() {
		return mResults[8];
	}
	public float getDegradedActivityLevel() {
		return mResults[9];
	}
	public float getDegradedActivity() {
		return mResults[10];
	}
	public float getDegradedNoise() {
		return mResults[11];
	}
	public float getDegradedPeak() {
		return mResults[12];
	}
	public float getPAMSmaxUtteranceTimeOffset() {
		return mResults[13];
	}
	public float getPAMSminUtteranceTimeOffset() {
		return mResults[14];
	}
	public float getPAMSdeviationOfOffsets() {
		return mResults[15];
	}
	public float getPAMSnumOfUtterances() {
		return mResults[16];
	}
	public float getPAMSpercentOfDegFileMuted() {
		return mResults[17];
	}
	public float getPAMSlongestMuteSection() {
		return mResults[18];
	}
	public float getPSQMvalue() {
		return mResults[31];
	}
	public float getPSQMplusValue() {
		return mResults[32];
	}
	public float getPSQMvalueSilentIntervalWeightingFactor() {
		return mResults[33];
	}
	public float getPESQscore() {
		return mResults[50];
	}
	public float getPESQavgUtteranceTimeOffset() {
		return mResults[51];
	}
	public float getPESQoffsetConfidence() {
			return mResults[52];
	}
	public float getPESQmaxUtteranceTimeOffset() {
			return mResults[53];
	}
	public float getPESQminUtteranceTimeOffset() {
			return mResults[54];
	}
	public float getPESQstdDeviationOfOffsets() {
			return mResults[55];
	}
	public float getPESQnumOfUtterances() {
			return mResults[56];
	}
}
