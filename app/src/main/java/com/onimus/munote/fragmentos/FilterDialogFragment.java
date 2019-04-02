package com.onimus.munote.fragmentos;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.onimus.munote.R;
import com.onimus.munote.bancos.banco.HMAuxCard;
import com.onimus.munote.bancos.banco.HMAuxNotes;
import com.onimus.munote.bancos.banco.RecordSpinnerCardAdapter;
import com.onimus.munote.bancos.banco.RecordSpinnerNotesMonthAdapter;
import com.onimus.munote.bancos.banco.RecordSpinnerNotesYearAdapter;
import com.onimus.munote.bancos.dao.CardDao;
import com.onimus.munote.bancos.dao.NotesDao;
import com.onimus.munote.files.Filters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.onimus.munote.Constants.*;
import static com.onimus.munote.files.FileUtilities.isAndroidMarshmallowOrSuperiorVersion;

public class FilterDialogFragment extends DialogFragment {

    public interface FilterListener {

        void onFilter(Filters filters);

    }
    private View view;
    private Spinner sp_card;
    private Spinner sp_year;
    private Spinner sp_month;
    private Spinner sp_sort;

    private CheckBox cb_credit;
    private CheckBox cb_debit;

    private Button btn_search;
    private Button btn_cancel;

    private NotesDao notesDao;
    private CardDao cardDao;
    private ArrayList<HMAuxNotes> hmAux;
    private RecordSpinnerNotesMonthAdapter adapterMonth;
    private RecordSpinnerNotesYearAdapter adapterYear;
    private RecordSpinnerCardAdapter adapterCard;

    private String anoAtual;
    private String mesAtual;
    private String newYear;

