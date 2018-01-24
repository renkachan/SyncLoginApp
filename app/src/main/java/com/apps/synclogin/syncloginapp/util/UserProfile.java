package com.apps.synclogin.syncloginapp.util;

/**
 * Created by Renka on 1/24/2018.
 */

public class UserProfile {
    private String id;
    private String firstName;
    private String lastName;
    private String email;

    public void setID (String id) {
        this.id = id;
    }

    public void  setFirstName (String firstName) {
        this.firstName = firstName;
    }

    public void setLastName (String lastName) {
        this.lastName = lastName;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
