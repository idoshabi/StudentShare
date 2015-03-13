package com.edu.StudentShare.Redis;

import java.util.ArrayList;
import java.util.Set;

import com.edu.StudentShare.Product.ProductData;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import redis.clients.jedis.Jedis;

public class Products {
	private static String key= "Products";
	private static String setItemProductUnique(int productId) {
		String uniqueId = String.format("ProductsHash:%s", productId);
		return uniqueId;
	}

	public static void setProduct(int productId, ProductData data) {
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			// / ... do stuff here ... for example
			int soldCount = data.get_sold();
			String uniqueId = setItemProductUnique(productId);
			jedis.zadd(key, soldCount, uniqueId);
			jedis.hset(uniqueId, "Product_Name", data.get_productName());
			jedis.hset(uniqueId, "Product_seller",
					String.valueOf(data.get_seller_id()));
			jedis.hset(uniqueId, "Product_description", data.getDescription());
			jedis.hset(uniqueId, "Product_price",
					String.valueOf(data.getPrice()));
			
			jedis.hset(uniqueId, "Product_quntity", String.valueOf(data.get_quntity()));
			
			jedis.hset(uniqueId, "Profile_picture", data.getImageUrl());
			jedis.hset(uniqueId, "id",  String.valueOf(productId));

			jedis.set(getUniqueSet(productId), uniqueId);
		}
	}
	private static String getUniqueSet(int userId)
	{
		String path = String.format("Products%s", userId);
		return path;
	}
	
	// Get all products by range
	public static ArrayList<ProductData> getTopProducts(int max) {
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			ProductData data;
			ArrayList<ProductData> list = new ArrayList<ProductData>();
			// / ... do stuff here ... for example
			Set<String> zSet = jedis.zrangeByScore(key, "-inf", "+inf",0, max);
			for (String item : zSet) {
				list.add(getProductByScore(jedis, item));
			}

			return list;

		}
	}
	
	public static void increaseProductSold(int productId, int numToInc)
	{
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			jedis.zincrby(key, numToInc, getUniqueSet(productId));

		}
		
	}

	public static void removeProduct(int productId) {
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			jedis.zrem(key, String.valueOf(productId));

		}
	}

	public static ProductData getProductById(int productId) {
		ProductData data = null;
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			String score = jedis.get(getUniqueSet(productId));
			if (score != null) {
				data = getProductByScore(jedis, score);
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return data;
	}

	private static ProductData getProductByScore(Jedis jedis, String uniqueId) {
		ProductData data = null;
		try {
			String Product_Name = jedis.hget(uniqueId, "Product_Name");
			String Product_description = jedis.hget(uniqueId,
					"Product_description");
			String Product_seller = jedis.hget(uniqueId, "Product_seller");
			String Profile_picture = jedis.hget(uniqueId, "Profile_picture");
			String Product_price = jedis.hget(uniqueId, "Product_price");
			String Product_quntity = jedis.hget(uniqueId, "Product_quntity");
			data = new ProductData(); 

			try{
				int id = Integer.valueOf(jedis.hget(uniqueId, "id"));
				data.setId(id);				
			}
			catch(Exception e){}

			data.set_productName(Product_Name);
			data.set_seller_id(Integer.valueOf(Product_seller));
			data.setPrice(Double.valueOf(Product_price));
			data.setImageUrl(Profile_picture);
			data.setDescription(Product_description);
			data.set_quntity(Integer.valueOf(Product_quntity));

			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}
