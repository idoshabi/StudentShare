package com.edu.StudentShare;

import java.io.IOException;
import java.util.logging.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import sun.util.logging.resources.logging;

public class Utils {
	private static Logger logger = null;
	private static FileHandler fh = null;

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
			if (path == null)
				System.out.println("Failed to open log file");
			fh = new FileHandler(path, 10000, 100, true);
			fh.setFormatter(new SimpleFormatter());
			logger = Logger.getLogger(AdapterLog.class.getName());
			logger.addHandler(fh);
			logger.setLevel(Level.CONFIG);

		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String log) {
		if (logger != null)
			logger.log(Level.ALL, log);
	}

	/**
	 * Dispose the logger
	 */
	public void dispose() {
		fh.close();
	}

	public void error(String log) {
		if (logger != null)
			logger.log(Level.WARNING, log);
	}

	public void info(String log) {
		if (logger != null)
			logger.log(Level.INFO, log);
	}

	public void fine(String log) {
		if (logger != null)
			logger.log(Level.FINE, log);
	}

	public void severe(String log) {
		if (logger != null)
			logger.log(Level.SEVERE, log);
	}

}
