////
// //
// PippinSoft
//
//

package pippin.binder;



import java.util.*;



/**
* This simple interface describes any data entry or display form that
* uses Envelopes to populate its fields.
*/
public interface BinderSet {


	/**
	* @return
	* The Class of the subclass of TypeSet used by the BinderSet.
	*/
	public Class getTypeSetClass();


	/**
	* @return
	* The TypeSet instance used by this BinderSet.
	*/
	public TypeSet getTypeSet();


	/**
	* Set the values in the BinderSet according to the values in the
	* specified Envelope. <br>
	*
	* Only those elements present in the specified Envelope will be modified
	* in this BinderSet.  Use BinderSet.clear() to reset this
	* BinderSet prior to calling BinderSet.setEnvelope() in cases where
	* an "absolute" rather than an incremental set operation is needed.
	*/
	public void setEnvelope(Envelope e);


	/**
	* @return
	* An Envelope containing all current values.
	*/
	public Envelope getEnvelope();


	/**
	* Loads the specified Envelope with all current values.
	*/
	public void loadEnvelope(Envelope e);


	/**
	* Adds a Binder to this set.  Verifies the Binder's boundType
	* against the type specified in the TypeSet under the given name.
	* Does not allow redundant addBinder operations.
	*/
	public void addBinder(String name, Binder binder);


	/**
	* @return
	* <code>true</code> if this BinderSet contains a Binder under
	* the specified name.
	*/
	public boolean hasBinder(String name);
	

	/**
	* @return
	* The Binder stored at the specified name, or null if there is none.
	*/
	public Binder getBinder(String name);


	/**
	* @return
	* The boundValue of the Binder stored at the specified name,
	* or null if the Binder has no boundValue.
	*
	* @exception NoSuchBinderException
	* The named Binder is not in this BinderSet.
	*/
	public Object getBoundValue(String name);


	/**
	* Sets the boundValue of the Binder stored at the specified name
	* to the specified value.
	*
	* @exception NoSuchBinderException
	* The named Binder is not in this BinderSet.
	*/
	public void setBoundValue(String name, Object value);


	/**
	* Sets the boundValue of the Binder stored at the specified name
	* to the default value of the TypeSet returned by getTypeSet().  If
	* no default is known to the TypeSet, there is no effect.
	*
	* @exception NoSuchBinderException
	* The named Binder is not in this BinderSet.
	*/
	public void setDefaultBoundValue(String name);


	/**
	* @return
	* The name of the specified Binder, or null if there is no such Binder
	* in the set.
	*/
	public String getName(Binder binder);


	/**
	* @return
	* The Binder stored at the specified index.
	*
	* Throws ArrayIndexOutOfBoundsException if an invalid index is given.
	*/
	public Binder getBinderAt(int index);


	/**
	* Identical to addBinder(), but does not forbid redundancy.
	*/
	public void replaceBinder(String name, Binder binder);


	/**
	* Removes the Binder at the specified name.
	*/
	public void removeBinder(String name);
	

	/**
	* Adds all the Binders in the specified set to this BinderSet.
	* Does not allow redundancy.  Checks types.
	*/
	public void addBinderSet(BinderSet set);


	/**
	* @return
	* An Enumeration of all the names of Binders in this set.
	*
	* Names are presented in the order in which they were added to the set,
	* other than for Binders that were replaced using replaceBinder().
	*/
	public Enumeration getBinderNames();


	/**
	* @return
	* An Enumeration of all the Binders in this set.
	*
	* Binders are presented in the order in which they were added to the set,
	* other than for Binders that were replaced using replaceBinder().
	*/
	public Enumeration getBinders();


	/**
	* Enables or disables all bound components in the BinderSet.
	*/
	public void setEnabled(boolean enabled);


	/**
	* @return
	* The number of Binders in this set.
	*/
	public int getBinderSetSize();


	/**
	* Notifies all Binders that they may dispose of resources.
	* The user has cancelled the pending operation in which the
	* BinderSet is participating.
	*/
	public void cancel();


	/**
	* Clears the contents of all Binders so that Binder.getBoundValue
	* returns null (or possibly a default value.)
	*/
	public void clear();


	/**
	* @return
	* A Vector of the names of elements that are required in the
	* TypeSet or in this BinderSet, but as yet have no
	* values assigned to them in the specified Envelope.
	*/
	public Vector getRequiredNames(Envelope env);


	/**
	* Sets the 'required' status of the named element.  This
	* is a BinderSet-instance status that augments the
	* required status as described by the TypeSet.  An element is
	* required by the BinderSet if the BinderSet -or- the TypeSet
	* indicates that it is required.
	*/
	public void setRequiredName(String name, boolean required);
}
