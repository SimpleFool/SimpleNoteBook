package com.example.sqltest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MDatabaseHelper extends SQLiteOpenHelper {
    final String CREATE_Note_SQL=
            "create table dict(_id integer primary "+
                    "key autoincrement , date,content)";
    public  MDatabaseHelper(Context context,String name,int version)
    {
        super(context,name,null,version);
    }
    @Override
    //第一次创建时和初始化时调用
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_Note_SQL);
    }
    @Override
    //数据更新时调用
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldversion,int newversion)
    {
        System.out.println("onUpdate Called:\n"+oldversion+"----->"+newversion);
    }
}
