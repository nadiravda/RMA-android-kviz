package ba.unsa.etf.rma.aktivnosti;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;

import ba.unsa.etf.rma.R;

public class MojAdapter extends ArrayAdapter<RangKlasa> {

 private Context mContext;
 int mResuorce;

    public MojAdapter(@NonNull Context context, int resource, @NonNull List<RangKlasa> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String ime = getItem(position).getNaziv();
        Integer broj = getItem(position).getBrojTacnih();
        Double procenat = getItem(position).getProcenatTacnih();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResuorce,parent,false);

        TextView t1 = (TextView) convertView.findViewById(R.id.igrac);
        TextView t2 = (TextView) convertView.findViewById(R.id.rBroj);
        TextView t3 = (TextView) convertView.findViewById(R.id.pTacnih);

        t1.setText(ime);
        t2.setText(String.valueOf(broj));
        t3.setText(String.valueOf(procenat));

        return convertView;

    }
}
