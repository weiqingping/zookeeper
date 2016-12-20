package com.my.test.zkClient.leader;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;


public class LeaderService {
	private ServiceInfo masterService;
	private ServiceInfo selfService;
	private String connectionUrl;
	private int sessionTimeOut;
	private ZkClient client;
	private String path;
	
	public ServiceInfo getSelfService() {
		return selfService;
	}

	public void setSelfService(ServiceInfo selfService) {
		this.selfService = selfService;
	}

	private LeaderService() {
	}
	
	public LeaderService(String connectionUrl, int sessionTimeOut,String path) {
		this.connectionUrl = connectionUrl;
		this.sessionTimeOut = sessionTimeOut;
		this.path=path;
		client=new ZkClient(connectionUrl, sessionTimeOut, sessionTimeOut,new SerializableSerializer());
	}



	public void run(){
		
		client.subscribeDataChanges(path, new IZkDataListener() {
			
			public void handleDataDeleted(String dataPath) throws Exception {
				// TODO Auto-generated method stub
				takeLeader();
			}
			
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// TODO Auto-generated method stub
				
			}
		});

		takeLeader();
		
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(){
		client.close();
	}
	
	private void releaseMaster(){
		if(checkMaster()){
		client.delete(path);
		}
	}
	
	private boolean takeLeader(){
		try {
			client.createEphemeral(path, selfService);
			this.masterService=selfService;
			System.out.println("the service:"+selfService.getServiceName()+" hold the master");
			try {
				
				System.out.println("模拟执行业务逻辑:");

				Thread.sleep(10000);
				releaseMaster();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		} catch (ZkNodeExistsException e) {
			// TODO: handle exception
			if(client.exists(path)){
				try {
					ServiceInfo info= client.readData(path);
					if(null!=info){
						this.masterService=info;
						
					}
					takeLeader();	
				} catch (ZkNoNodeException e2) {
					// TODO: handle exception
					takeLeader();	
				}
			
			}
			//System.out.println("the service:"+selfService.getServiceName()+" not hold the master");
			
		}
	
		return false;
	}
	
	private boolean checkMaster(){
        try {
            //读取当前master节点的数据，并赋值给masterdata
        	if(client.exists(path)){
        	ServiceInfo ms = client.readData(path);
        	masterService = ms;
            //这个时候，如果master节点的数据和当前过来争抢master节点的服务器的数据是一样的话，那么意味
            //当前的serverData就是master
            if (masterService.getServiceName().equals(selfService.getServiceName())) {
                return true;
            }
        	}
            return false;
        }catch (ZkNoNodeException e){
            return false;
        }catch (ZkInterruptedException e){
            return checkMaster();
        }catch (ZkException e){
            return false;
        }
    }
	
	
	public static void main(String[] args) {
		 final String SERVICE="192.168.17.130:2182,192.168.17.130:2183,192.168.17.130:2181";
		 final int SESSION_TIME_OUT=3000;
     ExecutorService  executorService=Executors.newFixedThreadPool(10);
     final Semaphore semaphore=new Semaphore(10);

     for (int i = 0; i < 10; i++) {
		   final int j=i;
		Thread t=new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				try {
					semaphore.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LeaderService service=new LeaderService(SERVICE, SESSION_TIME_OUT, "/leader");
				service.setSelfService(new ServiceInfo("192.168.17.129", "servce"+(j+1)));
				service.run();
				semaphore.release();
				
			}
		});
		
		executorService.execute(t);
   	  
	}
     
     executorService.shutdown();
    
	}
	

}
