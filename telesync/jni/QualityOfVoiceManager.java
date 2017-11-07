/* QualityOfVoiceManager */
/* Java-side JNI Wrapper for Malden QoV C++ Library */

package telesync.qov;

import java.util.*;
import telesync.qov.*;

public class QualityOfVoiceManager {
	
	static long  P56_FN		=				0x0001;
	static long  PAMS_FN	=				0x0002;
	static long  SMJ_FN		=				0x0004;
	static long  MIRS_FN	=				0x0004;	
	static long  HOLD_FN	=				0x0010;
	static long  TF_DISABLE_FN  =	0x0020;
	static long  RAF_DISABLE_FN	=	0x0040;
	static long  DAF_DISABLE_FN =	0x0080;
	static long  PSQM_FN	=				0x0100;
	static long  PESQ_FN	=				0x0200;
	static long PESQ			=				0x0200;
	static long PSQM			=				0x0100;
	static long	SILENCE_W_2		=		0x0400;
	static long	SILENCE_W_4		=		0x0800;
	static long	SILENCE_W_6		=		0x0C00;
	static long	SILENCE_W_8		=		0x1000;
	static long	SILENCE_W_10 	=		0x1400;
	static long	SILENCE_W_BITS 	=	0x1C00;
	static long  PESQ_MODEL		=		0x6000;
	static long  DUMMY_FN	  	=		0x8000;
	
	public static long DEFAULT_TEST = PESQ | PSQM | SILENCE_W_2 | P56_FN | PAMS_FN | MIRS_FN | 
				HOLD_FN;
				
	/* native methods */			
	
	public native static long compare(long type, String reference, String degraded, String results);
	public native static float[] compare(long type, String reference, String degraded);
	
	/* -end- native methods */
	
		static {
			System.loadLibrary("QoVImpl");
		}
	
	private QualityOfVoiceManager() {}
	
	
	public static float[] compare(String ref, String deg) throws Exception {
		
		return compare(DEFAULT_TEST,ref, deg);

	}
	
	
	/// testing 1 2 3
	// replace ref.filename and degraded.filename literals with .wav samples on local file system.
	public static void main( String[] args ) {
		
		QualityOfVoiceManager manager = new QualityOfVoiceManager();
		
		String ref, degraded, results;
		long type = DEFAULT_TEST;
		
		ref = "C:\\projects\\ts\\QoV\\ASTS\\ALAW\\American\\irs\\female\\onadruts.wav";
		degraded = "C:\\projects\\ts\\QoV\\ASTS\\ALAW\\American\\irs\\female\\then.wav";
		results = "C:\\projects\\ts\\jni\\results.rst";
			
		
		long errcode  = 0;
		
		try {
			errcode = manager.compare(DEFAULT_TEST, ref, degraded, results);
		}
		catch (UnsatisfiedLinkError ule) {
			System.out.println(ule.toString());
		}
		
		float[] f;
		f = manager.compare(type, ref, degraded);
		
		/* print out results */
		for (int i=0;i<f.length;i++) {
			System.out.println("f " + i + " = " + f[i]);
		}
		
		
	}
	
	
	
			
		
}

	
