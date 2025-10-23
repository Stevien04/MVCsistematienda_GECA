package Interfaces;

import java.util.List;
import Modelo.clsCategoria;

public interface CRUDCategoria {
    public List<clsCategoria> listar();
    public List<clsCategoria> listarInactivas(); // ✅ NUEVO MÉTODO
    public clsCategoria listarPorId(int id);
    public boolean agregar(clsCategoria categoria);
    public boolean actualizar(clsCategoria categoria);
    public boolean eliminar(int id);
    public boolean reactivar(int id); // ✅ NUEVO MÉTODO
}