package com.example.ftthonay;

public class OnayIslem {

    private String hizmet_no;
    private String hgw_barkod, hgw_model;
    private String ont_model,ont_barkod;
    private String onay_aciklama;
    private String tarih;
    private String eleAlindi;
    private String hataAciklama;

    public OnayIslem(String hizmet_no, String hgw_model, String hgw_barkod, String ont_model, String ont_barkod, String onay_aciklama, String tarih, String eleAlindi, String hataAciklama) {
        this.hizmet_no = hizmet_no;
        this.hgw_model = hgw_model;
        this.hgw_barkod = hgw_barkod;
        this.ont_model = ont_model;
        this.ont_barkod = ont_barkod;
        this.onay_aciklama = onay_aciklama;
        this.tarih = tarih;
        this.eleAlindi = eleAlindi;
        this.hataAciklama = hataAciklama;
    }

    public OnayIslem(String hizmet_no, String hgw_model, String hgw_barkod, String onay_aciklama, String tarih, String eleAlindi, String hataAciklama) {
        this(hizmet_no, hgw_model, hgw_barkod,null,null, onay_aciklama, tarih, eleAlindi, hataAciklama);
    }

    public OnayIslem() {
    }

    public String getHataAciklama() {
        return hataAciklama;
    }

    public void setHataAciklama(String hataAciklama) {
        this.hataAciklama = hataAciklama;
    }

    public String getEleAlindi() {
        return eleAlindi;
    }

    public void setEleAlindi(String eleAlindi) {
        this.eleAlindi = eleAlindi;
    }

    public String getHgw_model() {
        return hgw_model;
    }

    public void setHgw_model(String hgw_model) {
        this.hgw_model = hgw_model;
    }

    public String getHizmet_no() {
        return hizmet_no;
    }

    public void setHizmet_no(String hizmet_no) {
        this.hizmet_no = hizmet_no;
    }

    public String getHgw_barkod() {
        return hgw_barkod;
    }

    public void setHgw_barkod(String hgw_barkod) {
        this.hgw_barkod = hgw_barkod;
    }

    public String getOnt_model() {
        return ont_model;
    }

    public void setOnt_model(String ont_model) {
        this.ont_model = ont_model;
    }

    public String getOnt_barkod() {
        return ont_barkod;
    }

    public void setOnt_barkod(String ont_barkod) {
        this.ont_barkod = ont_barkod;
    }


    public String getOnay_aciklama() {
        return onay_aciklama;
    }

    public void setOnay_aciklama(String onay_aciklama) {
        this.onay_aciklama = onay_aciklama;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

}
