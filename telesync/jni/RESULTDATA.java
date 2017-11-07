package telesync.qov;

public class RESULTDATA {
		String mVersionID;
		String mCopyright1;
		String mCopyright2;
		String mDate;
		String mTime;
		String mDescription1;
		String mDescription2;
		String mDegSourceName;
		String mFilename; 														/*file pathname */
		String mConditionalFilename;									/*file pathname */
		String mAmplitudeUnits;												/*Units that measurements are made in */
		float mReserved;
		FAILCONDITIONS mGreaterThan;		/*pointer to structure containing storage conditions*/
		FAILCONDITIONS mLessThan;			/*pointer to structure containing storage conditions*/
		float[] mResults;													/*result values*/
		int  mRstOption;															/*v4.1 added 0 for none, 1 for store, 2 for store conditionally */
		String mRstFilename;		
	
		public RESULTDATA() {
			mResults = new float[100];
			mRstOption = 0;
			mGreaterThan = new FAILCONDITIONS();
			mLessThan = new FAILCONDITIONS();
			
		}
		public String getVersionID() {
			return mVersionID;
		}
		public void setVersionID( String version ) {
			mVersionID = version;
		}
		public String getCopyright1() {
			return mCopyright1;
		}
		public void setCopyright1( String val ) {
			mCopyright1 = val;
		}
		public String getCopyright2() {
			return mCopyright2;
		}
		public void setCopyright2( String val ) {
			mCopyright2 = val;
		}
		public String getDate() {
			return mDate;
		}
		public void setDate( String date ) {
			mDate = date;
		}
		public String getTime() {
			return mTime;
		}
		public void setTime( String time ) {
			mTime = time;
		}
		public String getDescription1() {
			return mDescription1;
		}
		public void setDescription1( String val ) {
			mDescription1 = val;
		}
		public String getDescription2() {
			return mDescription2;
		}
		public void setDescription2( String val ) {
			mDescription2 = val;
		}
		public String getDegSourceName() {
			return mDegSourceName;
		}
		public void setDegSourceName( String val ) {
			mDegSourceName = val;
		}
		public String getFilename() {
			return mFilename;
		}
		public void setFilename( String filename ) {
			mFilename = filename;
		}
		public String getConditionalFilename() {
			return mConditionalFilename;
		}
		public void setConditionalFilename( String val ) {
			mConditionalFilename = val;
		}
		public String getAmplitudeUnits() {
			return mAmplitudeUnits;
		}
		public void setAmplitudeUnits( String units ) {
			mAmplitudeUnits = units;
		}
		public FAILCONDITIONS getGreaterThan() {
			return mGreaterThan;
		}
		public void setGreaterThan( FAILCONDITIONS val ) {
			mGreaterThan = val;
		}
		public FAILCONDITIONS getLessThan() {
			return mLessThan;
		}
		public void setLessThan( FAILCONDITIONS val ) {
			mLessThan = val;
		}
		public float[] getResults() {
			return mResults;
		}
		public void setResults( float[] res ) {
			mResults = res;
		}
		public int getRstOption() {
			return mRstOption;
		}
		public void setRstOption( int val ) {
			mRstOption = val;
		}
		public String getRstFilename() {
			return mRstFilename;
		}
		public void setRstFilename( String file ) {
			mRstFilename = file;
		}
	} //end RESULTDATA