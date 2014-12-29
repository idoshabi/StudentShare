package com.edu.StudentShare;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DBadmin
 */
public class DBadmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
BookDataJDBCTemplate bookDataJDBCTemplate = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DBadmin() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init()  throws ServletException
    {
    	 bookDataJDBCTemplate = new BookDataJDBCTemplate();
    	 System.out.println("bookDataJDBCTemplate="+bookDataJDBCTemplate);
    	bookDataJDBCTemplate.createTable("books");
    	System.out.println("table created");
    	bookDataJDBCTemplate.insert("War and Peace", "Tolstoy");
    	System.out.println("insert one row into the table");
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("IN doGet");
		response.setContentType("text/plain");
		PrintWriter pw = response.getWriter();
		List<BookData> booklist =  bookDataJDBCTemplate.listBookDatas();
		for (Iterator<BookData> iterator = booklist.iterator(); iterator.hasNext();) 
		{
			BookData bookData = (BookData) iterator.next();
			pw.println("Title="+bookData.getTitle()+"  Author="+bookData.getAuthor());
		}		
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String title = request.getParameter("title");
		String author= request.getParameter("author");
		System.out.println("title="+title+",author="+author);
		if (title !=null && author !=null)
			bookDataJDBCTemplate.insert(title, author);
				
	}

}
