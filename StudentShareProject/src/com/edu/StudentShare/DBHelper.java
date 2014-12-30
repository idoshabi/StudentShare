package com.edu.StudentShare;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class DBHelper {
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

}
