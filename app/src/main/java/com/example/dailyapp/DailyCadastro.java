package com.example.dailyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class DailyCadastro extends AppCompatActivity {

    private FirebaseAuth mAuth; // Firebase authentication
    private GoogleSignInClient mGoogleSignInClient; // Google Sign-In client
    private TextView textpossoucadastro;

    private static final int RC_SIGN_IN = 9001; // Request code for Google Sign-In

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_cadastro);

        // Inicializa o Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        textpossoucadastro = findViewById(R.id.text_possou_cadastro);

        // Configura as opções de login do Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // Solicita o ID Token para autenticação
                .requestEmail()  // Solicita o email do usuário
                .build();


        // Inicializa o GoogleSignInClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Se o usuário já estiver logado, redireciona para UserProfileActivity
            Intent intent = new Intent(DailyCadastro.this, DailyFirstSight.class);
            // ess UserProfileActivity.class, ele vai ser a tela que vai ser sempre a primeira tela
            startActivity(intent);
            finish(); // Fecha a tela de login
        }

        textpossoucadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DailyLogin", "TextView clicked");
                Intent intent = new Intent(DailyCadastro.this, DailyLogin.class);
                startActivity(intent);
            }
        });

        // Configura o botão de login com Google
        findViewById(R.id.google).setOnClickListener(view -> signInWithGoogle());
    }

    // Método que inicia o fluxo de login com o Google
    private void signInWithGoogle() {
        // Cria a intenção de login e inicia a atividade de login com Google
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);  // Chama a atividade esperando um resultado (código RC_SIGN_IN)
    }

    // Método que trata o resultado do login, como o sucesso ou falha
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verifica se o resultado é do login com Google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);  // Recupera a conta do Google a partir dos dados da intenção
            try {
                // Tenta obter a conta do Google autenticada
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("GoogleSignIn", "Login com Google bem-sucedido");

                // Chama o método para autenticar no Firebase usando o ID Token do Google
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Se houver falha na autenticação, exibe uma mensagem de erro
                Log.w("GoogleSignIn", "Falha no login com Google", e);
                Toast.makeText(this, "Falha no login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para autenticar no Firebase usando as credenciais do Google
    private void firebaseAuthWithGoogle(String idToken) {
        // Cria credenciais de autenticação usando o ID Token do Google
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        // Realiza o login no Firebase com as credenciais
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Se o login foi bem-sucedido, obtém o usuário atual e atualiza a interface
                        Log.d("FirebaseAuth", "Autenticação com Firebase bem-sucedida");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);  // Atualiza a UI com o usuário autenticado
                    } else {
                        // Se o login falhou, exibe uma mensagem de erro
                        Log.w("FirebaseAuth", "Falha na autenticação com Firebase", task.getException());
                        Toast.makeText(DailyCadastro.this, "Falha na autenticação", Toast.LENGTH_SHORT).show();
                        updateUI(null);  // Atualiza a UI sem usuário autenticado
                    }
                });
    }


    // Método para atualizar a interface com base no usuário autenticado
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Se o usuário está autenticado, abre a tela de perfil e encerra a tela atual
            Intent intent = new Intent(DailyCadastro.this, DailyFirstSight.class);
            startActivity(intent);
            finish();  // Finaliza a tela de cadastro
        } else {
            // Exibe uma mensagem caso a autenticação tenha falhado
            Toast.makeText(DailyCadastro.this, "Erro ao autenticar. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openDailyLogin(View view) {
        Log.d("DailyCadastro", "openDailyLogin clicked");
        Intent intent = new Intent(DailyCadastro.this, DailyLogin.class);
        startActivity(intent);
    }
}
