package telesync.qov;

public class INPUTDATA {
		String mFilename;
		WAVEDATA mWaveData;
		int mByteOrder;
		float mScaling;
		long mPreviousData;
		
		public INPUTDATA() {
			
		}
		public String getFilename() {
			return mFilename;
		}
		public void setFilename( String filename ) {
			mFilename = filename;
		}
		public WAVEDATA getWaveData() {
			return mWaveData;
		}
		public void setWaveData( WAVEDATA wavedata ) {
			mWaveData = wavedata;
		}
		public int getByteOrder() {
			return mByteOrder;
		}
		public void setByteOrder( int byteorder ) {
			mByteOrder = byteorder;
		}
		public float getScaling() {
			return mScaling;
		}
		public void setScaling( float scaling ) {
			mScaling = scaling;
		}
		public long getPreviousData() {
			return mPreviousData;
		}
		public void setPreviousData( long data ) {
			mPreviousData = data;
		}
	} //end INPUTDATA
	
	