package com.martenumberto.smartcar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by marten on 09.12.16.
 */

public class InfoDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View inflatedView = inflater.inflate(R.layout.dialog_info, null);

        TextView endtext = (TextView) inflatedView.findViewById(R.id.txt_info_end);
        endtext.setText(getString(R.string.author) + " - " + getString(R.string.start_year));

        builder.setView(inflatedView)
                .setNegativeButton("Alles Klar!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}
