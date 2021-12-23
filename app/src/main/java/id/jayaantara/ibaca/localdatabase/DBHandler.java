package id.jayaantara.ibaca.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;

public class DBHandler extends SQLiteOpenHelper {

    public static final String db_name="db_ibaca";
    public static final String table_paper="tb_paper";
    public static final int VER=2;

    public static final String row_id_paper="id_paper";
    public static final String row_judul="judul";
    public static final String row_jenis="jenis";
    public static final String row_penulis="penulis";
    public static final String row_link="link";
    public static final String row_lisensi="lisensi";
    public static final String row_batasan_umur="batasan_umur";
    public static final String row_deskripsi="deskripsi";
    public static final String row_tanggal="tanggal";
    public static final String row_id_user ="id_user";


    private SQLiteDatabase db;

    public DBHandler(Context context) {
        super(context, db_name, null, VER);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query ="CREATE TABLE " + table_paper + "("
                + row_id_paper + " INTEGER,"
                + row_judul + " TEXT,"
                + row_jenis + " TEXT,"
                + row_penulis + " TEXT,"
                + row_link + " TEXT,"
                + row_lisensi + " TEXT,"
                + row_batasan_umur + " TEXT,"
                + row_deskripsi + " TEXT,"
                + row_tanggal + " TEXT,"
                + row_id_user + " INTEGER);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ table_paper);
        onCreate(db);
    }
    public void insertDataPaper(ContentValues values){
        db.insert(table_paper,null,values);
    }

    public void updateDataPaper(ContentValues values, long id){
        db.update(table_paper,values,row_id_paper + "=" + id, null);
    }

    public void deleteDataPaper(long id){
        db.delete(table_paper, row_id_paper + "=" + id, null);
    }

    public Cursor getAllDataPaper(){
        return db.query(table_paper,null,null,null,null, null,null);
    }

    public Cursor getDataPaper(long id){
        return db.rawQuery("SELECT*FROM " + table_paper + " WHERE " + row_id_paper + "=" + id, null);
    }

    public boolean checkBackupPaper(long id, String judul, String jenis, String penulis, String link, String deskripsi, String lisensi, String umur, String tanggal, long id_user){
        SQLiteDatabase DB = this.getWritableDatabase();
        String [] columns = {row_id_paper};
        String selections = row_id_paper + "=?" + " and " + row_judul + "=?" + " and " + row_jenis + "=?" + " and " + row_penulis + "=?" + " and " + row_link + "=?" + " and " + row_deskripsi + "=?" + " and " + row_lisensi + "=?" + " and " + row_batasan_umur + "=?" + " and " + row_tanggal + "=?" + " and " + row_id_user + "=?";
        String [] selectionArgs = {String.valueOf(id), judul, jenis, penulis, link, deskripsi, lisensi, umur, tanggal, String.valueOf(id_user)};
        Cursor cursor = db.query(table_paper, columns, selections, selectionArgs, null, null, null);
        int count = cursor.getCount();
        if(count>0){
            return true;
        }else{
            return false;
        }
    }

    public boolean checkBackupPaperExist(long id){
        SQLiteDatabase DB = this.getWritableDatabase();
        String [] columns = {row_id_paper};
        String selections = row_id_paper + "=?";
        String [] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(table_paper, columns, selections, selectionArgs, null, null, null);
        int count = cursor.getCount();
        if(count>0){
            return true;
        }else{
            return false;
        }
    }
}
