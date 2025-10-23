package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDModelo;
import Modelo.clsModelo;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class clsModeloDAO implements CRUDModelo {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsModeloDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsModelo> listar() {
        List<clsModelo> listaModelos = new ArrayList<>();
        String sql = "CALL usp_modelo_read()";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsModelo modelo = new clsModelo();
                modelo.setIdmodelo(resultSet.getInt("idmodelo"));
                modelo.setIdmarca(resultSet.getInt("idmarca"));
                modelo.setNombremarca(resultSet.getString("nombremarca"));
                modelo.setNombremodelo(resultSet.getString("nombremodelo"));
                modelo.setDescripcion(resultSet.getString("descripcion"));
                modelo.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    modelo.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    modelo.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaModelos.add(modelo);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar modelos: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaModelos;
    }
    
    @Override
    public clsModelo listarPorId(int id) {
        clsModelo modelo = null;
        String sql = "CALL usp_modelo_read_by_id(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                modelo = new clsModelo();
                modelo.setIdmodelo(resultSet.getInt("idmodelo"));
                modelo.setIdmarca(resultSet.getInt("idmarca"));
                modelo.setNombremarca(resultSet.getString("nombremarca"));
                modelo.setNombremodelo(resultSet.getString("nombremodelo"));
                modelo.setDescripcion(resultSet.getString("descripcion"));
                modelo.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    modelo.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    modelo.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar modelo por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return modelo;
    }
    
    @Override
    public List<clsModelo> listarPorMarca(int idMarca) {
        List<clsModelo> listaModelos = new ArrayList<>();
        String sql = "CALL usp_modelo_read_by_marca(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idMarca);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsModelo modelo = new clsModelo();
                modelo.setIdmodelo(resultSet.getInt("idmodelo"));
                modelo.setIdmarca(resultSet.getInt("idmarca"));
                modelo.setNombremarca(resultSet.getString("nombremarca"));
                modelo.setNombremodelo(resultSet.getString("nombremodelo"));
                modelo.setDescripcion(resultSet.getString("descripcion"));
                modelo.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    modelo.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    modelo.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaModelos.add(modelo);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar modelos por marca: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaModelos;
    }
    
    @Override
    public boolean agregar(clsModelo modelo) {
        boolean exito = false;
        String sql = "CALL usp_modelo_create(?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, modelo.getIdmarca());
            preparedStatement.setString(2, modelo.getNombremodelo());
            preparedStatement.setString(3, modelo.getDescripcion());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idGenerado = resultSet.getInt("idmodelo");
                modelo.setIdmodelo(idGenerado);
                exito = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar modelo: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizar(clsModelo modelo) {
        boolean exito = false;
        String sql = "CALL usp_modelo_update(?, ?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, modelo.getIdmodelo());
            preparedStatement.setInt(2, modelo.getIdmarca());
            preparedStatement.setString(3, modelo.getNombremodelo());
            preparedStatement.setString(4, modelo.getDescripcion());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar modelo: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "CALL usp_modelo_delete(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar modelo: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean activar(int id) {
        boolean exito = false;
        String sql = "CALL usp_modelo_activate(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al activar modelo: " + e.getMessage());
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