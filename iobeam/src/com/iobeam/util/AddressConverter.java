/* class AddressConverter
 *
 * Copyright (C) 2001, 2002  R M Pitman <rob@pitman.co.za>
 *
 * This file is part of JNetFilter.
 *
 * JNetFilter is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * JNetFilter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.iobeam.util;

import java.net.*;

/**
 * This class provides static methods for converting between various
 * representations of an IP address.
 */
public class AddressConverter
{

    /**
     * Create an InetAddress object from a 32-bit integer in network byte
     * order.
     */
    public static InetAddress int2Address(int address_)
    {
	try {
	    return InetAddress.getByName(int2Dotted(address_));
	}
	catch (UnknownHostException uhe) {
	    return null;    // should never happen!
	}
    }

    /** Convert a 32-bit int (representing an IP address in network
     * byte order) to a dotted-decimal string.
     */
    public static String int2Dotted(int address_) {
	// Changed on 12 Feb 2001
	StringBuffer dottedAddress = new StringBuffer();
	dottedAddress.append((address_ >> 0) & 0xff);
	dottedAddress.append(".");
	dottedAddress.append((address_ >> 8) & 0xff);
	dottedAddress.append(".");
	dottedAddress.append((address_ >> 16) & 0xff);
	dottedAddress.append(".");
	dottedAddress.append((address_ >> 24) & 0xff);
	return dottedAddress.toString();
    }

    /** Returns a String representation of an address and a mask.
     * @param address_ The IP address in network byte order.
     * @param mask_ the mask, also in network byte order.
     */
    public static String addressAndMask(int address_, int mask_) {

	/* Compute how many bits are set in the mask.
	 */
	int i;
	for (i=0; i<32; i++) {
	    if (mask_ == 0)
		break;
	    mask_ <<= 1;
	}
	if (i == 32)
	    return int2Dotted(address_);

	return int2Dotted(address_) + "/" + Integer.toString(i);
    }

    /** Converts a 32-bit integer from network byte order to host byte order.
     */
    public static int network2Host(int value_) {
	int val = (value_ & 0xff000000) >>> 24;
	val |= (value_ & 0xff0000) >>> 8;
	val |= (value_ & 0xff00) << 8;
	val |= (value_ & 0xff) << 24;
	return val;
    }

    /**
     * Create an InetAddress object from an array of 4 bytes in network byte
     * order.
     */
    public static InetAddress bytes2Address(byte[] bytes_)
    {
	StringBuffer dottedAddress = new StringBuffer();
	dottedAddress.append(((int) bytes_[0]) & 0xff);
	dottedAddress.append(".");
	dottedAddress.append(((int) bytes_[1]) & 0xff);
	dottedAddress.append(".");
	dottedAddress.append(((int) bytes_[2]) & 0xff);
	dottedAddress.append(".");
	dottedAddress.append(((int) bytes_[3]) & 0xff);
	try {
	    return InetAddress.getByName(dottedAddress.toString());
	}
	catch (UnknownHostException uhe) { 
	    return null;	// never happens
	}
    }

    /**
     * Convert an InetAddress to a 32-bit integer in network byte order
     * (i.e. big-endian format)
     */
    public static int address2Int(InetAddress address_)
    {
	byte[] temp = address_.getAddress();
	int intval = ((int) temp[3]) & 0xff;
	intval = intval << 8;
	intval |= ((int) temp[2]) & 0xff;
	intval = intval << 8;
	intval |= ((int) temp[1]) & 0xff;
	intval = intval << 8;
	intval |= ((int) temp[0]) & 0xff;
	return intval;
    }

	public static String address2String(InetAddress address_) {
		return int2Dotted(address2Int(address_));
	}
		
}
