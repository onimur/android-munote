package com.onimus.munote.bancos.model;

import android.arch.lifecycle.ViewModel;

import com.onimus.munote.files.Filters;


public class NotesActivityViewModel extends ViewModel {

    private Filters mFilters;

    public NotesActivityViewModel() {
        mFilters = Filters.getDefault();
    }
    public Filters getFilters() {
        return mFilters;
    }

    public void setFilters(Filters mFilters) {
        this.mFilters = mFilters;
    }
}
