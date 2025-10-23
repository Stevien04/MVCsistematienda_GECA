package Interfaces;

import Modelo.clsEmpleado;
import java.util.List;

public interface CRUDEmpleado {
    // Métodos básicos del CRUD
    List<clsEmpleado> listar();
    clsEmpleado listarPorId(int id);
    clsEmpleado listarPorUsuario(String usuario);
    clsEmpleado validarLogin(String usuario, String password);
    boolean agregar(clsEmpleado empleado);
    boolean actualizar(clsEmpleado empleado);
    boolean eliminar(int id);
    
    // Métodos de validación
    boolean existeDni(String dni);
    boolean existeDni(String dni, int idExcluir);
    boolean existeUsuario(String usuario);
    boolean existeUsuario(String usuario, int idExcluir);
    
    // Métodos de búsqueda y filtros
    List<clsEmpleado> buscar(String criterio, String valor);
    List<clsEmpleado> listarPorCargo(int idCargo);
    List<clsEmpleado> listarTodos(); // Incluye inactivos
    
    // Métodos de estadísticas y reportes
    int obtenerTotalEmpleados();
    boolean cambiarEstado(int id, String estado);
    
    // Métodos adicionales para perfil
    boolean actualizarPerfil(clsEmpleado empleado);
    
    
}