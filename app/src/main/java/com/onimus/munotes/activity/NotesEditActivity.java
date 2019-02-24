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

package com.onimus.munotes.activity;

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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.onimus.munotes.Constants;
import com.onimus.munotes.R;
import com.onimus.munotes.bancos.banco.HMAuxCard;
import com.onimus.munotes.bancos.banco.RecordSpinnerCardAdapter;
import com.onimus.munotes.bancos.dao.CardDao;
import com.onimus.munotes.bancos.dao.NotesDao;
import com.onimus.munotes.bancos.model.Notes;
import com.onimus.munotes.files.ImageUtilities;
import com.onimus.munotes.files.MenuToolbar;
import com.onimus.munotes.files.FileUtilities;
import com.onimus.munotes.files.MoneyTextWatcher;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.onimus.munotes.files.MoneyTextWatcher.formatPrice;
import static com.onimus.munotes.files.MoneyTextWatcher.getCurrencySymbol;

public class NotesEditActivity extends MenuToolbar {

    private Context context;
    private NotesDao notesDao;
    private Notes nAux;
    private CardDao cardDao;
    private RecordSpinnerCardAdapter adapter;
    private ArrayList<HMAuxCard> hmAux;
    //
    private File imgFile;
    //
    private File photoFile1;
    //
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    //
    private EditText et_title_invoice;
    private EditText et_value;
    private EditText et_desc_invoice;
    private Button btn_selec_date;
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
    private Spinner sp_parcelas;
    //
    private ImageButton ib_foto;
    //
    private int idCartao;
    private long idAtual;
    private String caminhoSemPath;
    private String caminho;
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
        outState.putString("caminhoSemPath", caminhoSemPath);

