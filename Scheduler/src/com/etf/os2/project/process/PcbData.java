package com.etf.os2.project.process;

public class PcbData {
	private long taul;
	private long pocetak;
	private int age;
	private int priority;
	private long vreme;
	private long StartWaitTime;
	private int preotet;
	private int cpuId;
	private int active;
	
	public PcbData() {
		taul=10;
		pocetak=0;
		age=0;
		priority=0;
		vreme=0;
		StartWaitTime=0;
		preotet=0;
		cpuId=-1;
		active=0;
	}
	
	
	public int getActive() {
		return active;
	}


	public void setActive(int active) {
		this.active = active;
	}


	public int getCpuId() {
		return cpuId;
	}

	public void setCpuId(int cpuId) {
		this.cpuId = cpuId;
	}

	public void setTaul(long t) {
		taul=t;
	}
	
	public long getTaul() {
		return taul;
	}
	

	public int getPreotet() {
		return preotet;
	}

	public void setPreotet(int preotet) {
		this.preotet = preotet;
	}

	public void setPocetak(long p) {
		pocetak=p;
	}
	
	public long getPocetak() {
		return pocetak;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public long getVreme() {
		return vreme;
	}

	public void setVreme(long vreme) {
		this.vreme = vreme;
	}

	public long getStartWaitTime() {
		return StartWaitTime;
	}

	public void setStartWaitTime(long waitTime) {
		this.StartWaitTime = waitTime;
	}
	
}
