package ba.unsa.etf.rma.aktivnosti;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.Pitanje;

public class DodajKvizAkt extends AppCompatActivity {

    private static final int EDIT_REQUEST_CODE = 44;





    Kviz kvizImport = new Kviz();
    boolean importovan = false;
    public ArrayList<Kategorija> kategorije;
    public ArrayList<Pitanje> dodanaPitanja = new ArrayList<>();
    public ArrayList<Pitanje> dajMogucaPitanja = new ArrayList<>();
    public ArrayAdapter adapterDodanaPitanja;
    public ArrayAdapter adapterMogucaPitanja;
    public ArrayAdapter<Kategorija> adapterKategorije;


    public ArrayList<Kviz> kvizovi = new ArrayList<>();
    public static Pitanje zaDodatno = new Pitanje("Dodaj Pitanje",null,null,null);
    public EditText etNaziv;
    public ListView lvDodanaPitanja;
    public Button btnDodajKviz;
    public Spinner spKategorije;
    public ListView lvMogucaPitanja;
    public Button btnImportKviz ;
    public AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent vrijednosti = getIntent();
        setContentView(R.layout.activity_dodaj_kviz_akt);
        Kviz neki = new Kviz();

        alertDialog = new AlertDialog.Builder(DodajKvizAkt.this).create();
        alertDialog.setTitle("Alert");
        //alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        //alertDialog.show();

        kvizovi = (ArrayList<Kviz>) vrijednosti.getSerializableExtra("SviKvizovi");


        //VARIJABLE

        btnDodajKviz = (Button) findViewById(R.id.btnDodajKviz);
        spKategorije = (Spinner) findViewById(R.id.spKategorije);
        etNaziv = (EditText) findViewById(R.id.etNaziv);
        lvDodanaPitanja = (ListView) findViewById(R.id.lvDodanaPitanja);
        lvMogucaPitanja = (ListView) findViewById(R.id.lvMogucaPitanja);
        btnImportKviz = (Button) findViewById(R.id.btnImportKviz);


        //SPINNER

        boolean ima = false;
        kategorije = (ArrayList<Kategorija>) vrijednosti.getSerializableExtra("listaKategorija");
        for(Kategorija k : kategorije){
            if(k.getNaziv().equals("Dodaj kategoriju")){
                ima = true;
            }
        }
        if(!ima){
            kategorije.add(new Kategorija("Dodaj kategoriju", "0"));
        }

