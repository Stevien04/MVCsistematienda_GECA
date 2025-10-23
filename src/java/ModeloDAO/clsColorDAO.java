package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDColor;
import Modelo.clsColor;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class clsColorDAO implements CRUDColor {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsColorDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsColor> listar() {
        List<clsColor> listaColores = new ArrayList<>();
        String sql = "CALL usp_color_read()";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsColor color = new clsColor();
                color.setIdcolor(resultSet.getInt("idcolor"));
                color.setNombrecolor(resultSet.getString("nombrecolor"));
                color.setCodigoHex(resultSet.getString("codigo_hex"));
                color.setDescripcion(resultSet.getString("descripcion"));
                color.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    color.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    color.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaColores.add(color);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar colores: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaColores;
    }
    
    @Override
    public clsColor listarPorId(int id) {
        clsColor color = null;
        String sql = "SELECT * FROM tbcolor WHERE idcolor = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                color = new clsColor();
                color.setIdcolor(resultSet.getInt("idcolor"));
                color.setNombrecolor(resultSet.getString("nombrecolor"));
                color.setCodigoHex(resultSet.getString("codigo_hex"));
                color.setDescripcion(resultSet.getString("descripcion"));
                color.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    color.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    color.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar color por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return color;
    }
    
    @Override
    public boolean agregar(clsColor color) {
        boolean exito = false;
        String sql = "CALL usp_color_create(?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, color.getNombrecolor());
            preparedStatement.setString(2, color.getCodigoHex());
            preparedStatement.setString(3, color.getDescripcion());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idGenerado = resultSet.getInt("idcolor");
                color.setIdcolor(idGenerado);
                exito = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar color: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizar(clsColor color) {
        boolean exito = false;
        String sql = "CALL usp_color_update(?, ?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, color.getIdcolor());
            preparedStatement.setString(2, color.getNombrecolor());
            preparedStatement.setString(3, color.getCodigoHex());
            preparedStatement.setString(4, color.getDescripcion());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar color: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "CALL usp_color_delete(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar color: " + e.getMessage());
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