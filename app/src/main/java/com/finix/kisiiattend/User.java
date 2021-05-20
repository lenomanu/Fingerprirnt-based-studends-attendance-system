package com.finix.kisiiattend;

public class User {
    String name, registrationNumber, email, occupation;

    public User() {

    }

    public User(String name, String registrationNumber, String email,String occupation) {
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.email = email;
        this.occupation = occupation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
