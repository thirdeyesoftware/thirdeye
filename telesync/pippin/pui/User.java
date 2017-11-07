package pippin.pui;


import java.util.*;
import java.io.*;


/**
* Set the name attribute to change the login user.  The appliance
* will then set authenticated to false.
*
* Read the challenge attribute and write an appropriate response.
* The write of the response will throw a PApplianceException if
* the response is invalid.
*
* A challenge value may only be reponded to once before it is invalid.
* A PApplianceError is thrown when an attempt is made to re-use a challenge.
*
* The appliance sets authenticated to true when a valid response
* has been written.
*
* Sending a response on an already authenticated user will throw
* a challenge re-use PApplicanceException, but will not un-authenticate
* the user.
*/
public class User extends PComponent {

	public User(PClient client, PComponent parent, String name, int id) {

		super(client, parent, name, id,
				new PTypeSet(new String[] {
					"name",				// 32 char String
					"authenticated",
					"challenge",		// 16 byte Radius value
					"response",			// 16 byte Radius value
				}, new byte[] {
					PAttribute.TYPE_STRING,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_MEMORY,
					PAttribute.TYPE_MEMORY,
				}));
	}

	protected PComponent makeSubComponent(String name,int id) {
		return null;
	}

	public synchronized void addStateChangeListener(PStateChangeListener lsnr)
			throws IOException,PApplianceException {

		throw new Error("The 'user' component must not be subscribed");
	}
}
