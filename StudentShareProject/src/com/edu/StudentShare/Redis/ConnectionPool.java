package com.edu.StudentShare.Redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class ConnectionPool {
	public static JedisPool pool;

public ConnectionPool() {
	pool = new JedisPool(new JedisPoolConfig(), "localhost");
	
	}


}
