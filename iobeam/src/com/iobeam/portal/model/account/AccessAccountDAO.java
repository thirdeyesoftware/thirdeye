package com.iobeam.portal.model.account;


import java.util.Vector;
import java.util.Date;
import java.util.Collection;
import java.sql.*;

import com.iobeam.portal.model.customer.CustomerPK;
import com.iobeam.portal.model.billablecustomer.BillableCustomerPK;
import com.iobeam.portal.util.DataAccessException;
import com.iobeam.portal.util.DBHelper;
import com.iobeam.portal.util.BlindKey;

public class AccessAccountDAO  {

		
	public static Collection selectAllOpenAccounts() 
			throws DataAccessException {
		Vector v = new Vector();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer(0);
		sb.append("SELECT * FROM ACCOUNT WHERE AccountStatusID = ?");
		sb.append(" ORDER BY AccountID ASC");
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, AccountStatus.OPEN.getID());
			rs = ps.executeQuery();
			while (rs.next()) {
				AccountData data = 
					createFromRS(rs);
				v.addElement(data);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to select accounts", sb.toString(), 
				AccountStatus.OPEN.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception ez) { }
		}
		return v;
		
	}

	public static Collection selectByCustomerPK(CustomerPK pk) 
			throws DataAccessException {
		Vector v = new Vector();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer(0);
		sb.append("SELECT * FROM ACCOUNT WHERE CustomerID = ?");
		sb.append(" ORDER BY AccountID ASC");
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, pk.getID());
			rs = ps.executeQuery();
			while (rs.next()) {
				AccountData data = createFromRS(rs);
				v.addElement(data);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to select accounts", sb.toString(), 
				pk.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception ez) { }
		}
		return v;
		
	}

	public static Collection selectByBillableCustomerPK(BillableCustomerPK pk) 
			throws DataAccessException {
		Vector v = new Vector();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer(0);
		sb.append("SELECT * FROM ACCOUNT WHERE BillableCustomerID = ?");
		sb.append(" ORDER BY AccountID ASC");
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, pk.getID());
			rs = ps.executeQuery();
			while (rs.next()) {
				AccountData data = createFromRS(rs);
				v.addElement(data);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to select accounts", sb.toString(), 
				pk.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception ez) { }
		}
		return v;
		
	}
	
	public static AccountData createFromRS(ResultSet rs) 
			throws SQLException {
		AccountPK pk = new AccountPK(rs.getLong("AccountID"));
		CustomerPK custPK = 	
			new CustomerPK(rs.getLong("CustomerID"));
		BillableCustomerPK billablePK = 	
			new BillableCustomerPK(
				new CustomerPK(rs.getLong("BillableCustomerID")));
		AccountStatus status = 
			AccountStatus.getInstanceFor(rs.getLong("AccountStatusID"));
		AccountNumber number = 
				new AccountNumber(rs.getString("AccountNumber"));
		return new AccountData(pk, custPK, billablePK, number, status);

	}
			

}
