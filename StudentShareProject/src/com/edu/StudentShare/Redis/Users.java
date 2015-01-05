package com.edu.StudentShare.Redis;

import java.util.ArrayList;
import java.util.Set;

import com.edu.StudentShare.User.User;
import com.edu.StudentShare.User.UserData;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import redis.clients.jedis.Jedis;

public class Users {
	private static String key= "Users";
	private static String setItemUserUnique(int UserId) {
		String uniqueId = String.format("UsersHash:%s", UserId);
		return uniqueId;
	}

	public static void setUser(int UserId, UserData data) {
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			// / ... do stuff here ... for example
			double rank = data.get_userRank();
			String uniqueId = setItemUserUnique(UserId);
			jedis.zadd(key, rank, uniqueId);
			jedis.hset(uniqueId, "User:UserName", data.get_userName());
			jedis.hset(uniqueId, "User:User_email",String.valueOf(data.get_email()));
			jedis.hset(uniqueId, "User:imgUrl", data.get_imgUrl());
			
			jedis.set(getUniqueSet(UserId), uniqueId);
			
			OnlineUsers.addUser(UserId);
		}
	}
	private static String getUniqueSet(int userId)
	{
		String path = String.format("Users%s", userId);
		return path;
	}
	
	// Get all Users by range
	public static ArrayList<UserData> getTopUsers(int max) {
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			UserData data;
			ArrayList<UserData> list = new ArrayList<UserData>();
			// / ... do stuff here ... for example
			Set<String> zSet = jedis.zrangeByScore(key, "-inf", "+inf",0, max);
			for (String item : zSet) {
				list.add(getUserByScore(jedis, item));
			}

			return list;

		}
	}
	public static void removeUser(int UserId) {
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			jedis.zrem(key, getUniqueSet(UserId));

		}
	}

	public static UserData getUserById(int UserId) {
		UserData data = null;
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			String score = jedis.get(getUniqueSet(UserId));
			if (score != null) {
				data = getUserByScore(jedis, score);
			}
			else
			{
				data = User.userJdbc.showUserInfo(UserId);
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return data;
	}
	public static void increaseUserRank(int userId, int rankToAdd)
	{
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			jedis.zincrby(key, rankToAdd, getUniqueSet(userId));

		}
		
	}
	
	private static UserData getUserByScore(Jedis jedis, String uniqueId) {
		UserData data = null;
		try {
			String User_Name = jedis.hget(uniqueId, "User:UserName");
			String User_email = jedis.hget(uniqueId,"User:User_email");
			String imgUrl = jedis.hget(uniqueId, "User:imgUrl");
			
			data = new UserData(); 		
			data.set_userName(User_Name);
			data.set_email(User_email);
			data.set_imgUrl(imgUrl);
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}
