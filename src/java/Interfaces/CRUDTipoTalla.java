package Interfaces;

import java.util.List;
import Modelo.clsTipoTalla;

public interface CRUDTipoTalla {
    public List<clsTipoTalla> listar();
    public clsTipoTalla listarPorId(int id);
    public boolean agregar(clsTipoTalla tipoTalla);
    public boolean actualizar(clsTipoTalla tipoTalla);
    public boolean eliminar(int id);
}