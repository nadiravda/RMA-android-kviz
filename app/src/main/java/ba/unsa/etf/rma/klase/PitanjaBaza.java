package ba.unsa.etf.rma.klase;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.aktivnosti.DodajKvizAkt;

public class PitanjaBaza extends AsyncTask<String,Void,String> {

    public interface Zavrseno {

        public Void onDone( ArrayList<Pitanje> pitanja);

    }

    private Context context;

    ArrayList<Pitanje> pitanjaBaza;

    private Zavrseno pozivatelj;
    public PitanjaBaza(Context current,Zavrseno z){this.context = current;pitanjaBaza = new ArrayList<>(); pozivatelj = z; }

    public PitanjaBaza(Context current){
        this.context = current;
    }

    public void Kontekst(Context current){
        this.context = current;
    }

    public ArrayList<Pitanje> getPitanja() {
        return pitanjaBaza;
    }

    public void setPitanja(ArrayList<Pitanje> pitanja) {
        this.pitanjaBaza = pitanja;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pozivatelj.onDone(pitanjaBaza);
    }

    @Override
    protected String doInBackground(String... params) {
        GoogleCredential credentials;


        try {

            InputStream tajnaStream = context.getResources().openRawResource(R.raw.secret);
            credentials = GoogleCredential.fromStream(tajnaStream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/datastore"));
            credentials.refreshToken();
            String TOKEN = credentials.getAccessToken();

            String url = "https://firestore.googleapis.com/v1/projects/rmaspirala3/databases/(default)/documents/Pitanja?access_token=";
            URL urlObj = new URL(url + URLEncoder.encode(TOKEN, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

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
                    for (int j = 0; j < ODGNiz.length(); j++) {
                        JSONObject redom = ODGNiz.getJSONObject(j);
                        nagovori.add(redom.getString("stringValue"));
                    }
                    JSONObject nazivTacnog = p.getJSONObject("indexTacnog");
                    int imeTacnog = nazivTacnog.getInt("integerValue");

                    pitanjaBaza.add(new Pitanje(imePitanja, "", nagovori, nagovori.get(imeTacnog)));

                    System.out.println(" ime " + imePitanja + "prvi odg " + nagovori.get(0) + "tacan" + imeTacnog);
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
}

