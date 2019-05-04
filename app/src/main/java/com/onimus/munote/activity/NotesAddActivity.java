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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.onimus.munote.R;
import com.onimus.munote.bancos.banco.RecordSpinnerCardAdapter;
import com.onimus.munote.bancos.dao.CardDao;
import com.onimus.munote.files.ImageUtilities;
import com.onimus.munote.files.MenuToolbar;
import com.onimus.munote.files.FileUtilities;
import com.onimus.munote.files.MoneyTextWatcher;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.onimus.munote.Constants.*;
import static com.onimus.munote.bancos.DBaseDirectory.createDirectoryDbase;
import static com.onimus.munote.files.ConvertType.convertToDate;
import static com.onimus.munote.files.MoneyTextWatcher.getCurrencySymbol;

public class NotesAddActivity extends MenuToolbar {

    private Context context;
    //
    private Button btn_select_date;
    private TextView tv_select_card;
    private TextView tv_click_image;
    private TextView tv_sp_disabled;
    private LinearLayout ll_hint_spinner;
    private ImageButton ib_photo;
    private EditText et_value;
    private TextView tv_value;
    //
    private View view_sp_disabled;
    //
    private Spinner sp_card;
    private Spinner sp_parcels;
    private Calendar calendar;
    //
    private Toolbar toolbar;
    //
    private String noPath;
    private File photoFile1;
    private File imgFile;
    private ImageUtilities imageUtilities;
    //
    private long idActual;
    //
    private CardDao cardDao;

