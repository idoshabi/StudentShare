package com.edu.StudentShare.Message;

public interface MessageDataDAO {

	   /** 
	    * This is the method to be used to create
	    * a table for a specific location
	    */
	   public void createTable(String placeName);

	   public int newMesseage(MessageData data);
	   
	   public Boolean deleteMesseage(int messeageId);
	   
	   
}
