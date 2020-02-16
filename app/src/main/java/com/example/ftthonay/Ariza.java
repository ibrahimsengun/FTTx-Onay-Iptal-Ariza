package com.example.ftthonay;

public class Ariza {

    private String hizmet_no;
    private String sonuc_kod_grubu;
    private String sonuc_kodu;
    private String aciklama;
    private String tarih;

    public Ariza(String hizmet_no, String sonuc_kod_grubu, String sonuc_kodu, String aciklama, String tarih) {
        this.hizmet_no = hizmet_no;
        this.sonuc_kod_grubu = sonuc_kod_grubu;
        this.sonuc_kodu = sonuc_kodu;
        this.aciklama = aciklama;
        this.tarih = tarih;
    }

    public Ariza() {
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getHizmet_no() {
        return hizmet_no;
    }

    public void setHizmet_no(String hizmet_no) {
        this.hizmet_no = hizmet_no;
    }

    public String getSonuc_kod_grubu() {
        return sonuc_kod_grubu;
    }

    public void setSonuc_kod_grubu(String sonuc_kod_grubu) {
        this.sonuc_kod_grubu = sonuc_kod_grubu;
    }

    public String getSonuc_kodu() {
        return sonuc_kodu;
    }

    public void setSonuc_kodu(String sonuc_kodu) {
        this.sonuc_kodu = sonuc_kodu;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}
