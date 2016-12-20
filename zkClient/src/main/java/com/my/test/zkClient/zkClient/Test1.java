package com.my.test.zkClient.zkClient;

import java.util.Iterator;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class Test1 {

	private static final String SERVICE="192.168.17.129:2182,192.168.17.129:2183,192.168.17.129:2184";
	private static int SESSION_TIME_OUT=3000;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ZkClient zk=new ZkClient(SERVICE, SESSION_TIME_OUT, SESSION_TIME_OUT, new MyZkSerializer(), SESSION_TIME_OUT);
		//createNode(zk,"/zkclient","zkclient");
		
		// createObjectNode(zk);
		//createtNode(zk,"/zkclient/commonnode","commonnode");
		
		addWatch(zk,"/zkclient");
		
		//deleteAll(zk,"/zkclient");
		System.in.read();
		zk.close();
	}
	
   private static void  createNode(ZkClient zk,String path,String data)throws Exception{
	   zk.createPersistent(path,  data);
	   String tdata=zk.readData(path, true);
	   System.out.println("create data:"+tdata);
   }
   
   private static void  createObjectNode(ZkClient zk)throws Exception{
	   
	   User user=new User();
	   user.setPhone("15982793071");
	   user.setUserName("weiqp");
	   createObjectNode(zk,"/zkclient/obj",user);
   }
   
   
   private static void  createtNode(ZkClient zk,String path,Object data)throws Exception{
	   if(!zk.exists(path))
	   zk.createPersistent(path,  data);
	   String tdata=zk.readData(path, true);
	   System.out.println("create data:"+tdata);
   }
   
   private static void  createObjectNode(ZkClient zk,String path,Object data)throws Exception{
	   if(!zk.exists(path))
	   zk.createPersistent(path,  data);
	   User tdata=zk.readData(path, true);
	   System.out.println("create data:"+tdata);
   }
   
   private static void  deleteAll(ZkClient zk,String path)throws Exception{
	   if(zk.exists(path))
	   zk.deleteRecursive(path);
	  
   }
   
   private static void  addChildWatch( final ZkClient zk,String path)throws Exception{
	   zk.subscribeChildChanges(path, new IZkChildListener() {	
		public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("监听事件:ChildChange"+" parentPath:"+parentPath);
			for (Iterator iterator = currentChilds.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				System.out.println("childpath:"+string);
				System.out.println("data:"+zk.readData(parentPath+"/"+string));
				
			}

		}
	});
   }
   
   private static void  addWatch(ZkClient zk,String path)throws Exception{
	   zk.subscribeDataChanges(path, new IZkDataListener() {
		
		public void handleDataDeleted(String dataPath) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("监听事件:DataDeleted"+" path:"+dataPath);
			
		}
		
		public void handleDataChange(String dataPath, Object data) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("监听事件:DataChange"+" path:"+dataPath+" data:"+data);

			
		}
	});
   }

}
