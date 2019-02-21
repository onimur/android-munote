/*
 *
 *  * Created by Murillo Comino on 09/02/19 12:26
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 09/02/19 12:11
 *
 */

package com.example.admin.munotes.files;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.munotes.Constants;
import com.example.admin.munotes.R;
import com.example.admin.munotes.bancos.banco.HMAuxCard;
import com.example.admin.munotes.bancos.dao.CardDao;
import com.example.admin.munotes.bancos.dao.NotesDao;
import com.example.admin.munotes.bancos.model.Card;
import com.example.admin.munotes.bancos.model.Notes;

import org.w3c.dom.Text;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.admin.munotes.Constants.FOLDER_NAME;
import static com.example.admin.munotes.Constants.FOLDER_NAME_NOTES;
import static com.example.admin.munotes.Constants.ID_BANCO;
import static com.example.admin.munotes.files.FileUtilities.isAndroidMarshmallowOrSuperiorVersion;
import static com.example.admin.munotes.files.MoneyTextWatcher.formatPriceSave;

public class MainUtilities extends AppCompatActivity {


    public static void setActionOnClick(final View view, View.OnClickListener action) {
        if (view != null) {
            view.setOnClickListener(action);
        }
    }

    public void setActionOnClick(final int btn, View.OnClickListener action) {
        View v = findViewById(btn);
        setActionOnClick(v, action);
    }

    public void setActionOnClick(final int btn, AdapterView.OnItemSelectedListener action) {
        AdapterView v = findViewById(btn);
        setActionOnClick(v, action);
    }

    public static void setActionOnClick(final AdapterView view, AdapterView.OnItemSelectedListener action) {
        if (view != null) {
            view.setOnItemSelectedListener(action);
        }
    }


