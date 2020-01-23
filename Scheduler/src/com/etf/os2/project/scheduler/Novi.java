package com.etf.os2.project.scheduler;

import java.util.ArrayList;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.PcbData;

public class Novi extends Scheduler{
	//private ArrayList<Pcb> lista=new ArrayList<>();
	private int flag;
	private ArrayList<ArrayList<Pcb>> procesori=new ArrayList<>();
	private final int MAX=1000;//proveri
	
	public Novi() {
		flag=0;
	}
	
	void dodajCFS(int id,Pcb pcb) {
		ArrayList<Pcb> list=procesori.get(id);
		
		if (pcb==null)
			return;
		
		if (pcb.getPcbData()==null)
			pcb.setPcbData(new PcbData());
		
		if ((Pcb.ProcessState.BLOCKED.equals(pcb.getPreviousState()))||(Pcb.ProcessState.CREATED.equals(pcb.getPreviousState()))){
			pcb.getPcbData().setVreme(0);
		}
		
		else {
			pcb.getPcbData().setVreme(pcb.getPcbData().getVreme()+pcb.getExecutionTime());
		}
		
		pcb.getPcbData().setStartWaitTime(Pcb.getCurrentTime());
		
		list.add(pcb);
		
	}
	
	Pcb uzmiCFS(int id) {
		ArrayList<Pcb> list=procesori.get(id);
		
		if (list.isEmpty())
			return null;
		
		Pcb min=list.get(0);
		Pcb pom=null;
		int n=0,f=0;
		
		if (Pcb.getCurrentTime()-min.getPcbData().getStartWaitTime()>MAX) {
			n=0;
			f=1;
		}
		
		if (f==0) {
			for(int i=1;i<list.size();i++) {
				pom= list.get(i);
				if (pom.getPcbData().getVreme()<min.getPcbData().getVreme()) {
					min=pom;
					n=i;
				}
				
			}
		}
		
		list.remove(n);
		long kvant=((Pcb.getCurrentTime()-min.getPcbData().getStartWaitTime())/Pcb.getProcessCount());
		if (kvant==0)
			 kvant=0;
			
		min.setTimeslice(kvant);
		
		return min;
	}
	
	
	
	public Pcb get(int cpuId) {
		Pcb pom=null;

		if (flag==0) {
			for(int i=0;i<Pcb.RUNNING.length;i++) {
				procesori.add(i,new ArrayList<>());
			}
			
			flag=1;
		}
		
		if (procesori.get(cpuId).size()>0) {
			//return procesori.get(cpuId).remove(0);
			return uzmiCFS(cpuId);
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
			pom=uzmiCFS(id);
			pom.getPcbData().setCpuId(cpuId);
		}
		
		return pom;
	}


	public void put(Pcb pcb) {
		if (pcb==null)
			return;
		
		if (flag==0) {
			for(int i=0;i<Pcb.RUNNING.length;i++) {
				procesori.add(i,new ArrayList<>());
			}
			
			flag=1;
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
			
			dodajCFS(id,pcb);
		}
		
		
		else {
			//procesori.get(pcb.getPcbData().getCpuId()).add(pcb);
			dodajCFS(pcb.getPcbData().getCpuId(),pcb);
		}
	}

}
