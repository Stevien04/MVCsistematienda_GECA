package Modelo;

import java.time.LocalDateTime;

public class clsCategoria {
    private int idcategoria;
    private String nombrecategoria;
    private String descripcion;
    private int estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    public clsCategoria() {}
    
    public clsCategoria(int idcategoria, String nombrecategoria, String descripcion, 
                       int estado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idcategoria = idcategoria;
        this.nombrecategoria = nombrecategoria;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdcategoria() { return idcategoria; }
    public void setIdcategoria(int idcategoria) { this.idcategoria = idcategoria; }
    
    public String getNombrecategoria() { return nombrecategoria; }
    public void setNombrecategoria(String nombrecategoria) { this.nombrecategoria = nombrecategoria; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    @Override
    public String toString() { return nombrecategoria; }
}