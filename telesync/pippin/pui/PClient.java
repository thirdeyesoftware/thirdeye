////
// //
// PippinSoft
//
//

package pippin.pui;
import pippin.util.DispatchInterest;
import pippin.util.*;
import java.util.*;
import java.net.*;
import java.io.*;



public class PClient implements Runnable {
	Socket mSo;
	DataInputStream mIn;
	DataOutputStream mOut;
	PDef mDef;
	private Vector mConnectionListeners = new Vector();
	private Vector mDispatchListeners = new Vector();
	private SubscriptionQueue mSubscriptionQueue = SubscriptionQueue.getInstance();
	
	private Fifo mDispatchList = new Fifo();

	private Thread mReader,mDispatcher;

	private static final int MAX_PENDING_COMMANDS = 16;
	private static final int COMMAND_WAIT_MILLIS = 5000;

	private CommandWaiter[] mOutstanding =
			new CommandWaiter[MAX_PENDING_COMMANDS];

	private int mOutHead = 0;
	private boolean mDisposed = false;

	public PClient(String host) throws IOException {
		this(InetAddress.getByName(host));
	}

	public PClient(InetAddress addr) throws IOException {
		this(addr, 6999);
	}

	public PClient(InetAddress addr, int port) throws IOException {
		
		mSo = new Socket(addr,port);
				
		mIn = new DataInputStream(mSo.getInputStream());
		mOut = new DataOutputStream(new BufferedOutputStream(
				mSo.getOutputStream()));
		
		mOut.writeBytes("PuiClientV0");
		mOut.flush();

		byte[] buf = new byte[11];
		mIn.readFully(buf);
		if ( !"PuiServerV0".equals(new String(buf)))
			throw new IOException("Invalid server reponse!" + (new String(buf)));
		
		int len = mIn.readUnsignedByte();
		buf = new byte[len];
		mIn.readFully(buf);

		String classname = new String(buf);

		try {
			mDef = (PDef)Class.forName(classname+ ".Def").newInstance();
			mDef.setClient(this);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IOException("Invalid class (" +classname+
					") specified by server!" + ex);
		}

		mSo.setSoTimeout(5000);
		
		mReader = new Thread(this);
		mReader.setDaemon(true);
		mReader.start();

		mDispatcher = new Thread(this);
		mDispatcher.setDaemon(true);
		mDispatcher.start();
	}


	/**
	* One-shot disposal.  Does nothing if we're
	* already disposed.
	*/
	public void dispose() throws IOException {
		synchronized (this) {
			if (mDisposed) {
				return;
			}
			mDisposed = true;
		}

		// Terminate the dispatch thread.
		
		mDispatchList.put(null);

		connectionLost();

		mIn.close();
		mOut.close();
		mSo.close();
	}


	public PComponent getRoot() {
		return mDef.getRoot();
	}


