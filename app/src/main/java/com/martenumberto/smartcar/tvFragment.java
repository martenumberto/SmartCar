package com.martenumberto.smartcar;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class tvFragment extends Fragment {

    String url_to_json = "http://192.168.178.50:9981/api/channel/grid";

    int vlcRequestCode = 4222;

    private ListView lv;
    ProgressDialog progress;
    ArrayList<HashMap<String, String>> TVArrayList;

    public tvFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lv = (ListView) getView().findViewById(R.id.tvList);

        TVArrayList = new ArrayList<>();

        new getTVitems().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView clickedChannel = (TextView) view.findViewById(R.id.tv_list_name);
                TextView txtservices = (TextView) view.findViewById(R.id.tv_list_services);

                String services = txtservices.getText().toString();
                String titel = clickedChannel.getText().toString();

                services = services.replace("[", "");
                services = services.replace("\"", "");
                services = services.replace("]", "");
                services = services.trim();

                Uri uri = Uri.parse("http://192.168.178.50:9981/play/stream/service/" + services);
                Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
                vlcIntent.setPackage("org.videolan.vlc");
                vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
                vlcIntent.putExtra("title", titel);
                startActivityForResult(vlcIntent, vlcRequestCode);

            }
        });

    }

    private class getTVitems extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(getActivity());
            progress.setMessage("Bitte warten");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... objects) {

            URL url = null;
            try {
                url = new URL(url_to_json);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder strBuilder = new StringBuilder();

                String line;

                while ((line = in.readLine()) != null) {
                    strBuilder.append(line).append('\n');
                }

                if (strBuilder.toString() != null) {
                    JSONObject jsonObj = new JSONObject(strBuilder.toString());
                    JSONArray entries = jsonObj.getJSONArray("entries");

                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject c = entries.getJSONObject(i);

                        String uuid = c.getString("uuid");
                        String name = c.getString("name");
                        String services = c.getString("services");

                        HashMap<String, String> tv = new HashMap<>();
                        tv.put("uuid", uuid);
                        tv.put("name", name);
                        tv.put("services", services);

                        TVArrayList.add(tv);

                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ListAdapter adapter = new SimpleAdapter(getActivity(), TVArrayList, R.layout.tv_list_item, new String[]{"uuid", "name", "services"}, new int[]{R.id.tv_list_uuid, R.id.tv_list_name, R.id.tv_list_services});

            lv.setAdapter(adapter);

            progress.hide();
        }
    }
}
