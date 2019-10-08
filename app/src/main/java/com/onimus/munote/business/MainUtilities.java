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

package com.onimus.munote.business;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.onimus.munote.BuildConfig;
import com.onimus.munote.R;
import com.onimus.munote.repository.dao.CardDao;
import com.onimus.munote.repository.dao.NotesDao;
import com.onimus.munote.repository.database.HMAuxCard;
import com.onimus.munote.repository.model.Card;
import com.onimus.munote.repository.model.Notes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.onimus.munote.Constants.CARD;
import static com.onimus.munote.Constants.CARD_TO_NOTES;
import static com.onimus.munote.Constants.DBASE_ID;
import static com.onimus.munote.Constants.FOLDER_NAME;
import static com.onimus.munote.Constants.FOLDER_NAME_NOTES;
import static com.onimus.munote.Constants.NOTES;
import static com.onimus.munote.Constants.NOTES_TO_CARD;
import static com.onimus.munote.Constants.SPINNER_ACTION_CRED_DEB;
import static com.onimus.munote.business.ConvertType.convertToDate;
import static com.onimus.munote.business.ConvertType.convertToInt;
import static com.onimus.munote.business.ConvertType.convertToLong;
import static com.onimus.munote.business.FileUtilities.isAndroidMarshmallowOrSuperiorVersion;
import static com.onimus.munote.business.MoneyTextWatcher.formatPriceSave;

@SuppressLint("Registered")
public class MainUtilities extends AppCompatActivity {

    protected static void setActionOnClick(final View view, View.OnClickListener action) {
        if (view != null) {
            view.setOnClickListener(action);
        }
    }

    protected void setActionOnClick(final int btn, View.OnClickListener action) {
        View v = findViewById(btn);
        setActionOnClick(v, action);
    }

    protected void setActionOnClick(final int btn, AdapterView.OnItemSelectedListener action) {
        AdapterView v = findViewById(btn);
        setActionOnClick(v, action);
    }

    private static void setActionOnClick(final AdapterView view, AdapterView.OnItemSelectedListener action) {
        if (view != null) {
            view.setOnItemSelectedListener(action);
        }
    }

    protected void setActionOnClickActivity(final View view, final Class<?> _class) {
        setActionOnClick(view, v -> callActivity(getApplicationContext(), _class));
    }

    protected void setActionOnClickActivity(final int btn, final Class<?> _class, final long id) {
        setActionOnClick(btn, v -> callListView(_class, id));
    }

    //Esse retorno não necessita de botão, utilizado pra outros métodos e também pra quando o usuário
    //aperta o botão de retorno do celular, ou em algum item do menutoolbar.
    protected void setAlertDialogToReturnOnClickActivity(final Class<?> _class, final String putType) {
        //Pega o String contido nos id das R.string.
        String idTitle = getString(R.string.alert_title_cancel);
        String idMessage = getString(R.string.alert_message_return);
        String idOK = getString(R.string.alert_ok);
        String idCancel = getString(R.string.alert_cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainUtilities.this);

        // 2. Una vários métodos de setter para definir as características do diálogo
        builder.setMessage(idMessage).setTitle(idTitle);

        //Adiciona os botões
        builder.setPositiveButton(idOK, (dialog, is) -> {

            switch (putType) {
                //vem do CardAddActivity e retorna pra CardActivity;
                case CARD: {

                    callActivity(getApplicationContext(), _class);
                    //
                    break;
                }
                //vem de CardAddActivity e ao clicar no item notas do MenuToolbar vai pra NotesAddActivity;
                case CARD_TO_NOTES: {
                    callListView(_class, -1L);
                    break;
                }
                //vem do NotesAddActivity e retorna pra NotesActivity;
                case NOTES: {
                    ImageUtilities.deleteImage();
                    callActivity(getApplicationContext(), _class);
                    break;
                }
                //vem do NotesAddActivity e ao clicar no item cartao do MenuToolbar vai pra CardAddActivity;
                case NOTES_TO_CARD: {
                    ImageUtilities.deleteImage();
                    callListView(_class, -1L);
                    break;
                }

            }
        });
        builder.setNegativeButton(idCancel, (dialog, is) -> {

        });

        builder.setIcon(R.mipmap.logo_round);
        // 3. Obtenha o AlertDialog de create () e mostre
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Inicia o alerta ao clicar em algum botão, e se clicar em ok, cancela operação retornando pra activity desejada.
    protected void setAlertDialogToReturnOnClickActivity(
            final int btn, final Class<?> _class, final String putType) {
        setActionOnClick(btn, v -> setAlertDialogToReturnOnClickActivity(_class, putType));
    }

    //Alerta para quando tem ação no "SIM" e no "NÃO";
    protected void setAlertDialogOnClickActivity(
            final Class<?> _classF, final Class<?> _classR,
            final long idActual, final String putType) {
        if (NOTES_TO_CARD.equals(putType)) {//Pega o String contido nos id das R.string.
            String idTitle = getString(R.string.tv_no_card);
            String idMessage = getString(R.string.alert_register_card);
            String idOK = getString(R.string.alert_ok);
            String idCancel = getString(R.string.alert_cancel);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainUtilities.this);

            // 2. Una vários métodos de setter para definir as características do diálogo
            builder.setMessage(idMessage).setTitle(idTitle);

            //Adiciona os botões
            builder.setPositiveButton(idOK, (dialog, is) -> callListView(_classF, -1L));
            builder.setNegativeButton(idCancel, (dialog, is) -> callListView(_classR, idActual));

            builder.setIcon(R.mipmap.logo_round);
            // 3. Obtenha o AlertDialog de create () e mostre
            AlertDialog dialog = builder.create();
            //////////////////////////////////////////////////////////////////
            //FORÇA o usuário a escolher entre sim ou não.
            //////////////////////////////////////////////////////////////////
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            dialog.show();
        }
    }

