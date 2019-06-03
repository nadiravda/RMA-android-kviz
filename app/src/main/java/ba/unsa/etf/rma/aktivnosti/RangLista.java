package ba.unsa.etf.rma.aktivnosti;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Pitanje;

/**
 * A simple {@link Fragment} subclass.
 */
public class RangLista extends Fragment {

    public static ListView kvizic;
    public static ListView odgovoriPitanja;
    public static TextView tekstPitanja;
    public static ArrayList<Pitanje> pitanja = new ArrayList<>();
    public static ArrayList<String> odgovori = new ArrayList<>();
    public static ArrayAdapter adapter;
    public static Integer brojTacnih = 0;
    public static Integer brojPreostalih = 0;
    public static Double procenatTacnih =0.0;
    public static String tacan;
    public int brojOdgovorenih = 0;
    public Pitanje novo;

public boolean proso = false;

    public RangLista() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View iv = inflater.inflate(R.layout.fragment_pitanje, container, false);

       odgovoriPitanja = (ListView) iv.findViewById(R.id.odgovoriPitanja);
       tekstPitanja = (TextView) iv.findViewById(R.id.tekstPitanja);

        pitanja = IgrajKvizAkt.kviz.dajRandomPitanja();
        final int brojodgovora = pitanja.size();
        novo = new Pitanje();
        novo = pitanja.get(brojOdgovorenih);
        tekstPitanja.setText(novo.getNaziv());

        brojPreostalih = pitanja.size();
        InformacijeFrag.infBrojPreostalihPitanja.setText(String.valueOf(brojPreostalih));
        InformacijeFrag.infBrojTacnihPitanja.setText(String.valueOf(brojTacnih));
        InformacijeFrag.infProcenatTacni.setText("0.00");

        odgovori = novo.dajRandomOdgovore();
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,odgovori);
        odgovoriPitanja.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        tacan = novo.getTacan();




        odgovoriPitanja.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    tekstPitanja.setText(novo.getNaziv());

                    if(brojOdgovorenih == pitanja.size()) {
                    tekstPitanja.setText("Kviz je završen!");
                    ArrayList<String> names = new ArrayList<String>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, names);
                    odgovoriPitanja.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    }
                    else {

                        if (odgovori.get(position).equals(tacan)) {

                            brojPreostalih--;
                            brojTacnih++;

                            procenatTacnih = ((double) brojTacnih / brojodgovora) * 100;
                            odgovoriPitanja.getChildAt(position).setBackgroundResource(R.color.grin);

                                    InformacijeFrag.infBrojPreostalihPitanja.setText(String.valueOf(brojPreostalih));
                                    InformacijeFrag.infBrojTacnihPitanja.setText(String.valueOf(brojTacnih));
                                    InformacijeFrag.infProcenatTacni.setText(String.valueOf(procenatTacnih));

                                    brojOdgovorenih++;
                            if(brojOdgovorenih == pitanja.size()) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                tekstPitanja.setText("Kviz je završen!");
                                ArrayList<String> names = new ArrayList<String>();
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, names);
                                odgovoriPitanja.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                    }
                                }, 2000);
                            }
                            else {
                                novo = pitanja.get(brojOdgovorenih);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        odgovori = novo.dajRandomOdgovore();
                                        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, odgovori);
                                        odgovoriPitanja.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                        tacan = novo.getTacan();
                                    }
                                }, 2000);
                            }

                        }
                        else {

                            brojPreostalih--;
                            procenatTacnih = ((double) brojTacnih / brojodgovora) * 100;

                            odgovoriPitanja.getChildAt(position).setBackgroundResource(R.color.crvena);
                            odgovoriPitanja.getChildAt(dajTacan(odgovori, tacan)).setBackgroundResource(R.color.grin);

                                    InformacijeFrag.infBrojPreostalihPitanja.setText(String.valueOf(brojPreostalih));
                                    InformacijeFrag.infProcenatTacni.setText(String.valueOf(procenatTacnih));
                            brojOdgovorenih++;
                            if(brojOdgovorenih == pitanja.size()) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                tekstPitanja.setText("Kviz je završen!");
                                ArrayList<String> names = new ArrayList<String>();
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, names);
                                odgovoriPitanja.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                    }
                                }, 2000);
                            }
                            else {
                                novo = pitanja.get(brojOdgovorenih);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        odgovori = novo.dajRandomOdgovore();
                                        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, odgovori);
                                        odgovoriPitanja.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                        tacan = novo.getTacan();
                                    }
                                }, 2000);
                            }

                        }
                    }
            }
        });





        return iv;
    }

    public int dajTacan(ArrayList<String> odgovori,String tacan){
        int pozicija = 0;
        for(int i =0; i<odgovori.size(); i++){
            if(odgovori.get(i).equals(tacan)){
                pozicija = i;
                break;
            }
        }
        return pozicija;
    }



    public boolean DajKojeNijeBiloSvijut(Pitanje p){

        if(pitanja.contains(p)){
           return true;
        }
            return false;
    }


}
