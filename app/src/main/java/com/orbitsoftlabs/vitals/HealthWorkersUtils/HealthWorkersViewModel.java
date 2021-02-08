package com.orbitsoftlabs.vitals.HealthWorkersUtils;

public class HealthWorkersViewModel {
    public String address, barangay, email, first_name, last_name , middle_name, messenger_username, mobile_no, user_id;

    public HealthWorkersViewModel(){

    }

    public HealthWorkersViewModel(String address, String barangay, String email, String first_name, String last_name, String middle_name, String messenger_username, String mobile_no, String user_id) {
        this.address = address;
        this.barangay = barangay;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.middle_name = middle_name;
        this.messenger_username = messenger_username;
        this.mobile_no = mobile_no;
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getMessenger_username() {
        return messenger_username;
    }

    public void setMessenger_username(String messenger_username) {
        this.messenger_username = messenger_username;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
