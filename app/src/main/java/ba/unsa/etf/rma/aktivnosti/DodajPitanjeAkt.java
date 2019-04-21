package ba.unsa.etf.rma.aktivnosti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.Pitanje;


public class DodajPitanjeAkt extends AppCompatActivity {


    ////////////////////

    String tacanOdgovor=new String();
    ArrayList<Kategorija> kategorije = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_pitanje_akt);
        Intent eks = getIntent();

        String a =  eks.getStringExtra("sarma");

        System.out.println(a);
        final ArrayList<Kategorija> kategorije = (ArrayList<Kategorija>) eks.getSerializableExtra("kate");
        final ArrayList<Pitanje> dodana = (ArrayList<Pitanje>) eks.getSerializableExtra("dodana");
        final ArrayList<Pitanje> moguca = (ArrayList<Pitanje>) eks.getSerializableExtra("moguca");
        final ArrayList<Kviz> kvizovi = (ArrayList<Kviz>) eks.getSerializableExtra("kvizovi");
        final String naziv = eks.getStringExtra("naziv");







        final ListView lvOdgovori = (ListView) findViewById(R.id.lvOdgovori);
        final EditText etNaziv = (EditText) findViewById(R.id.etNaziv);
        final EditText etOdgovor = (EditText) findViewById(R.id.etOdgovor);
        final Button btnDodajOdgovor =(Button) findViewById(R.id.btnDodajOdgovor);
        final Button btnDodajTacan =(Button) findViewById(R.id.btnDodajTacan);
        final Button btnDodajPitanje =(Button) findViewById(R.id.btnDodajPitanje);
        final ArrayList<String> odgovori = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,odgovori);
        lvOdgovori.setAdapter(adapter);




        btnDodajOdgovor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                odgovori.add(etOdgovor.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });

        btnDodajTacan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String tacni = etOdgovor.getText().toString();
               odgovori.add(0,tacni);
               tacanOdgovor=tacni;
               lvOdgovori.getChildAt(0).setBackgroundResource(R.color.zelena);
               adapter.notifyDataSetChanged();


            }
        });

        lvOdgovori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                odgovori.remove(i);
                adapter.notifyDataSetChanged();
            }
        });


        btnDodajPitanje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nazivPitanja = etNaziv.getText().toString();
                Pitanje p = new Pitanje();
                p.setTacan(tacanOdgovor);
                p.setNaziv(nazivPitanja);
                p.setTekstPitanja("");
                p.setOdgovori(odgovori);

                Intent intent = new Intent(DodajPitanjeAkt.this,DodajKvizAkt.class);
                dodana.add(p);
                intent.putExtra("listaKategorija",kategorije);
                intent.putExtra("SviKvizovi",kvizovi);
                intent.putExtra("dodana",dodana);
                intent.putExtra("moguca",moguca);
                intent.putExtra("ime",naziv);
                intent.putExtra("novoPitanje",true);
                DodajPitanjeAkt.this.startActivity(intent);
            }
        });




        //ZA VALIDACIJU
//        boolean imaVecPitanje = false;
//
//        for(Pitanje p: dodana){
//            if(p.getNaziv().equals(text)){
//                imaVecPitanje = true;
//                break;
//            }
//        }
//        for(Pitanje p: moguca){
//            if(p.getNaziv().equals(text)){
//                imaVecPitanje = true;
//                break;
//            }
//        }


//        adapterMogucaPitanja = new ArrayAdapter<Pitanje>(this, android.R.layout.simple_list_item_1, dajMogucaPitanja);
//        lvMogucaPitanja.setAdapter(adapterMogucaPitanja);
//        adapterMogucaPitanja.notifyDataSetChanged();





    }
}
