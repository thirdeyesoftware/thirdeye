////
//
// Telesync 5320 Project
//
//

package telesync.gui.rxdatapath;



import java.util.*;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import pippin.pui.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;
import telesync.gui.*;




public class DataPathNode implements TreeNode, PStateChangeListener {
	private STSLevel mSTSLevel;
	private Vector mChildren = null;
	private boolean mIsConcatenated = false;
	private TreeNode mParent;
	private String mComponentName;
	private int mSTSIndex;
	private ButtonForm mForm = null;
	private Rx48datapath mDataPath;
	private PComponent mTributary = null;
	private boolean mIsActivated = false;
	static private TypeSet cRateEnabledTypeSet = new RateEnabledTypeSet();
	private DataPathPanel mDataPathPanel;
	private Mode mMode = Mode.US_SONET;
	private boolean mSetupLock = true;
	private boolean mClearFlag = false;
	private boolean mUserRateEnabled = false;
	private Rx48 mCard;
	
	private Hashtable mErrorEventListeners = new Hashtable();
	
	public static DataPathNode createRootNode(Rx48datapath dp,
			DataPathPanel dataPathPanel, Rx48 card, Mode mode) {
		return new DataPathNode(dp, dataPathPanel, card, STSLevel.STS_48, mode);
	}
	public static DataPathNode createRootNode(Rx48datapath dp,
			DataPathPanel dataPathPanel, Rx48 card, Mode mode, boolean setupLock) {
		return new DataPathNode(dp, dataPathPanel, card, STSLevel.STS_48, mode, null, 0);
	}
	
	private DataPathNode(Rx48datapath dp, DataPathPanel dataPathPanel, Rx48 card, 
			STSLevel stsLevel, Mode mode) {
		this(dp, dataPathPanel, card, stsLevel, mode, null, 0);
	}
	
	private DataPathNode(Rx48datapath dp, DataPathPanel dataPathPanel, Rx48 card, 
			STSLevel stsLevel, Mode mode, boolean setupLock) {
		this(dp, dataPathPanel, card, stsLevel, mode, null, 0);
		mSetupLock = setupLock;
	}
	
	private DataPathNode(Rx48datapath dp, DataPathPanel dataPathPanel, Rx48 card, 
			STSLevel stsLevel, Mode mode, TreeNode parent, int index, boolean setupLock) {
			
			this(dp, dataPathPanel, card, stsLevel, mode, parent, index);
			mSetupLock = setupLock;
			
	}
	

	private DataPathNode(Rx48datapath dp, DataPathPanel dataPathPanel, Rx48 card, 
			STSLevel stsLevel, Mode mode, TreeNode parent, int index) {
		
		mCard = card;
		mSTSLevel = stsLevel;
		mChildren = new Vector();
		mParent = parent;
		mDataPath = dp;
		mMode = mode;
		mDataPathPanel = dataPathPanel;
		

		if (stsLevel.equals(STSLevel.STS_48)) {
			setComponentName("oc48c");
			activate();
		} else
		if (stsLevel.equals(STSLevel.STS_12)) {
			mSTSIndex = index * 4 + 1;
			setComponentName("sts12c-" + mSTSIndex);
		} else
		if (stsLevel.equals(STSLevel.STS_3)) {
			mSTSIndex = ((DataPathNode) getParent()).getSTSIndex() + index;
			setComponentName("sts3c-" + mSTSIndex);
		} else {
			mSTSIndex = index + 1;
			setComponentName("sts1-" +
					((DataPathNode) getParent()).getSTSIndex() +
					"," + mSTSIndex);
		}

		
		
		for (int i = 0; i < stsLevel.getChildCount(); ++i) {
			mChildren.addElement(
					new DataPathNode(dp, dataPathPanel, mCard,
					stsLevel.getChildLevel(), mode, this, i));
			
		}

		
		if (stsLevel.equals(STSLevel.STS_48)) {
		
			activate();
		
			//activateChildren();
		
		}
		
		
	}

	public void setUserRateEnabled(boolean b) {
		mUserRateEnabled = b;
	}
	public boolean isUserRateEnabled() {
		return mUserRateEnabled;
	}
	

