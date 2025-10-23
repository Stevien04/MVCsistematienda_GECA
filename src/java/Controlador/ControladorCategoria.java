package Controlador;

import ModeloDAO.clsCategoriaDAO;
import Modelo.clsCategoria;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorCategoria")
public class ControladorCategoria extends HttpServlet {
    
    private clsCategoriaDAO categoriaDAO;
    
    @Override
    public void init() throws ServletException {
        categoriaDAO = new clsCategoriaDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        // Debug: imprimir la acción recibida
        System.out.println("=== CONTROLADOR CATEGORÍA - ACCIÓN: " + accion + " ===");
        
        try {
            switch (accion) {
                case "listar":
                    listarCategorias(request, response);
                    break;
                case "listarInactivas":
                    listarCategoriasInactivas(request, response);
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarCategoria(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarCategoria(request, response);
                    break;
                case "eliminar":
                    eliminarCategoria(request, response);
                    break;
                case "reactivar":
                    reactivarCategoria(request, response);
                    break;
                default:
                    listarCategorias(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error en el sistema: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorCategoria?accion=listar");
        }
    }
    
    private void listarCategorias(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("=== LISTANDO CATEGORÍAS ACTIVAS ===");
        
        List<clsCategoria> listaCategorias = categoriaDAO.listar();
        List<clsCategoria> listaCategoriasInactivas = categoriaDAO.listarInactivas();
        
        System.out.println("Categorías activas: " + listaCategorias.size());
        System.out.println("Categorías inactivas: " + listaCategoriasInactivas.size());
        
        request.setAttribute("categorias", listaCategorias);
        request.setAttribute("categoriasInactivas", listaCategoriasInactivas);
        request.setAttribute("mostrarInactivas", false);
        request.getRequestDispatcher("/categoria/listar.jsp").forward(request, response);
    }
    
    private void listarCategoriasInactivas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("=== LISTANDO CATEGORÍAS INACTIVAS ===");
        
        List<clsCategoria> listaCategorias = categoriaDAO.listar();
        List<clsCategoria> listaCategoriasInactivas = categoriaDAO.listarInactivas();
        
        System.out.println("Categorías activas: " + listaCategorias.size());
        System.out.println("Categorías inactivas: " + listaCategoriasInactivas.size());
        
        request.setAttribute("categorias", listaCategorias);
        request.setAttribute("categoriasInactivas", listaCategoriasInactivas);
        request.setAttribute("mostrarInactivas", true);
        request.getRequestDispatcher("/categoria/listar.jsp").forward(request, response);
    }
    
    private void reactivarCategoria(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            System.out.println("=== REACTIVANDO CATEGORÍA ID: " + id + " ===");
            
            boolean exito = categoriaDAO.reactivar(id);
            
            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Categoría reactivada correctamente");
                session.setAttribute("tipoMensaje", "success");
                System.out.println("Categoría reactivada exitosamente: " + id);
            } else {
                session.setAttribute("mensaje", "Error al reactivar categoría");
                session.setAttribute("tipoMensaje", "error");
                System.out.println("Error al reactivar categoría: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            System.out.println("Excepción al reactivar categoría: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorCategoria?accion=listarInactivas");
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/categoria/formulario.jsp").forward(request, response);
    }
    
    private void agregarCategoria(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            
            clsCategoria categoria = new clsCategoria();
            categoria.setNombrecategoria(nombre);
            categoria.setDescripcion(descripcion);
            
            boolean exito = categoriaDAO.agregar(categoria);
            
            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Categoría agregada correctamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al agregar categoría");
                session.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error en los datos: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorCategoria?accion=listar");
    }
    
    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            clsCategoria categoria = categoriaDAO.listarPorId(id);
            
            if (categoria != null) {
                request.setAttribute("categoria", categoria);
                request.getRequestDispatcher("/categoria/editar.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/ControladorCategoria?accion=listar");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/ControladorCategoria?accion=listar");
        }
    }
    
    private void actualizarCategoria(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            
            clsCategoria categoria = new clsCategoria();
            categoria.setIdcategoria(id);
            categoria.setNombrecategoria(nombre);
            categoria.setDescripcion(descripcion);
            
            boolean exito = categoriaDAO.actualizar(categoria);
            
            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Categoría actualizada correctamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al actualizar categoría");
                session.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error en los datos: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorCategoria?accion=listar");
    }
    
    private void eliminarCategoria(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean exito = categoriaDAO.eliminar(id);
            
            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Categoría eliminada correctamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al eliminar categoría");
                session.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorCategoria?accion=listar");
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
        return "Controlador para gestión de categorías";
    }
}