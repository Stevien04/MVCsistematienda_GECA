package Controlador;

import ModeloDAO.clsTallaDAO;
import ModeloDAO.clsTipoTallaDAO;
import Modelo.clsTalla;
import Modelo.clsTipoTalla;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorTalla")
public class ControladorTalla extends HttpServlet {
    
    private clsTallaDAO tallaDAO;
    private clsTipoTallaDAO tipoTallaDAO;
    
    @Override
    public void init() throws ServletException {
        tallaDAO = new clsTallaDAO();
        tipoTallaDAO = new clsTipoTallaDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            switch (accion) {
                case "listar":
                    listarTallas(request, response);
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarTalla(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarTalla(request, response);
                    break;
                case "eliminar":
                    eliminarTalla(request, response);
                    break;
                default:
                    listarTallas(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error en el controlador: " + e.getMessage(), e);
        }
    }
    
    private void listarTallas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsTalla> listaTallas = tallaDAO.listar();
        request.setAttribute("tallas", listaTallas);
        request.getRequestDispatcher("talla/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsTipoTalla> listaTipoTallas = tipoTallaDAO.listar();
        request.setAttribute("tipoTallas", listaTipoTallas);
        request.getRequestDispatcher("talla/formulario.jsp").forward(request, response);
    }
    
    private void agregarTalla(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idTipoTalla = Integer.parseInt(request.getParameter("idTipoTalla"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        
        clsTalla talla = new clsTalla();
        talla.setIdtipotalla(idTipoTalla);
        talla.setNombretalla(nombre);
        talla.setDescripcion(descripcion);
        
        boolean exito = tallaDAO.agregar(talla);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Talla agregada correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al agregar talla");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorTalla?accion=listar");
    }
    
    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        clsTalla talla = tallaDAO.listarPorId(id);
        List<clsTipoTalla> listaTipoTallas = tipoTallaDAO.listar();
        
        if (talla != null) {
            request.setAttribute("talla", talla);
            request.setAttribute("tipoTallas", listaTipoTallas);
            request.getRequestDispatcher("talla/editar.jsp").forward(request, response);
        } else {
            response.sendRedirect("ControladorTalla?accion=listar");
        }
    }
    
    private void actualizarTalla(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        int idTipoTalla = Integer.parseInt(request.getParameter("idTipoTalla"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        
        clsTalla talla = new clsTalla();
        talla.setIdtalla(id);
        talla.setIdtipotalla(idTipoTalla);
        talla.setNombretalla(nombre);
        talla.setDescripcion(descripcion);
        
        boolean exito = tallaDAO.actualizar(talla);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Talla actualizada correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al actualizar talla");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorTalla?accion=listar");
    }
    
    private void eliminarTalla(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean exito = tallaDAO.eliminar(id);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Talla eliminada correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al eliminar talla");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorTalla?accion=listar");
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
        return "Controlador para gestión de tallas";
    }
}