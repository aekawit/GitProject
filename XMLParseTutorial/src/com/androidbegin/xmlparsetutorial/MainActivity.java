package com.androidbegin.xmlparsetutorial;

import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView textview;
	NodeList nodelist;
	ProgressDialog pDialog;
	String URL = "https://cfe132ba205f9ee267410edcbcb01ddc5eeeed6d.googledrive.com/host"
			+ "/0B1Pubw_66OGsfjdhUTJDNEJ5czNjOGF2VFJBcXQyTnJ1WVlISkpTSWNSN2drWW54N00wX1U/device2.xml";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textview = (TextView) findViewById(R.id.text);
		new DownloadXML().execute(URL);
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

			for (int temp = 0; temp < nodelist.getLength(); temp++) {
				Node nNode = nodelist.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					textview.setText(textview.getText() + "Name : "
							+ getNode("SERVICENAME", eElement) + "\n");
					textview.setText(textview.getText() + "NameSpace : "
							+ getNode("NAMESPACE", eElement) + "\n");
					textview.setText(textview.getText() + "URL : "
							+ getNode("URL", eElement) + "\n");
					textview.setText(textview.getText() + "SoapAction : "
							+ getNode("SOAP_ACTION", eElement) + "\n");
					textview.setText(textview.getText() + "MethodName : "
							+ getNode("METHOD_NAME", eElement) + "\n");
					textview.setText(textview.getText() + "Attibute : "
							+ getNode("ATTIBUTE", eElement) + "\n" + "\n" + "\n");
				}
			}
			// Close progressbar
			pDialog.dismiss();
		}
	}

	// getNode function
	private static String getNode(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}
}