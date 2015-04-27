package com.aeke.Project;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

import com.example.mainapp.R;
import com.example.mainapp.R.menu;

import android.R.bool;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData.Item;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.net.Uri;
import android.content.pm.PackageManager;

public class MainActivity extends Activity {
	protected static final int RESULT_SPEECH = 1;
	final static String SVOX_TTS_ENGINE = "com.svox.classic";
	final static int INTENT_CHECK_TTS = 0;
	private static String TextRequest = "";
	private static String TextResponse = "";
	private int Status,StatusService = 0;
	private String NAMESPACE,URL2,SOAP_ACTION,METHOD_NAME,Att;
	private String Txt,Name,Name2,Infor = "";
	TextToSpeech tts;
	Button b;
	NodeList nodelist;
	TextView text_v;
	ImageButton btnSpeak;
	ProgressDialog pDialog;
	String URL = "https://cfe132ba205f9ee267410edcbcb01ddc5eeeed6d.googledrive.com/host"
			+ "/0B1Pubw_66OGsfjdhUTJDNEJ5czNjOGF2VFJBcXQyTnJ1WVlISkpTSWNSN2drWW54N00wX1U/device2.xml";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		checkTTSEngineInstalled(SVOX_TTS_ENGINE);
		final MediaPlayer mpBtn = MediaPlayer.create(this, R.raw.buttonclickk);
		text_v = (TextView) findViewById(R.id.tv_result);
		text_v.setText("แตะไมค์ เมื่อต้องการสั่งงาน");
		
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		btnSpeak.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				text_v.setText("");
				tts.stop();
				mpBtn.start();
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "th");
				try {
					startActivityForResult(intent, RESULT_SPEECH);
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"ผิดพลาด ! เครื่องของคุณยังไม่ได้ติดตั้ง โปรแกรมเสียงเป็นข้อความ",Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});
	}
	public void checkTTSEngineInstalled(String packageName) {
		boolean isSvoxInstalled = isAppInstalled(packageName);
		if (isSvoxInstalled) {
			if (tts == null)
				tts = new TextToSpeech(MainActivity.this, null, packageName);
		} else if (!isSvoxInstalled) {
			Toast.makeText(getApplicationContext(),
					"กรุณาติดตั้ง SVOX Text-to-Speech Engine",
					Toast.LENGTH_LONG).show();
			Intent svoxIntent = new Intent(Intent.ACTION_VIEW);
			svoxIntent.setData(Uri.parse("market://details?id=" + packageName));
			startActivity(svoxIntent);
		}
	}

	public boolean isAppInstalled(String packageName) {
		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
		}
		return app_installed;
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		//final MediaPlayer mpBtn3 = MediaPlayer.create(this, R.raw.buttonclickk);
		int id = item.getItemId();
		if (id == R.id.search_icon){
			help();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}
	public void help(){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("รวมคำสั่งการใช้งาน"+"\n"+": ค้นหา"+"\n"+": ชื่อบริการ"+"\n"+": สถานะ"+"\n"+": เมนูหลัก");
		builder.setPositiveButton("เรียบร้อย", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
		    	tts.stop();
		    }
		});
		builder.show();
		
		Txt = 	"ต้องการค้นหาบริการ ให้พูดว่า ค้นหา"
				+"\n"+"ต้องการใช้งานบริการ ให้พูดชื่อบริการที่ต้องการใช้"
				+"\n"+"ต้องการตรวจสอบสถานะการทำงาน ให้พูดว่า สถานะ"
				+"\n"+"ต้องการกลับเมนูหลัก ให้พูดว่า เมนูหลัก";
		//text_v.setText("ค้นหา"+"\n"+"ชื่อบริการ"+"\n"+"สถานะ"+"\n"+"เมนูหลัก");
		tts.speak(Txt, TextToSpeech.QUEUE_FLUSH, null);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {
				ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				text_v.setText(text.get(0).trim());
			}
			if (text_v.getText().length() != 0 && text_v.getText().toString() != "") {
				TextRequest = text_v.getText().toString().trim();
				Toast toast = Toast.makeText(this, TextRequest,Toast.LENGTH_LONG);
				toast.show();
				
				if(TextRequest.equals("เมนูหลัก")){
					if(Status == 0){
						Txt = "ขณะนี้คุณอยู่บริการหลักแล้ว  ต้องการค้นหาบริการให้พูดว่า  ค้นหา";
						text_v.setText(Txt);
						tts.speak(Txt, TextToSpeech.QUEUE_FLUSH, null);
					}
					else{
						Status = 0;
						text_v.setText("");
						Txt = "กลับสู่เมนูหลักได้สำเร็จ";
						text_v.setText(Txt);
						tts.speak(Txt, TextToSpeech.QUEUE_FLUSH, null);
					}
				}
				else if (TextRequest.equals("บริการ")){
					help();
				}
				else if (TextRequest.equals("สถานะ")){
					if(Status == 1){
						Txt = "บริการที่เรียกใช้อยู่ขณะนี้คือ  "+ Name + "\n" + Infor;
					}
					else{
						Txt = "ขณะนี้อยู่ที่ เมนูหลัก ยังไม่มีการเรียกใช้งานบริการ";
					}
					text_v.setText(Txt);
					tts.speak(Txt, TextToSpeech.QUEUE_FLUSH, null);
				}
				else if(Status == 1){ //การเรียกใช้การแปลงสกุลเงิน
					if(StatusService == 1){
						if(TextRequest.equals("us dollar")){
							TextRequest = "THB,USD".toString().trim();
						}else if (TextRequest.equals("taiwan dollar")){
							TextRequest = "THB,TWD".toString().trim();
						}else if (TextRequest.equals("singapore dollar")){
							TextRequest = "THB,SGD".toString().trim();
						}else{
							TextRequest = "1";
						}
					}else if(StatusService == 3){
						if(TextRequest.equals("เปิด")){
							TextRequest = "on".toString().trim();
						}else if (TextRequest.equals("ปิด")){
							TextRequest = "off".toString().trim();
						}
					}
					AsyncCallWS task = new AsyncCallWS();
					task.execute();
					Status = 1;
	
				}
				else{		
					new DownloadXML().execute(URL);
				}
			} else {
				Txt = "ส่งคำสั่งไม่สำเร็จ";
				text_v.setText(Txt);
				tts.speak(Txt, TextToSpeech.QUEUE_FLUSH, null);
			}
			break;
		}
		}
	}

	private class DownloadXML extends AsyncTask<String, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setTitle("ติดต่อฐานข้อมูล XML");
			pDialog.setMessage("กำลังค้นหา...");
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
			text_v.setText("");
            if(!CheckInput()){
            	 Txt = "ไม่พบคำสั่งที่ต้องการ ลองใหม่อีกครั้ง";
            	 text_v.setText(Txt);
            	 tts.speak(Txt, TextToSpeech.QUEUE_FLUSH, null);
             }
			pDialog.dismiss();
		}
		private boolean CheckInput(){
			boolean Sts = false;
			for (int temp = 0; temp < nodelist.getLength(); temp++) {
				Node nNode = nodelist.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Name2 = getNode("SERVICENAME", eElement).toString();
					
					if(TextRequest.equals("ค้นหา")){
					Txt = text_v.getText() + getNode("SERVICENAME", eElement)+ "\n";
					text_v.setText(Txt);
					tts.speak(Txt, TextToSpeech.QUEUE_FLUSH, null);
					Sts = true;
					}
					
					else if(Name2.equals(TextRequest)){
					Name 		 += getNode("SERVICENAME", eElement)+ "\n";
					NAMESPACE 	 = getNode("NAMESPACE", eElement);
					URL2 		 = getNode("URL", eElement);
					SOAP_ACTION  = getNode("SOAP_ACTION", eElement);
					METHOD_NAME  = getNode("METHOD_NAME", eElement);
					Att 		 = getNode("ATTIBUTE", eElement);
					Infor		 = getNode("INFORMATION", eElement);
					Status = 1;
					Name = Name2;
					Txt = "เข้าสู่"+ Name2 +"\n" +Infor;
					text_v.setText(Txt);
					tts.speak(Txt, TextToSpeech.QUEUE_FLUSH, null);
					Sts = true;
						if(TextRequest.equals("บริการสกุลเงิน")){
							StatusService = 1;
						}else if(TextRequest.equals("บริการแปลงองศา")){
							StatusService = 2;						
						}else if(TextRequest.equals("บริการหลอดไฟ 1")||
								TextRequest.equals("บริการหลอดไฟ 2")||
								TextRequest.equals("บริการหลอดไฟ 3")||
								TextRequest.equals("บริการหลอดไฟ 4")){
							StatusService = 3;
						}
						
					}				
				}
			}
			return Sts;
		}
	}
	private static String getNode(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}
	private class AsyncCallWS extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			getTextResponse(TextRequest);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			text_v.setText(TextResponse);
			String str = TextResponse;
			tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
		}
		@Override
		protected void onPreExecute() {
			text_v.setText("กำลังค้นหา...");
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
	
	public void onDestroy() {
		super.onDestroy();
		if (tts != null)
			tts.shutdown();
	}
	
	public void getTextResponse(String TextRequest1){
		if(TextRequest1 != "1"){
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		String SttName[] = Att.split(",");
		String SttValue[] = TextRequest1.split(",");
		for(int i=0;i<SttName.length;i++){
			request.addProperty(SttName[i],SttValue[i]);
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);
					
		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			TextResponse = response.toString();
			if(StatusService == 1){
				TextResponse =  "อัตราการแลกเปลี่ยน " + TextRequest1 +" อยู่ที่ราคา "+ TextResponse;
			}else if(StatusService == 2){
				if(TextResponse.equals("Error")){
					TextResponse = "คำสั่งไม่ถูกต้อง กรุณาพูดค่าตัวเลข";
				}else if(TextResponse != "Error"){
					TextResponse = TextRequest1 +" องศาเซลเซียสเท่ากับ "+TextResponse +" องศาฟาเรนไฮน์";
				}
			}else if(StatusService == 3){
				if(TextResponse.equals("off")){
					TextResponse = "ทำการ เปิด ได้สำเร็จ";
				}else if(TextResponse.equals("on")){
					TextResponse = "ทำการ ปิด ได้สำเร็จ";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		}else{
			TextResponse = "ไม่มีคำสั่งนี้ ลองใหม่อีกครั้ง";
		}
	}
}
