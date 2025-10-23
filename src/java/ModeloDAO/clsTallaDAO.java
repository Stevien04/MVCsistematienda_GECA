package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDTalla;
import Modelo.clsTalla;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class clsTallaDAO implements CRUDTalla {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsTallaDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsTalla> listar() {
        List<clsTalla> listaTallas = new ArrayList<>();
        String sql = "CALL usp_talla_read()";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsTalla talla = new clsTalla();
                talla.setIdtalla(resultSet.getInt("idtalla"));
                talla.setIdtipotalla(resultSet.getInt("idtipotalla"));
                talla.setNombretipotalla(resultSet.getString("nombretipotalla"));
                talla.setNombretalla(resultSet.getString("nombretalla"));
                talla.setDescripcion(resultSet.getString("descripcion"));
                talla.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    talla.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    talla.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaTallas.add(talla);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar tallas: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaTallas;
    }
    
    @Override
    public clsTalla listarPorId(int id) {
        clsTalla talla = null;
        String sql = "SELECT * FROM tbtalla WHERE idtalla = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                talla = new clsTalla();
                talla.setIdtalla(resultSet.getInt("idtalla"));
                talla.setIdtipotalla(resultSet.getInt("idtipotalla"));
                talla.setNombretalla(resultSet.getString("nombretalla"));
                talla.setDescripcion(resultSet.getString("descripcion"));
                talla.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    talla.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    talla.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar talla por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return talla;
    }
    
    @Override
    public List<clsTalla> listarPorTipoTalla(int idTipoTalla) {
        List<clsTalla> listaTallas = new ArrayList<>();
        String sql = "CALL usp_talla_read_by_tipo(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idTipoTalla);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsTalla talla = new clsTalla();
                talla.setIdtalla(resultSet.getInt("idtalla"));
                talla.setIdtipotalla(resultSet.getInt("idtipotalla"));
                talla.setNombretipotalla(resultSet.getString("nombretipotalla"));
                talla.setNombretalla(resultSet.getString("nombretalla"));
                talla.setDescripcion(resultSet.getString("descripcion"));
                talla.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    talla.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    talla.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaTallas.add(talla);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar tallas por tipo: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaTallas;
    }
    
    @Override
    public boolean agregar(clsTalla talla) {
        boolean exito = false;
        String sql = "CALL usp_talla_create(?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, talla.getIdtipotalla());
            preparedStatement.setString(2, talla.getNombretalla());
            preparedStatement.setString(3, talla.getDescripcion());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idGenerado = resultSet.getInt("idtalla");
                talla.setIdtalla(idGenerado);
                exito = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar talla: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizar(clsTalla talla) {
        boolean exito = false;
        String sql = "CALL usp_talla_update(?, ?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, talla.getIdtalla());
            preparedStatement.setInt(2, talla.getIdtipotalla());
            preparedStatement.setString(3, talla.getNombretalla());
            preparedStatement.setString(4, talla.getDescripcion());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar talla: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "CALL usp_talla_delete(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar talla: " + e.getMessage());
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