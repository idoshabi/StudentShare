package com.edu.StudentShare.WishList;

import java.sql.Date;

public class WishData {
	
	@Override
	public String toString() {
		return "WishData [userId=" + userId + ", productId=" + productId
				+ ", dataTime=" + dataTime + "]";
	}
	public WishData(int userId, int productId, Date dataTime) {
		super();
		this.userId = userId;
		this.productId = productId;
		this.dataTime = dataTime;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public Date getDataTime() {
		return dataTime;
	}
	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}
	int userId;
	int productId;
	Date dataTime;

	
}
