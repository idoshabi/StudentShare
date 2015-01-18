package com.edu.StudentShare.Transaction;

public class Transac {
	public static TransactionDataJDBC transactionJdbc;
	static int transaction_id;
	
	public Transac() {
		init();
	}

	public void init() {
		transactionJdbc = new TransactionDataJDBC("Transaction");
		transactionJdbc.createTable();
	}
	
}
