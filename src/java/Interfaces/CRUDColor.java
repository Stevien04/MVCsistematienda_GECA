package Interfaces;

import java.util.List;
import Modelo.clsColor;

public interface CRUDColor {
    public List<clsColor> listar();
    public clsColor listarPorId(int id);
    public boolean agregar(clsColor color);
    public boolean actualizar(clsColor color);
    public boolean eliminar(int id);
}