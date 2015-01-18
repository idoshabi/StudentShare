package com.edu.StudentShare.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.edu.StudentShare.DBConn;
import com.edu.StudentShare.User.User;

public class TransactionDataJDBC implements TransactionDataDAO {

	public TransactionDataJDBC(String tableName) {

		conn = DBConn.getConnection();
		_tableName = tableName;
	}

	String _tableName;
	Connection conn = null;

	@Override
	public void createTable() {
		Statement st = null;

		try {
			st = conn.createStatement();
			st.executeUpdate("create database if not exists my_db");
			st.executeUpdate("use my_db");
			String SQL = "CREATE TABLE IF NOT EXISTS "
					+ _tableName
					+ "(id int NOT NULL AUTO_INCREMENT,`buyer` int NOT NULL,`amount` double NOT NULL, `saler` int NOT NULL,`productId` int NOT NULL,`date` DATE NOT NULL, primary key (id)) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin;";
			st = conn.createStatement();
			st.execute(SQL);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		System.out.println("Created the table=" + _tableName);
		return;

	}

	// Return transaction id
	public int makeTransaction(TransactionData data) {
		try {
			double transactionPoints = data.getPoint_amount();
			double buyerPointUser = User.userJdbc.getNumberOfPoints(data
					.getBuyerId());
			if (buyerPointUser < transactionPoints) {
				System.out
						.println("Failed at makeTransaction,  user dont have enght points");
				return -1;
			}
			
			
			
			

			// Decrease the buyer points first in order to catch any fraud
			// issues
			User.userJdbc.UpdateUserPoints(data.getBuyerId(),
					-1 * data.getPoint_amount());
			User.userJdbc.UpdateUserPoints(data.getSellerId(),
					data.getPoint_amount());
			int id = buildSQLString(data);
			
			return id;

		}

		catch (Exception ex) {

		}
		return 0;
	}

	private int buildSQLString(TransactionData data) {
		int id = 0;
		try {
			String SQL = "INSERT INTO " + _tableName
					+ " (`buyer`, `saler`, `productId`, `date`, `amount`)"
					+ " VALUES (?,?,?,?,?);";
			Statement st = null;

			PreparedStatement preparedStatement = conn.prepareStatement(SQL);
			preparedStatement.setInt(1, data.getBuyerId());
			preparedStatement.setInt(2, data.getSellerId());
			preparedStatement.setInt(3, data.getProductId());
			preparedStatement.setDate(4, data.getTransactionTime());
			preparedStatement.setDouble(5, data.getPoint_amount());
			preparedStatement.executeUpdate();
			st = conn.createStatement();

			ResultSet rs = st
					.executeQuery("SELECT LAST_INSERT_ID() as last_id;");
			while (rs.next()) {
				id = Integer.parseInt(rs.getString("last_id"));
			}
			System.out.println("Created Record");
		} catch (SQLException e) {

			System.out.println(e);

			return 0;
		}

		return id;

	}

	@Override
	public Boolean deleteTransaction(int transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

}
