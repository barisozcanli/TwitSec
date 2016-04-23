package com.peace.twitsec.data.session;

import com.peace.twitsec.data.mongo.model.User;

import java.util.Hashtable;

public final class TwitSecSession {

	private static Hashtable<String, User> sessionStorage = new Hashtable<String, User>();

	private static TwitSecSession instance = new TwitSecSession();

	private TwitSecSession(){
	}
	
	public static TwitSecSession getInstance() {
		return instance;
	}
	
	public boolean validateToken(String token){
		if(token == null){
			return false;
		}
		return sessionStorage.get(token) != null;
	}
	
	public void addToken(String token, User user){
		sessionStorage.put(token, user);
	}
	
	public void removeToken(String token){
		sessionStorage.remove(token);
	}
	
	public User getUser(String token){
		return sessionStorage.get(token);
	}
}
