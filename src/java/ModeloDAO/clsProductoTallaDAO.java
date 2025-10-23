package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDProductoTalla;
import Modelo.clsProductoTalla;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class clsProductoTallaDAO implements CRUDProductoTalla {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsProductoTallaDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsProductoTalla> listarPorProducto(int idProducto) {
        List<clsProductoTalla> listaProductoTallas = new ArrayList<>();
        String sql = "CALL usp_producto_talla_read(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idProducto);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsProductoTalla productoTalla = new clsProductoTalla();
                productoTalla.setIdproductoTalla(resultSet.getInt("idproducto_talla"));
                productoTalla.setIdproducto(resultSet.getInt("idproducto"));
                productoTalla.setIdtalla(resultSet.getInt("idtalla"));
                productoTalla.setNombretalla(resultSet.getString("nombretalla"));
                productoTalla.setNombretipotalla(resultSet.getString("nombretipotalla"));
                productoTalla.setStock(resultSet.getInt("stock"));
                productoTalla.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    productoTalla.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    productoTalla.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaProductoTallas.add(productoTalla);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar producto-tallas: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaProductoTallas;
    }
    
    @Override
    public clsProductoTalla listarPorId(int id) {
        clsProductoTalla productoTalla = null;
        String sql = "SELECT * FROM tbproducto_talla WHERE idproducto_talla = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                productoTalla = new clsProductoTalla();
                productoTalla.setIdproductoTalla(resultSet.getInt("idproducto_talla"));
                productoTalla.setIdproducto(resultSet.getInt("idproducto"));
                productoTalla.setIdtalla(resultSet.getInt("idtalla"));
                productoTalla.setStock(resultSet.getInt("stock"));
                productoTalla.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    productoTalla.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    productoTalla.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto-talla por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return productoTalla;
    }
    
    @Override
    public boolean agregar(clsProductoTalla productoTalla) {
        boolean exito = false;
        String sql = "CALL usp_producto_talla_create(?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, productoTalla.getIdproducto());
            preparedStatement.setInt(2, productoTalla.getIdtalla());
            preparedStatement.setInt(3, productoTalla.getStock());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idGenerado = resultSet.getInt("idproducto_talla");
                productoTalla.setIdproductoTalla(idGenerado);
                exito = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar producto-talla: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizarStock(int idProductoTalla, int stock) {
        boolean exito = false;
        String sql = "CALL usp_producto_talla_update_stock(?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idProductoTalla);
            preparedStatement.setInt(2, stock);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar stock de producto-talla: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "CALL usp_producto_talla_delete(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto-talla: " + e.getMessage());
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