package ba.unsa.etf.rma.aktivnosti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import ba.unsa.etf.rma.klase.KvizContainer;
import ba.unsa.etf.rma.klase.Pitanje;

public class KvizoviAkt extends AppCompatActivity  {

    ///////////////////////////////

    Spinner spPostojeceKategorije;
    ListView lvKvizovi;
    public static ArrayList<Kviz> kvizovi = new ArrayList<>();
    public static ArrayList<Kategorija> kategorije = new ArrayList<Kategorija>();
    ArrayList<Pitanje> svaPitanja = new ArrayList<>();

//    private KvizContainer kvizContainer


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
        if(kategorije.size()==0) {
            kategorije.add(new Kategorija("Svi", "0"));
            kategorije.add(new Kategorija("Jezici", "1"));
            kategorije.add(new Kategorija("Zivotinje", "2"));
            kategorije.add(new Kategorija("Tehnologija", "3"));
            adapterKategorije.notifyDataSetChanged();
        }


       //  INTENTI PRIMANJE
        Intent glavni = getIntent();
        if(glavni.hasExtra("dodanaKategorija")){
            kategorije = (ArrayList<Kategorija>) glavni.getSerializableExtra("dodanaKategorija");
            adapterKategorije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategorije);
            adapterKategorije.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPostojeceKategorije.setAdapter(adapterKategorije);
        }
        if(glavni.hasExtra("kvizovi")){
            String odNovog =(String) glavni.getStringExtra("id");
            Integer id = Integer.parseInt(odNovog);
            kvizovi =(ArrayList<Kviz>) glavni.getSerializableExtra("kvizovi");
            DajTrazenuKategoriju(id);

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
        nova.add(new Pitanje("setsto", "koje3", odg, "Jeste3"));
        nova.add(new Pitanje("sedmo", "koje3", odg, "Jeste3"));
//
        if(kvizovi.size()==0) {
            kvizovi.add(new Kviz("Dodaj Kviz", svaPitanja, kategorije.get(0)));

        }




        // Popravit nekad

//        for(Kategorija k : kategorije){
//            if(k.getNaziv().equals("Dodaj kategoriju")) {
//                kategorije.remove(k);
//                break;
//            }
//        }


        lvKvizovi.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(i != 0) {
                    Intent intent = new Intent(KvizoviAkt.this, IgrajKvizAkt.class);
                    intent.putExtra("kategorije", kategorije);
                    intent.putExtra("kvizovi", kvizovi);
                    Kviz novi = new Kviz();
                    novi = kvizovi.get(i);
                    intent.putExtra("kviz", novi);
                    KvizoviAkt.this.startActivity(intent);
                }
            }
        });

        //  Aktivnost DodajKvizAkt
        lvKvizovi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {

                Intent intent = new Intent(KvizoviAkt.this, DodajKvizAkt.class);
                //intent.putExtra("SvaPitanja",svaPitanja);
                if (pos == 0) {
                    intent.putExtra("SviKvizovi",kvizovi);
                    intent.putExtra("Novi",true);
                    intent.putExtra("listaKategorija",kategorije);
                    KvizoviAkt.this.startActivity(intent);
                }
                else{
                    intent.putExtra("Edit",true);
                    Kviz ovaj = kvizovi.get(pos);
                    intent.putExtra("ZaEdit",ovaj);
                    intent.putExtra("SviKvizovi",kvizovi);
                    intent.putExtra("listaKategorija",kategorije);
                    KvizoviAkt.this.startActivity(intent);
                }
                return false;
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

