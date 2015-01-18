package com.edu.StudentShare.Redis;

import java.util.Set;

import redis.clients.jedis.Jedis;

public class VisitorsCounter {

	private static String uniqueLog = "uniqueLog";

	public static void NewVisit(String ip)
	{
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
		    long status = jedis.pfadd("foo", "a");

			  //jedis.pfcount(uniqueLog, ip);
			 
			}
	}
	
	public static long GetNumberOfView()
	
	{
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			  long number = jedis.pfcount(uniqueLog);
			  return number;
			}
	}
	

}
