package Controlador;

import ModeloDAO.clsCargoDAO;
import Modelo.clsCargo;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorCargo")
public class ControladorCargo extends HttpServlet {
    
    private clsCargoDAO cargoDAO;
    
    @Override
    public void init() throws ServletException {
        cargoDAO = new clsCargoDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            switch (accion) {
                case "listar":
                    listarCargos(request, response);
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarCargo(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarCargo(request, response);
                    break;
                case "eliminar":
                    eliminarCargo(request, response);
                    break;
                default:
                    listarCargos(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error en el controlador: " + e.getMessage(), e);
        }
    }
    
    private void listarCargos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsCargo> listaCargos = cargoDAO.listar();
        request.setAttribute("cargos", listaCargos);
        request.getRequestDispatcher("cargo/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("cargo/formulario.jsp").forward(request, response);
    }
    
    private void agregarCargo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        
        clsCargo cargo = new clsCargo();
        cargo.setNombrecargo(nombre);
        cargo.setDescripcion(descripcion);
        
        boolean exito = cargoDAO.agregar(cargo);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Cargo agregado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al agregar cargo");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorCargo?accion=listar");
    }
    
    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        clsCargo cargo = cargoDAO.listarPorId(id);
        
        if (cargo != null) {
            request.setAttribute("cargo", cargo);
            request.getRequestDispatcher("cargo/editar.jsp").forward(request, response);
        } else {
            response.sendRedirect("ControladorCargo?accion=listar");
        }
    }
    
    private void actualizarCargo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        
        clsCargo cargo = new clsCargo();
        cargo.setIdcargo(id);
        cargo.setNombrecargo(nombre);
        cargo.setDescripcion(descripcion);
        
        boolean exito = cargoDAO.actualizar(cargo);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Cargo actualizado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al actualizar cargo");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorCargo?accion=listar");
    }
    
    private void eliminarCargo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean exito = cargoDAO.eliminar(id);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Cargo eliminado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al eliminar cargo");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorCargo?accion=listar");
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
        return "Controlador para gestión de cargos";
    }
}