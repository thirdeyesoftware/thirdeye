package com.iobeam.portal.model.prototype.subscription;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import javax.ejb.EJBException;
import java.math.*;

import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.util.*;


public class SubscriptionPrototypeDAO  {


	private static final String ALL_FIELDS =
			"subscriptionprototypeid, " +
			"description, " +
			"subscriptiontypeid, " +

			"billingcycleamount, " +
			"billingcycle, " +
			"billingcyclecount, " +
			"istaxable, " +

			"iscommissionable, " +
			"defaultcommissionrate, " +
			"duration, " +
			"requiresregistration, " +

			"active, " +

			"allowanonymousaccess, " +
			"maxgenerationcount, " + 
			"allowstandalone ";


	private static final String SELECT_BY_ID =
			"select " +
			ALL_FIELDS +
			"from subscription_prototype " +
			"where subscriptionprototypeid = ?";


	private static final String SELECT_BY_SUBSCRIPTION_TYPE_AND_ACTIVE = 
			"select " +
			ALL_FIELDS +
			"from subscription_prototype " +
			"where subscriptiontypeid = ? and " +
			"active = ?";


	private static final String DELETE_BY_ID =
			"delete from subscription_prototype where " +
			"subscriptionprototypeid = ?";


	static final String INSERT =
			"insert into subscription_prototype (" +
			ALL_FIELDS +
			") values " +
			"(?, ?, ?, ?, ?,   ?, ?, ?, ?, ?,  " +
			" ?, ?, ?,?)";



	/**
	* Returns the SubscriptionPrototype for the specified
	* SubscriptionPrototypePK, or null if there is none.
	*
	* The resulting SubscriptionPrototype is of the Class that corresponds
	* to the prototypes's SubscriptionType.
	*/
	public static SubscriptionPrototype selectByPK(SubscriptionPrototypePK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		SubscriptionPrototype prototype = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				prototype = createFromRS(rs);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select subscription_prototype by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return prototype;
	}