	public void setConcatenated(boolean isConcatenated) {
		mIsConcatenated = isConcatenated;

		
		
		if (mForm != null) {
			mForm.setTitle(this.toString());
		}
		
		
		
	}

	private void touch() {
		
		
		DefaultTreeModel treeModel = mDataPathPanel.getTreeModel();
		if (treeModel != null) treeModel.nodeChanged(this);
		
		
	}

	public boolean isConcatenated() {
		return mIsConcatenated;
	}


	public STSLevel getSTSLevel() {
		return mSTSLevel;
	}


	protected int getSTSIndex() {
		return mSTSIndex;
	}


	private void setComponentName(String componentName) {
		mComponentName = componentName;
	}


	public String getComponentName() {
		return mComponentName;
	}

	public void setSetupLock( boolean setupLock ) {
		mSetupLock = setupLock;
		if (mForm != null) {
			if (getSTSLevel().equals(STSLevel.STS_48)) {
				((OC48Panel)mForm).setSetupLock( setupLock );
			} else  {
				((TributaryPanel)mForm).setSetupLock( setupLock );
			}
		}
	}
	public boolean getSetupLock() {
		return mSetupLock;
	}
	

	public Enumeration children() {
		return mChildren.elements();
	}


	public boolean getAllowsChildren() {
		return !isLeaf();
	}


	public TreeNode getChildAt(int index) {
		return (TreeNode) mChildren.elementAt(index);
	}


	public int getChildCount() {
		return mChildren.size();
	}


	public int getIndex(TreeNode node) {
		return mChildren.indexOf(node);
	}


	public TreeNode getParent() {
		return mParent;
	}


	public boolean isLeaf() {
		if (getMode().equals(Mode.US_SONET)) {
			return mSTSLevel.equals(STSLevel.STS_1);
		} else {
			return mSTSLevel.equals(STSLevel.STS_3);
		}
	}


	protected Mode getMode() {
		return mMode;
	}


	public String toString() {
		STSLevel l = getSTSLevel();
		StringBuffer sb = new StringBuffer(16);

		sb.append(l.toString(getMode()));

		/*if (getMode().equals(Mode.US_SONET) &&
				isConcatenated() && !l.equals(STSLevel.STS_1)) {
			sb.append("c");
		}
		*/
		if (!l.equals(STSLevel.STS_1) && isConcatenated()) {
			sb.append("c");
		}
		if (!l.equals(STSLevel.STS_48)) {
			sb.append("(");

			if (l.equals(STSLevel.STS_1)) {
				sb.append(((DataPathNode) getParent()).getSTSIndex());
				sb.append(",");
			}

			sb.append(getSTSIndex());
			sb.append(")");
		}

		return sb.toString();
	}


	protected PComponent getTributary() throws PApplianceException, IOException {
		
		
		if (mTributary == null) {
			if (getSTSLevel().equals(STSLevel.STS_48)) {
				mTributary = mDataPath.getSubComponent("oc48c");
			} else {
				mTributary =
						((DataPathNode) getParent()).getTributary().getSubComponent(
						getComponentName());
				if (mTributary == null) {
					throw new NullPointerException(getComponentName());
				}
			}
		}
				
		return mTributary;
	}

	public JComponent getNodeComponent(boolean instantiate) throws Exception {
		if (mForm==null) {
			if (instantiate) {
				if (getSTSLevel().equals(STSLevel.STS_48)) {
					mForm = new OC48Panel(this.toString(),
							getMode(), (Oc48c_rx) getTributary(), mCard);
					((OC48Panel)mForm).setSetupLock( mSetupLock );
					
					/* init state */
					((OC48Panel)mForm).clearFlags();


				} else {
					mForm = new TributaryPanel(this.toString(), getSTSLevel(),
							getMode(), (RxStsNc) getTributary(), mCard);
					((TributaryPanel)mForm).setSetupLock( mSetupLock );
					/*if (mClearFlag) {
						((TributaryPanel)mForm).clearFlags();
						mClearFlag = false;
					}*/
					((TributaryPanel)mForm).clearFlags();
								
				}
			}
		}
		if (mCard.isTestRunning()) Refresher.getRefresher().addRefreshListener((RefreshListener)mForm);
			
		return mForm;
	}
			
