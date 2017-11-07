package telesync.gui.latch;


public class DelayLatch implements Runnable {
	private long mDelayMillis;
	DelayLatchClient mClient;
	private boolean mClientState = false;
	private long mTimeoutMillis = 0;

	public DelayLatch(long delayMillis, DelayLatchClient client) {
		mDelayMillis = delayMillis;
		mClient = client;

		(new Thread(this)).start();
	}

	/**
	* This method is called to indicate the arrival of an event
	* that should drive the state of DelayLatchClient to true.
	*/
	public synchronized void eventNotify() {
		//System.out.println("DelayLatch.eventNotify: " + mClient.getName());

		mTimeoutMillis = System.currentTimeMillis() + mDelayMillis;
		//if (mClient.getClientState() == false) {
			//System.out.println("DelayLatch.eventNotify: SET " + mClient.getName());
			mClient.setClientState(true);
			mClientState = true;

			notify();
		//}
	}


	private synchronized void delayTimedOut() {
		//System.out.println("DelayLatch.delayTimedOut: " + mClient.getName());
		mClient.setClientState(false);
		mClientState = false;
	}


	public synchronized void run() {
		long remainingMillis;

		for ( ; ; ) {
			remainingMillis = mTimeoutMillis - System.currentTimeMillis();

			while (remainingMillis > 0) {
				try {
					wait(remainingMillis);
				}
				catch (InterruptedException ie) {
					ie.printStackTrace();
				}
				remainingMillis = mTimeoutMillis - System.currentTimeMillis();
			}

			delayTimedOut();

			try {
				wait();
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
}
