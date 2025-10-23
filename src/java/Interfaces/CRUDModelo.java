package Interfaces;

import java.util.List;
import Modelo.clsModelo;

public interface CRUDModelo {
    public List<clsModelo> listar();
    public clsModelo listarPorId(int id);
    public List<clsModelo> listarPorMarca(int idMarca);
    public boolean agregar(clsModelo modelo);
    public boolean actualizar(clsModelo modelo);
    public boolean eliminar(int id);
    public boolean activar(int id);
}