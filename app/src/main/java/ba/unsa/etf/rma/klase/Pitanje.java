package ba.unsa.etf.rma.klase;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Pitanje implements Serializable{
    private String naziv;
    private String tekstPitanja;
    private ArrayList<String> odgovori;
    private String tacan;

    public Pitanje(String naziv, String tekstPitanja, ArrayList<String> odgovori, String tacan) {
        odgovori = new ArrayList<>();
        this.naziv = naziv;
        this.tekstPitanja = tekstPitanja;
        this.odgovori = odgovori;
        this.tacan = tacan;
    }
    public Pitanje(Pitanje p){
        odgovori = new ArrayList<>();
        this.naziv = p.naziv;
        this.tekstPitanja = p.tekstPitanja;
        this.odgovori = p.odgovori;
        this.tacan = p.tacan;
    }

    public Pitanje(){}
    public String getNaziv() {
        return naziv;
    }
    @Override
    public String toString(){
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getTekstPitanja() {
        return tekstPitanja;
    }

    public void setTekstPitanja(String tekstPitanja) {
        this.tekstPitanja = tekstPitanja;
    }

    public ArrayList<String> getOdgovori() {
        return odgovori;
    }

    public void setOdgovori(ArrayList<String> odgovori) {
        this.odgovori = odgovori;
    }

    public String getTacan() {
        return tacan;
    }

    public void setTacan(String tacan) {
        this.tacan = tacan;
    }

    public ArrayList<String> dajRandomOdgovore(){
        Collections.shuffle(odgovori);
        return odgovori;
    }

//    protected Pitanje(Parcel in) {
//        naziv = in.readString();
//        tekstPitanja = in.readString();
//        odgovori = in.readArrayList(ClassLoader.getSystemClassLoader());
//        tacan = in.readString();
//    }
//
//    public static final Creator<Pitanje> CREATOR = new Creator<Pitanje>() {
//        @Override
//        public Pitanje createFromParcel(Parcel in) {
//            return new Pitanje(in);
//        }
//
//        @Override
//        public Pitanje[] newArray(int size) {
//            return new Pitanje[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(naziv);
//        parcel.writeString(tekstPitanja);
//        parcel.writeList(odgovori);
//        parcel.writeString(tacan);
//    }
}

