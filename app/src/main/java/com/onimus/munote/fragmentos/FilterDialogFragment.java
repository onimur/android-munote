package com.onimus.munote.fragmentos;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
import com.onimus.munote.bancos.model.Filters;
import com.onimus.munote.files.SpinnerIndex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.onimus.munote.Constants.*;
import static com.onimus.munote.files.ConvertType.convertToInt;
import static com.onimus.munote.files.ConvertType.convertToLong;
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

    private int yearActual;
    private int monthActual;
    private int newYear = -1;

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
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);


        if (context instanceof FilterListener) {
            mFilterListener = (FilterListener) context;
        }
    }

    @NonNull
    @Override
    @SuppressWarnings("all")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Estilo para tirar o Titulo do dialog, pois em algumas API aparece em branco
            return  new Dialog(Objects.requireNonNull(getActivity()), R.style.DialogStyle);
    }

    @Override
    @SuppressWarnings("all")
    public void onResume() {
        super.onResume();
        (getDialog()).getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void startVariables() {
        sp_card = view.findViewById(R.id.sp_card);
        sp_year = view.findViewById(R.id.sp_year);
        sp_month = view.findViewById(R.id.sp_month);
        sp_sort = view.findViewById(R.id.sp_sort);

        cb_credit = view.findViewById(R.id.cb_credit);
        cb_debit = view.findViewById(R.id.cb_debit);

        btn_search = view.findViewById(R.id.btn_apply);
        btn_cancel = view.findViewById(R.id.btn_cancel);

    }

    private void startAction() {
        notesDao = new NotesDao(context);
        cardDao = new CardDao(context);
        //
        yearActual = Calendar.getInstance().get(Calendar.YEAR);
        monthActual = (Calendar.getInstance().get(Calendar.MONTH) + 1);
        //
        setArrowToSpinnerLowerVersion();
        setSpinnerCard();
        setSpinnerYear();
        setSpinnerMonth();
        setSpinnerSortBy();

        btn_search.setOnClickListener(v -> {
            if (mFilterListener != null) {
                mFilterListener.onFilter(getFilters());
            }

            dismiss();
        });

        btn_cancel.setOnClickListener(v -> dismiss());
    }

    private void setSpinnerSortBy() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.sort_by_array, R.layout.cel_spinner_parcels_layout);
        adapter.setDropDownViewResource(R.layout.cel_spinner_dropdown_parcels_layout);
        sp_sort.setAdapter(adapter);
    }

    private void setSpinnerYear() {
        RecordSpinnerNotesYearAdapter adapterYear = new RecordSpinnerNotesYearAdapter(context, R.layout.cel_spinner_year_layout, notesDao.getListYearNotes());
        sp_year.setAdapter(adapterYear);

        hmAux = notesDao.getListYearNotes();
        sp_year.setSelection(SpinnerIndex.getSpinnerIndex(sp_year, yearActual, hmAux, NotesDao.YEAR));
        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_year.setSelection(position);

                HMAuxNotes item = (HMAuxNotes) parent.getSelectedItem();
                newYear = convertToInt(item.get(NotesDao.YEAR));

                adapterMonth.updateDataChanged(notesDao.getListMonthNotes(newYear));
                if (newYear != yearActual) {
                    sp_month.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerMonth() {

        if (newYear > 0) {
            adapterMonth = new RecordSpinnerNotesMonthAdapter(context, R.layout.cel_spinner_month_layout, notesDao.getListMonthNotes(newYear));
        } else {
            adapterMonth = new RecordSpinnerNotesMonthAdapter(context, R.layout.cel_spinner_month_layout, notesDao.getListMonthNotes(yearActual));
        }
        sp_month.setAdapter(adapterMonth);
        hmAux = notesDao.getListMonthNotes(yearActual);
        if (newYear == -1 || newYear == yearActual) {
            sp_month.setSelection(SpinnerIndex.getSpinnerIndex(sp_month, monthActual, hmAux, NotesDao.MONTH));
        } else {
            sp_month.setSelection(0);

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
        RecordSpinnerCardAdapter adapterCard = new RecordSpinnerCardAdapter(context, R.layout.cel_spinner_card_layout, cardDao.getListCardOnFilter());

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

    private Filters getFilters() {
        Filters filters = new Filters();

        if (view != null) {
            HMAuxCard item;
            HMAuxNotes item2;
            HMAuxNotes item3;
            item = (HMAuxCard) sp_card.getSelectedItem();
            item2 = (HMAuxNotes) sp_year.getSelectedItem();
            item3 = (HMAuxNotes) sp_month.getSelectedItem();

            if (item != null && item2 != null && item3 != null) {
                filters.setYear(convertToInt(item2.get(NotesDao.YEAR)));
                filters.setMonth(convertToInt(item3.get(NotesDao.MONTH)));

                filters.setIdCard(convertToLong(item.get(CardDao.ID_CARD)));
                filters.setDescCard(item.get(CardDao.DESC_CARD));

            } else if (item == null) {
                Toast.makeText(context, getString(R.string.tv_no_card), Toast.LENGTH_SHORT).show();
                dismiss();

            } else if (item2 == null && item3 == null) {
                Toast.makeText(context, getString(R.string.tv_no_invoice), Toast.LENGTH_SHORT).show();
                dismiss();
            }
            if ((cb_credit.isChecked() && cb_debit.isChecked()) || (!cb_credit.isChecked() && !cb_debit.isChecked())) {
                filters.setType(3);
            } else if (cb_credit.isChecked() && !cb_debit.isChecked()) {
                filters.setType(1);
            } else if (cb_debit.isChecked() && !cb_credit.isChecked()) {
                filters.setType(2);
            }

            filters.setSortBy(getSelectedSortBy());
        }

        return filters;
    }
    private void setArrowToSpinnerLowerVersion() {
        if (isAndroidMarshmallowOrSuperiorVersion()) {
            ImageView[] iv_arrow;
            iv_arrow = new ImageView[4];

            for(int i=0; i< iv_arrow.length; i++) {
                String buttonID = "iv_arrow" + (i + 1);
                int resID = getResources().getIdentifier(buttonID, "id", context.getPackageName());
                iv_arrow[i] = view.findViewById(resID);
                iv_arrow[i].setEnabled(false);
                iv_arrow[i].setVisibility(View.INVISIBLE);
            }
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
