package com.iobeam.portal.model.subscription;

import java.util.*;
import java.util.logging.*;
import java.sql.*;
import javax.sql.*;
import javax.ejb.EJBException;
import java.math.*;

import com.iobeam.portal.model.account.AccountPK;
import com.iobeam.portal.model.prototype.subscription.*;
import com.iobeam.portal.model.venue.VenuePK;
import com.iobeam.portal.security.*;
import com.iobeam.portal.util.*;


public class SubscriptionDAO  {


	private static final String ALL_FIELDS =
			"subscriptionid, " +
			"secure_id, " +

			"accountid, " +
			"subscriptiontypeid, " +
			"subscriptionprototypeid, " +
			"subscriptionstatusid, " +

			"startdate, " +
			"expirationdate, " +

			"billingcycle, " +
			"billingcyclecount, " +
			"costperbillingcycle, " +
			"currentbillingcyclecount, " +

			"iscommissionable, " +
			"defaultcommissionrate, " +

			"parent_subscription_id, " +
			"registered, " +
			"requires_registration, " +
			"channelcode, " +
			"description, " +

			"allowanonymousaccess, " +
			"maxgenerationcount, " +
			"currentgenerationcount, " +
			"effective_venue_id, " +
			"allowstandalone ";


	private static final String SELECT_BY_ID =
			"select " +
			ALL_FIELDS +
			" from subscription " +
			"where subscriptionid = ?";


	private static final String SELECT_BY_SECURE_ID =
			"select " +
			ALL_FIELDS +
			" from subscription " +
			"where SECURE_id = ?";


	private static final String SELECT_BY_ACCOUNT = 
			"select " +
			ALL_FIELDS +
			" from subscription " +
			"where accountid = ?";

	private static final String SELECT_BY_ACCOUNT_TYPE = 
			"select " +
			ALL_FIELDS +
			"from subscription " +
			"where accountid = ? and " +
			"subscriptiontypeid = ?";

	private static final String SELECT_BY_ACCOUNT_STATUS = 
			"select " +
			ALL_FIELDS +
			"from subscription " +
			"where accountid = ? and " +
			"subscriptionstatusid = ?";

	private static final String SELECT_BY_ACCOUNT_TYPE_STATUS = 
			"select " +
			ALL_FIELDS +
			"from subscription " +
			"where accountid = ? and " +
			"subscriptiontypeid = ? and " +
			"subscriptionstatusid = ?";


	private static final String DELETE_BY_ID =
			"delete from subscription where " +
			"subscriptionid = ?";


	private static final String UPDATE =
			"update subscription set " +

			"accountid = ?, " +
			"subscriptionstatusid = ?, " +

			"startdate = ?, " +
			"expirationdate = ?, " +

			"billingcycle = ?, " +
			"billingcyclecount = ?, " +
			"costperbillingcycle = ?, " +
			"currentbillingcyclecount = ?, " +

			"iscommissionable = ?, " +
			"defaultcommissionrate = ?, " +

			"registered = ?, " +
			"channelcode = ?, " +
			"description = ?, " +

			"allowanonymousaccess = ?, " +
			"maxgenerationcount = ?, " +
			"currentgenerationcount = ? " +

			"where subscriptionid = ?";


	static final String INSERT =
			"insert into subscription (" +
			ALL_FIELDS +
			") values " +
			"(?, ?, ?, ?, ?,   ?, ?, ?, ?, ?,  " +
			" ?, ?, ?, ?, ?,   ?, ?, ?, ?, ?,   ?, ?, ?)";



	/**
	* Returns the Subscription for the specified SubscriptionPK,
	* or null if there is none.
	*
	* The resulting Subscription is of the Class that corresponds
	* to the Subscription's SubscriptionType.
	*/
	public static Subscription selectByPK(SubscriptionPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		Subscription subscription = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				subscription = createFromRS(rs);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select subscription by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return subscription;
	}



