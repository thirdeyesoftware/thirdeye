package pippin.util;


import java.util.*;


class FifoBlock {
	protected Object[] mBuffer;
	int mHead,mTail,mSize;

	FifoBlock mNext;

	FifoBlock(int size) {
		mBuffer = new Object[size];
		mHead = mTail = 0;
		mSize = size;
	}

	boolean isEmpty() {
		return mHead == mTail;
	}

	boolean isFull() {
		return mHead == mSize;
	}

	void put(Object o) {
		mBuffer[mHead++] = o;
	}

	Object get() {
		Object o = mBuffer[mTail];

		mBuffer[mTail] = null;	// kill the ref! (or joe will kill me!)
		if (++mTail == mHead)
			mTail = mHead = 0;

		return o;
	}
	
	Object peek() {
		return mBuffer[mTail];
	}

	int getSize() {
		return mHead - mTail;
	}
	
}


public class Fifo {
	protected FifoBlock mHead,mTail;
	private int mGetWaiters;

	public Fifo() {
		mHead = mTail = new FifoBlock(64);
	}

	public synchronized int getSize() {
		int size = 0;

		for ( FifoBlock b = mHead ; b != null ; b = b.mNext)
			size += b.getSize();
			
		return size;
	}

	private boolean _isEmpty() {
		return mHead.isEmpty();
	}

	public synchronized boolean isEmpty() {
		return _isEmpty();
	}


	public synchronized void put(Object o) {
		if (mTail.isFull()) {
			FifoBlock b = new FifoBlock(64);

			mTail.mNext = b;
			mTail = b;
		}

		mTail.put(o);

		if (mGetWaiters != 0)
			notify();
	}
	


	/**
	* Loads all available fifo entries into the specified Vector.
	* Blocks if there are none available.
	*
	* @return
	* The specified Vector, populated with available fifo entries,
	* or <code>null</code> if the Fifo has been stopped.
	*
	* @deprecated
	*/
	public Vector getAll(Vector fifoEntries) {
		Object m;
		int size = getSize();

		m = get();
		if (m != null) {
			fifoEntries.addElement(m);

			for (int i = 0; i < size && (m = get(true)) != null; ++i) {
				fifoEntries.addElement(m);
			}
		} else {
			fifoEntries = null;
		}

		return fifoEntries;
	}


	public synchronized Object get() {
		return _get(false);
	}


	public synchronized Object get(boolean dontBlock) {
		return _get(dontBlock);
	}


	private Object _get(boolean dontBlock) {
		while (mHead.isEmpty()) {
			if (dontBlock)
				return null;

			++mGetWaiters;
			try {
				wait();
			} catch (InterruptedException ex) {
			}
			--mGetWaiters;
		}

		Object o = mHead.get();

		if (mHead.isEmpty() && mHead != mTail) {
			mHead = mHead.mNext;
		}

		return o;
	}


	public synchronized Object peek() {
		if (mHead.isEmpty())
			return null;
		return mHead.peek();
	}


	public boolean hasMore() {
		return !mHead.isEmpty();
	}
	

	
			
}
