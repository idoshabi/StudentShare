package com.edu.StudentShare.Product;

public interface ProductDataDAO {

	public void createTable(String tableName);
	
	public int createNewProduct(ProductData prod);
	
	public Boolean deleteProduct(int productId);
	
	public Boolean buyProduct(int sellerID, int product_id);
	
	public Boolean updateProduct(int productId, ProductData prod);
	
	
}
