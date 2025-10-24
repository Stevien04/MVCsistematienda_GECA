package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDCliente;
import Modelo.clsCliente;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class clsClienteDAO implements CRUDCliente {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsClienteDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsCliente> listar() {
        List<clsCliente> listaClientes = new ArrayList<>();
        String sql = "SELECT * FROM tbcliente WHERE estado = 1 ORDER BY nombre, apellido";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsCliente cliente = new clsCliente();
                cliente.setIdcliente(resultSet.getInt("idcliente"));
                cliente.setNombre(resultSet.getString("nombre"));
                cliente.setApellido(resultSet.getString("apellido"));
                cliente.setDni(resultSet.getString("dni"));
                cliente.setTelefono(resultSet.getString("telefono"));
                cliente.setEmail(resultSet.getString("email"));
                cliente.setDireccion(resultSet.getString("direccion"));
                cliente.setEstado(resultSet.getInt("estado"));
                
                Date fechaRegistro = resultSet.getDate("fecha_registro");
                if (fechaRegistro != null) {
                    cliente.setFechaRegistro(fechaRegistro.toLocalDate());
                }
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    cliente.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    cliente.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaClientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaClientes;
    }
    
    @Override
    public clsCliente listarPorId(int id) {
        clsCliente cliente = null;
        String sql = "SELECT * FROM tbcliente WHERE idcliente = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                cliente = new clsCliente();
                cliente.setIdcliente(resultSet.getInt("idcliente"));
                cliente.setNombre(resultSet.getString("nombre"));
                cliente.setApellido(resultSet.getString("apellido"));
                cliente.setDni(resultSet.getString("dni"));
                cliente.setTelefono(resultSet.getString("telefono"));
                cliente.setEmail(resultSet.getString("email"));
                cliente.setDireccion(resultSet.getString("direccion"));
                cliente.setEstado(resultSet.getInt("estado"));
                
                Date fechaRegistro = resultSet.getDate("fecha_registro");
                if (fechaRegistro != null) {
                    cliente.setFechaRegistro(fechaRegistro.toLocalDate());
                }
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    cliente.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    cliente.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return cliente;
    }
    
    @Override
    public clsCliente listarPorDni(String dni) {
        clsCliente cliente = null;
        String sql = "SELECT * FROM tbcliente WHERE dni = ? AND estado = 1";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dni);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                cliente = new clsCliente();
                cliente.setIdcliente(resultSet.getInt("idcliente"));
                cliente.setNombre(resultSet.getString("nombre"));
                cliente.setApellido(resultSet.getString("apellido"));
                cliente.setDni(resultSet.getString("dni"));
                cliente.setTelefono(resultSet.getString("telefono"));
                cliente.setEmail(resultSet.getString("email"));
                cliente.setDireccion(resultSet.getString("direccion"));
                cliente.setEstado(resultSet.getInt("estado"));
                
                Date fechaRegistro = resultSet.getDate("fecha_registro");
                if (fechaRegistro != null) {
                    cliente.setFechaRegistro(fechaRegistro.toLocalDate());
                }
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    cliente.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    cliente.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente por DNI: " + e.getMessage());
        } finally {
            closeResources();
        }
        return cliente;
    }
    
    @Override
    public boolean agregar(clsCliente cliente) {
        boolean exito = false;
        String sql = "INSERT INTO tbcliente (nombre, apellido, dni, telefono, email, direccion, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, cliente.getNombre());
            preparedStatement.setString(2, cliente.getApellido());
            preparedStatement.setString(3, cliente.getDni());
            preparedStatement.setString(4, cliente.getTelefono());
            preparedStatement.setString(5, cliente.getEmail());
            preparedStatement.setString(6, cliente.getDireccion());
            preparedStatement.setDate(7, Date.valueOf(cliente.getFechaRegistro() != null ? cliente.getFechaRegistro() : LocalDate.now()));
            
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int idGenerado = resultSet.getInt(1);
                    cliente.setIdcliente(idGenerado);
                    exito = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar cliente: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizar(clsCliente cliente) {
        boolean exito = false;
        String sql = "UPDATE tbcliente SET nombre = ?, apellido = ?, dni = ?, telefono = ?, email = ?, direccion = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idcliente = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, cliente.getNombre());
            preparedStatement.setString(2, cliente.getApellido());
            preparedStatement.setString(3, cliente.getDni());
            preparedStatement.setString(4, cliente.getTelefono());
            preparedStatement.setString(5, cliente.getEmail());
            preparedStatement.setString(6, cliente.getDireccion());
            preparedStatement.setInt(7, cliente.getIdcliente());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "UPDATE tbcliente SET estado = 0, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idcliente = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    private void closeResources() {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
    
    // Método para listar clientes inactivos
    public List<clsCliente> listarInactivos() {
        List<clsCliente> listaClientes = new ArrayList<>();
        String sql = "SELECT * FROM tbcliente WHERE estado = 0 ORDER BY nombre, apellido";

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listaClientes.add(mapearCliente(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar clientes inactivos: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaClientes;
    }

// Método para reactivar cliente
    public boolean reactivar(int id) {
        boolean exito = false;
        String sql = "UPDATE tbcliente SET estado = 1, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idcliente = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al reactivar cliente: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
   public clsCliente login(String usuario, String password) {
    clsCliente cliente = null;
    String sql = "SELECT * FROM tbcliente " +
                 "WHERE (email = ? OR dni = ?) AND password = ? AND estado = 1";

    try {
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, usuario); // puede ser email o DNI
        preparedStatement.setString(2, usuario);
        preparedStatement.setString(3, password);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            cliente = new clsCliente();
            cliente.setIdcliente(resultSet.getInt("idcliente"));
            cliente.setNombre(resultSet.getString("nombre"));
            cliente.setApellido(resultSet.getString("apellido"));
            cliente.setDni(resultSet.getString("dni"));
            cliente.setTelefono(resultSet.getString("telefono"));
            cliente.setEmail(resultSet.getString("email"));
            cliente.setDireccion(resultSet.getString("direccion"));
            cliente.setPassword(resultSet.getString("password"));
            cliente.setEstado(resultSet.getInt("estado"));
            cliente.setFechaRegistro(resultSet.getDate("fecha_registro").toLocalDate());
        }
    } catch (SQLException e) {
        System.out.println("Error en login de cliente: " + e.getMessage());
    } finally {
        closeResources();
    }
    return cliente;
}


// Método auxiliar mejorado para mapear cliente con tipo de documento
    private clsCliente mapearClienteCompleto(ResultSet resultSet) throws SQLException {
        clsCliente cliente = new clsCliente();
        cliente.setIdcliente(resultSet.getInt("idcliente"));
        cliente.setIdtipodocumento(resultSet.getInt("idtipodocumento"));
        cliente.setNombretipodocumento(resultSet.getString("nombretipodocumento"));
        cliente.setAbreviatura(resultSet.getString("abreviatura"));
        cliente.setNumero_documento(resultSet.getString("numero_documento"));
        cliente.setNombre(resultSet.getString("nombre"));
        cliente.setApellido(resultSet.getString("apellido"));
        cliente.setTelefono(resultSet.getString("telefono"));
        cliente.setEmail(resultSet.getString("email"));
        cliente.setDireccion(resultSet.getString("direccion"));
        cliente.setUsuario(resultSet.getString("usuario"));
        cliente.setPassword(resultSet.getString("password"));
        cliente.setEstado(resultSet.getInt("estado"));

        Date fechaRegistro = resultSet.getDate("fecha_registro");
        if (fechaRegistro != null) {
            cliente.setFechaRegistro(fechaRegistro.toLocalDate());
        }

        Date fechaNacimiento = resultSet.getDate("fecha_nacimiento");
        if (fechaNacimiento != null) {
            cliente.setFechaNacimiento(fechaNacimiento.toLocalDate());
        }

        Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
        if (timestampCreacion != null) {
            cliente.setFechaCreacion(timestampCreacion.toLocalDateTime());
        }

        Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
        if (timestampActualizacion != null) {
            cliente.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
        }

        return cliente;
    }
    
    // Método para agregar cliente con todos los campos
    public boolean agregarClienteCompleto(clsCliente cliente) {
        boolean exito = false;
        String sql = "INSERT INTO tbcliente (idtipodocumento, numero_documento, nombre, apellido, "
                + "telefono, email, direccion, fecha_registro, usuario, password, fecha_nacimiento) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, cliente.getIdtipodocumento());
            preparedStatement.setString(2, cliente.getNumero_documento());
            preparedStatement.setString(3, cliente.getNombre());
            preparedStatement.setString(4, cliente.getApellido());
            preparedStatement.setString(5, cliente.getTelefono());
            preparedStatement.setString(6, cliente.getEmail());
            preparedStatement.setString(7, cliente.getDireccion());
            preparedStatement.setDate(8, Date.valueOf(cliente.getFechaRegistro()));
            preparedStatement.setString(9, cliente.getUsuario());
            preparedStatement.setString(10, cliente.getPassword());

            if (cliente.getFechaNacimiento() != null) {
                preparedStatement.setDate(11, Date.valueOf(cliente.getFechaNacimiento()));
            } else {
                preparedStatement.setNull(11, Types.DATE);
            }

            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int idGenerado = resultSet.getInt(1);
                    cliente.setIdcliente(idGenerado);
                    exito = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar cliente completo: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }

// Verificar si usuario existe
    public boolean existeUsuario(String usuario) {
        boolean existe = false;
        String sql = "SELECT COUNT(*) as count FROM tbcliente WHERE usuario = ? AND estado = 1";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                existe = resultSet.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
        } finally {
            closeResources();
        }
        return existe;
    }

// Verificar si número de documento existe
    public boolean existeNumeroDocumento(String numeroDocumento) {
        boolean existe = false;
        String sql = "SELECT COUNT(*) as count FROM tbcliente WHERE numero_documento = ? AND estado = 1";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, numeroDocumento);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                existe = resultSet.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar número de documento: " + e.getMessage());
        } finally {
            closeResources();
        }
        return existe;
    }

// Método auxiliar para mapear cliente (para reutilizar código)
    private clsCliente mapearCliente(ResultSet resultSet) throws SQLException {
        clsCliente cliente = new clsCliente();
        cliente.setIdcliente(resultSet.getInt("idcliente"));
        cliente.setNombre(resultSet.getString("nombre"));
        cliente.setApellido(resultSet.getString("apellido"));
        cliente.setDni(resultSet.getString("dni"));
        cliente.setTelefono(resultSet.getString("telefono"));
        cliente.setEmail(resultSet.getString("email"));
        cliente.setDireccion(resultSet.getString("direccion"));
        cliente.setEstado(resultSet.getInt("estado"));

        Date fechaRegistro = resultSet.getDate("fecha_registro");
        if (fechaRegistro != null) {
            cliente.setFechaRegistro(fechaRegistro.toLocalDate());
        }

        Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
        if (timestampCreacion != null) {
            cliente.setFechaCreacion(timestampCreacion.toLocalDateTime());
        }

        Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
        if (timestampActualizacion != null) {
            cliente.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
        }

        return cliente;
    }
}