package Modelo;

import java.time.LocalDateTime;

public class clsModelo {
    private int idmodelo;
    private int idmarca;
    private String nombremodelo;
    private String descripcion;
    private int estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Campos adicionales para joins
    private String nombremarca;
    
    public clsModelo() {}
    
    public clsModelo(int idmodelo, int idmarca, String nombremodelo, String descripcion, 
                    int estado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idmodelo = idmodelo;
        this.idmarca = idmarca;
        this.nombremodelo = nombremodelo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdmodelo() { return idmodelo; }
    public void setIdmodelo(int idmodelo) { this.idmodelo = idmodelo; }
    
    public int getIdmarca() { return idmarca; }
    public void setIdmarca(int idmarca) { this.idmarca = idmarca; }
    
    public String getNombremodelo() { return nombremodelo; }
    public void setNombremodelo(String nombremodelo) { this.nombremodelo = nombremodelo; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public String getNombremarca() { return nombremarca; }
    public void setNombremarca(String nombremarca) { this.nombremarca = nombremarca; }
    
    @Override
    public String toString() { return nombremodelo; }
}