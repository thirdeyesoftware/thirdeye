package telesync.qov.rmi.client;

import java.rmi.*;
import java.math.*;
import java.io.*;
import telesync.qov.rmi.*;
import telesync.qov.*;

/* this class demonstrates the usage of CompareEngine (MelSPA QoV) Java Wrapper */

public class CompareClient {
    public static void main(String args[]) {
        
        try {
					String referenceFilename = "", degradedFilename = "";
	  			if (args.length < 3) {
						System.out.println("usage : CompareClient <referencefile> <degradedfile> <rmiHost>");
						return;
					}
					referenceFilename = args[0];
					degradedFilename = args[1];
						
					/* bind CompareEngine ... */
					String name = "//" + args[2] + "/Compare";

					Compare compareEngine = (Compare)Naming.lookup(name);

					/* get byte array from file */
					byte[] refBytes = getBytesFromFile( referenceFilename );


					/* construct serializable DataSample objects with byte arrays */

					DataSample refSample = new DataSample(refBytes);


					byte[] degBytes = getBytesFromFile( degradedFilename );

					DataSample degSample = new DataSample(degBytes);

					
						/* call compareEngine to do the work */
						/* returns Results */
						/* can query for raw float[], hashtable or individual getters */
						
					Results r = compareEngine.compareSamples(refSample,degSample);

					float[] results;
					/* this is float[] */
					results = r.getRawResults();

					/* this is hashtable */
					//System.out.println(r.getHashResults());

					/* this is a getter */
					System.out.println("\n\n" + r.getPAMSavgUtteranceTimeOffset());
            
        } catch (Exception e) {
            System.err.println("CompareClient exception: " + 
                               e.getMessage());
            e.printStackTrace();
        }
    }
    private static byte[] getBytesFromFile( String filename ) throws Exception {
			FileInputStream fileStream = new FileInputStream( filename );
       
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
			int a = 0;
			while ((a=fileStream.read()) != -1) {
				bout.write(a);
			}
			fileStream.close();
			byte[] ret = bout.toByteArray();
			
			bout.close();
			return ret;
		}
		
       
       	
								
}
