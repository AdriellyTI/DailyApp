package com.example.dailyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyapp.adapter.TaskAdapter;
import com.example.dailyapp.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;  // Essa lista será preenchida com dados das tarefas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar o adapter e configurar RecyclerView
        taskAdapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(taskAdapter);

        // Configurar o FloatingActionButton
        ImageButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir a tela de criação de tarefa (DailyCreateTask)
                Intent intent = new Intent(MainActivity.this, DailyCreateTask.class);
                startActivity(intent);
            }
        });

        // Aqui você pode buscar as tarefas do servidor e preencher o RecyclerView
        // Exemplo: fetchTasksFromServer();
    }
}