package com.aeke.Project;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.mainapp.R;
import com.example.mainapp.R.menu;

import android.R.bool;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData.Item;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.net.Uri;
import android.content.pm.PackageManager;

public class MainActivity extends Activity {

	String NAMESPACE = "http://www.pttplc.com/ptt_webservice/";
	String URL = "http://www.pttplc.com/webservice/pttinfo.asmx";
	String SOAP_ACTION = "http://www.pttplc.com/ptt_webservice/GetOilPrice";
	String METHOD_NAME = "GetOilPrice";
	String TAG = "aeke";
	String[] aString,attrVal;
	String TextResponse = "TextResponse";
	protected static final int RESULT_SPEECH = 1;
	final static int INTENT_CHECK_TTS = 0;
	final static String SVOX_TTS_ENGINE = "com.svox.classic";
	TextToSpeech tts;
	ImageButton btnSpeak;
	Button btn;
	//TextView text_v;
	EditText text_v;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		checkTTSEngineInstalled(SVOX_TTS_ENGINE);
		final MediaPlayer mpBtn = MediaPlayer.create(this, R.raw.buttonclickk);
		
		btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText editText1 = (EditText) findViewById(R.id.editText1);
				
				aString = editText1.getText().toString().trim().split(",");
				attrVal = new String[]{
						"Language",
						"DD",
						"MM",
						"YYYY"
				};
				HashMap<String, Object> hm = new HashMap<>();
				for(int i=0;i<attrVal.length;i++){
					hm.put(attrVal[i],aString[i]);
				}
				AsyncCallWS task = new AsyncCallWS();
				task.execute(hm);
			}
		});
		
		
		text_v = (EditText) findViewById(R.id.tv_result);
		text_v.setText("������ ����͵�ͧ�����觧ҹ");
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		btnSpeak.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				text_v.setText("");
				mpBtn.start();
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "th");
				try {
					startActivityForResult(intent, RESULT_SPEECH);

				} catch (ActivityNotFoundException a) {
					Toast t = Toast
							.makeText(
									getApplicationContext(),
									"�Դ��Ҵ ! ����ͧ�ͧ�س�ѧ�����Դ��� ��������§�繢�ͤ���",
									Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		final MediaPlayer mpBtn3 = MediaPlayer.create(this, R.raw.buttonclickk);
		int id = item.getItemId();
		if (id == R.id.search_icon) {
			mpBtn3.start();
			Intent intent = new Intent(getApplicationContext(),
					Main2Activity.class);
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);

	}

	public void checkTTSEngineInstalled(String packageName) {
		boolean isSvoxInstalled = isAppInstalled(packageName);
		if (isSvoxInstalled) {
			if (tts == null)
				tts = new TextToSpeech(MainActivity.this, null, packageName);
		} else if (!isSvoxInstalled) {
			Toast.makeText(getApplicationContext(),
					"��سҵԴ��� SVOX Text-to-Speech Engine",
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {
				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				text_v.setText(text.get(0).trim());
			}
			if (text_v.getText().length() != 0
					&& text_v.getText().toString() != "") {
				
/*				aString = text_v.getText().toString().trim();
				Toast toast = Toast.makeText(this, aString,
						Toast.LENGTH_LONG);
				toast.show();*/

				AsyncCallWS task = new AsyncCallWS();

			} else {

				text_v.setText("�س�ѧ������������ ��س��������");
			}
			break;
		}
		}

	}

	private class AsyncCallWS extends AsyncTask<HashMap<String,Object>, Void, Void> {


		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG, "onPostExecute");
			text_v.setText(TextResponse);

			String str = TextResponse;
			tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
		}

		@Override
		protected void onPreExecute() {
			Log.i(TAG, "onPreExecute");
			text_v.setText("���ѧ�����ż�...");
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.i(TAG, "onProgressUpdate");
		}

		@Override
		protected Void doInBackground(HashMap<String, Object>... params) {
			
			getTextResponse(params[0]);
			return null;
		}
	}

	public void onDestroy() {
		super.onDestroy();
		if (tts != null)
			tts.shutdown();
	}

	public void getTextResponse(HashMap<String,Object> attr) {
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
/*		PropertyInfo TextRequestPI = new PropertyInfo();
		TextRequestPI.setName("Celsius");
		TextRequestPI.setValue(TextRequest1);
		TextRequestPI.setType(double.class);
		
		request.addProperty(TextRequestPI);*/

    	for(String kString : attr.keySet()){
   		request.addProperty(kString,attr.get(kString));
    	}
    	  

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			TextResponse = response.toString();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
