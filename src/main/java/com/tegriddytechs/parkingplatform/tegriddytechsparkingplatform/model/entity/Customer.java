package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.util.Objects;

public class Customer extends User {
    private String customerId ;
    private Boolean preferentialRequired;

    public Customer() {
        super();
    }

    public Customer(String customerId, String name, Boolean preferentialRequired) {
        super(customerId,name);
        this.customerId = customerId;
        this.preferentialRequired = preferentialRequired;
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

    public Customer(String id, String name, String customerId, Boolean preferentialRequired) {
        super(id, name);
        this.customerId = customerId;
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
