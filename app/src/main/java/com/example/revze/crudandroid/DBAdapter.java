package com.example.revze.crudandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.revze.crudandroid.domain.Siswa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by revze on 21/10/15.
 */
public class DBAdapter extends SQLiteOpenHelper{

    private static final String DB_NAME = "biodata";
    private static final String TABLE_NAME = "m_siswa";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "nama";
    private static final String COL_KELAS = "kelas";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + TABLE_NAME + ";";
    private SQLiteDatabase sqliteDatabase = null;

    public DBAdapter(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL(DROP_TABLE);
    }

    public void openDB() {
        if (sqliteDatabase == null) {
            sqliteDatabase = getWritableDatabase();
        }
    }

    public void closeDB() {
        if (sqliteDatabase != null) {
            if (sqliteDatabase.isOpen()) {
                sqliteDatabase.close();
            }
        }
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COL_NAME + " TEXT," + COL_KELAS + " TEXT);");
    }

    public void updateSiswa(Siswa siswa) {
        sqliteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, siswa.getNama());
        cv.put(COL_KELAS, siswa.getKelas());
        String whereClause = COL_ID + "==?";
        String whereArgs[] = new String[] { siswa.getId() };
        sqliteDatabase.update(TABLE_NAME, cv, whereClause, whereArgs);
        sqliteDatabase.close();
    }

    public void save(Siswa siswa) {
        sqliteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, siswa.getNama());
        contentValues.put(COL_KELAS, siswa.getKelas());

        sqliteDatabase.insertWithOnConflict(TABLE_NAME, null,
                contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        sqliteDatabase.close();
    }

    public void delete(Siswa siswa) {
        sqliteDatabase = getWritableDatabase();
        String whereClause = COL_ID + "==?";
        String[] whereArgs = new String[] { String.valueOf(siswa.getId()) };
        sqliteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
        sqliteDatabase.close();
    }

    public void deleteAll() {
        sqliteDatabase = getWritableDatabase();
        sqliteDatabase.delete(TABLE_NAME, null, null);
        sqliteDatabase.close();
    }

    public List<Siswa> getAllSiswa() {
        sqliteDatabase = getWritableDatabase();

        Cursor cursor = this.sqliteDatabase.query(TABLE_NAME, new String[] {
                COL_ID, COL_NAME, COL_KELAS }, null, null, null, null, null);
        List<Siswa> siswas = new ArrayList<Siswa>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Siswa siswa = new Siswa();
                siswa.setId(cursor.getString(cursor.getColumnIndex(COL_ID)));
                siswa.setNama(cursor.getString(cursor
                        .getColumnIndex(COL_NAME)));
                siswa.setKelas(cursor.getString(cursor
                        .getColumnIndex(COL_KELAS)));
                siswas.add(siswa);
            }
            sqliteDatabase.close();
            return siswas;
        } else {
            sqliteDatabase.close();
            return new ArrayList<Siswa>();
        }
    }
}