	/**
	* Returns the Subscription for the specified Subscription key,
	* or null if there is none.
	*
	* The resulting Subscription is of the Class that corresponds
	* to the Subscription's SubscriptionType.
	*/
	public static Subscription selectBySecureID(SecureID key)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		Subscription subscription = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_SECURE_ID);
			ps.setString(1, key.getID().toString());

			rs = ps.executeQuery();

			if (rs.next()) {
				subscription = createFromRS(rs);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select subscription by secureID failed", sqle,
				SELECT_BY_SECURE_ID, key);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return subscription;
	}



	/**
	* Returns all Subscriptions for the specified AccountPK.
	*/
	public static Collection selectByAccountPK(AccountPK pk) 
			throws DataAccessException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector v = new Vector();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ACCOUNT);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			while (rs.next()) {
				v.addElement(createFromRS(rs));
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not select subscriptions",
				sqle, SELECT_BY_ACCOUNT, pk.toString());
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return v;
	}



	/**
	* Returns Subscriptions for the specified SubscriptionType and AccountPK.
	*
	* Returns empty Collection if there are none.
	*/
	public static Collection selectBySubscriptionType(
			AccountPK accountPK, SubscriptionType type)
			throws DataAccessException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector v = new Vector();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ACCOUNT_TYPE);
			ps.setLong(1, accountPK.getID());
			ps.setInt(2, type.getID());

			rs = ps.executeQuery();
			while (rs.next()) {
				v.addElement(createFromRS(rs));
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not select subscriptions", sqle,
				SELECT_BY_ACCOUNT_TYPE, 
				accountPK + "," + type);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return v;
	}


	/**
	* Returns Collection of Subscriptions with specified AccountPK
	* and SubscriptionStatus.
	*
	* Returns empty Collection if there are none.
	*/
	public static Collection selectBySubscriptionStatus(
			AccountPK accountPK, SubscriptionStatus status) 
			throws DataAccessException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector v = new Vector();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ACCOUNT_STATUS);
			ps.setLong(1, accountPK.getID());
			ps.setInt(2, status.getID());

			rs = ps.executeQuery();
			while (rs.next()) {
				v.addElement(createFromRS(rs));
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not select subscriptions", sqle,
				SELECT_BY_ACCOUNT_STATUS, 
				accountPK + "," + status);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return v;
	}



	/**
	* Returns Collection of Subscriptions with specified AccountPK,
	* SubscriptionType, and SubscriptionStatus.
	*
	* Returns empty Collection if there are none.
	*/
	public static Collection selectBySubscriptionStatus(
			AccountPK accountPK,
			SubscriptionType stype,
			SubscriptionStatus status) 
			throws DataAccessException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector v = new Vector();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ACCOUNT_TYPE_STATUS);
			ps.setLong(1, accountPK.getID());
			ps.setInt(2, stype.getID());
			ps.setInt(3, status.getID());

			rs = ps.executeQuery();
			while (rs.next()) {
				v.addElement(createFromRS(rs));
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not select subscriptions", sqle,
				SELECT_BY_ACCOUNT_TYPE_STATUS, 
				accountPK + " " + stype + " " + status);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return v;
	}



	private static Subscription createFromRS(ResultSet rs)
			throws SQLException {

		Subscription s = null;


		// subscriptionid
		SubscriptionPK subscriptionPK =
				new SubscriptionPK(rs.getLong("subscriptionid"));

		// secure_id
		SecureID secureID = null;
		String sid = rs.getString("secure_id");
		if (sid != null) {
			secureID = SecureIDFactory.createSecureID(new BigInteger(sid));
		}

		// accountid
		AccountPK accountPK = new AccountPK(rs.getLong("accountid"));

		// subscriptiontypeid
		SubscriptionType subscriptionType = SubscriptionType.getInstanceFor(
				rs.getInt("subscriptiontypeid"));

		// subscriptionprototypeid
		SubscriptionPrototypePK subscriptionPrototypePK = null;
		subscriptionPrototypePK = new SubscriptionPrototypePK(
				rs.getLong("subscriptionprototypeid"));

		// subscriptionstatusid
		SubscriptionStatus subscriptionStatus =
				SubscriptionStatus.getInstanceFor(
				rs.getInt("subscriptionstatusid"));

		// startdate
		java.util.Date startDate = rs.getTimestamp("startdate");

		// expirationdate
		java.util.Date expirationDate = rs.getTimestamp("expirationdate");

		// billingcycle
		BillingCycle billingCycle = BillingCycle.getInstanceFor(
				rs.getInt("billingcycle"));

		// billingcyclecount
		int billingCycleCount = rs.getInt("billingcyclecount");

		// costperbillingcycle
		Money rate = new Money(rs.getDouble("costperbillingcycle"));

		// currentbillingcyclecount
		int currentBillingCycleCount = rs.getInt("currentbillingcyclecount");

		// iscommissionable
		boolean isCommissionable = "Y".equalsIgnoreCase(
				rs.getString("iscommissionable"));

		// defaultcommissionrate
		double defaultCommissionRate = rs.getDouble("defaultcommissionrate");

		// parent_subscription_id
		SubscriptionPK parentSubscriptionPK = null;
		long parentID = rs.getLong("parent_subscription_id");
		if (!rs.wasNull()) {
			parentSubscriptionPK = new SubscriptionPK(parentID);
		}

		// registered
		boolean isRegistered = "Y".equalsIgnoreCase(
				rs.getString("registered"));

		// requires_registration
		boolean requiresRegistration = "Y".equalsIgnoreCase(
				rs.getString("requires_registration"));

		// channelcode
		String channelCode = rs.getString("channelcode");

		// description
		String description = rs.getString("description");


		boolean allowstandalone = 
				"Y".equals(rs.getString("allowstandalone"));

		if (subscriptionType.equals(SubscriptionType.PUBLIC_VENUE)) {
			// maxgenerationcount
			int maxGenerationCount = rs.getInt("maxgenerationcount");
			// currentgenerationcount
			int currentGenerationCount = rs.getInt("currentgenerationcount");
			// allowanonymousaccess
			boolean hasAnonymousAccess = "Y".equalsIgnoreCase(
					rs.getString("allowanonymousaccess"));

			s = new PublicVenueSubscription(
					subscriptionPK,
					secureID,
					accountPK,
					subscriptionType,
					subscriptionPrototypePK,
					subscriptionStatus,
					startDate,
					expirationDate,
					billingCycle,
					billingCycleCount,
					rate,
					currentBillingCycleCount,
					isCommissionable,
					defaultCommissionRate,
					parentSubscriptionPK,
					isRegistered,
					requiresRegistration,
					channelCode,
					hasAnonymousAccess,
					maxGenerationCount,
					currentGenerationCount,
					allowstandalone);
		}

		if (subscriptionType.equals(SubscriptionType.PUBLIC_MEMBER)) {
			s = new PublicMemberSubscription(
					subscriptionPK,
					secureID,
					accountPK,
					subscriptionType,
					subscriptionPrototypePK,
					subscriptionStatus,
					startDate,
					expirationDate,
					billingCycle,
					billingCycleCount,
					rate,
					currentBillingCycleCount,
					isCommissionable,
					defaultCommissionRate,
					parentSubscriptionPK,
					isRegistered,
					requiresRegistration,
					channelCode);
		}

		if (subscriptionType.equals(SubscriptionType.PRIVATE_VENUE)) {
			// maxgenerationcount
			int maxGenerationCount = rs.getInt("maxgenerationcount");
			// currentgenerationcount
			int currentGenerationCount = rs.getInt("currentgenerationcount");

			s = new PrivateVenueSubscription(
					subscriptionPK,
					secureID,
					accountPK,
					subscriptionType,
					subscriptionPrototypePK,
					subscriptionStatus,
					startDate,
					expirationDate,
					billingCycle,
					billingCycleCount,
					rate,
					currentBillingCycleCount,
					isCommissionable,
					defaultCommissionRate,
					parentSubscriptionPK,
					isRegistered,
					requiresRegistration,
					channelCode,
					maxGenerationCount,
					currentGenerationCount,
					allowstandalone);
		}

		if (subscriptionType.equals(SubscriptionType.PRIVATE_MEMBER)) {
			// effective_venue_id
			VenuePK effectiveVenuePK = null;
			long evID = rs.getLong("effective_venue_id");
			if (!rs.wasNull()) {
				effectiveVenuePK = new VenuePK(evID);
			}

			s = new PrivateMemberSubscription(
					subscriptionPK,
					secureID,
					accountPK,
					subscriptionType,
					subscriptionPrototypePK,
					subscriptionStatus,
					startDate,
					expirationDate,
					billingCycle,
					billingCycleCount,
					rate,
					currentBillingCycleCount,
					isCommissionable,
					defaultCommissionRate,
					parentSubscriptionPK,
					isRegistered,
					requiresRegistration,
					channelCode,
					effectiveVenuePK);
		}

		if (subscriptionType.equals(SubscriptionType.VENUE_STAFF)) {
			// maxgenerationcount
			int maxGenerationCount = rs.getInt("maxgenerationcount");
			// currentgenerationcount
			int currentGenerationCount = rs.getInt("currentgenerationcount");

			s = new VenueStaffSubscription(
					subscriptionPK,
					secureID,
					accountPK,
					subscriptionType,
					subscriptionPrototypePK,
					subscriptionStatus,
					startDate,
					expirationDate,
					billingCycle,
					billingCycleCount,
					rate,
					currentBillingCycleCount,
					isCommissionable,
					defaultCommissionRate,
					parentSubscriptionPK,
					isRegistered,
					requiresRegistration,
					channelCode,
					maxGenerationCount,
					currentGenerationCount);
		}

		if (subscriptionType.equals(SubscriptionType.VENUE_OPERATOR)) {
			// effective_venue_id
			VenuePK effectiveVenuePK = null;
			long evID = rs.getLong("effective_venue_id");
			if (!rs.wasNull()) {
				effectiveVenuePK = new VenuePK(evID);
			}

			s = new VenueOperatorSubscription(
					subscriptionPK,
					secureID,
					accountPK,
					subscriptionType,
					subscriptionPrototypePK,
					subscriptionStatus,
					startDate,
					expirationDate,
					billingCycle,
					billingCycleCount,
					rate,
					currentBillingCycleCount,
					isCommissionable,
					defaultCommissionRate,
					parentSubscriptionPK,
					isRegistered,
					requiresRegistration,
					channelCode,
					effectiveVenuePK);
		}

		if (s != null) {
			s.setDescription(description);
		}

		return s;
	}



	static void delete(SubscriptionPK pk) 
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(DELETE_BY_ID);
			ps.setLong(1, pk.getID());

			int count = ps.executeUpdate();

			if (count == 0) {
				throw new DataNotFoundException(
					"delete subscription by id failed",
					DELETE_BY_ID, pk);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"delete subscription by id failed", sqle,
				DELETE_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}



	/**
	* Updates the specified Subscription record in the database.
	*
	* Throws IllegalStateException if the specified Subscription
	* does not have a SubscriptionPK.
	*/
	static Subscription update(Subscription subscription)  
			throws DataAccessException {


		Logger.getLogger("com.iobeam.portal.model.subscription").info(
				subscription.toString());

		PreparedStatement ps = null;
		Connection conn = null;

		if (subscription.getPK() == null) {
			throw new IllegalStateException("subscription has no pk");
		}

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(UPDATE);

			int i = 1;

			// accountid
			Logger.getLogger("com.iobeam.portal.model.subscription").info(
					"accountID = " + subscription.getAccountPK().getID());
			ps.setLong(i++, subscription.getAccountPK().getID());
			// subscriptionstatusid
			ps.setInt(i++, subscription.getSubscriptionStatus().getID());

			// startdate
			if (subscription.getStartDate() != null) {
				ps.setTimestamp(i++, new java.sql.Timestamp(
						subscription.getStartDate().getTime()));
			} else {
				ps.setNull(i++, Types.DATE);
			}
			// expirationdate
			if (subscription.getExpirationDate() != null) {
				ps.setTimestamp(i++, new java.sql.Timestamp(
						subscription.getExpirationDate().getTime()));
			} else {
				ps.setNull(i++, Types.DATE);
			}

			// billingcycle
			ps.setInt(i++, subscription.getBillingCycle().getID());
			// billingcyclecount
			ps.setInt(i++, subscription.getBillingCycleCount());
			// costperbillingcycle
			ps.setDouble(i++,
					subscription.getCostPerBillingCycle().getAmount());
			// currentbillingcyclecount
			ps.setInt(i++, subscription.getCurrentBillingCycleCount());

			// iscommissionable
			ps.setString(i++, subscription.isCommissionable() ? "Y" : "N");
			// defaultcommissionrate
			ps.setDouble(i++, subscription.getDefaultCommissionRate());

			// registered
			ps.setString(i++, subscription.isRegistered() ? "Y" : "N");

			// channelcode
			if (subscription.getChannelCode() != null) {
				ps.setString(i++, subscription.getChannelCode());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}

			// description
			if (subscription.getDescription() != null) {
				ps.setString(i++, subscription.getDescription());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}

			if (subscription instanceof PublicVenueSubscription) {
				PublicVenueSubscription pvs = (PublicVenueSubscription)
						subscription;

				// allowanonymousaccess
				ps.setString(i++,
						pvs.hasAnonymousAccess() ? "Y" : "N");

				// maxgenerationcount
				ps.setInt(i++, pvs.getMaxGenerationCount());

				// currentgenerationcount
				ps.setInt(i++, pvs.getCurrentGenerationCount());
			} else
			if (subscription instanceof PrivateVenueSubscription) {
				PrivateVenueSubscription pvs = (PrivateVenueSubscription)
						subscription;

				// allowanonymousaccess
				ps.setNull(i++, Types.VARCHAR);

				// maxgenerationcount
				ps.setInt(i++, pvs.getMaxGenerationCount());

				// currentgenerationcount
				ps.setInt(i++, pvs.getCurrentGenerationCount());
			} else
			if (subscription instanceof VenueStaffSubscription) {
				VenueStaffSubscription vss = (VenueStaffSubscription)
						subscription;

				// allowanonymousaccess
				ps.setNull(i++, Types.VARCHAR);

				// maxgenerationcount
				ps.setInt(i++, vss.getMaxGenerationCount());

				// currentgenerationcount
				ps.setInt(i++, vss.getCurrentGenerationCount());
			} else {
				// allowanonymousaccess
				ps.setNull(i++, Types.VARCHAR);

				// maxgenerationcount
				ps.setInt(i++, 0);

				// currentgenerationcount
				ps.setInt(i++, 0);
			}

			ps.setLong(i++, subscription.getPK().getID());

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"update subscription failed",
					UPDATE, subscription);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"update subscription failed", sqle,
				UPDATE, subscription);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return subscription;
	}




	/**
	* Creates a subscription record from the specified Subscription.
	*
	* Returns a Subscription instance with a valid SubscriptionPK.
	*
	* Throws IllegalStateException if the Subscription already has
	* a SubscriptionPK.
	*/
	public static Subscription create(Subscription subscription)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (subscription.getPK() != null) {
			throw new IllegalStateException("subscription already has pk " +
					subscription.getPK());
		}

		long key = BlindKey.getNextKey("SUBSCRIPTION_SEQ");
		subscription.setPK(new SubscriptionPK(key));

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(INSERT);

			int i = 1;

			// subscriptionid
			ps.setLong(i++, subscription.getPK().getID());
			// secure_id
			if (subscription.getSecureID() != null) {
				ps.setString(i++,
						subscription.getSecureID().getID().toString());
			} else {
				ps.setNull(i++, Types.NUMERIC);
			}
			// accountid
			ps.setLong(i++, subscription.getAccountPK().getID());
			// subscriptiontypeid
			ps.setInt(i++, subscription.getSubscriptionType().getID());
			// subscriptionprototypeid
			ps.setLong(i++, subscription.getSubscriptionPrototypePK().getID());
			// subscriptionstatusid
			ps.setInt(i++, subscription.getSubscriptionStatus().getID());

			// startdate
			if (subscription.getStartDate() != null) {
				ps.setTimestamp(i++,
						new Timestamp(subscription.getStartDate().getTime()));
			} else {
				ps.setNull(i++, Types.DATE);
			}

			// expirationdate
			if (subscription.getExpirationDate() != null) {
				ps.setTimestamp(i++,
						new Timestamp(
						subscription.getExpirationDate().getTime()));
			} else {
				ps.setNull(i++, Types.DATE);
			}

			// billingcycle
			ps.setInt(i++, subscription.getBillingCycle().getID());
			// billingcyclecount
			ps.setInt(i++, subscription.getBillingCycleCount());
			// costperbillingcycle
			ps.setDouble(i++,
					subscription.getCostPerBillingCycle().getAmount());
			// currentbillingcyclecount
			ps.setInt(i++, subscription.getCurrentBillingCycleCount());
			// iscommissionable
			ps.setString(i++, subscription.isCommissionable() ? "Y" : "N");
			// defaultcommissionrate
			ps.setDouble(i++, subscription.getDefaultCommissionRate());

			// parent_subscription_id
			if (subscription.getParentSubscriptionPK() != null) {
				ps.setLong(i++,
						subscription.getParentSubscriptionPK().getID());
			} else {
				ps.setNull(i++, Types.NUMERIC);
			}
			// registered
			ps.setString(i++, subscription.isRegistered() ? "Y" : "N");
			// requires_registration
			ps.setString(i++, subscription.requiresRegistration() ? "Y" : "N");

			// channelcode
			if (subscription.getChannelCode() != null) {
				ps.setString(i++, subscription.getChannelCode());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}

			// description
			if (subscription.getDescription() != null) {
				ps.setString(i++, subscription.getDescription());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}


			if (subscription instanceof PublicVenueSubscription) {
				PublicVenueSubscription pvs = (PublicVenueSubscription)
						subscription;

				// allowanonymousaccess
				ps.setString(i++, pvs.hasAnonymousAccess() ? "Y" : "N");

				// maxgenerationcount
				ps.setInt(i++, pvs.getMaxGenerationCount());

				// currentgenerationcount
				ps.setInt(i++, pvs.getCurrentGenerationCount());

				// effectivevenueid
				ps.setNull(i++, Types.NUMERIC);
			} else

			if (subscription instanceof PublicMemberSubscription) {
				PublicMemberSubscription pms = (PublicMemberSubscription)
						subscription;

				// allowanonymousaccess
				ps.setNull(i++, Types.VARCHAR);

				// maxgenerationcount
				ps.setNull(i++, Types.NUMERIC);

				// currentgenerationcount
				ps.setNull(i++, Types.NUMERIC);

				// effectivevenueid
				ps.setNull(i++, Types.NUMERIC);
			} else

			if (subscription instanceof PrivateVenueSubscription) {
				PrivateVenueSubscription pvs = (PrivateVenueSubscription)
						subscription;

				// allowanonymousaccess
				ps.setNull(i++, Types.VARCHAR);

				// maxgenerationcount
				ps.setInt(i++, pvs.getMaxGenerationCount());

				// currentgenerationcount
				ps.setInt(i++, pvs.getCurrentGenerationCount());

				// effectivevenueid
				ps.setNull(i++, Types.NUMERIC);
			} else

			if (subscription instanceof PrivateMemberSubscription) {
				PrivateMemberSubscription pms = (PrivateMemberSubscription)
						subscription;

				// allowanonymousaccess
				ps.setNull(i++, Types.VARCHAR);

				// maxgenerationcount
				ps.setNull(i++, Types.NUMERIC);

				// currentgenerationcount
				ps.setNull(i++, Types.NUMERIC);

				// effectivevenueid
				ps.setLong(i++, pms.getEffectiveVenuePK().getID());
			} else

			if (subscription instanceof VenueStaffSubscription) {
				VenueStaffSubscription vss = (VenueStaffSubscription)
						subscription;

				// allowanonymousaccess
				ps.setNull(i++, Types.VARCHAR);

				// maxgenerationcount
				ps.setInt(i++, vss.getMaxGenerationCount());

				// currentgenerationcount
				ps.setInt(i++, vss.getCurrentGenerationCount());

				// effectivevenueid
				ps.setNull(i++, Types.NUMERIC);
			} else

			if (subscription instanceof VenueOperatorSubscription) {
				VenueOperatorSubscription vos = (VenueOperatorSubscription)
						subscription;

				// allowanonymousaccess
				ps.setNull(i++, Types.VARCHAR);

				// maxgenerationcount
				ps.setNull(i++, Types.NUMERIC);

				// currentgenerationcount
				ps.setNull(i++, Types.NUMERIC);

				// effectivevenueid
				ps.setLong(i++, vos.getEffectiveVenuePK().getID());
			}

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"create subscription failed",
					INSERT, subscription);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"create subscription failed", sqle,
				INSERT, subscription);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return subscription;
	}
}
