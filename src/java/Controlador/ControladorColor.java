package Controlador;

import ModeloDAO.clsColorDAO;
import Modelo.clsColor;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorColor")
public class ControladorColor extends HttpServlet {
    
    private clsColorDAO colorDAO;
    
    @Override
    public void init() throws ServletException {
        colorDAO = new clsColorDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            switch (accion) {
                case "listar":
                    listarColores(request, response);
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarColor(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarColor(request, response);
                    break;
                case "eliminar":
                    eliminarColor(request, response);
                    break;
                default:
                    listarColores(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error en el controlador: " + e.getMessage(), e);
        }
    }
    
    private void listarColores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsColor> listaColores = colorDAO.listar();
        request.setAttribute("colores", listaColores);
        request.getRequestDispatcher("color/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("color/formulario.jsp").forward(request, response);
    }
    
    private void agregarColor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String codigoHex = request.getParameter("codigoHex");
        String descripcion = request.getParameter("descripcion");
        
        clsColor color = new clsColor();
        color.setNombrecolor(nombre);
        color.setCodigoHex(codigoHex);
        color.setDescripcion(descripcion);
        
        boolean exito = colorDAO.agregar(color);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Color agregado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al agregar color");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorColor?accion=listar");
    }
    
    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        clsColor color = colorDAO.listarPorId(id);
        
        if (color != null) {
            request.setAttribute("color", color);
            request.getRequestDispatcher("color/editar.jsp").forward(request, response);
        } else {
            response.sendRedirect("ControladorColor?accion=listar");
        }
    }
    
    private void actualizarColor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String codigoHex = request.getParameter("codigoHex");
        String descripcion = request.getParameter("descripcion");
        
        clsColor color = new clsColor();
        color.setIdcolor(id);
        color.setNombrecolor(nombre);
        color.setCodigoHex(codigoHex);
        color.setDescripcion(descripcion);
        
        boolean exito = colorDAO.actualizar(color);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Color actualizado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al actualizar color");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorColor?accion=listar");
    }
    
    private void eliminarColor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean exito = colorDAO.eliminar(id);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Color eliminado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al eliminar color");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorColor?accion=listar");
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
        return "Controlador para gestión de colores";
    }
}