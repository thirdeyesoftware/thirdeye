   //
   //
   // Joseph A. Dudar, Inc   (C) 1999
// //
////

package pippin.binder;



import java.util.*;
import javax.swing.text.*;
import javax.swing.*;



public class BlockingKeymap implements Keymap {

  String nm;
  Keymap parent;
  Hashtable bindings;
  Vector blockedBindings;
  Action defaultAction;

  public BlockingKeymap(String nm, Keymap parent) {
		this.nm = nm; 
		this.parent = parent;
		bindings = new Hashtable();
		blockedBindings = new Vector();
  }

  /**
	* Fetch the default action to fire if a 
	* key is typed (ie a KEY_TYPED KeyEvent is received)
	* and there is no binding for it.  Typically this
	* would be some action that inserts text so that 
	* the keymap doesn't require an action for each 
	* possible key.
	*/
  public Action getDefaultAction() {
		if (defaultAction != null) {
			 return defaultAction;
		}
		return (parent != null) ? parent.getDefaultAction() : null;
  }

  /**
	* Set the default action to fire if a key is typed.
	*/
  public void setDefaultAction(Action a) {
		defaultAction = a;
  }

  public String getName() {
		return nm;
  }

  public Action getAction(KeyStroke key) {
		Action a = (Action) bindings.get(key);
		if ((a == null) && (parent != null) && !blockedBindings.contains(key)) {
			 a = parent.getAction(key);
		}
		return a;
  }

  public KeyStroke[] getBoundKeyStrokes() {
		KeyStroke[] keys = new KeyStroke[bindings.size()];
		int i = 0;
		for (Enumeration e = bindings.keys() ; e.hasMoreElements() ;) {
			 keys[i++] = (KeyStroke) e.nextElement();
		}
		return keys;
  } 

  public Action[] getBoundActions() {
		Action[] actions = new Action[bindings.size()];
		int i = 0;
		for (Enumeration e = bindings.elements() ; e.hasMoreElements() ;) {
			 actions[i++] = (Action) e.nextElement();
		}
		return actions;
  } 

	public KeyStroke[] getKeyStrokesForAction(Action a) {
		if (a == null) {
			return null;
		}

		KeyStroke[] retValue = null;

		// Determine local bindings first.
		Vector keyStrokes = null;
		for (Enumeration enum = bindings.keys(); enum.hasMoreElements();) {
			Object key = enum.nextElement();
			if (bindings.get(key) == a) {
				if (keyStrokes == null) {
					keyStrokes = new Vector();
				}
				keyStrokes.addElement(key);
			}
		}

		// See if the parent has any.
		if (parent != null) {
			KeyStroke[] pStrokes = parent.getKeyStrokesForAction(a);
			if (pStrokes != null) {

				int rCount = 0;

				// Remove blocked strokes
				for (int i = 0; i < pStrokes.length; ++i) {
					if (blockedBindings.contains(pStrokes[i])) {
						pStrokes[i] = null;
						rCount++;
					}
				}

				// Remove any bindings defined in the parent that
				// are locally defined.
				for (int i = pStrokes.length - 1; i >= 0; i--) {
					if (pStrokes[i] != null && isLocallyDefined(pStrokes[i])) {
						pStrokes[i] = null;
						rCount++;
					}
				}

				if (rCount > 0 && rCount < pStrokes.length) {
					if (keyStrokes == null) {
						keyStrokes = new Vector();
					}

					for (int counter = pStrokes.length - 1; counter >= 0;
							counter--) {
						if (pStrokes[counter] != null) {
							keyStrokes.addElement(pStrokes[counter]);
						}
					}
				} else
				if (rCount == 0) {
					if (keyStrokes == null) {
						retValue = pStrokes;
					} else {
						retValue = new KeyStroke[keyStrokes.size() +
								pStrokes.length];
						keyStrokes.copyInto(retValue);
						System.arraycopy(pStrokes, 0, retValue,
						keyStrokes.size(), pStrokes.length);
						keyStrokes = null;
					}
				}
			}
		}

		if (keyStrokes != null) {
			retValue = new KeyStroke[keyStrokes.size()];
			keyStrokes.copyInto(retValue);
		}
		return retValue;
	}

  public boolean isLocallyDefined(KeyStroke key) {
		return bindings.containsKey(key);
  }

  public void addActionForKeyStroke(KeyStroke key, Action a) {
		bindings.put(key, a);
  }

  public void removeKeyStrokeBinding(KeyStroke key) {
		bindings.remove(key);
  }

  public void blockKeyStrokeBinding(KeyStroke key) {
		blockedBindings.addElement(key);
  }

  public void removeBindings() {
		bindings.clear();
  }

  public Keymap getResolveParent() {
		return parent;
  }

  public void setResolveParent(Keymap parent) {
		this.parent = parent;
  }

  /**
	* String representation of the keymap... potentially 
	* a very long string.
	*/
  public String toString() {
		return "Keymap[" + nm + "]" + bindings;
  }
}
