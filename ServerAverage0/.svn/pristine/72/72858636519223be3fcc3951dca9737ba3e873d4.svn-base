package com.server.jt.service;

import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;

public class ObserverListener implements Observer{
	
	private static Logger log = Logger.getLogger(ObserverListener.class);

	@Override
	public void update(Observable o, Object arg) {
		boolean socketStatus = false;
         if("monitorsocket".equals(arg.toString())){
        	 while(!socketStatus){//重连一直失败的话，则会一直重连
	        	 log.error("【monitorsocket】已经开始重连。。。");
	        	 JsonS.monitorSocketThread = new MonitorSocketThread();
	        	 JsonS.monitorSocketThread.addObserver(this);
	        	 new Thread(JsonS.monitorSocketThread).start();
	        	 if(JsonS.monitorSocketThread.Socket !=null && JsonS.monitorSocketThread.Socket.isConnected() == true){
	        		 
	        		 socketStatus = true;
	        		 log.error("【monitorsocket】重连完成");
	        	 }
	        	 try {
					 Thread.sleep(2000);
					 } catch (InterruptedException e) {	
						e.printStackTrace();
					 }
        	 }
         }

	}

}
