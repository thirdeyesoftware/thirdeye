package com.iobeam.portal.model.product;

import javax.sql.*;
import java.sql.*;

import java.util.*;
import com.iobeam.portal.util.*;

/**
 * data delegate for Product Categories
 */
public class ProductCategoryDAO  {

	public static void create(ProductCategory category) 
			throws DataAccessException {

		StringBuffer sb = new StringBuffer(0);
		sb.append("INSERT INTO ProductCategory (ProductCategoryID, ");
		sb.append("Category, ParentCategoryID, Active) VALUES (?,?,?,?)");
		PreparedStatement ps = null;
		Connection c = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, category.getPK().getID());
			ps.setString(2, category.getDescription());
			if (category.getParentCategoryPK() != null) {
				ps.setLong(3, category.getParentCategoryPK().getID());
			}
			else {
				ps.setLong(3, 0L);
			}
			ps.setString(4, "Y");
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"unable to create product category", 
					sb.toString(), category.toString());
			}

		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to create product category", sqle, 
				sb.toString(), category.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception e) { }
		}
	}

	public static void update(ProductCategory category) 
			throws DataAccessException  {
		StringBuffer sb = new StringBuffer(0);
		sb.append("UPDATE PRODUCT_CATEGORY set Category = ?,ParentCategoryID = ?,");
		sb.append("Active = ? WHERE ProductCategoryID = ?");
		PreparedStatement ps = null;
		Connection c = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setString(1, category.getDescription());
			if (category.getParentCategoryPK() != null) {
				ps.setLong(2, category.getParentCategoryPK().getID());
			}
			else {
				ps.setLong(2, 0L);
			}
			ps.setString(3, category.isActive() ? "Y" : "N");
			ps.setLong(4, category.getPK().getID());
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"unable to update product category", 
					sb.toString(), category.toString());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to update product category", sqle, 
				sb.toString(), category.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception ee) { }
		}
	}

	public static void delete(ProductCategoryPK pk) throws DataAccessException {
		StringBuffer sb = new StringBuffer(0);
		sb.append("DELETE FROM PRODUCT_CATEGORY WHERE ProductCategoryID = ?");
		PreparedStatement ps = null;
		Connection c = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"unable to delete product category", 
					sb.toString(), pk.toString());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to delete product category", 
				sqle, sb.toString(), pk.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception ee) { }
		}
	}

	public static Collection selectAll() throws DataAccessException {
		String sql = "SELECT * FROM PRODUCT_CATEGORY ORDER BY Category ASC ";
		Statement stmt = null;
		Connection c = null;
		Vector items = new Vector();
		ResultSet rs = null;
		try {
			c = DBHelper.getConnection();
			stmt  = c.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ProductCategory category = new ProductCategory(
					new ProductCategoryPK(rs.getLong("ProductCategoryID")));
				populate(category, rs);
				items.addElement(category);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrieve product categories", sqle, sql);
		}
		finally {
			try {
				rs.close();
				stmt.close();
				c.close();
			}
			catch (Exception ee) { }
		}
		return items;
	}
	
	public static Collection selectActive() throws DataAccessException  {
		String sql = "SELECT * FROM PRODUCT_CATEGORY WHERE ACTIVE = 'Y' " + 
			" ORDER BY Category ASC ";
		Statement stmt = null;
		Connection c = null;
		Vector items = new Vector();
		ResultSet rs = null;
		try {
			c = DBHelper.getConnection();
			stmt  = c.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ProductCategory category = new ProductCategory(
					new ProductCategoryPK(rs.getLong("ProductCategoryID")));
				populate(category, rs);
				items.addElement(category);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrieve active product categories", sqle, sql);
		}
		finally {
			try {
				rs.close();
				stmt.close();
				c.close();
			}
			catch (Exception ee) { }
		}
		return items;
	}					

	public static ProductCategory selectByPK(ProductCategoryPK pk)  
			throws DataAccessException {
		String sql = "SELECT * FROM PRODUCT_CATEGORY WHERE ProductCategoryID = ? " +
			" ORDER BY Category ASC ";
		PreparedStatement ps = null;
		Connection c = null;
		ProductCategory category = null;
		ResultSet rs = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setLong(1, pk.getID());
			rs = ps.executeQuery();

			if (rs.next()) {
				category = new ProductCategory(
					new ProductCategoryPK(rs.getLong("ProductCategoryID")));
				populate(category, rs);

			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrieve product categories", sqle, sql, pk.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception ee) { }
		}
		return category;
	}

	private static void populate(ProductCategory category, ResultSet rs) 
			throws SQLException {
		category.setDescription(rs.getString("Category"));
		if (rs.getLong("ParentCategoryID")  > 0) {
			category.setParentCategoryPK(
				new ProductCategoryPK(rs.getLong("ParentCategoryID")));
		}
		category.setIsActive(rs.getString("Active").equals("Y"));
	}
			
}