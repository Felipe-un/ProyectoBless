package com.poo.proyectobless;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// Imports para Realtime Database
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.poo.proyectobless.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    // Referencias para Realtime Database
    private FirebaseDatabase database;
    private DatabaseReference prendasRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Inicializar Realtime Database
        database = FirebaseDatabase.getInstance(); // Obtiene la instancia
        // Crea una referencia al nodo "prendas" en la raíz de la base de datos
        // Todos los datos de prendas se almacenarán bajo este nodo.
        prendasRef = database.getReference("prendas");

        binding.btnAgregarPrenda.setOnClickListener(v -> agregarOActualizarPrenda(false));
        binding.btnActualizarPrenda.setOnClickListener(v -> agregarOActualizarPrenda(true));
        binding.btnBuscarPrenda.setOnClickListener(v -> buscarPrenda());
        binding.btnEliminarPrenda.setOnClickListener(v -> eliminarPrenda());

        // cargarPrendas(); // Para el RecyclerView
    }

    private boolean validarCampos() {
        // (La lógica de validación puede permanecer igual)
        String codigo = binding.etCodigoPrenda.getText().toString().trim();
        String nombre = binding.etNombrePrenda.getText().toString().trim();
        String categoria = binding.etCategoriaPrenda.getText().toString().trim();
        String talla = binding.etTallaPrenda.getText().toString().trim();
        String color = binding.etColorPrenda.getText().toString().trim();
        String stockStr = binding.etStockPrenda.getText().toString().trim();
        String precioStr = binding.etPrecioVentaPrenda.getText().toString().trim();

        if (TextUtils.isEmpty(codigo)) {
            binding.etCodigoPrenda.setError("Código es requerido");
            return false;
        }
        if (TextUtils.isEmpty(nombre)) {
            binding.etNombrePrenda.setError("Nombre es requerido");
            return false;
        }
        if (TextUtils.isEmpty(categoria)) {
            binding.etCategoriaPrenda.setError("Categoría es requerida");
            return false;
        }
        if (TextUtils.isEmpty(talla)) {
            binding.etTallaPrenda.setError("Talla es requerida");
            return false;
        }
        if (TextUtils.isEmpty(color)) {
            binding.etColorPrenda.setError("Color es requerido");
            return false;
        }
        if (TextUtils.isEmpty(stockStr)) {
            binding.etStockPrenda.setError("Stock es requerido");
            return false;
        }
        if (TextUtils.isEmpty(precioStr)) {
            binding.etPrecioVentaPrenda.setError("Precio es requerido");
            return false;
        }
        // Limpiar errores si pasa la validación
        binding.etCodigoPrenda.setError(null);
        binding.etNombrePrenda.setError(null);
        return true;
    }

    private void agregarOActualizarPrenda(boolean esActualizacion) {
        if (!validarCampos()) {
            return;
        }

        String codigoId = binding.etCodigoPrenda.getText().toString().trim();
        String nombre = binding.etNombrePrenda.getText().toString().trim();
        String categoria = binding.etCategoriaPrenda.getText().toString().trim();
        String talla = binding.etTallaPrenda.getText().toString().trim();
        String color = binding.etColorPrenda.getText().toString().trim();

        int stock = 0;
        double precioVenta = 0.0;

        try {
            stock = Integer.parseInt(binding.etStockPrenda.getText().toString().trim());
            precioVenta = Double.parseDouble(binding.etPrecioVentaPrenda.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Stock y Precio deben ser números válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        Prenda prenda = new Prenda(codigoId, nombre, categoria, talla, color, stock, precioVenta);

        // En Realtime Database, usas child() para navegar a la ubicación deseada
        // y setValue() para guardar el objeto.
        // El `codigoId` será la clave del nodo bajo "prendas".
        prendasRef.child(codigoId).setValue(prenda)
                .addOnSuccessListener(aVoid -> {
                    String mensaje = esActualizacion ? "Prenda actualizada correctamente" : "Prenda agregada correctamente";
                    Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    // cargarPrendas();
                })
                .addOnFailureListener(e -> {
                    String mensajeError = esActualizacion ? "Error al actualizar prenda: " : "Error al agregar prenda: ";
                    Toast.makeText(MainActivity.this, mensajeError + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error al escribir en Realtime Database", e);
                });
    }

    private void buscarPrenda() {
        String codigoId = binding.etCodigoPrenda.getText().toString().trim();
        if (TextUtils.isEmpty(codigoId)) {
            binding.etCodigoPrenda.setError("Ingrese un código para buscar");
            Toast.makeText(this, "Ingrese un código para buscar", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.etCodigoPrenda.setError(null);

        prendasRef.child(codigoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Prenda prenda = dataSnapshot.getValue(Prenda.class); // Convierte el snapshot a tu objeto Prenda
                    if (prenda != null) {
                        // Asignamos el codigoId desde la clave del snapshot si no lo guardamos en el objeto
                        // if (prenda.getCodigoId() == null) {
                        //     prenda.setCodigoId(dataSnapshot.getKey());
                        // }

                        //binding.etCodigoPrenda.setText(prenda.getCodigoId()); // o dataSnapshot.getKey()
                        binding.etNombrePrenda.setText(prenda.getNombre());
                        binding.etCategoriaPrenda.setText(prenda.getCategoria());
                        binding.etTallaPrenda.setText(prenda.getTalla());
                        binding.etColorPrenda.setText(prenda.getColor());
                        binding.etStockPrenda.setText(String.valueOf(prenda.getStock()));
                        binding.etPrecioVentaPrenda.setText(String.valueOf(prenda.getPrecioVenta()));
                        Toast.makeText(MainActivity.this, "Prenda encontrada.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "No such document in Realtime Database");
                    Toast.makeText(MainActivity.this, "Prenda no encontrada con el código: " + codigoId, Toast.LENGTH_SHORT).show();
                    limpiarCamposExceptoCodigo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(MainActivity.this, "Error al buscar prenda: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarPrenda() {
        String codigoId = binding.etCodigoPrenda.getText().toString().trim();
        if (TextUtils.isEmpty(codigoId)) {
            binding.etCodigoPrenda.setError("Ingrese un código para eliminar");
            Toast.makeText(this, "Ingrese un código para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.etCodigoPrenda.setError(null);

        prendasRef.child(codigoId).removeValue() // removeValue() para eliminar un nodo
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this, "Prenda eliminada correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    // cargarPrendas();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Error al eliminar prenda: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error al eliminar de Realtime Database", e);
                });
    }

    private void limpiarCampos() {
        // (La lógica de limpiar campos puede permanecer igual)
        binding.etCodigoPrenda.setText("");
        binding.etNombrePrenda.setText("");
        binding.etCategoriaPrenda.setText("");
        binding.etTallaPrenda.setText("");
        binding.etColorPrenda.setText("");
        binding.etStockPrenda.setText("");
        binding.etPrecioVentaPrenda.setText("");
        binding.etCodigoPrenda.setError(null);

        // Limpiar errores también
        binding.etCodigoPrenda.requestFocus();
    }

    private void limpiarCamposExceptoCodigo() {
        // (La lógica puede permanecer igual)
        binding.etNombrePrenda.setText("");
        binding.etCategoriaPrenda.setText("");
        binding.etTallaPrenda.setText("");
        binding.etColorPrenda.setText("");
        binding.etStockPrenda.setText("");
        binding.etPrecioVentaPrenda.setText("");
    }


    // Método para cargar y mostrar prendas desde Realtime Database (para RecyclerView)
    /*
    private void cargarPrendas() {
        prendasRef.addValueEventListener(new ValueEventListener() { // Escucha cambios en tiempo real
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Limpiar lista anterior
                // prendasList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Prenda prenda = snapshot.getValue(Prenda.class);
                    if (prenda != null && snapshot.getKey() != null) {
                         prenda.setCodigoId(snapshot.getKey()); // Asigna la clave del nodo como ID
                         // prendasList.add(prenda);
                         Log.d(TAG, "Prenda cargada: " + prenda.getNombre() + " ID: " + prenda.getCodigoId());
                    }
                }
                // Notificar al adapter del RecyclerView
                // if (adapter != null) {
                //    adapter.notifyDataSetChanged();
                // }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPrendas:onCancelled", databaseError.toException());
                Toast.makeText(MainActivity.this, "Error al cargar prendas.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    */

}