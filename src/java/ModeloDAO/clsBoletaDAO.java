package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDBoleta;
import Modelo.clsBoleta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class clsBoletaDAO implements CRUDBoleta {
    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public clsBoletaDAO() {
        connection = clsConexion.getConnection();
    }

    @Override
    public List<clsBoleta> listar() {
        List<clsBoleta> boletas = new ArrayList<>();
        String sql = "SELECT b.*, c.nombre AS nombreCliente, c.dni AS dniCliente, "
                + "CONCAT(e.nombre, ' ', e.apellido) AS nombreEmpleado "
                + "FROM tbboleta b "
                + "LEFT JOIN tbcliente c ON b.idcliente = c.idcliente "
                + "LEFT JOIN tbempleado e ON b.idempleado = e.idempleado "
                + "WHERE b.estado = 1 "
                + "ORDER BY b.fecha_emision DESC, b.hora_emision DESC, b.idboleta DESC";

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                boletas.add(mapBoleta(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar boletas: " + e.getMessage());
        } finally {
            closeResources();
        }
        return boletas;
    }

    @Override
    public clsBoleta listarPorId(int id) {
        clsBoleta boleta = null;
        String sql = "SELECT b.*, c.nombre AS nombreCliente, c.dni AS dniCliente, "
                + "CONCAT(e.nombre, ' ', e.apellido) AS nombreEmpleado "
                + "FROM tbboleta b "
                + "LEFT JOIN tbcliente c ON b.idcliente = c.idcliente "
                + "LEFT JOIN tbempleado e ON b.idempleado = e.idempleado "
                + "WHERE b.idboleta = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                boleta = mapBoleta(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar boleta por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return boleta;
    }

    @Override
    public clsBoleta listarPorNumero(String numeroBoleta) {
        clsBoleta boleta = null;
        String sql = "SELECT b.*, c.nombre AS nombreCliente, c.dni AS dniCliente, "
                + "CONCAT(e.nombre, ' ', e.apellido) AS nombreEmpleado "
                + "FROM tbboleta b "
                + "LEFT JOIN tbcliente c ON b.idcliente = c.idcliente "
                + "LEFT JOIN tbempleado e ON b.idempleado = e.idempleado "
                + "WHERE b.numero_boleta = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, numeroBoleta);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                boleta = mapBoleta(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar boleta por número: " + e.getMessage());
        } finally {
            closeResources();
        }
        return boleta;
    }

    @Override
    public boolean agregar(clsBoleta boleta) {
        boolean exito = false;
        String sql = "INSERT INTO tbboleta (numero_boleta, fecha_emision, hora_emision, subtotal, igv, total, "
                + "estado_boleta, idcliente, idempleado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, boleta.getNumeroBoleta());
            preparedStatement.setDate(2, java.sql.Date.valueOf(
                    boleta.getFechaEmision() != null ? boleta.getFechaEmision() : LocalDate.now()));
            LocalTime hora = boleta.getHoraEmision() != null ? boleta.getHoraEmision() : LocalTime.now();
            preparedStatement.setTime(3, Time.valueOf(hora));
            preparedStatement.setBigDecimal(4, boleta.getSubtotal());
            preparedStatement.setBigDecimal(5, boleta.getIgv());
            preparedStatement.setBigDecimal(6, boleta.getTotal());
            preparedStatement.setString(7, boleta.getEstadoBoleta());
            preparedStatement.setInt(8, boleta.getIdcliente());
            preparedStatement.setInt(9, boleta.getIdempleado());

            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int idGenerado = resultSet.getInt(1);
                    boleta.setIdboleta(idGenerado);
                    exito = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }

    @Override
    public boolean actualizar(clsBoleta boleta) {
        boolean exito = false;
        String sql = "UPDATE tbboleta SET numero_boleta = ?, subtotal = ?, igv = ?, total = ?, estado_boleta = ?, "
                + "idcliente = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idboleta = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, boleta.getNumeroBoleta());
            preparedStatement.setBigDecimal(2, boleta.getSubtotal());
            preparedStatement.setBigDecimal(3, boleta.getIgv());
            preparedStatement.setBigDecimal(4, boleta.getTotal());
            preparedStatement.setString(5, boleta.getEstadoBoleta());
            preparedStatement.setInt(6, boleta.getIdcliente());
            preparedStatement.setInt(7, boleta.getIdboleta());

            exito = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }

    @Override
    public boolean anular(int id) {
        boolean exito = false;
        String sql = "UPDATE tbboleta SET estado_boleta = 'ANULADA', estado = 0, fecha_actualizacion = CURRENT_TIMESTAMP "
                + "WHERE idboleta = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            exito = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al anular boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }

    @Override
    public String generarNumeroBoleta() {
        String numero = "BOL-000001";
        String sql = "SELECT LPAD(COALESCE(MAX(CAST(SUBSTRING_INDEX(numero_boleta, '-', -1) AS UNSIGNED)), 0) + 1, 6, '0') AS siguiente "
                + "FROM tbboleta";

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String correlativo = resultSet.getString("siguiente");
                if (correlativo != null) {
                    numero = "BOL-" + correlativo;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al generar número de boleta: " + e.getMessage());
        } finally {
            closeResources();
        }
        return numero;
    }

    private clsBoleta mapBoleta(ResultSet rs) throws SQLException {
        clsBoleta boleta = new clsBoleta();
        boleta.setIdboleta(rs.getInt("idboleta"));
        boleta.setNumeroBoleta(rs.getString("numero_boleta"));

        java.sql.Date fechaEmision = rs.getDate("fecha_emision");
        if (fechaEmision != null) {
            boleta.setFechaEmision(fechaEmision.toLocalDate());
        }

        Time horaEmision = rs.getTime("hora_emision");
        if (horaEmision != null) {
            boleta.setHoraEmision(horaEmision.toLocalTime());
        }

        boleta.setSubtotal(rs.getBigDecimal("subtotal"));
        boleta.setIgv(rs.getBigDecimal("igv"));
        boleta.setTotal(rs.getBigDecimal("total"));
        boleta.setEstadoBoleta(rs.getString("estado_boleta"));
        boleta.setIdcliente(rs.getInt("idcliente"));
        boleta.setIdempleado(rs.getInt("idempleado"));
        boleta.setEstado(rs.getInt("estado"));

        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            boleta.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }

        Timestamp fechaActualizacion = rs.getTimestamp("fecha_actualizacion");
        if (fechaActualizacion != null) {
            boleta.setFechaActualizacion(fechaActualizacion.toLocalDateTime());
        }

        boleta.setNombreCliente(rs.getString("nombreCliente"));
        boleta.setDniCliente(rs.getString("dniCliente"));
        boleta.setNombreEmpleado(rs.getString("nombreEmpleado"));

        return boleta;
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