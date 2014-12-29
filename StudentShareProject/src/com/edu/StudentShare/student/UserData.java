package com.edu.StudentShare.student;

import java.util.Date;

public class UserData {
private String _userName;
private String _password;
private String _email;
private Date _birthday;
private String _first_name;
private String _last_name;
private short _userRank;

public UserData(String user, String password, String email, Date birthday, String first_name, String last_name)
{
	_userName = user;
	_email = email;
	_password = password;
	_birthday = birthday;
	_first_name = first_name;
	_last_name = last_name;
}

public String get_userName() {
	return _userName;
}
public void set_userName(String _userName) {
	this._userName = _userName;
}
Date get_birthday() {
	return _birthday;
}
void set_birthday(Date _birthday) {
	this._birthday = _birthday;
}
short get_userRank() {
	return _userRank;
}
void set_userRank(short _userRank) {
	this._userRank = _userRank;
}
String get_email() {
	return _email;
}
void set_email(String _email) {
	this._email = _email;
}
String get_password() {
	return _password;
}
void set_password(String _password) {
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

}
