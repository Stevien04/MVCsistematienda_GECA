package Interfaces;

import java.util.List;
import Modelo.clsProductoTalla;

public interface CRUDProductoTalla {
    public List<clsProductoTalla> listarPorProducto(int idProducto);
    public clsProductoTalla listarPorId(int id);
    public boolean agregar(clsProductoTalla productoTalla);
    public boolean actualizarStock(int idProductoTalla, int stock);
    public boolean eliminar(int id);
}