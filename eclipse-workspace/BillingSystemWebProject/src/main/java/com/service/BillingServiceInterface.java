package com.service;
import com.vo.*;
import java.util.*;

public interface BillingServiceInterface {
	
	Map<String, ProductInfo> billingService();
	float subTotal();
	float gstCalc();
	float calcTotal();

}
