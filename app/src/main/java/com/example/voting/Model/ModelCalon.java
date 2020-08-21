package com.example.voting.Model;

public class ModelCalon {
    String id;

    public String getPaslon_nomor() {
        return paslon_nomor;
    }

    public void setPaslon_nomor(String paslon_nomor) {
        this.paslon_nomor = paslon_nomor;
    }

    String paslon_nomor;
    String nama_ketua;
    String nama_wakil;
    String nis_ketua;
    String nis_wakil;
    String foto_paslon,
    persentase,pemilih;

    public String getPersentase() {
        return persentase;
    }

    public void setPersentase(String persentase) {
        this.persentase = persentase;
    }

    public String getPemilih() {
        return pemilih;
    }

    public void setPemilih(String pemilih) {
        this.pemilih = pemilih;
    }

    public String getFoto_paslon() {
        return foto_paslon;
    }

    public void setFoto_paslon(String foto_paslon) {
        this.foto_paslon = foto_paslon;
    }

    public String getNis_ketua() {
        return nis_ketua;
    }

    public void setNis_ketua(String nis_ketua) {
        this.nis_ketua = nis_ketua;
    }

    public String getNis_wakil() {
        return nis_wakil;
    }

    public void setNis_wakil(String nis_wakil) {
        this.nis_wakil = nis_wakil;
    }

    public void setNama_wakil(String nama_wakil) {
        this.nama_wakil = nama_wakil;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getNama_ketua() {
        return nama_ketua;
    }

    public void setNama_ketua(String nama_ketua) {
        this.nama_ketua = nama_ketua;
    }

    public String getNama_wakil() {
        return nama_wakil;
    }

}
