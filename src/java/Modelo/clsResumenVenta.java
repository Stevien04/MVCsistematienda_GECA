package Modelo;

public class clsResumenVenta {
    private String nombre;
    private int total;

    public clsResumenVenta() {
    }

    public clsResumenVenta(String nombre, int total) {
        this.nombre = nombre;
        this.total = total;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }}