package com.martenumberto.smartcar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 */
public class LightFragment extends Fragment {

    setState set = new setState();

    public LightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_light, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Switch lightincar = (Switch) getView().findViewById(R.id.switch_lightincar);
        Switch lightfoot = (Switch) getView().findViewById(R.id.switch_lightfoot);

        new getState(getContext(), getView()).execute("LightInCar");
        new getState(getContext(), getView()).execute("LightFoot");

        lightincar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    set.setState("LightInCar", "on");
                } else if (!b) {
                    set.setState("LightInCar", "off");
                }
            }
        });

        lightfoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    set.setState("LightFoot", "on");
                } else if (!b) {
                    set.setState("LightFoot", "off");
                }
            }
        });

    }
}