	/**
	* Reads the next PMessage from the InputStream on the
	* Socket connection to the pui appliance.
	*
	* @return the next PMessage from the pui appliance.
	*/
	private PMessage read() throws IOException {
		int type,source,componentID;
		byte[] buf;
		
		type = mIn.readUnsignedShort();
		
		source = mIn.readUnsignedShort();
		componentID = mIn.readUnsignedShort();

		PComponent comp = mDef.getComponent(componentID);
		
		PMessage msg = new PMessage(comp, type & 0x7FFF);
		
		msg.setSource(source);
		
		if ((type & PMessage.TYPE_RESPONSE_BIT) != 0) {
			
			msg.setResponse();
		}
		
		
		PTypeSet typeset = null;
				
		if (msg.getType() == PMessage.TYPE_ALARM) {
			typeset = new PAlarmTypeSet();
		} else typeset = comp.getTypeSet();

		int attrid;
		while ( (attrid = mIn.readUnsignedShort()) !=
				PMessage.ATTR_END_OF_MESSAGE) {
			int attrtype = mIn.readUnsignedByte();
			int attrlen = mIn.readUnsignedByte();
			int attrval;
			long longAttrval;
			Object val;
			
			
			switch (attrtype) {
			case PAttribute.TYPE_BYTE:
				attrval = mIn.readUnsignedByte();
				val = new Integer(attrval);
				break;

			case PAttribute.TYPE_SHORT:
				attrval = mIn.readUnsignedShort();
				val = new Integer(attrval);
				break;

			case PAttribute.TYPE_LONG:
				attrval = mIn.readInt();
				val = new Integer(attrval);
				break;

			case PAttribute.TYPE_LONGLONG:
				longAttrval = mIn.readLong();
				val = new Long(longAttrval);
				break;

			// case PAttribute.TYPE_INTEGER:
			// 	attrval = mIn.readInt();
			// 	val = new Integer(attrval);
			// 	break;

			case PAttribute.TYPE_STRING:
				buf = new byte[attrlen-1];
				mIn.readFully(buf);
				attrval = mIn.readUnsignedByte();
				val = new String(buf);
				break;

			case PAttribute.TYPE_MEMORY:
				buf = new byte[attrlen];
				mIn.readFully(buf);
				val = buf;
				break;

			case PAttribute.TYPE_BOOLEAN:
				boolean b;

				b = mIn.readBoolean();
				// attrval = mIn.readUnsignedShort();
				// b = attrval == 1;

				val = new Boolean(b);
				break;
			
			default:
				val = null;
			}
			
			if ( (attrid & 0x8000) == 0 ) {
				String attrname = null;
				
				if ( (type & 0x7FFF) == PMessage.TYPE_LIST_SUBS) {
					attrname = "sub-id" + attrid;
				} else {
					try {
						attrname = typeset.getName(attrid);
					}
					catch (RuntimeException ex) {
						System.out.print("ex=" +ex+ "\n"+
								"  comp=" +comp.mName+ "  " +comp+ "\n");
					}
					if (attrname == null)
						attrname = "INVALID-" +attrid;
				}

				PAttribute pa;
				
				/* THIS IS A HACK! */
				/* JSB 5/24/2002   */
//				if (attrname.equals("serialNumber")) {
//					pa = new PAttribute(attrname, attrid, PAttribute.TYPE_STRING, val.toString());
//				} else {
					pa = new PAttribute(attrname,attrid, (byte)attrtype,val);
//				}
				
				
				
				try {
					msg.putAttribute(pa);
				}
				catch (Error ee) {
					System.out.println("Bad attr " + pa + " for " + comp);
					throw ee;
				}
			} else {
				switch (attrid) {
				case PMessage.ATTR_ERROR:
					msg.putError((String)val);
					break;

				case PMessage.ATTR_EXCEPTION:
					msg.putException((String)val);
					break;

				case PMessage.ATTR_EXCEPTION_TYPE:
					if (attrtype != PAttribute.TYPE_SHORT) {
						throw new Error("Exception type is not a short");
					}
					msg.putExceptionType(((Integer) val).intValue());
					break;
				}
			}
		}

		return msg;
	}


	/**
	* Transmits the specified PMessage to the pui appliance.
	*/
	public synchronized void send(PMessage msg) throws IOException {
		int type;
		int source;
		int comp;

		if ( (type = msg.getType()) == -1) {
			throw new IOException("Invalid PMessage type: " + type);
		}

		source = msg.getSource();
		comp = msg.getComponentID();

		mOut.writeShort(type | (msg.isCommand() ? 0 : 0x8000) );
		mOut.writeShort(source);
		mOut.writeShort(comp);

		for (Enumeration e = msg.getAttributes() ; e.hasMoreElements() ; ) {
			PAttribute attr = (PAttribute)e.nextElement();

			mOut.writeShort(attr.getID());
			mOut.writeByte(attr.getType());

			switch (attr.getType()) {
			case PAttribute.TYPE_BYTE:
				mOut.writeByte(1);
				mOut.writeByte( ((Integer)attr.getValue()).intValue());
				break;

			case PAttribute.TYPE_SHORT:
				mOut.writeByte(2);
				mOut.writeShort( ((Integer)attr.getValue()).intValue());
				break;

			case PAttribute.TYPE_LONG:
				mOut.writeByte(4);
				mOut.writeInt( ((Integer)attr.getValue()).intValue());
				break;

			// case PAttribute.TYPE_INTEGER:
			// 	mOut.writeByte(4);
			// 	mOut.writeInt( ((Integer)attr.getValue()).intValue());
			// 	break;

			case PAttribute.TYPE_STRING:
				String val = (String)attr.getValue();
				mOut.writeByte(val.length()+1);
				mOut.writeBytes(val);
				mOut.writeByte(0);
				break;

			case PAttribute.TYPE_MEMORY:
				byte[] bval = (byte [])attr.getValue();
				mOut.writeByte(bval.length);
				mOut.write(bval,0,bval.length);
				break;

			case PAttribute.TYPE_BOOLEAN:
				mOut.writeByte(1);
				mOut.writeBoolean( ((Boolean)attr.getValue()).booleanValue());
				/*
				mOut.writeByte(2);
				short shortVal;
				if (((Boolean)attr.getValue()).booleanValue()) {
					shortVal = 1;
				} else {
					shortVal = 0;
				}
				mOut.writeShort(shortVal);
				*/

				break;
			}
		}
		mOut.writeShort(PMessage.ATTR_END_OF_MESSAGE);
		mOut.flush();
	}


