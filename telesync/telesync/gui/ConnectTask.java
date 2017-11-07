package telesync.gui;

/** ConnectTask is a dummy process to increment the progress bar. 
 *
 */
import java.lang.Thread;

public class ConnectTask {
	static int length = 1000;
	private int currentStep = 0;
	
	public ConnectTask() {
		currentStep = 0;
	}
	public int getCurrentStep() {
		return currentStep;
	}
	
	public void startTask() {
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				return new Task();
			}
		};
		worker.start();
	}
	public void stop() {
		currentStep = length;
	}
	public int getLength() {
		return length;
	}
	
	public boolean isDone() {
		if (currentStep >= length) 
			return true;
		else 
			return false;
	}

	class Task {
		Task() {
			while (currentStep < length) {
				try {
					Thread.sleep(500);
					currentStep += 1;
					if (currentStep==length) currentStep = 0; //ensure infinite loop.... lose this to add timeout...
				}
				catch (InterruptedException ie) {
				}
			}
		}
	} //end class Task
	
	
			
			
}
