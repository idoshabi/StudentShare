package com.edu.StudentShare.Transaction;

public interface TransactionDataDAO {
		
	void createTable();
	
	int makeTransaction(TransactionData data);
	
	Boolean deleteTransaction(int transactionId);
	
}
