package ba.unsa.etf.rma.aktivnosti;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Kviz;

public class IgrajKvizAkt extends AppCompatActivity {

    public static ArrayList<Kviz> kvizovi;
    public static ArrayList<Kategorija> kategorije;
    public static Kviz kviz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igraj_kviz_akt);


        Intent intent = getIntent();
        kvizovi = (ArrayList<Kviz>) intent.getSerializableExtra("kvizovi");
        kategorije = (ArrayList<Kategorija>) intent.getSerializableExtra("kategorije");
        kviz = (Kviz) intent.getSerializableExtra("kviz");

        InformacijeFrag informacijeFrag = new InformacijeFrag();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.InformacijePlace,informacijeFrag).commit();

        PitanjeFrag pitanjeFrag = new PitanjeFrag();
        manager.beginTransaction().replace(R.id.pitanjePlace,pitanjeFrag).commit();


    }
}
