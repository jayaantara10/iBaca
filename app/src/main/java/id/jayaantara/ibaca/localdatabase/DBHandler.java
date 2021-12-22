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
    public static final int VER=1;

    public static final String row_id_paper="id_paper";
    public static final String row_judul="judul";
    public static final String row_jenis="jenis";
    public static final String row_penulis="penulis";
    public static final String row_link="link";
    public static final String row_lisensi="lisensi";
    public static final String row_batasan_umur="batasan_umur";
    public static final String row_deskripsi="deskripsi";
    public static final String row_id_user ="id_user";


    private SQLiteDatabase db;

    public DBHandler(Context context) {
        super(context, db_name, null, VER);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query2 ="CREATE TABLE " + table_paper + "("
                + row_id_paper + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_judul + " TEXT,"
                + row_jenis + " TEXT,"
                + row_penulis + " TEXT,"
                + row_link + " TEXT,"
                + row_lisensi + " TEXT,"
                + row_batasan_umur + " TEXT,"
                + row_deskripsi + " TEXT,"
                + row_id_user + " INTEGER);";
        db.execSQL(query2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ table_paper);
        onCreate(db);
    }
    public void insertDataSosmed(ContentValues values){
        db.insert(table_paper,null,values);
    }

    public void updateDataSosmed(ContentValues values, long id){
        db.update(table_paper,values,row_id_paper + "=" + id, null);
    }

    public void deleteDataSosmed(long id){
        db.delete(table_paper, row_id_paper + "=" + id, null);
    }

    public Cursor getAllDataSosmed(){
        return db.query(table_paper,null,null,null,null, null,null);
    }

    public Cursor getDataSosmed(long id){
        return db.rawQuery("SELECT*FROM " + table_paper + " WHERE " + row_id_paper + "=" + id, null);
    }

    public Cursor getDataSosmedByUserId(long id){
        return db.rawQuery("SELECT*FROM " + table_paper + " WHERE " + row_id_user + "=" + id, null);
    }

    public Cursor getDataSosmedByUserIdSearchBar(long id, Editable s){
        return db.rawQuery("SELECT*FROM " + table_paper + " WHERE " + row_id_user + "=" + id + " AND " + row_judul + " LIKE " +  " '%" + s + "%'", null);
    }
}
