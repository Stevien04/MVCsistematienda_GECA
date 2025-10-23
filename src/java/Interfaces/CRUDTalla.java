package Interfaces;

import java.util.List;
import Modelo.clsTalla;

public interface CRUDTalla {
    public List<clsTalla> listar();
    public clsTalla listarPorId(int id);
    public List<clsTalla> listarPorTipoTalla(int idTipoTalla);
    public boolean agregar(clsTalla talla);
    public boolean actualizar(clsTalla talla);
    public boolean eliminar(int id);
}