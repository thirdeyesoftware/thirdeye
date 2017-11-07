package com.iobeam.portal.model.asset;


import javax.sql.*;
import java.sql.*;
import java.util.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.product.*;


/**
*/
public class AssetDAO  {

	static final String SELECT_BY_ID =
			"select " +
			"assetid, " +
			"productid, " +
			"description, " +
			"serialnumber " +
			"from asset " +
			"where assetid = ?";


	static final String DELETE_BY_ID =
			"delete from asset where " +
			"assetid = ?";


	static final String DELETE_BY_PRODUCT_ID =
			"delete from asset where " +
			"productid = ?";


	static final String INSERT =
			"insert into asset (" +
			"assetid, " +
			"productid, " +
			"description, " +
			"serialnumber " +
			") values " +
			"(?, ?, ?, ?)";


	static final String UPDATE =
			"update asset set " +
			"description=?, " +
			"serialnumber=? " +
			"where assetid = ?";


	/**
	* Returns the Asset for the specified assetPK,
	* or null if there is none.
	*/
	public static Asset select(AssetPK assetPK)
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		Asset asset = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, assetPK.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				asset = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select asset by id failed", sqle,
				SELECT_BY_ID, assetPK);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return asset;
	}

	
	static private Asset createFromRS(ResultSet rs, int columnOffset)
			throws SQLException, DataAccessException {

		int i = columnOffset;

		AssetPK pk = new AssetPK(rs.getLong(i++));
		ProductPK productPK = new ProductPK(rs.getLong(i++));

		String description = rs.getString(i++);
		String serialNumber = rs.getString(i++);

		Asset asset = new Asset(pk, productPK, description, serialNumber);

		return asset;
	}


	/**
	* Delete the specified Asset.
	*/
	public static void delete(AssetPK assetPK)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(DELETE_BY_ID);
			ps.setLong(1, assetPK.getID());

			int count = ps.executeUpdate();

			if (count == 0) {
				throw new DataNotFoundException(
					"delete asset by id failed",
					DELETE_BY_ID, assetPK);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"delete asset by id failed", sqle,
				DELETE_BY_ID, assetPK);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}


	/**
	* Delete the Asset or Assets associated with the specified Product.
	*/
	public static void delete(ProductPK productPK)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(DELETE_BY_PRODUCT_ID);
			ps.setLong(1, productPK.getID());

			int count = ps.executeUpdate();

			if (count == 0) {
				throw new DataNotFoundException(
					"delete asset by product failed",
					DELETE_BY_PRODUCT_ID, productPK);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"delete asset by product failed", sqle,
				DELETE_BY_PRODUCT_ID, productPK);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}


	/**
	* Creates an Asset record from the specified Asset.
	*
	* Returns a Asset instance with a valid AssetPK.
	*
	* Throws IllegalStateException if the Asset already has
	* an AssetPK.
	*/
	public static Asset create(Asset asset)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (asset.getPK() != null) {
			throw new IllegalStateException("asset already has pk " +
					asset.getPK());
		}

		long key = BlindKey.getNextKey("ASSET_SEQ");
		asset.setPK(new AssetPK(key));

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(INSERT);

			int i = 1;

			ps.setLong(i++, asset.getPK().getID());
			ps.setLong(i++, asset.getProductPK().getID());
			if (asset.getDescription() != null) {
				ps.setString(i++, asset.getDescription());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}
			if (asset.getSerialNumber() != null) {
				ps.setString(i++, asset.getSerialNumber());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"create asset failed",
					INSERT, asset);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"create asset failed", sqle,
				INSERT, asset);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return asset;
	}



	/**
	* Updates the specified Asset record in the database.
	*
	* Throws IllegalStateException if the specified Asset
	* does not have a AssetPK.
	*/
	public static Asset update(Asset asset)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (asset.getPK() == null) {
			throw new IllegalStateException("asset has no pk " +
					asset.getPK());
		}

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(UPDATE);

			int i = 1;

			if (asset.getDescription() != null) {
				ps.setString(i++, asset.getDescription());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}

			if (asset.getSerialNumber() != null) {
				ps.setString(i++, asset.getSerialNumber());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}

			ps.setLong(i++, asset.getPK().getID());

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataNotFoundException(
					"update asset by id failed",
					UPDATE, asset);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"update asset failed", sqle,
				UPDATE, asset);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return asset;
	}
}
