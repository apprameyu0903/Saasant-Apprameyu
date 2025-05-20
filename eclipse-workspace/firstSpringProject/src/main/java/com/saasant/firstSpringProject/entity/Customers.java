package com.saasant.firstSpringProject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "customers")
@Getter @Setter @ToString
public class Customers {
	
	//'customer_id','varchar(20)','NO','PRI',NULL,''
	//'customer_name','varchar(20)','NO','',NULL,''
	//'customer_mobile','varchar(12)','YES','',NULL,''
	//'customer_location','varchar(50)','YES','',NULL,''
	
	@Id
	private String customerId;
	private String customerName;
	private String customerMobile;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerMobile() {
		return customerMobile;
	}
	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}
	public String getCustomerLocation() {
		return customerLocation;
	}
	public void setCustomerLocation(String customerLocation) {
		this.customerLocation = customerLocation;
	}
	private String customerLocation;


}
