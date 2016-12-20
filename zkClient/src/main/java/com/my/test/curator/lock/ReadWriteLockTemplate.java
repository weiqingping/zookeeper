package com.my.test.curator.lock;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

public class ReadWriteLockTemplate {
   private InterProcessReadWriteLock interProcess;
   private InterProcessMutex writeLock;
   private InterProcessMutex readLock;

   
   
   public ReadWriteLockTemplate(CuratorFramework client,String path){
	   client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
		
		public void stateChanged(CuratorFramework client, ConnectionState newState) {
			// TODO Auto-generated method stub
			System.out.println("zookpper 连接状态:"+newState);
		}
	});
	   
	   
	   interProcess=new InterProcessReadWriteLock(client, path);
	   writeLock=interProcess.writeLock();
	   readLock=interProcess.readLock();
   }
   
   public <T,S> T doLock(WriteBac<S>writeBack,ReadBack<T>readBack) throws Exception{
	   T result=null;
	   try {
		   if(writeLock.acquire(10, TimeUnit.SECONDS)){
			   writeBack.execute();
		   }
		   if(readLock.acquire(10, TimeUnit.SECONDS)){
			   result=readBack.execute();
		   }
		   return result;
	} 
	  finally {
		  writeLock.release();
		  readLock.release();
	}
	  
	   
   }
   
   public interface WriteBac<S>{
	   public S execute();
   }
   
   public interface ReadBack<T>{
	   public T execute();

   }
}
