package com.saasant.firstSpringProject.service;
import com.saasant.firstSpringProject.vo.ProductInfo;
import java.util.*;

public interface BillingServiceInterface {
	
	Map<String, ProductInfo> billingService();
	float subTotal();
	float gstCalc();
	float calcTotal();

}
