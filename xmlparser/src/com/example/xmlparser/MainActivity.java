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
	
	private ArrayList<String> listVar,listURL;
	private String url1 = "https://cfe132ba205f9ee267410edcbcb01ddc5eeeed6d.googledrive.com/host/"
			+ "0B1Pubw_66OGsfjdhUTJDNEJ5czNjOGF2VFJBcXQyTnJ1WVlISkpTSWNSN2drWW54N00wX1U/device.xml";
	private EditText location, AllWebService, AllService, pressure;
	private EditText Namespace, URLL, Soap_action, Method_name, Var;
	private HandleXML obj;
	String a = "";
	String b ="";
	String[] parts ;
	
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
		
		String finalUrl = url1;
		obj = new HandleXML(finalUrl);
		obj.fetchXML();
		
		ArrayList<String> list = obj.getAllWebService();
		ArrayList<String> listNamespace = obj.getlistNamespace();
		listURL = obj.getlistURL();
		ArrayList<String> listSoap_action = obj.getlistSoap_action();
		ArrayList<String> listMethod = obj.getlistMethod();
		listVar = obj.getlistVar();
		
		while (obj.parsingComplete);
		if (location.getText().toString().trim().equals("1")) {
			AllWebService.setText(list.toString().replaceAll("[\\[-\\]]+", ""));
			location.setText("");
			Toast toast = Toast.makeText(this, "��ԡ�÷���շ�����", Toast.LENGTH_LONG);
			toast.show();
		}
		else if(location.getText().toString().trim().startsWith("2")){			
			String inputServ2=location.getText().toString().replace("2","").trim();
			for(int sr2=0;sr2<list.size();sr2++){
				if(list.get(sr2).equals(inputServ2)){
					AllWebService.setText(list.get(sr2));
					AllService.setText(listMethod.get(sr2));
					location.setText("");
					a = listMethod.get(sr2);
				
					parts = a.split(",");
					Toast toast = Toast.makeText(this, "��й��س�����ԡ��"+list.get(sr2), Toast.LENGTH_LONG);
					toast.show();
				}
			}
			
		}
		else if(location.getText().toString().trim().startsWith("3")){
			String inpustr=location.getText().toString().replace("3","").trim();
			for(int z=0;z<parts.length;z++){
				if(parts[z].equals(inpustr)){
					AllService.setText(parts[z]);
					location.setText("");
					
					Namespace.setText(listNamespace.get(0).toString());
					URLL.setText(listURL.get(0).toString());
					Soap_action.setText(listSoap_action.get(0).split(",")[z].toString());
					Method_name.setText(listMethod.get(0).split(",")[z].toString());
					Var.setText(listVar.get(0).split(",")[z].toString());
					
					Toast toast = Toast.makeText(this, "�س���͡���ԡ�� "+parts[z], Toast.LENGTH_LONG);
					toast.show();	
				}
			}
		}
		else if(location.getText().toString().trim().equals("0")){ 	// Array 1
			
			AllWebService.setText("");
			AllService.setText("");
			Namespace.setText("");
			URLL.setText("");
			Soap_action.setText("");
			Method_name.setText("");
			Var.setText("");
			
			location.setText("");
			Toast toast = Toast.makeText(this, "��ҧ������ ", Toast.LENGTH_LONG);
			toast.show();
		}
		else {
			location.setText("");
			Toast toast = Toast.makeText(this, "����դ���觹��", Toast.LENGTH_LONG);
			toast.show();
		}

	}

}
