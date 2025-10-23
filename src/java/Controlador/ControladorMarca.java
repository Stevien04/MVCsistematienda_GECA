package Controlador;

import ModeloDAO.clsMarcaDAO;
import Modelo.clsMarca;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorMarca")
public class ControladorMarca extends HttpServlet {
    
    private clsMarcaDAO marcaDAO;
    
    @Override
    public void init() throws ServletException {
        marcaDAO = new clsMarcaDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            switch (accion) {
                case "listar":
                    listarMarcas(request, response);
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarMarca(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarMarca(request, response);
                    break;
                case "eliminar":
                    eliminarMarca(request, response);
                    break;
                default:
                    listarMarcas(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error en el controlador: " + e.getMessage(), e);
        }
    }
    
    private void listarMarcas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsMarca> listaMarcas = marcaDAO.listar();
        request.setAttribute("marcas", listaMarcas);
        request.getRequestDispatcher("marca/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("marca/formulario.jsp").forward(request, response);
    }
    
    private void agregarMarca(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        
        clsMarca marca = new clsMarca();
        marca.setNombremarca(nombre);
        marca.setDescripcion(descripcion);
        
        boolean exito = marcaDAO.agregar(marca);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Marca agregada correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al agregar marca");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorMarca?accion=listar");
    }
    
    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        clsMarca marca = marcaDAO.listarPorId(id);
        
        if (marca != null) {
            request.setAttribute("marca", marca);
            request.getRequestDispatcher("marca/editar.jsp").forward(request, response);
        } else {
            response.sendRedirect("ControladorMarca?accion=listar");
        }
    }
    
    private void actualizarMarca(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        
        clsMarca marca = new clsMarca();
        marca.setIdmarca(id);
        marca.setNombremarca(nombre);
        marca.setDescripcion(descripcion);
        
        boolean exito = marcaDAO.actualizar(marca);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Marca actualizada correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al actualizar marca");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorMarca?accion=listar");
    }
    
    private void eliminarMarca(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean exito = marcaDAO.eliminar(id);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Marca eliminada correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al eliminar marca");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorMarca?accion=listar");
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
        return "Controlador para gestión de marcas";
    }
}