package Interfaces;

import java.util.List;
import Modelo.clsDetalle;

public interface CRUDDetalle {
    public List<clsDetalle> listarPorBoleta(int idBoleta);
    public clsDetalle listarPorId(int id);
    public boolean agregar(clsDetalle detalle);
    public boolean actualizar(clsDetalle detalle);
    public boolean eliminar(int id);
    public boolean eliminarPorBoleta(int idBoleta);
}