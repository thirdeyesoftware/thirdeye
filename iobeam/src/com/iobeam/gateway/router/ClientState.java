package com.iobeam.gateway.router;


/**
 * ClientState encapsulates the client (MAC) state within 
 * IP Tables.  The ClientState.getID() matches the kernel
 * MARK we place on packets to and from clients in this
 * state.
 */
public class ClientState implements java.io.Serializable {


	public static final int MARK_RESTRICTED = 4;
	public static final int MARK_PORTAL = 3;
	public static final int MARK_UNRESTRICTED = 2;

	public static final ClientState ALIEN_RESTRICTED =
			new ClientState(MARK_RESTRICTED,"Alien, Restricted");

	public static final ClientState MEMBER_RESTRICTED =
			new ClientState(MARK_PORTAL,"Member, Restricted");

	public static final ClientState MEMBER_UNRESTRICTED =
			new ClientState(MARK_UNRESTRICTED,"Member, Unrestricted");

	private String mName;
	private int mID;


	private ClientState(int id, String name) {
		mName = name;
		mID = id;
	}

	public int getID() {
		return mID;
	}

	public boolean equals(Object o) {

		if (o instanceof ClientState) {
			ClientState cs = (ClientState) o;

			if (cs.getID() == getID()) {
				return true;
			}
		}

		return false;
	}


	public int hashCode() {
		return getName().hashCode();
	}

	public String getName() {
		return mName;
	}

	public String toString() {
		return "ClientState(" + getName() + ", " + mID + ")";
	}
}
