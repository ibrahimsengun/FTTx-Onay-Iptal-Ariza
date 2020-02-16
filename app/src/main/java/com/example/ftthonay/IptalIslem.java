package com.example.ftthonay;

public class IptalIslem {
    private String hizmet_no;
    private String iptal_sebep;
    private String iptal_aciklama;
    private String tarih;


    public IptalIslem(String hizmet_no, String iptal_sebep, String iptal_aciklama, String tarih) {
        this.hizmet_no = hizmet_no;
        this.iptal_sebep = iptal_sebep;
        this.iptal_aciklama = iptal_aciklama;
        this.tarih = tarih;
    }

    public IptalIslem(String hizmet_no, String iptal_sebep, String tarih)
    {
        this(hizmet_no, iptal_sebep, null, tarih);
    }

    public IptalIslem() {
    }

    public String getHizmet_no() {
        return hizmet_no;
    }

    public void setHizmet_no(String hizmet_no) {
        this.hizmet_no = hizmet_no;
    }

    public String getIptal_sebep() {
        return iptal_sebep;
    }

    public void setIptal_sebep(String iptal_sebep) {
        this.iptal_sebep = iptal_sebep;
    }

    public String getIptal_aciklama() {
        return iptal_aciklama;
    }

    public void setIptal_aciklama(String iptal_aciklama) {
        this.iptal_aciklama = iptal_aciklama;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }
}
