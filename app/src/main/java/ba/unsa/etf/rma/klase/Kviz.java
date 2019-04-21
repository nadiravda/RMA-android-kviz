package ba.unsa.etf.rma.klase;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Kviz implements Serializable{
    private String naziv;
    private Random random;
    private ArrayList<Pitanje> pitanja;
    private Kategorija kategorija;

    public Kviz(String naziv, ArrayList<Pitanje> pitanja, Kategorija kategorija){
        this.pitanja = new ArrayList<>();
        this.naziv = naziv;
        this.pitanja = pitanja;
        this.kategorija = kategorija;
    }
    public Kviz(){
        pitanja = new ArrayList<>();
        kategorija = new Kategorija();
        random = new Random();
    }

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
    public ArrayList<Pitanje> dajRandomPitanja(){
        ArrayList<Pitanje> novaPitanja = new ArrayList<Pitanje>();
        if(pitanja!=null){
            novaPitanja = pitanja;
        }
        Collections.shuffle(novaPitanja);
        return novaPitanja;
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
            pitanja.add(p);
            return true;
        }
        return false;
    }


}
