package com.martenumberto.smartcar;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by marten on 07.12.16.
 */

public class setState extends Activity {

    public static final String HOST = "192.168.178.50";
    public static final Integer PORT = 7072;
    public static final String tag = "setState";

    PrintWriter out;

    public void setState(final String device, final String value) {

        Log.d(tag, "setState()");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket client = new Socket(HOST, PORT);

                    out = new PrintWriter(client.getOutputStream(), true);

                    String command = "set " + device + " " + value + "\n";
                    Log.d(tag, "send...");
                    out.printf(command);

                    client.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
