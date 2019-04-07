package ba.unsa.etf.rma.klase;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Kategorija implements Serializable
{
    private String naziv;
    private String id;

    @Override
    public String toString(){
        return naziv;
    }
    public Kategorija(){}

    public Kategorija(String naziv, String id){
        this.naziv = naziv;
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    protected Kategorija(Parcel in) {
//        naziv = in.readString();
//        id = in.readString();
//    }

//    public static final Creator<Kategorija> CREATOR = new Creator<Kategorija>() {
//        @Override
//        public Kategorija createFromParcel(Parcel in) {
//            return new Kategorija(in);
//        }
//
//        @Override
//        public Kategorija[] newArray(int size) {
//            return new Kategorija[size];
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
//        parcel.writeString(id);
//    }

}


