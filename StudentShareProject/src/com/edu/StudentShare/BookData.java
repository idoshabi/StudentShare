package com.edu.StudentShare;

import java.util.Date;

public class BookData {

	String title;
	String author;
	
	public BookData(){}
	public BookData(String title,String author)
	{
		this.title = title;
		this.author= author;
		
	}
	public void setTitle(String name)
	{
		title =name;
	}
	public void setAuthor(String a)
	{
		author =a;
	}
	public String getTitle()
	{
		return title;
	}
	public String getAuthor()
	{
		return author;
	}	
}
