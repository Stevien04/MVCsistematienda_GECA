package Interfaces;

import java.util.List;
import Modelo.clsMarca;

public interface CRUDMarca {
    public List<clsMarca> listar();
    public clsMarca listarPorId(int id);
    public boolean agregar(clsMarca marca);
    public boolean actualizar(clsMarca marca);
    public boolean eliminar(int id);
    public boolean activar(int id);
}