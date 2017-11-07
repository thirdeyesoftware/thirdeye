package com.iobeam.portal.task.billing.payment.processautomaticpayment;


import java.util.logging.*;
import com.Verisign.payment.*;
import java.security.*;



public class VerisignGateway {

	private PFProAPI mPayflow = null;


	private VerisignGateway(boolean testOnly) {
		mPayflow = createPayflowAPI(testOnly);
	}


	public static VerisignGateway getInstance() {
		return new VerisignGateway(false);
	}


	public static VerisignGateway getTestInstance() {
		return new VerisignGateway(true);
	}


	public VerisignResponse submitTransaction(VerisignParms parms)
			throws VerisignException {

		if (mPayflow == null) {
			throw new IllegalStateException("already submitted");
		}

		Provider[] providers = Security.getProviders();
		boolean needSunJSSE = true;
		for (int i = 0; i < providers.length; ++i) {
			System.out.println("Provider " + i + " " +
					providers[i].getName() + " " +
					providers[i].getClass().getName());

			if (providers[i].getName().equals("SunJSSE")) {
				needSunJSSE = false;
			}
		}
		if (needSunJSSE) {
			// Verisign PayFlow requires this Provider.
			Security.insertProviderAt
					(new com.sun.net.ssl.internal.ssl.Provider(), 2);

			// Security.insertProviderAt
			// 		(new com.sun.crypto.provider.SunJCE(), 4);

			// Security.insertProviderAt
			// 		(new sun.security.jgss.SunProvider(), 5);
		}

		providers = Security.getProviders();
		for (int i = 0; i < providers.length; ++i) {
			System.out.println("Provider " + i + " " +
					providers[i].getName() + " " +
					providers[i].getClass().getName());
		}

		Logger.getLogger("com.iobeam.portal.task.billing.payment").info(
				parms.getParmList());

		String rc = mPayflow.SubmitTransaction(parms.getParmList());

		VerisignResponse r = new VerisignResponse(rc);

		mPayflow.DestroyContext();
		mPayflow = null;

		Logger.getLogger("com.iobeam.portal.task.billing.payment").info(
				r.toString());

		if (r.getResultCode() != VerisignResponse.RC_APPROVED) {
			throw new VerisignException(r);
		}

		return r;
	}


	public String getVersion() {
		return mPayflow.Version();
	}



	private PFProAPI createPayflowAPI(boolean testOnly) {
		PFProAPI vapi = new PFProAPI();
		Logger.getLogger("com.iobeam.portal.task.billing.payment").info(
			VerisignConstants.getCertsPath());

		vapi.SetCertPath(VerisignConstants.getCertsPath());

		int rc = vapi.CreateContext(
				testOnly ?  VerisignConstants.getTestHost() :
						VerisignConstants.getHost(),
				VerisignConstants.getPort(),
				VerisignConstants.getTimeout(),
				"", 0, "", "");

		Logger.getLogger("com.iobeam.portal.task.billing.payment").info(
				"CreateContext returns " + rc);

		return vapi;
	}


	public static void main(String[] args) throws Exception {
		VerisignGateway gw = getTestInstance();

		String rc = gw.mPayflow.SubmitTransaction(args[0]);

		System.out.println(rc);
	}
}
