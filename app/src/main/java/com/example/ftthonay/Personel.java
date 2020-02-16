package com.example.ftthonay;

import java.util.List;

public class Personel {

    private String name;
    private String surname;
    private String TC_ID;
    private boolean amirlik;
    private List<OnayIslem> confirms;
    private List<IptalIslem> cancels;
    private List<CekGonder> cekGonder;

    public Personel(String name, String surname, String TC_ID, boolean amirlik, List<OnayIslem> confirms, List<IptalIslem> cancels) {
        this.name = name;
        this.surname = surname;
        this.TC_ID = TC_ID;
        this.amirlik = amirlik;
        this.confirms = confirms;
        this.cancels = cancels;
    }

    public Personel(String name, String surname, String TC_ID, boolean amirklik)
    {
        this(name, surname, TC_ID, amirklik, null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTC_ID() {
        return TC_ID;
    }

    public void setTC_ID(String TC_ID) {
        this.TC_ID = TC_ID;
    }

    public boolean isAmirklik() {
        return amirlik;
    }

    public void setAmirklik(boolean amirklik) {
        this.amirlik = amirklik;
    }

    public List<OnayIslem> getConfirms() {
        return confirms;
    }

    public void setConfirms(List<OnayIslem> confirms) {
        this.confirms = confirms;
    }

    public List<IptalIslem> getCancels() {
        return cancels;
    }

    public void setCancels(List<IptalIslem> cancels) {
        this.cancels = cancels;
    }
}

