package com.edu.StudentShare.Transaction;

public interface TransactionDataDAO {
		
	void createTable(String TableName);
	
	int makeTransaction(TransactionData data);
	
	Boolean deleteTransaction(int transactionId);
	
}
