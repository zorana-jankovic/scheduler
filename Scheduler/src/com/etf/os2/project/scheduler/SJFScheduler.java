package com.etf.os2.project.scheduler;

import java.util.LinkedList;
import com.etf.os2.project.process.*;


public class SJFScheduler extends Scheduler {
	private double alfa;
	//private int kvant;
	private int flag;
	private int preempt;
	private LinkedList<Pcb> list = new LinkedList<Pcb>();
	public static boolean[] proc;
	private LinkedList<Pcb> aging = new LinkedList<Pcb>();
	private final static int MAX = 5;
	
	

	public SJFScheduler(double a, int f) {
		alfa = a;
		preempt = f;
		flag=0;
		//kvant = 0;
	}

	public Pcb get(int cpuId) {
		
		if (list.size()!=aging.size()) {
			System.out.println("razliciti");
		}
		
		if (flag==0) {
			proc = new boolean[Pcb.RUNNING.length];
			for (int k = 0; k < Pcb.RUNNING.length; k++)
				proc[k] = false;
			flag=1;
		}

		if (list.size() == 0) {
			proc[cpuId] = false;
			return null;
		}

		int br = aging.getFirst().getPcbData().getAge();
		br--;
		if (br <= 0) {
			list.remove(aging.getFirst());
			
			proc[cpuId]=true;
			aging.getFirst().getPcbData().setPocetak(Pcb.getCurrentTime());
			return aging.removeFirst();
		}
		aging.getFirst().getPcbData().setAge(br);

		//Pcb ret = list.getFirst();
		for (int n = 0; n < list.size(); n++) {
			if (list.getFirst()==aging.get(n)) {
				if(n+1<list.size()) {
					br=aging.get(n+1).getPcbData().getAge()+aging.get(n).getPcbData().getAge();
					aging.get(n+1).getPcbData().setAge(br);
				}
				aging.remove(n);
				break;
			}
		}
		proc[cpuId] = true;
		list.getFirst().getPcbData().setPocetak(Pcb.getCurrentTime());
		return list.removeFirst();
	}

	public void put(Pcb pcb) {
		if (pcb==null)
			return;
		
		if (flag==0) {
			proc = new boolean[Pcb.RUNNING.length];
			for (int k = 0; k < Pcb.RUNNING.length; k++)
				proc[k] = false;
			flag=1;
		} 
		
		int n = -1;
		
		if (pcb.getPcbData()==null)
			pcb.setPcbData(new PcbData());
		
		double taul;
		
		if (pcb.getPcbData().getPreotet()==1) {
			double pom=(pcb.getPcbData().getTaul()-pcb.getExecutionTime());
			if (pom>0)
				taul=pom;
			else
				taul=(alfa * pcb.getExecutionTime()) + ((1 - alfa) * pcb.getPcbData().getTaul());
			
			pcb.getPcbData().setPreotet(0);
		}
		
		else
			taul = (alfa * pcb.getExecutionTime()) + ((1 - alfa) * pcb.getPcbData().getTaul());
		
		
		long t = (long) taul;
		pcb.getPcbData().setTaul(t);
		pcb.setTimeslice(0);

		int age = MAX;

		for (int l = 0; l < aging.size(); l++) {
			age = age - aging.get(l).getPcbData().getAge();
		}
		
	
		pcb.getPcbData().setAge(age);
		aging.add(pcb);
		
		if (list.size()==0) {
			list.add(0,pcb);
			n=0;
		}
		else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getPcbData().getTaul() > taul) {
					list.add(i, pcb);
					n = i;
					break;
				}
			}
			
			if (n==-1)
				list.add(pcb);
		}
		
		if (list.size()!=aging.size()) {
			System.out.println("razliciti");
		}
		
		if ((preempt == 1) && (n == 0)) {

			pokusajPreuzimanje(pcb);
		}

	}

	public void pokusajPreuzimanje(Pcb pcb) {

		for (int j = 0; j < Pcb.RUNNING.length; j++) {

			if (proc[j] == false)
				continue;
			
			long pom=(Pcb.RUNNING[j].getPcbData().getTaul()- (Pcb.getCurrentTime() - Pcb.RUNNING[j].getPcbData().getPocetak()));
			if ((pom > pcb.getPcbData().getTaul())||(pom<0)) {
				Pcb.RUNNING[j].preempt();
				Pcb.RUNNING[j].getPcbData().setPreotet(1);
				break;
			}
		}
	}


}
