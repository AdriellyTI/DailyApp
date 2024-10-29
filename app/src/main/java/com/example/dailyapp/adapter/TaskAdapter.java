package com.example.dailyapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyapp.R;
import com.example.dailyapp.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;

    // Construtor para inicializar a lista de tarefas
    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item de tarefa
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // Obter a tarefa da lista pela posição atual
        Task task = taskList.get(position);

        // Definir os valores da tarefa nos componentes visuais
        holder.taskTitle.setText(task.getTitulo());
        holder.taskDescription.setText(task.getDescricao());
        holder.taskNotificationType.setText(task.getTipoNotificacao());
        holder.taskColor.setText(task.getCor());
    }

    @Override
    public int getItemCount() {
        return taskList.size();  // Retorna o número total de tarefas na lista
    }

    // ViewHolder para os componentes visuais de cada item de tarefa
    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView taskTitle, taskDescription, taskNotificationType, taskColor;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitle = itemView.findViewById(R.id.textView_taskTitle);
            taskDescription = itemView.findViewById(R.id.textView_taskDescription);
            taskNotificationType = itemView.findViewById(R.id.textView_taskNotificationType);
            taskColor = itemView.findViewById(R.id.textView_taskColor);
        }
    }
}
