package ba.unsa.etf.rma.ba.unsa.etf.rma.fragmenti;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ba.unsa.etf.rma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFrag extends Fragment {


    public DetailFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.detail, container, false);
    }

}
