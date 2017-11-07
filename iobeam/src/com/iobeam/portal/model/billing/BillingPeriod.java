package com.iobeam.portal.model.billing;

import java.util.Date;
import java.io.Serializable;
import javax.ejb.EJBException;
import java.sql.*;
import java.util.Date;
import com.iobeam.portal.util.*;
import javax.ejb.ObjectNotFoundException;
import java.text.SimpleDateFormat;

/**
 * describes a date range.
 */
public class BillingPeriod implements Serializable {

	public static final long serialVersionUID = 200304170103L;

	private Date mStartDate;
	private Date mEndDate;

	private int mCurrentStep;

	private BillingPeriod(Date startDate, Date endDate, int step) {
		mStartDate = startDate;
		mEndDate = endDate;
		mCurrentStep = step;
	}

	public Date getStartDate() {
		return mStartDate;
	}

	public Date getEndDate() {
		return mEndDate;
	}

	public int getCurrentStep() {
		return mCurrentStep;
	}

	/**
	 * retrieves from db a valid billing period for the target date.
	 */
	public static BillingPeriod getInstanceFor(Date target) 
		throws ObjectNotFoundException {
		Connection c = null;
		try {
			c = DBHelper.getConnection();
			return getInstanceFor(c, target);
		}
		catch (SQLException sqle) {
			throw new EJBException(sqle);
		}

	}

	public static BillingPeriod getInstanceFor(Connection c, Date target) 
			throws ObjectNotFoundException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String dFormat = (new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).
			format(target);

		String sql = "SELECT * FROM BILLING_PERIOD WHERE StartDate <= ? AND " + 
			"EndDate >= ?";

		BillingPeriod billingPeriod = null;
		
		try {
			ps = c.prepareStatement(sql);

			ps.setTimestamp(1, new Timestamp(target.getTime()));
			ps.setTimestamp(2, new Timestamp(target.getTime()));
			rs = ps.executeQuery();
			if (rs.next()) {
				billingPeriod = new BillingPeriod(
						new java.util.Date(rs.getTimestamp("StartDate").getTime()),
						new java.util.Date(rs.getTimestamp("EndDate").getTime()),
						rs.getInt("currentbillingstep"));
			} else {
				System.out.println(sql.toString());
				System.out.println((new Timestamp(target.getTime())).toString());

				throw new ObjectNotFoundException(
					"could not find valid billing period for " + target.toString());
			}
		}
		catch (SQLException sqle) {
			System.out.println(sqle.toString());
			throw new ObjectNotFoundException("could not find billing period for " + 
				target.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception eee) { }
		}
		return billingPeriod;
	}

	public static BillingPeriod getInstanceFor(String host, int port, 
			String user, String password, String dsname, Date target) 
				throws Exception {
		Connection c = DBHelper.getConnection(host, port, user, password, dsname);
		return getInstanceFor(c, target);
	}

		
		
	public boolean equals(Object o) {
		if (o instanceof BillingPeriod) {
			BillingPeriod bp = (BillingPeriod)o;
			return (getStartDate().equals(bp.getStartDate()) &&
							getEndDate().equals(bp.getEndDate()));
		}
		return false;
	}

	/**
	 * returns true if specified date is within the range of
	 * billing period's start and end dates.
	 */
	public boolean contains(Date target) 
			throws NullPointerException {
		if (target == null) {
			throw new NullPointerException("target");
		}
		return (target.before(mEndDate) && target.after(mStartDate));
	}

	/**
	 * updates the current billing step for this billing period.
	 * @see BillingController for step definition.
	 */
	public void updateCurrentStep(int step) throws DataAccessException {

		String sql = "UPDATE BILLING_PERIOD set currentbillingstep = ? where " + 
			" startdate = ? and enddate = ?";

		PreparedStatement ps = null;
		Connection c = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setInt(1, step);
			ps.setTimestamp(2, 
				new Timestamp(mStartDate.getTime()));
			ps.setTimestamp(3, 
				new Timestamp(mEndDate.getTime()));
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"could not update billing period", sql, String.valueOf(step));
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not update billing period", sqle, sql, String.valueOf(step));
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception ez) {
			}
		}
		mCurrentStep = step;
	}

	/**
	 * creates a new billing period for the specified start and end dates.
	 */
	public static BillingPeriod create(java.util.Date start, java.util.Date end)
			throws DataAccessException {
		String sql = "INSERT INTO BILLING_PERIOD (" + 
			"billingperiodid, startdate, enddate, currentbillingstep) " + 
			"values (?,?,?,?)";
		PreparedStatement ps = null;
		Connection c = null;
		long key = 0;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setLong(1, key = BlindKey.getNextKey("billing_period_seq"));
			ps.setTimestamp(
				2, new Timestamp(start.getTime()));
			ps.setTimestamp(
				2, new Timestamp(end.getTime()));
			ps.setInt(3, 0);
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"could not create billing period",
					sql, (start.toString() + end.toString()));
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not create billing period",
				sqle, sql, (start.toString() + end.toString()));
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception ez) {
			}
		}
		return new BillingPeriod(start, end, 0);
	}

	public String toString() {
		return "BillingPeriod: " + mStartDate.toString() + 
			" to " + mEndDate.toString() + 
			", step = " + getCurrentStep();
	}
	
}

