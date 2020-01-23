package com.etf.os2.project.scheduler;

import java.util.ArrayList;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.PcbData;

public class ActiveExpire extends Scheduler{
	private ArrayList<ArrayList<Pcb>> active=new ArrayList<>();
	private ArrayList<ArrayList<Pcb>> expire=new ArrayList<>();
	private int flag;
	
	public ActiveExpire() {
		flag=0;
	}
	
	
	
	public Pcb uzmi(int cpuId) {
		
		ArrayList<Pcb> act=active.get(cpuId);
		ArrayList<Pcb> exp=expire.get(cpuId);
		
		if (act.size()<=0) {
			ArrayList<Pcb> pom=act;
			act=exp;
			exp=pom;
		}
		
		if (active.size()>0)
			return act.remove(0);
		return null;
	}

	
	public void dodaj(int id,Pcb pcb) {
		if (pcb==null)
			return;
		
		ArrayList<Pcb> act=active.get(id);
		ArrayList<Pcb> exp=expire.get(id);
		
		if (pcb.getPcbData()==null)
			pcb.setPcbData(new PcbData());
		
		if (pcb.getPcbData().getActive()==0) {
			act.add(pcb);
			pcb.getPcbData().setActive(1);
		}
		else {
			exp.add(pcb);
		}
		
	}



	
	public Pcb get(int cpuId) {
		Pcb pom=null;

		if (flag==0) {
			for(int i=0;i<Pcb.RUNNING.length;i++) {
				active.add(i,new ArrayList<>());
				expire.add(i,new ArrayList<>());
			}
			
			flag=1;
		}
		
		if (active.get(cpuId).size()+expire.get(cpuId).size()>0) {
			pom=uzmi(cpuId);
			if (pom!=null)
				pom.setTimeslice(10);//ROUND-ROBIN
			return pom;
		}
		
		int max=active.get(0).size()+expire.get(0).size();
		int id=0,j=0;
		
		for(j=0;j<Pcb.RUNNING.length;j++) {
			if (active.get(j).size()+expire.get(j).size()>=max) {
				max=active.get(j).size()+expire.get(j).size();
				id=j;
			}
		}	
		
		if (max>0) {
			pom=uzmi(id);
			pom.getPcbData().setCpuId(cpuId);
		}
		
		if (pom!=null)
			pom.setTimeslice(10);//ROUND-ROBIN
		return pom;
		
	}



	
	public void put(Pcb pcb) {
		if (pcb==null)
			return;
		
		System.out.println(pcb.getExecutionTime());
		
		if (flag==0) {
			for(int i=0;i<Pcb.RUNNING.length;i++) {
				active.add(i,new ArrayList<>());
				expire.add(i,new ArrayList<>());
			}
			
			flag=1;
		}
		
		if (pcb.getPcbData()==null)
			pcb.setPcbData(new PcbData());
		
		if (pcb.getPcbData().getCpuId()==-1) {
			int min=active.get(0).size()+expire.get(0).size();
			int id=0,j=0;
			
			
			for(j=0;j<Pcb.RUNNING.length;j++) {
				if (active.get(j).size()+expire.get(j).size()<min) {
					min=active.get(j).size()+expire.get(j).size();
					id=j;
				}
			}	
			
			pcb.getPcbData().setCpuId(id);
			
			
			dodaj(id,pcb);
		}
		
		
		else {
			
			dodaj(pcb.getPcbData().getCpuId(),pcb);
		}
	}
		
	

}
