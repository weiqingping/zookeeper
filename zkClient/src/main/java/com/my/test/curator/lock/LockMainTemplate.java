package com.my.test.curator.lock;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public class LockMainTemplate {
	private  InterProcessMutex lock;

	public InterProcessMutex getLock() {
		return lock;
	}

	public void setLock( String path,CuratorFramework client) {
		this.lock = lock;
		lock=new InterProcessMutex(client, path);
	
	}
	
	public <T>T  dolock( CallBack<T> callBack) throws Exception{
		T result=null;
		if(lock.acquire(10, TimeUnit.SECONDS)){
			
			result= callBack.execute();
		}
		result= callBack.execute();
		if(lock.isAcquiredInThisProcess()){
		lock.release();
		
		}
		return result;
	}
	
	
 public interface CallBack<T>{
	 public T execute();
	 
	 
 }
	
}
