package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDDetalle;
import Modelo.clsDetalle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class clsDetalleDAO implements CRUDDetalle {
    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public clsDetalleDAO() {
        connection = clsConexion.getConnection();
    }

    @Override
    public List<clsDetalle> listarPorBoleta(int idBoleta) {
        List<clsDetalle> detalles = new ArrayList<>();
        String sql = "SELECT d.*, p.nombreproducto, b.numero_boleta "
                + "FROM tbdetalleboleta d "
                + "INNER JOIN tbproducto p ON d.idproducto = p.idproducto "
                + "INNER JOIN tbboleta b ON d.idboleta = b.idboleta "
                + "WHERE d.idboleta = ? AND d.estado = 1 "
                + "ORDER BY d.iddetalle";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idBoleta);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                detalles.add(mapDetalle(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar detalles de boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return detalles;
    }

    @Override
    public clsDetalle listarPorId(int id) {
        clsDetalle detalle = null;
        String sql = "SELECT d.*, p.nombreproducto, b.numero_boleta "
                + "FROM tbdetalleboleta d "
                + "INNER JOIN tbproducto p ON d.idproducto = p.idproducto "
                + "INNER JOIN tbboleta b ON d.idboleta = b.idboleta "
                + "WHERE d.iddetalle = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                detalle = mapDetalle(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar detalle de boleta por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return detalle;
    }

    @Override
    public boolean agregar(clsDetalle detalle) {
        boolean exito = false;
        String sql = "INSERT INTO tbdetalleboleta (idboleta, idproducto, cantidad, precio_unitario, importe) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, detalle.getIdboleta());
            preparedStatement.setInt(2, detalle.getIdproducto());
            preparedStatement.setInt(3, detalle.getCantidad());
            preparedStatement.setBigDecimal(4, detalle.getPrecioUnitario());
            preparedStatement.setBigDecimal(5, detalle.getImporte());

            int filas = preparedStatement.executeUpdate();
            if (filas > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    detalle.setIddetalle(resultSet.getInt(1));
                    exito = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar detalle de boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }

    @Override
    public boolean actualizar(clsDetalle detalle) {
        boolean exito = false;
        String sql = "UPDATE tbdetalleboleta SET idproducto = ?, cantidad = ?, precio_unitario = ?, importe = ?, "
                + "fecha_creacion = fecha_creacion WHERE iddetalle = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, detalle.getIdproducto());
            preparedStatement.setInt(2, detalle.getCantidad());
            preparedStatement.setBigDecimal(3, detalle.getPrecioUnitario());
            preparedStatement.setBigDecimal(4, detalle.getImporte());
            preparedStatement.setInt(5, detalle.getIddetalle());

            exito = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar detalle de boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }

    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "UPDATE tbdetalleboleta SET estado = 0 WHERE iddetalle = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            exito = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar detalle de boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }

    @Override
    public boolean eliminarPorBoleta(int idBoleta) {
        boolean exito = false;
        String sql = "UPDATE tbdetalleboleta SET estado = 0 WHERE idboleta = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idBoleta);
            exito = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar detalles por boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }

    private clsDetalle mapDetalle(ResultSet rs) throws SQLException {
        clsDetalle detalle = new clsDetalle();
        detalle.setIddetalle(rs.getInt("iddetalle"));
        detalle.setIdboleta(rs.getInt("idboleta"));
        detalle.setIdproducto(rs.getInt("idproducto"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        detalle.setImporte(rs.getBigDecimal("importe"));
        detalle.setEstado(rs.getInt("estado"));

        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            detalle.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }

        detalle.setNombreproducto(rs.getString("nombreproducto"));
        detalle.setNumeroBoleta(rs.getString("numero_boleta"));

        return detalle;
    }

    private void closeResources() {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}