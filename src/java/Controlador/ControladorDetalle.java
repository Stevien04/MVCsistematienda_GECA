package Controlador;

import ModeloDAO.clsDetalleDAO;
import ModeloDAO.clsBoletaDAO;
import ModeloDAO.clsProductoDAO;
import Modelo.clsDetalle;
import Modelo.clsBoleta;
import Modelo.clsProducto;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorDetalle")
public class ControladorDetalle extends HttpServlet {
    
    private clsDetalleDAO detalleDAO;
    private clsBoletaDAO boletaDAO;
    private clsProductoDAO productoDAO;
    
    @Override
    public void init() throws ServletException {
        detalleDAO = new clsDetalleDAO();
        boletaDAO = new clsBoletaDAO();
        productoDAO = new clsProductoDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            switch (accion) {
                case "listar":
                    listarDetalles(request, response);
                    break;
                case "agregar":
                    agregarDetalle(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarDetalle(request, response);
                    break;
                case "eliminar":
                    eliminarDetalle(request, response);
                    break;
                case "finalizarVenta":
                    finalizarVenta(request, response);
                    break;
                default:
                    listarDetalles(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error en el controlador: " + e.getMessage(), e);
        }
    }
    
    private void listarDetalles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idBoleta = Integer.parseInt(request.getParameter("idBoleta"));
        List<clsDetalle> listaDetalles = detalleDAO.listarPorBoleta(idBoleta);
        clsBoleta boleta = boletaDAO.listarPorId(idBoleta);
        List<clsProducto> listaProductos = productoDAO.listar();
        
        request.setAttribute("detalles", listaDetalles);
        request.setAttribute("boleta", boleta);
        request.setAttribute("productos", listaProductos);
        request.getRequestDispatcher("detalle/listar.jsp").forward(request, response);
    }
    
    private void agregarDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idBoleta = Integer.parseInt(request.getParameter("idBoleta"));
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        
        // Obtener producto para calcular importe
        clsProducto producto = productoDAO.listarPorId(idProducto);
        if (producto == null) {
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Producto no encontrado");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("ControladorDetalle?accion=listar&idBoleta=" + idBoleta);
            return;
        }
        
        BigDecimal precioUnitario = producto.getPrecio();
        BigDecimal importe = precioUnitario.multiply(new BigDecimal(cantidad));
        
        clsDetalle detalle = new clsDetalle();
        detalle.setIdboleta(idBoleta);
        detalle.setIdproducto(idProducto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setImporte(importe);
        
        boolean exito = detalleDAO.agregar(detalle);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Producto agregado al detalle");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al agregar producto al detalle");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorDetalle?accion=listar&idBoleta=" + idBoleta);
    }
    
    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        clsDetalle detalle = detalleDAO.listarPorId(id);
        List<clsProducto> listaProductos = productoDAO.listar();
        
        if (detalle != null) {
            request.setAttribute("detalle", detalle);
            request.setAttribute("productos", listaProductos);
            request.getRequestDispatcher("detalle/editar.jsp").forward(request, response);
        } else {
            response.sendRedirect("ControladorDetalle?accion=listar&idBoleta=" + detalle.getIdboleta());
        }
    }
    
    private void actualizarDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        int idBoleta = Integer.parseInt(request.getParameter("idBoleta"));
        
        // Obtener producto para calcular importe
        clsProducto producto = productoDAO.listarPorId(idProducto);
        if (producto == null) {
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Producto no encontrado");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("ControladorDetalle?accion=listar&idBoleta=" + idBoleta);
            return;
        }
        
        BigDecimal precioUnitario = producto.getPrecio();
        BigDecimal importe = precioUnitario.multiply(new BigDecimal(cantidad));
        
        clsDetalle detalle = new clsDetalle();
        detalle.setIddetalle(id);
        detalle.setIdproducto(idProducto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setImporte(importe);
        
        boolean exito = detalleDAO.actualizar(detalle);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Detalle actualizado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al actualizar detalle");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorDetalle?accion=listar&idBoleta=" + idBoleta);
    }
    
    private void eliminarDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        int idBoleta = Integer.parseInt(request.getParameter("idBoleta"));
        
        boolean exito = detalleDAO.eliminar(id);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Detalle eliminado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al eliminar detalle");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorDetalle?accion=listar&idBoleta=" + idBoleta);
    }
    
    private void finalizarVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idBoleta = Integer.parseInt(request.getParameter("idBoleta"));
        
        // Calcular totales
        List<clsDetalle> detalles = detalleDAO.listarPorBoleta(idBoleta);
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (clsDetalle detalle : detalles) {
            subtotal = subtotal.add(detalle.getImporte());
        }
        
        BigDecimal igv = subtotal.multiply(new BigDecimal("0.18")); // 18% IGV
        BigDecimal total = subtotal.add(igv);
        
        // Actualizar boleta con totales
        clsBoleta boleta = boletaDAO.listarPorId(idBoleta);
        boleta.setSubtotal(subtotal);
        boleta.setIgv(igv);
        boleta.setTotal(total);
        
        boolean exito = boletaDAO.actualizar(boleta);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Venta finalizada correctamente");
            session.setAttribute("tipoMensaje", "success");
            session.setAttribute("boletaFinalizada", boleta);
            response.sendRedirect("boleta/comprobante.jsp?idBoleta=" + idBoleta);
        } else {
            session.setAttribute("mensaje", "Error al finalizar venta");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("ControladorDetalle?accion=listar&idBoleta=" + idBoleta);
        }
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
        return "Controlador para gestión de detalles de boleta";
    }
}