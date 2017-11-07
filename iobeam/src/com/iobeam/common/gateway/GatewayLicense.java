package com.iobeam.common.gateway;


import com.iobeam.util.MACAddress;
import com.iobeam.util.Signer;
import com.iobeam.portal.security.PortalKeyStore;


/**
* Describes a license for a Gateway based on the MACAddress of the
* Gateway's ISP-side NIC.
*
* @deprecated use only MAC for gateway validation.
*/
public class GatewayLicense implements java.io.Serializable {

	private MACAddress mMACAddress;
	private String mSignature;

	public GatewayLicense(MACAddress macAddress, String signature) {
		mMACAddress = macAddress;
		if (macAddress == null) {
			throw new NullPointerException("macAddress");
		}

		mSignature = signature;
		if (signature == null) {
			throw new NullPointerException("signature");
		}
	}


	public MACAddress getMACAddress() {
		return mMACAddress;
	}


	public String getSignature() {
		return mSignature;
	}


	/**
	* Returns true if this is a valid license, according to the
	* Portal's certificate.
	*/
	public boolean isValid() throws Exception {
		return Signer.isValidSignature(getMACAddress().toString(),
				getSignature(),
				PortalKeyStore.getCertificate());
	}
}
