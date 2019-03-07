package com.simarro.practicas.domotiapp.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;

import com.simarro.practicas.domotiapp.pojos.Temperature;
import com.simarro.practicas.domotiapp.R;

import java.util.ArrayList;

public class TemperatureAdapter extends ArrayAdapter<Temperature> {
    ArrayList<Temperature> temperatures;
    Activity context;

    public TemperatureAdapter(Fragment context, ArrayList<Temperature> temperatures) {
        super(context.getActivity(), R.layout.ver_cuentas_activity, temperatures);
        this.temperatures = temperatures;
        this.context = context.getActivity();
    }
}