        super.onSaveInstanceState(outState);
    }

    private void startVariables() {
        context = getBaseContext();
        getParameters();
        //
        notesDao = new NotesDao(context);
        cardDao = new CardDao(context);

        toolbar = findViewById(R.id.toolbar);
        et_title_invoice = findViewById(R.id.et_title_invoice);
        et_value = findViewById(R.id.et_value);
        et_desc_invoice = findViewById(R.id.et_desc_invoice);
        btn_selec_date = findViewById(R.id.btn_selec_date);
        sp_card = findViewById(R.id.sp_card);
        sp_parcelas = findViewById(R.id.sp_parcelas);
        view_sp_disabled = findViewById(R.id.view_sp_disabled);
        tv_sp_disabled = findViewById(R.id.tv_sp_disabled);
        ib_foto = findViewById(R.id.ib_foto);
        ll_hint_spinner = findViewById(R.id.ll_hint_spinner);
        tv_select_card = findViewById(R.id.tv_select_card);
        tv_click_image = findViewById(R.id.tv_click_image);
        rb_credit = findViewById(R.id.rb_credit);
        rb_debit = findViewById(R.id.rb_debit);
        tv_value = findViewById(R.id.tv_value);
        //
        et_value.addTextChangedListener(new MoneyTextWatcher(et_value));
        //
        tv_value.setText((getString(R.string.tv_value) + " ("+ getCurrencySymbol() + "):"));
        //
    }

    private void startAction(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        //
        setArrowToSpinnerLowerVersion();
        setSpinnerCard();
        setSpinnerParcel();

        if (idAtual != -1) {

            nAux = notesDao.getNotesById(idAtual);
            idCartao = (int) nAux.getIdcartao();
            caminho = nAux.getFotonotas();
            //
            createDirectoryImage();
            checkCard();

            setField();

        }
        //

        File path = new File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + Constants.FOLDER_NAME + Constants.FOLDER_NAME_NOTES));
        imgFile = new File(path, caminho);
        //checa o caminho da imagem e retorna boolean
        if (setImageSaveToImageButton(caminho,imgFile)){
            setAlertDialogUpdateOnClickActivity(R.id.btn_salvar, NotesActivity.class, context, idAtual, "notas", caminhoSemPath);
        } else {
            setAlertDialogUpdateOnClickActivity(R.id.btn_salvar, NotesActivity.class, context, idAtual, "notas", caminho);
        }


        setActionOnClick(R.id.btn_selec_date, new OnButtonClickActionCalendar());
        //Se existir imagem já salva é possivel visualiza-la
        setActionOnClick(R.id.ib_foto, new OnButtonClickActionImage(imgFile));
        setActionOnClick(R.id.btn_cancelar, new OnButtonClickActionCancel());
        setActionOnClick(R.id.btn_picture, new OnButtonClickActionPicture(savedInstanceState));
        //Quando o Cartão possui credito ou debito, realiza a ação no RadioButton de desabilitar o spinner e o outro radiobutton
        setActionOnClick(R.id.rb_credit, new OnRadioButtonClickActionCredit());
        setActionOnClick(R.id.rb_debit, new OnRadioButtonClickActionDedit());
    }

    private void createDirectoryImage() {
        imageUtilities = new ImageUtilities();
        try {
            imageUtilities.createDirectory(Constants.FOLDER_NAME_NOTES);
            photoFile1 = imageUtilities.createImageFile();
            caminhoSemPath = ImageUtilities.returnCaminhoSemPath();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSpinnerParcel() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.parcelas_array, R.layout.celula_spinner_parcelas_layout);
        adapter.setDropDownViewResource(R.layout.celular_spinner_dropdown_parcelas_layout);
        sp_parcelas.setAdapter(adapter);
    }

    private void setSpinnerCard() {
        adapter = new RecordSpinnerCardAdapter(context, R.layout.celula_spinner_card_layout, cardDao.getListCard());
        sp_card.setAdapter(adapter);
    }

    private void setField() {
        sp_card.setSelection(getSpinnerIndex(sp_card, String.valueOf(idCartao)));
        sp_parcelas.setSelection(nAux.getParcelas() -1);

        if (nAux.getTipo() == 1) {
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

        et_title_invoice.setText(nAux.getTitlenotas());
        String price = nAux.getPreconotas();
        //formata o valor como 0,00 ou 0.00
        if (price != null) {
            price = formatPrice(price);
        }
        et_value.setText(price);
        et_desc_invoice.setText(nAux.getDesnotas());
        //formata a data
        String data = formatDate(String.valueOf((int) nAux.getDia() + "/" + nAux.getMes() + "/" + nAux.getAno()));
        btn_selec_date.setText(data);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PROCESSO_TIRAR_FOTO && resultCode == RESULT_OK) {

            //tira o background
            ib_foto.setBackgroundResource(0);
            //envia a foto tirada para o ImageButton
            imageUtilities.setPhotoToBitmap(context, ib_foto);
            //Torna o imagem
            ib_foto.setEnabled(true);
            ib_foto.setClickable(true);

            tv_click_image.setVisibility(View.VISIBLE);
            setAlertDialogUpdateOnClickActivity(R.id.btn_salvar, NotesActivity.class, context, idAtual, "notas", caminhoSemPath, imgFile);

            setActionOnClick(R.id.ib_foto, new OnButtonClickActionImage(photoFile1));

        } else {
            setMessage(R.string.operation_cancel);
        }
    }

    private void getParameters() {
        idAtual = getIntent().getLongExtra(Constants.ID_BANCO, 0);
    }

    //Verifica em qual posição o IDCartão desejado está;
    public int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;
        hmAux = cardDao.getListCard();

        for (int i = 0; i < spinner.getCount(); i++) {
            HMAuxCard model = hmAux.get(i);
            String modelS = model.get(CardDao.IDCARTAO);
            if (modelS != null) {
                if (modelS.equals(myString)) {
                    index = i;
                }
            }
        }
        return index;
    }

    private class OnButtonClickActionImage implements View.OnClickListener {
        private File imagemFile;

        public OnButtonClickActionImage(File imagemFile) {
            this.imagemFile = imagemFile;
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            //IMPORTANTE PRA CONSEGUIR LER A IMAGEM:
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setDataAndType(FileUtilities.getUri(getApplicationContext(), imagemFile), "image/*");
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
                caminhoSemPath = savedInstanceState.getString("caminhoSemPath");

            } else if (ImageUtilities.checkDirectory()) {
                try {
                    imageUtilities.createDirectory(Constants.FOLDER_NAME_NOTES);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dispatchTakePictureIntent();
            //
        }
    }

    private void dispatchTakePictureIntent() {
        Intent fotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (fotoIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            photoFile = photoFile1;
            // Continue only if the File was successfully created
            if (photoFile != null) {

                fotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtilities.getUri(getApplicationContext(), photoFile));
                startActivityForResult(fotoIntent, Constants.PROCESSO_TIRAR_FOTO);
            }
        }
    }

    private class OnSpinnerClickAction implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sp_card.setSelection(position);
            validation("spinner_action_credito_debito");

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            sp_card.setEnabled(false);
            ll_hint_spinner.setVisibility(View.VISIBLE);

        }
    }

    private class OnButtonClickActionCalendar implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(NotesEditActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            calendar.set(year, month, day);
                            String format = "dd/MM/yyyy";
                            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
                            Date date;

                            try {
                                date = sdf.parse(sdf.format(calendar.getTime()));
                                String dayS = new SimpleDateFormat("dd", Locale.ENGLISH).format(date);
                                String monthS = new SimpleDateFormat("MM", Locale.ENGLISH).format(date);
                                String yearS = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(date);

                                btn_selec_date.setText((dayS + "/" + monthS + "/" + yearS));
                            } catch (ParseException ignored) {

                            }
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
            callListView(NotesViewActivity.class, idAtual);

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
            setAlertDialogOnClickActivity(CardAddActivity.class, NotesViewActivity.class, idAtual, "notas_cartao");


        } else if (idCartao == -1) {
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
            sp_parcelas.setEnabled(true);
            view_sp_disabled.setVisibility(View.INVISIBLE);

        }
    }

    private class OnRadioButtonClickActionDedit implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            view_sp_disabled.setVisibility(View.VISIBLE);
            tv_sp_disabled.setEnabled(false);
            sp_parcelas.setSelection(0);
            sp_parcelas.setEnabled(false);

        }
    }

    public void onBackPressed() {
        ImageUtilities.deleteImage();
        callListView(NotesViewActivity.class, idAtual);

    }
}

