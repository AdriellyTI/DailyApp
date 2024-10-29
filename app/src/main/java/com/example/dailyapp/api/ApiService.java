package com.example.dailyapp.api;

import com.example.dailyapp.model.Task;
import com.example.dailyapp.model.TaskResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

// Retrofit Interface para enviar e buscar dados
public interface ApiService {
    @FormUrlEncoded
    @POST("create_task.php")
    Call<ResponseBody> createTask(
            @Field("titulo") String titulo,
            @Field("descricao") String descricao,
            @Field("tipo_notificacao") String tipoNotificacao,
            @Field("cor") String cor
    );

    @GET("get_tasks.php")
    Call<List<Task>> getTasks();

    @POST("create_task.php")
    Call<TaskResponse> createTask(@Body Task task);
}

