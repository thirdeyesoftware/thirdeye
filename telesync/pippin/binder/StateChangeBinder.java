package pippin.binder;

public interface StateChangeBinder {
	public void setBoundValue( Object value, long eventTime );
	public void resetChangeIndicator();
	public void setEnableIndicator( boolean enable );
	public boolean indicatorEnabled();
	public boolean isIndicatorRaised();
	
}
