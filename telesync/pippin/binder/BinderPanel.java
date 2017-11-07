   //
   //
   // Joseph A. Dudar, Inc   (C) 1999
// //
////

package pippin.binder;



import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
* This is a basic GUI panel that implements the BinderSet interface.
*/
public class BinderPanel extends JPanel implements BinderSet {

	BinderSetSupport mBinderSetSupport;
	private Vector mBinderEventListeners = new Vector();
	
	public BinderPanel(TypeSet typeSet) {
		mBinderSetSupport = new BinderSetSupport(this, typeSet);

		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						Component[] components = getComponents();
						if (components.length > 0) {
							components[0].requestFocus();
						}
					}
				});
			}
			public void focusLost(FocusEvent event) {
			}
		});

		setOpaque(false);
	}


	/**
	* Hook for notifying this BinderPanel that the JFrame
	* it composes is being shown.
	*
	* @see pippin.binder.BinderDialog#setVisible
	*/
	public void start() {
	}


	/**
	* Hook for notifying this BinderPanel that the JFrame
	* it composes is being disposed.
	*
	* @see pippin.binder.BinderDialog#dispose
	*/
	public void dispose() {
	}


	public Class getTypeSetClass() {
		return mBinderSetSupport.getTypeSetClass();
	}


	public TypeSet getTypeSet() {
		return mBinderSetSupport.getTypeSet();
	}


	public void setEnvelope(Envelope e) {
		
		mBinderSetSupport.setEnvelope(e);
	}
	public void setEnvelope(Envelope e, boolean init) {
		mBinderSetSupport.setEnvelope(e,init);
	}
	

	public Envelope getEnvelope() {
		return mBinderSetSupport.getEnvelope();
	}


	public void loadEnvelope(Envelope e) {
		mBinderSetSupport.loadEnvelope(e);
	}


	/**
	* This implementation (via BinderSetSupport) sets the
	* Binder to the default value if a default is present in the TypeSet.
	*/
	public void addBinder(String name, Binder binder) {
		mBinderSetSupport.addBinder(name, binder);
	}

	public void setBinderVisible(String name, boolean vis) {
			mBinderSetSupport.setBinderVisible(name, vis);
	}
	
	/**
	* Automatically creates the Binder appropriate to the
	* specified named element in the TypeSet and adds the
	* new Binder to the panel as a JComponent and as a Binder.
	*/
	public void addBinder(String name) {
		addBinder(name,true);
	}

	public void addBinder(String name, boolean isVisible) {
		Binder b = createBinder(name);
		addBinder(name,b);
		if (isVisible) add((JComponent)b);
	}
	
		

	public Binder createBinder(String name) {
		Binder b = null;
		TypeSetElement tse = getTypeSet().getElement(name);

		b = createBinderImpl(tse);

		if (b == null) {
			throw new Error("Unknown type " + tse.getType());
		}

		return b;
	}


	/**
	* Hook providing an opportunity for subclasses to
	* create automatic Binder instances for addBinder(String).
	*/
	protected Binder createBinderImpl(TypeSetElement element) {
		return null;
	}


	public boolean hasBinder(String name) {
		return mBinderSetSupport.hasBinder(name);
	}


	public Binder getBinder(String name) {
		return mBinderSetSupport.getBinder(name);
	}


	public Object getBoundValue(String name) {
		return mBinderSetSupport.getBoundValue(name);
	}


	public void setBoundValue(String name, Object value) {
		mBinderSetSupport.setBoundValue(name, value);
	}


	public void setDefaultBoundValue(String name) {
		mBinderSetSupport.setDefaultBoundValue(name);
	}


	public String getName(Binder binder) {
		return mBinderSetSupport.getName(binder);
	}


	public Binder getBinderAt(int index) {
		return mBinderSetSupport.getBinderAt(index);
	}


	public void replaceBinder(String name, Binder binder) {
		mBinderSetSupport.replaceBinder(name, binder);
	}


	public void removeBinder(String name) {
		mBinderSetSupport.removeBinder(name);
	}


	public void addBinderSet(BinderSet set) {
		mBinderSetSupport.addBinderSet(set);
	}


	public Enumeration getBinderNames() {
		return mBinderSetSupport.getBinderNames();
	}


	public Enumeration getBinders() {
		return mBinderSetSupport.getBinders();
	}


	public void setEnabled(boolean enabled) {
		mBinderSetSupport.setEnabled(enabled);
	}


	public int getBinderSetSize() {
		return mBinderSetSupport.getBinderSetSize();
	}


	public void cancel() {
		mBinderSetSupport.cancel();
	}


	public void clear() {
		mBinderSetSupport.clear();
	}


	public Vector getRequiredNames() {
		return getRequiredNames(getEnvelope());
	}


	public Vector getRequiredNames(Envelope env) {
		return mBinderSetSupport.getRequiredNames(env);
	}


	public void setRequiredName(String name, boolean required) {
		mBinderSetSupport.setRequiredName(name, required);
	}
	
	
	public void clearBinderFlags() {
		mBinderSetSupport.clearBinderFlags();
	}
	public void raiseFlag() {
		mBinderSetSupport.raiseFlag();
	}
	public void addFlagListener(FlagListener l) {
		mBinderSetSupport.addFlagListener(l);
	}
	
}
