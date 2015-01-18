package com.edu.StudentShare;

import java.io.IOException;
import java.util.logging.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import sun.util.logging.resources.logging;

public class Utils {
	private static Logger logger = null;
	private static FileHandler fh = null;
	private static Gson json = null;

	public static java.sql.Date TosqlDate(java.util.Date utilDate) {
		java.sql.Date sqlDate = null;
		try {
			sqlDate = new java.sql.Date(utilDate.getTime());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sqlDate;

	}
	// ConvertToJson
	public static String toJson(Object obj) {
		if (Utils.json == null) {
			Utils.json = new Gson();
		}
		String jsonString = json.toJson(obj);
		
		return jsonString;
	}
	public static int retiveUserId(HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");

		if (userId != null) {

			int id = (int) userId;
			return id;

		} else {
			return 0;
		}
	}
	public Utils(String path) {
		try {
			if (path == null){
				System.out.println("Failed to open log file");
			}
			if ( logger !=null ){
				return;
			}
			fh = new FileHandler(path, 10000, 100, true);
			fh.setFormatter(new SimpleFormatter());
			logger = Logger.getLogger(AdapterLog.class.getName());
			logger.addHandler(fh);
			logger.setLevel(Level.CONFIG);

		} catch (SecurityException  e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void write(String log) {
		if (logger != null){
			System.out.println(log);

			logger.log(Level.ALL, log);
		}
	}

	/**
	 * Dispose the logger
	 */
	public void dispose() {
		fh.close();
	}

	public void error(String log) {
		if (logger != null){
			System.out.println(log);
			logger.log(Level.WARNING, log);
		}
	}

	public void info(String log) {
		if (logger != null){
			System.out.println(log);

			logger.log(Level.INFO, log);
		}
	}

	public void fine(String log) {
		if (logger != null){
			System.out.println(log);

			logger.log(Level.FINE, log);
		}
	}

	public void severe(String log) {
		if (logger != null){
			System.out.println(log);

			logger.log(Level.SEVERE, log);
		}
	}

}
