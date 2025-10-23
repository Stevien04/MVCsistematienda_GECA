package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDCategoria;
import Modelo.clsCategoria;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class clsCategoriaDAO implements CRUDCategoria {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsCategoriaDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsCategoria> listar() {
        List<clsCategoria> listaCategorias = new ArrayList<>();
        String sql = "SELECT * FROM tbcategoria WHERE estado = 1 ORDER BY nombrecategoria";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                listaCategorias.add(mapearCategoria(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar categorías: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaCategorias;
    }
    
    @Override
    public List<clsCategoria> listarInactivas() {
        List<clsCategoria> listaCategorias = new ArrayList<>();
        String sql = "SELECT * FROM tbcategoria WHERE estado = 0 ORDER BY nombrecategoria";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                listaCategorias.add(mapearCategoria(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar categorías inactivas: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaCategorias;
    }
    
    @Override
    public clsCategoria listarPorId(int id) {
        clsCategoria categoria = null;
        String sql = "SELECT * FROM tbcategoria WHERE idcategoria = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                categoria = mapearCategoria(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar categoría por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return categoria;
    }
    
    @Override
    public boolean agregar(clsCategoria categoria) {
        boolean exito = false;
        String sql = "INSERT INTO tbcategoria (nombrecategoria, descripcion, estado) VALUES (?, ?, 1)";
        
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, categoria.getNombrecategoria());
            preparedStatement.setString(2, categoria.getDescripcion());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int idGenerado = resultSet.getInt(1);
                    categoria.setIdcategoria(idGenerado);
                    exito = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar categoría: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizar(clsCategoria categoria) {
        boolean exito = false;
        String sql = "UPDATE tbcategoria SET nombrecategoria = ?, descripcion = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idcategoria = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, categoria.getNombrecategoria());
            preparedStatement.setString(2, categoria.getDescripcion());
            preparedStatement.setInt(3, categoria.getIdcategoria());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar categoría: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "UPDATE tbcategoria SET estado = 0, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idcategoria = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar categoría: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean reactivar(int id) {
        boolean exito = false;
        String sql = "UPDATE tbcategoria SET estado = 1, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idcategoria = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al reactivar categoría: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    // Método auxiliar para reutilizar el mapeo
    private clsCategoria mapearCategoria(ResultSet resultSet) throws SQLException {
        clsCategoria categoria = new clsCategoria();
        categoria.setIdcategoria(resultSet.getInt("idcategoria"));
        categoria.setNombrecategoria(resultSet.getString("nombrecategoria"));
        categoria.setDescripcion(resultSet.getString("descripcion"));
        categoria.setEstado(resultSet.getInt("estado"));
        
        Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
        if (timestampCreacion != null) {
            categoria.setFechaCreacion(timestampCreacion.toLocalDateTime());
        }
        
        Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
        if (timestampActualizacion != null) {
            categoria.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
        }
        
        return categoria;
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