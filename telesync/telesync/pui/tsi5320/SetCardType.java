////
//
// Telesync 5320
//
//

package telesync.pui.tsi5320;


import java.util.*;
import java.net.*;
import java.io.*;
import pippin.pui.*;


class SetCardType implements PConnectionListener {

	private SetCardType(PClient pClient, int slotNumber,
			Integer typeID) throws Exception {
		this(pClient, slotNumber, typeID, false);
	}

	private SetCardType(PClient pClient, int slotNumber,
			Integer typeID, boolean config) throws Exception {

		pClient.addConnectionListener(this);

		Bus bus = (Bus) pClient.getRoot().getSubComponent("bus");

		if (config) {
			bus.getSlotAt(slotNumber).put("cfgdType", typeID);
		} else {
			bus.getSlotAt(slotNumber).put("cardType", typeID);
		}
	}


	public static void main(String[] args) {
		if (args.length != 3 && args.length != 4) {
			System.err.print("use: " +
					"java SetCardType unitName slot# cardType [cfg]\n");
			System.exit(1);
		}


		PClient pClient;
		SetCardType session;
		Integer typeID;
		int slotNumber;
		String unitName;

		try {
			unitName = args[0];
			slotNumber = Integer.parseInt(args[1]);
			typeID = new Integer(args[2]);
			pClient = new PClient(InetAddress.getByName(unitName));

			if (args.length == 3) {
				session = new SetCardType(pClient, slotNumber, typeID);
			} else {
				session = new SetCardType(pClient, slotNumber, typeID, true);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
	}


	/**
	* @see ConnectionListener
	*/
	public void connectionLost() {
		System.out.println("SetCardType: connectionLost!");
		System.exit(-1);
	}
}
