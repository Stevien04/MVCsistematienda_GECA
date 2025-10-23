package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDProducto;
import Modelo.clsProducto;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class clsProductoDAO implements CRUDProducto {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsProductoDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsProducto> listar() {
        List<clsProducto> listaProductos = new ArrayList<>();
        String sql = "CALL usp_producto_read()";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsProducto producto = new clsProducto();
                producto.setIdproducto(resultSet.getInt("idproducto"));
                producto.setIdcategoria(resultSet.getInt("idcategoria"));
                producto.setNombrecategoria(resultSet.getString("nombrecategoria"));
                producto.setIdmodelo(resultSet.getInt("idmodelo"));
                producto.setNombremodelo(resultSet.getString("nombremodelo"));
                producto.setNombremarca(resultSet.getString("nombremarca"));
                producto.setIdcolor(resultSet.getInt("idcolor"));
                producto.setNombrecolor(resultSet.getString("nombrecolor"));
                producto.setCodigoHex(resultSet.getString("codigo_hex"));
                producto.setNombreproducto(resultSet.getString("nombreproducto"));
                producto.setDescripcion(resultSet.getString("descripcion"));
                producto.setPrecio(resultSet.getBigDecimal("precio"));
                producto.setStock(resultSet.getInt("stock"));
                producto.setEstado(resultSet.getInt("estado"));
                
                Date fechaCreacion = resultSet.getDate("fecha_creacion");
                if (fechaCreacion != null) {
                    producto.setFechaCreacion(fechaCreacion.toLocalDate());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    producto.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaProductos.add(producto);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaProductos;
    }
    
    @Override
    public clsProducto listarPorId(int id) {
        clsProducto producto = null;
        String sql = "CALL usp_producto_read_by_id(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                producto = new clsProducto();
                producto.setIdproducto(resultSet.getInt("idproducto"));
                producto.setIdcategoria(resultSet.getInt("idcategoria"));
                producto.setNombrecategoria(resultSet.getString("nombrecategoria"));
                producto.setIdmodelo(resultSet.getInt("idmodelo"));
                producto.setNombremodelo(resultSet.getString("nombremodelo"));
                producto.setNombremarca(resultSet.getString("nombremarca"));
                producto.setIdcolor(resultSet.getInt("idcolor"));
                producto.setNombrecolor(resultSet.getString("nombrecolor"));
                producto.setCodigoHex(resultSet.getString("codigo_hex"));
                producto.setNombreproducto(resultSet.getString("nombreproducto"));
                producto.setDescripcion(resultSet.getString("descripcion"));
                producto.setPrecio(resultSet.getBigDecimal("precio"));
                producto.setStock(resultSet.getInt("stock"));
                producto.setEstado(resultSet.getInt("estado"));
                
                Date fechaCreacion = resultSet.getDate("fecha_creacion");
                if (fechaCreacion != null) {
                    producto.setFechaCreacion(fechaCreacion.toLocalDate());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    producto.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return producto;
    }
    
    @Override
    public boolean agregar(clsProducto producto) {
        boolean exito = false;
        String sql = "CALL usp_producto_create(?, ?, ?, ?, ?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, producto.getIdcategoria());
            preparedStatement.setInt(2, producto.getIdmodelo());
            preparedStatement.setInt(3, producto.getIdcolor());
            preparedStatement.setString(4, producto.getNombreproducto());
            preparedStatement.setString(5, producto.getDescripcion());
            preparedStatement.setBigDecimal(6, producto.getPrecio());
            preparedStatement.setInt(7, producto.getStock());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idGenerado = resultSet.getInt("idproducto");
                producto.setIdproducto(idGenerado);
                exito = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    // Método para listar productos inactivos
    public List<clsProducto> listarInactivos() {
        List<clsProducto> listaProductos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombrecategoria, m.nombremodelo, ma.nombremarca, "
                + "col.nombrecolor, col.codigo_hex "
                + "FROM tbproducto p "
                + "INNER JOIN tbcategoria c ON p.idcategoria = c.idcategoria "
                + "INNER JOIN tbmodelo m ON p.idmodelo = m.idmodelo "
                + "INNER JOIN tbmarca ma ON m.idmarca = ma.idmarca "
                + "INNER JOIN tbcolor col ON p.idcolor = col.idcolor "
                + "WHERE p.estado = 0 "
                + 
                "ORDER BY p.nombreproducto"; 

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listaProductos.add(mapearProducto(resultSet));
            }

            System.out.println("=== PRODUCTOS INACTIVOS ENCONTRADOS: " + listaProductos.size() + " ===");

        } catch (SQLException e) {
            System.out.println("Error al listar productos inactivos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return listaProductos;
    }

// Método auxiliar para reutilizar el mapeo
    private clsProducto mapearProducto(ResultSet resultSet) throws SQLException {
        clsProducto producto = new clsProducto();
        producto.setIdproducto(resultSet.getInt("idproducto"));
        producto.setIdcategoria(resultSet.getInt("idcategoria"));
        producto.setNombrecategoria(resultSet.getString("nombrecategoria"));
        producto.setIdmodelo(resultSet.getInt("idmodelo"));
        producto.setNombremodelo(resultSet.getString("nombremodelo"));
        producto.setNombremarca(resultSet.getString("nombremarca"));
        producto.setIdcolor(resultSet.getInt("idcolor"));
        producto.setNombrecolor(resultSet.getString("nombrecolor"));
        producto.setCodigoHex(resultSet.getString("codigo_hex"));
        producto.setNombreproducto(resultSet.getString("nombreproducto"));
        producto.setDescripcion(resultSet.getString("descripcion"));
        producto.setPrecio(resultSet.getBigDecimal("precio"));
        producto.setStock(resultSet.getInt("stock"));
        producto.setEstado(resultSet.getInt("estado"));

        Date fechaCreacion = resultSet.getDate("fecha_creacion");
        if (fechaCreacion != null) {
            producto.setFechaCreacion(fechaCreacion.toLocalDate());
        }

        Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
        if (timestampActualizacion != null) {
            producto.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
        }

        return producto;
    }
    
    @Override
    public boolean actualizar(clsProducto producto) {
        boolean exito = false;
        String sql = "CALL usp_producto_update(?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, producto.getIdproducto());
            preparedStatement.setInt(2, producto.getIdcategoria());
            preparedStatement.setInt(3, producto.getIdmodelo());
            preparedStatement.setInt(4, producto.getIdcolor());
            preparedStatement.setString(5, producto.getNombreproducto());
            preparedStatement.setString(6, producto.getDescripcion());
            preparedStatement.setBigDecimal(7, producto.getPrecio());
            preparedStatement.setInt(8, producto.getStock());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "CALL usp_producto_delete(?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean activar(int id) {
        boolean exito = false;
        String sql = "UPDATE tbproducto SET estado = 1, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idproducto = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;

            System.out.println("=== ACTIVANDO PRODUCTO ID: " + id + " - ÉXITO: " + exito + " ===");

        } catch (SQLException e) {
            System.out.println("Error al activar producto: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return exito;
    }
    
    public List<clsProducto> listarActivos() {
        List<clsProducto> listaProductos = new ArrayList<>();
        String sql = "CALL usp_producto_read_activos()"; // O usar consulta directa si no tienes stored procedure

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listaProductos.add(mapearProducto(resultSet));
            }
        } catch (SQLException e) {
            // Si falla el stored procedure, usar consulta directa
            System.out.println("Error con stored procedure, usando consulta directa: " + e.getMessage());
            listarActivosConsultaDirecta(listaProductos);
        } finally {
            closeResources();
        }
        return listaProductos;
    }

    /**
     * Método alternativo si no tienes stored procedure para productos activos
     */
    private void listarActivosConsultaDirecta(List<clsProducto> listaProductos) {
        String sql = "SELECT p.*, c.nombrecategoria, m.nombremodelo, ma.nombremarca, "
                + "col.nombrecolor, col.codigo_hex "
                + "FROM tbproducto p "
                + "INNER JOIN tbcategoria c ON p.idcategoria = c.idcategoria "
                + "INNER JOIN tbmodelo m ON p.idmodelo = m.idmodelo "
                + "INNER JOIN tbmarca ma ON m.idmarca = ma.idmarca "
                + "INNER JOIN tbcolor col ON p.idcolor = col.idcolor "
                + "WHERE p.estado = 1 AND p.stock > 0 "
                + "ORDER BY p.nombreproducto";

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listaProductos.add(mapearProducto(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos activos: " + e.getMessage());
        }
    }

    /**
     * Método para actualizar el stock después de una compra
     */
    public boolean actualizarStock(int idProducto, int cantidad) {
        boolean exito = false;

        // Intentar con stored procedure primero
        String sql = "CALL usp_producto_update_stock(?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idProducto);
            preparedStatement.setInt(2, cantidad);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int filasAfectadas = resultSet.getInt("filas_afectadas");
                exito = filasAfectadas > 0;
            }
        } catch (SQLException e) {
            // Si falla el stored procedure, usar UPDATE directo
            System.out.println("Error con stored procedure, usando UPDATE directo: " + e.getMessage());
            exito = actualizarStockDirecto(idProducto, cantidad);
        } finally {
            closeResources();
        }
        return exito;
    }

    /**
     * Método alternativo para actualizar stock si no tienes stored procedure
     */
    private boolean actualizarStockDirecto(int idProducto, int cantidad) {
        boolean exito = false;
        String sql = "UPDATE tbproducto SET stock = stock - ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idproducto = ? AND stock >= ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cantidad);
            preparedStatement.setInt(2, idProducto);
            preparedStatement.setInt(3, cantidad);

            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());
        }
        return exito;
    }

    /**
     * Método para verificar stock disponible
     */
    public boolean verificarStock(int idProducto, int cantidadRequerida) {
        boolean stockDisponible = false;
        String sql = "SELECT stock FROM tbproducto WHERE idproducto = ? AND estado = 1";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idProducto);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int stockActual = resultSet.getInt("stock");
                stockDisponible = stockActual >= cantidadRequerida;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar stock: " + e.getMessage());
        } finally {
            closeResources();
        }
        return stockDisponible;
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