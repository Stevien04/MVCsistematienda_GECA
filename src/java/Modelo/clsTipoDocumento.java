package Modelo;

import java.time.LocalDateTime;

public class clsTipoDocumento {
    private int idtipodocumento;
    private String nombretipodocumento;
    private String abreviatura;
    private String descripcion;
    private int estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    public clsTipoDocumento() {}
    
    public clsTipoDocumento(int idtipodocumento, String nombretipodocumento, String abreviatura, 
                           String descripcion, int estado, LocalDateTime fechaCreacion, 
                           LocalDateTime fechaActualizacion) {
        this.idtipodocumento = idtipodocumento;
        this.nombretipodocumento = nombretipodocumento;
        this.abreviatura = abreviatura;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdtipodocumento() { return idtipodocumento; }
    public void setIdtipodocumento(int idtipodocumento) { this.idtipodocumento = idtipodocumento; }
    
    public String getNombretipodocumento() { return nombretipodocumento; }
    public void setNombretipodocumento(String nombretipodocumento) { this.nombretipodocumento = nombretipodocumento; }
    
    public String getAbreviatura() { return abreviatura; }
    public void setAbreviatura(String abreviatura) { this.abreviatura = abreviatura; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    @Override
    public String toString() { 
        return nombretipodocumento + " (" + abreviatura + ")";
    }
}