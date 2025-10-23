package Modelo;

import java.time.LocalDateTime;

public class clsMarca {
    private int idmarca;
    private String nombremarca;
    private String descripcion;
    private int estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    public clsMarca() {}
    
    public clsMarca(int idmarca, String nombremarca, String descripcion, 
                   int estado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idmarca = idmarca;
        this.nombremarca = nombremarca;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdmarca() { return idmarca; }
    public void setIdmarca(int idmarca) { this.idmarca = idmarca; }
    
    public String getNombremarca() { return nombremarca; }
    public void setNombremarca(String nombremarca) { this.nombremarca = nombremarca; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    @Override
    public String toString() { return nombremarca; }
}