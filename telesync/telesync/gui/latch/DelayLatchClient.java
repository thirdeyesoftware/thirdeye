package telesync.gui.latch;


public interface DelayLatchClient {
	public void setClientState(boolean state);

	public String getName();
	
	public boolean getClientState();
	
}
