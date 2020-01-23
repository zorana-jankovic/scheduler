package com.etf.os2.project.scheduler;

import java.util.LinkedList;

import com.etf.os2.project.process.*;

public class CFSScheduler extends Scheduler{
private LinkedList<Pcb> list=new LinkedList<>();
	

	public CFSScheduler() {}
	
	

	public Pcb get(int cpuId) {
		
		if (list.isEmpty())
			return null;
		
		Pcb min=list.getFirst();
		Pcb pom=null;
		int n=0;
		
		for(int i=1;i<list.size();i++) {
			pom=list.get(i);
			if (pom.getPcbData().getVreme()<min.getPcbData().getVreme()) {
				min=pom;
				n=i;
			}
			
		}
		
		list.remove(n);
		long kvant=((Pcb.getCurrentTime()-min.getPcbData().getStartWaitTime())/Pcb.getProcessCount());//kako da ne padne na 0???
		if (kvant==0)
			 kvant=10;
			
		min.setTimeslice(kvant);
		
		
		return min;
		
		
		
	}

	
	public void put(Pcb pcb) {
	
		if (pcb==null)
			return;
		
		
		
		if (pcb.getPcbData()==null)
			pcb.setPcbData(new PcbData());
		
		if ((Pcb.ProcessState.BLOCKED==pcb.getPreviousState())||(Pcb.ProcessState.CREATED==pcb.getPreviousState())){
			pcb.getPcbData().setVreme(0);
		}
		
		else {
			pcb.getPcbData().setVreme(pcb.getPcbData().getVreme()+pcb.getExecutionTime());
		}
		
		pcb.getPcbData().setStartWaitTime(Pcb.getCurrentTime());
		
		list.add(pcb);
	}

}
