package com.my.test.zkClient.zkClient;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

public class MyZkSerializer implements ZkSerializer {

	public byte[] serialize(String data) throws ZkMarshallingError {
		// TODO Auto-generated method stub
		return data.getBytes();
	}

	public String deserialize(byte[] bytes) throws ZkMarshallingError {
		// TODO Auto-generated method stub
		return new String(bytes);
	}

	public byte[] serialize(Object data) throws ZkMarshallingError {
		// TODO Auto-generated method stub
		return serialize( data.toString());
	}

	

}
