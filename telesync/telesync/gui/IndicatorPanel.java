package telesync.gui;

import pippin.binder.*;
import telesync.gui.latch.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;



public class IndicatorPanel extends JPanel implements ErrorEventListener {
	private JLabel mLabelHistory, mLabelError;
	private boolean mIsInverted = false;
	private static ImageIcon ERROR_ICON = ErrorIcon.getImageIcon();
	private static ImageIcon NO_INDICATOR_ICON = NoIndicatorIcon.getImageIcon();
	private static ImageIcon ERROR_HISTORY_ICON = ErrorHistoryIcon.getImageIcon();
	
	private DelayLatch mDelayLatch = null;
	private Object mValue;
	private JLabel mainLabel;
	private String mTitle;
	private boolean mDoAutoHistory;
	
	public IndicatorPanel(String title, String desc, boolean inverted, boolean doAutoHistory) {
		super();
		init(title,desc, inverted, doAutoHistory);
	}
	
		
	public IndicatorPanel(String title, String description) {
		this(title, description, false);
	}
	
	public IndicatorPanel(String title, String desc, boolean doAutoHistory) {
		this(title,desc,false,doAutoHistory);
	}
	
	public IndicatorPanel(String title) {
		this(title,null);
	}
	
	
	private void init(String title, String description, boolean inverted, boolean doAutoHistory) {
		mTitle = title;
		mDoAutoHistory = doAutoHistory;
		
		JPanel mainPanel = new JPanel();
		mainLabel = new JLabel(title);
		JPanel indicatorPanel = new JPanel();
		
		if (description != null) {
			mainLabel.setToolTipText(description);
		}
		
		mIsInverted = inverted;
		setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		
		mainLabel.setHorizontalAlignment(JLabel.CENTER);
		mainLabel.setFont( new Font("Arial",Font.BOLD,10) );
		mainPanel.setLayout( new BorderLayout() );
		mainPanel.setBorder( new CompoundBorder(new EtchedBorder(), new EmptyBorder(2,2,2,2)) );	
		mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(mainLabel, BorderLayout.NORTH);
		
		
		mLabelError = new JLabel(NO_INDICATOR_ICON);
		mLabelError.setToolTipText("Error Indicator");
		mLabelError.setAlignmentX(Component.CENTER_ALIGNMENT);
				
		mLabelHistory = new JLabel(NO_INDICATOR_ICON);
		mLabelHistory.setAlignmentX(Component.CENTER_ALIGNMENT);
		mLabelHistory.setToolTipText("Error History Indicator");
		indicatorPanel.setLayout( new BoxLayout(indicatorPanel, BoxLayout.X_AXIS) );
		
		
		indicatorPanel.add( mLabelError );
		indicatorPanel.add( mLabelHistory );
		indicatorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add( indicatorPanel, BorderLayout.CENTER );
		
		add( mainPanel );
		setBorder(null);
		
		setSize(mainPanel.getSize().width, mainPanel.getSize().height);
		
			
	}


	public void enableLatch(boolean timerEnabled) {
		//System.out.println("IndicatorPanel.enableLatch: " + mTitle + " " +
		//		timerEnabled);

		if (timerEnabled) {
			if (mDelayLatch == null) mDelayLatch = new DelayLatch(2000, new IndicatorPanelAdapter(this));
		} else mDelayLatch = null;
		
	}
	

	public boolean isInverted() {
		return mIsInverted;
	}

	public void setInverted(boolean invert) {
		mIsInverted = invert;
	}
	

	/* setHistoryIndicator() - illuminates the error history indicator */
	public void setHistoryIndicator( boolean isLit ) {
		if (isLit) {
			if (!isError()) {
				mLabelHistory.setIcon(ERROR_HISTORY_ICON);
			}
		} else {
			mLabelHistory.setIcon(NO_INDICATOR_ICON);
		}
	}
	public String getName() {
			return mTitle;
	}
	
	/* setErrorIndicator() - illumiates the error indicator 
	   toggles between NOT illuminated and illuminated
	*/
	public void setErrorIndicator( boolean isLit ) {
		boolean oldErrorState = isError();
		
		mLabelError.setIcon( (isLit) ? ERROR_ICON : NO_INDICATOR_ICON);

		if (oldErrorState && !isLit && isAutoHistoryEnabled()) {
			setHistoryIndicator(true);
		}
	}
	
	public boolean isAutoHistoryEnabled() {
		return mDoAutoHistory;
	}
	
	/* resetIndicators() - resets indicators to their non-illuminated states */
	public void resetIndicators() {
		mLabelError.setIcon(NO_INDICATOR_ICON);
		mLabelHistory.setIcon(NO_INDICATOR_ICON);
	}
	
	public void resetHistory() {
		mLabelHistory.setIcon(NO_INDICATOR_ICON);
	}
	public void resetError() {
		mLabelError.setIcon(NO_INDICATOR_ICON);
		if (mValue instanceof Boolean) {
			mValue = Boolean.FALSE;
		} else {
			mValue = new Integer(0);
		}
	}
	

