package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.util.Objects;

public class Customer extends Person  {
    private String customerId;
    private Boolean preferentialRequired;

    public Customer() {
        super();

    }

    public Customer(int id ,String name,String customerId, Boolean preferentialRequired) {
        super(id, name);
        this.preferentialRequired = preferentialRequired;
        this.customerId = String.valueOf(customerId);
    }
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Boolean getPreferentialRequired() {
        return preferentialRequired;
    }

    public void setPreferentialRequired(Boolean preferentialRequired) {
        this.preferentialRequired = preferentialRequired;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(customerId);
    }
}
