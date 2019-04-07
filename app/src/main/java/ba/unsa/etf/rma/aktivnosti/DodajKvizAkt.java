package ba.unsa.etf.rma.aktivnosti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.Pitanje;

public class DodajKvizAkt extends AppCompatActivity {

    public interface DodajKvizAktInterface {
        //        void dodajKategoriju(Kategorija novaKategorija);
//        void dodajPitanje(Pitanje novoPitanje);
        void dodajKviz(Kviz noviKviz);
    }

    DodajKvizAktInterface dodajKvizAktInterface;

    ///////////////////////

    ArrayList<Pitanje> dodanaPitanja = new ArrayList<>();
    ArrayList<Pitanje> dajMogucaPitanja = new ArrayList<>();
    ArrayAdapter adapterDodanaPitanja;
    ArrayAdapter adapterMogucaPitanja;
    ArrayList<Kviz> kvizovi = new ArrayList<>();
    public static Pitanje zaDodatno = new Pitanje("Dodaj Pitanje",null,null,null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent vrijednosti = getIntent();
        setContentView(R.layout.activity_dodaj_kviz_akt);
        Kategorija odabranaKategorija = new Kategorija();
        Kviz neki = new Kviz();



        kvizovi = (ArrayList<Kviz>) vrijednosti.getSerializableExtra("SviKvizovi");

//        if(vrijednosti.hasExtra("novoPitanje")) {
//            Pitanje pit = new Pitanje();
//            pit = (Pitanje) vrijednosti.getSerializableExtra("novoPitanje");
//            dodanaPitanja.add(pit);
//            adapterDodanaPitanja.notifyDataSetChanged();
//        }

        //VARIJABLE

        Button btnDodajKviz = (Button) findViewById(R.id.btnDodajKviz);
        final Spinner spKategorije = (Spinner) findViewById(R.id.spKategorije);
        final EditText etNaziv = (EditText) findViewById(R.id.etNaziv);
        final ListView lvDodanaPitanja = (ListView) findViewById(R.id.lvDodanaPitanja);
        final ListView lvMogucaPitanja = (ListView) findViewById(R.id.lvMogucaPitanja);

        //SPINNER

        final ArrayList<Kategorija> kategorije = (ArrayList<Kategorija>) vrijednosti.getSerializableExtra("listaKategorija");
        kategorije.add(new Kategorija("Dodaj kategoriju", "0"));
        ArrayAdapter<Kategorija> adapterKategorije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategorije);
        adapterKategorije.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategorije.setAdapter(adapterKategorije);
        adapterKategorije.notifyDataSetChanged();

        // ZA DODAVANJE NOVE KATEGORIJE
        final Kategorija nova = new Kategorija();

