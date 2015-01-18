package com.edu.StudentShare.User;

import java.util.Date;

public class UserData {
@Override
	public String toString() {
		return "UserData [_userName=" + _userName + ", _password=" + _password
				+ ", _email=" + _email + ", _birthday=" + _birthday
				+ ", _first_name=" + _first_name + ", _last_name=" + _last_name
				+ ", _userRank=" + _userRank + ", _score=" + _score + "]";
	}

public UserData(String _userName, String _password, String _email,
			Date _birthday, String _first_name, String _last_name,
			double _userRank, double _score, String url) {
		this._userName = _userName;
		this._password = _password;
		this._email = _email;
		this._birthday = _birthday;
		this._first_name = _first_name;
		this._last_name = _last_name;
		this._userRank = _userRank;
		this._score = _score;
		this._imgUrl = url;
	}

public UserData() {
	// TODO Auto-generated constructor stub
}

private String _userName;
private String _password;
private String _email;
private Date _birthday;
private String _first_name;
private String _last_name;
private double _userRank;
private double _score;
private String _imgUrl;

public double get_score() {
	return _score;
}

public void set_score(double _score) {
	this._score = _score;
}


public String get_userName() {
	return _userName;
}
public void set_userName(String _userName) {
	this._userName = _userName;
}
public Date get_birthday() {
	return _birthday;
}
public void set_birthday(Date _birthday) {
	this._birthday = _birthday;
}
public double get_userRank() {
	return _userRank;
}
public void set_userRank(short _userRank) {
	this._userRank = _userRank;
}
public String get_email() {
	return _email;
}
public void set_email(String _email) {
	this._email = _email;
}
public String get_password() {
	return _password;
}
public void set_password(String _password) {
	this._password = _password;
}

public String get_first_name() {
	return _first_name;
}

public void set_first_name(String _first_name) {
	this._first_name = _first_name;
}

public String get_last_name() {
	return _last_name;
}

public void set_last_name(String _last_name) {
	this._last_name = _last_name;
}

public String get_imgUrl() {
	return _imgUrl;
}

public void set_imgUrl(String _url) {
	this._imgUrl = _url;
}




				
	
}
