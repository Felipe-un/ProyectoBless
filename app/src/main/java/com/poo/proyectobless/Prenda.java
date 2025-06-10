package com.poo.proyectobless;

import com.google.firebase.database.Exclude; // Importante para Realtime Database
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Prenda {
    // No es necesario almacenar el codigoId DENTRO del objeto si va a ser la clave del nodo
    //
    // private String codigoId;
    private String nombre;
    private String categoria;
    private String talla;
    private String color;
    private int stock;
    private double precioVenta;

    public Prenda() {
        // Constructor vacío requerido por Firebase
    }

    public Prenda(String codigoId, String nombre, String categoria, String talla, String color, int stock, double precioVenta) {
        // this.codigoId = codigoId;
        this.nombre = nombre;
        this.categoria = categoria;
        this.talla = talla;
        this.color = color;
        this.stock = stock;
        this.precioVenta = precioVenta;
    }

    // Getters y Setters
    //public String getCodigoId() { return codigoId; }
    //public void setCodigoId(String codigoId) { this.codigoId = codigoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    // ...otros getters y setters...

    public String getTalla() {
        return talla;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getColor() {
        return color;
    }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    // Si el codigoId es la clave del nodo en Realtime Database,
    // puedes excluirlo de ser guardado como un campo redundante dentro del objeto JSON.
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombre", nombre);
        result.put("categoria", categoria);
        result.put("talla", talla);
        result.put("color", color);
        result.put("stock", stock);
        result.put("precioVenta", precioVenta);
        // No incluimos codigoId aquí si es la clave del nodo.
        return result;
    }

}