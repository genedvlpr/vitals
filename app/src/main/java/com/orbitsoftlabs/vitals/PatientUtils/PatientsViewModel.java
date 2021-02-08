package com.orbitsoftlabs.vitals.PatientUtils;

public class PatientsViewModel {
    public String patientFName, patientLName, patientAddress, patientAge, patientContactNo, dateDiagnosed,
            seasonDiagnosed,monthDiagnosed,patientBirthday,patientBarangay, pID;

    public PatientsViewModel() {

    }

    public PatientsViewModel(String patientFName, String patientLName, String patientAddress, String patientAge, String patientContactNo, String dateDiagnosed, String seasonDiagnosed, String monthDiagnosed, String patientBirthday, String patientBarangay, String pID) {
        this.patientFName = patientFName;
        this.patientLName = patientLName;
        this.patientAddress = patientAddress;
        this.patientAge = patientAge;
        this.patientContactNo = patientContactNo;
        this.dateDiagnosed = dateDiagnosed;
        this.seasonDiagnosed = seasonDiagnosed;
        this.monthDiagnosed = monthDiagnosed;
        this.patientBirthday = patientBirthday;
        this.patientBarangay = patientBarangay;
        this.pID = pID;
    }

    public String getPatientFName() {
        return patientFName;
    }

    public void setPatientFName(String patientFName) {
        this.patientFName = patientFName;
    }

    public String getPatientLName() {
        return patientLName;
    }

    public void setPatientLName(String patientLName) {
        this.patientLName = patientLName;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientContactNo() {
        return patientContactNo;
    }

    public void setPatientContactNo(String patientContactNo) {
        this.patientContactNo = patientContactNo;
    }

    public String getDateDiagnosed() {
        return dateDiagnosed;
    }

    public void setDateDiagnosed(String dateDiagnosed) {
        this.dateDiagnosed = dateDiagnosed;
    }

    public String getSeasonDiagnosed() {
        return seasonDiagnosed;
    }

    public void setSeasonDiagnosed(String seasonDiagnosed) {
        this.seasonDiagnosed = seasonDiagnosed;
    }

    public String getMonthDiagnosed() {
        return monthDiagnosed;
    }

    public void setMonthDiagnosed(String monthDiagnosed) {
        this.monthDiagnosed = monthDiagnosed;
    }

    public String getPatientBirthday() {
        return patientBirthday;
    }

    public void setPatientBirthday(String patientBirthday) {
        this.patientBirthday = patientBirthday;
    }

    public String getPatientBarangay() {
        return patientBarangay;
    }

    public void setPatientBarangay(String patientBarangay) {
        this.patientBarangay = patientBarangay;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }
}
