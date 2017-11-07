////
//
// Telesync 5320 Project
//
//

package telesync.gui.rxdatapath;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import pippin.pui.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;



public class STSLevel {
	static public STSLevel STS_1 = new STSLevel(1);
	static public STSLevel STS_3 = new STSLevel(3, STS_1);
	static public STSLevel STS_12 = new STSLevel(12, STS_3);
	static public STSLevel STS_48 = new STSLevel(48, STS_12);
	static public STSLevel STS_192 = new STSLevel(192, STS_48);
	
	private int mLevel;
	private STSLevel mChildLevel = null;

	private STSLevel(int level) {
		this(level, null);
	}


	private STSLevel(int level, STSLevel childLevel) {
		mLevel = level;
		mChildLevel = childLevel;
	}


	public String toString() {
		return toString(Mode.US_SONET);
	}


	public String toString(Mode mode) {
		String s;

		if (mode.equals(Mode.US_SONET)) {
			s = "STS-" + mLevel;

		} else {
			s = "STM-" + mLevel / 3;
		}

		return s;
	}


	public int hashCode() {
		return mLevel;
	}


	public boolean equals(Object o) {
		if (o instanceof STSLevel) {
			return ((STSLevel) o).getLevel() == getLevel();
		} else {
			return false;
		}
	}


	public STSLevel getChildLevel() {
		return mChildLevel;
	}


	public int getChildCount() {
		STSLevel c = getChildLevel();

		if (c != null) {
			return getLevel() / c.getLevel();
		} else {
			return 0;
		}
	}


	protected int getLevel() {
		return mLevel;
	}
}
