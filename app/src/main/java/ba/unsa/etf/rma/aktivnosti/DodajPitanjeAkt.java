package ba.unsa.etf.rma.aktivnosti;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.PitanjaBaza;
import ba.unsa.etf.rma.klase.Pitanje;
import com.google.common.collect.Lists;

import static ba.unsa.etf.rma.aktivnosti.KvizoviAkt.kvizovi;


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
        final ArrayList<Pitanje> dodana = (ArrayList<Pitanje>) eks.getSerializableExtra("dodana");
        final ArrayList<Pitanje> moguca = (ArrayList<Pitanje>) eks.getSerializableExtra("moguca");
       // final ArrayList<Kviz> kvizovi = (ArrayList<Kviz>) eks.getSerializableExtra("kvizovi");
        final String naziv = eks.getStringExtra("naziv");

        final ListView lvOdgovori = (ListView) findViewById(R.id.lvOdgovori);
        final EditText etNaziv = (EditText) findViewById(R.id.etNazivk);
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
               adapter.notifyDataSetChanged();
               if(odgovori.size() > 1) {
                   lvOdgovori.getChildAt(0).setBackgroundResource(R.color.zelena);
               }
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
                boolean a = DaLiImaPitanje(p);
                if (a) {
                    Intent intent = new Intent(DodajPitanjeAkt.this, DodajKvizAkt.class);
                    new SetPitanjeBaza().execute(p);
                    dodana.add(p);
                    intent.putExtra("listaKategorija", kategorije);
                    intent.putExtra("SviKvizovi", kvizovi);
                    intent.putExtra("dodana", dodana);
                    intent.putExtra("moguca", moguca);
                    intent.putExtra("ime", naziv);
                    intent.putExtra("novoPitanje", true);
                    DodajPitanjeAkt.this.startActivity(intent);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(DodajPitanjeAkt.this).create();
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setMessage("Uneseno pitanje već postoji!");
                    alertDialog.show();
                }
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

    public Boolean DaLiImaPitanje(Pitanje s){
        for(int i = 0;i<kvizovi.size(); i++){
            for(int j = 0; j<kvizovi.get(i).getPitanja().size();j++){
                if(kvizovi.get(i).getPitanja().get(j).getNaziv().equals(s.getNaziv())){
                    return false;
                }
            }
        }
        return true;
    }

    public class SetPitanjeBaza extends AsyncTask<Pitanje, Void, Void> {
        GoogleCredential credentials;

        @Override
        protected Void doInBackground(Pitanje... params) {
            try {
                Pitanje novo = params[0];

                InputStream tajnaStream = getResources().openRawResource(R.raw.secret);
                credentials = GoogleCredential.fromStream(tajnaStream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/datastore"));
                credentials.refreshToken();

                String TOKEN = credentials.getAccessToken();

                String url = "https://firestore.googleapis.com/v1/projects/rmaspirala3/databases/(default)/documents/Pitanja?access_token=";
                URL urlObj = new URL(url + URLEncoder.encode(TOKEN, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection)urlObj.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                String string = "{\"fields\": {\"indexTacnog\": {\"integerValue\": \"" + novo.getOdgovori().indexOf(novo.getTacan()) +"\"},\"naziv\": {\"stringValue\": \"" + novo.getNaziv() + "\"},\"odgovori\": {\"arrayValue\": {\"values\": [ ";
                for(int i = 0; i < novo.getOdgovori().size(); i++){

                    if(i != novo.getOdgovori().size()-1) {
                        string += "{\"stringValue\": \"" + novo.getOdgovori().get(i) + "\"},";
                    }
                    else {
                        string += "{\"stringValue\": \"" + novo.getOdgovori().get(i) + "\"}";
                        }
                    }

                string += "]}}}}";

                try(OutputStream os = conn.getOutputStream()){
                    byte[] input = string.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                InputStream odgovor = conn.getInputStream();
                try(BufferedReader br = new BufferedReader(new InputStreamReader(odgovor, "utf-8"))){
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while((responseLine = br.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                    Log.d("ODGOVOR", response.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
