package com.poo.proyectobless;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.poo.proyectobless.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

//    Button btn_iniciar_sesion;
//   EditText txtcorreo, txtcontraseña;;
//    TextView txtregistro;

    private ActivityLoginBinding binding;
    FirebaseAuth auth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityLoginBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            auth = FirebaseAuth.getInstance();

            //reemplazar por BINDING
            //txtcorreo = findViewById(R.id.txtcorreo);
            //txtcontraseña = findViewById(R.id.txtcontraseña);
            //btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion);
            //txtregistro = findViewById(R.id.txtregistro);

            binding.btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String correo = binding.txtcorreo.getText().toString().trim();
                    String contraseña = binding.txtcontraseA.getText().toString().trim();

                    if (correo.isEmpty() && contraseña.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Por favor, ingrese todos los campos", Toast.LENGTH_SHORT).show();
                    } else {
                        iniciarSesion(correo, contraseña);
                    }
                }
            });

            binding.txtregistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
                }
            });
    }

    private void iniciarSesion(String correo, String contraseña) {
            auth.signInWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                }
            });
    }
}