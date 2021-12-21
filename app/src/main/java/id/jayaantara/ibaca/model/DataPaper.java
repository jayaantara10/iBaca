package id.jayaantara.ibaca.model;

import com.google.gson.annotations.SerializedName;

public class DataPaper {
    @SerializedName("id")
    private long id;
    @SerializedName("judul")
    private String judul;
    @SerializedName("jenis")
    private String jenis;
    @SerializedName("penulis")
    private String penulis;
    @SerializedName("link")
    private String link;
    @SerializedName("lisensi")
    private String lisensi;
    @SerializedName("batasan_umur")
    private String batasan_umur;
    @SerializedName("deskripsi")
    private String deskripsi;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("update_at")
    private String update_at;
    @SerializedName("id_user")
    private long id_user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLisensi() {
        return lisensi;
    }

    public void setLisensi(String lisensi) {
        this.lisensi = lisensi;
    }

    public String getBatasan_umur() {
        return batasan_umur;
    }

    public void setBatasan_umur(String batasan_umur) {
        this.batasan_umur = batasan_umur;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }
}