	/**
	* Sends the specified PMessage to the pui appliance,
	* and synchronously waits for a reply PMessage.
	*/
	public PMessage sendCommand(PMessage cmdmsg)
			throws IOException, PApplianceException {
		
		Thread thr = Thread.currentThread();
		int id, ndx;
		PMessage respmsg;
				
		CommandWaiter waiter = new CommandWaiter();
		waiter.mThread = thr;

		synchronized (mOutstanding) {

			// Determine the next slot in the CommandWaiter array.
			//
			if (mOutHead == Integer.MAX_VALUE) {
				mOutHead %= MAX_PENDING_COMMANDS;
			}
			id = mOutHead++;
			ndx = id % MAX_PENDING_COMMANDS;


			// Wait for the selected slot to become available.
			//
			while (mOutstanding[ndx] != null) {
				try {
					mOutstanding.wait(COMMAND_WAIT_MILLIS);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}


			// Claim the slot and record the source id expected from
			// the response PMessage.
			//
			mOutstanding[ndx] = waiter;
			waiter.mSource = id;


			// Mark the PMessage to be sent with an id matching the
			// the one set on the CommandWaiter.
			//
			cmdmsg.setSource(id);
		}


		send(cmdmsg);

		synchronized (waiter) {
			while (waiter.mComplete == false) {
				try {
					//System.out.println("PClient.sendCommand: wait");
					waiter.wait(COMMAND_WAIT_MILLIS);
					//System.out.println("PClient.sendCommand: wait done");
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}

		if (waiter.mException != null)
			throw waiter.mException;
		

		PMessage msg = waiter.mResponse;
		String s;

		if ( (s = msg.getError()) != null)
			throw mDef.createPApplianceException(msg.getExceptionType(), s);

		if ( (s = msg.getException()) != null)
			throw mDef.createPApplianceError(msg.getExceptionType(), s);

		return waiter.mResponse;
	}

	private void dispatchMessages() {
		PMessage msg;
		
		boolean doDispatch = false;
		
		/* dispatchInterest computes the entities that are "interested" in receiving dispatch information. */
		DispatchInterest dispatchInterest = DispatchInterest.getInstance();
		
		while ((msg = (PMessage) mDispatchList.get()) != null) {
			
			if (dispatchInterest != null && dispatchInterest.hasInterest(msg.getComponent().getName(), msg)) {
				fireDispatchStarted();
				doDispatch = true;
			}
			
			do {
					
				msg.getComponent().dispatch(msg);
					
			} while ((msg = (PMessage) mDispatchList.get(true)) != null);
			
			if (doDispatch) fireDispatchComplete();
			
		}


		/*
		while (mDispatchList.getAll(messages) != null) {
			fireDispatchStarted();
			for (int i = 0; i < messages.size(); ++i) {
				msg = (PMessage) messages.elementAt(i);
				msg.getComponent().dispatch(msg);
			}
			messages.setSize(0);
			fireDispatchComplete();
		}
		*/
	}


	public void run() {
		Thread thr = Thread.currentThread();

		if (thr == mDispatcher) {
			dispatchMessages();
			return;
		}

		if (thr == mReader) {
			processInputStream();
			return;
		}
	}


	private void processInputStream() {
		PMessage msg;
		
		PComponent root = mDef.getRoot();
		//System.out.println("PClient.processInputStream:");
		try {
			System.out.println("PClient.processInputStream: starting dispatch...");
			PMessage lastMsg = null;
			
			while (!mDisposed) {
				try {
					if ((msg = this.read()) == null) {
						break;
					} else {
						//System.out.println("PClient.processInputStream msg = " + msg);
						if (msg.isCommand()) {
							PComponent comp;
							if (msg.getType() == PMessage.TYPE_POLL) {
								// Ignore poll responses from the appliance.
							} else if (msg.getType() == PMessage.TYPE_ALARM) {
								// ignore
								//System.out.println("ignore message with type = 7");
								System.out.println("PClient.processInputStream():received ALARM msg.");
								Sniffer.getSniffer().dispatch(msg.getComponent().getName(), "<ALARM-command>");
								Sniffer.getSniffer().dispatch(msg.getComponent().getName(),msg);
								//System.out.println(msg.toString());
								//ignore otherwise.
								mDispatchList.put(msg);
							} else
							if (msg.getType() == PMessage.TYPE_STATE_CHG) {
								if ((comp = msg.getComponent()) != null) {
									//System.out.println("PClient.processInputStream: " +
									//		msg);
									mDispatchList.put(msg);
									Sniffer.getSniffer().dispatch(msg.getComponent().getName(), "<State Change>");
									Sniffer.getSniffer().dispatch(msg.getComponent().getName(),msg);
									
								} else {
									System.out.println("PClient.processInputStream: " +
											"null PComponent on TYPE_STATE_CHG");
									
								}
							} else {
								System.out.println("PClient.processInputStream: " +
										"unexpected command type " + msg.getType());
							}
						} else {
							/* this is a response, not a command */
							
							if (msg.getType() == PMessage.TYPE_ALARM) {
								System.out.println("PClient.processInputStream():received ALARM msg.");
								Sniffer.getSniffer().dispatch(msg.getComponent().getName(), "c<ALARM>");
								Sniffer.getSniffer().dispatch(msg.getComponent().getName(),msg);
								//System.out.println(msg.toString());
								
							} 
							else {
							
							
								// Process responses
								// msg.isCommand() == false
								Sniffer.getSniffer().dispatch(msg.getComponent().getName(), "<Command>");
								if (msg.getType() == PMessage.TYPE_SUBCANCEL) {
									Sniffer.getSniffer().dispatch(msg.getComponent().getName(),"UNSUBSCRIBE");
									System.out.println(msg.getComponent().getName() + " UNSUBSCRIBE");
								} else if (msg.getType() == PMessage.TYPE_SUBSCRIBE) {
									Sniffer.getSniffer().dispatch(msg.getComponent().getName(),"SUBSCRIBE");
									System.out.println(msg.getComponent().getName() + " SUBSCRIBE");
								}
								Sniffer.getSniffer().dispatch(msg.getComponent().getName(),msg);
								synchronized (mOutstanding) {
									int src = msg.getSource();
									int ndx = src % MAX_PENDING_COMMANDS;

									CommandWaiter waiter = mOutstanding[ndx];

									if (waiter.mSource != src) {
										throw new IOException("PClient.run: src=" + src +
												"  mOutstanding[ndx].mSource=" +
												waiter.mSource);
									} else {
										waiter.mResponse = msg;
										waiter.mComplete = true;
										synchronized (waiter) {
											waiter.notify();
										}
									}
									mOutstanding[ndx] = null;
									mOutstanding.notifyAll();
								} //end sync.
							}
							
						}
					}
				} catch (InterruptedIOException ex) {
					// Poll the appliance if the read times out.
					send(new PMessage(root, PMessage.TYPE_POLL));
				}
			
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();

			synchronized (mOutstanding) {
				for (int i = 0; i < MAX_PENDING_COMMANDS; i++) {
					CommandWaiter waiter = mOutstanding[i];
					if (waiter != null) {
						synchronized (waiter) {
							waiter.mException = ex;
							waiter.mComplete = true;
							waiter.notify();
						}
					}
				}
				mOutstanding.notifyAll();
			}

			try {
				dispose();
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
			
		}
	}


	public PComponent getComponent(PMessage msg) {
		return mDef.getComponent(msg.getComponentID());
	}


	/**
	* Notify all PConnectionListeners that the connection was lost.
	*/
	private void connectionLost() {
		PConnectionListener listener;

		Vector v = (Vector) mConnectionListeners.clone();

		for (Enumeration en = v.elements(); en.hasMoreElements(); ) {
			((PConnectionListener) en.nextElement()).connectionLost();
		}
	}


	public void addConnectionListener(PConnectionListener listener) {
		synchronized (mConnectionListeners) {
			if (!mConnectionListeners.contains(listener)) {
				mConnectionListeners.addElement(listener);
			}
		}
	}


	public void removeConnectionListener(PConnectionListener listener) {
		mConnectionListeners.removeElement(listener);
	}


	public void addDispatchListener(DispatchListener listener) {
		synchronized (mDispatchListeners) {
			if (!mDispatchListeners.contains(listener)) {
				mDispatchListeners.addElement(listener);
			}
		}
	}


	public void removeDispatchListener(DispatchListener listener) {
		mDispatchListeners.removeElement(listener);
	}


	private void fireDispatchStarted() {
		Vector v = (Vector) mDispatchListeners.clone();
		for (Enumeration en = v.elements(); en.hasMoreElements(); ) {
			((DispatchListener) en.nextElement()).dispatchStarted();
		}
	}


	private void fireDispatchComplete() {
		Vector v = (Vector) mDispatchListeners.clone();
		for (Enumeration en = v.elements(); en.hasMoreElements(); ) {
			((DispatchListener) en.nextElement()).dispatchComplete();
		}
	}

	public void addSubscriptionRequest(PComponent comp, PStateChangeListener lsnr) {
		mSubscriptionQueue.addSubscriptionRequest(comp, lsnr);
	}
	
	static private class CommandWaiter {
		Thread mThread;
		PMessage mResponse;
		int mSource;
		boolean mComplete;
		IOException mException;
	}
}
