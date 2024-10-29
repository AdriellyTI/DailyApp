package com.example.dailyapp;



import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class FirstFragment  extends DialogFragment {

    //Primeiro fragmento: pequena tela de opções que aparece quando o usuario aperta no
    // botão de adicionar da classe First_Sight
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Infla o layout customizado para o painel
        View view = inflater.inflate(R.layout.panel_dialog, null);

        // Configura os botões
        TextView createItemText = view.findViewById(R.id.create_item_text);
        createItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DailyCreateTask.class);
                startActivity(intent);
                dismiss(); // Fecha o painel
            }
        });

        TextView createListText = view.findViewById(R.id.create_list_text);
        createListText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DailyCreateList.class);
                startActivity(intent);
                dismiss(); // Fecha o painel
            }
        });

        TextView createStudyText = view.findViewById(R.id.create_study_text);
        createStudyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DailyCreateStudy.class);
                startActivity(intent);
                dismiss(); // Fecha o painel
            }
        });;

        builder.setView(view);
        return builder.create();
    }
}
