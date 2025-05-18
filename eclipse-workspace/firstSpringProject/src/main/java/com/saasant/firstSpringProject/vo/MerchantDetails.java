package com.saasant.firstSpringProject.vo;

public class MerchantDetails
{
    String merchantName;

    public MerchantDetails(String merchantName){
        this.merchantName = merchantName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName)
    {
        this.merchantName = merchantName;
    }

    @Override
    public String toString() {
        return "MerchantDetails{" +
                "merchantName='" + merchantName + '\'' +
                '}';
    }
}
