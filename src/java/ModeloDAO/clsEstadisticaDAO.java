package ModeloDAO;

import Config.clsConexion;
import Modelo.clsResumenVenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsEstadisticaDAO {
    private Connection connection;

    public clsEstadisticaDAO() {
        connection = clsConexion.getConnection();
    }

    public int obtenerTotalClientesActivos() {
        String sql = "SELECT COUNT(*) AS total FROM tbcliente WHERE estado = 1";
        return ejecutarConsultaEntero(sql);
    }

    public int obtenerTotalProductosActivos() {
        String sql = "SELECT COUNT(*) AS total FROM tbproducto WHERE estado = 1";
        return ejecutarConsultaEntero(sql);
    }

    public int obtenerTotalVentas() {
        String sql = "SELECT COUNT(*) AS total FROM tbboleta WHERE estado = 1";
        return ejecutarConsultaEntero(sql);
    }

    public int obtenerTotalProductosVendidos() {
        String sql = "SELECT COALESCE(SUM(cantidad), 0) AS total FROM tbdetalleboleta WHERE estado = 1";
        return ejecutarConsultaEntero(sql);
    }

    public double obtenerTotalIngresos() {
        double total = 0.0;
        if (connection == null) {
            return total;
        }

        String sql = "SELECT COALESCE(SUM(total), 0) AS total FROM tbboleta WHERE estado = 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                total = resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener total de ingresos: " + e.getMessage());
        }
        return total;
    }

    public List<clsResumenVenta> obtenerTopCategorias(int limite) {
        List<clsResumenVenta> categorias = new ArrayList<>();
        if (connection == null) {
            return categorias;
        }

        String sql = "SELECT c.nombrecategoria AS nombre, SUM(d.cantidad) AS total "
                   + "FROM tbdetalleboleta d "
                   + "INNER JOIN tbproducto p ON d.idproducto = p.idproducto "
                   + "INNER JOIN tbcategoria c ON p.idcategoria = c.idcategoria "
                   + "WHERE d.estado = 1 "
                   + "GROUP BY c.nombrecategoria "
                   + "ORDER BY total DESC "
                   + "LIMIT ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, limite);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String nombre = resultSet.getString("nombre");
                    int total = resultSet.getInt("total");
                    categorias.add(new clsResumenVenta(nombre, total));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener top de categorías: " + e.getMessage());
        }

        return categorias;
    }

    public List<clsResumenVenta> obtenerTopProductos(int limite) {
        List<clsResumenVenta> productos = new ArrayList<>();
        if (connection == null) {
            return productos;
        }

        String sql = "SELECT p.nombreproducto AS nombre, SUM(d.cantidad) AS total "
                   + "FROM tbdetalleboleta d "
                   + "INNER JOIN tbproducto p ON d.idproducto = p.idproducto "
                   + "WHERE d.estado = 1 "
                   + "GROUP BY p.nombreproducto "
                   + "ORDER BY total DESC "
                   + "LIMIT ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, limite);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String nombre = resultSet.getString("nombre");
                    int total = resultSet.getInt("total");
                    productos.add(new clsResumenVenta(nombre, total));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener top de productos: " + e.getMessage());
        }

        return productos;
    }

    public void cerrarConexion() {
        if (connection != null) {
            clsConexion.closeConnection(connection);
            connection = null;
        }
    }

    private int ejecutarConsultaEntero(String sql) {
        int total = 0;
        if (connection == null) {
            return total;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                total = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar consulta estadística: " + e.getMessage());
        }
        return total;
    }
}