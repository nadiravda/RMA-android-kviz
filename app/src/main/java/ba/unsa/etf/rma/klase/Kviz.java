package ba.unsa.etf.rma.klase;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Kviz implements Serializable{
    private String naziv;
    private ArrayList<Pitanje> pitanja;
    private Kategorija kategorija;

    public Kviz(String naziv, ArrayList<Pitanje> pitanja, Kategorija kategorija){
        this.pitanja = new ArrayList<>();
        this.naziv = naziv;
        this.pitanja = pitanja;
        this.kategorija = kategorija;
    }
    public Kviz(){}

    @Override
    public String toString(){
        return naziv;
    }
    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList<Pitanje> getPitanja() {
        return pitanja;
    }

    public void setPitanja(ArrayList<Pitanje> pitanja) {
        this.pitanja = pitanja;
    }

    public void obrisiPitanje(Pitanje pitanjeP){
        pitanja.remove(pitanjeP);
    }

    public void obrisiPitanje(String stringP){
        for(Pitanje p : pitanja){
            if(p.getNaziv().equals(stringP)){
                pitanja.remove(p);
                break;
            }
        }
    }

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }

    public boolean dodajPitanje(Pitanje p){
        if(!this.pitanja.contains(p))
        {
        pitanja.add(p); return true;
            }
        return false;
    }

//    @Override
//    protected Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(naziv);
//    }
//
//    protected Kviz(Parcel in) {
//       naziv = in.readString();
//
//    }
//
//    public static final Creator<Kviz> CREATOR = new Creator<Kviz>() {
//        @Override
//        public Kviz createFromParcel(Parcel in) {
//            return new Kviz(in);
//        }
//
//        @Override
//        public Kviz[] newArray(int size) {
//            return new Kviz[size];
//        }
//    };


}
