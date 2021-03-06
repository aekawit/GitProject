package com.example.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class HandleXML {

	private String country = "deviceName";
	private String temperature = "temperature";
	private String humidity = "humidity";
	private String pressure = "pressure";
	private String urlString = null;
	private XmlPullParserFactory xmlFactoryObject;
	public volatile boolean parsingComplete = true;
	
	private ArrayList<String> AllWebService = new ArrayList<String>();
	private ArrayList<String> listNamespace = new ArrayList<String>();
	private ArrayList<String> listURL = new ArrayList<String>();
	private ArrayList<String> listSoap_action = new ArrayList<String>();
	private ArrayList<String> listMethod = new ArrayList<String>();
	private ArrayList<String> listVar = new ArrayList<String>();
	

	public HandleXML(String url) {
		this.urlString = url;
	}
	public ArrayList<String> getAllWebService() {
		return AllWebService;
	}
	public ArrayList<String> getlistNamespace() {
		return listNamespace;
	}
	public ArrayList<String> getlistURL() {
		return listURL;
	}
	public ArrayList<String> getlistSoap_action() {
		return listSoap_action;
	}
	public ArrayList<String> getlistMethod() {
		return listMethod;
	}
	public ArrayList<String> getlistVar() {
		return listVar;
	}

	public void parseXMLAndStoreIt(XmlPullParser myParser) {
		int event;
		String text = null;
		try {
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) { 
				String name = myParser.getName();
				switch (event) {
					case XmlPullParser.START_TAG:
						break;
						
					case XmlPullParser.TEXT: 
						text = myParser.getText();
						break;
	
					case XmlPullParser.END_TAG:{  
						if(name.equals("serviceName")){	
							AllWebService.add(text);
						}
						else if(name.equals("namespace")){
							listNamespace.add(text);
						}
						else if(name.equals("functionURL")){
							listURL.add(text);
						}
						else if(name.equals("valueURL")){
							listSoap_action.add(text);
						}
						else if(name.equals("functionName")){
							listMethod.add(text);
						}
						else if(name.equals("valueName")){
							listVar.add(text);
						}
						break;
					}
				}
				event = myParser.next();
			}
			parsingComplete = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void fetchXML() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(urlString);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setReadTimeout(10000 /* milliseconds */);
					conn.setConnectTimeout(15000 /* milliseconds */);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.connect();
					InputStream stream = conn.getInputStream();

					xmlFactoryObject = XmlPullParserFactory.newInstance();
					XmlPullParser myparser = xmlFactoryObject.newPullParser();

					myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
					myparser.setInput(stream, null);
					parseXMLAndStoreIt(myparser);
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();

	}

}