package telesync.gui;

import pippin.pui.*;
import pippin.util.DispatchInterest;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.event.*;
import java.lang.Thread;
import java.awt.*;

import java.beans.*;

public class ConnectForm extends JDialog {

	
	Thread connectThread;
	
	Timer timer;
	ConnectTask task;
	
	JLabel labelMessage;
	JLabel labelProgress;
	
	private String mUnitName = "";
	private PClient mPClient = null;
	private GClient parent = null;
	private boolean userCanceled = false;
	private boolean stop = false;
	private JPanel lPanel;
	private JOptionPane optionPane;
	StringBuffer sbProgress = new StringBuffer(0);
	
	public ConnectForm( GClient client, String label ) {
		super((Frame)client, "Connecting", true);
		
		task = new ConnectTask();
		mUnitName = label;
		parent = client;
		
		
		labelMessage = new JLabel("Connecting to " + label + " ");
		Object[] array = {labelMessage};
		final String buttonCaption = "Cancel";
		Object[] options = {buttonCaption};
		
		optionPane = new JOptionPane( array, JOptionPane.PLAIN_MESSAGE, JOptionPane.CANCEL_OPTION,
														null, options, options[0]);
		
		setContentPane( optionPane );														
		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
		addPropertyListener( optionPane );
		
		
		
		timer = new Timer(250, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (sbProgress.length() > 20) {
					sbProgress.setLength(0);
				}
				sbProgress.append(".");
				
				labelMessage.setText("Connecting to " + mUnitName + " " + sbProgress.toString());
								
				if (task.isDone() || stop) {
					timer.stop();
					mPClient = null;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							Object[] options = {"Close"};
							optionPane = new JOptionPane("Unable to connect to " + mUnitName, JOptionPane.WARNING_MESSAGE,
												JOptionPane.CLOSED_OPTION, null,options, options[0]);	
							setContentPane( optionPane );
							addPropertyListener( optionPane );
							pack();
						}
					});
					
				}
			}
		});

		addWindowListener( new WindowAdapter() {
			public void windowClosing( WindowEvent e ) {
				optionPane.setValue( new Integer(JOptionPane.CLOSED_OPTION) );
			}
		});
		
		Runnable ct = new Runnable() {
			public void run() {
				try {
					getConnection();
					setVisible( false );
					Object options[] = {"Close"};
					optionPane = new JOptionPane("Connected to " + mUnitName, 
																JOptionPane.INFORMATION_MESSAGE, JOptionPane.CLOSED_OPTION, null, 
																options, options[0]  );
					System.out.println("ConnectForm.run()-connection complete");
					SwingUtilities.invokeLater( new Runnable() {
						public void run() {
							setContentPane( optionPane );
							addPropertyListener( optionPane );
							pack();
						}
					});
					
				}
				catch (Exception e) {
					mPClient = null;
					stop = true;
					setVisible( false );
					
					Object options[] = {"Close"};
					optionPane = new JOptionPane("Unable to connect to " + mUnitName, JOptionPane.ERROR_MESSAGE, 
							JOptionPane.CLOSED_OPTION, null, options, options[0]);
					SwingUtilities.invokeLater( new Runnable() {
						public void run() {
							setContentPane( optionPane );
							addPropertyListener( optionPane );
							pack();
						}
					});
					
					
					
					
				}
				
			}
		};

		connectThread = new Thread( ct, "connectThread" );
		
		connect();
	}
	public void connect() {
		
		task.startTask();
		timer.start();

		connectThread.start();
	}
	
	
	public PClient getPClient() {
		return mPClient;
	}
	
	public boolean userCanceled() {
		return userCanceled;
	}
	
	public void getConnection() throws Exception {
		
		mPClient = new PClient(mUnitName);
		mPClient.addDispatchListener(
			new DispatchListener() {
				public void dispatchStarted() {
					parent.suspendCursor();
				}

				public void dispatchComplete() {
					parent.resumeCursor();
				}
		});


	}
	private void addPropertyListener( final JOptionPane optionPane ) {
		optionPane.addPropertyChangeListener( new PropertyChangeListener() {
			public void propertyChange( PropertyChangeEvent event ) {
				String prop = event.getPropertyName();
				if (isVisible() && (event.getSource()==	optionPane)) {
					Object value = optionPane.getValue();
					System.out.println(value.toString());
					optionPane.setValue( JOptionPane.UNINITIALIZED_VALUE );
					if (value.equals("Cancel")) {
						if (connectThread != null) {
							connectThread.interrupt();
							timer.stop();
							userCanceled = true;
						}
						setVisible( false );
					} else if (value.equals("Close")) {
						setVisible( false );
					}
					
										
				}
			}
		});
	}
	
}
