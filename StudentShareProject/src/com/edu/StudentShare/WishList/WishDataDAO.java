package com.edu.StudentShare.WishList;

public interface WishDataDAO {

		void createTable();
		
		int createNewWish(WishData data);
		
		Boolean deleteWish(int userId, int wishId);
		
	}
