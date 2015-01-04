package com.edu.StudentShare.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import com.edu.StudentShare.DBConn;
import com.edu.StudentShare.LogFilter;
import com.edu.StudentShare.Message.MessageData;

public class UserDataJDBC implements UserDataDAO {

	Connection conn = null;
	String tableName = null;

	public UserDataJDBC(String table_name) {
		conn = DBConn.getConnection();
		tableName = table_name;
	}

	public UserDataJDBC() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createTable() {
		Statement st = null;

		try {
			st = conn.createStatement();
			st.executeUpdate("create database if not exists my_db");
			st.executeUpdate("use my_db");
			String SQL = "CREATE TABLE IF NOT EXISTS "
					+ tableName
					+ "(id int NOT NULL AUTO_INCREMENT,`username` VARCHAR(100) NOT NULL,`password` VARCHAR(100) NOT NULL,`email` VARCHAR(100),`birth` DATE	,`first_Name`  VARCHAR(100) NOT NULL,`last_Name` VARCHAR(100) NOT NULL,`points` double DEFAULT 100, `Rank`  double NOT NULL DEFAULT 0,`image_url` VARCHAR(100) NOT NULL DEFAULT 'default image link',`description` VARCHAR(100) NOT NULL DEFAULT 'empty', primary key (id)) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin;";
			st = conn.createStatement();
			st.execute(SQL);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		System.out.println("Created the table=" + tableName);
		return;

	}

	@Override
	public int createNewUser(UserData user) {

		// this is for selecting db incase we do insert first
		// ((DriverManagerDataSource)dataSource).setUrl("jdbc:mysql://localhost:3306/TestData");
		Statement st = null;
		int id = 0;
		String SQL = "INSERT INTO "
				+ tableName
				+ " (`username`, `password`, `email`, `birth`, `first_Name`, `last_Name`)"
				+ " VALUES (?,?,?,?,?,?);";
		System.out.println("insert SQL=" + SQL);
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(SQL);
			preparedStatement.setString(1, user.get_userName());
			preparedStatement.setString(2, user.get_password());
			preparedStatement.setString(3, user.get_email());
			preparedStatement.setDate(4, (java.sql.Date) user.get_birthday());
			preparedStatement.setString(5, user.get_first_name());
			preparedStatement.setString(6, user.get_last_name());
			preparedStatement.executeUpdate();
			st = conn.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT LAST_INSERT_ID() as last_id;");
			while (rs.next()) {
				id = Integer.parseInt(rs.getString("last_id"));
			}
			System.out.println("Created Record");
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
			return 0;
		}
		return id;
	}

	public Boolean UpdateUserPoints(int userId, double amount) {
		int id = 0;
		try {
			String SQL = "UPDATE " + tableName + " SET points = points+ ? "
					+ " WHERE id = ?;";
			PreparedStatement pre = conn.prepareStatement(SQL);

			pre.setDouble(1, amount);
			pre.setInt(2, userId);

			pre.executeUpdate();

			return true;

		} catch (Exception ex) {
			LogFilter.log.error("Failed at createNewProduct with "
					+ ex.toString());

			return false;
		}

	}

	public double getNumberOfPoints(int userId) {
		double points = 0;

		try {
			String selectpointsFromBuyer = "Select points FROM " + tableName
					+ " WHERE id =" + userId;
			java.sql.PreparedStatement stmnt = conn
					.prepareStatement(selectpointsFromBuyer);
			ResultSet result = stmnt.executeQuery(selectpointsFromBuyer);
			while (result.next()) {
				points = result.getDouble("points");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return points;
	}

	@Override
	public void deleteUser(int userId) {
		try {
			String SQL = "DELETE from " + tableName + " WHERE id = ?";

			PreparedStatement pre = conn.prepareStatement(SQL);
			pre.setInt(1, userId);
			pre.executeUpdate();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	@Override
	public int connect(String user, String pwd) {
		int id = 0;
		try {

			String SQL = "SELECT id FROM " + tableName
					+ " WHERE username = ? AND password = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(SQL);
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, pwd);

			ResultSet rSet = preparedStatement.executeQuery();
			if (rSet.next()) {
				id = rSet.getInt("id");
			}

			if (id == 0) {
				return 0;
			}

		} catch (Exception ex) {
			System.out.println(ex);
			return 0;
		}

		return id;
	}

	@Override
	public Boolean changePassword(int userId, String oldPwd, String newPwd) {
		try {
			String GET_PASS_SQL = "SELECT password FROM " + tableName
					+ " WHERE id = ? ;";
			PreparedStatement preparedStatPassword = conn
					.prepareStatement(GET_PASS_SQL);
			preparedStatPassword.setInt(1, userId);
			// preparedStatPassword.setString(2, oldPwd);

			ResultSet rset = preparedStatPassword.executeQuery();

			String pwd = null;
			while (rset.next()) {
				pwd = rset.getString("password");
			}

			if (!pwd.equals(oldPwd)) {
				System.out.print(String
						.format("The user: %s Enterd wrong password %s",
								userId, oldPwd));
				return false;
			}
			// Return wrong password messeage

			// TODO Auto-generated method stub
			String SQL = "UPDATE " + tableName + " SET password = ? "
					+ " WHERE id = ?;";
			System.out.println("insert SQL=" + SQL);

			PreparedStatement preparedStatement = conn.prepareStatement(SQL);
			preparedStatement.setString(1, newPwd);
			preparedStatement.setInt(2, userId);
			System.out.println(preparedStatement.toString());

			preparedStatement.executeUpdate();

			System.out.println("Created Record");
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		return true;
	}

	public List<UserData> getCurrentBirthdayUsers(Date date) {
		try {
			String SQL = "SELECT id FROM" + tableName + "WHERE birth = ?";
			PreparedStatement ps = conn.prepareStatement(SQL);
			ps.setDate(1, date);
			List<UserData> list = new ArrayList<UserData>();
			list.add(null);
			return list;
		}

		catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	@Override
	public UserData showUserInfo(int userid) {
		try {
			UserData data = null;
			String SQL = "SELECT `username`, `password`, `email`, `birth`, `first_Name`, `last_Name`, `image_url`, `points`, `Rank`, `description` From "
					+ tableName + " WHERE id = ? ;";
			PreparedStatement stmnt = conn.prepareStatement(SQL);
			stmnt.setInt(1, userid);
			ResultSet rSet = stmnt.executeQuery();
			while (rSet.next()) {
				String username = rSet.getString("username");
				String email = rSet.getString("email");
				String first_name = rSet.getString("first_Name");
				String image_url = rSet.getString("image_url");
				String password = rSet.getString("password");
				double points = rSet.getDouble("points");
				Date date = rSet.getDate("birth");
				String last_name = rSet.getString("last_Name");
				double Rank = rSet.getDouble("Rank");
				String description = rSet.getString("description");
				
				data = new UserData(username, password, email, date, first_name,
						last_name, Rank, points, image_url);
			}
			return data;
		}

		catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;

	}

}
