package com.onimus.munotes.files;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.onimus.munotes.R;
import com.onimus.munotes.fragmentos.NotesFragmentBoth;
import com.onimus.munotes.fragmentos.NotesFragmentCredit;
import com.onimus.munotes.fragmentos.NotesFragmentDebit;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
private Context context;
private String data1;
private String data2;

    public ViewPagerAdapter(FragmentManager fm, Context context, String data1, String data2) {
        super(fm);
        this.context = context;
        this.data1 = data1;
        this.data2 = data2;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0: {
                fragment = NotesFragmentCredit.newInstance(data1, data2);
                break;
            }
            case 1: {
                fragment = NotesFragmentDebit.newInstance(data1, data2);
                break;
            }
            case 2: {
                fragment = NotesFragmentBoth.newInstance(data1, data2);

                break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0: {
             return   context.getString(R.string.cb_credito);
            }
            case 1: {
                return context.getString(R.string.cb_debito);
            }
            case 2: {
             return  context.getString(R.string.text_both);
            }
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        // este método será chamado para cada fragmento no ViewPager
        if (object instanceof NotesFragmentBoth) {
            // não força recarregmento
            return POSITION_UNCHANGED;
        } else {
            //POSITION_NONE significa algo como: este fragmento não é mais válido
            //acionando o ViewPager para atualizar a instância desse fragmento.
            return POSITION_NONE;
        }
    }

}
