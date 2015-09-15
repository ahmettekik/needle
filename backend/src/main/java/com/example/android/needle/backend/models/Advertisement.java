package com.example.android.needle.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import java.util.Date;

/**
 * Created by jonfisk on 04/09/15.
 */
@Entity
public class Advertisement {
    @Id
    private Long key;

    @Index
    private String userEmail;

    @Unindex
    private String description;

    @Index
    private Date advertisementDate;

    @Index
    private String countryCode;

    @Index
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


    public String getUserAccountId() {
        return userEmail;
    }

    public void setUserAccountId(String userEmail) {
        this.userEmail = userEmail;
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
