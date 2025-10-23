package Modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class clsEmpleado {
    private int idempleado;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDate fechaContrato;
    private BigDecimal salario;
    private int estado;
    private int idcargo;
    private String usuario;
    private String password;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Campos adicionales para joins
    private String nombrecargo;
    
    public clsEmpleado() {}
    
    public clsEmpleado(int idempleado, String nombre, String apellido, String dni, 
                      String telefono, String email, String direccion, LocalDate fechaContrato,
                      BigDecimal salario, int estado, int idcargo, String usuario, String password,
                      LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idempleado = idempleado;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.fechaContrato = fechaContrato;
        this.salario = salario;
        this.estado = estado;
        this.idcargo = idcargo;
        this.usuario = usuario;
        this.password = password;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdempleado() { return idempleado; }
    public void setIdempleado(int idempleado) { this.idempleado = idempleado; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public LocalDate getFechaContrato() { return fechaContrato; }
    public void setFechaContrato(LocalDate fechaContrato) { this.fechaContrato = fechaContrato; }
    
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public int getIdcargo() { return idcargo; }
    public void setIdcargo(int idcargo) { this.idcargo = idcargo; }
    
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public String getNombrecargo() { return nombrecargo; }
    public void setNombrecargo(String nombrecargo) { this.nombrecargo = nombrecargo; }
    
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    @Override
    public String toString() {
        return getNombreCompleto();
    }
}