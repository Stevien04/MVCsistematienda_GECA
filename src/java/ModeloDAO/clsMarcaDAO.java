package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDMarca;
import Modelo.clsMarca;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class clsMarcaDAO implements CRUDMarca {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsMarcaDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsMarca> listar() {
        List<clsMarca> listaMarcas = new ArrayList<>();
        String sql = "CALL usp_marca_read()";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsMarca marca = new clsMarca();
                marca.setIdmarca(resultSet.getInt("idmarca"));
                marca.setNombremarca(resultSet.getString("nombremarca"));
                marca.setDescripcion(resultSet.getString("descripcion"));
                marca.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    marca.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    marca.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaMarcas.add(marca);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar marcas: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaMarcas;
    }
    
    @Override
    public clsMarca listarPorId(int id) {
        clsMarca marca = null;
        String sql = "CALL usp_marca_read_by_id(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                marca = new clsMarca();
                marca.setIdmarca(resultSet.getInt("idmarca"));
                marca.setNombremarca(resultSet.getString("nombremarca"));
                marca.setDescripcion(resultSet.getString("descripcion"));
                marca.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    marca.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    marca.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar marca por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return marca;
    }
    
    @Override
    public boolean agregar(clsMarca marca) {
        boolean exito = false;
        String sql = "CALL usp_marca_create(?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, marca.getNombremarca());
            preparedStatement.setString(2, marca.getDescripcion());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idGenerado = resultSet.getInt("idmarca");
                marca.setIdmarca(idGenerado);
                exito = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar marca: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizar(clsMarca marca) {
        boolean exito = false;
        String sql = "CALL usp_marca_update(?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, marca.getIdmarca());
            preparedStatement.setString(2, marca.getNombremarca());
            preparedStatement.setString(3, marca.getDescripcion());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar marca: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "CALL usp_marca_delete(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar marca: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean activar(int id) {
        boolean exito = false;
        String sql = "CALL usp_marca_activate(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al activar marca: " + e.getMessage());
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