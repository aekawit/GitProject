package com.androidpeople.xml.parsing;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

public class XMLParsingDOMExample extends Activity {

	String a = "";
	HashMap<String, Object> hm;
	ListView list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		/** Create a new layout to display the view */
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(1);
		list=(ListView)findViewById(R.id.gridView1);
		/** Create a new textview array to display the results */

		try {

			URL url = new URL("http://www.umr-lab.com/device.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();

			
			NodeList nodeList = doc.getElementsByTagName("device");
			
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}
	}
	
	private Element[] getnodeVal(NodeList nlist) {
		Element[] element=new Element[nlist.getLength()];
		for (int i = 0; i < nlist.getLength(); i++) {
			Node node = nlist.item(i);
			element[i] = (Element) node;
		}
		return element;
	}
	
}
