////
// //
// PippinSoft
//
//

package pippin.binder;



import javax.swing.*;
import javax.accessibility.*;



/**
* This simple interface expresses the binding relation between
* a JComponent and a displayed and possibly editable value.
*/
public interface Binder {

	public static final boolean SHOW_COMPONENT = true;
	public static final boolean HIDE_COMPONENT = false;
	public static final boolean RECEIVE_EVENTS = true;
	public static final boolean HIDE_EVENTS = false;
	
	
	
	/**
	* @param binderSet
	* The BinderSet that contains this Binder.
	*
	* @see BinderSet#addBinder
	*/
	public void setBinderSet(BinderSet binderSet);


	public BinderSet getBinderSet();


	/**
	* Implementation must do type checking, and is further
	* responsible for propagating the value, as appropriate,
	* to the Binder's display component.
	*/
	public void setBoundValue(Object o);


	/**
	* @return
	* The bound value, or <code>null</code> if there is none.
	*/
	public Object getBoundValue();


	/**
	* @return
	* The class of the bound value.
	*/
	public Class getBoundType();


	/**
	* Enables or disables the entire Binder.  The specific implementation
	* is responsible for disabling and enabled all components used in
	* assembling the Binder.
	*
	* A disabled Binder is used for display purposes only.
	*/
	public void setEnabled(boolean enabled);


	/**
	* @return
	* The AccessibleContext associated with this Binder.
	*
	* Typically, this is handled implicitly by a subclass that is a
	* JComponent.  It is also frequently delegated to an aggregated
	* JComponent in the Binder implementation.
	*/
	public AccessibleContext getAccessibleContext();


	/**
	* Notifies the Binder that a parent Dialog was cancelled, and
	* the Binder should release resources as appropriate.
	*/
	public void cancel();


	/**
	* Sets the bound value of this Binder to null, or a default value.
	*/
	public void clear();


	/**
	* @return
	* <code>true</code> if the bound value of this Binder is valid
	* according to the Binder's internal rules for data validation. <p>
	*
	* This is a hook for subclasses to provide custom validation
	* rules.
	*/
	public boolean isValid();
}
