package rasporedjivac;

public class PcbData {
	private long taul;
	private long pocetak;
	private int age;
	private int priority;
	private long vreme;
	private long StartWaitTime;
	
	public PcbData() {
		taul=0;
		pocetak=0;
		age=0;
		priority=0;
		vreme=0;
		StartWaitTime=0;
	}
	
	public void setTaul(long t) {
		taul=t;
	}
	
	public long getTaul() {
		return taul;
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
