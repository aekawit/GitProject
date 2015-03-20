package com.example.db;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
 
public class DialogConnect extends ProgressDialog{
 
    private AsyncTask task;
 
    public DialogConnect(Context context,AsyncTask task) {
        //���ҧ Dialog �ҡ super class
        super(context);
 
        //����� AsyncTask (ConnectServer) ��ҡѺ Dialog
        this.task = task;
    }
 
    //����ա��¡��ԡ�͹��� Dialog ����Ҩ��ҷӧҹ Function ���
    public void cancel() {
        //¡��ԡ�������͡Ѻ Server
        task.cancel(true);
        super.cancel();
    }
 
}