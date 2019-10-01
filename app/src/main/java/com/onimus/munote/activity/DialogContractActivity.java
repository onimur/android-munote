package com.onimus.munote.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.onimus.munote.R;
import com.onimus.munote.files.MenuToolbar;

import static com.onimus.munote.Constants.*;

public class DialogContractActivity extends MenuToolbar {

    private View view;
    private LinearLayout linearLayout;
    private ScrollView scrollView;
    private CheckBox cb_contract;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_contract_screen);

        view = View.inflate(this, R.layout.checkbox_contract, null);
        linearLayout = view.findViewById(R.id.linear_layout);
        scrollView = view.findViewById(R.id.scrollView);
        cb_contract = view.findViewById(R.id.checkbox);
        textView = view.findViewById(R.id.textview);

        //Envia o Dialogo do Contrato
        setContractDialog();
    }

    private void setContractDialog() {
        if (!getDialogStatus()) {
            //Justifica o texto
            setJustifiedText();

            cb_contract.setText(getString(R.string.text_cb_terms));

            //Envia os parametros paro o Scrollview
            setLayoutParamsToScrollView();

            //Cria o alerta
            setTermsAlertDialog();
        } else {
            //carrega a próxima activity
            callActivity(getBaseContext(), MenuActivity.class);
        }
    }

    private void setTermsAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_dialog_terms)
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.text_ok, null)
                .setNegativeButton(R.string.btn_cancel, (dialog, which) -> {
                    //Ao cancelar o dialogo é cancelado e o aplicativo fechado
                    storeDialogStatus(false);
                    dialog.cancel();
                    DialogContractActivity.this.finish();
                });

        final AlertDialog mDialog = builder.create();
        //Criei o método do Button Postive aqui para quando clicar em ok e o checkbox não tiver sido
        //selecionado, então manda um toast e não fecha o alert dialog
        mDialog.setOnShowListener(dialog -> {
            Button ok = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            ok.setOnClickListener(v -> {
                if (!cb_contract.isChecked()) {
                    storeDialogStatus(false);
                    setMessage(getApplicationContext(), R.string.toast_necessary_accept, true);
                } else {
                    storeDialogStatus(true);
                    //carrega a próxima activity
                    callActivity(getBaseContext(), MenuActivity.class);
                }
            });
        });

        mDialog.show();
    }

    private void setLayoutParamsToScrollView() {
        int height = getHeightScreen() * 2 / 5;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);

        scrollView.setLayoutParams(layoutParams);
    }

    private void setJustifiedText() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        } else {
            WebView webView = new WebView(view.getContext());
            webView.setVerticalScrollBarEnabled(false);

            linearLayout.removeView(textView);
            linearLayout.addView(webView);

            String newContentString = String.valueOf(fromHtml("<![CDATA[<body style=\"text-align:justify;color:gray;background-color:white; \">"
                    + getResources().getString(R.string.message_dialog_terms)
                    + "</body>]]>"));

            webView.loadData(newContentString, "text/html; charset=utf-8", "utf-8");
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
