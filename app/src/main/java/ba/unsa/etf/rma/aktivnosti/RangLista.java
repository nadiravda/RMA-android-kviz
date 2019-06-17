package ba.unsa.etf.rma.aktivnosti;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.RangKlasa;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Kviz;
import static ba.unsa.etf.rma.aktivnosti.KvizoviAkt.kategorije;
import static ba.unsa.etf.rma.aktivnosti.KvizoviAkt.kvizovi;

/**
 * A simple {@link Fragment} subclass.
 */
public class RangLista extends Fragment {

    public static ListView lista;

    public RangLista() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View iv = inflater.inflate(R.layout.fragment_rang_lista, container, false);

        ArrayList<RangKlasa> nova = new ArrayList<>();
         RangKlasa rang = new RangKlasa("sarma","Ibro",3,55.51);
        nova.add(rang);
        lista = (ListView) iv.findViewById(R.id.listaRangova);
        RangAdapter adapter = new RangAdapter(getContext(),R.layout.adapterranglista,nova);
        lista.setAdapter(adapter);

        return iv;
    }

}
