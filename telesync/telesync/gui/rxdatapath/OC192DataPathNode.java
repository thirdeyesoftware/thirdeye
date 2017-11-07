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




public class OC192DataPathNode implements TreeNode, PStateChangeListener {
	private STSLevel mSTSLevel;
	private Vector mChildren = null;
	private boolean mIsConcatenated = false;
	private TreeNode mParent;
	private String mComponentName;
	private int mSTSIndex;
	private ButtonForm mForm = null;
	private Rx192datapath mDataPath;
	private PComponent mTributary = null;
	private boolean mIsActivated = false;
	static private TypeSet cRateEnabledTypeSet = new RateEnabledTypeSet();
	private OC192DataPathPanel mDataPathPanel;
	private Mode mMode = Mode.US_SONET;
	private boolean mSetupLock = true;
	private boolean mClearFlag = false;
	
	private Hashtable mErrorEventListeners = new Hashtable();
	
	public static OC192DataPathNode createRootNode(Rx192datapath dp,
			OC192DataPathPanel dataPathPanel, Mode mode) {
		return new OC192DataPathNode(dp, dataPathPanel, STSLevel.STS_192, mode);
	}
	public static OC192DataPathNode createRootNode(Rx192datapath dp,
			OC192DataPathPanel dataPathPanel, Mode mode, boolean setupLock) {
		return new OC192DataPathNode(dp, dataPathPanel, STSLevel.STS_192, mode, null, 0);
	}
	
	private OC192DataPathNode(Rx192datapath dp, OC192DataPathPanel dataPathPanel,
			STSLevel stsLevel, Mode mode) {
		this(dp, dataPathPanel, stsLevel, mode, null, 0);
	}
	
	private OC192DataPathNode(Rx192datapath dp, OC192DataPathPanel dataPathPanel,
			STSLevel stsLevel, Mode mode, boolean setupLock) {
		this(dp, dataPathPanel, stsLevel, mode, null, 0);
		mSetupLock = setupLock;
	}
	
	private OC192DataPathNode(Rx192datapath dp, OC192DataPathPanel dataPathPanel,
			STSLevel stsLevel, Mode mode, TreeNode parent, int index, boolean setupLock) {
			
			this(dp, dataPathPanel, stsLevel, mode, parent, index);
			mSetupLock = setupLock;
	}
	

	private OC192DataPathNode(Rx192datapath dp, OC192DataPathPanel dataPathPanel,
			STSLevel stsLevel, Mode mode, TreeNode parent, int index) {

		mSTSLevel = stsLevel;
		mChildren = new Vector();
		mParent = parent;
		mDataPath = dp;
		mMode = mode;
		mDataPathPanel = dataPathPanel;
		

		if (stsLevel.equals(STSLevel.STS_192)) {
			setComponentName("oc192c");
			activate();
		} else 
		if (stsLevel.equals(STSLevel.STS_48)) {
			mSTSIndex = index  * 16 + 1;
			setComponentName("sts48c-" + mSTSIndex);
			
			
			activate();
		} else
		if (stsLevel.equals(STSLevel.STS_12)) {
			mSTSIndex = ((OC192DataPathNode) getParent()).getSTSIndex() + (index * 4) ;
			
			
			setComponentName("sts12c-" + mSTSIndex);
				
		} else
		if (stsLevel.equals(STSLevel.STS_3)) {
			
			mSTSIndex = ((OC192DataPathNode) getParent()).getSTSIndex() + index;
			
			setComponentName("sts3c-" + mSTSIndex);
			
			
		} else {
			mSTSIndex = index + 1;
			setComponentName("sts1-" +
					((OC192DataPathNode) getParent()).getSTSIndex() +
					"," + mSTSIndex);
			
		}

		
		
		for (int i = 0; i < stsLevel.getChildCount(); ++i) {
			mChildren.addElement(
					new OC192DataPathNode(dp, dataPathPanel,
					stsLevel.getChildLevel(), mode, this, i));
			
		}


		if (stsLevel.equals(STSLevel.STS_192)) {
			activate();
			activateChildren();
		}
	}



	public void setConcatenated(boolean isConcatenated) {
		mIsConcatenated = isConcatenated;

		DefaultTreeModel treeModel = mDataPathPanel.getTreeModel();

		// protect nodes created during init of the DataPathPanel
		if (treeModel != null) {
			treeModel.nodeChanged(this);
		}

		if (mForm != null) {
			mForm.setTitle(this.toString());
		}
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
			if (getSTSLevel().equals(STSLevel.STS_192)) {
				((OC192Panel)mForm).setSetupLock( setupLock );
			} else  {
				((OC192TributaryPanel)mForm).setSetupLock( setupLock );
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
			return (mSTSLevel.equals(STSLevel.STS_3) || mSTSLevel.equals(STSLevel.STS_12) ||
							mSTSLevel.equals(STSLevel.STS_48));
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
		if (!l.equals(STSLevel.STS_192)) {
			sb.append("(");

			if (l.equals(STSLevel.STS_1)) {
				sb.append(((OC192DataPathNode) getParent()).getSTSIndex());
				sb.append(",");
			}

			sb.append(getSTSIndex());
			sb.append(")");
		}

		return sb.toString();
	}


	protected PComponent getTributary() throws PApplianceException, IOException {
		if (mTributary == null) {
			if (getSTSLevel().equals(STSLevel.STS_192)) {
				mTributary = mDataPath.getSubComponent("oc192c");
			} else {
				mTributary =
						((OC192DataPathNode) getParent()).getTributary().getSubComponent(
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
				if (getSTSLevel().equals(STSLevel.STS_192)) {
					mForm = new OC192Panel(this.toString(),
							getMode(), (Oc192c_rx) getTributary());
					((OC192Panel)mForm).setSetupLock( mSetupLock );
					/*if (mClearFlag) {
						((OC192Panel)mForm).clearFlags();
						mClearFlag = false;
					}
					*/
					((OC192Panel)mForm).clearFlags();


				} else {
					mForm = new OC192TributaryPanel(this.toString(), getSTSLevel(),
							getMode(), (RxStsNc192) getTributary());
					((OC192TributaryPanel)mForm).setSetupLock( mSetupLock );
					/*if (mClearFlag) {
						((TributaryPanel)mForm).clearFlags();
						mClearFlag = false;
					}*/
					((OC192TributaryPanel)mForm).clearFlags();
								
				}
			}
		}
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

				Boolean b = (Boolean) env.getElement("rateEnabled");
				setConcatenated(b.booleanValue());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	protected void activateChildren() {
		for (Enumeration en = mChildren.elements(); en.hasMoreElements(); ) {
			((OC192DataPathNode) en.nextElement()).activate();
		}
	}


	public void dispose() {
		for (Enumeration en = mChildren.elements(); en.hasMoreElements(); ) {
			((OC192DataPathNode) en.nextElement()).dispose();
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
		PEnvelope env = new PEnvelope(cRateEnabledTypeSet);
		env.mergePAttributeList(event.getAttributes());
		
		if (env.hasElement("rateEnabled")) {
			
			
			Boolean b = (Boolean) env.getElement("rateEnabled");
			
			setConcatenated(b.booleanValue());
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
	}



	static private class RateEnabledTypeSet extends TypeSet {
		public RateEnabledTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("rateEnabled", Boolean.class),
				new TypeSetElement("b3Errors", Long.class),
				new TypeSetElement("patternErrs", Long.class),
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
