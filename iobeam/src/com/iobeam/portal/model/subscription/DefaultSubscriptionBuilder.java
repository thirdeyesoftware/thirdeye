package com.iobeam.portal.model.subscription;


import javax.ejb.*;
import javax.naming.*;
import java.util.Date;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.prototype.subscription.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.model.customer.*;


public class DefaultSubscriptionBuilder extends SubscriptionBuilder {

	public Subscription createSubscription(
			SubscriptionPrototype prototype, Date startDate)
			throws SubscriptionException {

		return createSubscription(prototype, startDate,
				(SubscriptionPK) null,
				(VenuePK) null);
	}


	private Subscription createSubscription(
			SubscriptionPrototype prototype, Date startDate,
			SubscriptionPK generatorSubscriptionPK, VenuePK generatorVenuePK)
			throws SubscriptionException {

		Subscription s = null;

		if (prototype.getSubscriptionType().equals(
				SubscriptionType.PUBLIC_VENUE)) {

			PublicVenueSubscriptionPrototype pvp =
					(PublicVenueSubscriptionPrototype) prototype;

			Date endDate = null;

			if (pvp.getDuration().equals(Duration.CONTINUOUS)) {
				endDate = null;
			} else {
				if (startDate != null) {
					endDate = new Date(startDate.getTime() +
							pvp.getDuration().getTime());
				}
			}

			s = new PublicVenueSubscription(pvp, startDate, endDate,

					pvp.getBillingCycle(),
					pvp.getBillingCycleCount(),
					pvp.getCostPerBillingCycle(),
					pvp.isCommissionable(),
					pvp.getDefaultCommissionRate(),
					generatorSubscriptionPK,

					pvp.requiresRegistration(),
					pvp.hasAnonymousAccess(),
					pvp.getMaxGenerationCount(),
					false);
		} else
		if (prototype.getSubscriptionType().equals(
				SubscriptionType.PRIVATE_VENUE)) {

			PrivateVenueSubscriptionPrototype pvp =
					(PrivateVenueSubscriptionPrototype) prototype;

			Date endDate = null;

			if (pvp.getDuration().equals(Duration.CONTINUOUS)) {
				endDate = null;
			} else {
				if (startDate != null) {
					endDate = new Date(startDate.getTime() +
							pvp.getDuration().getTime());
				}
			}

			s = new PrivateVenueSubscription(pvp, startDate, endDate,

					pvp.getBillingCycle(),
					pvp.getBillingCycleCount(),
					pvp.getCostPerBillingCycle(),
					pvp.isCommissionable(),
					pvp.getDefaultCommissionRate(),
					generatorSubscriptionPK,

					pvp.requiresRegistration(),
					pvp.getMaxGenerationCount(),
					false);
		} else
		if (prototype.getSubscriptionType().equals(
				SubscriptionType.PUBLIC_MEMBER)) {

			PublicMemberSubscriptionPrototype pmp =
					(PublicMemberSubscriptionPrototype) prototype;

			Date endDate = null;

			if (pmp.getDuration().equals(Duration.CONTINUOUS)) {
				endDate = null;
			} else {
				if (startDate != null) {
					endDate = new Date(startDate.getTime() +
							pmp.getDuration().getTime());
				}
			}

			s = new PublicMemberSubscription(pmp, startDate, endDate,

					pmp.getBillingCycle(),
					pmp.getBillingCycleCount(),
					pmp.getCostPerBillingCycle(),
					pmp.isCommissionable(),
					pmp.getDefaultCommissionRate(),
					generatorSubscriptionPK,

					pmp.requiresRegistration());
		} else
		if (prototype.getSubscriptionType().equals(
				SubscriptionType.VENUE_STAFF)) {

			VenueStaffSubscriptionPrototype vsp =
					(VenueStaffSubscriptionPrototype) prototype;

			Date endDate = null;

			if (vsp.getDuration().equals(Duration.CONTINUOUS)) {
				endDate = null;
			} else {
				if (startDate != null) {
					endDate = new Date(startDate.getTime() +
							vsp.getDuration().getTime());
				}
			}

			s = new VenueStaffSubscription(vsp, startDate, endDate,

					vsp.getBillingCycle(),
					vsp.getBillingCycleCount(),
					vsp.getCostPerBillingCycle(),
					vsp.isCommissionable(),
					vsp.getDefaultCommissionRate(),
					generatorSubscriptionPK,

					vsp.requiresRegistration(),
					vsp.getMaxGenerationCount());
		} else
		if (prototype.getSubscriptionType().equals(
				SubscriptionType.PRIVATE_MEMBER)) {

			PrivateMemberSubscriptionPrototype pmp =
					(PrivateMemberSubscriptionPrototype) prototype;

			Date endDate = null;

			if (pmp.getDuration().equals(Duration.CONTINUOUS)) {
				endDate = null;
			} else {
				if (startDate != null) {
					endDate = new Date(startDate.getTime() +
							pmp.getDuration().getTime());
				}
			}

			s = new PrivateMemberSubscription(pmp, startDate, endDate,

					pmp.getBillingCycle(),
					pmp.getBillingCycleCount(),
					pmp.getCostPerBillingCycle(),
					pmp.isCommissionable(),
					pmp.getDefaultCommissionRate(),
					generatorSubscriptionPK,

					pmp.requiresRegistration(),
					generatorVenuePK);
		} else
		if (prototype.getSubscriptionType().equals(
				SubscriptionType.WEB_HOSTING)) {

			WebHostingSubscriptionPrototype pmp =
					(WebHostingSubscriptionPrototype) prototype;

			Date endDate = null;

			if (pmp.getDuration().equals(Duration.CONTINUOUS)) {
				endDate = null;
			} else {
				if (startDate != null) {
					endDate = new Date(startDate.getTime() +
							pmp.getDuration().getTime());
				}
			}

			s = new WebHostingSubscription(pmp, startDate, endDate,

					pmp.getBillingCycle(),
					pmp.getBillingCycleCount(),
					pmp.getCostPerBillingCycle(),
					pmp.isCommissionable(),
					pmp.getDefaultCommissionRate(),
					generatorSubscriptionPK,
					pmp.requiresRegistration());

		} else 
		if (prototype.getSubscriptionType().equals(
				SubscriptionType.WISP_OPERATOR)) {

			WispOperatorSubscriptionPrototype pmp =
					(WispOperatorSubscriptionPrototype) prototype;

			Date endDate = null;

			if (pmp.getDuration().equals(Duration.CONTINUOUS)) {
				endDate = null;
			} else {
				if (startDate != null) {
					endDate = new Date(startDate.getTime() +
							pmp.getDuration().getTime());
				}
			}

			s = new WispOperatorSubscription(pmp, startDate, endDate,
					pmp.getBillingCycle(),
					pmp.getBillingCycleCount(),
					pmp.getCostPerBillingCycle(),
					pmp.isCommissionable(),
					pmp.getDefaultCommissionRate(),
					generatorSubscriptionPK,
					pmp.requiresRegistration());
		} else 
		if (prototype.getSubscriptionType().equals(
				SubscriptionType.CUSTOMER_SUPPORT_OPERATOR)) {

			CustomerSupportOperatorSubscriptionPrototype pmp =
					(CustomerSupportOperatorSubscriptionPrototype) prototype;

			Date endDate = null;

			if (pmp.getDuration().equals(Duration.CONTINUOUS)) {
				endDate = null;
			} else {
				if (startDate != null) {
					endDate = new Date(startDate.getTime() +
							pmp.getDuration().getTime());
				}
			}

			s = new CustomerSupportOperatorSubscription(pmp, startDate, endDate);

		} 
		else {
			throw new UnsupportedOperationException("not impl yet");
		}
			
		s.setDescription(prototype.getDescription());

		return s;
	}


