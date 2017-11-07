/* DataSample */
/* Serializable entity to pass source and degraded sample data to/from Malden RMI Wrapper */

package telesync.qov.rmi;

import java.io.*;

public class DataSample implements Serializable {
	transient byte[] mStream;
	static private int mVersion = 2001092401;
	
	public DataSample() {
	}
	
	public DataSample(byte[] stream) {
		mStream = stream;
	}
	
	public byte[] getStream() {
		return mStream;
	}
	
	private void writeObject( ObjectOutputStream os ) throws IOException {
		os.writeInt( mVersion );
		
		os.writeInt( mStream.length );
		os.write(mStream);
		os.flush();
		
	}
	
	private void readObject( ObjectInputStream is ) throws ClassNotFoundException, IOException {
		init();
		int ver = is.readInt();
		
		if (ver != mVersion) {
			throw new IOException("DataSample.readObject() : incorrect version");
		}
		
		int length = is.readInt();
		
		mStream = new byte[length];
		
		is.readFully(mStream);
		
			
	}
	
		
	private void init() {
		/* do some work to work around constructor not being called upon activation */
		
	}
	
		
}
