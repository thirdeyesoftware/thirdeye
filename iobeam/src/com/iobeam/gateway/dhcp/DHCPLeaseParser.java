package com.iobeam.gateway.dhcp;


import java.util.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.*;
import com.iobeam.util.MACAddress;



public class DHCPLeaseParser {


	private static final String TOK_LEASE = "lease";
	private static final String TOK_START = "starts";
	private static final String TOK_END = "ends";
	private static final String TOK_BINDING = "binding state";
	private static final String TOK_MAC = "hardware ethernet";
	private static final String TOK_HOSTNAME = "client-hostname";
	private static final String TOK_ABANDONED = "abandoned";


	private static Collection cRequiredProperties = new ArrayList();
	static {
		cRequiredProperties.add(TOK_START);
		cRequiredProperties.add(TOK_END);
		//cRequiredProperties.add(TOK_BINDING);
		cRequiredProperties.add(TOK_MAC);
		// cRequiredProperties.add(TOK_HOSTNAME);
	}

	private static Collection cUnboundRequiredProperties = new ArrayList();
	static {
		cUnboundRequiredProperties.add(TOK_START);
		cUnboundRequiredProperties.add(TOK_END);
		// cUnboundRequiredProperties.add(TOK_BINDING);
		// cRequiredProperties.add(TOK_MAC);
		// cRequiredProperties.add(TOK_HOSTNAME);
	}


	/**
	* @#exception DHCPLeaseParseException The specified lease body lines
	* have an unexpected format.
	*/
	public static DHCPLease createLease(List bodyLines,
			int startingLineNumber) throws DHCPLeaseParseException {

		int lineNumber = startingLineNumber;
		InetAddress inetAddress = null;
		MACAddress macAddress = null;
		Date start = null, end = null;
		boolean isBound = true;
		boolean abandoned = false;
		String clientHostname = null;
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		if (bodyLines.size() < 2) {
			throw new DHCPLeaseParseException("lease at line " +
					startingLineNumber + " has empty body");
		}

		StringTokenizer st;
		String tok;
		List processedProperties = new ArrayList();

		try {
			st = new StringTokenizer((String) bodyLines.get(0));
			tok = st.nextToken();

			if (!tok.equals(TOK_LEASE)) {
				throw new DHCPLeaseParseException("no lease token at " +
						lineNumber);
			}

			tok = st.nextToken();
			try {
				inetAddress = InetAddress.getByName(tok);
			}
			catch (UnknownHostException uhe) {
				throw new DHCPLeaseParseException("bad ip address at " +
						lineNumber);
			}

			++lineNumber;

			for (int i = 1; i < bodyLines.size(); ++i, ++lineNumber) {
				String line = (String) bodyLines.get(i);

				if (line.startsWith(TOK_START)) {
					st = new StringTokenizer(
							line.substring(TOK_START.length())," \t;");

					// skip day of week
					st.nextToken();

					start = df.parse(st.nextToken(";"));

					processedProperties.add(TOK_START);
				} else
				if (line.startsWith(TOK_END)) {
					st = new StringTokenizer(
							line.substring(TOK_END.length())," \t;");

					// skip day of week
					st.nextToken();

					end = df.parse(st.nextToken(";"));

					processedProperties.add(TOK_END);
				} else
				if (line.startsWith(TOK_BINDING)) {
					st = new StringTokenizer(
							line.substring(TOK_BINDING.length())," \t;");
					tok = st.nextToken();
					if (tok.equals("free")) {
						isBound = false;
					} else
					if (tok.equals("active")) {
						isBound = true;
					} else {
						throw new DHCPLeaseParseException("unexpected " +
								"binding state '" + tok + "' at line " +
								lineNumber);
					}

					processedProperties.add(TOK_BINDING);
				} else
				if (line.startsWith(TOK_MAC)) {
					st = new StringTokenizer(
							line.substring(TOK_MAC.length())," \t;");

					tok = st.nextToken();
					macAddress = new MACAddress(tok);

					processedProperties.add(TOK_MAC);
				} else
				if (line.startsWith(TOK_ABANDONED)) {
					processedProperties.add(TOK_ABANDONED);
					abandoned = true;
					isBound = false;
				} else
				if (line.startsWith(TOK_HOSTNAME)) {
					st = new StringTokenizer(
							line.substring(TOK_HOSTNAME.length())," \t;");

					clientHostname = st.nextToken("\" \t;");

					processedProperties.add(TOK_HOSTNAME);
				}
			}
		}
		catch (ParseException pe) {
			throw new DHCPLeaseParseException("invalid date format at " +
					lineNumber + "(" + pe.getMessage() + ")");
		}
		catch (NoSuchElementException nsee) {
			throw new DHCPLeaseParseException("early line termination at " +
					lineNumber);
		}


		List missingProperties = getMissingPropertyNames(isBound,
				processedProperties);
		if (!missingProperties.isEmpty() && !abandoned) {
			throw new DHCPLeaseParseException("lease block at line " +
					startingLineNumber + " missing required properties: " +
					missingProperties);
		}

		return new DHCPLease(inetAddress, macAddress, start, end,
				isBound, clientHostname, abandoned);
	}


	/**
	* Returns a List of String names of required properties
	* missing from the specified List.
	*
	* Returns an empty Collection if the specified Map is complete.
	*/
	public static List getMissingPropertyNames(boolean isBound,
			List properties) {

		List missing = getRequiredProperties(isBound);
		missing.removeAll(properties);

		return missing;
	}


	public static List getRequiredProperties(boolean isBound) {
		if (isBound) {
			return new ArrayList(cRequiredProperties);
		} else {
			return new ArrayList(cUnboundRequiredProperties);
		}
	}
}
