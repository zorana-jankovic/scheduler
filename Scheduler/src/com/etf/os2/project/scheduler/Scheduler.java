package com.etf.os2.project.scheduler;

import com.etf.os2.project.process.Pcb;



public abstract class Scheduler {
    public abstract Pcb get(int cpuId);

    public abstract void put(Pcb pcb);

    public static Scheduler createScheduler(String[] args) {
    	if (args.length==0)
			return null;
		
		String ime=args[0];
		
		if ("SJF".equals(ime)) {
			double pom1;
			int pom2;
			
			if (args.length!=3)
				return null;
			
			pom1=Double.parseDouble(args[1]);
			
			if ((pom1<0)||(pom1>1))
				return null;
			
			pom2=Integer.parseInt(args[2]);
			
			if ((pom2!=0)&&(pom2!=1))
				return null;
			
			return  new SJFScheduler(pom1,pom2);
			
		}
		
		if ("MFQS".equals(ime)) {
			int br;
			int[] niz;
			
			if (args.length<=2)
				return null;
			
			
			br=Integer.parseInt(args[1]);
			if (br<0)
				return null;
			
			if (args.length!=(br+2))
				return null;
			
			niz=new int[br];
			
			int n=2;
			for (int i=0;i<br;i++)
				niz[i]=Integer.parseInt(args[n++]);
			
			return new MFQSScheduler(br,niz);
		}
		
		if ("CFS".equals(ime)) {
			return new CFSScheduler();
		}
	
		if ("Novi".equals(ime)) {
			//int br=Integer.parseInt(args[1]);
			
			return new Novi();
		}
		
		if ("NoviSJF".equals(ime)) {
			
			double pom1;
			int pom2;
			
			if (args.length!=3)
				return null;
			
			pom1=Double.parseDouble(args[1]);
			
			if ((pom1<0)||(pom1>1))
				return null;
			
			pom2=Integer.parseInt(args[2]);
			
			if ((pom2!=0)&&(pom2!=1))
				return null;
			
			return  new NoviSJF(pom1,pom2);
			
			
		}
		
		if ("NoviMFQS".equals(ime)) {
			int br;
			int[] niz;
			
			if (args.length<=2)
				return null;
			
			
			br=Integer.parseInt(args[1]);
			if (br<0)
				return null;
			
			if (args.length!=(br+2))
				return null;
			
			niz=new int[br];
			
			int n=2;
			for (int i=0;i<br;i++)
				niz[i]=Integer.parseInt(args[n++]);
			
			return new NoviMFQS(br,niz);
		}
		
		if ("ActiveExpire".equals(ime)) {
			return new ActiveExpire();
		}
		
		return null;
	}
    
}
