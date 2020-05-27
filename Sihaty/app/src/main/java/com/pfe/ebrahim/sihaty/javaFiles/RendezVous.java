package com.pfe.ebrahim.sihaty.javaFiles;

/**
 * Created by ebrahim on 30/03/2018.
 */

public class RendezVous {
    public String specialite;
    public String date;
    public String isConfimrmed;
    public String id;

    public RendezVous(String id, String isConfimrmed, String specialite, String date) {
        this.specialite = specialite;
        this.date = date;
        this.isConfimrmed = isConfimrmed;
        this.id = id;
    }
}