        spKategorije.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DodajKvizAkt.this, DodajKategorijuAkt.class);
                nova.setId(kategorije.get(i).getId());
                nova.setNaziv(kategorije.get(i).getNaziv());
                if (i == kategorije.size() - 1) {
                    DodajKvizAkt.this.startActivity(intent);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        // DodanaPitanjaListView


        // ZA KVIZ




        if (vrijednosti.hasExtra("Novi")) {
            dajMogucaPitanja = dajSvaPitanja(kvizovi);
            for(Pitanje p : dajMogucaPitanja){
                if(p.getNaziv().equals("Obrisi"));
                dajMogucaPitanja.remove(p);
                break;
            }
            adapterMogucaPitanja = new ArrayAdapter<Pitanje>(this, android.R.layout.simple_list_item_1, dajMogucaPitanja);
            lvMogucaPitanja.setAdapter(adapterMogucaPitanja);
            ArrayList<String> pit= new ArrayList<>();
            pit.add("sarma");
            dodanaPitanja.add(0,new Pitanje("Dodaj Pitanje","neko",pit,"neko"));
            adapterDodanaPitanja = new ArrayAdapter<Pitanje>(this, android.R.layout.simple_list_item_1, dodanaPitanja);
            lvDodanaPitanja.setAdapter(adapterDodanaPitanja);
            adapterDodanaPitanja.notifyDataSetChanged();

        } else if (vrijednosti.hasExtra("Edit")) {
            // prosljedjen kviz
            neki = (Kviz) vrijednosti.getSerializableExtra("ZaEdit");
            spKategorije.setSelection(Integer.parseInt(neki.getKategorija().getId()));
            etNaziv.setText(neki.getNaziv());
            // dodana pitanja
            dodanaPitanja = dajDodana(neki);
            adapterDodanaPitanja = new ArrayAdapter<Pitanje>(this, android.R.layout.simple_list_item_1, dodanaPitanja);
            lvDodanaPitanja.setAdapter(adapterDodanaPitanja);
            adapterDodanaPitanja.notifyDataSetChanged();

            dajMogucaPitanja = DajMogucaPitanja(dajSvaPitanja(kvizovi), neki);
            adapterMogucaPitanja = new ArrayAdapter<Pitanje>(this, android.R.layout.simple_list_item_1, dajMogucaPitanja);
            lvMogucaPitanja.setAdapter(adapterMogucaPitanja);
            adapterMogucaPitanja.notifyDataSetChanged();
        }

        // FALI JOS PAR USLOVA ZA VALIDACIJU AKO JE SU OBJE CRVENE ITD

        btnDodajKviz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean proslo = false;
                Kviz novi = new Kviz();
                novi.setNaziv(etNaziv.getText().toString());
                novi.setKategorija(nova);
                novi.setPitanja(dodanaPitanja);
                if (isEmpty(etNaziv) || etNaziv.getText().equals("Naziv kviza - INPUT")) {
                    etNaziv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else if (dodanaPitanja.size() == 1) {
                    lvDodanaPitanja.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else{
                    etNaziv.setBackgroundColor(getResources().getColor(R.color.bijela));
                    lvDodanaPitanja.setBackgroundColor(getResources().getColor(R.color.bijela));
                    proslo =true;
                }
                for(Kviz k : kvizovi){
                    if(k.getNaziv().equals(etNaziv.getText().toString())){
                        etNaziv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        proslo=false;
                    }
                }
                if(proslo==true){
                    kvizovi.add(novi);
                    novi.obrisiPitanje("Dodaj Pitanje");
                    Intent vratiKvizNaPocetnu =new Intent(DodajKvizAkt.this,KvizoviAkt.class);
                    String odNovog = novi.getKategorija().getId();
                    vratiKvizNaPocetnu.putExtra("sarma",true);
                    vratiKvizNaPocetnu.putExtra("id",odNovog);
                    vratiKvizNaPocetnu.putExtra("kvizovi",kvizovi);
                    DodajKvizAkt.this.startActivity(vratiKvizNaPocetnu);
                }

            }

        });

        lvMogucaPitanja.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lvDodanaPitanja.setBackgroundColor(getResources().getColor(R.color.bijela));
                dodanaPitanja.add(dajMogucaPitanja.get(i));
                dajMogucaPitanja.remove(i);
                adapterDodanaPitanja.notifyDataSetChanged();
                adapterMogucaPitanja.notifyDataSetChanged();

            }
        });




        lvDodanaPitanja.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(i!=0) {
                    dajMogucaPitanja.add(dodanaPitanja.get(i));
                    dodanaPitanja.remove(i);
                    adapterDodanaPitanja.notifyDataSetChanged();
                    adapterMogucaPitanja.notifyDataSetChanged();
                }
                else{
                    Intent intent = new Intent(DodajKvizAkt.this,DodajPitanjeAkt.class);
                    intent.putExtra("PitanjaDodana",dodanaPitanja);
                    intent.putExtra("PitanjaMoguca",dajMogucaPitanja);
                    intent.putExtra("kati",kategorije);
                    DodajKvizAkt.this.startActivity(intent);
                }
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }

    public ArrayList<Pitanje> dajSvaPitanja(ArrayList<Kviz> kvizovi) {
        ArrayList<Pitanje> svaPitanja = new ArrayList<>();
        for (int i = 0; i < kvizovi.size(); i++) {
            for (int j = 0; j < kvizovi.get(i).getPitanja().size(); j++) {
                svaPitanja.add(kvizovi.get(i).getPitanja().get(j));
            }

        }
        return svaPitanja;
    }



    public ArrayList<Pitanje> DajMogucaPitanja(ArrayList<Pitanje> pitanja,Kviz k){
        ArrayList<Pitanje> pitanjaSva = pitanja;
        ArrayList<Pitanje> pitanjaIzKviza = k.getPitanja();

        for(int i = 0; i<pitanjaIzKviza.size();i++){
            for(int j = 0;j<pitanjaSva.size();j++ ){
                if(pitanjaIzKviza.get((i)).getNaziv().equals(pitanjaSva.get(j).getNaziv())) {
                    pitanjaSva.remove(j);
                }
            }

        }
        return pitanjaSva;
    }


    public ArrayList<Pitanje> dajDodana( Kviz k) {
        ArrayList<Pitanje> svaPitanja = new ArrayList<>();
        Pitanje pitanje =new Pitanje(zaDodatno);
        svaPitanja.add(zaDodatno);
        for (int i = 0; i < k.getPitanja().size(); i++) {
            svaPitanja.add(k.getPitanja().get(i));
        }
        return svaPitanja;
    }

}




