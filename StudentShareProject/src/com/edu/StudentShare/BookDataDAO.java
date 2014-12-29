package com.edu.StudentShare;


import java.util.List;
import javax.sql.DataSource;


public interface  BookDataDAO {

		/** 
		 * This is a method for creating the database should be done only once.
		 */
	//	public void createDB();
		/** 
	    * This is the method to be used to initialize
	    * database resources ie. connection.
	    */
//	   public void setDataSource(DataSource ds);
	   /** 
	    * This is the method to be used to create
	    * a table for a specific location
	    */
	   public void createTable(String placeName);
	   /** 
	    * This is the method to be insert a row into the table
	    */
	   public void insert(String title,String author);
	   /** 
	    * This is the method to be used to list down
	    * all the records from the above table.
	    */
	   public List<BookData> listBookDatas();
	   /** 
	    * This is the method to be used to delete
	    * a record from the above table corresponding
	    * to specific params
	    */
	   public void delete(String title);
	   
	}

