package com.iobeam.gateway.dhcp;


import java.util.*;
import java.io.*;


public class DHCPLeaseInputStream extends InputStream {

	private InputStream mInputStream;
	private LineNumberReader mLineNumberReader;
	private boolean mAtEOF = false;

	private static final int ST_LEASE_START = 0;
	private static final int ST_LEASE_BODY = 1;
	private static final int ST_LEASE_END = 2;


	public DHCPLeaseInputStream(InputStream inputStream) {
		mInputStream = inputStream;

		mLineNumberReader = new LineNumberReader(
				new InputStreamReader(mInputStream));
	}


	/**
	* Exceptions are thrown for problems with the DHCPLeaseInputStream.
	* All exceptions are fatal to the InputStream and leave it in an
	* indeterminate state; it is up to the caller to ignore or recover
	* the stream state.
	*
	* Unlike ObjectInputStream, we return null to indicate EOF, and
	* throw EOFException after that.
	*
	* @#exception DHCPLeaseParseException The InputStream is readable
	* but has an unexpected format.
	*/
	public DHCPLease readLease() throws IOException, DHCPLeaseParseException {

		if (mAtEOF) {
			throw new EOFException();
		}

		DHCPLease lease = null;
		int state = ST_LEASE_START;

		String line;
		int bodyStartLine = 0;
		List leaseBodyLines = new ArrayList();

		while (state != ST_LEASE_END &&
				(line = mLineNumberReader.readLine()) != null) {
			line = line.trim();

			/*if (line.indexOf("abandoned") >= 0) {
				continue;
			}
			*/

			if (line.startsWith("#") || line.length() == 0) {
				continue;
			}

			switch (state) {

			case ST_LEASE_START:
				if (line.startsWith("lease ")) {
					state = ST_LEASE_BODY;
					leaseBodyLines.clear();
					leaseBodyLines.add(line);
					bodyStartLine = mLineNumberReader.getLineNumber();
				} else {
					throw new DHCPLeaseParseException("expected lease " +
							"block at line " +
							mLineNumberReader.getLineNumber());
				}

				break;

			case ST_LEASE_BODY:
				if (line.startsWith("}")) {
					if (line.length() > 1) {
						throw new DHCPLeaseParseException("improper lease " +
								"body termination at line " +
								mLineNumberReader.getLineNumber());
					}

					lease = DHCPLeaseParser.createLease(leaseBodyLines,
							bodyStartLine);

					state = ST_LEASE_END;
				} else {
					if (!line.endsWith(";")) {
						throw new DHCPLeaseParseException("missing " +
								"; at line " +
								mLineNumberReader.getLineNumber());
					}
					leaseBodyLines.add(line);
				}

				break;
			}
		}

		if (lease == null) {
			mAtEOF = true;
		}

		return lease;
	}


	/**
	* This method is public as an artifact of inheritance.
	*
	* This method should not normally be called in a public context.
	*/
	public int read() throws IOException {
		return mInputStream.read();
	}
}
