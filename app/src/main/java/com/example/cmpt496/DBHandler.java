package com.example.cmpt496;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {


    private static final String DB_NAME = "tower";


    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "towers";

    private static final String ID_COL = "id";
    private static final String TOWER_NAME = "towerName";

    private static final String PLANT_NAME = "plantName";
    private static final String ENTRY_DATE = "entryDate";

    private static final String PLANT_STAGE = "plantStage";
    private static final String BOARD_ID = "boardId";


    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TOWER_NAME + " TEXT,"
                + PLANT_NAME + " TEXT,"
                + ENTRY_DATE + " TEXT,"
                + PLANT_STAGE + " TEXT,"
                + BOARD_ID + " TEXT)";

        db.execSQL(query);
    }


    public void addNewTower(String towerName, String plantName, String entryDate, String boardId, String plantStage) {


        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();

        values.put(TOWER_NAME, towerName);
        values.put(PLANT_NAME, plantName);
        values.put(ENTRY_DATE, entryDate);
        values.put(BOARD_ID, boardId);
        values.put(PLANT_STAGE, plantStage);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public HashMap<Integer, ArrayList<String>> getDbEntries(){

        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        HashMap<Integer, ArrayList<String>> retData = new HashMap<>();


        int i = 0;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                ArrayList<String> list = new ArrayList<>();

                list.add(cursor.getString(1));
                list.add(cursor.getString(2));
                list.add(cursor.getString(3));
                list.add(cursor.getString(4));
                //list.add(cursor.getString(5));
                list.add(cursor.getString(0));

                retData.put(i, list);



                i++;



            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return retData;

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    //textfields should be inserted into the paramater.
    //updates the fields based on the column id given
    public void updateDb(String towerName, String plantName, String plantStage, String entryDate, String id ){

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues value = new ContentValues();
        value.put(PLANT_NAME, plantName);
        value.put(PLANT_STAGE, plantStage);
        value.put(ENTRY_DATE, entryDate);
        value.put(TOWER_NAME, towerName);
        db.update(TABLE_NAME,value,ID_COL  + " = " + id,null );

    }



    //deletes field based on column id given
    public void deleteEntry(String pk_id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE from " +  TABLE_NAME
        + " WHERE " + ID_COL + " = " + pk_id);



    }


    //based on these two params the PK will be found
    public void getPlantId(String towerName, String entryDate, String plantName, String stage){

        SQLiteDatabase db = this.getWritableDatabase();

    }

    public String getPlantNames(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT "+ PLANT_NAME+ " from " + TABLE_NAME + " ", null);
        if (c.moveToFirst()){
            do {
                // Passing values
                return c.getString(0);
                // Do something Here with values
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return null;
    }

}