package com.example.ftthonay;

class CekGonder {
    private String ariza_turu;
    private String foto_id;
    private String aciklama;
    private String tarih;

    public CekGonder(String ariza_turu, String foto_id, String aciklama, String tarih) {
        this.ariza_turu = ariza_turu;
        this.foto_id = foto_id;
        this.aciklama = aciklama;
        this.tarih = tarih;
    }

    public CekGonder() {
    }

    public String getAriza_turu() {
        return ariza_turu;
    }

    public void setAriza_turu(String ariza_turu) {
        this.ariza_turu = ariza_turu;
    }

    public String getFotograf() {
        return foto_id;
    }

    public void setFotograf(String foto_id) {
        this.foto_id = foto_id;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }
}
