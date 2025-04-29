package com.billing;

public class CustomerDetails
{
    String customerId;
    String customerName;
    String mobileNumber;

    public CustomerDetails(String customerId, String customerName, String mobileNumber)
    {

        this.customerId = customerId;
        this.customerName = customerName;
        this.mobileNumber = mobileNumber;

    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "CustomerDetails{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}
