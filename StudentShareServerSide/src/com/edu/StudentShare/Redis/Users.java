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
 			jedis.hset(uniqueId, "User:Rank", String.valueOf((int)data.get_userRank()));
			jedis.hset(uniqueId, "User:score", String.valueOf((int)data.get_score()));
			jedis.hset(uniqueId, "User:id", String.valueOf(data.getId()));

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
	
	public static void updateRedisKey(int UserId, String field, short value){
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			jedis.hincrBy(setItemUserUnique(UserId), field, value)
			;}
		
	}public static void updateRedisKey(int UserId, String field, int value){
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			jedis.hincrBy(setItemUserUnique(UserId), field, value)
			;}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void updateRedisKey(int UserId, String field, double value){
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			jedis.hincrByFloat(setItemUserUnique(UserId), field, value)
			;}catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	public static void removeUser(int UserId) {
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			jedis.zrem(key, setItemUserUnique(UserId));

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static UserData getUserById(int UserId) {
		UserData data = null;
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			String score = jedis.get(setItemUserUnique(UserId));
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
	public static void increaseUserRank(int userId, double rankToAdd)
	{
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			jedis.zincrby(key, rankToAdd, setItemUserUnique(userId));
				updateRedisKey(userId, "User:Rank", (int)rankToAdd);
		}
		
	}
	
	private static UserData getUserByScore(Jedis jedis, String uniqueId) {
		UserData data = null;
		try {
			String User_Name = jedis.hget(uniqueId, "User:UserName");
			String User_email = jedis.hget(uniqueId,"User:User_email");
			String imgUrl = jedis.hget(uniqueId, "User:imgUrl");
			String rank = jedis.hget(uniqueId, "User:Rank");
			String points = jedis.hget(uniqueId, "User:score"); 
			int id = Integer.valueOf(jedis.hget(uniqueId, "User:id"));
			
			double x = jedis.zscore(key , uniqueId);
			data = new UserData(); 		
			data.set_userName(User_Name);
			data.set_email(User_email);
			data.set_imgUrl(imgUrl);
			data.set_userRank(Double.parseDouble(rank));
			data.set_score(Double.parseDouble(points));
			data.setId(id);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}
