package com.onimus.munote.repository.model;

import androidx.lifecycle.ViewModel;


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
