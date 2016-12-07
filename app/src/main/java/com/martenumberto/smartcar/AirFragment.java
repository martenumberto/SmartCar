package com.martenumberto.smartcar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class AirFragment extends Fragment {

    private static final String tag = "AirFragment";
    setState set = new setState();

    public AirFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_air, container, false);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SeekBar barLinks = (SeekBar) getView().findViewById(R.id.barTempLinks);
        SeekBar barRechts = (SeekBar) getView().findViewById(R.id.barTempRechts);
        ToggleButton btnDry = (ToggleButton) getView().findViewById(R.id.btnDry);
        ToggleButton btnEC = (ToggleButton) getView().findViewById(R.id.btnEC);
        ToggleButton btnUML = (ToggleButton) getView().findViewById(R.id.btnUML);


        new getState(getContext(), getView()).execute("TempLinks");
        new getState(getContext(), getView()).execute("TempRechts");
        new getState(getContext(), getView()).execute("Lufttrockner");
        new getState(getContext(), getView()).execute("ECSwitch");
        new getState(getContext(), getView()).execute("UMLuft");

        barLinks.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String converted = String.valueOf(progress);
                Log.d(tag, converted);
                set.setState("TempLinks", converted);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        barRechts.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String converted = String.valueOf(progress);
                set.setState("TempRechts", converted);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnDry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    set.setState("Lufttrockner", "on");
                } else {
                    set.setState("Lufttrockner", "off");
                }
            }
        });
        btnEC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    set.setState("ECSwitch", "on");
                } else {
                    set.setState("ECSwitch", "off");
                }
            }
        });
        btnUML.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    set.setState("UMLuft", "on");
                } else {
                    set.setState("UMLuft", "off");
                }
            }
        });


    }
}
