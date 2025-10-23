package Interfaces;

import java.util.List;
import Modelo.clsBoleta;

public interface CRUDBoleta {
    public List<clsBoleta> listar();
    public clsBoleta listarPorId(int id);
    public clsBoleta listarPorNumero(String numeroBoleta);
    public boolean agregar(clsBoleta boleta);
    public boolean actualizar(clsBoleta boleta);
    public boolean anular(int id);
    public String generarNumeroBoleta();
}