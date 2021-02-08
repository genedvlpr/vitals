package com.orbitsoftlabs.vitals.MedSolutionUtils;

public class MedViewModel {
    public String medName;
    public String medDescription;
    public String medForIllness;

    public MedViewModel(){

    }

    public MedViewModel(String medName, String medDescription, String medForIllness) {
        this.medName = medName;
        this.medDescription = medDescription;
        this.medForIllness = medForIllness;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedDescription() {
        return medDescription;
    }

    public void setMedDescription(String medDescription) {
        this.medDescription = medDescription;
    }

    public String getMedForIllness() {
        return medForIllness;
    }

    public void setMedForIllness(String medForIllness) {
        this.medForIllness = medForIllness;
    }
}
