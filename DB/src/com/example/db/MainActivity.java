package com.example.db;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
 
public class MainActivity extends Activity {
 
    private ListView listView;
    private ArrayList<String> list;
    private ArrayAdapter<String> arrayAdapter;
    private ConnectServer connectServer;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        //����� listView �Ѻ View
        listView = (ListView)findViewById(R.id.listView);
 
        //���ҧ����������͡Ѻ Server 价�� URL ����˹�
        connectServer = new ConnectServer(this, "http://10.80.41.12/android/ttt.php");
 
        //��������觤�� age �դ�� 20 Ẻ post
        connectServer.addValue("age","20");
 
        //�������͡Ѻ Server
        connectServer.execute();
    }
 
    //��Ҵ֧�����Ũҡ Server �������� ���ҷӧҹ��� Function ���
    public void setList(ArrayList<String> list){
        this.list = list;
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, this.list);
        listView.setAdapter(arrayAdapter);
    }
 
    //����������ö�������͡Ѻ Server ����ҷӧҹ��� Function ���
    public void cannotConnectToServer() {
        Toast.makeText(this, "�������ö�������͡Ѻ Server", Toast.LENGTH_LONG).show();
    }
 
    //��Ҵ֧�����Ũҡ Server �ջѭ�� ���ҷӧҹ��� Function ���
    public void errorConnectToServer() {
        Toast.makeText(this, "�Դ�ͼԴ��Ҵ㹡�ô֧������", Toast.LENGTH_LONG).show();
    }
}