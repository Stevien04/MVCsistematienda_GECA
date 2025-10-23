package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDCargo;
import Modelo.clsCargo;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class clsCargoDAO implements CRUDCargo {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsCargoDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsCargo> listar() {
        List<clsCargo> listaCargos = new ArrayList<>();
        String sql = "CALL usp_cargo_read()";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsCargo cargo = new clsCargo();
                cargo.setIdcargo(resultSet.getInt("idcargo"));
                cargo.setNombrecargo(resultSet.getString("nombrecargo"));
                cargo.setDescripcion(resultSet.getString("descripcion"));
                cargo.setEstado(resultSet.getInt("estado"));
                
                // Convertir Timestamp a LocalDateTime
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    cargo.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    cargo.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaCargos.add(cargo);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar cargos: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaCargos;
    }
    
    @Override
    public clsCargo listarPorId(int id) {
        clsCargo cargo = null;
        String sql = "CALL usp_cargo_read_by_id(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                cargo = new clsCargo();
                cargo.setIdcargo(resultSet.getInt("idcargo"));
                cargo.setNombrecargo(resultSet.getString("nombrecargo"));
                cargo.setDescripcion(resultSet.getString("descripcion"));
                cargo.setEstado(resultSet.getInt("estado"));
                
                // Convertir Timestamp a LocalDateTime
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    cargo.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    cargo.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cargo por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return cargo;
    }
    
    @Override
    public boolean agregar(clsCargo cargo) {
        boolean exito = false;
        String sql = "CALL usp_cargo_create(?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, cargo.getNombrecargo());
            preparedStatement.setString(2, cargo.getDescripcion());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idGenerado = resultSet.getInt("idcargo");
                cargo.setIdcargo(idGenerado);
                exito = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar cargo: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizar(clsCargo cargo) {
        boolean exito = false;
        String sql = "CALL usp_cargo_update(?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cargo.getIdcargo());
            preparedStatement.setString(2, cargo.getNombrecargo());
            preparedStatement.setString(3, cargo.getDescripcion());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar cargo: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "CALL usp_cargo_delete(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar cargo: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean activar(int id) {
        boolean exito = false;
        String sql = "CALL usp_cargo_activate(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al activar cargo: " + e.getMessage());
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