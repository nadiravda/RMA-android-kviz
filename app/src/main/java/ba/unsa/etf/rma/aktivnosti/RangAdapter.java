package ba.unsa.etf.rma.aktivnosti;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.RangKlasa;

public class RangAdapter extends ArrayAdapter<RangKlasa> {
    private Context con;
    int res;

    public RangAdapter(@NonNull Context context, int resource, @NonNull List<RangKlasa> objects) {
        super(context, resource, objects);
        con = context;
        res = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

       String nazivIgraca = getItem(position).getNazivIgraca();
       Integer pozicija = getItem(position).getPozicija();
       String pozicijaS = pozicija.toString();
       Double procenat = getItem(position).getProcenat();
       String procenatS = procenat.toString();
       String NazivKviza = getItem(position).getNazivKviza();

       RangKlasa novi = new RangKlasa(NazivKviza,nazivIgraca,pozicija,procenat);

        LayoutInflater inflater = LayoutInflater.from(con);
        convertView = inflater.inflate(res,parent,false);

        TextView imeIgraca = (TextView) convertView.findViewById(R.id.igrac);
        TextView poz = (TextView) convertView.findViewById(R.id.rBroj);
        TextView proc = (TextView) convertView.findViewById(R.id.pTacnih);

        imeIgraca.setText(nazivIgraca);
        poz.setText(pozicijaS);
        proc.setText(procenatS);

        return convertView;

    }
}
