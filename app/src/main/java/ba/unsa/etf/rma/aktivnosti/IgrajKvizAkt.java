package ba.unsa.etf.rma.aktivnosti;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.MyReceiver;

public class IgrajKvizAkt extends AppCompatActivity {

    public static final int REQUEST_CODE=101;

    public static Kviz kviz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igraj_kviz_akt);


        Intent intent = getIntent();
        kviz = (Kviz) intent.getSerializableExtra("kviz");

        if(kviz.getPitanja() != null) {
            int minuta = kviz.getPitanja().size();
            if (minuta % 2 != 0) {
                minuta++;
            }
            minuta = minuta / 2;


            Date datum = new Date();
            datum.setTime(DajVrijeme() + minuta * 1000 * 60);
            Intent vra = new Intent(AlarmClock.ACTION_SET_ALARM);
            vra.putExtra(AlarmClock.EXTRA_HOUR, datum.getHours());
            vra.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
            vra.putExtra(AlarmClock.EXTRA_MINUTES, datum.getMinutes());
            startActivity(vra);

            String strDateFormat = "hh:mm:ss a";
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
            String formattedDate = dateFormat.format(datum);
            System.out.println("Trenutno vrijeme: " + formattedDate);
        }


        InformacijeFrag informacijeFrag = new InformacijeFrag();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.InformacijePlace,informacijeFrag).commit();

        PitanjeFrag pitanjeFrag = new PitanjeFrag();
        RangLista r = new RangLista();
        manager.beginTransaction().replace(R.id.pitanjePlace,pitanjeFrag).commit();


    }


    public long DajVrijeme(){
        Date d = new Date();
        d.setTime(System.currentTimeMillis());

        if(d.getMinutes() > 0){
            d.setSeconds(0);
            d.setMinutes(d.getMinutes()+1);
        }
        return d.getTime();
    }


}
