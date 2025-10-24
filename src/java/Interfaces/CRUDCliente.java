package Interfaces;

import Modelo.clsCliente;
import java.util.List;

public interface CRUDCliente {
    List<clsCliente> listar();
    clsCliente listarPorId(int id);
    clsCliente listarPorDni(String dni);
    boolean agregar(clsCliente cliente);
    boolean actualizar(clsCliente cliente);
    boolean eliminar(int id);
    boolean Registrarcliente(clsCliente cliente);
}