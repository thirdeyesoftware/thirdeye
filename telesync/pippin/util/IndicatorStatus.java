package pippin.util;

import java.io.Serializable;

public class IndicatorStatus implements Serializable {
	private boolean mError;
	private boolean mHistory;
	public static final long serialVersionUID = 2002110501L;

	public IndicatorStatus() {
		mError = false;
		mHistory = false;
	}
	public void setHistory(boolean b) {
		mHistory = b;
	}
	public void setError(boolean b) {
		mError = b;
	}
	public boolean getHistory() {
		return mHistory;
	}
	public boolean getError() {
		return mError;
	}
	public String toString() {
		return "error = " + mError + "\nHistory=" + mHistory;
	}
}
	