package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDEmpleado;
import Modelo.clsEmpleado;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class clsEmpleadoDAO implements CRUDEmpleado {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsEmpleadoDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsEmpleado> listar() {
        List<clsEmpleado> listaEmpleados = new ArrayList<>();
        String sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo WHERE e.estado = 1 ORDER BY e.nombre, e.apellido";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                listaEmpleados.add(mapearEmpleado(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar empleados: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return listaEmpleados;
    }
    
    @Override
    public clsEmpleado listarPorId(int id) {
        clsEmpleado empleado = null;
        String sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo WHERE e.idempleado = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                empleado = mapearEmpleado(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar empleado por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return empleado;
    }
    
    @Override
    public clsEmpleado listarPorUsuario(String usuario) {
        clsEmpleado empleado = null;
        String sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo WHERE e.usuario = ? AND e.estado = 1";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                empleado = mapearEmpleado(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar empleado por usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return empleado;
    }
    
    @Override
    public clsEmpleado validarLogin(String usuario, String password) {
        clsEmpleado empleado = null;
        String sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo WHERE e.usuario = ? AND e.password = ? AND e.estado = 1";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, password);

            System.out.println("Ejecutando consulta: " + sql);
            System.out.println("Parámetros - Usuario: " + usuario + ", Password: " + password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                empleado = mapearEmpleado(resultSet);
                System.out.println("Empleado encontrado: " + empleado.getNombreCompleto());
            } else {
                System.out.println("No se encontró empleado con esas credenciales");
            }
        } catch (SQLException e) {
            System.out.println("Error al validar login: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return empleado;
    }
    
    @Override
    public boolean agregar(clsEmpleado empleado) {
        boolean exito = false;
        String sql = "INSERT INTO tbempleado (nombre, apellido, dni, telefono, email, direccion, fecha_contrato, salario, estado, idcargo, usuario, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, ?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, empleado.getNombre());
            preparedStatement.setString(2, empleado.getApellido());
            preparedStatement.setString(3, empleado.getDni());
            preparedStatement.setString(4, empleado.getTelefono());
            preparedStatement.setString(5, empleado.getEmail());
            preparedStatement.setString(6, empleado.getDireccion());
            preparedStatement.setDate(7, Date.valueOf(empleado.getFechaContrato()));
            preparedStatement.setBigDecimal(8, empleado.getSalario());
            preparedStatement.setInt(9, empleado.getIdcargo());
            preparedStatement.setString(10, empleado.getUsuario());
            preparedStatement.setString(11, empleado.getPassword());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int idGenerado = resultSet.getInt(1);
                    empleado.setIdempleado(idGenerado);
                    exito = true;
                    System.out.println("Empleado agregado correctamente con ID: " + idGenerado);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar empleado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizar(clsEmpleado empleado) {
        boolean exito = false;
        String sql = "UPDATE tbempleado SET nombre = ?, apellido = ?, dni = ?, telefono = ?, email = ?, direccion = ?, fecha_contrato = ?, salario = ?, idcargo = ?, usuario = ?, password = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idempleado = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, empleado.getNombre());
            preparedStatement.setString(2, empleado.getApellido());
            preparedStatement.setString(3, empleado.getDni());
            preparedStatement.setString(4, empleado.getTelefono());
            preparedStatement.setString(5, empleado.getEmail());
            preparedStatement.setString(6, empleado.getDireccion());
            preparedStatement.setDate(7, Date.valueOf(empleado.getFechaContrato()));
            preparedStatement.setBigDecimal(8, empleado.getSalario());
            preparedStatement.setInt(9, empleado.getIdcargo());
            preparedStatement.setString(10, empleado.getUsuario());
            preparedStatement.setString(11, empleado.getPassword());
            preparedStatement.setInt(12, empleado.getIdempleado());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
            
            if (exito) {
                System.out.println("Empleado actualizado correctamente: " + empleado.getIdempleado());
            } else {
                System.out.println("No se pudo actualizar el empleado: " + empleado.getIdempleado());
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar empleado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "UPDATE tbempleado SET estado = 0, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idempleado = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
            
            if (exito) {
                System.out.println("Empleado eliminado correctamente: " + id);
            } else {
                System.out.println("No se pudo eliminar el empleado: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar empleado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return exito;
    }
    
    // ========== MÉTODOS DE VALIDACIÓN ==========
    
    @Override
    public boolean existeDni(String dni) {
        String sql = "SELECT COUNT(*) FROM tbempleado WHERE dni = ? AND estado = 1";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dni);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar DNI: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }
    
    @Override
    public boolean existeDni(String dni, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM tbempleado WHERE dni = ? AND idempleado != ? AND estado = 1";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dni);
            preparedStatement.setInt(2, idExcluir);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar DNI (excluyendo): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }
    
    @Override
    public boolean existeUsuario(String usuario) {
        String sql = "SELECT COUNT(*) FROM tbempleado WHERE usuario = ? AND estado = 1";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }
    
    @Override
    public boolean existeUsuario(String usuario, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM tbempleado WHERE usuario = ? AND idempleado != ? AND estado = 1";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario);
            preparedStatement.setInt(2, idExcluir);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar usuario (excluyendo): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }
    
    // ========== MÉTODOS DE BÚSQUEDA Y FILTROS ==========
    
    @Override
    public List<clsEmpleado> buscar(String criterio, String valor) {
        List<clsEmpleado> listaEmpleados = new ArrayList<>();
        String sql = "";
        
        try {
            switch (criterio.toLowerCase()) {
                case "nombre":
                    sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo WHERE (e.nombre LIKE ? OR e.apellido LIKE ?) AND e.estado = 1 ORDER BY e.nombre, e.apellido";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, "%" + valor + "%");
                    preparedStatement.setString(2, "%" + valor + "%");
                    break;
                    
                case "dni":
                    sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo WHERE e.dni = ? AND e.estado = 1 ORDER BY e.nombre, e.apellido";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, valor);
                    break;
                    
                case "cargo":
                    sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo WHERE c.nombrecargo LIKE ? AND e.estado = 1 ORDER BY e.nombre, e.apellido";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, "%" + valor + "%");
                    break;
                    
                case "usuario":
                    sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo WHERE e.usuario LIKE ? AND e.estado = 1 ORDER BY e.nombre, e.apellido";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, "%" + valor + "%");
                    break;
                    
                default:
                    sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo WHERE (e.nombre LIKE ? OR e.apellido LIKE ? OR e.dni LIKE ? OR e.usuario LIKE ?) AND e.estado = 1 ORDER BY e.nombre, e.apellido";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, "%" + valor + "%");
                    preparedStatement.setString(2, "%" + valor + "%");
                    preparedStatement.setString(3, "%" + valor + "%");
                    preparedStatement.setString(4, "%" + valor + "%");
                    break;
            }
            
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                listaEmpleados.add(mapearEmpleado(resultSet));
            }
            
            System.out.println("Búsqueda completada: " + listaEmpleados.size() + " empleados encontrados");
        } catch (SQLException e) {
            System.out.println("Error al buscar empleados: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return listaEmpleados;
    }
    
    @Override
    public List<clsEmpleado> listarPorCargo(int idCargo) {
        List<clsEmpleado> listaEmpleados = new ArrayList<>();
        String sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo WHERE e.idcargo = ? AND e.estado = 1 ORDER BY e.nombre, e.apellido";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idCargo);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                listaEmpleados.add(mapearEmpleado(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar empleados por cargo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return listaEmpleados;
    }
    
    @Override
    public List<clsEmpleado> listarTodos() {
        List<clsEmpleado> listaEmpleados = new ArrayList<>();
        String sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo ORDER BY e.estado DESC, e.nombre, e.apellido";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                listaEmpleados.add(mapearEmpleado(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar todos los empleados: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return listaEmpleados;
    }
    
    // Método para listar empleados inactivos
    public List<clsEmpleado> listarInactivos() {
        List<clsEmpleado> listaEmpleados = new ArrayList<>();
        String sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo WHERE e.estado = 0 ORDER BY e.nombre, e.apellido";

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listaEmpleados.add(mapearEmpleado(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar empleados inactivos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return listaEmpleados;
    }

// Método para reactivar empleado
    public boolean reactivar(int id) {
        boolean exito = false;
        String sql = "UPDATE tbempleado SET estado = 1, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idempleado = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;

            if (exito) {
                System.out.println("Empleado reactivado: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error al reactivar empleado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return exito;
    }
    
    // ========== MÉTODOS DE ESTADÍSTICAS Y REPORTES ==========
    
    @Override
    public int obtenerTotalEmpleados() {
        String sql = "SELECT COUNT(*) FROM tbempleado WHERE estado = 1";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener total de empleados: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return 0;
    }
    
    @Override
    public boolean cambiarEstado(int id, String estado) {
        boolean exito = false;
        int estadoNum = "activo".equalsIgnoreCase(estado) ? 1 : 0;
        String sql = "UPDATE tbempleado SET estado = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idempleado = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, estadoNum);
            preparedStatement.setInt(2, id);
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
            
            if (exito) {
                System.out.println("Estado del empleado actualizado: " + id + " -> " + estado);
            }
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado del empleado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return exito;
    }
    
    // ========== MÉTODOS ADICIONALES PARA PERFIL ==========
    
    @Override
    public boolean actualizarPerfil(clsEmpleado empleado) {
        boolean exito = false;
        String sql = "UPDATE tbempleado SET nombre = ?, apellido = ?, telefono = ?, email = ?, direccion = ?, password = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idempleado = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, empleado.getNombre());
            preparedStatement.setString(2, empleado.getApellido());
            preparedStatement.setString(3, empleado.getTelefono());
            preparedStatement.setString(4, empleado.getEmail());
            preparedStatement.setString(5, empleado.getDireccion());
            preparedStatement.setString(6, empleado.getPassword());
            preparedStatement.setInt(7, empleado.getIdempleado());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
            
            if (exito) {
                System.out.println("Perfil actualizado correctamente: " + empleado.getIdempleado());
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar perfil: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return exito;
    }
    
    // ========== MÉTODOS AUXILIARES ==========
    
    /**
     * Método auxiliar para mapear un ResultSet a clsEmpleado
     */
    private clsEmpleado mapearEmpleado(ResultSet resultSet) throws SQLException {
        clsEmpleado empleado = new clsEmpleado();
        empleado.setIdempleado(resultSet.getInt("idempleado"));
        empleado.setNombre(resultSet.getString("nombre"));
        empleado.setApellido(resultSet.getString("apellido"));
        empleado.setDni(resultSet.getString("dni"));
        empleado.setTelefono(resultSet.getString("telefono"));
        empleado.setEmail(resultSet.getString("email"));
        empleado.setDireccion(resultSet.getString("direccion"));
        empleado.setSalario(resultSet.getBigDecimal("salario"));
        empleado.setEstado(resultSet.getInt("estado"));
        empleado.setIdcargo(resultSet.getInt("idcargo"));
        empleado.setNombrecargo(resultSet.getString("nombrecargo"));
        empleado.setUsuario(resultSet.getString("usuario"));
        empleado.setPassword(resultSet.getString("password"));
        
        // Mapear fechas
        Date fechaContrato = resultSet.getDate("fecha_contrato");
        if (fechaContrato != null) {
            empleado.setFechaContrato(fechaContrato.toLocalDate());
        }
        
        Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
        if (timestampCreacion != null) {
            empleado.setFechaCreacion(timestampCreacion.toLocalDateTime());
        }
        
        Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
        if (timestampActualizacion != null) {
            empleado.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
        }
        
        return empleado;
    }
    
    /**
     * Método para obtener empleados con salario mayor a un monto específico
     */
    public List<clsEmpleado> listarPorSalarioMayorA(BigDecimal salarioMinimo) {
        List<clsEmpleado> listaEmpleados = new ArrayList<>();
        String sql = "SELECT e.*, c.nombrecargo FROM tbempleado e INNER JOIN tbcargo c ON e.idcargo = c.idcargo WHERE e.salario > ? AND e.estado = 1 ORDER BY e.salario DESC";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBigDecimal(1, salarioMinimo);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                listaEmpleados.add(mapearEmpleado(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar empleados por salario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return listaEmpleados;
    }
    
    /**
     * Método para obtener el número de empleados por cargo
     */
    public int contarEmpleadosPorCargo(int idCargo) {
        String sql = "SELECT COUNT(*) FROM tbempleado WHERE idcargo = ? AND estado = 1";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idCargo);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar empleados por cargo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return 0;
    }
    
    /**
     * Método para verificar si un empleado puede ser eliminado (sin dependencias)
     */
    public boolean puedeEliminar(int idEmpleado) {
        // Aquí puedes agregar validaciones de integridad referencial
        // Por ejemplo, verificar si el empleado tiene ventas registradas, etc.
        return true; // Por ahora siempre retorna true
    }
    
    private void closeResources() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}