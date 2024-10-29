package com.example.dailyapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dailyapp.api.ApiService;
import com.example.dailyapp.model.Task;
import com.example.dailyapp.model.TaskResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DailyCreateTask extends AppCompatActivity {

    private EditText taskTitle, taskDescription;
    private Spinner notificationSpinner, colorSpinner;
    private Button saveButton;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_create_task);

        // Inicializar os componentes da interface
        taskTitle = findViewById(R.id.title_text);
        taskDescription = findViewById(R.id.description_text);
        notificationSpinner = findViewById(R.id.notificatio_spinner);
        colorSpinner = findViewById(R.id.colorSpinner);
        saveButton = findViewById(R.id.savebutton);

        // Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://seu-servidor.com/") // URL do backend
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Configurar o clique no botão de salvar
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitle.getText().toString();
                String description = taskDescription.getText().toString();
                String notificationType = notificationSpinner.getSelectedItem().toString();
                String color = colorSpinner.getSelectedItem().toString();

                // Verifica se os campos estão preenchidos
                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(DailyCreateTask.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Chama o método para criar a tarefa
                    saveTask(title, description, notificationType, color);
                }
            }
        });
    }

    private void saveTask(String title, String description, String notificationType, String color) {
        // Cria um objeto Task para enviar via Retrofit
        Task task = new Task(title, description, notificationType, color);

        // Faz a requisição para o servidor usando Retrofit
        Call<TaskResponse> call = apiService.createTask(task);
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful()) {
                    TaskResponse taskResponse = response.body();
                    if (taskResponse != null && taskResponse.getMessage() != null) {
                        Toast.makeText(DailyCreateTask.this, "Tarefa criada com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DailyCreateTask.this, "Erro ao criar tarefa", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DailyCreateTask.this, "Resposta falhou: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Toast.makeText(DailyCreateTask.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("DailyCreateTask", "Erro ao enviar dados: ", t);
            }
        });
    }
}
