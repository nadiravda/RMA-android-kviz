package ba.unsa.etf.rma.aktivnosti;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.KvizContainer;
import ba.unsa.etf.rma.klase.Pitanje;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KvizoviAkt extends AppCompatActivity {


    public class KreirajDokumentTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            GoogleCredential credentials;
            try {
                InputStream tajnaStream = getResources().openRawResource(R.raw.secret);
                credentials = GoogleCredential.fromStream(tajnaStream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/datastore"));
                credentials.refreshToken();
                String TOKEN = credentials.getAccessToken();

                String url = "https://firestore.googleapis.com/v1/projects/rmaspirala3/databases/(default)/documents/Kvizovi?access_token=";
                URL urlObj = new URL(url + URLEncoder.encode(TOKEN, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                String dokument = "{ \"fields\": {\"naziv\": {\"stringValue\": \"novi dokument\"}}}";

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = dokument.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                InputStream odgovor = conn.getInputStream();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(odgovor, "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    Log.d("ODGOVOR", response.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    ///////////////////////////////

    Spinner spPostojeceKategorije;
    ListView lvKvizovi;
    public static ArrayList<Kviz> kvizovi = new ArrayList<>();
    public static ArrayList<Kategorija> kategorije = new ArrayList<Kategorija>();
    ArrayList<Pitanje> svaPitanja = new ArrayList<>();
    ArrayList<Pitanje> baza = new ArrayList<>();
    ArrayList<Kviz> FireKvizovi = new ArrayList<>();
    ArrayList<Kategorija> kategorijeBaza = new ArrayList<>();
    ArrayList<Pitanje> pitanjaBaza = new ArrayList<>();
    ArrayList<Kviz> pomocni = new ArrayList<>();
    public static boolean ucitano = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // new KreirajDokumentTask().execute("proba");

        // ID
        spPostojeceKategorije = (Spinner) findViewById(R.id.spPostojeceKategorije);
        lvKvizovi = (ListView) findViewById(R.id.lvKvizovi);


        if (kategorije.contains(new Kategorija("Dodaj kategoriju", "0"))) {
            kategorije.remove(new Kategorija("Dodaj kategoriju", "0"));
        }

        // Uzimanje iz baze kategorija i kvizova

        try {
            if(ucitano) {
                if (kategorije.size() == 0) {
                    kategorije.add(new Kategorija("Svi", "0"));
                }
                new DajBazaKategorije().execute().get();
                new DajPitanjaIzFirea().execute().get();
                if (kvizovi.size() == 0) {
                    kvizovi.add(new Kviz("Dodaj Kviz", svaPitanja, kategorije.get(0)));
                }
                new DajKvizoveBaza().execute().get();
                ucitano = false;
            }
            pomocni = kvizovi;

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        // SPINNER
        ArrayAdapter<Kategorija> adapterKategorije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategorije);
        adapterKategorije.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPostojeceKategorije.setAdapter(adapterKategorije);
        if (kategorije.size() == 0) {

            kategorije.add(new Kategorija("Jezici", "1"));
            kategorije.add(new Kategorija("Zivotinje", "2"));
            kategorije.add(new Kategorija("Tehnologija", "3"));
            adapterKategorije.notifyDataSetChanged();
        }


        //  INTENTI PRIMANJE
        Intent glavni = getIntent();
        if (glavni.hasExtra("dodanaKategorija")) {

            adapterKategorije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategorije);
            adapterKategorije.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPostojeceKategorije.setAdapter(adapterKategorije);
            adapterKategorije.notifyDataSetChanged();
        }
        if (glavni.hasExtra("kvizovi")) {
            String odNovog = (String) glavni.getStringExtra("id");
            Integer id = Integer.parseInt(odNovog);
           // DajTrazenuKategoriju(id);

        }


        // ZA FILTRIRANJE
        spPostojeceKategorije.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i >= 0 && i < kategorije.size()) {
                    DajTrazenuKategoriju(kategorije.get(i).getNaziv());
                } else {
                    Toast.makeText(KvizoviAkt.this, "Nema ove kategorije", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        final ArrayList<String> odg = new ArrayList<>();
        odg.add("Jeste1");
        svaPitanja.add(new Pitanje("Obrisi", "koje", odg, "Jeste1"));

//        svaPitanja.add(new Pitanje("Trece", "koje3", odg, "Jeste3"));
//        ArrayList<Pitanje> nova = new ArrayList<>();
//        nova.add(new Pitanje("cet", "koje", odg, "Jeste1"));
//        nova.add(new Pitanje("pet", "koje2", odg, "Jeste2"));
//        nova.add(new Pitanje("setsto", "koje3", odg, "Jeste3"));
//        nova.add(new Pitanje("sedmo", "koje3", odg, "Jeste3"));
//
        if (kvizovi.size() == 0) {
            kvizovi.add(new Kviz("Dodaj Kviz", svaPitanja, kategorije.get(0)));

        }


        lvKvizovi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
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
                    intent.putExtra("SviKvizovi", kvizovi);
                    intent.putExtra("Novi", true);
                    intent.putExtra("listaKategorija", kategorije);

                    KvizoviAkt.this.startActivity(intent);
                } else {
                    intent.putExtra("Edit", true);
                    Kviz ovaj = pomocni.get(pos);
                    intent.putExtra("ZaEdit", ovaj);
                    intent.putExtra("SviKvizovi", kvizovi);
                    intent.putExtra("listaKategorija", kategorije);
                    KvizoviAkt.this.startActivity(intent);
                }
                return false;
            }
        });

    }


    // POMOCNA METODA ZA FILTRIRANJE
    public void DajTrazenuKategoriju(String s) {


        ArrayAdapter adapterKvizovi;
        if (s.equals("Svi")) {
            adapterKvizovi = new ArrayAdapter<Kviz>(this, android.R.layout.simple_list_item_1, kvizovi);
            lvKvizovi.setAdapter(adapterKvizovi);

        } else {
            pomocni = new ArrayList<>();
            for (Kviz k : kvizovi) {
               // int tmp = Integer.parseInt(k.getKategorija().getId());
                if (k.getKategorija().getNaziv().equals("Svi")) {
                    pomocni.add(k);
                    continue;
                }
                if (k.getKategorija().getNaziv().equals(s)) {
                    pomocni.add(k);
                }
            }
            adapterKvizovi = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pomocni);
            lvKvizovi.setAdapter(adapterKvizovi);
        }
        adapterKvizovi.notifyDataSetChanged();
    }

    public class DajBazaKategorije extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            GoogleCredential credentials;
            ArrayList<Kategorija> sve = new ArrayList<>();

            try {
                InputStream tajnaStream = getResources().openRawResource(R.raw.secret);
                credentials = GoogleCredential.fromStream(tajnaStream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/datastore"));
                credentials.refreshToken();
                String TOKEN = credentials.getAccessToken();

                String url = "https://firestore.googleapis.com/v1/projects/rmaspirala3/databases/(default)/documents/Kategorije?access_token=";
                URL urlObj = new URL(url + URLEncoder.encode(TOKEN, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();


                conn.setRequestProperty("Content-type", "application/json");
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                InputStream in = new BufferedInputStream(conn.getInputStream());

                String rezultat = convertStreamToString(in);


                try {
                    JSONObject jo = new JSONObject(rezultat);


                    sve = new ArrayList<>();
                    JSONArray items = jo.getJSONArray("documents");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject ka = items.getJSONObject(i).getJSONObject("fields");
                        JSONObject naziv = ka.getJSONObject("naziv");
                        JSONObject id = ka.getJSONObject("idIkonice");
                        kategorije.add(new Kategorija(naziv.getString("stringValue"), String.valueOf(id.getInt("integerValue"))));
                        System.out.println(" ime " + naziv.getString("stringValue") + String.valueOf(id.getInt("integerValue")));
                    }

                } catch (JSONException e) {
                    Log.e("aplikacija", "unexpected JSON exception", e);
                }

                Log.e("odgovor", rezultat);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class DajKvizoveBaza extends AsyncTask<Void, Void, Void> {
        GoogleCredential credentials;

        @Override
        protected Void doInBackground(Void... strings) {
            try {

                InputStream tajnaStream = getResources().openRawResource(R.raw.secret);
                credentials = GoogleCredential.fromStream(tajnaStream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/datastore"));
                credentials.refreshToken();
                String TOKEN = credentials.getAccessToken();
                String url = "https://firestore.googleapis.com/v1/projects/rmaspirala3/databases/(default)/documents/Kvizovi?access_token=";
                URL urlObj = new URL(url + URLEncoder.encode(TOKEN, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                InputStream in = new BufferedInputStream(conn.getInputStream());
                String rezultat = convertStreamToString(in);

                try {
                    JSONArray dokumenti = new JSONObject(rezultat).getJSONArray("documents");
                    for (int i = 0; i < dokumenti.length(); i++) {
                        JSONObject fields = dokumenti.getJSONObject(i).getJSONObject("fields");
                        String naziv = fields.getJSONObject("naziv").getString("stringValue");
                        String idKategorije = fields.getJSONObject("idKategorije").getString("stringValue");

                        ArrayList<String> pitanja = new ArrayList<>();
                        JSONArray nizOdgovori = fields.getJSONObject("pitanja").getJSONObject("arrayValue").getJSONArray("values");

                        for (int j = 0; j < nizOdgovori.length(); j++)
                            pitanja.add(nizOdgovori.getJSONObject(j).getString("stringValue"));

                        kvizovi.add(new Kviz(naziv, dajPitanja(pitanja), dajKategoriju(idKategorije)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public class DajPitanjaIzFirea extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            GoogleCredential credentials;


            try{
                InputStream tajnaStream = getResources().openRawResource(R.raw.secret);
                credentials = GoogleCredential.fromStream(tajnaStream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/datastore"));
                credentials.refreshToken();
                String TOKEN = credentials.getAccessToken();

                String url = "https://firestore.googleapis.com/v1/projects/rmaspirala3/databases/(default)/documents/Pitanja?access_token=";
                URL urlObj = new URL(url + URLEncoder.encode(TOKEN, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection)urlObj.openConnection();

                conn.setRequestProperty("Content-type", "application/json");
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                InputStream in = new BufferedInputStream(conn.getInputStream());

                String rezultat = convertStreamToString(in);


                try {
                    JSONObject jo = new JSONObject(rezultat);

                    JSONArray items = jo.getJSONArray("documents");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject p = items.getJSONObject(i).getJSONObject("fields");

                        JSONObject naziv = p.getJSONObject("naziv");
                        String imePitanja = naziv.getString("stringValue");

                        JSONObject ODG = p.getJSONObject("odgovori");
                        JSONObject ODG1 = ODG.getJSONObject("arrayValue");
                        JSONArray ODGNiz = ODG1.getJSONArray("values");

                        ArrayList<String> nagovori = new ArrayList<>();
                        for(int j = 0; j<ODGNiz.length(); j++){
                            JSONObject redom = ODGNiz.getJSONObject(j);
                            nagovori.add(redom.getString("stringValue"));
                        }
                        JSONObject nazivTacnog = p.getJSONObject("indexTacnog");
                        int imeTacnog = nazivTacnog.getInt("integerValue");

                        pitanjaBaza.add(new Pitanje(imePitanja,"",nagovori,nagovori.get(imeTacnog)));

                        System.out.println(" ime " + imePitanja + "prvi odg " + nagovori.get(0) + "tacan" + imeTacnog);
                    }

                } catch (JSONException e) {
                    Log.e("aplikacija", "unexpected JSON exception", e);
                }

                Log.e("odgovor", rezultat);

            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

        public String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }


        // OVO NE VALJA URADIT

        public ArrayList<Pitanje>  dajPitanja(ArrayList<String> p){
        ArrayList<Pitanje> pk = new ArrayList<>();

        for(int i = 0; i<pitanjaBaza.size(); i++){
            for(int j = 0; j<p.size(); j++){
                if(pitanjaBaza.get(i).getNaziv().equals(p.get(j)))
                    pk.add(pitanjaBaza.get(i));
            }
        }
            return pk;
        }

        // DOOOVDE // odradio hehe

        public Kategorija dajKategoriju(String id){

        Kategorija kate = new Kategorija();

        for(int i = 0; i <kategorije.size(); i++){
            if(kategorije.get(i).getNaziv().equals(id)){
                kate = kategorije.get(i);
            }
        }
        return kate;

        }



    }




