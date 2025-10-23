package Interfaces;

import java.util.List;
import Modelo.clsTipoDocumento;

public interface CRUDTipoDocumento {
    public List<clsTipoDocumento> listar();
    public clsTipoDocumento listarPorId(int id);
    public boolean agregar(clsTipoDocumento tipoDocumento);
    public boolean actualizar(clsTipoDocumento tipoDocumento);
    public boolean eliminar(int id);
}