    //Alerta para quando tem ação de delete.
    protected void setAlertDialogDeleteOnClickActivity(
            final int btn, final Class<?> _class, final Context context,
            final long idActual, final String putType) {
        setActionOnClick(btn, v -> {

            //Pega o String contido nos id das R.string.
            String idTitle = getString(R.string.alert_title_delete);
            String idMessage = getString(R.string.alert_message_delete);
            String idOK = getString(R.string.alert_ok);
            String idCancel = getString(R.string.alert_cancel);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainUtilities.this);

            // 2. Una vários métodos de setter para definir as características do diálogo
            builder.setMessage(idMessage).setTitle(idTitle);

            //Adiciona os botões
            builder.setPositiveButton(idOK, (dialog, is) -> {
                switch (putType) {
                    case CARD: {
                        CardDao cardDao;
                        cardDao = new CardDao(context);
                        cardDao.deleteCard(idActual);

                        NotesDao notesDao;
                        notesDao = new NotesDao(context);
                        notesDao.updateDeletedCard(idActual);
                        break;
                    }
                    case NOTES: {
                        NotesDao notesDao;
                        notesDao = new NotesDao(context);
                        Notes notes = notesDao.getNotesById(idActual);
                        String newPath = notes.getPhotoNotes();
                        ManageDirectory md = new ManageDirectory(context);
                        String path = FOLDER_NAME + FOLDER_NAME_NOTES;
                        File dir = md.createPublicDirectoryFileForVariousApi(path, Environment.DIRECTORY_PICTURES);
                        File imgFile = md.createFile(dir, newPath);
                        ImageUtilities.deleteImage(imgFile);
                        notesDao.deleteNotes(idActual);
                        break;

                    }
                }
                callActivity(getApplicationContext(), _class);

            });
            builder.setNegativeButton(idCancel, (dialog, is) -> {

            });

            builder.setIcon(R.mipmap.logo_round);
            // 3. Obtenha o AlertDialog de create () e mostre
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    protected void setAlertDialogUpdateOnClickActivity(
            final int btn, final Class<?> _class, final Context context,
            final long idActual, final String putType) {
        setActionOnClick(btn, v -> {

            if (CARD.equals(putType)) {
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
                    builder.setPositiveButton(idOK, (dialog, is) -> {

                        saveCard(idActual, context);
                        callListView(_class, idActual);

                    });
                    builder.setNegativeButton(idCancel, (dialog, is) -> {
                    });

                    builder.setIcon(R.mipmap.logo_round);
                    // 3. Obtenha o AlertDialog de create () e mostre
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                ////////
            }

        });
    }

    protected void setAlertDialogUpdateOnClickActivity(
            final int btn, final Class<?> _class, final Context context, final long idActual,
            final String putType, final String path) {
        setActionOnClick(btn, v -> {

            if (NOTES.equals(putType)) {
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
                    builder.setPositiveButton(idOK, (dialog, is) -> {
                        //Verifica se o imageview foi alterado, se for main então não, se for new, ele foi alterado;
                        //Se for main a imagem gerada precisa ser deletada e o way da imagem no banco de dados
                        // precisa ser deletado, se for new será salva;

                        //  if (tagName.equals("main")) {
                        //     ImageUtilities.deleteImage();
                        //   saveNotes(idActual, context);
                        //    } else {

                        saveNotes(idActual, context, path);
                        //    }
                        callActivity(context, _class);

                    });
                    builder.setNegativeButton(idCancel, (dialog, is) -> {

                    });
                    builder.setIcon(R.mipmap.logo_round);
                    // 3. Obtenha o AlertDialog de create () e mostre
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }


        });
    }

    protected void setAlertDialogUpdateOnClickActivity(
            final int btn, final Class<?> _class, final Context context,
            final long idActual, final String putType, final String noPath, final File imgFile) {
        setActionOnClick(btn, v -> {

            if (NOTES.equals(putType)) {
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
                    builder.setPositiveButton(idOK, (dialog, is) -> {

                        ImageUtilities.deleteImage(imgFile);

                        saveNotes(idActual, context, noPath);

                        callActivity(context, _class);

                    });
                    builder.setNegativeButton(idCancel, (dialog, is) -> {

                    });
                    builder.setIcon(R.mipmap.logo_round);
                    // 3. Obtenha o AlertDialog de create () e mostre
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }


    protected void callListView(Class<?> _class, long id) {
        Intent mIntent = new Intent(getApplicationContext(), _class);
        mIntent.putExtra(DBASE_ID, id);
        //
        startActivity(mIntent);
        //
        finish();
    }

    //Action pra click do botão e inicar alguma ação/intent ao invés de startar uma Activity
    protected void openESFileExplorer() {
        ManageDirectory md = new ManageDirectory(this);
        String path = md.createPublicDirectoryFileForVariousApi(FOLDER_NAME, Environment.DIRECTORY_PICTURES).getAbsolutePath();
        Uri selectedUri = Uri.parse(path);

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setDataAndType(selectedUri, "resource/folder");
        //Verifica se tem o ES File Explorer Instalado
        if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
            startActivity(Intent.createChooser(intent, "Open folder"));

        } else {
            setMessage(getApplicationContext(), R.string.install_ES_File, false);
        }
    }


    protected void clearField(EditText et, boolean b) {
        //limpa edittext e se for definido como true então o Focus volta pro editext;
        et.getText().clear();

        if (b) {
            et.requestFocus();
        }
    }

    protected void clearField(CheckBox cb) {
        //limpa checkbox
        cb.setChecked(false);
    }

    protected void callActivity(Context context, final Class<?> _class) {
        Intent mIntent = new Intent(context, _class);
        startActivity(mIntent);
        //
        finish();
    }

    protected void saveCard(long idActual, Context context) {

        EditText et_desc_card;
        CheckBox cb_credit;
        CheckBox cb_debit;
        CardDao cardDao = new CardDao(context);
        Card cAux = new Card();
        //
        et_desc_card = findViewById(R.id.et_desc_card);
        cb_credit = findViewById(R.id.cb_credit);
        cb_debit = findViewById(R.id.cb_debit);
        //
        cAux.setDescCard(et_desc_card.getText().toString().trim());

        if (cb_credit.isChecked() && !cb_debit.isChecked()) {
            cAux.setType(1);
        }
        if (!cb_credit.isChecked() && cb_debit.isChecked()) {
            cAux.setType(2);
        }
        if (cb_credit.isChecked() && cb_debit.isChecked()) {
            cAux.setType(3);
        }
        //
        if (idActual != -1) {
            cAux.setIdCard(idActual);
            //
            cardDao.updateCard(cAux);
        } else {
            idActual = cardDao.nextID();
            cAux.setIdCard(idActual);
            //
            cardDao.insertCard(cAux);
        }
    }


    @SuppressWarnings("all")
    protected void saveNotes(long idActual, Context context, String pathPhoto) {

        EditText et_title_invoice;
        EditText et_value;
        EditText et_desc_invoice;
        Button btn_select_date;
        Spinner sp_card;
        Spinner sp_parcels;
        RadioButton rb_debit;

        int year;
        int month;
        int day;
        //
        String idCard;
        //
        NotesDao notesDao = new NotesDao(context);
        //
        Notes cAux = new Notes();
        //
        et_desc_invoice = findViewById(R.id.et_desc_invoice);
        et_title_invoice = findViewById(R.id.et_title_invoice);
        et_value = findViewById(R.id.et_value);
        btn_select_date = findViewById(R.id.btn_select_date);
        sp_card = findViewById(R.id.sp_card);
        sp_parcels = findViewById(R.id.sp_parcels);
        rb_debit = findViewById(R.id.rb_debit);

        //Transforma a data do textview calendário em Date, formata e salva separado;
        String date = btn_select_date.getText().toString();
        String format = "dd/MM/yyyy";

        Date dateD = convertToDate(format, date);
        year = convertToInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(dateD));
        month = convertToInt(new SimpleDateFormat("MM", Locale.getDefault()).format(dateD));
        day = convertToInt(new SimpleDateFormat("dd", Locale.getDefault()).format(dateD));

        //
        //Recupera o ID do cartão que está selecionado no Spinner
        HMAuxCard item = (HMAuxCard) sp_card.getSelectedItem();
        idCard = item.get(CardDao.ID_CARD);
        //
        int positionSpParcels = sp_parcels.getSelectedItemPosition() + 1;
        //
        cAux.setDescNotes(et_desc_invoice.getText().toString());
        cAux.setTitleNotes(et_title_invoice.getText().toString());
        cAux.setPriceNotes(formatPriceSave(et_value.getText().toString()));
        cAux.setIdCard(convertToLong(idCard));
        cAux.setPhotoNotes(pathPhoto);
        cAux.setYear(year);
        cAux.setMonth(month);
        cAux.setDay(day);
        //
        if (rb_debit.isChecked()) {
            cAux.setType(2);
            cAux.setParcels(1);
        } else {
            cAux.setType(1);
            cAux.setParcels(positionSpParcels);
        }

        if (idActual != -1) {
            cAux.setIdNotes(idActual);
            //
            notesDao.updateNotes(cAux);

        } else {
            idActual = notesDao.nextID();
            cAux.setIdNotes(idActual);
            //
            notesDao.insertNotes(cAux);
        }
    }

    protected boolean validation(String putType) {
        //Faz validação, se tiver tudo certo, retorna verdadeiro.
        switch (putType) {
            case NOTES: {
                LinearLayout ll_hint_spinner;
                EditText et_title_invoice;
                EditText et_value;
                Button btn_select_date;
                TextView tv_select_card;
                //
                String title_invoice;
                String value;
                String select_date;
                //
                ll_hint_spinner = findViewById(R.id.ll_hint_spinner);
                et_title_invoice = findViewById(R.id.et_title_invoice);
                et_value = findViewById(R.id.et_value);
                btn_select_date = findViewById(R.id.btn_select_date);
                tv_select_card = findViewById(R.id.tv_select_card);
                //
                value = et_value.getText().toString();
                title_invoice = et_title_invoice.getText().toString();
                if (!value.isEmpty()) {
                    value = formatPriceSave(value);
                }

                select_date = btn_select_date.getText().toString();
                //
                if (title_invoice.trim().isEmpty()) {
                    setMessage(getApplicationContext(), R.string.toast_title_invoice, false);
                    return false;
                }
                //
                if (value.trim().isEmpty() || value.equals("0.00")) {
                    setMessage(getApplicationContext(), R.string.toast_value_required, false);
                    return false;
                }
                //
                if (tv_select_card.getText().equals(getString(R.string.tv_choose_card)) && ll_hint_spinner.isEnabled()) {
                    setMessage(getApplicationContext(), R.string.toast_ll_hint_spinner_enabled, false);
                    return false;

                }
                if (tv_select_card.getText().equals(getString(R.string.tv_no_card))) {
                    setMessage(getApplicationContext(), R.string.message_register_card, false);
                    return false;

                } else if (select_date.equals(getString(R.string.tv_select_date))) {
                    setMessage(getApplicationContext(), R.string.toat_select_date, false);
                    return false;
                }
                //
                return true;
            }
            case CARD: {
                String desc_card;
                EditText et_desc_card;
                CheckBox cb_credit;
                CheckBox cb_debit;

                et_desc_card = findViewById(R.id.et_desc_card);
                cb_credit = findViewById(R.id.cb_credit);
                cb_debit = findViewById(R.id.cb_debit);

                desc_card = et_desc_card.getText().toString();

                if (desc_card.trim().isEmpty()) {
                    setMessage(getApplicationContext(), R.string.toast_desc_card, false);
                    return false;
                }

                if (!cb_debit.isChecked() && !cb_credit.isChecked()) {
                    setMessage(getApplicationContext(), R.string.toast_cb_checked, false);

                    return false;
                }
                return true;
            }
            case SPINNER_ACTION_CRED_DEB: {
                int typeCard;
                HMAuxCard item;
                Spinner sp_card;
                Spinner sp_parcels;
                View view_sp_disabled;
                TextView tv_sp_disabled;

                RadioButton rb_credit;
                RadioButton rb_debit;

                rb_credit = findViewById(R.id.rb_credit);
                rb_debit = findViewById(R.id.rb_debit);
                sp_card = findViewById(R.id.sp_card);
                sp_parcels = findViewById(R.id.sp_parcels);
                view_sp_disabled = findViewById(R.id.view_sp_disabled);
                tv_sp_disabled = findViewById(R.id.tv_sp_disabled);

                tv_sp_disabled.setEnabled(true);
                view_sp_disabled.setVisibility(View.INVISIBLE);
                sp_parcels.setEnabled(true);

                //Recupera o tipo do cartão
                item = (HMAuxCard) sp_card.getSelectedItem();
                typeCard = convertToInt(item.get(CardDao.TYPE));
                switch (typeCard) {
                    //Credito
                    case 1: {
                        rb_credit.setEnabled(true);
                        rb_debit.setEnabled(false);
                        rb_credit.setChecked(true);

                        return true;

                    }
                    //Debito
                    case 2: {
                        sp_parcels.setEnabled(false);
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

                        if (rb_debit.isChecked()) {
                            sp_parcels.setEnabled(false);
                            view_sp_disabled.setVisibility(View.VISIBLE);
                            tv_sp_disabled.setEnabled(false);
                        }
                        return true;

                    }
                }

                return true;

            }
        }
        return true;
    }

    protected void setArrowToSpinnerLowerVersion(int qtd) {
        if (isAndroidMarshmallowOrSuperiorVersion()) {
            ImageView[] iv_arrow;
            iv_arrow = new ImageView[qtd];

            for (int i = 0; i < iv_arrow.length; i++) {
                String imageId = "iv_arrow" + (i + 1);
                int resID = getResources().getIdentifier(imageId, "id", getPackageName());
                iv_arrow[i] = findViewById(resID);
                iv_arrow[i].setEnabled(false);
                iv_arrow[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    protected boolean setImageSaveToImageButton(final String path, final File imgFile) {
        TextView tv_click_image = findViewById(R.id.tv_click_image);
        ImageButton ib_photo = findViewById(R.id.ib_photo);

        String filePath = imgFile.getAbsolutePath();
        //se o path for vazio ou se o path existir, mas a foto não.
        if (path.equals("") || !imgFile.exists()) {
            tv_click_image.setVisibility(View.INVISIBLE);
            ib_photo.getLayoutParams().height = (int) (getWidthScreen() / 1.5);
            ib_photo.getLayoutParams().width = (int) (getWidthScreen() / 1.5);
            ib_photo.setBackgroundResource(R.drawable.logo_512);
            ib_photo.setClickable(false);
            ib_photo.setEnabled(false);

            return true;
        } else {
            Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), myBitmap);

            ib_photo.setBackground(bitmapDrawable);

            return false;
        }
    }

    protected void setImageSaveToImageButton(final File imgFile) {
        TextView tv_click_image = findViewById(R.id.tv_click_image);
        ImageButton ib_photo = findViewById(R.id.ib_photo);

        String filePath = imgFile.getAbsolutePath();
        //se o way for vazio ou se o way existir, mas a foto não.
        if (!imgFile.exists()) {
            tv_click_image.setVisibility(View.INVISIBLE);
            ib_photo.getLayoutParams().height = (int) (getWidthScreen() / 1.5);
            ib_photo.getLayoutParams().width = (int) (getWidthScreen() / 1.5);
            ib_photo.setBackgroundResource(R.drawable.logo_512);
            ib_photo.setClickable(false);

        } else {
            Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), myBitmap);

            ib_photo.setBackground(bitmapDrawable);

        }
    }

    private int getWidthScreen() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    protected int getHeightScreen() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    //formata a data para Ex: 01/01/2019 ao invés de ficar 1/1/2019
    @SuppressWarnings("all")
    protected String formatDate(String dateF) {
        String format = "dd/MM/yyyy";
        Date date = convertToDate(format, dateF);
        return new SimpleDateFormat(format, Locale.ENGLISH).format(date);
    }

    public void setMessage(Context context, int idMessage, boolean isLong) {

        if (isLong) {
            Toast.makeText(context, context.getString(idMessage), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, context.getString(idMessage), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressWarnings("deprecation")
    protected static Spanned fromHtml(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    protected void loadAdmob() {
        AdView mAdView = findViewById(R.id.adView);

        mAdView.setAdListener(new AdManager());

        AdRequest.Builder builder = new AdRequest.Builder();
        if (BuildConfig.DEBUG) {
            builder.addTestDevice("322CF4FABD4B5A1207AAA9224C571B6E");
            builder.addTestDevice("43539323E5E3D50693C87D974197959B");
        }

        AdRequest adRequest = builder.build();
        mAdView.loadAd(adRequest);
    }
}
