package com.globalegrow;

import java.util.Date;

public class PunchCard{
	String userId;
	String name;
	Date date;
	@Override
	public String toString() {
		return "PunchCard [userId=" + userId + ", name=" + name + ", date=" + date + "]";
	}
}