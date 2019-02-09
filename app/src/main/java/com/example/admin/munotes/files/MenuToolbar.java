package com.example.admin.munotes.files;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;

import com.example.admin.munotes.R;
import com.example.admin.munotes.Constants;
import com.example.admin.munotes.activity.CardAddActivity;
import com.example.admin.munotes.activity.MenuActivity;
import com.example.admin.munotes.activity.NotesAddActivity;

import static com.example.admin.munotes.Constants.CARDADDACTIVITY;
import static com.example.admin.munotes.Constants.NOTASADDACTIVITY;

@SuppressLint("Registered")
public class MenuToolbar extends MainUtilities {

    String nameActivity = getClass().getSimpleName();

    public boolean onCreateOptionsMenu(Menu menu) {


        // Inflar o menu; isso adiciona itens à barra de ação, se estiver presente.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        switch (nameActivity) {
            //Quando estiver na Activity selecionada o menu referente ao item dela fica invisivel;
            case NOTASADDACTIVITY: {
                MenuItem itemNotas = menu.findItem(R.id.action_notas);
                itemNotas.setVisible(false);
                break;
            }
            case CARDADDACTIVITY: {
                MenuItem itemCartao = menu.findItem(R.id.action_cartao);
                itemCartao.setVisible(false);
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
            actionMenuToolbar(Constants.HOME);
            return true;
        }
        if (id == R.id.action_galeria) {
            actionMenuToolbar(Constants.GALERIA);
            return true;
        }

        if (id == R.id.action_notas) {
            actionMenuToolbar(Constants.NOTAS);
            return true;
        }

        if (id == R.id.action_cartao) {
            actionMenuToolbar(Constants.CARTAO);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void actionMenuToolbar(String category) {

        switch (nameActivity) {
            //No momento que acessa o menu o usuário está na NotesAddActivity;
            case NOTASADDACTIVITY: {
                switch (category) {

                    case Constants.HOME: {
                        setAlertDialogToReturnOnClickActivity(MenuActivity.class, "notas");

                        break;
                    }
                    case Constants.GALERIA: {
                        openESFileExplorer();
                        break;
                    }
                    case Constants.CARTAO: {
                        setAlertDialogToReturnOnClickActivity(CardAddActivity.class, "notas_cartao");
                        break;
                    }
                }
                break;

            }
            //No momento que acessa o menu o usuário está na CardAddActivity;
            case CARDADDACTIVITY: {
                switch (category) {

                    case Constants.HOME: {
                        setAlertDialogToReturnOnClickActivity(MenuActivity.class, "cartao");
                        break;
                    }
                    case Constants.GALERIA: {
                        openESFileExplorer();
                        break;
                    }
                    case Constants.NOTAS: {
                        setAlertDialogToReturnOnClickActivity(NotesAddActivity.class, "cartao_notas");
                        break;
                    }

                }
                break;
            }

            default: {
                switch (category) {

                    case Constants.HOME: {
                        callActivity(getApplicationContext(), MenuActivity.class);
                        break;
                    }
                    case Constants.GALERIA: {
                        openESFileExplorer();
                        break;
                    }
                    case Constants.NOTAS: {
                        callListView(NotesAddActivity.class, -1L);
                        break;
                    }
                    case Constants.CARTAO: {
                        callListView(CardAddActivity.class, -1L);
                        break;
                    }
                }
                break;
            }
        }


    }


}
