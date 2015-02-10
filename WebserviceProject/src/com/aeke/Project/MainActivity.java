package com.aeke.Project;

import java.util.ArrayList;
import java.util.Locale;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.mainapp.R;
import com.example.mainapp.R.menu;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.net.Uri;
import android.content.pm.PackageManager;

public class MainActivity extends Activity {

	private final String NAMESPACE = "http://tempuri.org/";
	private final String URL = "http://192.168.50.22:8080/webservice.asmx";
	private final String SOAP_ACTION = "http://tempuri.org/on";
	private final String METHOD_NAME = "on";
	private String TAG = "aeke";
	private static String TextRequest;
	private static String TextResponse;
	private static boolean light = false;
	private static boolean fan = false;
	private static boolean air = false;
	private static boolean tv = false;
	protected static final int RESULT_SPEECH = 1;
	final static int INTENT_CHECK_TTS = 0;
	final static String VAJA_TTS_ENGINE = "com.spt.tts.vaja";
	final static String SVOX_TTS_ENGINE = "com.svox.classic";
	TextToSpeech tts;
	ImageButton btnSpeak;
	TextView text_v;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		checkTTSEngineInstalled(SVOX_TTS_ENGINE);
		final MediaPlayer mpBtn = MediaPlayer.create(this, R.raw.buttonclickk);
		
		text_v = (TextView) findViewById(R.id.tv_result);
		text_v.setText("������ ������觧ҹ");
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		btnSpeak.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				text_v.setText("");
				checkTTSEngineInstalled(SVOX_TTS_ENGINE);
				mpBtn.start();
				
				
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
				try {
					startActivityForResult(intent, RESULT_SPEECH);
					
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),"�Դ��Ҵ ! ����ͧ�ͧ�س�ѧ�����Դ��� ��������§�繢�ͤ���",
					Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});

		/*
		 * Intent intent = new Intent();
		 * intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		 * startActivityForResult(intent, INTENT_CHECK_TTS);
		 */
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.main, menu);
        return true;*/
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu); 
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		final MediaPlayer mpBtn3 = MediaPlayer.create(this, R.raw.buttonclickk);
		int id = item.getItemId();
		if (id == R.id.search_icon) {
			mpBtn3.start();
			Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
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
			Toast.makeText(getApplicationContext(),"��سҵԴ��� SVOX Text-to-Speech Engine",
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


		final MediaPlayer mpBtn2 = MediaPlayer.create(this, R.raw.buttonclicklast);
		
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

				AsyncCallWS task = new AsyncCallWS();
				task.execute();
				mpBtn2.start();
			} else {

				text_v.setText("�س�ѧ������������ ��س��������");
			}
			break;
		}
		}
		
//		  if (requestCode == INTENT_CHECK_TTS) { if (resultCode ==
//		  TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) { tts = new
//		  TextToSpeech(MainActivity.this, new OnInitListener() { public void
//		  onInit(int arg0){ tts.setLanguage(Locale.US); } }); } else { Intent
//		  intent = new Intent();
//		  intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
//		  startActivity(intent); } }
		
	}

	
	
	private class AsyncCallWS extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			Log.i(TAG, "doInBackground");
			getTextResponse(TextRequest);
			return null;
		}

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
			text_v.setText("���ѧ�ӹǳ��...");
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.i(TAG, "onProgressUpdate");
		}
	}

	public void onDestroy() {
		super.onDestroy();
		if (tts != null)
			tts.shutdown();
	}

	public boolean checkStatus(String Voice) {

		if (Voice.equals("�Դ�") && !light) { // !light = light=false
			return true;
		} else if (Voice.equals("�Դ�") && light) {
			return true;
		} else if (Voice.equals("�Դ�Ѵ��") && !fan) {
			return true;
		} else if (Voice.equals("�Դ�Ѵ��") && fan) {
			return true;
		} else if (Voice.equals("�Դ����") && !tv) {
			return true;
		} else if (Voice.equals("�Դ����") && tv) {
			return true;
		} else if (Voice.equals("�Դ����") && !air) {
			return true;
		} else if (Voice.equals("�Դ����") && air) {
			return true;
		}else if (Voice.equals("�Դ������")) {
			light = false;
			fan = false;
			tv = false;
			air = false;
			return false;
		}	
		else {
			return false;
		}
	}

	public void getTextResponse(String TextRequest) {
		if (TextRequest.trim().equals("ʶҹ�".trim())) {
			TextResponse = "";
			if (light == true) {
				TextResponse += "� ���ѧ�ӧҹ ";
			}
			if (fan == true) {
				TextResponse += "�Ѵ�� ���ѧ�ӧҹ ";
			}
			if (air == true) {
				TextResponse += "���� ���ѧ�ӧҹ ";
			}
			if (tv == true) {
				TextResponse += "���� ���ѧ�ӧҹ ";
			}

			if (light == false && fan == false && air == false && tv == false) {
				TextResponse = "��й�� ����ա�÷ӧҹ�";
			}
		} else {

			if (checkStatus(TextRequest)) {
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				PropertyInfo TextRequestPI = new PropertyInfo();
				TextRequestPI.setName("TextRequest");
				TextRequestPI.setValue(TextRequest);
				TextRequestPI.setType(double.class);
				request.addProperty(TextRequestPI);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

				try {
					androidHttpTransport.call(SOAP_ACTION, envelope);
					SoapPrimitive response = (SoapPrimitive) envelope
							.getResponse();
					TextResponse = response.toString();

					if (TextResponse.trim().equals("L1".trim())) {
						TextResponse = "�س�ӡ�� �Դ� �������";
						light = true;
					} else if (TextResponse.trim().equals("L0".trim())) {
						TextResponse = "�س�ӡ�� �Դ� �������";
						light = false;
					} else if (TextResponse.trim().equals("F1".trim())) {
						TextResponse = "�س�ӡ�� �Դ�Ѵ�� �������";
						fan = true;
					} else if (TextResponse.trim().equals("F0".trim())) {
						TextResponse = "�س�ӡ�� �Դ�Ѵ�� �������";
						fan = false;
					} else if (TextResponse.trim().equals("A1".trim())) {
						TextResponse = "�س�ӡ�� �Դ���� �������";
						air = true;
					} else if (TextResponse.trim().equals("A0".trim())) {
						TextResponse = "�س�ӡ�� �Դ���� �������";
						air = false;
					} else if (TextResponse.trim().equals("T1".trim())) {
						TextResponse = "�س�ӡ�� �Դ���� �������";
						tv = true;
					} else if (TextResponse.trim().equals("T0".trim())) {
						TextResponse = "�س�ӡ�� �Դ���� �������";
						tv = false;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			else {
				if (TextRequest.trim().equals("�Դ�".trim())
						|| TextRequest.trim().equals("�Դ�Ѵ��".trim())
						|| TextRequest.trim().equals("�Դ����".trim())
						|| TextRequest.trim().equals("�Դ����".trim())) {

					TextResponse = "�ػ�ó�ӧҹ��������";

				} else if (TextRequest.trim().equals("�Դ�".trim())
						|| TextRequest.trim().equals("�Դ�Ѵ��".trim())
						|| TextRequest.trim().equals("�Դ����".trim())
						|| TextRequest.trim().equals("�Դ����".trim())) {

					TextResponse = "ʶҹ� �ѧ���ӧҹ";
				}else if (TextRequest.trim().equals("�Դ������".trim())){

					TextResponse = "�Դ��÷ӧҹ ����������";
				} else {
					TextResponse = "�س����������١��ͧ �ͧ�����ա����";
				}
			}

		}

	}

}
