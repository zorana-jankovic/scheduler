package com.etf.os2.project.scheduler;

import java.util.ArrayList;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.PcbData;

public class NoviMFQS extends Scheduler{
	private int flag;
	private int br;
	private int [] kvanti;
	private final int MAX=1000;//proveri
	
	private ArrayList<ArrayList<ArrayList<Pcb>>> redovi;
	
	
	public NoviMFQS(int cnt, int[]niz) {
		flag=0;
		br=cnt;
		kvanti=niz;
	}


	public Pcb get(int cpuId) {
		Pcb pom=null;

		if (flag==0) {
			
			redovi=new ArrayList<>(Pcb.RUNNING.length);
			for(int j=0;j<Pcb.RUNNING.length;j++) {
				redovi.add(new ArrayList<>());
				for(int l=0;l<br;l++) {
					redovi.get(j).add(new ArrayList<Pcb>());
				}
			}
			
			flag=1;
		}
		
		
		ArrayList<ArrayList<Pcb>> lista=redovi.get(cpuId);
		int cnt=0,i;
		
		for(i=0;i<br;i++) {
			if (lista.get(i).size()>0) {
				cnt=1;
				break;
			}
		}
		
		if (cnt==1) {
			return uzmi(cpuId);
		}
		
		
		lista=redovi.get(0);
		cnt=0;
		
		for(i=0;i<br;i++) 
			cnt=cnt+lista.get(i).size();
		
		int max=cnt,id=0;
		
		for(i=1;i<Pcb.RUNNING.length;i++) {
			lista=redovi.get(i);
			cnt=0;
			
			for(int j=0;j<br;j++) 
				cnt=cnt+lista.get(j).size();
			
			if (cnt>=max) {
				max=cnt;
				id=i;
			}
			
		}
	
		
		if (max>0) {
			pom=uzmi(id);
			pom.getPcbData().setCpuId(cpuId);
		}
		
		return pom;
		
	}

	
	public void put(Pcb pcb) {
		if (pcb==null)
			return;
		
		if (flag==0) {
			redovi=new ArrayList<>(Pcb.RUNNING.length);
			for(int j=0;j<Pcb.RUNNING.length;j++) {
				redovi.add(new ArrayList<>());
				for(int l=0;l<br;l++) {
					redovi.get(j).add(new ArrayList<Pcb>());
				}
			}
			
			flag=1;
		}
			
	
		
		
		if (pcb.getPcbData()==null)
			pcb.setPcbData(new PcbData());
		
		ArrayList<ArrayList<Pcb>> lista;
		
		if (pcb.getPcbData().getCpuId()==-1) {
			lista=redovi.get(0);
			int cnt=0,i;
			
			for(i=0;i<br;i++) 
				cnt=cnt+lista.get(i).size();
			
			int min=cnt,id=0;
			
			for(i=1;i<Pcb.RUNNING.length;i++) {
				lista=redovi.get(i);
				cnt=0;
				
				for(int j=0;j<br;j++) 
					cnt=cnt+lista.get(j).size();
				
				if (cnt<=min) {
					min=cnt;
					id=i;
				}
				
			}
			
			
			pcb.getPcbData().setCpuId(id);
			
			
			dodaj(id,pcb);
		}
		
		
		else {
			
			dodaj(pcb.getPcbData().getCpuId(),pcb);
		}
		
	}
	
	void dodaj(int id,Pcb pcb) {
		if (pcb==null)
			return;
		
		ArrayList<ArrayList<Pcb>> list=redovi.get(id);
		
		if (pcb.getPcbData()==null)
			pcb.setPcbData(new PcbData());
		
		if (Pcb.ProcessState.CREATED==pcb.getPreviousState()) {
			int pr=pcb.getPriority();
			
			if (pr<0)
				pr=0;
			if (pr>br-1)
				pr=br-1;
			
			list.get(pr).add(pcb);
			pcb.getPcbData().setPriority(pr);
		
			
		}
		
		if (Pcb.ProcessState.BLOCKED==pcb.getPreviousState()) {
			int pr=pcb.getPcbData().getPriority();
			
			if (pr-1<0)
				pr=0;
			else
				pr=pr-1;
			
			list.get(pr).add(pcb);
			pcb.getPcbData().setPriority(pr);
		}
		
		if ((Pcb.ProcessState.RUNNING==pcb.getPreviousState())||(Pcb.ProcessState.READY==pcb.getPreviousState())) {
			int pr=pcb.getPcbData().getPriority();
			
			if (pr+1>br-1)
				pr=br-1;
			else
				pr=pr+1;
			
			list.get(pr).add(pcb);
			pcb.getPcbData().setPriority(pr);
		}
		
		pcb.getPcbData().setVreme(Pcb.getCurrentTime());
	}
	
	Pcb uzmi(int id) {
		Pcb pcb=null;
		int n=0,f=0;
		ArrayList<ArrayList<Pcb>> list=redovi.get(id);
		Pcb pom;
		
		for(int j=0;j<br;j++) {
			if (list.get(j).size()>0) {
				pom=list.get(j).get(0);
				if (Pcb.getCurrentTime()-pom.getPcbData().getVreme()>MAX) {
					n=j;
					f=1;
					pcb=list.get(j).remove(0);
					break;
				}
			}
		}
		
		/*
		long time=Pcb.getCurrentTime();
		
		for(int j=br-1;j>1;j--) {
			if (list.get(j).size()>0) {
				for(int i=0;i<list.get(j).size();i++) {
					pom=list.get(j).get(i);
					if (time-pom.getPcbData().getVreme()>MAX) {
						list.get(j).remove(i);
						list.get(j-1).add(pom);
						pom.getPcbData().setVreme(time);
					}
					else
						break;
				}
			}
		}
		
		*/
		
		if (f==0) {
		
			for(int i=0;i<br;i++) {
				if (list.get(i).size()>0) {
					pcb=list.get(i).remove(0);
					n=i;
					break;
				}
			}
		}
		
		if (pcb!=null)
			pcb.setTimeslice(kvanti[n]);
		
		return pcb;
	}

}
