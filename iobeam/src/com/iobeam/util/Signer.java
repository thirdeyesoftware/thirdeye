package com.iobeam.util;


import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.io.*;


public class Signer {


	static private sun.misc.BASE64Encoder cEncoder =
			new sun.misc.BASE64Encoder();

	static private sun.misc.BASE64Decoder cDecoder =
			new sun.misc.BASE64Decoder();


	private Signer() {
		throw new UnsupportedOperationException("don't instantiate this");
	}


	static public String getSignature(String signedText,
			PrivateKey privateKey)
			throws Exception {

		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(privateKey);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter oosw = new OutputStreamWriter(baos, "US-ASCII");
		oosw.write(signedText);
		oosw.flush();

		signature.update(baos.toByteArray());

		String signatureText = cEncoder.encodeBuffer(signature.sign());

		return signatureText;
	}


	static public boolean isValidSignature(String signedText,
			String signatureText, Certificate certificate)
			throws Exception {
		return isValidSignature(signedText, signatureText,
				certificate.getPublicKey());
	}


	static public boolean isValidSignature(String signedText,
			String signatureText, PublicKey publicKey)
			throws Exception {

		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initVerify(publicKey);

		byte[] signatureBytes = cDecoder.decodeBuffer(signatureText);


		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter oosw = new OutputStreamWriter(baos, "US-ASCII");
		oosw.write(signedText);
		oosw.flush();

		signature.update(baos.toByteArray());

		return signature.verify(signatureBytes);
	}
}
