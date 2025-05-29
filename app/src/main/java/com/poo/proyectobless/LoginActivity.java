package com.poo.proyectobless;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.poo.proyectobless.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    EditText usuario, contraseña;
    TextView lblregistrate;
    Button btningresar;
    Connection con;

    public LoginActivity() {
        ConexionDB instanceConecction = new ConexionDB();
        con = instanceConecction.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usuario = (EditText) findViewById(R.id.txtusuario);
        contraseña = (EditText) findViewById(R.id.txtcontraseña);
        lblregistrate = (TextView) findViewById(R.id.lblregistrate);
        btningresar = (Button) findViewById(R.id.btningresar);

        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginActivity.login().execute("");
            }
        });

        lblregistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(registro);

            }
        });


    }

    public class login extends AsyncTask<String, String, String> {
        String z = null;
        Boolean exito = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            if (con == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });

                z = "En conexión";
            } else {
                try {
                    String sql = "SELECT * FROM usuarios WHERE usuario = '" + usuario.getText() + "' AND contraseña = '" + contraseña.getText() + "'";
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(sql);

                    if (rs.next()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Acceso exitoso", Toast.LENGTH_SHORT).show();
                                Intent menu = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(menu);
                            }
                        });

                        usuario.setText("");
                        contraseña.setText("");
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Error en el usuario o contraseña", Toast.LENGTH_SHORT).show();
                            }
                        });

                        usuario.setText("");
                        contraseña.setText("");

                    }


                } catch (Exception e) {
                    exito = false;
                    Log.e("Error de conexión: ", e.getMessage());
                }
            }


            return z;
        }
    }


}