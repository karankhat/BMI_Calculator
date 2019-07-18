package com.example.bmicalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by HP on 13-07-2018.
 */

public class DatabaseAct extends SQLiteOpenHelper {

    SQLiteDatabase sdb;
    Context context;

    DatabaseAct(Context context)
    {
        super(context,"BMIhistorydb",null,1);
        this.context=context;
        sdb=this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        String sql="CREATE TABLE BMIhistory(bmi DOUBLE,date TEXT)";
        sdb.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sdb, int i, int i1) {

    }

    public void addBMI(double BMI)
    {
        Date d=new Date();

        ContentValues cv=new ContentValues();
        cv.put("BMI",BMI);

        String date=""+d;
        cv.put("date",date);

        long rid=sdb.insert("BMIhistory",null,cv);

        if (rid<0)
            Toast.makeText(context, "Issue in saving the record!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Record saved!", Toast.LENGTH_SHORT).show();
    }

    public void deleteAllBMI()
    {
        sdb.delete("BMIhistory",null,null);
    }

    public String viewBMI()
    {
        Cursor cursor=sdb.query("BMIhistory",null,null,null,null,null,null);
        cursor.moveToFirst();

        StringBuffer sb=new StringBuffer("");
        int count=1;

        if (cursor.getCount()>0)
        {
            do
            {
                String BMI=cursor.getString(0);
                String da=cursor.getString(1);

                sb.append((count++)+".BMI: "+BMI+"\n   "+da+"\n\n");

                //Date da=new Date();
                //da.setTime(t);
                //String d=da.toString();

                //DateFormat dfd=DateFormat.getDateInstance();
                //DateFormat dft=DateFormat.getTimeInstance();

                //Date d=null;
                //Date t=null;
                //try
                //{
                //    d = dfd.parse(da);
                //    t = dft.parse(da);
                //}
                //catch(ParseException p)
                //{
                //    Toast.makeText(context, "Invalid Date!", Toast.LENGTH_SHORT).show();
                //}

            }while (cursor.moveToNext());
        }

        return sb.toString();
    }

}
