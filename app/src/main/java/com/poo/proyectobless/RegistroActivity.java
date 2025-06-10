package com.poo.proyectobless;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Importar Log
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.poo.proyectobless.databinding.ActivityRegistroBinding;

import java.util.HashMap;
import java.util.Map;


public class RegistroActivity extends AppCompatActivity {

    /*
    Button btnregistrarse;
    EditText usuarioRegister, emailRegister, contraseñaRegister;
     */

    private ActivityRegistroBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        /*usuarioRegister = findViewById(R.id.usuarioRegister);
        emailRegister = findViewById(R.id.emailRegister);
        contraseñaRegister = findViewById(R.id.contraseñaRegister);
        btnregistrarse = findViewById(R.id.btnregistarse);

         */
        binding.btnregistarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = binding.usuarioRegister.getText().toString().trim();
                String email = binding.emailRegister.getText().toString().trim();
                String contraseña = binding.contraseARegister.getText().toString().trim();

                if (usuario.isEmpty() || email.isEmpty() || contraseña.isEmpty()) { // Usar || (OR) aquí
                    Toast.makeText(RegistroActivity.this, "Por favor, ingrese todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    registroUsuario(usuario, email, contraseña);
                }
            }
        });
    }

    private void registroUsuario(String usuario, String email, String contraseña) {
        auth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { // 'this' para el Executor

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    // La creación del usuario en Firebase Auth fue exitosa
                    Log.d("RegistroActivity", "createUserWithEmail:success");
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    if (firebaseUser != null) {
                        String userId = firebaseUser.getUid();
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("id", userId);
                        userData.put("usuario", usuario);
                        userData.put("email", email);
                        userData.put("contraseña", contraseña);


                        db.collection("usuarios").document(userId).set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    // Datos guardados exitosamente en Firestore
                                    Log.d("RegistroActivity", "DocumentSnapshot successfully written!");
                                    Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                                })
                                .addOnFailureListener(e -> {
                                    // Falló el guardado en Firestore
                                    Log.w("RegistroActivity", "Error writing document", e);
                                    Toast.makeText(RegistroActivity.this, "Error al guardar datos adicionales: " + e.getMessage(), Toast.LENGTH_LONG).show();

                                    // Opcional: Intentar eliminar el usuario de Auth para consistencia (Esto es ayuda de IA)
                                    firebaseUser.delete().addOnCompleteListener(deleteTask -> {
                                        if (deleteTask.isSuccessful()) {
                                            Log.d("RegistroActivity", "Usuario Auth eliminado después de fallo en Firestore.");
                                        } else {
                                            Log.w("RegistroActivity", "No se pudo eliminar usuario Auth.", deleteTask.getException());
                                        }
                                    });
                                });
                    } else {
                        // Esto no debería ocurrir si task.isSuccessful() es true y getCurrentUser() es null
                        Log.w("RegistroActivity", "createUserWithEmail:success, but currentUser is null.");
                        Toast.makeText(RegistroActivity.this, "Error al obtener usuario.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // La creación del usuario en Firebase Auth falló
                    Log.w("RegistroActivity", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegistroActivity.this, "Error al crear cuenta: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}