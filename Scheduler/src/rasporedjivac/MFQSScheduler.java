package rasporedjivac;

import java.util.ArrayList;

public class MFQSScheduler extends Scheduler{
	private int br;
	//private int[] prioriteti;
	private int [] kvanti;
	private ArrayList<ArrayList<Pcb>> redovi;
	
	
	public MFQSScheduler(int cnt, int[]niz) {
		br=cnt;
		
		/*for (int j=0;j<br;j++)
			prioriteti[j]=j;
			*/
//		kvanti=new int[br];
//		
//		for (int i=0;i<niz.length;i++) 
//			kvanti[i]=niz[i];
		

		kvanti=niz;
		redovi=new ArrayList<>(br);
		
		for(int i=0;i<br;i++) {
			redovi.add(new ArrayList<>());
		}
	}
	


	public Pcb get(int cpuId) {
		Pcb pcb=null;
		int n=0;
	
		
		for(int i=0;i<br;i++) {
			if (redovi.get(i).size()>0) {
				pcb=redovi.get(i).remove(0);
				n=i;
				break;
			}
		}
		
		if (pcb!=null)
			pcb.setTimeslice(kvanti[n]);
		
		return pcb;
	}


	public void put(Pcb pcb) {
		if (pcb==null)
			return;
		
		if (pcb.getPcbData()==null)
			pcb.setPcbData(new PcbData());
		
		if (Pcb.ProcessState.CREATED==pcb.getPreviousState()) {
			int pr=pcb.getPriority();
			
			if (pr<0)
				pr=0;
			if (pr>br-1)
				pr=br-1;
			
			redovi.get(pr).add(pcb);
			pcb.getPcbData().setPriority(pr);
			
		}
		
		if (Pcb.ProcessState.BLOCKED==pcb.getPreviousState()) {
			int pr=pcb.getPcbData().getPriority();
			
			if (pr-1<0)
				pr=0;
			else
				pr=pr-1;
			
			redovi.get(pr).add(pcb);
			pcb.getPcbData().setPriority(pr);
		}
		
		if (Pcb.ProcessState.READY==pcb.getPreviousState()) {
			int pr=pcb.getPcbData().getPriority();
			
			if (pr+1>br-1)
				pr=br-1;
			else
				pr=pr+1;
			
			redovi.get(pr).add(pcb);
			pcb.getPcbData().setPriority(pr);
		}
	}

}
