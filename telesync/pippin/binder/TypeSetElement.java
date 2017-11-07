////
// //
// PippinSoft
//
//

package pippin.binder;



/**
* An element of a TypeSet.
*/
public class TypeSetElement {
	String mName;
	Class mType;
	boolean mIsRequired = false;
	Object mDefaultValue;
	String mLabel = null;
	ElementConverter mAccessConverter = null;
	ElementConverter mMutateConverter = null;

	/**
	* Indicates that this TypeSetElement's associated Envelope values
	* should be recorded in historical caches.
	*/
	boolean mIsHistorical = true;

	/**
	* Indicates that this TypeSetElement's associated Envelope values
	* represent a continuous state that holds the current value for
	* all points in time until the next event that changes the state.
	*/
	boolean mIsContinuous = true;


	/**
	* @param name
	* The name of this TypeSetElement.  Information on this instance
	* is retrieved from the enclosing TypeSet using this name as a key.
	*
	* @param type
	* The Class of values taken by this TypeSetElement's corresponding
	* element in an Envelope.
	*
	* @param isRequired
	* Indicates that this TypeSetElement is a required field.
	*
	* @param defaultValue
	* An optional default that is used when a value has not been
	* assigned to this TypeSetElement's corresponding Envelope element.
	*/
	public TypeSetElement(String name, Class type,
			boolean isRequired, Object defaultValue) {

		if (name == null || type == null) {
			throw new NullPointerException();
		}

		mName = name;
		mType = type;
		mIsRequired = isRequired;
		mDefaultValue = defaultValue;

		if (defaultValue != null && !isValidType(defaultValue.getClass())) {
			throw new ClassCastException(mDefaultValue.getClass().getName() +
					" is not a " + mType.getName());
		}
	}


	public TypeSetElement(String name, Class type) {
		this(name, type, false, null);
	}


	public TypeSetElement(String name, Class type, boolean isRequired) {
		this(name, type, isRequired, null);
	}


	public String getName() {
		return mName;
	}


	public boolean hasDefault() {
		return mDefaultValue != null;
	}


	public Object getDefaultValue() {
		return mDefaultValue;
	}


	public boolean isRequired() {
		return mIsRequired;
	}


	public Class getType() {
		return mType;
	}


	public boolean isValidType(Class type) {
		return mType.isAssignableFrom(type);
	}


	/**
	* @param isHistorical
	* Indicates that changes to this TypeSetElement's correponding
	* Envelope value are historically significant.
	*
	* The default value is <code>true</code>.
	*/
	protected void setHistorical(boolean isHistorical) {
		mIsHistorical = isHistorical;
	}


	protected boolean isHistorical() {
		return mIsHistorical;
	}


	/**
	* @param isContinuous
	* Indicates that this TypeSetElement's correponding
	* Envelope value represents a continuous state of its associated
	* entity.
	*
	* The default value is <code>true</code>.
	*/
	protected void setContinuous(boolean isContinuous) {
		mIsContinuous = isContinuous;
	}


	protected boolean isContinuous() {
		return mIsContinuous;
	}


	/**
	* Sets the preferred UI label for this TypeSetElement.
	*/
	protected void setLabel(String label) {
		mLabel = label;
	}


	/**
	* @return
	* The preferred UI label for EnvelopeElements corresponding
	* to this TypeSetElement.
	*/
	public String getLabel() {
		return mLabel;
	}


	/**
	* Sets the optional type conversion policy used by accessConvert().
	*/
	public void setAccessConverter(ElementConverter accessConverter) {
		mAccessConverter = accessConverter;
	}


	/**
	* Optional type conversion typically used for remapping
	* enumerated types. <p>
	*
	* This method is invoked to perform optional Object replacement
	* of PEnvelope elements as they are read from the PEnvelope and
	* stored in a PComponent.  The method name is confusing since
	* accessConvert refers to the process of accessing the actual
	* entity in the pui appliance, not the client-side PComponent. <p>
	*
	* @see pippin.binder.pui.PEnvelope#applyToPComponent
	*
	* This method is used in conjunction with Envelope.getElement().
	*/
	public Object accessConvert(Object o) {
		if (mAccessConverter != null) {
			o = mAccessConverter.convert(o);
		}

		return o;
	}


	/**
	* Sets the optional type conversion policy used by mutateConvert().
	*/
	public void setMutateConverter(ElementConverter mutateConverter) {
		mMutateConverter = mutateConverter;
	}


	/**
	* Optional type conversion typically used for remapping
	* enumerated types. <br>
	*
	* This method is used in conjunction with Envelope.putElement().
	*/
	public Object mutateConvert(Object o) {
		if (mMutateConverter != null) {
			o = mMutateConverter.convert(o);
		}

		return o;
	}
}