    public void setActionOnClickActivity(final int btn, final Class<?> _class) {
        setActionOnClick(btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callActivity(getApplicationContext(), _class);
            }
        });
    }

    public void setActionOnClickActivity(final int btn, final Class<?> _class, final long id) {
        setActionOnClick(btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callListView(_class, id);
            }
        });
    }


    //Esse retorno não necessita de botão, utilizado pra outros métodos e também pra quando o usuário
    //aperta o botão de retorno do celular, ou em algum item do menutoolbar.
    public void setAlertDialogToReturnOnClickActivity(final Class<?> _class, final String putType) {
        //Pega o String contido nos id das R.string.
        String idTitle = getString(R.string.alert_title_cancel);
        String idMessage = getString(R.string.alert_message_retorno);
        String idOK = getString(R.string.alert_ok);
        String idCancel = getString(R.string.alert_cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainUtilities.this);

        // 2. Una vários métodos de setter para definir as características do diálogo
        builder.setMessage(idMessage).setTitle(idTitle);

        //Adiciona os botões
        builder.setPositiveButton(idOK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int is) {

                switch (putType) {
                    //vem do CardAddActivity e retorna pra CardActivity;
                    case "cartao": {

                        callActivity(getApplicationContext(), _class);
                        //
                        break;
                    }
                    //vem de CardAddActivity e ao clicar no item notas do MenuToolbar vai pra NotesAddActivity;
                    case "cartao_notas": {
                        callListView(_class, -1L);
                        break;
                    }
                    //vem do NotesAddActivity e retorna pra NotesActivity;
                    case "notas": {
                        ImageUtilities.deleteImage();
                        callActivity(getApplicationContext(), _class);
                        break;
                    }
                    //vem do NotesAddActivity e ao clicar no item cartao do MenuToolbar vai pra CardAddActivity;
                    case "notas_cartao": {
                        ImageUtilities.deleteImage();
                        callListView(_class, -1L);
                        break;
                    }

                }
            }
        });
        builder.setNegativeButton(idCancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int is) {

            }
        });

        builder.setIcon(R.mipmap.logo_round);
        // 3. Obtenha o AlertDialog de create () e mostre
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //Inicia o alerta ao clicar em algum botão, e se clicar em ok, cancela operação retornando pra activity desejada.
    public void setAlertDialogToReturnOnClickActivity(final int btn, final Class<?> _class, final String putType) {
        setActionOnClick(btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlertDialogToReturnOnClickActivity(_class, putType);

            }
        });
    }

    //Alerta para quando tem ação no "SIM" e no "NÃO";
    public void setAlertDialogOnClickActivity(final Class<?> _classF, final Class<?> _classR, final long idAtual, final String putType) {
        switch (putType) {
            case "notas_cartao": {
                //Pega o String contido nos id das R.string.
                String idTitle = getString(R.string.tv_no_card);
                String idMessage = getString(R.string.alert_register_card);
                String idOK = getString(R.string.alert_ok);
                String idCancel = getString(R.string.alert_cancel);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainUtilities.this);

                // 2. Una vários métodos de setter para definir as características do diálogo
                builder.setMessage(idMessage).setTitle(idTitle);

                //Adiciona os botões
                builder.setPositiveButton(idOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int is) {

                        callListView(_classF, -1L);
                    }
                });
                builder.setNegativeButton(idCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int is) {
                        callListView(_classR, idAtual);

                    }
                });

                builder.setIcon(R.mipmap.logo_round);
                // 3. Obtenha o AlertDialog de create () e mostre
                AlertDialog dialog = builder.create();
                //////////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////////
                //FORÇA o usuário a escolher entre sim ou não.
                //////////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////////
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                dialog.show();

                break;
            }
        }
    }

    //Alerta para quando tem ação de delete.
    public void setAlertDialogDeleteOnClickActivity(final int btn, final Class<?> _class, final Context context, final long idAtual, final String putType) {
        setActionOnClick(btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Pega o String contido nos id das R.string.
                String idTitle = getString(R.string.alert_title_delete);
                String idMessage = getString(R.string.alert_message_delete);
                String idOK = getString(R.string.alert_ok);
                String idCancel = getString(R.string.alert_cancel);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainUtilities.this);

                // 2. Una vários métodos de setter para definir as características do diálogo
                builder.setMessage(idMessage).setTitle(idTitle);

                //Adiciona os botões
                builder.setPositiveButton(idOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int is) {
                        switch (putType) {
                            case "cartao": {
                                CardDao cardDao;
                                cardDao = new CardDao(context);
                                cardDao.deleteCard(idAtual);

                                NotesDao notesDao;
                                notesDao = new NotesDao(context);
                                notesDao.updateDeletedCard(idAtual);
                                break;
                            }
                            case "notas": {
                                NotesDao notesDao;
                                notesDao = new NotesDao(context);
                                Notes notes = notesDao.getNotesById(idAtual);
                                String caminho = notes.getFotonotas();
                                File path = new File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + FOLDER_NAME + FOLDER_NAME_NOTES));
                                File imgFile = new File(path, caminho);
                                ImageUtilities.deleteImage(imgFile);
                                notesDao.deleteNotes(idAtual);
                                break;

                            }
                        }
                        callActivity(getApplicationContext(), _class);

                    }
                });
                builder.setNegativeButton(idCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int is) {

                    }
                });

                builder.setIcon(R.mipmap.logo_round);
                // 3. Obtenha o AlertDialog de create () e mostre
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setAlertDialogUpdateOnClickActivity(final int btn, final Class<?> _class, final Context context, final long idAtual, final String putType) {
        setActionOnClick(btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (putType) {
                    case "cartao": {
                        if (validation(putType)) {
                            //Pega o String contido nos id das R.string.
                            String idTitle = getString(R.string.alert_title_update);
                            String idMessage = getString(R.string.alert_message_update);
                            String idOK = getString(R.string.alert_ok);
                            String idCancel = getString(R.string.alert_cancel);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainUtilities.this);

                            // 2. Una vários métodos de setter para definir as características do diálogo
                            builder.setMessage(idMessage).setTitle(idTitle);

                            //Adiciona os botões
                            builder.setPositiveButton(idOK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int is) {

                                    saveCard(idAtual, context);
                                    callListView(_class, idAtual);

                                }
                            });
                            builder.setNegativeButton(idCancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int is) {
                                }
                            });

                            builder.setIcon(R.mipmap.logo_round);
                            // 3. Obtenha o AlertDialog de create () e mostre
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        break;
                    }
                    ////////
                }

            }
        });
    }

    public void setAlertDialogUpdateOnClickActivity(final int btn, final Class<?> _class, final Context context, final long idAtual, final String putType, final String caminhoSemPath) {
        setActionOnClick(btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (putType) {
                    case "notas": {

                        if (validation(putType)) {
                            //Pega o String contido nos id das R.string.
                            String idTitle = getString(R.string.alert_title_update);
                            String idMessage = getString(R.string.alert_message_update_invoice);
                            String idOK = getString(R.string.alert_ok);
                            String idCancel = getString(R.string.alert_cancel);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainUtilities.this);

                            // 2. Una vários métodos de setter para definir as características do diálogo
                            builder.setMessage(idMessage).setTitle(idTitle);

                            //Adiciona os botões
                            builder.setPositiveButton(idOK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int is) {
                                    //Verifica se o imageview foi alterado, se for main então não, se for new, ele foi alterado;
                                    //Se for main a imagem gerada precisa ser deletada e o caminho da imagem no banco de dados
                                    // precisa ser deletado, se for new será salva;

                                    //  if (tagName.equals("main")) {
                                    //     ImageUtilities.deleteImage();
                                    //   saveNotes(idAtual, context);
                                    //    } else {

                                    saveNotes(idAtual, context, caminhoSemPath);
                                    //    }

                                    callActivity(context, _class);

                                }
                            });
                            builder.setNegativeButton(idCancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int is) {

                                }
                            });
                            builder.setIcon(R.mipmap.logo_round);
                            // 3. Obtenha o AlertDialog de create () e mostre
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        break;
                    }
                }


            }
        });
    }

    public void setAlertDialogUpdateOnClickActivity(final int btn, final Class<?> _class, final Context context, final long idAtual, final String putType, final String caminhoSemPath, final File imagemFile) {
        setActionOnClick(btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (putType) {
                    case "notas": {
                        if (validation(putType)) {
                            //Pega o String contido nos id das R.string.
                            String idTitle = getString(R.string.alert_title_update);
                            String idMessage = getString(R.string.alert_message_update_invoice);
                            String idOK = getString(R.string.alert_ok);
                            String idCancel = getString(R.string.alert_cancel);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainUtilities.this);

                            // 2. Una vários métodos de setter para definir as características do diálogo
                            builder.setMessage(idMessage).setTitle(idTitle);

                            //Adiciona os botões
                            builder.setPositiveButton(idOK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int is) {

                                    ImageUtilities.deleteImage(imagemFile);

                                    saveNotes(idAtual, context, caminhoSemPath);

                                    callActivity(context, _class);

                                }
                            });
                            builder.setNegativeButton(idCancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int is) {

                                }
                            });
                            builder.setIcon(R.mipmap.logo_round);
                            // 3. Obtenha o AlertDialog de create () e mostre
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        break;
                    }
                }


            }
        });
    }


    public void callListView(Class<?> _class, long id) {
        Intent mIntent = new Intent(getApplicationContext(), _class);
        mIntent.putExtra(ID_BANCO, id);
        //
        startActivity(mIntent);
        //
        finish();
    }

    //Action pra click do botão e inicar alguma ação/intent ao invés de startar uma Activity
    public void openESFileExplorer() {

        Uri selectedUri = Uri.parse(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + Constants.FOLDER_NAME));
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setDataAndType(selectedUri, "resource/folder");
        //Verifica se tem o ES File Explorer Instalado
        if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
            startActivity(Intent.createChooser(intent, "Open folder"));

        } else {
            setMessage(R.string.install_ES_File);
        }
    }


    public void clearField(EditText et, boolean b) {
        //limpa edittext e se for definido como true então o Focus volta pro editext;

        et.getText().clear();

        if (b) {
            et.requestFocus();
        }
    }

    public void clearField(CheckBox cb) {
        //limpa checkbox
        cb.setChecked(false);
    }

    public void callActivity(Context context, final Class<?> _class) {
        Intent mIntent = new Intent(context, _class);
        startActivity(mIntent);
        //
        finish();
    }

    protected void saveCard(long idAtual, Context context) {

        EditText et_desc_card;
        EditText et_number_card;
        CheckBox cb_credito;
        CheckBox cb_debito;
        CardDao cardDao = new CardDao(context);
        Card cAux = new Card();
        //
        et_desc_card = findViewById(R.id.et_desc_card);
        et_number_card = findViewById(R.id.et_number_card);
        cb_credito = findViewById(R.id.cb_credito);
        cb_debito = findViewById(R.id.cb_debito);

        //

        cAux.setDescartao(et_desc_card.getText().toString().trim());

        if (cb_credito.isChecked() && !cb_debito.isChecked()) {
            cAux.setTipo(1);
        }
        if (!cb_credito.isChecked() && cb_debito.isChecked()) {
            cAux.setTipo(2);
        }
        if (cb_credito.isChecked() && cb_debito.isChecked()) {
            cAux.setTipo(3);
        }
        if (et_number_card.getText().toString().isEmpty()) {
            cAux.setNumbercard("");
        } else {
            cAux.setNumbercard(et_number_card.getText().toString().trim());
        }

        //
        if (idAtual != -1) {
            cAux.setIdcartao(idAtual);
            //
            cardDao.updateCard(cAux);
        } else {
            idAtual = cardDao.nextID();
            cAux.setIdcartao(idAtual);
            //
            cardDao.insertCard(cAux);
        }

    }


    protected void saveNotes(long idAtual, Context context, String caminhoFoto) {

        EditText et_title_invoice;
        EditText et_value;
        EditText et_desc_invoice;
        Button btn_select_date;
        Spinner sp_card;
        Spinner sp_parcelas;
        RadioButton rb_debit;

        int year = -1;
        int month = -1;
        int day = -1;
        //
        String idCartao;
        //
        NotesDao notesDao = new NotesDao(context);
        //
        Notes cAux = new Notes();
        //
        et_desc_invoice = findViewById(R.id.et_desc_invoice);
        et_title_invoice = findViewById(R.id.et_title_invoice);
        et_value = findViewById(R.id.et_value);
        btn_select_date = findViewById(R.id.btn_selec_date);
        sp_card = findViewById(R.id.sp_card);
        sp_parcelas = findViewById(R.id.sp_parcelas);
        rb_debit = findViewById(R.id.rb_debit);

        //Transforma a data do textview calendário em Date, formata e salva separado;
        String date = btn_select_date.getText().toString();
        SimpleDateFormat dateF = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date dateD = dateF.parse(date);
            year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(dateD));
            month = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(dateD));
            day = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(dateD));


        } catch (ParseException e) {
            e.printStackTrace();
        }
        //
        //Recupera o ID do cartão que está selecionado no Spinner
        HMAuxCard item = (HMAuxCard) sp_card.getSelectedItem();
        idCartao = item.get(CardDao.IDCARTAO);
        //
       int positionSpParcelas = sp_parcelas.getSelectedItemPosition() + 1;

        //
        cAux.setDesnotas(et_desc_invoice.getText().toString());
        cAux.setTitlenotas(et_title_invoice.getText().toString());
        cAux.setPreconotas(formatPriceSave(et_value.getText().toString()));
        cAux.setIdcartao(convertToLong(idCartao));
        cAux.setFotonotas(caminhoFoto);
        cAux.setAno(year);
        cAux.setMes(month);
        cAux.setDia(day);
        //

        if (rb_debit.isChecked()) {
            cAux.setTipo(2);
            cAux.setParcelas(1);
        } else {
            cAux.setTipo(1);
            cAux.setParcelas(positionSpParcelas);
        }

        if (idAtual != -1) {
            cAux.setIdnotas(idAtual);
            //
            notesDao.updateNotes(cAux);

        } else {
            idAtual = notesDao.nextID();
            cAux.setIdnotas(idAtual);
            //
            notesDao.insertNotes(cAux);

        }

    }

    protected int convertToInt(String numero) {
        try {
            return Integer.parseInt(numero);
        } catch (Exception e) {
            return -1;
        }
    }

    private Long convertToLong(String numero) {
        try {
            return Long.valueOf(numero);
        } catch (Exception e) {
            return -1L;
        }
    }

    protected boolean validation(String putType) {
        //Faz validação, se tiver tudo certo, retorna verdadeiro.
        switch (putType) {
            case "notas": {
                LinearLayout ll_hint_spinner;
                EditText et_title_invoice;
                EditText et_value;
                Button btn_selec_date;
                TextView tv_select_card;
                //
                String title_invoice;
                String value;
                String select_date;

                //
                ll_hint_spinner = findViewById(R.id.ll_hint_spinner);
                et_title_invoice = findViewById(R.id.et_title_invoice);
                et_value = findViewById(R.id.et_value);
                btn_selec_date = findViewById(R.id.btn_selec_date);
                tv_select_card = findViewById(R.id.tv_select_card);
                //
                value = et_value.getText().toString();
                title_invoice = et_title_invoice.getText().toString();
                if (!value.isEmpty()){
                    value = formatPriceSave(value);
                }

                select_date = btn_selec_date.getText().toString();
                //
                if (title_invoice.trim().isEmpty()) {
                    setMessage(R.string.toast_title_invoice);
                    return false;
                }
                //
                if (value.trim().isEmpty() || value.equals("0.00") ) {
                    setMessage(R.string.toast_value_required);
                    return false;
                }
                //
                if (tv_select_card.getText().equals(getString(R.string.tv_choose_card)) && ll_hint_spinner.isEnabled()) {
                    setMessage(R.string.toast_ll_hint_spinner_enabled);
                    return false;

                }
                if (tv_select_card.getText().equals(getString(R.string.tv_no_card))) {
                    setMessage(R.string.mensagem_register_card);
                    return false;

                } else if (select_date.equals(getString(R.string.tv_selec_date))) {
                    setMessage(R.string.toat_select_date);
                    return false;
                }
                //
                return true;
            }
            case "cartao": {
                int qtd;
                String desc_card;
                EditText et_number_card;
                EditText et_desc_card;
                CheckBox cb_credito;
                CheckBox cb_debito;

                et_desc_card = findViewById(R.id.et_desc_card);
                et_number_card = findViewById(R.id.et_number_card);
                cb_credito = findViewById(R.id.cb_credito);
                cb_debito = findViewById(R.id.cb_debito);

                qtd = et_number_card.getText().length();
                desc_card = et_desc_card.getText().toString();

                if (desc_card.trim().isEmpty()) {
                    setMessage(R.string.toast_desc_card);
                    return false;
                }

                if (qtd != 4 && qtd != 0) {
                    setMessage(R.string.toast_number_card);
                    return false;
                }

                if (!cb_debito.isChecked() && !cb_credito.isChecked()) {
                    setMessage(R.string.toast_cb_checked);

                    return false;
                }
                return true;
            }
            case "spinner_action_credito_debito": {
                int tipoCard;
                HMAuxCard item;
                Spinner sp_card;
                Spinner sp_parcelas;
                View view_sp_disabled;
                TextView tv_sp_disabled;

                RadioButton rb_credit;
                RadioButton rb_debit;

                rb_credit = findViewById(R.id.rb_credit);
                rb_debit = findViewById(R.id.rb_debit);
                sp_card = findViewById(R.id.sp_card);
                sp_parcelas = findViewById(R.id.sp_parcelas);
                view_sp_disabled = findViewById(R.id.view_sp_disabled);
                tv_sp_disabled = findViewById(R.id.tv_sp_disabled);

                tv_sp_disabled.setEnabled(true);
                view_sp_disabled.setVisibility(View.INVISIBLE);
                sp_parcelas.setEnabled(true);

                //Recupera o tipo do cartão
                item = (HMAuxCard) sp_card.getSelectedItem();
                tipoCard = convertToInt(item.get(CardDao.TIPO));
                switch (tipoCard) {
                    //Credito
                    case 1: {

                        rb_credit.setEnabled(true);

                        rb_debit.setEnabled(false);


                        rb_credit.setChecked(true);

                        return true;

                    }
                    //Debito
                    case 2: {
                        sp_parcelas.setEnabled(false);
                        view_sp_disabled.setVisibility(View.VISIBLE);

                        rb_credit.setEnabled(false);

                        tv_sp_disabled.setEnabled(false);

                        rb_debit.setEnabled(true);
                        rb_debit.setChecked(true);

                        return true;

                    }
                    //Cred/Deb
                    case 3: {
                        rb_credit.setVisibility(View.VISIBLE);
                        rb_credit.setEnabled(true);

                        rb_debit.setVisibility(View.VISIBLE);
                        rb_debit.setEnabled(true);

                        return true;

                    }
                }

                return true;

            }
        }
        return true;
    }

    public boolean setArrowToSpinnerLowerVersion (){
        ImageView iv_arrow;
        ImageView iv_arrow2;

        iv_arrow = findViewById(R.id.iv_arrow1);
        iv_arrow2 = findViewById(R.id.iv_arrow2);

        if (isAndroidMarshmallowOrSuperiorVersion()){
            iv_arrow.setEnabled(false);
            iv_arrow2.setEnabled(false);
            iv_arrow.setVisibility(View.INVISIBLE);
            iv_arrow2.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }

    }

    public boolean setImageSaveToImageButton(final String caminho, final File imgFile) {
        TextView tv_click_image = findViewById(R.id.tv_click_image);
        ImageButton ib_foto = findViewById(R.id.ib_foto);

        String filePath = imgFile.getAbsolutePath();
        //se o caminho for vazio ou se o caminho existir, mas a foto não.
        if (caminho.equals("") || !imgFile.exists()) {
            tv_click_image.setVisibility(View.INVISIBLE);
            ib_foto.getLayoutParams().height = (int) (setWidthScreen()/1.5);
            ib_foto.getLayoutParams().width = (int) (setWidthScreen()/1.5);
            ib_foto.setBackgroundResource(R.drawable.logo_512);
            ib_foto.setClickable(false);
            ib_foto.setEnabled(false);

            return true;
        } else {
            Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), myBitmap);

            ib_foto.setBackground(bitmapDrawable);

            return false;
        }
    }

    public void setImageSaveToImageButton(final File imgFile) {
        TextView tv_click_image = findViewById(R.id.tv_click_image);
        ImageButton ib_foto = findViewById(R.id.ib_foto);

        String filePath = imgFile.getAbsolutePath();
        //se o caminho for vazio ou se o caminho existir, mas a foto não.
        if (!imgFile.exists()) {
            tv_click_image.setVisibility(View.INVISIBLE);
            ib_foto.getLayoutParams().height = (int) (setWidthScreen()/1.5);
            ib_foto.getLayoutParams().width = (int) (setWidthScreen()/1.5);
            ib_foto.setBackgroundResource(R.drawable.logo_512);
            ib_foto.setClickable(false);

        } else {
            Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), myBitmap);

            ib_foto.setBackground(bitmapDrawable);

        }
    }

    public int setWidthScreen() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return  displayMetrics.widthPixels;
    }


    //formata a data para Ex: 01/01/2019 ao invés de ficar 1/1/2019
    protected String formatDate(String dateF) {
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse((dateF));
        } catch (ParseException ignored) {

        }
        String dateFF = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(date);
        return dateFF;
    }

    public void setMessage(int idMessage) {
        Toast.makeText(this, getString(idMessage), Toast.LENGTH_SHORT).show();
    }

    public void setMessage(Context context, int idMessage) {
        Toast.makeText(context, context.getString(idMessage), Toast.LENGTH_SHORT).show();
    }
}
