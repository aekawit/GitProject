package com.example.testdb;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	// ��˹��������ͧ��� php 㹡�ô֧������
	// �ҡ������ͧ����ѹ Emulator �� Server ����� IP 10.0.2.2
	// (����ͧ����ͧ)
	public static final String KEY_SERVER = "http://10.80.41.12/android/getData.php";

	
	private TextView txtJson; // Text �ʴ������� JSON
	private TextView txtResult; // Text �ʴ������ŷ����ҡ Server

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ����� View
		txtJson = (TextView) findViewById(R.id.txtJson);
		txtResult = (TextView) findViewById(R.id.txtResult);

		// �ӡ�ô֧����������ѧ��ѹ getServerData()
		txtResult.setText("RESULT : \n" + getServerData());
	}

	
	
	private String getServerData() {
		String returnString = "";
		InputStream is = null;
		String result = "";
		// ��ǹ�ͧ��á�˹���������������Ѻ php
		// ��ǹ�������ö����ء�����㹡���������������Ѻ Server ��
		// �ҡ������ҧ�觤�� moreYear ����դ�� 1990
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		// �觤�� POST DATA ��� PHP
		nameValuePairs.add(new BasicNameValuePair("moreYear", "1990"));

		// ��ǹ�ͧ����������͡Ѻ http ���ʹ֧������
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(KEY_SERVER);
			// update 17-11-2011 ������� post �� utf-8
			// �����������ջѭ�ҡѺ������
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// ��ǹ�ͧ����ŧ���Ѿ�����������ٻẺ�ͧ String
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-11"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// ��ǹ�ͧ����ŧ������ JSON �͡����ٻẺ�ͧ�����ŷ�������͹����
		try {
			// �ʴ����͡����ٻẺ�ͧ JSON
			txtJson.setText("JSON : \n" + result);

			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);

				// ����� Log �����ͻ�ͧ�ѹ��ͼԴ��Ҵ
				Log.i("log_tag",
						"id: " + json_data.getInt("id") + ", name: "
								+ json_data.getString("name") + ", sex: "
								+ json_data.getString("sex") + ", birthyear: "
								+ json_data.getInt("birthyear"));

				// �Ӣ������������������ʴ����
				returnString += "\nID : " + json_data.getInt("id")
						+ "\nName : " + json_data.getString("name")
						+ "\nSex : " + json_data.getString("sex")
						+ "\nBirthyear: " + json_data.getInt("birthyear")
						+ "\n";
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		// �觼��Ѿ����ʴ�� txtResult
		return returnString;
	}
}
