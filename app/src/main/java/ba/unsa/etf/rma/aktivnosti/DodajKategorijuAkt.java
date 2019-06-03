package ba.unsa.etf.rma.aktivnosti;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.Pitanje;
import com.google.common.collect.Lists;
import com.maltaisn.icondialog.Icon;
import com.maltaisn.icondialog.IconDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static ba.unsa.etf.rma.aktivnosti.KvizoviAkt.kategorije;
import static ba.unsa.etf.rma.aktivnosti.KvizoviAkt.kvizovi;

public class DodajKategorijuAkt extends AppCompatActivity implements IconDialog.Callback {

    private Icon[] selectedIcons;
    public EditText etNaziv;
    public Button btnDodajKategoriju;
    ArrayList<Kategorija> kate = new ArrayList<>();

    public ArrayList<Kviz> kvizovi = new ArrayList<>();
    Integer brojKategorija;
    ArrayList<Kategorija> kategorijeZaProvjeru = new ArrayList<>();
    public AlertDialog alertDialog;
    public boolean ikona;



    public  class DodavanjeKategorije extends AsyncTask<Kategorija,Void,Void>{
        @Override
        protected Void doInBackground(Kategorija... strings) {
            GoogleCredential credentials;
            Kategorija nova = strings[0];
            try {
                InputStream tajnaStream = getResources().openRawResource(R.raw.secret);
                credentials = GoogleCredential.fromStream(tajnaStream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/datastore"));
                credentials.refreshToken();
                String TOKEN = credentials.getAccessToken();

                System.out.println("wraaaaaaaaaaaaaaaaaaaaaa " +  TOKEN);
                String url = "https://firestore.googleapis.com/v1/projects/rmaspirala3/databases/(default)/documents/Kategorije?access_token=";
                URL urlObj = new URL(url + URLEncoder.encode(TOKEN, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection)urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                String dokument = "{ \"fields\": {\n" +
                        "        \"idIkonice\": {\n" +
                        "          \"integerValue\": \""+Integer.parseInt(nova.getId())+"\"\n" +
                        "        },\n" +
                        "        \"naziv\": {\n" +
                        "          \"stringValue\": \""+nova.getNaziv()+"\"\n" +
                        "        }\n" +
                        "      }}";

                try(OutputStream os = conn.getOutputStream()){
                    byte[] input = dokument.getBytes("utf-8");
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
    }

    public class DajKategorije extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... params) {
            GoogleCredential credentials;
            ArrayList<Kategorija> sve = new ArrayList<>();

            try{
                InputStream tajnaStream = getResources().openRawResource(R.raw.secret);
                credentials = GoogleCredential.fromStream(tajnaStream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/datastore"));
                credentials.refreshToken();
                String TOKEN = credentials.getAccessToken();

                String url = "https://firestore.googleapis.com/v1/projects/rmaspirala3/databases/(default)/documents/Kategorije?access_token=";
                URL urlObj = new URL(url + URLEncoder.encode(TOKEN, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection)urlObj.openConnection();



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
                        JSONObject kategorije = items.getJSONObject(i).getJSONObject("fields");
                        JSONObject naziv = kategorije.getJSONObject("naziv");
                        JSONObject id = kategorije.getJSONObject("idIkonice");
                        sve.add(new Kategorija(naziv.getString("stringValue"),String.valueOf(id.getInt("integerValue"))));
                        System.out.println(" ime " + naziv.getString("stringValue") + String.valueOf(id.getInt("integerValue")));
                    }
                    kategorijeZaProvjeru = sve;

        } catch (JSONException e) {
                    Log.e("aplikacija", "unexpected JSON exception", e);
                }

                Log.e("odgovor", rezultat);

            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
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
            }
        }
        return sb.toString();
    }

    @Override
    public void onIconDialogIconsSelected(Icon[] icons) {
        selectedIcons = icons;
    }

    public Boolean DaLiImaKategorija(ArrayList<Kategorija> k, String s){
        for (Kategorija x : k)
        {
            if(x.getNaziv().equals(s)) return false;
        }
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_kategoriju_akt);

        new DajKategorije().execute();
       final  IconDialog iconDialog = new IconDialog();
       iconDialog.setSelectedIcons(1,2);

        Button dodajIkonu = (Button) findViewById(R.id.btnDodajIkonu);
        final EditText etIkona = (EditText) findViewById(R.id.etIkona);
        Intent vrijednosti = getIntent();
        etNaziv = (EditText) findViewById(R.id.etNaziv);
        btnDodajKategoriju = (Button) findViewById(R.id.btnDodajKategoriju);



        dodajIkonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iconDialog.setSelectedIcons(selectedIcons);
                iconDialog.show(getSupportFragmentManager(), "icon_dialog");
                ikona = true;

            }
        });
        etIkona.setFocusable(false);

        btnDodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent vrijednosti = getIntent();
                ArrayList<Pitanje> dajMogucaPitanja = (ArrayList<Pitanje>) vrijednosti.getSerializableExtra("moguca");
                ArrayList<Pitanje> dodanaPitanja = (ArrayList<Pitanje>) vrijednosti.getSerializableExtra("dodana");
                String naziv = (String) vrijednosti.getStringExtra("naziv");

                Kategorija nova = new Kategorija();
                nova.setNaziv(etNaziv.getText().toString());
                if(ikona) {
                    nova.setId(Integer.toString(selectedIcons[0].getId()));
                    etIkona.setText(Integer.toString(selectedIcons[0].getId()));
                }


                new DajKategorije().execute();
                boolean a = true;

                if(!ikona){
                    etIkona.setBackgroundColor(getResources().getColor(R.color.crvena));
                }
                else{
                    etIkona.setBackgroundColor(getResources().getColor(R.color.bijela));
                }

                if(etNaziv.getText().toString().equals("") || etNaziv.getText().toString().equals("Naziv Kategorije")){
                    etNaziv.setBackgroundColor(getResources().getColor(R.color.crvena));
                    a=false;
                }
                else{
                    etNaziv.setBackgroundColor(getResources().getColor(R.color.bijela));
                    a= true;
                }

                if(ikona && a) {

                    if (DaLiImaKategorija(kategorijeZaProvjeru, nova.getNaziv())) {
                        new DodavanjeKategorije().execute(nova);
                        kategorije.add(nova);
                        Intent intent = new Intent(DodajKategorijuAkt.this, DodajKvizAkt.class);
                        intent.putExtra("dodana", dodanaPitanja);
                        intent.putExtra("moguca", dajMogucaPitanja);
                        intent.putExtra("naziv", naziv);
                        intent.putExtra("novaKategorija", true);
                        DodajKategorijuAkt.this.startActivity(intent);
                    } else {
                        alertDialog = new AlertDialog.Builder(DodajKategorijuAkt.this).create();
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.setMessage("Unesena kategorija već postoji!");
                        alertDialog.show();
                    }
                }



            }
        });

    }
}
