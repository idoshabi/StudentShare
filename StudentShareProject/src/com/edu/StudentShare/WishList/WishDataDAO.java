package com.edu.StudentShare.WishList;

public interface WishDataDAO {

		void createTable(String tableName);
		
		int createNewWish(WishData data);
		
		Boolean deleteNewWish(int wishId);

		
		
	}
