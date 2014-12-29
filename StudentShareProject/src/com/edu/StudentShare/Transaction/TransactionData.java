package com.edu.StudentShare.Transaction;

import java.sql.Date;

public class TransactionData {

		private int sellerId;
		private int buyerId;
		private int productId;
		private int point_amount;
		private Date transactionTime;
		public int getSellerId() {
			return sellerId;
		}
		public void setSellerId(int sellerId) {
			this.sellerId = sellerId;
		}
		public int getBuyerId() {
			return buyerId;
		}
		public void setBuyerId(int buyerId) {
			this.buyerId = buyerId;
		}
		public int getProductId() {
			return productId;
		}
		public void setProductId(int productId) {
			this.productId = productId;
		}
		public int getPoint_amount() {
			return point_amount;
		}
		public void setPoint_amount(int point_amount) {
			this.point_amount = point_amount;
		}
		public Date getTransactionTime() {
			return transactionTime;
		}
		public void setTransactionTime(Date transactionTime) {
			this.transactionTime = transactionTime;
		}
}
