package com.edu.StudentShare.Message;

import java.sql.Date;

public class MessageData {

		public MessageData(String title, String contant, int senderId,
			int reciverId, Date dataTime, int id) {
		super();
		this.title = title;
		this.contant = contant;
		this.senderId = senderId;
		this.reciverId = reciverId;
		this.dataTime = dataTime;
		this.id = id;
	}
		
		public MessageData(String title, String contant, int senderId,
				int reciverId, Date dataTime) {
			super();
			this.title = title;
			this.contant = contant;
			this.senderId = senderId;
			this.reciverId = reciverId;
			this.dataTime = dataTime;
		}
		@Override
		public String toString() {
			return "MessageData [title=" + title + ", contant=" + contant
					+ ", senderId=" + senderId + ", reciverId=" + reciverId
					+ ", id=" + id + ", dataTime=" + dataTime + "]";
		}


		private String title;
		private String contant;
		private int senderId;
		private int reciverId;
		private int id;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
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