	/* ErrorEventListener */
	public void errorEventNotify(ErrorEvent event) {
		//System.out.println("IndicatorPanel.errorEventNotify: " +
		//		mTitle + " " + event);
		
		Object value = event.getValue();
		boolean val = false;
		boolean oldVal = false;

		if (mValue==null) {
			if (value instanceof Boolean) {
				mValue = Boolean.FALSE;
			} else {
				mValue = (Object)new Integer(0); //this is to stop erroneous error indication on init.
			}
			
		}
		

		if (value != null) {
			
			if (value.getClass().isAssignableFrom(Boolean.class)) {
				val = ((Boolean)value).booleanValue();
				oldVal = ((Boolean) mValue).booleanValue();

				if (isInverted()) {
					val = !val;
				}
				
				//if (!mValue.equals(value)) {
				if (val) {
					setErrorIndicator(true);
					if (mDelayLatch != null) {
						mDelayLatch.eventNotify();
					} else {
						setErrorIndicator(true);
					}
				} else {
					setErrorIndicator( false );
					if (!hasHistory() && oldVal == true) {
						// setHistoryIndicator(true);
					}
				}
				mValue = value;
				//}
				
				
			} else if (value.getClass().isAssignableFrom(Long.class)) {
				val = ((Long)value).intValue() > 0;
				int vn = ((Long) value).intValue();
				int ovn = 0;
				
				if (mValue instanceof Long) {
					ovn = ((Long) mValue).intValue();
				} else
				if (mValue instanceof Integer) {
					ovn = ((Integer) mValue).intValue();
				} else {
					throw new Error("unexpect history value " + mValue);
				}

				mValue = value;

				
				if (val && vn != ovn) {
					//System.out.println("IndicatorPanel.errorEventNotify: " +
					//		mTitle + " " + vn + " != " + ovn);
					if (mDelayLatch != null) {
						//System.out.println("IndicatorPanel.errorEventNotify: " +
						//		mTitle + " " + event + " notify");
						mDelayLatch.eventNotify();
					} else {
						//System.out.println("IndicatorPanel.errorEventNotify: " +
						//		mTitle + " " + event + " no DelayLatch");
						setErrorIndicator( true );
					}
					
					if (!hasHistory() && !event.isInit()) {
						// setHistoryIndicator(true);
					}
				} else {
					//System.out.println("IndicatorPanel.errorEventNotify: " +
					//		mTitle + " " + val + " " + vn + " " + ovn);
				}
			} else {
				throw new Error("unexpect event value " + value);
			}
		}
	}


	private boolean hasHistory() {
		return (mLabelHistory.getIcon().equals(ERROR_HISTORY_ICON));
	}
	

	public boolean isError() {
		return (mLabelError.getIcon().equals(ERROR_ICON));
	}
	
	public DelayLatch getDelayLatch() {
		return mDelayLatch;
	}
	public void setDelayLatch( DelayLatch latch ) {
		//System.out.println("IndicatorPanel.setDelayLatch: " +
		//		mTitle + " " + latch);
		mDelayLatch = latch;
	}
	
	private static class IndicatorPanelAdapter implements DelayLatchClient {
		IndicatorPanel mIndicatorPanel;

		IndicatorPanelAdapter(IndicatorPanel indicatorPanel) {
			mIndicatorPanel = indicatorPanel;
		}


		public void setClientState(boolean clientState) {
			if (SwingUtilities.isEventDispatchThread()) {
				//System.out.println(mIndicatorPanel.getName() + " + " + clientState);
				mIndicatorPanel.setErrorIndicator(clientState);
			} else {
				SwingUtilities.invokeLater(new IndicatorPanelInvoker(this, clientState));
			}
		}
		public boolean getClientState() {
			return mIndicatorPanel.isError();
		}
		
		public String getName() {
			return mIndicatorPanel.mTitle;
		}
	}
		
		
	private static class IndicatorPanelInvoker implements Runnable {
		IndicatorPanelAdapter mAdapter;
		boolean mState;

		IndicatorPanelInvoker(IndicatorPanelAdapter adapter, boolean state) {
			mAdapter = adapter;
			mState = state;
		}

		public void run() {
			mAdapter.setClientState(mState);
		}
	}
		
	//test 1 2 3
	public static void main(String[] args) {
			JFrame f = new JFrame();
			IndicatorPanel prim, sec, tir;
			JPanel p = new JPanel();
			p.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
			
			p.add(prim = new IndicatorPanel("LOL"));
			p.add(sec = new IndicatorPanel("LOS"));
			p.add(tir = new IndicatorPanel("LOP"));
			p.add(new IndicatorPanel("B1"));
			p.add(new IndicatorPanel("B2"));
			p.add(new IndicatorPanel("B3"));
			
			f.getContentPane().add(p);
			prim.setHistoryIndicator( true );
			prim.setErrorIndicator( true );
			f.setSize(150,100);
			f.setVisible(true);
			f.addWindowListener( new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					System.exit(0);
				}
			});
			
			
			
	}
		
}