	public Subscription createDefaultSubscription(
			SubscriptionType subscriptionType, Date startDate)
			throws SubscriptionException {

		if (subscriptionType.getDefaultSubscriptionPrototypePK() == null) {
			throw new SubscriptionUnavailableException(
					"no default prototype defined for " + subscriptionType,
					subscriptionType);
		}

		SubscriptionPrototype proto;

		try {
			proto = getAccessSubscriptionPrototype().findByPrimaryKey(
					subscriptionType.getDefaultSubscriptionPrototypePK());

			if (!proto.getSubscriptionType().equals(subscriptionType)) {
				throw new Error("prototype for " + subscriptionType +
						" is " + proto.getSubscriptionType());
			}
		}
		catch (FinderException fe) {
			throw new Error(fe);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return createSubscription(proto, startDate);
	}


	/**
	* Creates a new Subscription according to the specified "generator"
	* Subscription and the specified prototype.  The generator must have
	* remaining generation capacity, that is, it hasn't exhausted the
	* number of Subscriptions it can generate.  Also, the generator must
	* be able to build Subscriptions of the SubscriptionType described
	* by the specified prototype.
	*
	* The resulting, generated Subscription has the same AccountPK as
	* the generator, and has a ParentSubscriptionPK set to the generator.
	*
	* @exception SubscriptionException there is a problem
	* creating the Subscription.
	*/
	public Subscription createSubscription(
			Subscription generator, SubscriptionPrototype prototype)
			throws SubscriptionException {

		Subscription s = null;

		SubscriptionType subscriptionType = generator.getSubscriptionType();

		if (!subscriptionType.isGenerative()) {
			throw new SubscriptionException(generator.toString() +
					" is not generative");
		}

		SubscriptionType generatedType = subscriptionType.getGeneratedType();

		if (!prototype.getSubscriptionType().equals(generatedType)) {
			throw new SubscriptionException(generator.toString() +
					" generates " + generatedType + " and not " +
					prototype.getSubscriptionType());
		}

		SubscriptionGenerator g = (SubscriptionGenerator) generator;
		if (g.getCurrentGenerationCount() >= g.getMaxGenerationCount()) {
			throw new ExhaustedGeneratorSubscriptionException(generator);
		}
		g.setCurrentGenerationCount(
				g.getCurrentGenerationCount() + 1);


		Account generatorAccount = null;

		try {
			generatorAccount = getAccount(generator.getAccountPK());
			CustomerPK customerPK = (CustomerPK)
					generatorAccount.getCustomer().getPrimaryKey();

			VenuePK generatorVenuePK = null;
			try {
				Venue v = getAccessVenue().findByCustomerPK(customerPK);
				generatorVenuePK = v.getPK();
			}
			catch (FinderException fe) {
			}

			s = createSubscription(prototype, (Date) null,
					generator.getPK(), generatorVenuePK);
			s.setAccountPK(generator.getAccountPK());

			getAccessSubscription().update(generator);
		}
		catch (SubscriptionException se) {
			throw se;
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}


		return s;
	}


	/**
	* Creates a new Subscription according to the default generating
	* behavior for the specified "generator" Subscription.  The generator
	* must have remaining generation capacity, that is, it hasn't
	* exhausted the number of Subscriptions it can generate.
	*
	* The resulting, generated Subscription has the same AccountPK as
	* the generator, and has a ParentSubscriptionPK set to the generator.
	*
	* @exception SubscriptionException there is a problem creating
	* the Subscription.
	*/
	public Subscription createDefaultSubscription(
			Subscription generator)
			throws SubscriptionException {

		SubscriptionType subscriptionType = generator.getSubscriptionType();

		if (!subscriptionType.isGenerative()) {
			throw new SubscriptionException(generator.toString() +
					" is not generative");
		}

		SubscriptionType generatedType = subscriptionType.getGeneratedType();

		if (generatedType.getDefaultSubscriptionPrototypePK() == null) {
			throw new SubscriptionUnavailableException(
					"no default prototype defined for " + generatedType,
					generatedType);
		}

		SubscriptionPrototype proto;

		try {
			proto = getAccessSubscriptionPrototype().findByPrimaryKey(
					generatedType.getDefaultSubscriptionPrototypePK());

			if (!proto.getSubscriptionType().equals(generatedType)) {
				throw new Error("prototype for " + generatedType +
						" is " + proto.getSubscriptionType());
			}
		}
		catch (FinderException fe) {
			throw new Error(fe);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return createSubscription(generator, proto);
	}



	private AccessSubscriptionPrototype getAccessSubscriptionPrototype() {
		try {
			InitialContext ic = new InitialContext();

			AccessSubscriptionPrototypeHome h =
					(AccessSubscriptionPrototypeHome)
					ic.lookup(AccessSubscriptionPrototypeHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccessSubscription getAccessSubscription() {
		try {
			InitialContext ic = new InitialContext();

			AccessSubscriptionHome h =
					(AccessSubscriptionHome)
					ic.lookup(AccessSubscriptionHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}

	private Account getAccount(AccountPK pk)
			throws FinderException {
		try {
			InitialContext ic = new InitialContext();

			AccountHome h =
					(AccountHome)
					ic.lookup(AccountHome.JNDI_NAME);

			return h.findByPrimaryKey(pk);
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccessVenue getAccessVenue() {
		try {
			InitialContext ic = new InitialContext();

			AccessVenueHome h =
					(AccessVenueHome)
					ic.lookup(AccessVenueHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
