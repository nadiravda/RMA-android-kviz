package ba.unsa.etf.rma.aktivnosti;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Baza;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.KvizContainer;
import ba.unsa.etf.rma.klase.Pitanje;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static ba.unsa.etf.rma.klase.Baza.TABLE_KVIZ;

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
    Boolean koji = false;
    final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 20;
    ArrayList<String> dogadjaj = new ArrayList<>();
    ArrayList<Date> datumi = new ArrayList<>();
    Date datum;
    Baza db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        db.getWritableDatabase().delete(TABLE_KVIZ,null,null);
//        db = new Baza(this);
//        ArrayList<Pitanje> a = new ArrayList<>();
//         ArrayList<String> k = new ArrayList<>();
//         k.add("as");
//        a.add(new Pitanje("vra","",k,"0"));
//        Kategorija o = new Kategorija("nov","0");
//        db.dodajKviz(new Kviz("sarm1a",a,o));
//        Kategorija o1 = new Kategorija("nov","0");
//        db.dodajKategoriju(o);
//        kategorije = db.getKategorije();
//        kvizovi = db.getKvizovi();
//        pomocni = kvizovi;

        // new KreirajDokumentTask().execute("proba");

        // ID
        spPostojeceKategorije = (Spinner) findViewById(R.id.spPostojeceKategorije);
        lvKvizovi = (ListView) findViewById(R.id.lvKvizovi);

        if (ContextCompat.checkSelfPermission(KvizoviAkt.this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(KvizoviAkt.this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
            else{
                DajEventove();
        }


        System.out.println("KOliko eevntova " + dogadjaj.size());

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

                    if(kvizovi.get(i).getPitanja() != null && i!=0) {
                        int minuta = kvizovi.get(i).getPitanja().size();
                        if (minuta % 2 != 0) {
                            minuta++;
                        }
                        minuta = minuta / 2;

                        datum = new Date();
                        datum.setTime(DajVrijeme() + minuta * 1000 * 60);
                        Integer k = DaLiSePoklapa(datum);
                        if(!k.equals("uredu je") && k>0){
                           AlertDialog alertDialog = new AlertDialog.Builder(KvizoviAkt.this).create();
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setMessage("Imate dogadjaj za " + k.toString() + " minute");
                            alertDialog.show();
                        }
                        else {
                            if (i != 0) {
                                Intent intent = new Intent(KvizoviAkt.this, IgrajKvizAkt.class);
                                intent.putExtra("kategorije", kategorije);
                                intent.putExtra("kvizovi", kvizovi);
                                Kviz novi = new Kviz();
                                if (!koji) {
                                    novi = kvizovi.get(i);
                                } else {
                                    novi = pomocni.get(i);
                                }
                                intent.putExtra("kviz", novi);

                                KvizoviAkt.this.startActivity(intent);
                            }
                        }
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
            koji = false;

        } else {
            koji = true;
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


    public void DajEventove() {
        final String[] INSTANCE_PROJECTION = new String[] {
                CalendarContract.Instances.EVENT_ID,      // 0
                CalendarContract.Instances.BEGIN,         // 1
                CalendarContract.Instances.TITLE,          // 2
                CalendarContract.Instances.ORGANIZER
        };

        // The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_BEGIN_INDEX = 1;
        final int PROJECTION_TITLE_INDEX = 2;
        final int PROJECTION_ORGANIZER_INDEX = 3;

        // Specify the date range you want to search for recurring event instances
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2019, 2, 23, 8, 0);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2050, 1, 24, 8, 0);
        long endMillis = endTime.getTimeInMillis();


        // The ID of the recurring event whose instances you are searching for in the Instances table
        String selection = CalendarContract.Instances.EVENT_ID + " = ?";
        String[] selectionArgs = new String[] {"207"};

        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

        // Submit the query
        Cursor cur =  getContentResolver().query(builder.build(), INSTANCE_PROJECTION, null, null, null);



        while (cur.moveToNext()) {

            // Get the field values
            long eventID = cur.getLong(PROJECTION_ID_INDEX);
            long beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
            String title = cur.getString(PROJECTION_TITLE_INDEX);
            String organizer = cur.getString(PROJECTION_ORGANIZER_INDEX);

            // Do something with the values.
            Log.i("Calendar", "Event:  " + title);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Log.i("Calendar", "Date: " + formatter.format(calendar.getTime()));
            datumi.add(calendar.getTime());
            dogadjaj.add(String.format("Event: %s\nOrganizer: %s\nDate: %s", title, organizer, formatter.format(calendar.getTime())));
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS : {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DajEventove();

                } else {

                }
                return;
            }

        }
    }

    public long DajVrijeme(){
        Date d = new Date();
        d.setTime(System.currentTimeMillis());

        if(d.getMinutes() > 0){
            d.setSeconds(0);
            d.setMinutes(d.getMinutes()+1);
        }
        return d.getTime();
    }

    public int DaLiSePoklapa(Date d){
        int vrijeme = 0;
        for(int i =0; i<datumi.size();i++){
            if(d.getYear() == datumi.get(i).getYear() && d.getMonth() == datumi.get(i).getMonth() && d.getDay() == datumi.get(i).getDay() ){
                if( d.getTime()>=datumi.get(i).getTime() && datumi.get(i).getMinutes() - new Date(DajVrijemee()).getMinutes()>0){
                  vrijeme  = datumi.get(i).getMinutes() - new Date(DajVrijemee()).getMinutes();
                    return vrijeme;
                }
            }
        }
        return 0;
    }

    public long DajVrijemee(){
        Date d = new Date();
        d.setTime(System.currentTimeMillis());

        if(d.getMinutes() > 0){
            d.setSeconds(0);
            d.setMinutes(d.getMinutes());
        }
        return d.getTime();
    }

    }




