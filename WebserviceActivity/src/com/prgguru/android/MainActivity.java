package com.prgguru.android;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.example.webserviceactivity.R;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private String NAMESPACE;
	private String URL2;
	private String SOAP_ACTION;
	private String METHOD_NAME;
	private String Att;
	private String Name  ="";
	private String Name2 ="";
	private static String celcius;
	private static String fahren;
	private int a = 0;
	private int s = 0;
	Button b;
	TextView tv;
	EditText et;
	NodeList nodelist;
	ProgressDialog pDialog;
	String URL = "https://cfe132ba205f9ee267410edcbcb01ddc5eeeed6d.googledrive.com/host"
			+ "/0B1Pubw_66OGsfjdhUTJDNEJ5czNjOGF2VFJBcXQyTnJ1WVlISkpTSWNSN2drWW54N00wX1U/device2.xml";
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		et = (EditText) findViewById(R.id.editText1);
		tv = (TextView) findViewById(R.id.tv_result);
		tv.setText("������ ����������");
		b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				celcius = et.getText().toString().trim();
				if(celcius.equals("Back")){
					if(a == 0){
						tv.setText("��й��س�����ԡ����ѡ��������  ��ͧ��ä��Һ�ԡ�����ٴ��� Sert");
					}else{
					a = 0;
					tv.setText("");
					tv.setText("��Ѻ���������ѡ������� "+ "\n");
					}
				}
				else if(a == 1){
					AsyncCallWS task = new AsyncCallWS();
					task.execute();
					
				}else{		
					new DownloadXML().execute(URL);
				}
			}
			
		});
	}
	private class DownloadXML extends AsyncTask<String, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setTitle("�Դ��Ͱҹ������ XML");
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.show();
		}
		@Override
		protected Void doInBackground(String... Url) {
			try {
				URL url = new URL(Url[0]);
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new InputSource(url.openStream()));
				doc.getDocumentElement().normalize();
				nodelist = doc.getElementsByTagName("SERVICE");

			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args) {
			tv.setText("");
             if(!checkInput()){
            	tv.setText("not matching"); 
             }
			pDialog.dismiss();
		}
		
		private boolean checkInput(){
			boolean stu=false;
			
			for (int temp = 0; temp < nodelist.getLength(); temp++) {
				Node nNode = nodelist.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Name2 = getNode("SERVICENAME", eElement).toString();
					
					if(celcius.equals("Sert")){
					Name2 = getNode("SERVICENAME", eElement).toString();
					tv.setText(tv.getText()+ getNode("SERVICENAME", eElement)+ "\n");
					stu=true;
					}
					
					else if(Name2.equals(celcius)){
					Name 		 += getNode("SERVICENAME", eElement)+ "\n";
					NAMESPACE 	 = getNode("NAMESPACE", eElement);
					URL2 		 = getNode("URL", eElement);
					SOAP_ACTION  = getNode("SOAP_ACTION", eElement);
					METHOD_NAME  = getNode("METHOD_NAME", eElement);
					Att 		 = getNode("ATTIBUTE", eElement);
					String Infor = getNode("INFORMATION", eElement);
					a = 1;
					tv.setText("�س���������ԡ�� "+ Name2 +" �������" +"\n" +"�س����ö����ҵ������㹢�й��"+"\n"+"\n"+Infor);
					stu=true;
					}				
				}
			}
			
			return stu;
		}
		
	}
	
	private static String getNode(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}
	
	public void getFahrenheit(String celsius) {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		String sttname[]=Att.split(",");
		String sttValue[]=celsius.split(",");
		for(int i=0;i<sttname.length;i++){
			request.addProperty(sttname[i],sttValue[i]);
		}
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);

		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			fahren = response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class AsyncCallWS extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			getFahrenheit(celcius);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			tv.setText(fahren);
		}

		@Override
		protected void onPreExecute() {
			tv.setText("���ѧ����...");
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
}
