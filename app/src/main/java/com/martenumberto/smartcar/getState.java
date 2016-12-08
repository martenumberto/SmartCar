package com.martenumberto.smartcar;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by marten on 07.12.16.
 */

public class getState extends AsyncTask<String, Void, String> {


    public static final String HOST = "192.168.178.50";
    public static final Integer PORT = 7072;
    public static final String tag = "getState AsyncTask";
    public Boolean isConnected = false;
    PrintWriter out;
    BufferedReader in;

    MainActivity main = new MainActivity();
    StartFragment start = new StartFragment();

    String device;

    Context context;
    View view;

    getState(Context c, View v) {
        context = c;
        view = v;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Socket client = new Socket(HOST, PORT);

            isConnected = true;

            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            out.printf("getstate " + params[0] + "\n");
            this.device = params[0];

            Log.d(tag, "getstate");

            String answer = in.readLine().toString();

            Log.d(tag, answer);

            client.close();
            isConnected = false;

            return answer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ANTWORT LEER";
    }

    @Override
    protected void onPostExecute(String r) {

        String[] sepperated = r.split(":");
        //sepperated[0]; ist "status:"
        String result = sepperated[1].trim();

        if (device == "UMLuft") {
            ToggleButton btn = (ToggleButton) view.findViewById(R.id.btnUML);
            if (Integer.parseInt(result) == 1) {
                btn.setChecked(true);
            } else {
                btn.setChecked(false);
            }

        } else if (device == "TempLinks") {
            SeekBar bar = (SeekBar) view.findViewById(R.id.barTempLinks);
            bar.setProgress(Integer.parseInt(result));
        } else if (device == "TempRechts") {
            SeekBar bar = (SeekBar) view.findViewById(R.id.barTempRechts);
            bar.setProgress(Integer.parseInt(result));
        } else if (device == "Lufttrockner") {
            ToggleButton btn = (ToggleButton) view.findViewById(R.id.btnDry);
            if (Integer.parseInt(result) == 1) {
                btn.setChecked(true);
            } else {
                btn.setChecked(false);
            }
        } else if (device == "ECSwitch") {
            ToggleButton btn = (ToggleButton) view.findViewById(R.id.btnEC);
            if (Integer.parseInt(result) == 1) {
                btn.setChecked(true);
            } else {
                btn.setChecked(false);
            }
        } else if (device == "LightInCar") {
            Switch btn = (Switch) view.findViewById(R.id.switch_lightincar);
            if (Integer.parseInt(result) == 1) {
                btn.setChecked(true);
            } else {
                btn.setChecked(false);
            }
        } else if (device == "LightFoot") {
            Switch btn = (Switch) view.findViewById(R.id.switch_lightfoot);
            if (Integer.parseInt(result) == 1) {
                btn.setChecked(true);
            } else {
                btn.setChecked(false);
            }
        }

        Log.d(tag, "Result: " + result + "\nDevice : " + device);

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
