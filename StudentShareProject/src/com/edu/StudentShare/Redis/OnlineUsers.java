package com.edu.StudentShare.Redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.edu.StudentShare.User.UserData;

import redis.clients.jedis.Jedis;

public class OnlineUsers {
	static String key = "OnlineUsers";

	private static String getUniqeUsers(int userId) {
		String path = String.format("OnlineUsers%s", userId);

		return path;
	}

	public static void addUser(int userId) {
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			// Calc the time from epoh time to peak the lastes
			long epoch = getCurrentEpohTime();

			jedis.zadd(key, epoch, getUniqeUsers(userId));
		}
	}

	private static long getCurrentEpohTime() {
		long epoch = System.currentTimeMillis() / 1000;
		return epoch;
	}

	public static ArrayList<UserData> getOnlineUserByTime(int max, int intervalTime) {
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			ArrayList<UserData> list = new ArrayList<UserData>();
			long startTime = getCurrentEpohTime() - 60*intervalTime;
			long endTime = getCurrentEpohTime();
			Set<String> zSet = jedis.zrangeByScore(key, startTime, endTime, 0, max);
			for (String item : zSet) {
				item = item.replaceAll("\\D+","");
				list.add(Users.getUserById(Integer.valueOf(item)));
			}

			return list;
		}
	}

}
