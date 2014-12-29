package com.edu.StudentShare.Transaction;
import java.sql.Connection;
import java.sql.ResultSet;

import com.mysql.jdbc.PreparedStatement;
public class TransactionDataJDBC implements TransactionDataDAO{
		String tableName = "Transaction";
		String usersTableName = "Users";
		Connection conn = null;
	@Override
	public void createTable(String TableName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int makeTransaction(TransactionData data) {
		try {
			
		String selectpointsFromBuyer = "Select ponints FROM " + usersTableName+ data
				+ "WHERE userID = " +data.getPoint_amount();
		java.sql.PreparedStatement stmnt = conn.prepareStatement(selectpointsFromBuyer);
		ResultSet result = stmnt.executeQuery(selectpointsFromBuyer);
		// if result set of user < amount 
//discount
}
		catch (Exception ex)
		{
			
		}
		return 0;
	}

	@Override
	public Boolean deleteTransaction(int transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

}