	/**
	* Returns all SubscriptionPrototypes for the specified
	* SubscriptionType and available status.
	*/
	public static Collection selectBySubscriptionType(
			SubscriptionType subscriptionType,
			boolean isAvailable) 
			throws DataAccessException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector v = new Vector();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_SUBSCRIPTION_TYPE_AND_ACTIVE);
			ps.setLong(1, subscriptionType.getID());
			ps.setString(2, isAvailable ? "Y" : "N");

			rs = ps.executeQuery();

			while (rs.next()) {
				v.addElement(createFromRS(rs));
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not select subscriptionprototypes",
				sqle, SELECT_BY_SUBSCRIPTION_TYPE_AND_ACTIVE,
				subscriptionType.toString() + " active=" + isAvailable);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return v;
	}



	private static SubscriptionPrototype createFromRS(ResultSet rs)
			throws SQLException {

		SubscriptionPrototype prototype = null;


		// subscriptionprototypeid
		SubscriptionPrototypePK subscriptionPrototypePK =
				new SubscriptionPrototypePK(rs.getLong(
				"subscriptionprototypeid"));

		// description
		String description = rs.getString("description");

		// subscriptiontypeid
		SubscriptionType subscriptionType = SubscriptionType.getInstanceFor(
				rs.getInt("subscriptiontypeid"));

		// billingcycleamount
		Money costPerBillingCycle =
				new Money(rs.getDouble("billingcycleamount"));

		// billingcycle
		BillingCycle billingCycle = BillingCycle.getInstanceFor(
				rs.getInt("billingcycle"));

		// billingcyclecount
		int billingCycleCount = rs.getInt("billingcyclecount");

		// istaxable
		boolean isTaxable = "Y".equalsIgnoreCase(rs.getString("istaxable"));

		// iscommissionable
		boolean isCommissionable = "Y".equalsIgnoreCase(
				rs.getString("iscommissionable"));

		// defaultcommissionrate
		double defaultCommissionRate = rs.getDouble("defaultcommissionrate");

		// duration
		long durationMillis = rs.getLong("duration");
		if (rs.wasNull()) {
			throw new NullPointerException("duration");
		}
		Duration duration = new Duration(durationMillis);

		// requiresregistration
		boolean requiresRegistration = "Y".equalsIgnoreCase(
				rs.getString("requiresregistration"));

		// isactive
		boolean isAvailable = "Y".equalsIgnoreCase(rs.getString("active"));

		boolean allowstandalone = "Y".equals(rs.getString("allowstandalone"));

		if (subscriptionType.equals(SubscriptionType.PUBLIC_VENUE)) {
			// allowanonymousaccess
			boolean hasAnonymousAccess = "Y".equalsIgnoreCase(
					rs.getString("allowanonymousaccess"));
			// maxgenerationcount
			int maxGenerationCount = rs.getInt("maxgenerationcount");

			prototype = new PublicVenueSubscriptionPrototype(
					subscriptionPrototypePK,
					subscriptionType,
					description,
					isAvailable,
					costPerBillingCycle,
					billingCycle,
					billingCycleCount,
					isTaxable,
					isCommissionable,
					defaultCommissionRate,
					duration,
					requiresRegistration,
					hasAnonymousAccess,
					maxGenerationCount,
					allowstandalone);
		}

		if (subscriptionType.equals(SubscriptionType.PUBLIC_MEMBER)) {
			prototype = new PublicMemberSubscriptionPrototype(
					subscriptionPrototypePK,
					subscriptionType,
					description,
					isAvailable,
					costPerBillingCycle,
					billingCycle,
					billingCycleCount,
					isTaxable,
					isCommissionable,
					defaultCommissionRate,
					duration,
					requiresRegistration);
		}

		if (subscriptionType.equals(SubscriptionType.PRIVATE_VENUE)) {
			// maxgenerationcount
			int maxGenerationCount = rs.getInt("maxgenerationcount");

			prototype = new PrivateVenueSubscriptionPrototype(
					subscriptionPrototypePK,
					subscriptionType,
					description,
					isAvailable,
					costPerBillingCycle,
					billingCycle,
					billingCycleCount,
					isTaxable,
					isCommissionable,
					defaultCommissionRate,
					duration,
					requiresRegistration,
					maxGenerationCount,
					allowstandalone);
		}

		if (subscriptionType.equals(SubscriptionType.PRIVATE_MEMBER)) {

			prototype = new PrivateMemberSubscriptionPrototype(
					subscriptionPrototypePK,
					subscriptionType,
					description,
					isAvailable,
					costPerBillingCycle,
					billingCycle,
					billingCycleCount,
					isTaxable,
					isCommissionable,
					defaultCommissionRate,
					duration,
					requiresRegistration);
		}

		if (subscriptionType.equals(SubscriptionType.VENUE_STAFF)) {
			// maxgenerationcount
			int maxGenerationCount = rs.getInt("maxgenerationcount");

			prototype = new VenueStaffSubscriptionPrototype(
					subscriptionPrototypePK,
					subscriptionType,
					description,
					isAvailable,
					costPerBillingCycle,
					billingCycle,
					billingCycleCount,
					isTaxable,
					isCommissionable,
					defaultCommissionRate,
					duration,
					requiresRegistration,
					maxGenerationCount);
		}

		if (subscriptionType.equals(SubscriptionType.VENUE_OPERATOR)) {

			prototype = new VenueOperatorSubscriptionPrototype(
					subscriptionPrototypePK,
					subscriptionType,
					description,
					isAvailable,
					costPerBillingCycle,
					billingCycle,
					billingCycleCount,
					isTaxable,
					isCommissionable,
					defaultCommissionRate,
					duration,
					requiresRegistration);

		}

		return prototype;
	}



	static void delete(SubscriptionPrototypePK pk) 
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
					"delete subscriptionPrototype by id failed",
					DELETE_BY_ID, pk);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"delete subscriptionPrototype by id failed", sqle,
				DELETE_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}



	/**
	* Creates a subscription_prototype record from the
	* specified SubscriptionPrototype.
	*
	* Returns a SubscriptionPrototype instance with a
	* valid SubscriptionPrototypePK.
	*
	* Throws IllegalStateException if the SubscriptionPrototype already has
	* a SubscriptionPrototypePK.
	*/
	public static SubscriptionPrototype create(
			SubscriptionPrototype prototype)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (prototype.getPK() != null) {
			throw new IllegalStateException("subscriptionPrototype already " +
					"has pk " + prototype.getPK());
		}

		long key = BlindKey.getNextKey("SUBSCRIPTION_PROTOTYPE_SEQ");
		prototype.setPK(new SubscriptionPrototypePK(key));

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(INSERT);

			int i = 1;

			// subscriptionprototypeid
			ps.setLong(i++, prototype.getPK().getID());
			// description
			ps.setString(i++, prototype.getDescription());
			// subscriptiontypeid
			ps.setInt(i++, prototype.getSubscriptionType().getID());

			// billingcycleamount
			ps.setDouble(i++, prototype.getCostPerBillingCycle().getAmount());
			// billingcycle
			ps.setInt(i++, prototype.getBillingCycle().getID());
			// billingcyclecount
			ps.setInt(i++, prototype.getBillingCycleCount());
			// istaxable
			ps.setString(i++, prototype.isTaxable() ? "Y" : "N");
			// iscommissionable
			ps.setString(i++, prototype.isCommissionable() ? "Y" : "N");
			// defaultcommissionrate
			ps.setDouble(i++, prototype.getDefaultCommissionRate());
			// duration
			ps.setLong(i++, prototype.getDuration().getTime());
			// requiresregistration
			ps.setString(i++, prototype.requiresRegistration() ? "Y" : "N");

			// active
			ps.setString(i++, prototype.isAvailable() ? "Y" : "N");


			if (prototype instanceof PublicVenueSubscriptionPrototype) {
				PublicVenueSubscriptionPrototype pvsp =
						(PublicVenueSubscriptionPrototype) prototype;

				ps.setString(i++, pvsp.hasAnonymousAccess() ? "Y" : "N");
				ps.setInt(i++, pvsp.getMaxGenerationCount());
			} else

			if (prototype instanceof PublicMemberSubscriptionPrototype) {
				PublicMemberSubscriptionPrototype pmsp =
						(PublicMemberSubscriptionPrototype) prototype;

				ps.setNull(i++, Types.VARCHAR);
				ps.setNull(i++, Types.NUMERIC);
			} else

			if (prototype instanceof PrivateVenueSubscriptionPrototype) {
				PrivateVenueSubscriptionPrototype pvsp =
						(PrivateVenueSubscriptionPrototype) prototype;

				ps.setNull(i++, Types.VARCHAR);
				ps.setInt(i++, pvsp.getMaxGenerationCount());
			} else

			if (prototype instanceof PrivateMemberSubscriptionPrototype) {
				PrivateMemberSubscriptionPrototype pmsp =
						(PrivateMemberSubscriptionPrototype) prototype;

				ps.setNull(i++, Types.VARCHAR);
				ps.setNull(i++, Types.NUMERIC);
			} else

			if (prototype instanceof VenueStaffSubscriptionPrototype) {
				VenueStaffSubscriptionPrototype vssp =
						(VenueStaffSubscriptionPrototype) prototype;

				ps.setNull(i++, Types.VARCHAR);
				ps.setInt(i++, vssp.getMaxGenerationCount());
			} else

			if (prototype instanceof VenueOperatorSubscriptionPrototype) {
				VenueOperatorSubscriptionPrototype vosp =
						(VenueOperatorSubscriptionPrototype) prototype;

				ps.setNull(i++, Types.VARCHAR);
				ps.setNull(i++, Types.NUMERIC);
			}

			ps.setNull(i++, Types.VARCHAR);

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"create subscriptionPrototype failed",
					INSERT, prototype);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"create subscriptionPrototype failed", sqle,
				INSERT, prototype);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return prototype;
	}
}
