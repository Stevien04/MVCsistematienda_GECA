package Interfaces;

import java.util.List;
import Modelo.clsProducto;

public interface CRUDProducto {
    public List<clsProducto> listar();
    public clsProducto listarPorId(int id);
    public boolean agregar(clsProducto producto);
    public boolean actualizar(clsProducto producto);
    public boolean eliminar(int id);
    public boolean activar(int id);
    
    //metodos del carrito
    List<clsProducto> listarActivos();
    boolean actualizarStock(int idProducto, int cantidad);
    boolean verificarStock(int idProducto, int cantidadRequerida);
}