package Modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class clsBoleta {
    private int idboleta;
    private String numeroBoleta;
    private LocalDate fechaEmision;
    private LocalTime horaEmision;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
    private String estadoBoleta;
    private int idcliente;
    private int idempleado;
    private int estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Campos adicionales para joins
    private String nombreCliente;
    private String dniCliente;
    private String nombreEmpleado;
    
    public clsBoleta() {}
    
    public clsBoleta(int idboleta, String numeroBoleta, LocalDate fechaEmision, LocalTime horaEmision,
                    BigDecimal subtotal, BigDecimal igv, BigDecimal total, String estadoBoleta,
                    int idcliente, int idempleado, int estado, LocalDateTime fechaCreacion, 
                    LocalDateTime fechaActualizacion) {
        this.idboleta = idboleta;
        this.numeroBoleta = numeroBoleta;
        this.fechaEmision = fechaEmision;
        this.horaEmision = horaEmision;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.estadoBoleta = estadoBoleta;
        this.idcliente = idcliente;
        this.idempleado = idempleado;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdboleta() { return idboleta; }
    public void setIdboleta(int idboleta) { this.idboleta = idboleta; }
    
    public String getNumeroBoleta() { return numeroBoleta; }
    public void setNumeroBoleta(String numeroBoleta) { this.numeroBoleta = numeroBoleta; }
    
    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }
    
    public LocalTime getHoraEmision() { return horaEmision; }
    public void setHoraEmision(LocalTime horaEmision) { this.horaEmision = horaEmision; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    public BigDecimal getIgv() { return igv; }
    public void setIgv(BigDecimal igv) { this.igv = igv; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public String getEstadoBoleta() { return estadoBoleta; }
    public void setEstadoBoleta(String estadoBoleta) { this.estadoBoleta = estadoBoleta; }
    
    public int getIdcliente() { return idcliente; }
    public void setIdcliente(int idcliente) { this.idcliente = idcliente; }
    
    public int getIdempleado() { return idempleado; }
    public void setIdempleado(int idempleado) { this.idempleado = idempleado; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    
    public String getDniCliente() { return dniCliente; }
    public void setDniCliente(String dniCliente) { this.dniCliente = dniCliente; }
    
    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }
}