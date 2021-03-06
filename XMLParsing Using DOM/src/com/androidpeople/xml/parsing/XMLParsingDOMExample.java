package com.androidpeople.xml.parsing;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class XMLParsingDOMExample extends Activity {

	
	ArrayList<String> AllWebService = new ArrayList<String>();
	int a ;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Create a new layout to display the view */
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(1);

		/** Create a new textview array to display the results */
		TextView name[];
		TextView website[];
		TextView category[];
		
		try {

			URL url = new URL("https://cfe132ba205f9ee267410edcbcb01ddc5eeeed6d.googledrive.com/"
					+ "host/0B1Pubw_66OGsfjdhUTJDNEJ5czNjOGF2VFJBcXQyTnJ1WVlISkpTSWNSN2drWW54N00wX1U/example.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("service");
			NodeList nodeList2 = doc.getElementsByTagName("services");
			/** Assign textview array lenght by arraylist size */
			name = new TextView[nodeList2.getLength()];
			website = new TextView[nodeList2.getLength()];
			category = new TextView[nodeList2.getLength()];
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				
				name[i] = new TextView(this);
				website[i] = new TextView(this);
				category[i] = new TextView(this);
				
				Element fstElmnt = (Element) node;
				NodeList nameList = fstElmnt.getElementsByTagName("name");
					
				Element nameElement = (Element) nameList.item(0);
				nameList = nameElement.getChildNodes();
				name[i].setText("Name = "+ ((Node) nameList.item(0)).getNodeValue());
					
				AllWebService.add(((Node) nameList.item(0)).getNodeValue());
				layout.addView(name[i]);
				
			}
			
			for (int i = 0; i < nodeList2.getLength(); i++) {
				Node node = nodeList2.item(i);
				
				name[i] = new TextView(this);
				website[i] = new TextView(this);
				category[i] = new TextView(this);
				
				Element fstElmnt = (Element) node;
				NodeList websiteList = fstElmnt.getElementsByTagName("servicelist");
				Element websiteElement = (Element) websiteList.item(0);
				websiteList = websiteElement.getChildNodes();
				website[i].setText("ServiceList = "	+ ((Node) websiteList.item(0)).getNodeValue());
					
				//AllWebService.add(((Node) websiteList.item(0)).getNodeValue());
				layout.addView(website[i]);
											
			}
			Toast toast = Toast.makeText(this, AllWebService.toString(), Toast.LENGTH_LONG);
			toast.show();
					
			
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		/** Set the layout view to display */
		setContentView(layout);
		
	}
}
