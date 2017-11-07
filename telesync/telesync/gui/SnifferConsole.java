package telesync.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

import telesync.gui.icons.OrbitIcon;
import pippin.util.*;
import pippin.binder.*;

public class SnifferConsole extends JInternalFrame
		implements SnifferListener, InternalFrameListener {
	
	private JTextArea mTextArea;
	
	public SnifferConsole(String title) {
		super(title, true, false);
		
		init();
	}
	
	public void snifferEventNotify(final String name, final String buffer) {
		
		
		if (SwingUtilities.isEventDispatchThread()) {
			mTextArea.append(name + "\t" + buffer + "\n");
			mTextArea.setCaretPosition(mTextArea.getText().length());
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					snifferEventNotify(name, buffer);
				}
			});
		}
		
	}

	private void init() {
				
		
		setIconifiable(false);
		addInternalFrameListener(this);
		setFrameIcon(OrbitIcon.getImageIcon());

		JPanel mattePanel = new JPanel();

		mattePanel.setBorder(new CompoundBorder(
				new EmptyBorder(5,5,5,5),
				new BevelBorder(BevelBorder.LOWERED)));
		mattePanel.setLayout(new BorderLayout());
	
		mTextArea = new JTextArea();
		mTextArea.setEnabled(false);
		JScrollPane sp = new JScrollPane(mTextArea);
		sp.setBorder(null);
		sp.setViewportBorder(
				new MatteBorder(2,2,2,2, mTextArea.getBackground()));
		mattePanel.add(sp, BorderLayout.CENTER);
		JPanel buttonPanel = ButtonMaker.createButtonPanel();
		
		buttonPanel.add(Box.createHorizontalGlue());
		JButton saveButton = ButtonMaker.createPButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Sniffer.getSniffer().setEnabled(false);
				JFileChooser chooser = new JFileChooser();
				
				chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				
				if (chooser.showSaveDialog(null) ==
						JFileChooser.APPROVE_OPTION) {
					File f = chooser.getSelectedFile();
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(f);
						fos.write(mTextArea.getText().getBytes());
						fos.flush();
						fos.close();
					}
					catch (Exception e) {
						if (fos != null) {
							try {
								fos.close();
							}
							catch (Exception ee) {}
						}
						e.printStackTrace();
					}

				}
				Sniffer.getSniffer().setEnabled(true);
			}
		});
		buttonPanel.add(saveButton);
		
		JButton actionButton = ButtonMaker.createPButton("Stop");
		actionButton.setActionCommand("stop");
		actionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("stop")) {
					Sniffer.getSniffer().setEnabled(false);
					((JButton)event.getSource()).setActionCommand("start");
					((JButton)event.getSource()).setText("Start");
				}
				else {
					Sniffer.getSniffer().setEnabled(true);
					((JButton)event.getSource()).setActionCommand("stop");
					((JButton)event.getSource()).setText("Stop");
				}
			}
		});
		buttonPanel.add(actionButton);
		
		JButton clearButton = ButtonMaker.createPButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mTextArea.setText(null);
			}
		});

		buttonPanel.add(Box.createHorizontalStrut(2));		
		buttonPanel.add(clearButton);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		
		Container contentPane = getContentPane();
		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(mattePanel);
		contentPane.add(buttonPanel);
		
		mTextArea.append("sniffer console <init>\n ");
		Sniffer.getSniffer().addSnifferListener(this);
		
	} 
	
	public void dispose() {
		super.dispose();
		Sniffer.getSniffer().removeSnifferListener(this);
	}
	
	
	public void internalFrameActivated(InternalFrameEvent e) {
		this.toFront();
	}


	public void internalFrameClosed(InternalFrameEvent e) {

		
	}


	public void internalFrameClosing(InternalFrameEvent e) {
		
		
		
	}


	public void internalFrameDeactivated(InternalFrameEvent e) {
	}


	public void internalFrameDeiconified(InternalFrameEvent e) {
		this.toFront();
	}


	public void internalFrameIconified(InternalFrameEvent e) {
	}


	public void internalFrameOpened(InternalFrameEvent e) {
		this.toFront();
	}
	
	
}

