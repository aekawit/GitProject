package com.example.xmlparser;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String url1 = "https://cfe132ba205f9ee267410edcbcb01ddc5eeeed6d.googledrive.com/host/0B1Pubw_66OGsfjdhUTJDNEJ5czNjOGF2VFJBcXQyTnJ1WVlISkpTSWNSN2drWW54N00wX1U/device.xml";
	private EditText location, AllWebService, AllService, pressure;
	private EditText Namespace, URLL, Soap_action, Method_name, Var;
	private HandleXML obj;
	private ArrayList<String> listt = new ArrayList<String>();
	private ArrayList<String> listSer = new ArrayList<String>();
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		location = (EditText) findViewById(R.id.editText1);
		AllWebService = (EditText) findViewById(R.id.editText2);
		AllService = (EditText) findViewById(R.id.editText3);
		pressure = (EditText) findViewById(R.id.editText5);
		
		Namespace = (EditText) findViewById(R.id.editText4);
		URLL = (EditText) findViewById(R.id.EditText01);
		Soap_action = (EditText) findViewById(R.id.EditText02);
		Method_name = (EditText) findViewById(R.id.EditText03);
		Var = (EditText) findViewById(R.id.EditText04);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void open(View view) {
		String a = "";
		String b ="";
		String finalUrl = url1;
		obj = new HandleXML(finalUrl);
		obj.fetchXML();
		
		
		ArrayList<String> list = obj.getAllWebService();
		ArrayList<String> listNamespace = obj.getlistNamespace();
		ArrayList<String> listURL = obj.getlistURL();
		ArrayList<String> listSoap_action = obj.getlistSoap_action();
		ArrayList<String> listMethod = obj.getlistMethod();
		ArrayList<String> listVar = obj.getlistVar();
		
		
		
		
		while (obj.parsingComplete);
		if (location.getText().toString().trim().equals("1")) {
			AllWebService.setText(list.toString());
			location.setText("");
			Toast toast = Toast.makeText(this, "��ԡ�÷���շ�����", Toast.LENGTH_SHORT);
			toast.show();
				 
		} 
		else if(location.getText().toString().trim().equals("2")){
			
			AllService.setText(listMethod.toString());
			location.setText("");
			Toast toast = Toast.makeText(this, "�Դ���ԡ�� ", Toast.LENGTH_SHORT);
			toast.show();
		}
		else if(location.getText().toString().trim().equals(list.get(0).trim())){ 	// Array 1
			
			AllWebService.setText(list.get(0).toString());
			AllService.setText(listMethod.get(0).toString());
			Namespace.setText(listNamespace.get(0).toString());
			URLL.setText(listURL.get(0).toString());
			Soap_action.setText(listSoap_action.get(0).toString());
			Method_name.setText(listMethod.get(0).toString());
			Var.setText(listVar.get(0).toString());
			
			location.setText("");
			Toast toast = Toast.makeText(this, "�Դ���ԡ�� ", Toast.LENGTH_SHORT);
			toast.show();
		}
		else if(location.getText().toString().trim().equals(list.get(1).trim())){	// Array 2
			
			AllWebService.setText(list.get(1).toString());
			AllService.setText(listMethod.get(1).toString());
			
			location.setText("");
			Toast toast = Toast.makeText(this, "�Դ���ԡ�� ", Toast.LENGTH_SHORT);
			toast.show();
		}
		else if(location.getText().toString().trim().equals(list.get(2).trim())){	// Array 3
			
			AllWebService.setText(list.get(2).toString());
			AllService.setText(listMethod.get(2).toString());
			
			location.setText("");
			Toast toast = Toast.makeText(this, "�Դ���ԡ�� ", Toast.LENGTH_SHORT);
			toast.show();
		}
		else if(location.getText().toString().trim().equals(list.get(3).trim())){	// Array 4
			
			AllWebService.setText(list.get(3).toString());
			AllService.setText(listMethod.get(3).toString());
			
			location.setText("");
			Toast toast = Toast.makeText(this, "�Դ���ԡ�� ", Toast.LENGTH_SHORT);
			toast.show();
		}
		else if(location.getText().toString().trim().equals(list.get(4).trim())){	// Array 5
			
			AllWebService.setText(list.get(4).toString());
			AllService.setText(listMethod.get(4).toString());
			
			location.setText("");
			Toast toast = Toast.makeText(this, "�Դ���ԡ�� ", Toast.LENGTH_SHORT);
			toast.show();
		}
		else if(location.getText().toString().trim().equals(list.get(5).trim())){	// Array 6
			
			AllWebService.setText(list.get(5).toString());
			AllService.setText(listMethod.get(5).toString());
			
			location.setText("");
			Toast toast = Toast.makeText(this, "�Դ���ԡ�� ", Toast.LENGTH_SHORT);
			toast.show();
		}
		else if(location.getText().toString().trim().equals("0".trim())){
			location.setText("");
			AllWebService.setText("");
			AllService.setText("");
			Namespace.setText("");
			URLL.setText("");
			Soap_action.setText("");
			Method_name.setText("");
			Var.setText("");
			pressure.setText("");
			Toast toast = Toast.makeText(this, "���¤�ҷ����� ", Toast.LENGTH_SHORT);
			toast.show();
		}
		else {
			location.setText("");
			Toast toast = Toast.makeText(this, "����դ���觹��", Toast.LENGTH_SHORT);
			toast.show();
		}

	}

}


/*
a += list.get(i).toString()+"-";*/

/*
String[] parts = a.split("-");

part1 = parts[0];
part2 = parts[1];
part3 = parts[2];
part4 = parts[3];
part5 = parts[4];
part6 = parts[5];*/


/*

for (int i = 0; i < list.size(); i++) {
	a += list.get(i)+ System.getProperty("line.separator");
	list2.add(list.get(i).trim());
}*/