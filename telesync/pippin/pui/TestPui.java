////
// //
// PippinSoft
//
//


package pippin.pui;

import java.util.*;
import java.net.*;
import java.io.*;

class TestPui
	implements PConnectionListener,PStateChangeListener
{

	private static void suball(PClient pc,TestPui tp,PComponent comp)
			throws IOException,PApplianceException {
		Enumeration e = comp.getSubComponents();

		System.out.println(comp.toString() + " " +comp.getAttributes() + "\n");

		if (!comp.getName().equals("clock"))
			comp.addStateChangeListener(tp);

		while (e.hasMoreElements()) {
			PComponent child = (PComponent)e.nextElement();
//			System.out.println(child);

			suball(pc,tp,child);
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.print("use: java pippin.pui.TestPui <host>\n");
			System.exit(1);
		}

		PClient pc;
		try {
			TestPui tp;

long bgn = System.currentTimeMillis();
			pc = new PClient(InetAddress.getByName(args[0]));
			tp = new TestPui();
			pc.addConnectionListener((PConnectionListener) tp);

			PMessage msg;

			PComponent root = pc.getRoot();

			PComponent bus = root.getSubComponent("bus");

			Object o;

			o = bus.get("slotCount");
			System.out.print("slotCount: " + o + "\n\n");


			bus.addStateChangeListener(tp);

			o = bus.get("slotCount");
			System.out.print("slotCount: " + o + "\n\n");

			System.out.print("bus.getSize(): " + bus.getSize() + "\n\n");


			suball(pc,tp,root);

long end = System.currentTimeMillis();

			System.out.print("subs-done! " +(end-bgn)+ "\n");

//			msg = new PMessage(root,PMessage.TYPE_LIST_SUBS);
//			msg = pc.sendCommand(msg);
//			System.out.println(msg.toString());


			PComponent system = root.getSubComponent("system");
			PComponent refclk = root.getSubComponent("refclk");

			system.put("name","Two");
			system.put("name","One");

			PComponent clock = system.getSubComponent("clock");
			for (int i=0 ; i<10 ; i++) {
				System.out.println(clock.getAttributes().toString());
			}
			System.out.println();


			refclk.put("source",new Integer(0));

			Thread.sleep(2000);
			System.out.print("get(pll_los)=" +refclk.get("pll_los")+ "\n");

//			refclk.put("source",new Integer(9));
			refclk.put("source",new Integer(2));

			Thread.sleep(2000);
			System.out.print("get(pll_los)=" +refclk.get("pll_los")+ "\n");

			refclk.put("source",new Integer(0));

			Thread.sleep(2000);
			System.out.print("get(pll_los)=" +refclk.get("pll_los")+ "\n");

			refclk.put("source",new Integer(2));

			Thread.sleep(2000);
			System.out.print("get(pll_los)=" +refclk.get("pll_los")+ "\n");

		} catch (Exception ex) {
			System.out.print("got ex: " +ex+ "\n");
			ex.printStackTrace();
			pc = null;
			System.exit(1);
		}
	}


	/**
	* @see ConnectionListener
	*/
	public void connectionLost() {
		System.out.println("TestPui: connectionLost!?!\n");
	}


	public void stateChanged(PStateChangeEvent e) {
		System.out.print("stateChanged: " + e.getComponent() + "\n" +
				e.getAttributes()+ "\n\n");

		PComponent comp = e.getComponent();
		if (comp instanceof telesync.pui.tsi5320.Slot) {
			try {
				Enumeration ce = comp.getSubComponents();

				while (ce.hasMoreElements()) {
					PComponent child = (PComponent)ce.nextElement();
					child.addStateChangeListener(this);
				}
			} catch (Exception ex) {
				System.out.print("TestPui.stateChanged: " +ex);
			}
		}
	}
}
