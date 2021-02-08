package com.orbitsoftlabs.vitals.ClinicUtils;

public class ClinicViewModel {
    String clinicName;
    String clinicAddress;
    String clinicBarangay;
    String clinicPhoneNo;

    public ClinicViewModel(){

    }

    public ClinicViewModel(String clinicName, String clinicAddress, String clinicBarangay, String clinicPhoneNo) {
        this.clinicName = clinicName;
        this.clinicAddress = clinicAddress;
        this.clinicBarangay = clinicBarangay;
        this.clinicPhoneNo = clinicPhoneNo;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getClinicBarangay() {
        return clinicBarangay;
    }

    public void setClinicBarangay(String clinicBarangay) {
        this.clinicBarangay = clinicBarangay;
    }

    public String getClinicPhoneNo() {
        return clinicPhoneNo;
    }

    public void setClinicPhoneNo(String clinicPhoneNo) {
        this.clinicPhoneNo = clinicPhoneNo;
    }
}
