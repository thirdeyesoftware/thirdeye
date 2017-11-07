package telesync.qov;

	public class WAVEFORMATEX {
		public int mFormatTag;
		public int mChannels;
		public long mSamplesPerSec;
		public long mAvgBytesPerSec;
		public int mBlockAlign;
		public int mBitsPerSample;
		public int mSize;
		
		public WAVEFORMATEX() {
			
		}
		
		
		public int getFormatTag() {
			return mFormatTag;
		}
		public void setFormatTag( int tag ) {
			mFormatTag = tag;
		}
		public int getChannels() {
			return mChannels;
		}
		public void setChannels( int channels ) {
			mChannels = channels;
		}
		public long getSamplesPerSec() {
			return mSamplesPerSec;
		}
		public void setSamplesPerSec( long samples ) {
			mSamplesPerSec = samples;
		}
		public long getAvgBytesPerSec() {
			return mAvgBytesPerSec;
		}
		public int getBlockAlign() {
			return mBlockAlign;
		}
		public void setBlockAlign( int align ) {
			mBlockAlign = align;
		}
		public int getBitsPerSample() {
			return mBitsPerSample;
		}
		public void setBitsPerSample( int bits ) {
			mBitsPerSample = bits;
		}
		public int getSize() {
			return mSize;
		}
		public void setSize( int size ) {
			mSize = size;
		}
	} //end WAVEFORMATEX
	
	
	