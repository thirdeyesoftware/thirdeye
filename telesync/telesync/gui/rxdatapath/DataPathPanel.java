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




public class DataPathPanel extends JPanel
		implements TreeSelectionListener, TreeExpansionListener, ClearFlagEventListener {
	JSplitPane mSplitPane;
	JTree mTree;
	DataPathNode mRootNode;
	DataPathNode mLastDataPathNode;
	
	DefaultTreeModel mTreeModel;
	Rx48datapath mDataPath;
	Mode mMode = Mode.US_SONET;
	Rx48 mCard;
	boolean mSetupLock = false; 
	ClientView mClientView;
	
	public DataPathPanel(ClientView clientView, Rx48datapath dp, Rx48 card) {
		mDataPath = dp;
		mCard = card;
		mClientView = clientView;
		
		setLayout(new BorderLayout());
		
		mSplitPane = new JSplitPane();
		add(mSplitPane, BorderLayout.CENTER);

		mTreeModel = new DefaultTreeModel(
				mRootNode = DataPathNode.createRootNode(mDataPath, this, mCard,
				mMode));
		mTree = new JTree(mTreeModel);
		mTree.setCellRenderer(new DataPathCellRenderer());
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
					mRootNode = DataPathNode.createRootNode(mDataPath, this, mCard, mode));
			
			mTree.setModel(mTreeModel);
			mTree.setSelectionInterval(0, 0);
		}
	}

	public void deactivate() {
		if (mLastDataPathNode != null) {
			try {
				if (mLastDataPathNode.getNodeComponent() instanceof RefreshListener) {
					Refresher.getRefresher().removeRefreshListener(
							(RefreshListener)mLastDataPathNode.getNodeComponent());
				}
			}
			catch (Exception e) {
				GClient.getClient().showErrorDialog(e);
			}

		}
	}
	public void activate() {
		if (mLastDataPathNode != null) {
			try {
				if (mLastDataPathNode.getNodeComponent() instanceof RefreshListener) {
					Refresher.getRefresher().addRefreshListener(
							(RefreshListener)mLastDataPathNode.getNodeComponent());
				}
			}
			catch (Exception e) {
				GClient.getClient().showErrorDialog(e);
			}

		}
	}
	
		
	public void valueChanged(TreeSelectionEvent event) {
		TreePath tp = event.getPath();
		
		DataPathNode n = (DataPathNode) tp.getLastPathComponent();
		
		try {
			if (mLastDataPathNode != null) {
					if (mLastDataPathNode.getNodeComponent() instanceof RefreshListener) {
						Refresher.getRefresher().removeRefreshListener(
								(RefreshListener)mLastDataPathNode.getNodeComponent());
					}
				}
				mLastDataPathNode = n;
		
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
		DataPathNode n = (DataPathNode) tp.getLastPathComponent();
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
	private void setSetupLock(DataPathNode parentNode) {
		if (parentNode==null) return; //will this ever happen?
		parentNode.setSetupLock( mSetupLock );
		
		if (parentNode.getChildCount() > 0) {
			Enumeration e = parentNode.children();
			while (e.hasMoreElements()) {
				DataPathNode node = (DataPathNode) e.nextElement();
				if (node.getChildCount() > 0) {
					setSetupLock(node);
				}
				else node.setSetupLock( mSetupLock );
			}
		}
	}
	
	private void clearFlags(DataPathNode node) {
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
					DataPathNode childNode = (DataPathNode) e.nextElement();
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
