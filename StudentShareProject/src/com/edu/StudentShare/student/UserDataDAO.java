package com.edu.StudentShare.student;

import java.sql.Date;
import java.util.List;

public interface UserDataDAO {

	public void createTable();
	
	public int createNewUser(UserData user);
	
	public void deleteUser(int userId);
	
	public int connect(String user, String pwd);
		
	public Boolean changePassword(int userId, String oldPwd, String newPwd);
	
	public List<UserData> getCurrentBirthdayUsers(Date date);
	
}
