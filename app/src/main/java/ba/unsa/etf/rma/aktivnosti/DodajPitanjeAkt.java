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
    String naziv = new String();
    Pitanje pitanje= new Pitanje();
    ArrayList<Kategorija> kati = new ArrayList<>();
    final Intent intent = new Intent();
    ArrayList<Kategorija> kategorije=(ArrayList<Kategorija>) intent.getSerializableExtra("kate");
    final ArrayList<Pitanje> dodana = (ArrayList<Pitanje>) intent.getSerializableExtra("PitanjaDodana");
    final ArrayList<Pitanje> moguca = (ArrayList<Pitanje>) intent.getSerializableExtra("PitanjaMoguca");
    final String tekstKviza= (String) intent.getStringExtra("nazivKv");
    final ArrayList<Kviz> kvizovi= (ArrayList<Kviz>) intent.getSerializableExtra("kvizoviSvi");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_pitanje_akt);



//        final ArrayList<Pitanje> dodana = (ArrayList<Pitanje>) intent.getSerializableExtra("PitanjaDodana");
//        final ArrayList<Pitanje> moguca = (ArrayList<Pitanje>) intent.getSerializableExtra("PitanjaMoguca");
//        final String tekstKviza= (String) intent.getStringExtra("nazivKv");
//        final ArrayList<Kviz> kvizovi= (ArrayList<Kviz>) intent.getSerializableExtra("kvizoviSvi");



        final ListView lvOdgovori = (ListView) findViewById(R.id.lvOdgovori);
        EditText etNaziv = (EditText) findViewById(R.id.etNaziv);
        final EditText etOdgovor = (EditText) findViewById(R.id.etOdgovor);
        final Button btnDodajOdgovor =(Button) findViewById(R.id.btnDodajOdgovor);
        final Button btnDodajTacan =(Button) findViewById(R.id.btnDodajTacan);
        final Button btnDodajPitanje =(Button) findViewById(R.id.btnDodajPitanje);
        final ArrayList<String> odgovori = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,odgovori);
        lvOdgovori.setAdapter(adapter);


        naziv = etNaziv.getText().toString();
        final String odgovor = etOdgovor.getText().toString();

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
//
//                Kviz kvizSaNovimPitanjem = new Kviz();
//                kvizSaNovimPitanjem = (Kviz) vrijednosti.getSerializableExtra("kvizNovoPitanje");
//                etNaziv.setText(kvizSaNovimPitanjem.getNaziv());
//                spKategorije.setSelection(Integer.parseInt(kvizSaNovimPitanjem.getKategorija().getId()));
//                adapterKategorije.notifyDataSetChanged();
//                adapterDodanaPitanja.notifyDataSetChanged();
//                adapterMogucaPitanja.notifyDataSetChanged();

//                pitanje.setNaziv(naziv);
//                pitanje.setTekstPitanja("vra");
//                pitanje.setOdgovori(odgovori);
//                pitanje.setTacan(tacanOdgovor);
//                dodana.add(pitanje);

//                Intent zuja = new Intent(DodajPitanjeAkt.this,DodajKvizAkt.class);
//                intent.putExtra("ima",true);
//                Pitanje p = new Pitanje();
//                p.setTacan(tacanOdgovor);
//                p.setNaziv(naziv);
//                p.setTekstPitanja("");
//                p.setOdgovori(odgovori);
//              //  dodana.add(p);
//                  intent.putExtra("Pitanje",true);
//                  intent.putExtra("Dod",dodana);
//                  intent.putExtra("Mog",moguca);
//                  intent.putExtra("Kvi",kvizovi);
//                  intent.putExtra("Kat",kategorije);;
//                  intent.putExtra("KviNaziv",tekstKviza);
//                DodajPitanjeAkt.this.startActivity(zuja);
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
