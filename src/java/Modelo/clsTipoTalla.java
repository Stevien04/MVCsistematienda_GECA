package Modelo;

import java.time.LocalDateTime;

public class clsTipoTalla {
    private int idtipotalla;
    private String nombretipotalla;
    private String descripcion;
    private int estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    public clsTipoTalla() {}
    
    public clsTipoTalla(int idtipotalla, String nombretipotalla, String descripcion, 
                       int estado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idtipotalla = idtipotalla;
        this.nombretipotalla = nombretipotalla;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdtipotalla() { return idtipotalla; }
    public void setIdtipotalla(int idtipotalla) { this.idtipotalla = idtipotalla; }
    
    public String getNombretipotalla() { return nombretipotalla; }
    public void setNombretipotalla(String nombretipotalla) { this.nombretipotalla = nombretipotalla; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    @Override
    public String toString() { return nombretipotalla; }
}