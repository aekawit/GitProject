package com.example.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
 
public class ConnectServer extends AsyncTask<String, Integer, String>{
    
	private HttpPost httppost;
    private HttpClient httpclient;
    private List<NameValuePair> nameValuePairs;
    private DialogConnect dialogConnect;
    private Context context;
    String a ="";
    ConnectServer(Context context,String URL){
        this.context = context;
 
        //���ҧ��ǹ��Сͺ������㹡��������Ѻ Server
        this.httpclient = new DefaultHttpClient();
        this.httppost = new HttpPost(URL);
        this.nameValuePairs = new ArrayList<NameValuePair>();
 
        //���ҧ Dialog �͹�������͡Ѻ Server
        //�ա���� ConnectServer ���Ѻ Dialog ������㹡��¡��ԡ
        dialogConnect = new DialogConnect(this.context, this);
        dialogConnect.setTitle(this.context.getString(R.string.app_name));
        dialogConnect.setMessage("��س����ѡ����");
    }
 
    //Function ����Ѻ���������㹡���觤��Ẻ Post
    public void addValue(String key,String value){
        nameValuePairs.add(new BasicNameValuePair(key, value));
    }
 
    //��͹���з� doInBackground �зӧҹ��� Function ����͹
    protected void onPreExecute() {
        dialogConnect.show();
    }
 
    //������ӧҹẺ Background
    protected String doInBackground(String... params) {
        InputStream is = null;
        String result = null;
 
        //���������������Ѻ Server
        try {
            //�ӡ���觵���õ�ҧ� ��ٻẺ�ͧ UTF-8
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
 
            //��ҹ���Ѿ����ٻẺ�ͧ UTF-8
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
 
            is.close();
            result = sb.toString();
 
        //��Ң���������͡Ѻ Server �ջѭ�� ���ʴ� Log Error
        } catch (ClientProtocolException e) {
            Log.e("ConnectServer", e.toString());
        } catch (IOException e) {
            Log.e("ConnectServer", e.toString());
        }
 
        return result;
    }
 
    
    //��ҷӧҹ��� doInBackground �������� ���ҷӧҹ��� Function ���
    protected void onPostExecute(String result) {
        //list ������红�����
        ArrayList<String> list = new ArrayList<String>();
 
        //��� result �� null ��� �������ö�������͡Ѻ server ��
        //����������Ѻ server �� �зӧҹ���仹��
        if(result != null){
            //���������ŧ JSON �繢�����
            try {
                //�ŧ���Ѿ���������� JSON Object
                JSONObject jObject = new JSONObject(result);
 
                //��Ҷ֧�����Ũҡ database ����ռ��Ѿ�� status ��Ѻ����� OK
                if(jObject.getString("status").equals("OK")){
                    //�ŧ���Ѿ���������� JSON Array
                    JSONArray jResult = jObject.getJSONArray("result");
 
                    //�֧��Ҵ�ͧ������� jResult
                    int size = jResult.length();
 
                    //ǹ Loop ��Ҥ������ List
                    for(int i=0;i<size;i++){
                    	a = jResult.getJSONObject(i).getString("name");
                    	
                    	
                    	
                        String data = "ID : "+ jResult.getJSONObject(i).getString("id") + "\n"
                                      + "Name : " + jResult.getJSONObject(i).getString("name") + "\n"
                                      + "Age : " + jResult.getJSONObject(i).getString("age");
                        list.add(data);
                    }
 
                //��Ҵ֧�����Ũҡ database �ջѭ�Ҩ��ʴ� error
                }else{
                    ((MainActivity)context).errorConnectToServer();
                }
                ((MainActivity)context).setList(list);
 
            //��Ң���ŧ������ JSON �ջѭ�Ҩ��ҷӧҹ��ǹ���
            } catch (JSONException e) {
                Log.e("ConnectServer", "Error parsing data " + e.toString());
                ((MainActivity)context).errorConnectToServer();
            }
 
        //����������͡Ѻ server �����зӧҹ���仹��
        }else{
            ((MainActivity)context).cannotConnectToServer();
        }
 
        dialogConnect.dismiss();
        
    }
    
}