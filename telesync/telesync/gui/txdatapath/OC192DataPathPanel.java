////
//
// Telesync 5320 Project
//
//

package telesync.gui.txdatapath;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.util.*;

import pippin.pui.*;
import pippin.binder.*;
import pippin.binder.pui.*;
import telesync.pui.tsi5320.*;
import telesync.gui.*;



public class OC192DataPathPanel extends JPanel
		implements TreeSelectionListener, TreeExpansionListener, ClearFlagEventListener {
	JSplitPane mSplitPane;
	JTree mTree;
	OC192DataPathNode mRootNode;
	DefaultTreeModel mTreeModel;
	Tx192datapath mDataPath;
	Mode mMode = Mode.US_SONET;


	public OC192DataPathPanel(Tx192datapath dp) {
		mDataPath = dp;
		setLayout(new BorderLayout());

		mSplitPane = new JSplitPane();
		add(mSplitPane, BorderLayout.CENTER);

		mTreeModel = new DefaultTreeModel(
				mRootNode = OC192DataPathNode.createRootNode(mDataPath, this,
				mMode));
		mTree = new JTree(mTreeModel);
		mTree.setCellRenderer(new OC192DataPathCellRenderer());

		mSplitPane.setLeftComponent(new JScrollPane(mTree));
		mSplitPane.setDividerLocation(155);
		mSplitPane.setDividerSize(3);

		mTree.addTreeSelectionListener(this);
		mTree.addTreeExpansionListener(this);

		mTree.setSelectionInterval(0, 0);
		mTree.collapseRow(0);
		mTree.expandRow(0);
	}


	public void setMode(Mode mode) {
		if (!mMode.equals(mode)) {
			mMode = mode;
			mTreeModel = new DefaultTreeModel(
					mRootNode = OC192DataPathNode.createRootNode(mDataPath, this, mode));
			mTree.setModel(mTreeModel);
			mTree.setSelectionInterval(0, 0);
		}
	}


	public void valueChanged(TreeSelectionEvent event) {
		TreePath tp = event.getPath();

		OC192DataPathNode n = (OC192DataPathNode) tp.getLastPathComponent();
		
		
		
		try {
			mSplitPane.setRightComponent(n.getNodeComponent());
		}
		catch (Exception e) {
			GClient.getClient().showErrorDialog(e);
		}
	}


	public void treeCollapsed(TreeExpansionEvent event) {
	}


	public void treeExpanded(TreeExpansionEvent event) {
		TreePath tp = event.getPath();
		OC192DataPathNode n = (OC192DataPathNode) tp.getLastPathComponent();
		n.activateChildren();
	}


	public void dispose() {
		mRootNode.dispose();
	}


	protected DefaultTreeModel getTreeModel() {
		return mTreeModel;
	}


	protected JTree getTree() {
		return mTree;
	}
	
	public void clearFlags() {
		if (mRootNode==null) return; // won't happen... 
		try {
			if (mRootNode.getNodeComponent() instanceof ClearFlagEventListener) {
				((ClearFlagEventListener)mRootNode.getNodeComponent()).clearFlags();
			}
			if (mRootNode.getChildCount() > 0) {
				for (Enumeration e = mRootNode.children();e.hasMoreElements();) {
					OC192DataPathNode node = (OC192DataPathNode) e.nextElement();

					if (node.getNodeComponent() instanceof ClearFlagEventListener) {
						((ClearFlagEventListener)node.getNodeComponent()).clearFlags();
					}
				}
			}
		}
		catch (Exception ez) {
			ez.printStackTrace();
		}

	}
	
}
