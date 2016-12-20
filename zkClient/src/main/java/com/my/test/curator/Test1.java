package com.my.test.curator;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class Test1 {
	private static final String SERVICE="192.168.17.129:2182,192.168.17.129:2183,192.168.17.129:2184";
	private static int SESSION_TIME_OUT=3000;
	
	public static void main(String[] args) throws Exception {
		CuratorFramework factory= CuratorFrameworkFactory.newClient(SERVICE, SESSION_TIME_OUT, SESSION_TIME_OUT, new ExponentialBackoffRetry(SESSION_TIME_OUT,30000));
		factory.start();
		//addNode(factory,"/curator/node2","node2");
		//update(factory,"/curator/node2","node2");
		//delate(factory,"/curator");
		
		//addNodeWatch(factory,"/curator/node2");
		//addChildNodeWatch(factory,"/curator");
		addTreeWatch(factory,"/curator");
		System.in.read();
		factory.close();
	}
	
	
	private static void addNode(CuratorFramework factory,String path,String data)throws Exception{
		
		factory.create().creatingParentsIfNeeded().forPath(path, data.getBytes());

	}
	
    private static void update(CuratorFramework factory,String path,String data)throws Exception{
		
		factory.setData().forPath(path, "update".getBytes());
	}
    
   private static void delate(CuratorFramework factory,String path)throws Exception{
		
		factory.delete().deletingChildrenIfNeeded().forPath(path);
	}
   
   private static void addNodeWatch (CuratorFramework factory,String path)throws Exception{
		
		 final NodeCache nodeCach=new NodeCache(factory, path);
		nodeCach.getListenable().addListener(new NodeCacheListener() {
			
			public void nodeChanged() throws Exception {
				// TODO Auto-generated method stub
				String data=new String(nodeCach.getCurrentData().getData());
				String path=nodeCach.getCurrentData().getPath();
				System.out.println("node change:path:"+path+" data:"+data);
			}
		}
		);
		
		nodeCach.start();
		
   }
   
   private static void addChildNodeWatch (CuratorFramework factory,String path)throws Exception{
		
	   final PathChildrenCache pathCache=new PathChildrenCache(factory, path, true);
	   pathCache.getListenable().addListener(new PathChildrenCacheListener() {
		
		public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("事件:"+event.getType()+" path:"+event.getData().getPath()+" data:"+event.getData().getData());
			
		}
	});
		 
	   pathCache.start(StartMode.BUILD_INITIAL_CACHE);
		
 }
    
   private static void addTreeWatch (CuratorFramework factory,String path)throws Exception{
		
	   final TreeCache pathCache=new TreeCache(factory, path);
	   pathCache.getListenable().addListener(new TreeCacheListener() {
		
		public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("事件:"+event.getType()+" path:"+event.getData().getPath()+" data:"+event.getData().getData());

		}
	});
	   pathCache.start();
		
 }
    
	
}
