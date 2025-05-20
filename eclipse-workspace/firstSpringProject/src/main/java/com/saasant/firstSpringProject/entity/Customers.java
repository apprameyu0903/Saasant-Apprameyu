package com.saasant.firstSpringProject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customers {
	
	//'customer_id','varchar(20)','NO','PRI',NULL,''
	//'customer_name','varchar(20)','NO','',NULL,''
	//'customer_mobile','varchar(12)','YES','',NULL,''
	//'customer_location','varchar(50)','YES','',NULL,''
	
	@Id
	private String customerId;
	private String customerName;
	private String customerMobile;
	private String customerLocation;


}
