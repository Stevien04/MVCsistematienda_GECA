package Modelo;

import java.time.LocalDateTime;

public class clsProductoTalla {
    private int idproductoTalla;
    private int idproducto;
    private int idtalla;
    private int stock;
    private int estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Campos adicionales para joins
    private String nombreproducto;
    private String nombretalla;
    private String nombretipotalla;
    
    public clsProductoTalla() {}
    
    public clsProductoTalla(int idproductoTalla, int idproducto, int idtalla, int stock, 
                           int estado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idproductoTalla = idproductoTalla;
        this.idproducto = idproducto;
        this.idtalla = idtalla;
        this.stock = stock;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdproductoTalla() { return idproductoTalla; }
    public void setIdproductoTalla(int idproductoTalla) { this.idproductoTalla = idproductoTalla; }
    
    public int getIdproducto() { return idproducto; }
    public void setIdproducto(int idproducto) { this.idproducto = idproducto; }
    
    public int getIdtalla() { return idtalla; }
    public void setIdtalla(int idtalla) { this.idtalla = idtalla; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public String getNombreproducto() { return nombreproducto; }
    public void setNombreproducto(String nombreproducto) { this.nombreproducto = nombreproducto; }
    
    public String getNombretalla() { return nombretalla; }
    public void setNombretalla(String nombretalla) { this.nombretalla = nombretalla; }
    
    public String getNombretipotalla() { return nombretipotalla; }
    public void setNombretipotalla(String nombretipotalla) { this.nombretipotalla = nombretipotalla; }
}