package Modelo;

import java.time.LocalDateTime;

public class clsCargo {
    private int idcargo;
    private String nombrecargo;
    private String descripcion;
    private int estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public clsCargo() {}
    
    public clsCargo(int idcargo, String nombrecargo, String descripcion, int estado, 
                   LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idcargo = idcargo;
        this.nombrecargo = nombrecargo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdcargo() {
        return idcargo;
    }
    
    public void setIdcargo(int idcargo) {
        this.idcargo = idcargo;
    }
    
    public String getNombrecargo() {
        return nombrecargo;
    }
    
    public void setNombrecargo(String nombrecargo) {
        this.nombrecargo = nombrecargo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public int getEstado() {
        return estado;
    }
    
    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    @Override
    public String toString() {
        return nombrecargo;
    }
}