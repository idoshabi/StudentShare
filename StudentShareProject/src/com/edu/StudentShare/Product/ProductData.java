package com.edu.StudentShare.Product;

import java.sql.Date;

public class ProductData {
	private String _productName;
	private double price;
	private int _seller_id;
	private int _quntity;
	private Date postTime;
	private int _sold;
	private String description;
	private String imageUrl;

	public ProductData(String productName, double price, int seller_id , int quntity, Date postTime, int sold, String description,
			String image)
	{
	this._productName = productName;
	this.price = price;
	this._seller_id = seller_id;
	this._quntity = quntity;
	this.postTime = postTime;
	this._sold = sold;
	this.description = description;
	this.imageUrl = image;
	}
	public String get_productName() {
		return _productName;
	}
	public void set_productName(String _productName) {
		this._productName = _productName;
	}
	public int get_seller_id() {
		return _seller_id;
	}
	public void set_seller_id(int _seller_id) {
		this._seller_id = _seller_id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(short price) {
		this.price = price;
	}
	public int get_quntity() {
		return _quntity;
	}
	public void set_quntity(int _quntity) {
		this._quntity = _quntity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int get_sold() {
		return _sold;
	}
	public void set_sold(int _sold) {
		this._sold = _sold;
	}
	public Date getPostTime() {
		return postTime;
	}
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@Override
	public String toString() {
		String output = "productName: " + _productName +" price: "+ price
				+" seller_id: " + _seller_id +" quntity: " + _quntity+ " description: "+
				description;
				
				
		return output;
	}
}
