package com.etf.os2.project.scheduler;

import java.util.ArrayList;
import java.util.LinkedList;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.PcbData;

public class NoviSJF extends Scheduler{
	private double alfa;
	//private int kvant;
	private int flag;
	private int preempt;
	//private LinkedList<Pcb> list = new LinkedList<Pcb>();
	public static boolean[] proc;
	private ArrayList<ArrayList<Pcb>> aging=new ArrayList<>();
	private final static int MAX =100;
	private int znak;
	private ArrayList<ArrayList<Pcb>> procesori=new ArrayList<>();
	
	

	public NoviSJF(double a, int f) {
		alfa = a;
		preempt = f;
		flag=0;
		znak=0;
		//kvant = 0;
	}

	public Pcb uzmi(int cpuId,int id) {
		
		ArrayList<Pcb> list=procesori.get(cpuId);
		ArrayList<Pcb> age=aging.get(cpuId);
		
		if (list.size()!=age.size()) {
			System.out.println("razliciti");
		}
		
		if (flag==0) {
			proc = new boolean[Pcb.RUNNING.length];
			for (int k = 0; k < Pcb.RUNNING.length; k++)
				proc[k] = false;
			flag=1;
		}

		if (list.size() == 0) {
			proc[id] = false;
			return null;
		}

		int br = age.get(0).getPcbData().getAge();
		br--;
		if (br <= 0) {
			list.remove(age.get(0));
			
			proc[id]=true;
			age.get(0).getPcbData().setPocetak(Pcb.getCurrentTime());
			return age.remove(0);
		}
		age.get(0).getPcbData().setAge(br);

		
		for (int n = 0; n < list.size(); n++) {
			if (list.get(0)==age.get(n)) {
				if(n+1<list.size()) {
					br=age.get(n+1).getPcbData().getAge()+age.get(n).getPcbData().getAge();
					age.get(n+1).getPcbData().setAge(br);
				}
				age.remove(n);
				break;
			}
		}
		proc[id] = true;
		list.get(0).getPcbData().setPocetak(Pcb.getCurrentTime());
		return list.remove(0);
	}

	public void stavi(int id,Pcb pcb) {
		if (pcb==null)
			return;
		
		ArrayList<Pcb> list=procesori.get(id);
		ArrayList<Pcb> age=aging.get(id);
		
		
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

		int vreme = MAX;

		for (int l = 0; l < age.size(); l++) {
			vreme = vreme - age.get(l).getPcbData().getAge();
		}

		pcb.getPcbData().setAge(vreme);
		age.add(pcb);
		
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
		
		if (list.size()!=age.size()) {
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
			
			if (Pcb.RUNNING[j].getPcbData()==null) {
				int djd;
				djd=j+3;
			}
			
			long pom=(Pcb.RUNNING[j].getPcbData().getTaul()- (Pcb.getCurrentTime() - Pcb.RUNNING[j].getPcbData().getPocetak()));
			if ((pom > pcb.getPcbData().getTaul())||(pom<0)) {
				Pcb.RUNNING[j].preempt();
				Pcb.RUNNING[j].getPcbData().setPreotet(1);
				break;
			}
		}
	}

	
	public Pcb get(int cpuId) {
		Pcb pom=null;

		if (znak==0) {
			for(int i=0;i<Pcb.RUNNING.length;i++) {
				procesori.add(i,new ArrayList<>());
				aging.add(i,new ArrayList<>());
			}
			
			znak=1;
		}
		
		if (procesori.get(cpuId).size()>0) {
			//return procesori.get(cpuId).remove(0);
			return uzmi(cpuId,cpuId);
		}
		
		int max=procesori.get(0).size();
		int id=0,j=0;
		
		for(j=0;j<procesori.size();j++) {
			if (procesori.get(j).size()>=max) {
				max=procesori.get(j).size();
				id=j;
			}
		}
		
		if (max>0) {
			//pom=procesori.get(id).remove(0);
			pom=uzmi(id,cpuId);
			pom.getPcbData().setCpuId(cpuId);
		}
		
		else
			proc[cpuId]=false;
		return pom;
	}
	
	public void put(Pcb pcb) {
		if (pcb==null)
			return;
		
		if (znak==0) {
			for(int i=0;i<Pcb.RUNNING.length;i++) {
				procesori.add(i,new ArrayList<>());
				aging.add(i,new ArrayList<>());
			}
			
			znak=1;
		}
		
		if (pcb.getPcbData()==null)
			pcb.setPcbData(new PcbData());
		
		if (pcb.getPcbData().getCpuId()==-1) {
			int min=procesori.get(0).size();
			int id=0,j=0;
			
			for(j=0;j<procesori.size();j++) {
				if (procesori.get(j).size()<min) {
					min=procesori.get(j).size();
					id=j;
				}
			}	
			
			pcb.getPcbData().setCpuId(id);
			//procesori.get(id).add(pcb);	
			
			stavi(id,pcb);
		}
		
		
		else {
			//procesori.get(pcb.getPcbData().getCpuId()).add(pcb);
			stavi(pcb.getPcbData().getCpuId(),pcb);
		}
	}
}
