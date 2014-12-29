package com.edu.StudentShare.Message;

import java.sql.Date;

public class MessageData {

		private String title;
		private String contant;
		private int senderId;
		private int reciverId;
		private Date dataTime;
		public Date getDataTime() {
			return dataTime;
		}
		public void setDataTime(Date dataTime) {
			this.dataTime = dataTime;
		}
		public int getReciverId() {
			return reciverId;
		}
		public void setReciverId(int reciverId) {
			this.reciverId = reciverId;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContant() {
			return contant;
		}
		public void setContant(String contant) {
			this.contant = contant;
		}
		public int getSenderId() {
			return senderId;
		}
		public void setSenderId(int senderId) {
			this.senderId = senderId;
		}

}
