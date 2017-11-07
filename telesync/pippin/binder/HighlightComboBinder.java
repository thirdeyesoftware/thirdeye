   //
   //
   // Joseph A. Dudar, Inc (sorta)  (C) 2001
// //
////

package pippin.binder;



import java.awt.*;
import javax.swing.*;
import java.util.Vector;

public class HighlightComboBinder extends ComboBinder {

	Object mHighlightChoice;
	Color mHighlightColor;
	Color mRestColor = null;
	boolean mUseHighlight = true;
	
	public HighlightComboBinder( String title, Class commandTypeClass,
			Vector choices, Object highlightChoice) {
		
		this(title, commandTypeClass, highlightChoice, 
			UIManager.getColor("pippin.colors.titlePanelBG"));
		
		setChoices( choices );
	}
	public HighlightComboBinder( String title, Class commandTypeClass,
		Vector choices, Object highlightChoice, Color highlightColor) {
			this(title, commandTypeClass, highlightChoice, highlightColor);
			setChoices( choices );
	}
	
			
	public HighlightComboBinder( String title, Class commandTypeClass, 
		Object highlightChoice) {
			this(title, commandTypeClass, highlightChoice, UIManager.getColor("pippin.colors.titlePanelBG"));
			
	}
	
	public HighlightComboBinder( String title, Class commandTypeClass,
		Object highlightChoice, Color highlightColor) {
			super(title, commandTypeClass);
			mHighlightChoice = highlightChoice;
			mHighlightColor = highlightColor;
	}
	
	public void setBoundValue(Object value) {
		if (mUseHighlight) {
			
			if (value.equals(mHighlightChoice)) {
				setHighlight(mHighlightColor);
			}
			else {
				if (mRestColor != null) {
					setHighlight( mRestColor );
				} 
				else clearHighlight();
			}
		} 
		else clearHighlight();
		
		
		super.setBoundValue( value );
	}
	
	public void setRestColor(Color c) {
		mRestColor = c;
	}		
			
	public void setUseHighlight(boolean b) {
		mUseHighlight = b;
		if (!b) {
			clearHighlight();
		}
	}
}



