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

package com.onimus.munote.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.onimus.munote.R;
import com.onimus.munote.bancos.banco.HMAuxCard;
import com.onimus.munote.bancos.banco.RecordSpinnerCardAdapter;
import com.onimus.munote.bancos.dao.CardDao;
import com.onimus.munote.bancos.dao.NotesDao;
import com.onimus.munote.bancos.model.Notes;
import com.onimus.munote.files.ImageUtilities;
import com.onimus.munote.files.MenuToolbar;
import com.onimus.munote.files.FileUtilities;
import com.onimus.munote.files.MoneyTextWatcher;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.onimus.munote.Constants.*;
import static com.onimus.munote.files.ConvertType.convertToDate;
import static com.onimus.munote.files.MoneyTextWatcher.formatPrice;
import static com.onimus.munote.files.MoneyTextWatcher.getCurrencySymbol;
import static com.onimus.munote.files.SpinnerIndex.getSpinnerIndex;

public class NotesEditActivity extends MenuToolbar {

    private Context context;
    private NotesDao notesDao;
    private Notes nAux;
    private CardDao cardDao;
    //
    private File imgFile;
    //
    private File photoFile1;
    private Calendar calendar;
    //
    private EditText et_title_invoice;
    private EditText et_value;
    private EditText et_desc_invoice;
    private Button btn_select_date;
    private TextView tv_select_card;
    private TextView tv_click_image;
    private TextView tv_sp_disabled;
    private TextView tv_value;
    //
    private RadioButton rb_credit;
    private RadioButton rb_debit;
    //
    private View view_sp_disabled;
    private ImageUtilities imageUtilities;
    //
    private LinearLayout ll_hint_spinner;
    //
    private Spinner sp_card;
    private Spinner sp_parcels;
    //
    private ImageButton ib_photo;
    //
    private long idCard;
    private long idActual;
    private String noPath;
    private String newPath;
    //
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit_screen);

        startVariables();
        startAction(savedInstanceState);
    }

    //se a tela morrer, faz cast e recria a tela;
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(NO_PATH, noPath);

        super.onSaveInstanceState(outState);
    }

    private void startVariables() {
        context = getBaseContext();
        //
        getParameters();
        //
        notesDao = new NotesDao(context);
        cardDao = new CardDao(context);


        toolbar = findViewById(R.id.toolbar);
        et_title_invoice = findViewById(R.id.et_title_invoice);
        et_value = findViewById(R.id.et_value);
        et_desc_invoice = findViewById(R.id.et_desc_invoice);
        btn_select_date = findViewById(R.id.btn_select_date);
        sp_card = findViewById(R.id.sp_card);
        sp_parcels = findViewById(R.id.sp_parcels);
        view_sp_disabled = findViewById(R.id.view_sp_disabled);
        tv_sp_disabled = findViewById(R.id.tv_sp_disabled);
        ib_photo = findViewById(R.id.ib_photo);
        ll_hint_spinner = findViewById(R.id.ll_hint_spinner);
        tv_select_card = findViewById(R.id.tv_select_card);
        tv_click_image = findViewById(R.id.tv_click_image);
        rb_credit = findViewById(R.id.rb_credit);
        rb_debit = findViewById(R.id.rb_debit);
        tv_value = findViewById(R.id.tv_value);
        //
        loadAdmob();
    }

    @SuppressWarnings("deprecation")
    private void startAction(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        //
        et_value.addTextChangedListener(new MoneyTextWatcher(et_value));
        tv_value.setText((getString(R.string.tv_value) + " (" + getCurrencySymbol() + "):"));
        //
        setArrowToSpinnerLowerVersion(2);
        setSpinnerCard();
        setSpinnerParcel();
        //
        if (idActual != -1) {

            nAux = notesDao.getNotesById(idActual);
            idCard = nAux.getIdCard();
            newPath = nAux.getPhotoNotes();
            //
            createDirectoryImage();
            checkCard();

            setField();
        }
        //
        File path = new File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + FOLDER_NAME + FOLDER_NAME_NOTES));
        imgFile = new File(path, newPath);
        //checa o newPath da imagem e retorna boolean
        if (setImageSaveToImageButton(newPath, imgFile)) {
            setAlertDialogUpdateOnClickActivity(R.id.btn_save, NotesActivity.class, context, idActual, NOTES, noPath);
        } else {
            setAlertDialogUpdateOnClickActivity(R.id.btn_save, NotesActivity.class, context, idActual, NOTES, newPath);
        }

        setActionOnClick(R.id.btn_select_date, new OnButtonClickActionCalendar());
        //Se existir imagem já salva é possivel visualiza-la
        setActionOnClick(R.id.ib_photo, new OnButtonClickActionImage(imgFile));
        setActionOnClick(R.id.btn_cancel, new OnButtonClickActionCancel());
        setActionOnClick(R.id.btn_picture, new OnButtonClickActionPicture(savedInstanceState));
        //Quando o Cartão possui credito ou debito, realiza a ação no RadioButton de desabilitar o spinner e o outro radiobutton
        setActionOnClick(R.id.rb_credit, new OnRadioButtonClickActionCredit());
        setActionOnClick(R.id.rb_debit, new OnRadioButtonClickActionDedit());
    }

    private void createDirectoryImage() {
        imageUtilities = new ImageUtilities(context);
        try {
            imageUtilities.createDirectory(FOLDER_NAME_NOTES);
            photoFile1 = imageUtilities.createImageFile();
            noPath = ImageUtilities.returnNoPath();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSpinnerParcel() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.parcels_array, R.layout.cel_spinner_parcels_layout);
        adapter.setDropDownViewResource(R.layout.cel_spinner_dropdown_parcels_layout);
        sp_parcels.setAdapter(adapter);
    }

    private void setSpinnerCard() {
        RecordSpinnerCardAdapter adapter = new RecordSpinnerCardAdapter(context, R.layout.cel_spinner_card_layout, cardDao.getListCard());
        sp_card.setAdapter(adapter);
    }

    private void setField() {
        ArrayList<HMAuxCard> hmAux = cardDao.getListCard();
        //recupera a posição do ID do cartão
        sp_card.setSelection(getSpinnerIndex(sp_card, idCard, hmAux, CardDao.ID_CARD));
        sp_parcels.setSelection(nAux.getParcels() - 1);

        if (nAux.getType() == 1) {
            rb_credit.setChecked(true);
            rb_debit.setEnabled(false);
            tv_sp_disabled.setEnabled(true);
            view_sp_disabled.setVisibility(View.INVISIBLE);

        } else {
            rb_debit.setChecked(true);
            rb_credit.setEnabled(false);
            view_sp_disabled.setVisibility(View.VISIBLE);
            tv_sp_disabled.setEnabled(false);
        }

        et_title_invoice.setText(nAux.getTitleNotes());
        String price = nAux.getPriceNotes();
        //formata o valor como 0,00 ou 0.00
        if (price != null) {
            price = formatPrice(price);
        }
        et_value.setText(price);
        et_desc_invoice.setText(nAux.getDescNotes());
        //formata a data
        String data = formatDate(nAux.getDay() + "/" + nAux.getMonth() + "/" + nAux.getYear());
        btn_select_date.setText(data);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PICTURE && resultCode == RESULT_OK) {

            //tira o background
            ib_photo.setBackgroundResource(0);
            //envia a foto tirada para o ImageButton
            imageUtilities.setPhotoToBitmap(ib_photo);
            //Torna o imagem
            ib_photo.setEnabled(true);
            ib_photo.setClickable(true);

            tv_click_image.setVisibility(View.VISIBLE);
            setAlertDialogUpdateOnClickActivity(R.id.btn_save, NotesActivity.class, context, idActual, NOTES, noPath, imgFile);

            setActionOnClick(R.id.ib_photo, new OnButtonClickActionImage(photoFile1));

        } else {
            setMessage(context, R.string.operation_cancel, false);
        }
    }

    private void getParameters() {
        idActual = getIntent().getLongExtra(DBASE_ID, 0);
    }

    private class OnButtonClickActionImage implements View.OnClickListener {
        private File imageFile;

        private OnButtonClickActionImage(File imageFile) {
            this.imageFile = imageFile;
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            //IMPORTANTE PRA CONSEGUIR LER A IMAGEM:
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setDataAndType(FileUtilities.getUri(getApplicationContext(), imageFile), "image/*");
            startActivity(intent);

        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class OnButtonClickActionPicture implements View.OnClickListener {

        OnButtonClickActionPicture(Bundle savedInstanceState) {
            this.savedInstanceState = savedInstanceState;
        }

        Bundle savedInstanceState;

        @Override
        public void onClick(View v) {
            //Analisa savedInstance para recuperar os valores e inicializar as variaveis
            if (savedInstanceState != null) {
                //
                noPath = savedInstanceState.getString(NO_PATH);

            } else if (ImageUtilities.checkDirectory()) {
                try {
                    imageUtilities.createDirectory(FOLDER_NAME_NOTES);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dispatchTakePictureIntent();
            //
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile;

            photoFile = photoFile1;
            // Continue only if the File was successfully created
            if (photoFile != null) {

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtilities.getUri(getApplicationContext(), photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
            }
        }
    }

    private class OnSpinnerClickAction implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sp_card.setSelection(position);
            validation(SPINNER_ACTION_CRED_DEB);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            sp_card.setEnabled(false);
            ll_hint_spinner.setVisibility(View.VISIBLE);

        }
    }
    @SuppressWarnings("all")
    private class OnButtonClickActionCalendar implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            //
            DatePickerDialog datePickerDialog = new DatePickerDialog(NotesEditActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            calendar.set(year, month, day);
                            String format = "dd/MM/yyyy";
                            Date date = convertToDate(format, calendar.getTime());
                            String dayS = new SimpleDateFormat("dd", Locale.ENGLISH).format(date);
                            String monthS = new SimpleDateFormat("MM", Locale.ENGLISH).format(date);
                            String yearS = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(date);

                            btn_select_date.setText((dayS + "/" + monthS + "/" + yearS));
                        }
                    }, year, month, day);
            datePickerDialog.show();
            datePickerDialog.getDatePicker();
        }
    }

    private class OnButtonClickActionCancel implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ImageUtilities.deleteImage();
            callListView(NotesViewActivity.class, idActual);

        }
    }

    private class OnLinearLayoutClickAction implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //perfromClick, ao clicar no linear layout do spinner chama o spinner
            sp_card.performClick();
            //textview fica invisivel
            ll_hint_spinner.setVisibility(View.INVISIBLE);
            //desabilita linearlayout;
            ll_hint_spinner.setEnabled(false);
        }
    }

    private void checkCard() {
        //desabilita linearlayout;
        ll_hint_spinner.setEnabled(false);
        //
        if (sp_card.getCount() == 0) {
            tv_select_card.setEnabled(true);
            tv_select_card.setText(getString(R.string.tv_no_card));
            ImageUtilities.deleteImage();
            setAlertDialogOnClickActivity(CardAddActivity.class, NotesViewActivity.class, idActual, NOTES_TO_CARD);
        } else if (idCard == -1L) {
            ll_hint_spinner.setEnabled(true);
            ll_hint_spinner.setVisibility(View.VISIBLE);
            sp_card.setEnabled(true);
            tv_select_card.setText(getString(R.string.tv_choose_card));
            //Hint básico para o spinner
            tv_select_card.setEnabled(false);
            setActionOnClick(R.id.ll_hint_spinner, new OnLinearLayoutClickAction());
            setActionOnClick(R.id.sp_card, new OnSpinnerClickAction());
        } else {
            sp_card.setEnabled(true);
            tv_select_card.setEnabled(false);
            setActionOnClick(R.id.sp_card, new OnSpinnerClickAction());
        }
    }

    private class OnRadioButtonClickActionCredit implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tv_sp_disabled.setEnabled(true);
            sp_parcels.setEnabled(true);
            view_sp_disabled.setVisibility(View.INVISIBLE);

        }
    }

    private class OnRadioButtonClickActionDedit implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            view_sp_disabled.setVisibility(View.VISIBLE);
            tv_sp_disabled.setEnabled(false);
            sp_parcels.setSelection(0);
            sp_parcels.setEnabled(false);

        }
    }

    public void onBackPressed() {
        ImageUtilities.deleteImage();
        callListView(NotesViewActivity.class, idActual);

    }
}

