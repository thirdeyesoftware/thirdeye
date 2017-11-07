////
//
// Telesync 5320 Project
//
//

package telesync.gui.rxdatapath;



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
	Rx192datapath mDataPath;
	Mode mMode = Mode.US_SONET;

	boolean mSetupLock = false; 
	
	public OC192DataPathPanel(Rx192datapath dp) {
		mDataPath = dp;
		setLayout(new BorderLayout());
		
		mSplitPane = new JSplitPane();
		add(mSplitPane, BorderLayout.CENTER);

		mTreeModel = new DefaultTreeModel(
				mRootNode = OC192DataPathNode.createRootNode(mDataPath, this,
				mMode));
		mTree = new JTree(mTreeModel);
		mTree.setCellRenderer(new OC192DataPathCellRenderer());
		mTree.setMinimumSize(new Dimension(155, mTree.getHeight()));
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
			int preferredWidth = mSplitPane.getWidth() - mSplitPane.getLeftComponent().getWidth() - 100; 
			// the x factor (100) is a fudge.
			
			n.getNodeComponent().setMaximumSize( new Dimension( preferredWidth, this.getHeight()));
			n.getNodeComponent().setPreferredSize( new Dimension(preferredWidth, this.getHeight()));			
			
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
		n.setSetupLock( mSetupLock );
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
	public void setSetupLock( boolean setupLock ) {
		mSetupLock = setupLock;
		setSetupLock(mRootNode);
		
	}
	private void setSetupLock(OC192DataPathNode parentNode) {
		if (parentNode==null) return; //will this ever happen?
		parentNode.setSetupLock( mSetupLock );
		
		if (parentNode.getChildCount() > 0) {
			Enumeration e = parentNode.children();
			while (e.hasMoreElements()) {
				OC192DataPathNode node = (OC192DataPathNode) e.nextElement();
				if (node.getChildCount() > 0) {
					setSetupLock(node);
				}
				else node.setSetupLock( mSetupLock );
			}
		}
	}
	
	private void clearFlags(OC192DataPathNode node) {
		if (node==null) return;
		try {
			JComponent c = node.getNodeComponent(false);
			
			if (c!=null) {
				if (c instanceof ClearFlagEventListener) {
					((ClearFlagEventListener)c).clearFlags();
				}
			} else node.setClearFlag( true );
			
			if (node.getChildCount() > 0) {
				for (Enumeration e = node.children();e.hasMoreElements();) {
					OC192DataPathNode childNode = (OC192DataPathNode) e.nextElement();
					c = childNode.getNodeComponent(false);
					
					if (childNode.getChildCount() > 0) {
						clearFlags(childNode);
					}
					if (c==null) {
						childNode.setClearFlag( true );
						continue;
					}
					if (c instanceof ClearFlagEventListener) {
						((ClearFlagEventListener)c).clearFlags();
					}
					
				}
			}
		}
		catch (Exception ez) {
			ez.printStackTrace();
		}
	}
	
	public void clearFlags() {
			
		clearFlags(mRootNode);
	
	}
	
	/* this needs to go away soon.  the patternErr and b3error attributes need to show for all datapath
	   nodes */
	   
	public void addErrorEventListener(String key, ErrorEventListener listener) {
		mRootNode.addErrorEventListener(key, listener);
		
	}
	
	
}
