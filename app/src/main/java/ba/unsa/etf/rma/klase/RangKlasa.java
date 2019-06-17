package ba.unsa.etf.rma.klase;

import java.io.Serializable;

public class RangKlasa implements Serializable{

    public RangKlasa(String nazivKviza, String nazivIgraca, int pozicija, Double procenat) {
        NazivKviza = nazivKviza;
        this.nazivIgraca = nazivIgraca;
        this.pozicija = pozicija;
        this.procenat = procenat;
    }

    String NazivKviza;
    String nazivIgraca;
    int pozicija;

    public String getNazivKviza() {
        return NazivKviza;
    }

    public void setNazivKviza(String nazivKviza) {
        NazivKviza = nazivKviza;
    }

    public String getNazivIgraca() {
        return nazivIgraca;
    }

    public void setNazivIgraca(String nazivIgraca) {
        this.nazivIgraca = nazivIgraca;
    }

    public int getPozicija() {
        return pozicija;
    }

    public void setPozicija(int pozicija) {
        this.pozicija = pozicija;
    }

    public Double getProcenat() {
        return procenat;
    }

    public void setProcenat(Double procenat) {
        this.procenat = procenat;
    }

    Double procenat;

}
