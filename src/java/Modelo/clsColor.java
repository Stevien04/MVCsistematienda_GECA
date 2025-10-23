package Modelo;

import java.time.LocalDateTime;

public class clsColor {
    private int idcolor;
    private String nombrecolor;
    private String codigoHex;
    private String descripcion;
    private int estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    public clsColor() {}
    
    public clsColor(int idcolor, String nombrecolor, String codigoHex, String descripcion, 
                   int estado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idcolor = idcolor;
        this.nombrecolor = nombrecolor;
        this.codigoHex = codigoHex;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdcolor() { return idcolor; }
    public void setIdcolor(int idcolor) { this.idcolor = idcolor; }
    
    public String getNombrecolor() { return nombrecolor; }
    public void setNombrecolor(String nombrecolor) { this.nombrecolor = nombrecolor; }
    
    public String getCodigoHex() { return codigoHex; }
    public void setCodigoHex(String codigoHex) { this.codigoHex = codigoHex; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    @Override
    public String toString() { return nombrecolor; }
}