package com.my.test.zkClient.leader;

import java.io.Serializable;

public class ServiceInfo implements Serializable {
private String serviceId;
private String serviceName;
public String getServiceId() {
	return serviceId;
}
public void setServiceId(String serviceId) {
	this.serviceId = serviceId;
}
public String getServiceName() {
	return serviceName;
}
public void setServiceName(String serviceName) {
	this.serviceName = serviceName;
}
public ServiceInfo(String serviceId, String serviceName) {
	this.serviceId = serviceId;
	this.serviceName = serviceName;
}

}
