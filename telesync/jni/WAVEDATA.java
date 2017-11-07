package telesync.qov;

public class WAVEDATA {
		WAVEFORMATEX mWFX;
		int mExtendedData;
		String mList;
		long mPData;
		long mSize;
		
		public WAVEDATA() {
			
		}
		public WAVEFORMATEX getWaveFormatEx() {
			return mWFX;
		}
		public void setWaveFormatEx( WAVEFORMATEX wfx ) {
			mWFX = wfx;
		}
		public int getExtendedData() {
			return mExtendedData;
		}
		public void setExtendedData( int data ) {
			mExtendedData = data;
		}
		public String getList() {
			return mList;
		}
		public void setList( String list ) {
			mList = list;
		}
		public long getPData() {
			return mPData;
		}
		public void setPData( long data ) {
			mPData = data;
		}
		public long getSize() {
			return mSize;
		}
		public void setSize( long size ) {
			mSize = size;
		}
	} //end WAVEDATA
	
	