package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDDetalle;
import Modelo.clsDetalle;
import java.sql.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class clsDetalleDAO implements CRUDDetalle {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsDetalleDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsDetalle> listarPorBoleta(int idBoleta) {
        List<clsDetalle> listaDetalles = new ArrayList<>();
        String sql = "SELECT d.*, p.nombreproducto, b.numero_boleta " +
                    "FROM tbdetalle d " +
                    "INNER JOIN tbproducto p ON d.idproducto = p.idproducto " +
                    "INNER JOIN tbboleta b ON d.idboleta = b.idboleta " +
                    "WHERE d.idboleta = ? AND d.estado = 1 " +
                    "ORDER BY d.iddetalle";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idBoleta);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsDetalle detalle = new clsDetalle();
                detalle.setIddetalle(resultSet.getInt("iddetalle"));
                detalle.setIdboleta(resultSet.getInt("idboleta"));
                detalle.setIdproducto(resultSet.getInt("idproducto"));
                detalle.setCantidad(resultSet.getInt("cantidad"));
                detalle.setPrecioUnitario(resultSet.getBigDecimal("precio_unitario"));
                detalle.setImporte(resultSet.getBigDecimal("importe"));
                detalle.setEstado(resultSet.getInt("estado"));
                detalle.setNombreproducto(resultSet.getString("nombreproducto"));
                detalle.setNumeroBoleta(resultSet.getString("numero_boleta"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    detalle.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                listaDetalles.add(detalle);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar detalles: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaDetalles;
    }
    
    @Override
    public clsDetalle listarPorId(int id) {
        clsDetalle detalle = null;
        String sql = "SELECT d.*, p.nombreproducto, b.numero_boleta " +
                    "FROM tbdetalle d " +
                    "INNER JOIN tbproducto p ON d.idproducto = p.idproducto " +
                    "INNER JOIN tbboleta b ON d.idboleta = b.idboleta " +
                    "WHERE d.iddetalle = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                detalle = new clsDetalle();
                detalle.setIddetalle(resultSet.getInt("iddetalle"));
                detalle.setIdboleta(resultSet.getInt("idboleta"));
                detalle.setIdproducto(resultSet.getInt("idproducto"));
                detalle.setCantidad(resultSet.getInt("cantidad"));
                detalle.setPrecioUnitario(resultSet.getBigDecimal("precio_unitario"));
                detalle.setImporte(resultSet.getBigDecimal("importe"));
                detalle.setEstado(resultSet.getInt("estado"));
                detalle.setNombreproducto(resultSet.getString("nombreproducto"));
                detalle.setNumeroBoleta(resultSet.getString("numero_boleta"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    detalle.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar detalle por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return detalle;
    }
    
    @Override
    public boolean agregar(clsDetalle detalle) {
        boolean exito = false;
        String sql = "INSERT INTO tbdetalle (idboleta, idproducto, cantidad, precio_unitario, importe) VALUES (?, ?, ?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, detalle.getIdboleta());
            preparedStatement.setInt(2, detalle.getIdproducto());
            preparedStatement.setInt(3, detalle.getCantidad());
            preparedStatement.setBigDecimal(4, detalle.getPrecioUnitario());
            preparedStatement.setBigDecimal(5, detalle.getImporte());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int idGenerado = resultSet.getInt(1);
                    detalle.setIddetalle(idGenerado);
                    exito = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar detalle: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizar(clsDetalle detalle) {
        boolean exito = false;
        String sql = "UPDATE tbdetalle SET idproducto = ?, cantidad = ?, precio_unitario = ?, importe = ? WHERE iddetalle = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, detalle.getIdproducto());
            preparedStatement.setInt(2, detalle.getCantidad());
            preparedStatement.setBigDecimal(3, detalle.getPrecioUnitario());
            preparedStatement.setBigDecimal(4, detalle.getImporte());
            preparedStatement.setInt(5, detalle.getIddetalle());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar detalle: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "UPDATE tbdetalle SET estado = 0 WHERE iddetalle = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar detalle: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminarPorBoleta(int idBoleta) {
        boolean exito = false;
        String sql = "UPDATE tbdetalle SET estado = 0 WHERE idboleta = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idBoleta);
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar detalles por boleta: " + e.getMessage());
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
}