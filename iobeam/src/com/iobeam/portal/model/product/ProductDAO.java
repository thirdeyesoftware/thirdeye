package com.iobeam.portal.model.product;

import javax.sql.*;
import java.sql.*;

import java.util.*;
import com.iobeam.portal.util.*;

/**
 * data delegate for Products
 */
public class ProductDAO  {

	public static void create(Product product) 
			throws DataAccessException {

		StringBuffer sb = new StringBuffer(0);
		sb.append("INSERT INTO Product (ProductID, ProductNumber");
		sb.append("ProductCategoryID, Description, Price, ");
		sb.append("BillingCycleCount, IsCommissionable, ");
		sb.append("DefaultCommissionRate, IsTaxable, Active) VALUES ");
		sb.append("(?,?,?,?,?,?,?,?,?,?,?)");
		PreparedStatement ps = null;
		Connection c = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, product.getPK().getID());
			ps.setString(2, product.getProductNumber());
			ps.setLong(3, 
				product.getProductCategory().getPK().getID());
			ps.setString(4, product.getDescription());
			ps.setDouble(5, product.getPrice().getAmount());
			ps.setInt(6, product.getBillingCycleCount());
			ps.setString(7, product.isCommissionable() ? "Y" : "N");
			ps.setDouble(8, product.getDefaultCommissionRate());
			ps.setString(9, product.isTaxable() ? "Y" : "N");
			ps.setString(10, product.isActive() ? "Y" : "N");

			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"unable to create product ", 
					sb.toString(), product.toString());
			}

		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to create product", sqle, 
				sb.toString(), product.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception e) { }
		}
	}

	public void update(Product product) throws DataAccessException  {
		StringBuffer sb = new StringBuffer(0);
		sb.append("UPDATE PRODUCT SET ProductNumber = ?, Price = ?, ");
		sb.append("Description = ?, ProductCategoryID = ?, isCommissionable = ?, ");
		sb.append("DefaultCommissionRate = ?, isTaxable = ?, ");
		sb.append("BillingCycleCount = ?, Price = ?, Active = ? ");
		sb.append(" WHERE ProductID = ? ");

		PreparedStatement ps = null;
		Connection c = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setString(1, product.getProductNumber());
			ps.setDouble(2, product.getPrice().getAmount());
			ps.setString(3, product.getDescription());
			ps.setLong(4, product.getProductCategory().getPK().getID());
			ps.setString(5, product.isCommissionable() ? "Y" : "N");
			ps.setDouble(6, product.getDefaultCommissionRate());
			ps.setString(7, product.isTaxable() ? "Y" : "N");
			ps.setInt(8, product.getBillingCycleCount());
			ps.setDouble(9, product.getPrice().getAmount());
			ps.setString(10, product.isActive() ? "Y" : "N");
			ps.setLong(11, product.getPK().getID());
			
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"unable to update product", 
					sb.toString(), product.toString());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to update product", sqle, 
				sb.toString(), product.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception ee) { }
		}
	}

	public void delete(ProductPK pk) throws DataAccessException {
		StringBuffer sb = new StringBuffer(0);
		sb.append("DELETE FROM PRODUCT WHERE ProductID = ?");
		PreparedStatement ps = null;
		Connection c = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"unable to delete product", 
					sb.toString(), pk.toString());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to delete product", 
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
		String sql = "SELECT * FROM PRODUCT ";
		Statement stmt = null;
		Connection c = null;
		Vector items = new Vector();
		ResultSet rs = null;
		try {
			c = DBHelper.getConnection();
			stmt  = c.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Product product = new Product(
					new ProductPK(rs.getLong("ProductID")));
				populate(product, rs);
				items.addElement(product);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrieve products", sqle, sql);
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
		String sql = "SELECT * FROM PRODUCT WHERE ACTIVE = 'Y' " + 
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
				Product product = new Product(
					new ProductPK(rs.getLong("ProductID")));
				populate(product, rs);
				items.addElement(product);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrieve active products", sqle, sql);
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

	public static Product selectByPK(ProductPK pk)  
			throws DataAccessException {
		String sql = "SELECT * FROM Product WHERE ProductID = ? ";
		PreparedStatement ps = null;
		Connection c = null;
		Product product = null;
		ResultSet rs = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setLong(1, pk.getID());
			rs = ps.executeQuery();
			if (rs.next()) {
				product = new Product(
					new ProductPK(rs.getLong("ProductID")));
				populate(product, rs);

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
		return product;
	}


	public static Product selectByProductNumber(String productNumber)  
			throws DataAccessException {
		String sql = "SELECT * FROM Product WHERE ProductNumber = ? ";
		PreparedStatement ps = null;
		Connection c = null;
		Product product = null;
		ResultSet rs = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setString(1, productNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				product = new Product(
					new ProductPK(rs.getLong("ProductID")));
				populate(product, rs);

			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrieve product categories", sqle, sql, productNumber);
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception ee) { }
		}
		return product;
	}

	public static Collection selectByCategory(ProductCategoryPK pk)  
			throws DataAccessException {
		String sql = "SELECT * FROM Product WHERE ProductCategoryID = ? ";
		PreparedStatement ps = null;
		Connection c = null;
		Vector v = new Vector();
		ResultSet rs = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setLong(1, pk.getID());
			rs = ps.executeQuery();
			while (rs.next()) {
				Product product = new Product(
					new ProductPK(rs.getLong("ProductID")));
				populate(product, rs);
				v.addElement(product);
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
		return v;
	}



	private static void populate(Product product, ResultSet rs) 
			throws DataAccessException, SQLException {
		product.setProductNumber(rs.getString("ProductNumber"));
		product.setDescription(rs.getString("Description"));
		product.setProductCategory(
			ProductCategoryDAO.selectByPK(
				new ProductCategoryPK(rs.getLong("ProductCategoryID"))));
		product.setIsCommissionable(rs.getString("IsCommissionable").equals("Y"));
		product.setDefaultCommissionRate(
			rs.getDouble("DefaultCommissionRate"));
		product.setBillingCycleCount(rs.getInt("BillingCycleCount"));
		product.setPrice(
			new Money(rs.getDouble("Price")));
		product.setIsActive(rs.getString("Active").equals("Y"));
		product.setIsTaxable(rs.getString("Active").equals("Y"));
		
	}
			
}