    private FilterListener mFilterListener;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_filters, container, false);

        startVariables();
        startAction();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);


        if (context instanceof FilterListener) {
            mFilterListener = (FilterListener) context;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getDialog().getWindow()).setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void startVariables() {
        sp_card = view.findViewById(R.id.sp_card);
        sp_year = view.findViewById(R.id.sp_year);
        sp_month = view.findViewById(R.id.sp_month);
        sp_sort = view.findViewById(R.id.sp_sort);

        cb_credit = view.findViewById(R.id.cb_credito);
        cb_debit = view.findViewById(R.id.cb_debito);

        btn_search = view.findViewById(R.id.buttonSearch);
        btn_cancel = view.findViewById(R.id.buttonCancel);

    }
    private void startAction() {
        notesDao = new NotesDao(context);
        cardDao = new CardDao(context);
        //
        anoAtual = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        mesAtual = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        //
        setArrowToSpinnerLowerVersion();
        setSpinnerCard();
        setSpinnerYear();
        setSpinnerMonth();
        setSpinnerSortBy();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFilterListener != null) {
                    mFilterListener.onFilter(getFilters());
                }

                dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setSpinnerSortBy() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.sort_by_array, R.layout.celula_spinner_parcelas_layout);
        adapter.setDropDownViewResource(R.layout.celular_spinner_dropdown_parcelas_layout);
        sp_sort.setAdapter(adapter);
    }

    private void setSpinnerYear() {
        adapterYear = new RecordSpinnerNotesYearAdapter(context, R.layout.celula_spinner_year_layout, notesDao.getListYearNotes());
        sp_year.setAdapter(adapterYear);
        sp_year.setSelection(getSpinnerYearIndex(sp_year, anoAtual));
        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_year.setSelection(position);

                HMAuxNotes item = (HMAuxNotes) parent.getSelectedItem();
                newYear = item.get(NotesDao.YEAR);

                adapterMonth.updateDataChanged(notesDao.getListMonthNotes(newYear));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerMonth() {

        if (newYear != null) {
            adapterMonth = new RecordSpinnerNotesMonthAdapter(context, R.layout.celula_spinner_month_layout, notesDao.getListMonthNotes(newYear));
        } else {
            adapterMonth = new RecordSpinnerNotesMonthAdapter(context, R.layout.celula_spinner_month_layout, notesDao.getListMonthNotes(anoAtual));
        }
        sp_month.setAdapter(adapterMonth);
        if (newYear != null && newYear.equals(anoAtual)) {
            sp_month.setSelection(getSpinnerMonthIndex(sp_month, mesAtual, anoAtual));
        } else if (newYear == null) {
            sp_month.setSelection(getSpinnerMonthIndex(sp_month, mesAtual, anoAtual));

        }
        //inicializa o spinner_month
        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_month.setSelection(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerCard() {
        adapterCard = new RecordSpinnerCardAdapter(context, R.layout.celula_spinner_card_layout, cardDao.getListCardOnFilter());

        sp_card.setAdapter(adapterCard);
        sp_card.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_card.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Filters getFilters() {
        Filters filters = new Filters();

        if (view != null) {
            HMAuxCard item;
            HMAuxNotes item2;
            HMAuxNotes item3;
            item = (HMAuxCard) sp_card.getSelectedItem();
            item2 = (HMAuxNotes) sp_year.getSelectedItem();
            item3 = (HMAuxNotes) sp_month.getSelectedItem();

            if (item != null && item2 != null && item3 != null) {
                filters.setYear(item2.get(NotesDao.YEAR));
                filters.setMonth(item3.get(NotesDao.MONTH));

                filters.setIdCard(item.get(CardDao.ID_CARD));
                filters.setDescCard(item.get(CardDao.DESC_CARD));

            } else if (item == null) {
                Toast.makeText(context, getString(R.string.tv_no_card), Toast.LENGTH_SHORT).show();
                dismiss();

            } else if (item2 == null && item3 == null) {
                Toast.makeText(context, getString(R.string.tv_no_invoice), Toast.LENGTH_SHORT).show();
                dismiss();
            }
            if ((cb_credit.isChecked() && cb_debit.isChecked()) || (!cb_credit.isChecked() && !cb_debit.isChecked())) {
                filters.setType("3");
            } else if (cb_credit.isChecked() && !cb_debit.isChecked()) {
                filters.setType("1");
            } else if (cb_debit.isChecked() && !cb_credit.isChecked()) {
                filters.setType("2");
            }

            filters.setSortBy(getSelectedSortBy());
        }

        return filters;
    }

    //Verifica em qual posição está o YEAR;
    public int getSpinnerYearIndex(Spinner spinner, String ano) {
        int index = 0;
        hmAux = notesDao.getListYearNotes();

        for (int i = 0; i < spinner.getCount(); i++) {
            HMAuxNotes model = hmAux.get(i);
            String modelS = model.get(NotesDao.YEAR);
            if (modelS != null) {
                if (modelS.equals(ano)) {
                    index = i;
                }
            }
        }
        return index;
    }

    //Verifica em qual posição está o mês;
    public int getSpinnerMonthIndex(Spinner spinner, String mes, String ano) {
        int index = 0;
        hmAux = notesDao.getListMonthNotes(ano);

        for (int i = 0; i < spinner.getCount(); i++) {
            HMAuxNotes model = hmAux.get(i);
            String modelS = model.get(NotesDao.MONTH);
            if (modelS != null) {
                if (modelS.equals(mes)) {
                    index = i;
                }
            }
        }
        return index;
    }

    public void setArrowToSpinnerLowerVersion() {
        ImageView iv_arrow;
        ImageView iv_arrow2;
        ImageView iv_arrow3;
        ImageView iv_arrow4;

        iv_arrow = view.findViewById(R.id.iv_arrow1);
        iv_arrow2 = view.findViewById(R.id.iv_arrow2);
        iv_arrow3 = view.findViewById(R.id.iv_arrow3);
        iv_arrow4 = view.findViewById(R.id.iv_arrow4);

        if (isAndroidMarshmallowOrSuperiorVersion()) {
            iv_arrow.setEnabled(false);
            iv_arrow2.setEnabled(false);
            iv_arrow3.setEnabled(false);
            iv_arrow4.setEnabled(false);
            iv_arrow.setVisibility(View.INVISIBLE);
            iv_arrow2.setVisibility(View.INVISIBLE);
            iv_arrow3.setVisibility(View.INVISIBLE);
            iv_arrow4.setVisibility(View.INVISIBLE);
        }
    }

    private String getSelectedSortBy() {
        String selected = (String) sp_sort.getSelectedItem();
        if (getString(R.string.sort_by_day).equals(selected)) {
            return FIELD_DAY;
        }
        if (getString(R.string.sort_by_price).equals(selected)) {
            return FIELD_PRICE;
        }
        if (getString(R.string.sort_by_title).equals(selected)) {
            return FIELD_TITLE;
        }

        return null;
    }
}
