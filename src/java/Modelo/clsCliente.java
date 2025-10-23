package Modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class clsCliente {
    private int idcliente;
    private int idtipodocumento;
    private String nombretipodocumento;
    private String abreviatura;
    private String numero_documento;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDate fechaRegistro;
    private String usuario;
    private String password;
    private LocalDate fechaNacimiento;
    private int estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    public clsCliente() {}
    
    // Constructor completo
    public clsCliente(int idcliente, int idtipodocumento, String nombretipodocumento, 
                     String abreviatura, String numero_documento, String nombre, 
                     String apellido, String telefono, String email, String direccion, 
                     LocalDate fechaRegistro, String usuario, String password, 
                     LocalDate fechaNacimiento, int estado, LocalDateTime fechaCreacion, 
                     LocalDateTime fechaActualizacion) {
        this.idcliente = idcliente;
        this.idtipodocumento = idtipodocumento;
        this.nombretipodocumento = nombretipodocumento;
        this.abreviatura = abreviatura;
        this.numero_documento = numero_documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.usuario = usuario;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public int getIdcliente() { return idcliente; }
    public void setIdcliente(int idcliente) { this.idcliente = idcliente; }
    
    public int getIdtipodocumento() { return idtipodocumento; }
    public void setIdtipodocumento(int idtipodocumento) { this.idtipodocumento = idtipodocumento; }
    
    public String getNombretipodocumento() { return nombretipodocumento; }
    public void setNombretipodocumento(String nombretipodocumento) { this.nombretipodocumento = nombretipodocumento; }
    
    public String getAbreviatura() { return abreviatura; }
    public void setAbreviatura(String abreviatura) { this.abreviatura = abreviatura; }
    
    public String getNumero_documento() { return numero_documento; }
    public void setNumero_documento(String numero_documento) { this.numero_documento = numero_documento; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    // Métodos auxiliares
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    public String getDocumentoCompleto() {
        return (abreviatura != null ? abreviatura : "DOC") + ": " + numero_documento;
    }
    
    // Para compatibilidad con código existente
    public String getDni() {
        return numero_documento; // Mantener compatibilidad
    }
    
    public void setDni(String dni) {
        this.numero_documento = dni; // Mantener compatibilidad
        this.idtipodocumento = 1; // Asumir DNI por defecto
    }
    
    @Override
    public String toString() {
        return getNombreCompleto();
    }
}