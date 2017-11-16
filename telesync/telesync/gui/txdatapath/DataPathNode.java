////
//
// Telesync 5320 Project
//
//

package telesync.gui.txdatapath;



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



public class DataPathNode implements TreeNode, PStateChangeListener {
	private STSLevel mSTSLevel;
	private Vector mChildren = null;
	private boolean mIsConcatenated = false;
	private TreeNode mParent;
	private String mComponentName;
	private int mSTSIndex;
	private ButtonForm mForm = null;
	private Tx48datapath mDataPath;
	private PComponent mTributary = null;
	private boolean mIsActivated = false;
	static private TypeSet cRateEnabledTypeSet = new RateEnabledTypeSet();
	private DataPathPanel mDataPathPanel;
	private Mode mMode = Mode.US_SONET;
	private boolean mClearFlag = false;
	


	public static DataPathNode createRootNode(Tx48datapath dp,
			DataPathPanel dataPathPanel, Mode mode) {
		return new DataPathNode(dp, dataPathPanel, STSLevel.STS_48, mode);
	}


	private DataPathNode(Tx48datapath dp, DataPathPanel dataPathPanel,
			STSLevel stsLevel, Mode mode) {
		this(dp, dataPathPanel, stsLevel, mode, null, 0);
	}


	private DataPathNode(Tx48datapath dp, DataPathPanel dataPathPanel,
			STSLevel stsLevel, Mode mode, TreeNode parent, int index) {

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
					new DataPathNode(dp, dataPathPanel, 
					stsLevel.getChildLevel(), mode, this, i));
		}


		if (stsLevel.equals(STSLevel.STS_48)) {
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
		}*/
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

	public JComponent getNodeComponent( boolean instantiate ) throws Exception {
		if (mForm==null) {
			if (instantiate) {
				if (getSTSLevel().equals(STSLevel.STS_48)) {
					mForm = new OC48Panel(this.toString(),
							getMode(), (Oc48c) getTributary());
					/*if (mClearFlag) {
						mClearFlag = false;
						((OC48Panel)mForm).clearFlags();
					}*/
					((OC48Panel)mForm).clearFlags();	
				
				} else {
					mForm = new TributaryPanel(this.toString(), getSTSLevel(),
							getMode(), (TxStsNc) getTributary());
					/*if (mClearFlag) {
						mClearFlag = false;
						((TributaryPanel)mForm).clearFlags();
					}*/
					((TributaryPanel)mForm).clearFlags();	
					
				}
			}
		}
		return mForm;
	}
	
	public JComponent getNodeComponent()
			throws Exception {
		

		return getNodeComponent( true );
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
			((DataPathNode) en.nextElement()).activate();
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
		PEnvelope env = new PEnvelope(cRateEnabledTypeSet);
		env.mergePAttributeList(event.getAttributes());

		if (env.hasElement("rateEnabled")) {
			Boolean b = (Boolean) env.getElement("rateEnabled");
			setConcatenated(b.booleanValue());
		}
	}



	static private class RateEnabledTypeSet extends TypeSet {
		public RateEnabledTypeSet() {
			super(new TypeSetElement[] {
				new TypeSetElement("rateEnabled", Boolean.class),
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
	
}