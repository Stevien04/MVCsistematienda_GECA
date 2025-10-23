package Interfaces;

import java.util.List;
import Modelo.clsCargo;

public interface CRUDCargo {
    public List<clsCargo> listar();
    public clsCargo listarPorId(int id);
    public boolean agregar(clsCargo cargo);
    public boolean actualizar(clsCargo cargo);
    public boolean eliminar(int id);
    public boolean activar(int id);
}