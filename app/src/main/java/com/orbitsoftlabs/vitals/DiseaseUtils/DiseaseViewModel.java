package com.orbitsoftlabs.vitals.DiseaseUtils;

public class DiseaseViewModel {
    public String diseaseName;
    public String diseaseDesc;

    public DiseaseViewModel(){

    }

    public DiseaseViewModel(String diseaseName, String diseaseDesc) {
        this.diseaseName = diseaseName;
        this.diseaseDesc = diseaseDesc;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDiseaseDesc() {
        return diseaseDesc;
    }

    public void setDiseaseDesc(String diseaseDesc) {
        this.diseaseDesc = diseaseDesc;
    }
}
