package com.example.sqltest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    MDatabaseHelper Data;
    Button insert=null;
    Button search=null;
    Button delete=null;
    Button update=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建MDatabaseHelper对象，指定数据库版本为1，使用相对路径
        //数据库文件会自动保存在程序的数据文件夹的databases目录下
        Data=new MDatabaseHelper(this,"Record",1);
        insert=findViewById(R.id.insert);
        search=findViewById(R.id.search);
        delete=findViewById(R.id.delete);
        update=findViewById(R.id.update);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入
                String date=((EditText)findViewById(R.id.edit_date)).getText().toString();
                String content=((EditText)findViewById(R.id.edit_content)).getText().toString();
                //插入日志记录
                insertData(Data.getReadableDatabase(),date,content);
                //显示提示消息
                Toast.makeText(MainActivity.this,"添加成功!",Toast.LENGTH_SHORT).show();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=((EditText)findViewById(R.id.edit_search)).getText().toString();
                //执行查询
                Cursor cursor=Data.getReadableDatabase().rawQuery(
                        "select * from dict where date like ? or content like ?",
                        new String[]{"%"+key+"%","%"+key+"%"});
                //创建Bundle用于传输数据
                Bundle pass_data=new Bundle();
                pass_data.putSerializable("data",converCursorToList(cursor));
                Intent intent=new Intent(MainActivity.this,ResulteActivity.class);
                intent.putExtras(pass_data);
                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=((EditText)findViewById(R.id.edit_search)).getText().toString();
                deleteData(Data.getReadableDatabase(),key);
                Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=((EditText)findViewById(R.id.edit_search)).getText().toString();
                String content=((EditText)findViewById(R.id.edit_content)).getText().toString();
                upateData(Data.getReadableDatabase(),key,content);
                Toast.makeText(MainActivity.this,"已更新指定日期的内容",Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected ArrayList<Map<String,String>> converCursorToList(Cursor cursor)
    {
        ArrayList<Map<String,String>> resulte=
                new ArrayList<Map<String,String>>();
        //遍历Cursor结果集
        while(cursor.moveToNext())
        {
            //将结果放入ArrayList中
            Map<String,String> map=new HashMap<>();
            //取出查询记录中第2列、第三列的值
            map.put("date",cursor.getString(1));
            map.put("content",cursor.getString(2));
            resulte.add(map);
        }
        return resulte;
    }

    private void insertData(SQLiteDatabase sqLiteDatabase,String date,String content)
    {
        //执行插入语句
        sqLiteDatabase.execSQL("insert into dict values(null,?,?)",new String[]{date,content});
    }
    private void deleteData(SQLiteDatabase sqLiteDatabase,String information)
    {
        //执行删除语句
        sqLiteDatabase.delete("dict","date like ? or content like ? ",new String[]{information,information});
        //sqLiteDatabase.delete("dict","content like ? ",new String[]{information});
    }
    private void upateData(SQLiteDatabase sqLiteDatabase,String date,String content)
    {
        //执行更新语句(只针对日期)
        ContentValues values=new ContentValues();
        values.put("content",content);
        sqLiteDatabase.update("dict",values,"date like ?",new String[]{date});
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //退出程序时关闭MDatabaseHelper里的SQLiteDatabase
        if (Data!=null)
        {
            Data.close();
        }
    }
}
