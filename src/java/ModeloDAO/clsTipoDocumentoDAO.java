package ModeloDAO;

import Config.clsConexion;
import Interfaces.CRUDTipoDocumento;
import Modelo.clsTipoDocumento;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class clsTipoDocumentoDAO implements CRUDTipoDocumento {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public clsTipoDocumentoDAO() {
        connection = clsConexion.getConnection();
    }
    
    @Override
    public List<clsTipoDocumento> listar() {
        List<clsTipoDocumento> listaTipoDocumentos = new ArrayList<>();
        String sql = "SELECT * FROM tbtipodocumento WHERE estado = 1 ORDER BY nombretipodocumento";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                clsTipoDocumento tipoDocumento = new clsTipoDocumento();
                tipoDocumento.setIdtipodocumento(resultSet.getInt("idtipodocumento"));
                tipoDocumento.setNombretipodocumento(resultSet.getString("nombretipodocumento"));
                tipoDocumento.setAbreviatura(resultSet.getString("abreviatura"));
                tipoDocumento.setDescripcion(resultSet.getString("descripcion"));
                tipoDocumento.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    tipoDocumento.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    tipoDocumento.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
                
                listaTipoDocumentos.add(tipoDocumento);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar tipos de documento: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listaTipoDocumentos;
    }
    
    @Override
    public clsTipoDocumento listarPorId(int id) {
        clsTipoDocumento tipoDocumento = null;
        String sql = "SELECT * FROM tbtipodocumento WHERE idtipodocumento = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                tipoDocumento = new clsTipoDocumento();
                tipoDocumento.setIdtipodocumento(resultSet.getInt("idtipodocumento"));
                tipoDocumento.setNombretipodocumento(resultSet.getString("nombretipodocumento"));
                tipoDocumento.setAbreviatura(resultSet.getString("abreviatura"));
                tipoDocumento.setDescripcion(resultSet.getString("descripcion"));
                tipoDocumento.setEstado(resultSet.getInt("estado"));
                
                Timestamp timestampCreacion = resultSet.getTimestamp("fecha_creacion");
                if (timestampCreacion != null) {
                    tipoDocumento.setFechaCreacion(timestampCreacion.toLocalDateTime());
                }
                
                Timestamp timestampActualizacion = resultSet.getTimestamp("fecha_actualizacion");
                if (timestampActualizacion != null) {
                    tipoDocumento.setFechaActualizacion(timestampActualizacion.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar tipo de documento por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return tipoDocumento;
    }
    
    @Override
    public boolean agregar(clsTipoDocumento tipoDocumento) {
        boolean exito = false;
        String sql = "INSERT INTO tbtipodocumento (nombretipodocumento, abreviatura, descripcion) VALUES (?, ?, ?)";
        
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tipoDocumento.getNombretipodocumento());
            preparedStatement.setString(2, tipoDocumento.getAbreviatura());
            preparedStatement.setString(3, tipoDocumento.getDescripcion());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int idGenerado = resultSet.getInt(1);
                    tipoDocumento.setIdtipodocumento(idGenerado);
                    exito = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar tipo de documento: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean actualizar(clsTipoDocumento tipoDocumento) {
        boolean exito = false;
        String sql = "UPDATE tbtipodocumento SET nombretipodocumento = ?, abreviatura = ?, descripcion = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idtipodocumento = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tipoDocumento.getNombretipodocumento());
            preparedStatement.setString(2, tipoDocumento.getAbreviatura());
            preparedStatement.setString(3, tipoDocumento.getDescripcion());
            preparedStatement.setInt(4, tipoDocumento.getIdtipodocumento());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar tipo de documento: " + e.getMessage());
        } finally {
            closeResources();
        }
        return exito;
    }
    
    @Override
    public boolean eliminar(int id) {
        boolean exito = false;
        String sql = "UPDATE tbtipodocumento SET estado = 0, fecha_actualizacion = CURRENT_TIMESTAMP WHERE idtipodocumento = ?";
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            int filasAfectadas = preparedStatement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar tipo de documento: " + e.getMessage());
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