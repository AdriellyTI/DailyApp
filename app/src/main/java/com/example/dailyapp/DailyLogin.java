package com.example.dailyapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class DailyLogin extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText emailEditText, passwordEditText;
    private TextView textViewSignup;
    private Button btnAvancar;


    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_login);



        // Inicializa o FirebaseAuth
        mAuth = FirebaseAuth.getInstance();


        // Verifica se o usuário já está autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Se o usuário já estiver logado, redireciona para UserProfileActivity
            Intent intent = new Intent(DailyLogin.this, DailyFirstSight.class);
            // ess UserProfileActivity.class, ele vai ser a tela que vai ser sempre a primeira tela
            startActivity(intent);
            finish(); // Fecha a tela de login
        }


        emailEditText = findViewById(R.id.edit_email);
        passwordEditText = findViewById(R.id.edit_password);
        textViewSignup = findViewById(R.id.text_view_signup);
        btnAvancar = findViewById(R.id.button_avancar);



        // Configurar o Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DailyLogin", "TextView clicked");
                Intent intent = new Intent(DailyLogin.this, DailyCadastro.class);
                startActivity(intent);
            }
        });

        // Associa o botão de login ao método signInWithGoogle
        findViewById(R.id.sign_in_button).setOnClickListener(view -> signInWithGoogle());

        // Configurando o clique do botão de login
        btnAvancar.setOnClickListener(view -> signInWithEmail(view));
    }

    public void signInWithEmail(View view) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Por favor, insira o email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Por favor, insira a senha");
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login bem-sucedido
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(DailyLogin.this, UserProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = task.getException().getMessage();
                        if (errorMessage.contains("password is invalid")) {
                            Toast.makeText(DailyLogin.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                        } else if (errorMessage.contains("no user record")) {
                            Toast.makeText(DailyLogin.this, "Email não registrado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DailyLogin.this, "Erro: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // Método para iniciar o processo de Sign-In com Google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                        Toast.makeText(DailyLogin.this, "Falha na autenticação", Toast.LENGTH_SHORT).show();
                        updateUI(null);  // Atualiza a UI sem usuário autenticado
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(DailyLogin.this, DailyFirstSight.class);
            startActivity(intent);
            finish(); // Fecha a tela de login
        } else {
            Toast.makeText(DailyLogin.this, "Erro ao autenticar. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }


    public void openDailyCadastro(View view) {
        Log.d("DailyLogin", "openDailyCadastro clicked");
        Intent intent = new Intent(DailyLogin.this, DailyCadastro.class);
        startActivity(intent);
    }
}
