////
// //
// PippinSoft
//
//

package pippin.pui;

import java.util.LinkedList;
import pippin.util.Fifo;

/**
 * Singleton blocking queue used for component subscription requests.
 * 
 * @see PClient.addSubscriptionRequest()
 * @see PComponent.addStateChangeListener()
 */
public class SubscriptionQueue implements Runnable {
	
	private Thread mQueueEater;
	private LinkedList mQueue;
	
	private static final SubscriptionQueue mInstance = new SubscriptionQueue();
	
	private SubscriptionQueue() {
		mQueue = new LinkedList();
		mQueueEater = new Thread(this,"QueueConsumer");
		mQueueEater.setDaemon(true);
		mQueueEater.start();
	}
	
	public static SubscriptionQueue getInstance() {
		return mInstance;
	}
	
	/** 
	 * adds a QueueItem to the queue.
	 */
	public void addSubscriptionRequest(PComponent comp, PStateChangeListener lsnr) {
		synchronized (mQueue) {
			QueueItem item = new QueueItem(comp,lsnr);
			mQueue.add(item);
			mQueue.notify();
			//System.out.println("SubscriptionQueue.addSubscriptionRequest()-" + comp.getName());
		}
	}
	
	/**
	 * returns a queue item off of the queue.  This method
	 * blocks if the queue is empty.
	 */
	public QueueItem get() throws InterruptedException  {
		synchronized (mQueue) {
			if (mQueue.isEmpty()) {
				mQueue.wait();
			}
			return (QueueItem)mQueue.removeFirst();
		}
	}
		

	class QueueItem {
		private PComponent mComponent;
		private PStateChangeListener mListener;
		
		public QueueItem(PComponent comp, PStateChangeListener lsnr) {
			mComponent = comp;
			mListener = lsnr;
		}
		public PComponent getComponent() {
			return mComponent;
		}
		public PStateChangeListener getListener() {
			return mListener;
		}
	}
	
	public void run() {
		QueueItem queueItem;

		for (;;) {
			try {
				// get() will block if no items are on the queue.
				queueItem = get();
				queueItem.getComponent().subscribe(queueItem.getListener());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	
	}
	
			
}

					
	