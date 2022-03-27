
package com.example.pfemaps;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

class DBHelper extends SQLiteOpenHelper {

    String dbpath;
    String dbName;
    Context mcontext;
    public DBHelper( Context context , String name, int version) {
        super(context, name,null , version  );
        this.dbName = name;
        this.mcontext = context;
    this.dbpath =  "data/data/"+"demo.importdatatbasedemo" + "/databases/";
    }

    public void checkdb(){
        SQLiteDatabase checkdb = null;
        String filePath = dbpath + dbName;
        try{
            checkdb = SQLiteDatabase.openDatabase(filePath,null,0);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(checkdb != null){
            Toast.makeText(mcontext,"database already exists",Toast.LENGTH_SHORT).show();
        }else{

            CopyData();
        }
    }

    public void CopyData(){
        this.getReadableDatabase();

        try {
            InputStream ios = mcontext.getAssets().open(dbName);
            OutputStream os = new FileOutputStream(dbpath+ dbName);
            byte[] buffer = new byte[10];
            int len;
            while ((len = ios.read(buffer))> 0){
                os.write(buffer,0,len);
            }
            os.flush();
            ios.close();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("CopyDb","Database Copied");
        Toast.makeText(mcontext,"Database Copied",Toast.LENGTH_SHORT).show();
    }

    public  void OpenDatabase(){
        String filePath = dbpath+dbName;
        SQLiteDatabase.openDatabase(filePath,null,0);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(username TEXT primary key, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
    }
    public Boolean insertData(String order, String id,String lattitude , String langitude){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order", order);
        values.put("id", id);
        values.put("lattitude", lattitude);
        values.put("langitude", langitude);

        long result= db.insert("releve", null, values  );
        if (result== -1) return false;
        else
            return false;
    }

    public Cursor getdata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from COORDONNEESGPS ",null);

            return cursor;
           // return cursor.moveToFirst();
    }
    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=? and password=?", new String[] {username,password});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }



}
