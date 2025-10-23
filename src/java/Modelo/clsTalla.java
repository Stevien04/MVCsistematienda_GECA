package Modelo;

import java.time.LocalDateTime;

public class clsTalla {
    private int idtalla;
    private int idtipotalla;
    private String nombretalla;
    private String descripcion;
    private int estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Campos adicionales para joins
    private String nombretipotalla;
    
    public clsTalla() {}
    
    public clsTalla(int idtalla, int idtipotalla, String nombretalla, String descripcion, 
                   int estado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idtalla = idtalla;
        this.idtipotalla = idtipotalla;
        this.nombretalla = nombretalla;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdtalla() { return idtalla; }
    public void setIdtalla(int idtalla) { this.idtalla = idtalla; }
    
    public int getIdtipotalla() { return idtipotalla; }
    public void setIdtipotalla(int idtipotalla) { this.idtipotalla = idtipotalla; }
    
    public String getNombretalla() { return nombretalla; }
    public void setNombretalla(String nombretalla) { this.nombretalla = nombretalla; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public String getNombretipotalla() { return nombretipotalla; }
    public void setNombretipotalla(String nombretipotalla) { this.nombretipotalla = nombretipotalla; }
    
    @Override
    public String toString() { return nombretalla; }
}