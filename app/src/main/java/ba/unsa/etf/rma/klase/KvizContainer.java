package ba.unsa.etf.rma.klase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KvizContainer implements Serializable {

    private List<Kviz> kvizList;
    private List<Pitanje> pitanjeList;
    private List<Kategorija> kategorijaList;

    public KvizContainer() {
        this.kvizList = new ArrayList<>();
        this.pitanjeList = new ArrayList<>();
        this.kategorijaList =new ArrayList<>();
    }

    public List<Kviz> getKvizList() {
        return kvizList;
    }

    public List<Pitanje> getPitanjeList() {
        return pitanjeList;
    }

    public List<Kategorija> getKategorijaList() {
        return kategorijaList;
    }

    public void dodajKviz(final Kviz kviz) {
        kvizList.add(kviz);
    }

    public void dodajPitanje(final Pitanje pitanje) {
        pitanjeList.add(pitanje);
    }

    public void dodajKategorija(final Kategorija kategorija) {
        kategorijaList.add(kategorija);
    }

    // biznis logika ovdje

}
