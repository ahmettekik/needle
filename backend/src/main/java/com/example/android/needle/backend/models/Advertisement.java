package com.example.android.needle.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

/**
 * Created by jonfisk on 04/09/15.
 */
@Entity
public class Advertisement {
    @Id
    private Long key;

    private Long userAccountId;


    private String description;

    private Date advertisementDate;

    private String countryCode;

    private int zipCode;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Long userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAdvertisementDate() {
        return advertisementDate;
    }

    public void setAdvertisementDate(Date advertisementDate) {
        this.advertisementDate = advertisementDate;
    }
}
