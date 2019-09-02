package com.globalegrow;

public class PunchCardDay{
	String userId;
	String name;
	String date;
	/**
	 * 当日上班时间
	 */
	String beWork;
	/**
	 * 当日下班时间
	 */
	String offWork;
	/**
	 * 9-18点工作制，当日迟到和早退时间
	 */
	int layTime;
	/**
	 * 9-18点工作制，当日早到和晚走时间
	 */
	int workTime;
	/**
	 * 9:30-18:30点工作制，当日迟到和早退时间
	 */
	int layTime1;
	/**
	 * 9:30-18:30点工作制，当日早到和晚走时间
	 */
	int workTime1;
	/**
	 * 当日未打卡次数 
	 */
	int noPCard;
	
	/**
	 * 是否是工作日
	 */
	boolean work;
	
	public PunchCardDay(String userId, String name, String date, String beWork, String offWork, int layTime,
			int workTime, int layTime1, int workTime1, int noPCard, boolean work) {
		super();
		this.userId = userId;
		this.name = name;
		this.date = date;
		this.beWork = beWork;
		this.offWork = offWork;
		this.layTime = layTime;
		this.workTime = workTime;
		this.layTime1 = layTime1;
		this.workTime1 = workTime1;
		this.noPCard = noPCard;
		this.work = work;
	}

	public PunchCardDay() {
		super();
	}

	@Override
	public String toString() {
		return "PunchCardDay [userId=" + userId + ", name=" + name + ", date=" + date + ", beWork=" + beWork
				+ ", offWork=" + offWork + "]";
	}
}