    //
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_add_screen);

        startVariables();
        startAction(savedInstanceState);
    }

    @Override
    //se a tela morrer, faz cast e recria a tela;
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(NO_PATH, noPath);

        super.onSaveInstanceState(outState);
    }

    private void startVariables() {
        //bundle variavel de cache
        context = getBaseContext();
        //
        cardDao = new CardDao(context);
        //
        getParameters();
        //
        btn_select_date = findViewById(R.id.btn_select_date);
        tv_select_card = findViewById(R.id.tv_select_card);
        ll_hint_spinner = findViewById(R.id.ll_hint_spinner);
        sp_card = findViewById(R.id.sp_card);
        sp_parcels = findViewById(R.id.sp_parcels);
        view_sp_disabled = findViewById(R.id.view_sp_disabled);
        tv_sp_disabled = findViewById(R.id.tv_sp_disabled);
        ib_photo = findViewById(R.id.ib_photo);
        et_value = findViewById(R.id.et_value);
        tv_click_image = findViewById(R.id.tv_click_image);
        tv_value = findViewById(R.id.tv_value);
        //
        toolbar = findViewById(R.id.toolbar);
        //
        loadAdmob();
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    private void startAction(final Bundle savedInstanceState) {
        et_value.setHint((getCurrencySymbol() + " " + getString(R.string.et_value)));
        et_value.addTextChangedListener(new MoneyTextWatcher(et_value));
        //
        tv_value.setText((getString(R.string.tv_value) + " (" + getCurrencySymbol() + "):"));
        //
        createDirectoryDbase(context);
        createDirectoryImage();
        //
        setSupportActionBar(toolbar);
        //
        setArrowToSpinnerLowerVersion();
        setSpinnerCard();
        setSpinnerParcel();
        //Verifica se tem cartão cadastrado, se não tiver desabilita o Spinner.
        //Recupera o tipo de cartão
        checkCard();
        //
        File path = new File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + FOLDER_NAME + FOLDER_NAME_NOTES));
        imgFile = new File(path, noPath);
        setImageSaveToImageButton(imgFile);
        //
        setActionOnClick(R.id.btn_select_date, new OnButtonClickActionCalendar());
        setActionOnClick(R.id.btn_picture, new OnButtonClickActionPicture(savedInstanceState));
        //
        setActionOnClick(R.id.btn_save, new OnButtonClickActionSave());
        setAlertDialogToReturnOnClickActivity(R.id.btn_cancel, NotesActivity.class, NOTES);
        //Quando o Cartão possui credito ou debito, realiza a ação no RadioButton de desabilitar o spinner e o outro radiobutton
        setActionOnClick(R.id.rb_credit, new OnRadioButtonClickActionCredit());
        setActionOnClick(R.id.rb_debit, new OnRadioButtonClickActionDebit());
        //
    }

    private void setSpinnerParcel() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.parcels_array, R.layout.cel_spinner_parcels_layout);
        adapter.setDropDownViewResource(R.layout.cel_spinner_dropdown_parcels_layout);
        sp_parcels.setAdapter(adapter);
    }

    private void setSpinnerCard() {
        //
        RecordSpinnerCardAdapter adapter = new RecordSpinnerCardAdapter(context, R.layout.cel_spinner_card_layout, cardDao.getListCard());
        sp_card.setAdapter(adapter);
    }

    private void createDirectoryImage() {
        imageUtilities = new ImageUtilities();
        try {
            imageUtilities.createDirectory(FOLDER_NAME_NOTES);
            photoFile1 = imageUtilities.createImageFile();
            noPath = ImageUtilities.returnNoPath();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PICTURE && resultCode == RESULT_OK) {
            ib_photo.setBackgroundResource(0);
            imageUtilities.setPhotoToBitmap(context, ib_photo);
            ib_photo.setClickable(true);
            tv_click_image.setVisibility(View.VISIBLE);
            setActionOnClick(R.id.ib_photo, new OnButtonClickActionImage(imgFile));

        } else {
            setMessage(context, R.string.operation_cancel, false);
        }
    }

    /////////////////////
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void getParameters() {
        idActual = getIntent().getLongExtra(DBASE_ID, 0);
    }

    ///////////////////
    private class OnButtonClickActionCalendar implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            //
            DatePickerDialog datePickerDialog = new DatePickerDialog(NotesAddActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            calendar.set(year, month, day);
                            String format = "dd/MM/yyyy";
                            Date date = convertToDate(format, calendar.getTime());
                            String dayS = new SimpleDateFormat("dd", Locale.ENGLISH).format(date);
                            String monthS = new SimpleDateFormat("MM", Locale.ENGLISH).format(date);
                            String yearS = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(date);
                            btn_select_date.setText(String.valueOf(dayS + "/" + monthS + "/" + yearS));
                        }
                    }, year, month, day);
            datePickerDialog.show();
            datePickerDialog.getDatePicker();
        }
    }

    ///////////////
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

    private class OnSpinnerClickAction implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sp_card.setSelection(position);

            //valida o radiobutton e o spinner parcelas
            validation(SPINNER_ACTION_CRED_DEB);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            sp_card.setEnabled(false);
            ll_hint_spinner.setVisibility(View.VISIBLE);

        }
    }

    private class OnButtonClickActionSave implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String tagName = String.valueOf(ib_photo.getTag());
            if (validation(NOTES)) {
                //Verifica se o imageview foi alterado, se for main então não, se for new, ele foi alterado;
                //Se for main a imagem gerada precisa ser deletada e o caminho da imagem no banco de dados
                // precisa ser deletado, se for new será salva;
                if (tagName.equals("main")) {
                    ImageUtilities.deleteImage();
                    noPath = "";
                }

                saveNotes(idActual, context, noPath);
                callActivity(context, NotesActivity.class);

            }
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

    private class OnRadioButtonClickActionDebit implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            view_sp_disabled.setVisibility(View.VISIBLE);
            tv_sp_disabled.setEnabled(false);
            sp_parcels.setSelection(0);
            sp_parcels.setEnabled(false);

        }
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

    private void checkCard() {
        ll_hint_spinner.setVisibility(View.VISIBLE);
        if (sp_card.getCount() == 0) {
            tv_select_card.setEnabled(true);
            tv_select_card.setText(getString(R.string.tv_no_card));
            ImageUtilities.deleteImage();
            setAlertDialogOnClickActivity(CardAddActivity.class, NotesActivity.class, idActual, NOTES_TO_CARD);

        } else {
            sp_card.setEnabled(true);
            tv_select_card.setText(getString(R.string.tv_choose_card));
            //Hint básico para o spinner
            tv_select_card.setEnabled(false);
            setActionOnClick(R.id.ll_hint_spinner, new OnLinearLayoutClickAction());
            setActionOnClick(R.id.sp_card, new OnSpinnerClickAction());
        }
    }

////////////////////

    public void onBackPressed() {
        setAlertDialogToReturnOnClickActivity(NotesActivity.class, NOTES);
    }


}




