package Modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class clsProducto {
    private int idproducto;
    private int idcategoria;
    private int idmodelo;
    private int idcolor;
    private String nombreproducto;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private LocalDate fechaCreacion;
    private int estado;
    private LocalDateTime fechaActualizacion;
    
    // Campos adicionales para joins
    private String nombrecategoria;
    private String nombremodelo;
    private String nombremarca;
    private String nombrecolor;
    private String codigoHex;
    
    public clsProducto() {}
    
    public clsProducto(int idproducto, int idcategoria, int idmodelo, int idcolor, 
                      String nombreproducto, String descripcion, BigDecimal precio, 
                      int stock, LocalDate fechaCreacion, int estado, LocalDateTime fechaActualizacion) {
        this.idproducto = idproducto;
        this.idcategoria = idcategoria;
        this.idmodelo = idmodelo;
        this.idcolor = idcolor;
        this.nombreproducto = nombreproducto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdproducto() { return idproducto; }
    public void setIdproducto(int idproducto) { this.idproducto = idproducto; }
    
    public int getIdcategoria() { return idcategoria; }
    public void setIdcategoria(int idcategoria) { this.idcategoria = idcategoria; }
    
    public int getIdmodelo() { return idmodelo; }
    public void setIdmodelo(int idmodelo) { this.idmodelo = idmodelo; }
    
    public int getIdcolor() { return idcolor; }
    public void setIdcolor(int idcolor) { this.idcolor = idcolor; }
    
    public String getNombreproducto() { return nombreproducto; }
    public void setNombreproducto(String nombreproducto) { this.nombreproducto = nombreproducto; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public String getNombrecategoria() { return nombrecategoria; }
    public void setNombrecategoria(String nombrecategoria) { this.nombrecategoria = nombrecategoria; }
    
    public String getNombremodelo() { return nombremodelo; }
    public void setNombremodelo(String nombremodelo) { this.nombremodelo = nombremodelo; }
    
    public String getNombremarca() { return nombremarca; }
    public void setNombremarca(String nombremarca) { this.nombremarca = nombremarca; }
    
    public String getNombrecolor() { return nombrecolor; }
    public void setNombrecolor(String nombrecolor) { this.nombrecolor = nombrecolor; }
    
    public String getCodigoHex() { return codigoHex; }
    public void setCodigoHex(String codigoHex) { this.codigoHex = codigoHex; }
    
    @Override
    public String toString() { return nombreproducto; }
}