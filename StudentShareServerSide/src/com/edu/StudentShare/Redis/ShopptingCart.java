package com.edu.StudentShare.Redis;

import java.util.ArrayList;
import java.util.List;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;

import com.edu.StudentShare.Product.Prod;
import com.edu.StudentShare.Product.ProductData;

import redis.clients.jedis.Jedis;

public class ShopptingCart {

	private static String getUniqueSet(int userId) {
		String path = String.format("ShopptingCart%s", userId);
		return path;
	}

	static public int addItemToCart(int userId, int productId) {
		if (Prod.productJdbc.checkIfProductExist(productId))
		{
			try (Jedis jedis = ConnectionPool.pool.getResource()) {
				jedis.rpush(getUniqueSet(userId), String.valueOf(productId));
			}
			return productId;
		}
		return 0;

	}

	public static List<String> CheckoutPurchase(int userId) {
		List<String> list = null;

		try (Jedis jedis = ConnectionPool.pool.getResource()) {

			list = jedis.lrange(getUniqueSet(userId), 0, -1);
			for (String product_id : list) {
				int id = Prod.productJdbc.buyProduct(userId,
						Integer.parseInt(product_id));
				if (id == -1)
				{
					return null;
				}
			}
			deleteCartForUser(userId);
		}

		return list;
	}

	// Return list of product ids as string for ids
	static public List<ProductData> getItemFromCart(int userId) {
		List<String> list = null;
		List<ProductData> prodList = null;

		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			prodList = new ArrayList<ProductData>();

			list = jedis.lrange(getUniqueSet(userId), 0, -1);
			for (String item : list) {
				ProductData data = Products.getProductById(Integer.valueOf(item));
				if (data != null)
					prodList.add(data);

			}
		}

		return prodList;

	}

	static public Boolean deleteItemFromCart(int userId, int productId) {
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			long result = jedis.lrem(getUniqueSet(userId), 0, String.valueOf(productId));
			if (result== 0)
				return false;
			return true;
		}
	}

	static public void deleteCartForUser(int userId) {
		try (Jedis jedis = ConnectionPool.pool.getResource()) {
			jedis.del(getUniqueSet(userId));
		}
	}

}
