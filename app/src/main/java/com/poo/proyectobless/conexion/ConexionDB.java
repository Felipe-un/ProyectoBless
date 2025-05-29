package com.poo.proyectobless.conexion;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionDB {

    private String ip = "192.168.20.27"; //Default SQL Server IP Address
    private String usuario = "sa"; //Default SQL Server User name
    private String password = ""; //Default SQL Server Password
    private String baseDeDatos = "test"; //Default Database Name

    @SuppressLint("NewApi")
        public Connection connect() {
        Connection connection = null;
        String connectionURL = null;

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" + ip + ":1433/" + baseDeDatos + ";user=" + usuario + ";password=" + password + ";";
            connection = DriverManager.getConnection(connectionURL);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error de conexión: ", e.getMessage());
        }

        return connection;

    }


}
