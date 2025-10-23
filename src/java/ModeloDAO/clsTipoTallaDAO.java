package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDTipoTalla;
import Modelo.clsTipoTalla;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class clsTipoTallaDAO implements CRUDTipoTalla {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsTipoTallaDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsTipoTalla> listar() {
        List<clsTipoTalla> listaTipoTallas = new ArrayList<>();
        String sql = "CALL usp_tipotalla_read()";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsTipoTalla tipoTalla = new clsTipoTalla();
                tipoTalla.setIdtipotalla(resultSet.getInt("idtipotalla"));
                tipoTalla.setNombretipotalla(resultSet.getString("nombretipotalla"));
                tipoTalla.setDescripcion(resultSet.getString("descripcion"));
                tipoTalla.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    tipoTalla.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    tipoTalla.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaTipoTallas.add(tipoTalla);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar tipos de talla: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaTipoTallas;
    }
    
    @Override
    public clsTipoTalla listarPorId(int id) {
        clsTipoTalla tipoTalla = null;
        String sql = "SELECT * FROM tbtipotalla WHERE idtipotalla = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                tipoTalla = new clsTipoTalla();
                tipoTalla.setIdtipotalla(resultSet.getInt("idtipotalla"));
                tipoTalla.setNombretipotalla(resultSet.getString("nombretipotalla"));
                tipoTalla.setDescripcion(resultSet.getString("descripcion"));
                tipoTalla.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    tipoTalla.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    tipoTalla.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar tipo de talla por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return tipoTalla;
    }
    
    @Override
    public boolean agregar(clsTipoTalla tipoTalla) {
        boolean exito = false;
        String sql = "CALL usp_tipotalla_create(?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tipoTalla.getNombretipotalla());
            preparedStatement.setString(2, tipoTalla.getDescripcion());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idGenerado = resultSet.getInt("idtipotalla");
                tipoTalla.setIdtipotalla(idGenerado);
                exito = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar tipo de talla: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizar(clsTipoTalla tipoTalla) {
        boolean exito = false;
        String sql = "CALL usp_tipotalla_update(?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, tipoTalla.getIdtipotalla());
            preparedStatement.setString(2, tipoTalla.getNombretipotalla());
            preparedStatement.setString(3, tipoTalla.getDescripcion());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar tipo de talla: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "CALL usp_tipotalla_delete(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar tipo de talla: " + e.getMessage());
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