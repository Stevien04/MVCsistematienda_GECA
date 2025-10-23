package Controlador;

import ModeloDAO.clsProductoDAO;
import ModeloDAO.clsCategoriaDAO;
import ModeloDAO.clsModeloDAO;
import ModeloDAO.clsColorDAO;
import Modelo.clsProducto;
import Modelo.clsCategoria;
import Modelo.clsModelo;
import Modelo.clsColor;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorProducto")
public class ControladorProducto extends HttpServlet {
    
    private clsProductoDAO productoDAO;
    private clsCategoriaDAO categoriaDAO;
    private clsModeloDAO modeloDAO;
    private clsColorDAO colorDAO;
    
    @Override
    public void init() throws ServletException {
        productoDAO = new clsProductoDAO();
        categoriaDAO = new clsCategoriaDAO();
        modeloDAO = new clsModeloDAO();
        colorDAO = new clsColorDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            switch (accion) {
                case "listar":
                    listarProductos(request, response);
                    break;
                case "listarInactivos":
                    listarProductosInactivos(request, response); // ✅ CORREGIDO: era listarClientesInactivos
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarProducto(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarProducto(request, response);
                    break;
                case "eliminar":
                    eliminarProducto(request, response);
                    break;
                case "reactivar": // ✅ NUEVO: falta este caso
                    reactivarProducto(request, response);
                    break;
                default:
                    listarProductos(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error en el controlador: " + e.getMessage(), e);
        }
    }
    
    // ✅ MÉTODOS ACTUALIZADOS PARA PESTAÑAS
    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsProducto> listaProductos = productoDAO.listar();
        List<clsProducto> listaProductosInactivos = productoDAO.listarInactivos(); // Cargar ambos
        
        request.setAttribute("productos", listaProductos);
        request.setAttribute("productosInactivos", listaProductosInactivos);
        request.setAttribute("mostrarInactivos", false);
        request.getRequestDispatcher("/producto/listar.jsp").forward(request, response); // ✅ Ruta absoluta
    }
    
    private void listarProductosInactivos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== LISTAR PRODUCTOS INACTIVOS - INICIANDO ===");

        List<clsProducto> listaProductos = productoDAO.listar();
        List<clsProducto> listaProductosInactivos = productoDAO.listarInactivos();

        System.out.println("Productos activos: " + listaProductos.size());
        System.out.println("Productos inactivos: " + listaProductosInactivos.size());

        request.setAttribute("productos", listaProductos);
        request.setAttribute("productosInactivos", listaProductosInactivos);
        request.setAttribute("mostrarInactivos", true);
        request.getRequestDispatcher("/producto/listar.jsp").forward(request, response);
    }
    
    private void reactivarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            boolean exito = productoDAO.activar(id);
            
            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Producto reactivado correctamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al reactivar producto");
                session.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorProducto?accion=listarInactivos");
    }
    
    // ✅ MÉTODOS ACTUALIZADOS CON RUTAS ABSOLUTAS
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsCategoria> listaCategorias = categoriaDAO.listar();
        List<clsModelo> listaModelos = modeloDAO.listar();
        List<clsColor> listaColores = colorDAO.listar();
        
        request.setAttribute("categorias", listaCategorias);
        request.setAttribute("modelos", listaModelos);
        request.setAttribute("colores", listaColores);
        request.getRequestDispatcher("/producto/formulario.jsp").forward(request, response); // ✅ Ruta absoluta
    }
    
    private void agregarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int idModelo = Integer.parseInt(request.getParameter("idModelo"));
            int idColor = Integer.parseInt(request.getParameter("idColor"));
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            BigDecimal precio = new BigDecimal(request.getParameter("precio"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            
            clsProducto producto = new clsProducto();
            producto.setIdcategoria(idCategoria);
            producto.setIdmodelo(idModelo);
            producto.setIdcolor(idColor);
            producto.setNombreproducto(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);
            
            boolean exito = productoDAO.agregar(producto);
            
            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Producto agregado correctamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al agregar producto");
                session.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error en los datos: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorProducto?accion=listar"); // ✅ Ruta absoluta
    }
    
    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            clsProducto producto = productoDAO.listarPorId(id);
            List<clsCategoria> listaCategorias = categoriaDAO.listar();
            List<clsModelo> listaModelos = modeloDAO.listar();
            List<clsColor> listaColores = colorDAO.listar();
            
            if (producto != null) {
                request.setAttribute("producto", producto);
                request.setAttribute("categorias", listaCategorias);
                request.setAttribute("modelos", listaModelos);
                request.setAttribute("colores", listaColores);
                request.getRequestDispatcher("/producto/editar.jsp").forward(request, response); // ✅ Ruta absoluta
            } else {
                response.sendRedirect(request.getContextPath() + "/ControladorProducto?accion=listar"); // ✅ Ruta absoluta
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/ControladorProducto?accion=listar");
        }
    }
    
    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int idModelo = Integer.parseInt(request.getParameter("idModelo"));
            int idColor = Integer.parseInt(request.getParameter("idColor"));
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            BigDecimal precio = new BigDecimal(request.getParameter("precio"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            
            clsProducto producto = new clsProducto();
            producto.setIdproducto(id);
            producto.setIdcategoria(idCategoria);
            producto.setIdmodelo(idModelo);
            producto.setIdcolor(idColor);
            producto.setNombreproducto(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);
            
            boolean exito = productoDAO.actualizar(producto);
            
            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Producto actualizado correctamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al actualizar producto");
                session.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error en los datos: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorProducto?accion=listar"); // ✅ Ruta absoluta
    }
    
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean exito = productoDAO.eliminar(id);
            
            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Producto eliminado correctamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al eliminar producto");
                session.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorProducto?accion=listar"); // ✅ Ruta absoluta
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
        return "Controlador para gestión de productos";
    }
}