        adapterKategorije = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategorije);
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
                if (kategorije.get(i).getNaziv().equals("Dodaj kategoriju")) {
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

        }
        if (vrijednosti.hasExtra("Edit")) {
            // prosljedjen kviz
            neki = (Kviz) vrijednosti.getSerializableExtra("ZaEdit");
            spKategorije.setSelection(Integer.parseInt(neki.getKategorija().getId()));
            etNaziv.setText(neki.getNaziv());
            // dodana pitanja
            dodanaPitanja = dajDodana(neki);
            adapterDodanaPitanja = new ArrayAdapter<Pitanje>(this, android.R.layout.simple_list_item_1, dodanaPitanja);
            lvDodanaPitanja.setAdapter(adapterDodanaPitanja);
            adapterDodanaPitanja.notifyDataSetChanged();
            // moguca pitanja
            dajMogucaPitanja = DajMogucaPitanja(dajSvaPitanja(kvizovi), neki);
            adapterMogucaPitanja = new ArrayAdapter<Pitanje>(this, android.R.layout.simple_list_item_1, dajMogucaPitanja);
            lvMogucaPitanja.setAdapter(adapterMogucaPitanja);
            adapterMogucaPitanja.notifyDataSetChanged();
        }





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
                    intent.putExtra("kate",kategorije);
                    intent.putExtra("dodana",dodanaPitanja);
                    intent.putExtra("moguca",dajMogucaPitanja);
                    intent.putExtra("kvizovi",kvizovi);
                    intent.putExtra("naziv",etNaziv.getText().toString());

                    DodajKvizAkt.this.startActivity(intent);
                }
            }
        });

        if(vrijednosti.hasExtra("novoPitanje")){


            dodanaPitanja = (ArrayList<Pitanje>) vrijednosti.getSerializableExtra("dodana");
            dajMogucaPitanja = (ArrayList<Pitanje>) vrijednosti.getSerializableExtra("moguca");
            etNaziv.setText(vrijednosti.getStringExtra("ime"));

            adapterMogucaPitanja = new ArrayAdapter<Pitanje>(this, android.R.layout.simple_list_item_1, dajMogucaPitanja);
            lvMogucaPitanja.setAdapter(adapterMogucaPitanja);
            adapterMogucaPitanja.notifyDataSetChanged();

            adapterDodanaPitanja = new ArrayAdapter<Pitanje>(this, android.R.layout.simple_list_item_1, dodanaPitanja);
            lvDodanaPitanja.setAdapter(adapterDodanaPitanja);
            adapterDodanaPitanja.notifyDataSetChanged();

        }

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
                if(novi.getKategorija().getNaziv().equals("Dodaj kategoriju")){
                    proslo = false;
                    spKategorije.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                if(!novi.getKategorija().getNaziv().equals("Dodaj kategoriju")){
                    spKategorije.setBackgroundColor(getResources().getColor(R.color.bijela));
                }
                if(proslo){
                    kvizovi.add(novi);
                    novi.obrisiPitanje("Dodaj Pitanje");
                    Intent vratiKvizNaPocetnu =new Intent(DodajKvizAkt.this,KvizoviAkt.class);
                    String odNovog = novi.getKategorija().getId();
                    vratiKvizNaPocetnu.putExtra("sarma",true);
                    vratiKvizNaPocetnu.putExtra("id",odNovog);
                    vratiKvizNaPocetnu.putExtra("kvizovi",kvizovi);
                    vratiKvizNaPocetnu.putExtra("dodanaKategorija",kategorije);
                    DodajKvizAkt.this.startActivity(vratiKvizNaPocetnu);
                }
            }

        });

        btnImportKviz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/*");
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                try {
                    Kviz novi = new Kviz();
                    novi =  CitajPodatke(uri);
                    setKvizImport(novi);
                    importovan = true;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private Kviz CitajPodatke(Uri uri) throws IOException {

        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        Kviz ne = new Kviz();
        ne.setNaziv("");
        ne.setKategorija(new Kategorija("Sve","0"));
        ne.setPitanja(new ArrayList<Pitanje>());
        int brojodgUKvizu = 0;
        String naziv_kviza;
        String naziv_kategorije;
        int kolikoImaPitanja = 0;
        int pitanjaUKvizu = 0;
        ArrayList<Pitanje> pitanja = new ArrayList<>();
        Kviz kviz = new Kviz();
        Kategorija kategorija = new Kategorija();
        boolean proslo = true;

        String line;
        boolean prvi_red = true;

        try {
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (prvi_red) {
                    kviz.setNaziv(values[0]);
                    //ima li kviz
                    if(DaLiPostojiKviz(values[0])){
                            alertDialog.setMessage("Kviz kojeg Importujere vec Postoji");
                            alertDialog.show();
                            proslo = false;
                    }
                    pitanjaUKvizu = Integer.parseInt(values[2]);
                    kviz.setKategorija(DaLiPostojiKategorija(values[1]));

                } else {
                    int brojOdg = 0;
                    Pitanje pitanje = new Pitanje();
                    pitanje.setNaziv(values[0]);
                    pitanje.setTekstPitanja("");
                    Integer tacan = Integer.parseInt(values[2]);
                    if(tacan < values.length) {
                        pitanje.setTacan(values[tacan]);
                    }
                    brojodgUKvizu = Integer.parseInt(values[1]);

                    for (int i = 3; i < values.length; i++) {
                        if(values[i].contains(",")){
                            alertDialog.setMessage("Neki od odogovora u kvizu sadrze zarez");
                            alertDialog.show();
                            proslo = false;
                        }
                        pitanje.dodajOdgovor(values[i]);
                        brojOdg++;
                    }
                    if(brojOdg != brojodgUKvizu){
                        alertDialog.setMessage("Kviz kojeg Importujere ima neispravan broj odgovora");
                        alertDialog.show();
                        proslo = false;
                    }
                    if(tacan<0 || tacan>=(brojOdg+3)){
                        alertDialog.setMessage("Kviz kojeg importujete ima neispravan index tacnog odgovora");
                        alertDialog.show();
                        proslo = false;
                    }

                    pitanja.add(pitanje);
                    kolikoImaPitanja++;
                }
                prvi_red = false;
            }
            if(kolikoImaPitanja != pitanjaUKvizu){
                alertDialog.setMessage("Kviz kojeg importujete ima neispravan broj pitanja");
                alertDialog.show();
                proslo = false;
            }
            if(proslo){
                kviz.setPitanja(pitanja);
            }
            else{
                kviz = ne;
            }

        } catch (IOException e){
            Log.w("DodajKvizAkt","Nije moguce procitati liniju");
        }
        inputStream.close();
        return kviz;

    }


    public Kviz getKvizImport() {
        return kvizImport;
    }

    public void setKvizImport(Kviz kvizImport) {

        this.kvizImport = kvizImport;

        spKategorije.setSelection(Integer.parseInt(kvizImport.getKategorija().getId()));
        etNaziv.setText(kvizImport.getNaziv());
        // dodana pitanja
        dodanaPitanja = dajDodana(kvizImport);
        adapterDodanaPitanja = new ArrayAdapter<Pitanje>(this, android.R.layout.simple_list_item_1, dodanaPitanja);
        lvDodanaPitanja.setAdapter(adapterDodanaPitanja);
        adapterDodanaPitanja.notifyDataSetChanged();
        // moguca pitanja
        dajMogucaPitanja = DajMogucaPitanja(dajSvaPitanja(kvizovi), kvizImport);
        adapterMogucaPitanja = new ArrayAdapter<Pitanje>(this, android.R.layout.simple_list_item_1, dajMogucaPitanja);
        lvMogucaPitanja.setAdapter(adapterMogucaPitanja);
        adapterMogucaPitanja.notifyDataSetChanged();

    }




    public Kategorija DaLiPostojiKategorija(String s){
        boolean a = true;
        Integer broj = kategorije.size();
        Kategorija kate = new Kategorija();
        for(Kategorija k : kategorije){
            if(k.getNaziv().equals(s)){
                a = false;
                kate = k;
                break;
            }
        }
        if(a){
            Kategorija k = new Kategorija(s,broj.toString());
            kategorije.add(k);
            adapterKategorije.notifyDataSetChanged();
            return k;
        }
        else{
            return kate;
        }
    }

    public boolean DaLiPostojiKviz(String naziv){
        boolean ima = false;
        for(int i = 0; i <kvizovi.size();i++){
            if(kvizovi.get(i).getNaziv().equals(naziv)) {
                ima = true;
                break;
            }
        }
        return ima;
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




