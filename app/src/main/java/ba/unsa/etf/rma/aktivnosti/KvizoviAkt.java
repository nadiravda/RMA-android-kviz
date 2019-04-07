package ba.unsa.etf.rma.aktivnosti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.Pitanje;

public class KvizoviAkt extends AppCompatActivity  {



    ///////////////////////////////

    Spinner spPostojeceKategorije;
    ListView lvKvizovi;
    ArrayList<Kviz> kvizovi = new ArrayList<>();
    ArrayList<Kategorija> kategorije = new ArrayList<Kategorija>();
    ArrayList<Pitanje> svaPitanja = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ID
        spPostojeceKategorije = (Spinner) findViewById(R.id.spPostojeceKategorije);
        lvKvizovi = (ListView) findViewById(R.id.lvKvizovi);

        // SPINNER
        ArrayAdapter<Kategorija> adapterKategorije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategorije);
        adapterKategorije.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPostojeceKategorije.setAdapter(adapterKategorije);
        kategorije.add(new Kategorija("Svi","0"));
        kategorije.add(new Kategorija("Jezici", "1"));
        kategorije.add(new Kategorija("Zivotinje", "2"));
        kategorije.add(new Kategorija("Tehnologija", "3"));
        adapterKategorije.notifyDataSetChanged();


       //  INTENTI PRIMANJE
        Intent glavni = getIntent();
        if(glavni.hasExtra("kvizovi")){
            String odNovog =(String) glavni.getStringExtra("id");
            Integer id = Integer.parseInt(odNovog);
            kvizovi =(ArrayList<Kviz>) glavni.getSerializableExtra("kvizovi");
            DajTrazenuKategoriju(id);

        }
        if(glavni.hasExtra("dodanaKategorija")){
            kategorije.add((Kategorija) glavni.getParcelableExtra("novaKategorija"));
        }

        // ZA FILTRIRANJE
        spPostojeceKategorije.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i >=0 && i<kategorije.size()){
                    DajTrazenuKategoriju(i);
                }
                else{
                    Toast.makeText(KvizoviAkt.this,"Nema ove kategorije",Toast.LENGTH_SHORT);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });





        final ArrayList<String> odg = new ArrayList<>();
        odg.add("Jeste1");
        odg.add("Jeste2");
        odg.add("Jeste3");
        svaPitanja.add(new Pitanje("Obrisi", "koje", odg, "Jeste1"));
        svaPitanja.add(new Pitanje("Drugo", "koje2", odg, "Jeste2"));
        svaPitanja.add(new Pitanje("Trece", "koje3", odg, "Jeste3"));
//        svaPitanja.add(new Pitanje("Trece", "koje3", odg, "Jeste3"));
        ArrayList<Pitanje> nova = new ArrayList<>();
        nova.add(new Pitanje("cet", "koje", odg, "Jeste1"));
        nova.add(new Pitanje("pet", "koje2", odg, "Jeste2"));
//        nova.add(new Pitanje("setsto", "koje3", odg, "Jeste3"));
//        nova.add(new Pitanje("sedmo", "koje3", odg, "Jeste3"));
//
        if(!glavni.hasExtra("sarma")) {
            kvizovi.add(new Kviz("Dodaj Kviz", svaPitanja, kategorije.get(0)));
        }

        kvizovi.add(new Kviz("testPitanja",nova,kategorije.get(1)));
        kvizovi.add(new Kviz("test2",svaPitanja,kategorije.get(1)));
//        kvizovi.add(new Kviz("test2",svaPitanja,kategorije.get(1)));
//        kvizovi.add(new Kviz("test2",null,kategorije.get(2)));
//        kvizovi.add(new Kviz("test3",null,kategorije.get(2)));
//        kvizovi.add(new Kviz("test3",null,kategorije.get(3)));


         //Aktivnost DodajKvizAkt
        lvKvizovi.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    //intent.putExtra("SvaPitanja",svaPitanja);
                    if (i == 0) {
                        Intent intent = new Intent(KvizoviAkt.this, DodajKvizAkt.class);
                        intent.putExtra("SviKvizovi",kvizovi);
                        intent.putExtra("Novi",true);
                        intent.putExtra("listaKategorija",kategorije);
                        KvizoviAkt.this.startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(KvizoviAkt.this, DodajKvizAkt.class);
                        intent.putExtra("Edit",true);
                        Kviz ovaj = kvizovi.get(i);
                        intent.putExtra("ZaEdit",ovaj);
                        intent.putExtra("SviKvizovi",kvizovi);
                        intent.putExtra("listaKategorija",kategorije);
                        KvizoviAkt.this.startActivity(intent);
                    }
            }
        });
    }

    // POMOCNA METODA ZA FILTRIRANJE
    public void DajTrazenuKategoriju(int i){

        ArrayList<Kviz> pomocni = new ArrayList<>();
        ArrayAdapter adapterKvizovi;
        if(i == 0){

            adapterKvizovi = new ArrayAdapter <Kviz>(this,android.R.layout.simple_list_item_1, kvizovi);
            lvKvizovi.setAdapter(adapterKvizovi);
     }
        else{
            for (Kviz k : kvizovi){
                int tmp = Integer.parseInt(k.getKategorija().getId());
                if(tmp == 0){
                    pomocni.add(k);
                    continue;
                }
                if( tmp == i){
                    pomocni.add(k);
                }
            }
            adapterKvizovi = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,pomocni);
            lvKvizovi.setAdapter(adapterKvizovi);
        }
        adapterKvizovi.notifyDataSetChanged();
    }
}

