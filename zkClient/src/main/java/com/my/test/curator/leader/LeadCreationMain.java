package com.my.test.curator.leader;

import java.io.Closeable;
import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

public abstract class LeadCreationMain  extends LeaderSelectorListenerAdapter implements Closeable {
   private  LeaderSelector selector;
   
	public LeadCreationMain(CuratorFramework client,String path) {
	LeaderSelector selector=new LeaderSelector(client, path, this);
	selector.autoRequeue();
	selector.start();
	this.selector=selector;
	}
	
	public void start(){
		selector.start();
	}

	public void close() throws IOException {
		// TODO Auto-generated method stub
		this.selector.close();
	}
	
	
	
}
