package com.example.dailyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView emailTextView;
    private Button btnLogout;

    private ImageView voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        // Create an instance of ImageView by finding it in the layout
        voltar = findViewById(R.id.voltar);

        // Set the click listener for the voltar ImageView
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UserProfileActivity", "Voltar clicked");
                Intent intent = new Intent(UserProfileActivity.this, DailyFirstSight.class);
                startActivity(intent);
            }
        });


        // Inicializa FirebaseAuth e o GoogleSignInClient
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Conecta o TextView e o botão de logout do layout
        emailTextView = findViewById(R.id.emailTextView);
        btnLogout = findViewById(R.id.btn_logout);

        // Exibe o email do usuário atual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            emailTextView.setText("" + user.getEmail());
        } else {
            emailTextView.setText("No user data available");
        }

        // Define a ação do botão de logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });
    }

    // Exibe a caixa de diálogo para confirmação do logout
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setMessage("Você quer realmente sair?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    // Método de logout
    private void signOut() {
        // Deslogar do Firebase
        mAuth.signOut();

        // Deslogar do Google
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(UserProfileActivity.this, "Você saiu da conta", Toast.LENGTH_SHORT).show();

            // Redireciona para a tela de login
            Intent intent = new Intent(UserProfileActivity.this, DailyLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
