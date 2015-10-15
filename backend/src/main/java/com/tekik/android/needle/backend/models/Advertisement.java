package com.tekik.android.needle.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by jonfisk on 04/09/15.
 */
@Entity
@Index
public class Advertisement {


    @Id
    private Long key;

    private String userEmail;

    private String description;

    private Date date;

    private String phoneNumber;

    private String name;

    private String countryCode;

    private String zipCode;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAdvertisementDate() {
        return date;
    }

    public Long getKey() {
        return key;
    }

    public void setAdvertisementDate(Date advertisementDate) {
        this.date = advertisementDate;
    }
}
