package Controlador;

import ModeloDAO.clsModeloDAO;
import ModeloDAO.clsMarcaDAO;
import Modelo.clsModelo;
import Modelo.clsMarca;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorModelo")
public class ControladorModelo extends HttpServlet {
    
    private clsModeloDAO modeloDAO;
    private clsMarcaDAO marcaDAO;
    
    @Override
    public void init() throws ServletException {
        modeloDAO = new clsModeloDAO();
        marcaDAO = new clsMarcaDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            switch (accion) {
                case "listar":
                    listarModelos(request, response);
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarModelo(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarModelo(request, response);
                    break;
                case "eliminar":
                    eliminarModelo(request, response);
                    break;
                default:
                    listarModelos(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error en el controlador: " + e.getMessage(), e);
        }
    }
    
    private void listarModelos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsModelo> listaModelos = modeloDAO.listar();
        request.setAttribute("modelos", listaModelos);
        request.getRequestDispatcher("modelo/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsMarca> listaMarcas = marcaDAO.listar();
        request.setAttribute("marcas", listaMarcas);
        request.getRequestDispatcher("modelo/formulario.jsp").forward(request, response);
    }
    
    private void agregarModelo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idMarca = Integer.parseInt(request.getParameter("idMarca"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        
        clsModelo modelo = new clsModelo();
        modelo.setIdmarca(idMarca);
        modelo.setNombremodelo(nombre);
        modelo.setDescripcion(descripcion);
        
        boolean exito = modeloDAO.agregar(modelo);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Modelo agregado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al agregar modelo");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorModelo?accion=listar");
    }
    
    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        clsModelo modelo = modeloDAO.listarPorId(id);
        List<clsMarca> listaMarcas = marcaDAO.listar();
        
        if (modelo != null) {
            request.setAttribute("modelo", modelo);
            request.setAttribute("marcas", listaMarcas);
            request.getRequestDispatcher("modelo/editar.jsp").forward(request, response);
        } else {
            response.sendRedirect("ControladorModelo?accion=listar");
        }
    }
    
    private void actualizarModelo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        int idMarca = Integer.parseInt(request.getParameter("idMarca"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        
        clsModelo modelo = new clsModelo();
        modelo.setIdmodelo(id);
        modelo.setIdmarca(idMarca);
        modelo.setNombremodelo(nombre);
        modelo.setDescripcion(descripcion);
        
        boolean exito = modeloDAO.actualizar(modelo);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Modelo actualizado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al actualizar modelo");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorModelo?accion=listar");
    }
    
    private void eliminarModelo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean exito = modeloDAO.eliminar(id);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Modelo eliminado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al eliminar modelo");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorModelo?accion=listar");
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
        return "Controlador para gestión de modelos";
    }
}