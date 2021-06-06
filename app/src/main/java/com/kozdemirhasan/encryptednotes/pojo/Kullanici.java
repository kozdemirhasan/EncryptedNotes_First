package com.kozdemirhasan.encryptednotes.pojo;

/**
 * Created by Casper on 17.02.2018.
 */

public class Kullanici {
    int kullaniciId;
    String password;
    String fakePassword;
    int silmeDurum;
    int silmeGun;
    int metinBoyutu;

    public Kullanici() {
    }

    public Kullanici(int kullaniciId, String password, String fakePassword) {
        this.kullaniciId = kullaniciId;
        this.password = password;
        this.fakePassword = fakePassword;
    }

    public Kullanici(int kullaniciId, String password, String fakePassword, int silmeDurum, int silmeGun, int metinBoyutu) {
        this.kullaniciId = kullaniciId;
        this.password = password;
        this.fakePassword = fakePassword;
        this.silmeDurum = silmeDurum;
        this.silmeGun = silmeGun;
        this.metinBoyutu = metinBoyutu;
    }

    public Kullanici(String fakePassword, int silmeDurum, int silmeGun, int metinBoyutu) {
        this.fakePassword = fakePassword;
        this.silmeDurum = silmeDurum;
        this.silmeGun = silmeGun;
        this.metinBoyutu = metinBoyutu;
    }

    public int getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(int kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFakePassword() {
        return fakePassword;
    }

    public void setFakePassword(String fakePassword) {
        this.fakePassword = fakePassword;
    }

    public int getSilmeDurum() {
        return silmeDurum;
    }

    public void setSilmeDurum(int silmeDurum) {
        this.silmeDurum = silmeDurum;
    }

    public int getSilmeGun() {
        return silmeGun;
    }

    public void setSilmeGun(int silmeGun) {
        this.silmeGun = silmeGun;
    }

    public int getMetinBoyutu() {
        return metinBoyutu;
    }

    public void setMetinBoyutu(int metinBoyutu) {
        this.metinBoyutu = metinBoyutu;
    }
}
