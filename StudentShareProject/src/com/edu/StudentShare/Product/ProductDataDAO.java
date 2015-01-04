package com.edu.StudentShare.Product;

import java.util.List;

public interface ProductDataDAO {

	public void createTable();
	
	public int createNewProduct(ProductData prod);
	
	public Boolean deleteProduct(int userid, int productId);
	
	public int buyProduct(int buyerId, int product_id);
	
	public ProductData getProductInfo(int id);
	
	public List<ProductData> getProductsByUser(int user_id);
	
	public Boolean updateProduct(int productId, ProductData prod);
	
	public List<String> getSellers();
}
