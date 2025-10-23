package Modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class clsDetalle {
    private int iddetalle;
    private int idboleta;
    private int idproducto;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal importe;
    private int estado;
    private LocalDateTime fechaCreacion;
    
    // Campos adicionales para joins
    private String nombreproducto;
    private String numeroBoleta;
    
    public clsDetalle() {}
    
    public clsDetalle(int iddetalle, int idboleta, int idproducto, int cantidad, 
                     BigDecimal precioUnitario, BigDecimal importe, int estado, 
                     LocalDateTime fechaCreacion) {
        this.iddetalle = iddetalle;
        this.idboleta = idboleta;
        this.idproducto = idproducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.importe = importe;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }
    
    // Getters y Setters
    public int getIddetalle() { return iddetalle; }
    public void setIddetalle(int iddetalle) { this.iddetalle = iddetalle; }
    
    public int getIdboleta() { return idboleta; }
    public void setIdboleta(int idboleta) { this.idboleta = idboleta; }
    
    public int getIdproducto() { return idproducto; }
    public void setIdproducto(int idproducto) { this.idproducto = idproducto; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    
    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public String getNombreproducto() { return nombreproducto; }
    public void setNombreproducto(String nombreproducto) { this.nombreproducto = nombreproducto; }
    
    public String getNumeroBoleta() { return numeroBoleta; }
    public void setNumeroBoleta(String numeroBoleta) { this.numeroBoleta = numeroBoleta; }
}