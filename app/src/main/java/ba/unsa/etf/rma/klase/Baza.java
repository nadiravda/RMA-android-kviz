package ba.unsa.etf.rma.klase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Baza extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BazaRMA";

    public Baza(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public Baza(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String TABLE_KATEGORIJA = "Kategorija";
    public static final String TABLE_PITANJA = "Pitanje";
    public static final String TABLE_KVIZ = "Kviz";
    //kolone

    //kolone za Kategorija
    public static final String KATEGORIJA_ID = "_id";
    public static final String KATEGORIJA_NAZIV = "naziv";

    //kolone za Pitanja
    public static final String PITANJE_ID = "_id";
    public static final String PITANJE_NAZIV = "naziv";
    public static final String PITANJE_ODGOVORI = "odgovori";
    public static final String PITANJE_TACAN_ODGOVOR = "indexTacnog";

    //kolone za Kviz
    public static final String KVIZ_ID = "_id";
    public static final String KVIZ_NAZIV = "naziv";
    public static final String KVIZ_KATEGORIJA = "kategorija";
    public static final String KVIZ_PITANJA = "pitanja";

    private static final String CREATE_TABLE_CATEGORY = "create table " +
            TABLE_KATEGORIJA + " (" + KATEGORIJA_ID +
            " integer primary key autoincrement, " +
            KATEGORIJA_NAZIV + " text unique);";

    private static final String CREATE_TABLE_PITANJE = "create table " +
            TABLE_PITANJA + " (" + PITANJE_ID +
            " integer primary key autoincrement, " +
            PITANJE_NAZIV + " text, " + PITANJE_ODGOVORI + " text, " +
            PITANJE_TACAN_ODGOVOR + " text);";

    private static final String CREATE_TABLE_KVIZ = "create table " +
            TABLE_KVIZ + " (" + KVIZ_ID +
            " integer primary key autoincrement, " +
            KVIZ_NAZIV + " text, " + KVIZ_KATEGORIJA + " text, " +
            KVIZ_PITANJA + " text);";




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_PITANJE);
        db.execSQL(CREATE_TABLE_KVIZ);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORIJA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PITANJA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KVIZ);
        onCreate(db);
    }

    private String[] koloneKategorija = new String[]{KATEGORIJA_ID, KATEGORIJA_NAZIV};
    private String[] koloneKviz = new String[]{KVIZ_ID, KVIZ_NAZIV,KVIZ_KATEGORIJA,KVIZ_PITANJA};
    private String[] kolonePitanja = new String[]{PITANJE_ID, PITANJE_NAZIV,PITANJE_TACAN_ODGOVOR,PITANJE_ODGOVORI};

    private Cursor getCursor(String tabela, String[] kolone, String where) {
        SQLiteDatabase db = getWritableDatabase();
        return db.query(tabela, kolone, where, null, null, null, null);
    }

    private String getString(Cursor c, String kolona) {
        return c.getString(c.getColumnIndexOrThrow(kolona));
    }

    private long getLong(Cursor c, String kolona) {
        return c.getLong(c.getColumnIndexOrThrow(kolona));
    }

    private int getInt(Cursor c, String kolona) {
        return c.getInt(c.getColumnIndexOrThrow(kolona));
    }

    // Dodavanje

    public long dodajKategoriju(Kategorija naziv) {
        ContentValues novi = new ContentValues();
        novi.put(KATEGORIJA_NAZIV, naziv.getNaziv());
        novi.put(KATEGORIJA_ID,Integer.parseInt(naziv.getId()));
        SQLiteDatabase db = getWritableDatabase();
        return db.insertWithOnConflict(TABLE_KATEGORIJA, null, novi, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public long dodajKviz(Kviz kviz){
        ContentValues novi = new ContentValues();
        novi.put(KVIZ_NAZIV, kviz.getNaziv());
        novi.put(KVIZ_KATEGORIJA, kviz.getKategorija().getNaziv());
        //Dole ide koma separatorr..
        String tmp = new String ();
        for(Pitanje p : kviz.getPitanja()){
            tmp += p.getNaziv()+ ",";
        }
        novi.put(KVIZ_PITANJA, tmp);
        SQLiteDatabase db = getWritableDatabase();
       return  db.insertWithOnConflict(TABLE_KVIZ, null, novi, SQLiteDatabase.CONFLICT_IGNORE);

    }

    public long dodajPitanje(Pitanje pitanje){
        ContentValues novi = new ContentValues();
        novi.put(PITANJE_NAZIV, pitanje.getNaziv());
        novi.put(PITANJE_TACAN_ODGOVOR, pitanje.getTacan());
        //Dole ide koma separatorr..
        String koma = convertListToString(pitanje.getOdgovori());
        novi.put(PITANJE_ODGOVORI, koma);
        SQLiteDatabase db = getWritableDatabase();
        return  db.insertWithOnConflict(TABLE_PITANJA, null, novi, SQLiteDatabase.CONFLICT_IGNORE);

    }

    public ArrayList<Kategorija> getKategorije() {
        ArrayList<Kategorija> k = new ArrayList<>();
        Cursor c = getCursor(TABLE_KATEGORIJA, koloneKategorija, null);
        if (c.moveToFirst()) {
            do {
                k.add(new Kategorija(String.valueOf(getInt(c, koloneKategorija[0])),getString(c, koloneKategorija[1])));
            }
            while (c.moveToNext());
        }

        return k;
    }


    public ArrayList<Kviz> getKvizovi(){
        ArrayList<Kviz> k = new ArrayList<>();
        Cursor c = getCursor(TABLE_KVIZ, koloneKviz, null);
        if (c.moveToFirst()) {
            do {
                k.add(new Kviz(getString(c,koloneKviz[1]),dajPitanja(getString(c,koloneKviz[3])),dajKategoriju(getString(c,koloneKviz[2]))));
            }
            while (c.moveToNext());
        }
        return k;
    }

    public ArrayList<Pitanje> getPitanja(){
        ArrayList<Pitanje> k = new ArrayList<>();
        Cursor c = getCursor(TABLE_PITANJA, kolonePitanja, null);
        if (c.moveToFirst()) {
            do {
                k.add(new Pitanje(getString(c,kolonePitanja[1]),"",convertStringToList(getString(c,kolonePitanja[3])),getString(c,kolonePitanja[2])));
            }
            while (c.moveToNext());
        }
        return k;


    }


    // Funkcije za konvertovanje kviza/pitanja/kateogrije

    private static final String LIST_SEPARATOR = ",";

    public static String convertListToString(List<String> stringList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : stringList) {
            stringBuilder.append(str).append(LIST_SEPARATOR);
        }

        // Remove last separator
        stringBuilder.setLength(stringBuilder.length() - LIST_SEPARATOR.length());

        return stringBuilder.toString();
    }

    public static ArrayList<String> convertStringToList(String str) {
        ArrayList<String> temp = new ArrayList<>();
                temp.addAll(Arrays.asList(str.split(LIST_SEPARATOR)));
        return temp;
    }

    private ArrayList<Pitanje> dajPitanja(String temp){
        ArrayList<Pitanje> pitanjaUBazi = getPitanja();
        ArrayList<Pitanje> pitanja = new ArrayList<>();
        ArrayList<String> pitanjaString = new ArrayList<>();
        String[] p1 = temp.split(",");
        pitanjaString = new ArrayList<String>(Arrays.asList(p1));
        if(pitanjaUBazi == null ) return pitanja;
        for(Pitanje p : pitanjaUBazi){
            for(String x : pitanjaString){
                if(p.getNaziv().equals(x) && !pitanja.contains(p)) pitanja.add(p);
            }
        }

        return pitanja;
    }
    private Kategorija dajKategoriju(String temp){
        ArrayList<Kategorija> kategorijeUBazi = getKategorije();
        Kategorija kat = new Kategorija();

        if(kategorijeUBazi == null)return new Kategorija("Greska", "-1");

        for(Kategorija k: kategorijeUBazi){
            if(k.getNaziv().equals(temp))  kat = k;
        }

        return kat;
    }


}
