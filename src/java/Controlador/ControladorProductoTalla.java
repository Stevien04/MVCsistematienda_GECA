package Controlador;

import ModeloDAO.clsProductoTallaDAO;
import ModeloDAO.clsProductoDAO;
import ModeloDAO.clsTallaDAO;
import Modelo.clsProductoTalla;
import Modelo.clsProducto;
import Modelo.clsTalla;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorProductoTalla")
public class ControladorProductoTalla extends HttpServlet {
    
    private clsProductoTallaDAO productoTallaDAO;
    private clsProductoDAO productoDAO;
    private clsTallaDAO tallaDAO;
    
    @Override
    public void init() throws ServletException {
        productoTallaDAO = new clsProductoTallaDAO();
        productoDAO = new clsProductoDAO();
        tallaDAO = new clsTallaDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            switch (accion) {
                case "listar":
                    listarProductoTallas(request, response);
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarProductoTalla(request, response);
                    break;
                case "actualizarStock":
                    actualizarStock(request, response);
                    break;
                case "eliminar":
                    eliminarProductoTalla(request, response);
                    break;
                default:
                    listarProductoTallas(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error en el controlador: " + e.getMessage(), e);
        }
    }
    
    private void listarProductoTallas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        List<clsProductoTalla> listaProductoTallas = productoTallaDAO.listarPorProducto(idProducto);
        clsProducto producto = productoDAO.listarPorId(idProducto);
        
        request.setAttribute("productoTallas", listaProductoTallas);
        request.setAttribute("producto", producto);
        request.getRequestDispatcher("productotalla/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        List<clsTalla> listaTallas = tallaDAO.listar();
        clsProducto producto = productoDAO.listarPorId(idProducto);
        
        request.setAttribute("tallas", listaTallas);
        request.setAttribute("producto", producto);
        request.getRequestDispatcher("productotalla/formulario.jsp").forward(request, response);
    }
    
    private void agregarProductoTalla(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        int idTalla = Integer.parseInt(request.getParameter("idTalla"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        
        clsProductoTalla productoTalla = new clsProductoTalla();
        productoTalla.setIdproducto(idProducto);
        productoTalla.setIdtalla(idTalla);
        productoTalla.setStock(stock);
        
        boolean exito = productoTallaDAO.agregar(productoTalla);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Talla agregada al producto correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al agregar talla al producto");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorProductoTalla?accion=listar&idProducto=" + idProducto);
    }
    
    private void actualizarStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idProductoTalla = Integer.parseInt(request.getParameter("id"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        
        boolean exito = productoTallaDAO.actualizarStock(idProductoTalla, stock);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Stock actualizado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al actualizar stock");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorProductoTalla?accion=listar&idProducto=" + idProducto);
    }
    
    private void eliminarProductoTalla(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idProductoTalla = Integer.parseInt(request.getParameter("id"));
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        
        boolean exito = productoTallaDAO.eliminar(idProductoTalla);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Talla eliminada del producto correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al eliminar talla del producto");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorProductoTalla?accion=listar&idProducto=" + idProducto);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controlador para gestión de producto-tallas";
    }
}