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

package com.onimus.munote.files;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;

import com.onimus.munote.R;
import com.onimus.munote.activity.CardAddActivity;
import com.onimus.munote.activity.MenuActivity;
import com.onimus.munote.activity.NotesAddActivity;

import static com.onimus.munote.Constants.*;

@SuppressLint("Registered")
public class MenuToolbar extends MainUtilities {

    private final String nameActivity = getClass().getSimpleName();

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflar o menu; isso adiciona itens à barra de ação, se estiver presente.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        switch (nameActivity) {
            //Quando estiver na Activity selecionada o menu referente ao item dela fica invisivel;
            case NOTES_ADD_ACTIVITY: {
                MenuItem itemNotes = menu.findItem(R.id.action_notes);
                itemNotes.setVisible(false);
                break;
            }
            case CARD_ADD_ACTIVITY: {
                MenuItem itemCard = menu.findItem(R.id.action_card);
                itemCard.setVisible(false);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Lidar com cliques de itens da barra de ação aqui.
        // A barra de ação manipulará automaticamente os cliques
        // no botão Início / Acima, desde que você especifique uma
        // atividade pai em AndroidManifest.xml.
        int id = item.getItemId();

        //nenhuma inspeção Simplificável se Declaração
        if (id == R.id.action_home) {
            actionMenuToolbar(HOME);
            return true;
        }
        if (id == R.id.action_gallery) {
            actionMenuToolbar(GALLERY);
            return true;
        }

        if (id == R.id.action_notes) {
            actionMenuToolbar(NOTES);
            return true;
        }

        if (id == R.id.action_card) {
            actionMenuToolbar(CARD);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void actionMenuToolbar(String category) {

        switch (nameActivity) {
            //No momento que acessa o menu o usuário está na NotesAddActivity;
            case NOTES_ADD_ACTIVITY: {
                switch (category) {

                    case HOME: {
                        setAlertDialogToReturnOnClickActivity(MenuActivity.class, NOTES);
                        break;
                    }
                    case GALLERY: {
                        openESFileExplorer();
                        break;
                    }
                    case CARD: {
                        setAlertDialogToReturnOnClickActivity(CardAddActivity.class, NOTES_TO_CARD);
                        break;
                    }
                }
                break;
            }
            //No momento que acessa o menu o usuário está na CardAddActivity;
            case CARD_ADD_ACTIVITY: {
                switch (category) {
                    case HOME: {
                        setAlertDialogToReturnOnClickActivity(MenuActivity.class, CARD);
                        break;
                    }
                    case GALLERY: {
                        openESFileExplorer();
                        break;
                    }
                    case NOTES: {
                        setAlertDialogToReturnOnClickActivity(NotesAddActivity.class, CARD_TO_NOTES);
                        break;
                    }
                }
                break;
            }

            default: {
                switch (category) {

                    case HOME: {
                        callActivity(getApplicationContext(), MenuActivity.class);
                        break;
                    }
                    case GALLERY: {
                        openESFileExplorer();
                        break;
                    }
                    case NOTES: {
                        callListView(NotesAddActivity.class, -1L);
                        break;
                    }
                    case CARD: {
                        callListView(CardAddActivity.class, -1L);
                        break;
                    }
                }
                break;
            }
        }
    }
}
