package Controlador;

import ModeloDAO.clsTipoDocumentoDAO;
import Modelo.clsTipoDocumento;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorTipoDocumento")
public class ControladorTipoDocumento extends HttpServlet {
    
    private clsTipoDocumentoDAO tipoDocumentoDAO;
    
    @Override
    public void init() throws ServletException {
        tipoDocumentoDAO = new clsTipoDocumentoDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            switch (accion) {
                case "listar":
                    listarTipoDocumentos(request, response);
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarTipoDocumento(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarTipoDocumento(request, response);
                    break;
                case "eliminar":
                    eliminarTipoDocumento(request, response);
                    break;
                default:
                    listarTipoDocumentos(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error en el controlador: " + e.getMessage(), e);
        }
    }
    
    private void listarTipoDocumentos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsTipoDocumento> listaTipoDocumentos = tipoDocumentoDAO.listar();
        request.setAttribute("tipoDocumentos", listaTipoDocumentos);
        request.getRequestDispatcher("tipodocumento/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("tipodocumento/formulario.jsp").forward(request, response);
    }
    
    private void agregarTipoDocumento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String abreviatura = request.getParameter("abreviatura");
        String descripcion = request.getParameter("descripcion");
        
        clsTipoDocumento tipoDocumento = new clsTipoDocumento();
        tipoDocumento.setNombretipodocumento(nombre);
        tipoDocumento.setAbreviatura(abreviatura);
        tipoDocumento.setDescripcion(descripcion);
        
        boolean exito = tipoDocumentoDAO.agregar(tipoDocumento);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Tipo de documento agregado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al agregar tipo de documento");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorTipoDocumento?accion=listar");
    }
    
    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        clsTipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(id);
        
        if (tipoDocumento != null) {
            request.setAttribute("tipoDocumento", tipoDocumento);
            request.getRequestDispatcher("tipodocumento/editar.jsp").forward(request, response);
        } else {
            response.sendRedirect("ControladorTipoDocumento?accion=listar");
        }
    }
    
    private void actualizarTipoDocumento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String abreviatura = request.getParameter("abreviatura");
        String descripcion = request.getParameter("descripcion");
        
        clsTipoDocumento tipoDocumento = new clsTipoDocumento();
        tipoDocumento.setIdtipodocumento(id);
        tipoDocumento.setNombretipodocumento(nombre);
        tipoDocumento.setAbreviatura(abreviatura);
        tipoDocumento.setDescripcion(descripcion);
        
        boolean exito = tipoDocumentoDAO.actualizar(tipoDocumento);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Tipo de documento actualizado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al actualizar tipo de documento");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorTipoDocumento?accion=listar");
    }
    
    private void eliminarTipoDocumento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean exito = tipoDocumentoDAO.eliminar(id);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Tipo de documento eliminado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al eliminar tipo de documento");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorTipoDocumento?accion=listar");
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
        return "Controlador para gestión de tipos de documento";
    }
}