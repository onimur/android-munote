package com.onimus.munote.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onimus.munote.R;
import com.onimus.munote.files.MenuToolbar;

import static com.onimus.munote.Constants.*;

public class DialogContractActivity extends MenuToolbar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_contract_screen);
        //Envia o Dialogo do Contrato
        setContractDialog();

    }

    private void setContractDialog() {
        if (!getDialogStatus()) {
            View view = View.inflate(this, R.layout.checkbox_contract, null);
            final CheckBox cb_contract = view.findViewById(R.id.checkbox);
            final TextView textView = view.findViewById(R.id.textview);

            //Justifica o texto
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            } else {
                WebView webView = new WebView(view.getContext());
                webView.setVerticalScrollBarEnabled(false);

                LinearLayout linearLayout = view.findViewById(R.id.linear_layout);
                linearLayout.removeView(textView);
                linearLayout.addView(webView);

                String newContentString = String.valueOf(fromHtml("<![CDATA[<body style=\"text-align:justify;color:gray;background-color:white; \">"
                        + getResources().getString(R.string.message_dialog_terms)
                        + "</body>]]>"));

                webView.loadData(newContentString, "text/html; charset=utf-8", "utf-8");

            }
            cb_contract.setText(getString(R.string.text_cb_terms));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.title_dialog_terms)
                    .setView(view)
                    .setCancelable(false)
                    .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (!cb_contract.isChecked()) {
                                storeDialogStatus(false);
                                DialogContractActivity.this.finish();
                            } else {
                                storeDialogStatus(true);
                                //carrega a próxima activity
                                callActivity(getBaseContext(), MenuActivity.class);

                            }

                        }
                    })
                    .setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            storeDialogStatus(false);
                            dialog.cancel();
                            DialogContractActivity.this.finish();
                        }
                    });
            AlertDialog mDialog = builder.create();
            mDialog.show();
        } else {
            //carrega a próxima activity
            callActivity(getBaseContext(), MenuActivity.class);
        }

    }

    private void storeDialogStatus(boolean isChecked) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_USER, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putBoolean(CHECK_TERMS, isChecked);
        mEditor.apply();
    }

    private boolean getDialogStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_USER, MODE_PRIVATE);
        return sharedPreferences.getBoolean(CHECK_TERMS, false);
    }

}
