package Controlador;

import ModeloDAO.clsTipoTallaDAO;
import Modelo.clsTipoTalla;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorTipoTalla")
public class ControladorTipoTalla extends HttpServlet {
    
    private clsTipoTallaDAO tipoTallaDAO;
    
    @Override
    public void init() throws ServletException {
        tipoTallaDAO = new clsTipoTallaDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            switch (accion) {
                case "listar":
                    listarTipoTallas(request, response);
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarTipoTalla(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarTipoTalla(request, response);
                    break;
                case "eliminar":
                    eliminarTipoTalla(request, response);
                    break;
                default:
                    listarTipoTallas(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error en el controlador: " + e.getMessage(), e);
        }
    }
    
    private void listarTipoTallas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsTipoTalla> listaTipoTallas = tipoTallaDAO.listar();
        request.setAttribute("tipoTallas", listaTipoTallas);
        request.getRequestDispatcher("tipotalla/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("tipotalla/formulario.jsp").forward(request, response);
    }
    
    private void agregarTipoTalla(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        
        clsTipoTalla tipoTalla = new clsTipoTalla();
        tipoTalla.setNombretipotalla(nombre);
        tipoTalla.setDescripcion(descripcion);
        
        boolean exito = tipoTallaDAO.agregar(tipoTalla);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Tipo de talla agregado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al agregar tipo de talla");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorTipoTalla?accion=listar");
    }
    
    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        clsTipoTalla tipoTalla = tipoTallaDAO.listarPorId(id);
        
        if (tipoTalla != null) {
            request.setAttribute("tipoTalla", tipoTalla);
            request.getRequestDispatcher("tipotalla/editar.jsp").forward(request, response);
        } else {
            response.sendRedirect("ControladorTipoTalla?accion=listar");
        }
    }
    
    private void actualizarTipoTalla(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        
        clsTipoTalla tipoTalla = new clsTipoTalla();
        tipoTalla.setIdtipotalla(id);
        tipoTalla.setNombretipotalla(nombre);
        tipoTalla.setDescripcion(descripcion);
        
        boolean exito = tipoTallaDAO.actualizar(tipoTalla);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Tipo de talla actualizado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al actualizar tipo de talla");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorTipoTalla?accion=listar");
    }
    
    private void eliminarTipoTalla(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean exito = tipoTallaDAO.eliminar(id);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Tipo de talla eliminado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al eliminar tipo de talla");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorTipoTalla?accion=listar");
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
        return "Controlador para gestión de tipos de talla";
    }
}