	public JComponent getNodeComponent()
			throws Exception {
			
		return getNodeComponent(true);
	}


	protected void activate() {
		
		
		if (!mIsActivated) {
			try {
				getTributary().addStateChangeListener(this);
				mIsActivated = true;

				PEnvelope env = new PEnvelope(cRateEnabledTypeSet);
				
				env.mergePComponent(getTributary());
				
//System.out.println("DataPathNode.activate()-" + getSTSLevel().toString());
				Boolean b = (Boolean) env.getElement("rateEnabled");
				setConcatenated(b.booleanValue());
//System.out.println("DataPathNode.activate()- rateEnabled = " + b.toString());
				b = (Boolean)env.getElement("rateEnabledUser");
				setUserRateEnabled(b.booleanValue());
//System.out.println("DataPathNode.activate()- rateEnabledUser = " + b.toString());
				touch();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}


	protected void activateChildren() {
		for (Enumeration en = mChildren.elements(); en.hasMoreElements(); ) {
			DataPathNode node = (DataPathNode)en.nextElement();
			
			node.activate();
		}
	}


	public void dispose() {
		for (Enumeration en = mChildren.elements(); en.hasMoreElements(); ) {
			((DataPathNode) en.nextElement()).dispose();
		}

		if (mForm != null) {
			mForm.dispose();
		}

		if (mTributary != null && mIsActivated) {
			try {
				mTributary.removeStateChangeListener(this);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			mIsActivated = false;
		}
	}


	public void stateChanged(PStateChangeEvent event) {
		//refreshRequired - use this flag so node does not "blink" 
		//                  unnecessarily.
		boolean refreshRequired = false;
		
		PEnvelope env = new PEnvelope(cRateEnabledTypeSet);
		env.mergePAttributeList(event.getAttributes());
		
		if (env.hasElement("rateEnabledUser")) {
			
			setUserRateEnabled(((Boolean)env.getElement("rateEnabledUser")).booleanValue());
			refreshRequired = true;			
		}
					
		if (env.hasElement("rateEnabled")) {
			setConcatenated(((Boolean) env.getElement("rateEnabled")).booleanValue());
			refreshRequired = true;
		}
		
		if (mErrorEventListeners != null) {
			ErrorEvent errorEvent;
			for (Enumeration e = mErrorEventListeners.keys();e.hasMoreElements();) {
				String key = (String)e.nextElement();
				if (env.hasElement(key)) {
					errorEvent = new ErrorEvent(this, env.getElement(key));
					
					ErrorEventListener listener = (ErrorEventListener)mErrorEventListeners.get(key);
					listener.errorEventNotify(errorEvent);
				}
			}
		} //end if
		
		if (refreshRequired) touch();
		
	}



	static private class RateEnabledTypeSet extends TypeSet {
		public RateEnabledTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("rateEnabled", Boolean.class),
				new TypeSetElement("b3Errors", Long.class),
				new TypeSetElement("patternErrs", Long.class),
				new TypeSetElement("rateEnabledUser", Boolean.class),
			});
		}
	}


	public TreeNode[] getPath() {
		return getPathToRoot(this, 0);
	}


	protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
		TreeNode[] retNodes;

		if (aNode == null) {
			if (depth == 0) {
				return null;
			} else {
				retNodes = new TreeNode[depth];
			}
		} else {
			depth++;
			retNodes = getPathToRoot(aNode.getParent(), depth);
			retNodes[retNodes.length - depth] = aNode;
		}
		return retNodes;
	}
	public void setClearFlag( boolean flag ) {
		mClearFlag = flag;
	}

	public void addErrorEventListener(String key, ErrorEventListener listener) {
		mErrorEventListeners.put(key, listener);
		
	}
	public void removeErrorEventListener(ErrorEventListener listener) {
		mErrorEventListeners.remove(listener);
	}
	
}
