package ba.unsa.etf.rma.aktivnosti;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Kviz;

import static ba.unsa.etf.rma.aktivnosti.KvizoviAkt.kategorije;
import static ba.unsa.etf.rma.aktivnosti.KvizoviAkt.kvizovi;

public class IgrajKvizAkt extends AppCompatActivity {


    public static Kviz kviz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igraj_kviz_akt);


        Intent intent = getIntent();
        kviz = (Kviz) intent.getSerializableExtra("kviz");

        InformacijeFrag informacijeFrag = new InformacijeFrag();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.InformacijePlace,informacijeFrag).commit();

        RangLista pitanjeFrag = new RangLista();
        manager.beginTransaction().replace(R.id.pitanjePlace,pitanjeFrag).commit();


    }
}
