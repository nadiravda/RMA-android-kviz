package ba.unsa.etf.rma.aktivnosti;

import java.util.Map;

public class RangKlasa {

    private String naziv;
    private Integer brojTacnih;
    private String ImeKviza;
    private Double procenatTacnih;

    public RangKlasa(String naziv, Integer brojTacnih, String imeKviza, Double procenatTacnih) {
        this.naziv = naziv;
        this.brojTacnih = brojTacnih;
        ImeKviza = imeKviza;
        this.procenatTacnih = procenatTacnih;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getBrojTacnih() {
        return brojTacnih;
    }

    public void setBrojTacnih(Integer brojTacnih) {
        this.brojTacnih = brojTacnih;
    }

    public String getImeKviza() {
        return ImeKviza;
    }

    public void setImeKviza(String imeKviza) {
        ImeKviza = imeKviza;
    }

    public Double getProcenatTacnih() {
        return procenatTacnih;
    }

    public void setProcenatTacnih(Double procenatTacnih) {
        this.procenatTacnih = procenatTacnih;
    }
}
