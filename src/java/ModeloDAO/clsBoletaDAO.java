package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDBoleta;
import Modelo.clsBoleta;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class clsBoletaDAO implements CRUDBoleta {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsBoletaDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsBoleta> listar() {
        List<clsBoleta> listaBoletas = new ArrayList<>();
        String sql = "SELECT b.*, c.nombre as nombreCliente, c.dni as dniCliente, e.nombre as nombreEmpleado " +
                    "FROM tbboleta b " +
                    "LEFT JOIN tbcliente c ON b.idcliente = c.idcliente " +
                    "LEFT JOIN tbempleado e ON b.idempleado = e.idempleado " +
                    "WHERE b.estado = 1 " +
                    "ORDER BY b.fecha_emision DESC, b.hora_emision DESC";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsBoleta boleta = new clsBoleta();
                boleta.setIdboleta(resultSet.getInt("idboleta"));
                boleta.setNumeroBoleta(resultSet.getString("numero_boleta"));
                boleta.setSubtotal(resultSet.getBigDecimal("subtotal"));
                boleta.setIgv(resultSet.getBigDecimal("igv"));
                boleta.setTotal(resultSet.getBigDecimal("total"));
                boleta.setEstadoBoleta(resultSet.getString("estado_boleta"));
                boleta.setIdcliente(resultSet.getInt("idcliente"));
                boleta.setIdempleado(resultSet.getInt("idempleado"));
                boleta.setEstado(resultSet.getInt("estado"));
                boleta.setNombreCliente(resultSet.getString("nombreCliente"));
                boleta.setDniCliente(resultSet.getString("dniCliente"));
                boleta.setNombreEmpleado(resultSet.getString("nombreEmpleado"));
                
                Date fechaEmision = resultSet.getDate("fecha_emision");
                if (fechaEmision != null) {
                    boleta.setFechaEmision(fechaEmision.toLocalDate());
                }
                
                Time horaEmision = resultSet.getTime("hora_emision");
                if (horaEmision != null) {
                    boleta.setHoraEmision(horaEmision.toLocalTime());
                }
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    boleta.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    boleta.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaBoletas.add(boleta);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar boletas: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaBoletas;
    }
    
    @Override
    public clsBoleta listarPorId(int id) {
        clsBoleta boleta = null;
        String sql = "SELECT b.*, c.nombre as nombreCliente, c.dni as dniCliente, e.nombre as nombreEmpleado " +
                    "FROM tbboleta b " +
                    "LEFT JOIN tbcliente c ON b.idcliente = c.idcliente " +
                    "LEFT JOIN tbempleado e ON b.idempleado = e.idempleado " +
                    "WHERE b.idboleta = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                boleta = new clsBoleta();
                boleta.setIdboleta(resultSet.getInt("idboleta"));
                boleta.setNumeroBoleta(resultSet.getString("numero_boleta"));
                boleta.setSubtotal(resultSet.getBigDecimal("subtotal"));
                boleta.setIgv(resultSet.getBigDecimal("igv"));
                boleta.setTotal(resultSet.getBigDecimal("total"));
                boleta.setEstadoBoleta(resultSet.getString("estado_boleta"));
                boleta.setIdcliente(resultSet.getInt("idcliente"));
                boleta.setIdempleado(resultSet.getInt("idempleado"));
                boleta.setEstado(resultSet.getInt("estado"));
                boleta.setNombreCliente(resultSet.getString("nombreCliente"));
                boleta.setDniCliente(resultSet.getString("dniCliente"));
                boleta.setNombreEmpleado(resultSet.getString("nombreEmpleado"));
                
                Date fechaEmision = resultSet.getDate("fecha_emision");
                if (fechaEmision != null) {
                    boleta.setFechaEmision(fechaEmision.toLocalDate());
                }
                
                Time horaEmision = resultSet.getTime("hora_emision");
                if (horaEmision != null) {
                    boleta.setHoraEmision(horaEmision.toLocalTime());
                }
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    boleta.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    boleta.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar boleta por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return boleta;
    }
    
    @Override
    public clsBoleta listarPorNumero(String numeroBoleta) {
        clsBoleta boleta = null;
        String sql = "SELECT b.*, c.nombre as nombreCliente, c.dni as dniCliente, e.nombre as nombreEmpleado " +
                    "FROM tbboleta b " +
                    "LEFT JOIN tbcliente c ON b.idcliente = c.idcliente " +
                    "LEFT JOIN tbempleado e ON b.idempleado = e.idempleado " +
                    "WHERE b.numero_boleta = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, numeroBoleta);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                boleta = new clsBoleta();
                boleta.setIdboleta(resultSet.getInt("idboleta"));
                boleta.setNumeroBoleta(resultSet.getString("numero_boleta"));
                boleta.setSubtotal(resultSet.getBigDecimal("subtotal"));
                boleta.setIgv(resultSet.getBigDecimal("igv"));
                boleta.setTotal(resultSet.getBigDecimal("total"));
                boleta.setEstadoBoleta(resultSet.getString("estado_boleta"));
                boleta.setIdcliente(resultSet.getInt("idcliente"));
                boleta.setIdempleado(resultSet.getInt("idempleado"));
                boleta.setEstado(resultSet.getInt("estado"));
                boleta.setNombreCliente(resultSet.getString("nombreCliente"));
                boleta.setDniCliente(resultSet.getString("dniCliente"));
                boleta.setNombreEmpleado(resultSet.getString("nombreEmpleado"));
                
                Date fechaEmision = resultSet.getDate("fecha_emision");
                if (fechaEmision != null) {
                    boleta.setFechaEmision(fechaEmision.toLocalDate());
                }
                
                Time horaEmision = resultSet.getTime("hora_emision");
                if (horaEmision != null) {
                    boleta.setHoraEmision(horaEmision.toLocalTime());
                }
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    boleta.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    boleta.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar boleta por número: " + e.getMessage());
        } finally {
            closeResources();
        }
        return boleta;
    }
    
    @Override
    public boolean agregar(clsBoleta boleta) {
        boolean exito = false;
        String sql = "INSERT INTO tbboleta (numero_boleta, fecha_emision, hora_emision, subtotal, igv, total, estado_boleta, idcliente, idempleado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, boleta.getNumeroBoleta());
            preparedStatement.setDate(2, Date.valueOf(boleta.getFechaEmision()));
            preparedStatement.setTime(3, Time.valueOf(boleta.getHoraEmision()));
            preparedStatement.setBigDecimal(4, boleta.getSubtotal());
            preparedStatement.setBigDecimal(5, boleta.getIgv());
            preparedStatement.setBigDecimal(6, boleta.getTotal());
            preparedStatement.setString(7, boleta.getEstadoBoleta());
            preparedStatement.setInt(8, boleta.getIdcliente());
            preparedStatement.setInt(9, boleta.getIdempleado());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int idGenerado = resultSet.getInt(1);
                    boleta.setIdboleta(idGenerado);
                    exito = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizar(clsBoleta boleta) {
        boolean exito = false;
        String sql = "UPDATE tbboleta SET numero_boleta = ?, fecha_emision = ?, hora_emision = ?, subtotal = ?, igv = ?, total = ?, estado_boleta = ?, idcliente = ?, idempleado = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idboleta = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, boleta.getNumeroBoleta());
            preparedStatement.setDate(2, Date.valueOf(boleta.getFechaEmision()));
            preparedStatement.setTime(3, Time.valueOf(boleta.getHoraEmision()));
            preparedStatement.setBigDecimal(4, boleta.getSubtotal());
            preparedStatement.setBigDecimal(5, boleta.getIgv());
            preparedStatement.setBigDecimal(6, boleta.getTotal());
            preparedStatement.setString(7, boleta.getEstadoBoleta());
            preparedStatement.setInt(8, boleta.getIdcliente());
            preparedStatement.setInt(9, boleta.getIdempleado());
            preparedStatement.setInt(10, boleta.getIdboleta());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean anular(int id) {
        boolean exito = false;
        String sql = "UPDATE tbboleta SET estado_boleta = 'ANULADA', fecha_actualizacion = CURRENT_TIMESTAMP WHERE idboleta = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al anular boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public String generarNumeroBoleta() {
        String numeroBoleta = "";
        String sql = "SELECT COUNT(*) as total FROM tbboleta WHERE DATE(fecha_creacion) = CURDATE()";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                int totalHoy = resultSet.getInt("total") + 1;
                String fechaActual = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
                numeroBoleta = "B" + fechaActual + String.format("%04d", totalHoy);
            }
        } catch (SQLException e) {
            System.out.println("Error al generar número de boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return numeroBoleta;
    }
    
    private void closeResources() {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}