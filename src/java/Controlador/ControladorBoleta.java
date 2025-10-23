package Controlador;

import ModeloDAO.clsBoletaDAO;
import ModeloDAO.clsClienteDAO;
import ModeloDAO.clsEmpleadoDAO;
import Modelo.clsBoleta;
import Modelo.clsCliente;
import Modelo.clsEmpleado;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorBoleta")
public class ControladorBoleta extends HttpServlet {
    
    private clsBoletaDAO boletaDAO;
    private clsClienteDAO clienteDAO;
    private clsEmpleadoDAO empleadoDAO;
    
    @Override
    public void init() throws ServletException {
        boletaDAO = new clsBoletaDAO();
        clienteDAO = new clsClienteDAO();
        empleadoDAO = new clsEmpleadoDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            switch (accion) {
                case "listar":
                    listarBoletas(request, response);
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarBoleta(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarBoleta(request, response);
                    break;
                case "anular":
                    anularBoleta(request, response);
                    break;
                case "ver":
                    verBoleta(request, response);
                    break;
                default:
                    listarBoletas(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error en el controlador: " + e.getMessage(), e);
        }
    }
    
    private void listarBoletas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsBoleta> listaBoletas = boletaDAO.listar();
        request.setAttribute("boletas", listaBoletas);
        request.getRequestDispatcher("boleta/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsCliente> listaClientes = clienteDAO.listar();
        String numeroBoleta = boletaDAO.generarNumeroBoleta();
        
        request.setAttribute("clientes", listaClientes);
        request.setAttribute("numeroBoleta", numeroBoleta);
        request.getRequestDispatcher("boleta/formulario.jsp").forward(request, response);
    }
    
    private void agregarBoleta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String numeroBoleta = request.getParameter("numeroBoleta");
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        BigDecimal subtotal = new BigDecimal(request.getParameter("subtotal"));
        BigDecimal igv = new BigDecimal(request.getParameter("igv"));
        BigDecimal total = new BigDecimal(request.getParameter("total"));
        
        // Obtener el empleado de la sesión
        HttpSession session = request.getSession();
        clsEmpleado empleado = (clsEmpleado) session.getAttribute("empleado");
        
        if (empleado == null) {
            session.setAttribute("mensaje", "Debe iniciar sesión para generar boletas");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("ControladorBoleta?accion=listar");
            return;
        }
        
        clsBoleta boleta = new clsBoleta();
        boleta.setNumeroBoleta(numeroBoleta);
        boleta.setFechaEmision(LocalDate.now());
        boleta.setHoraEmision(LocalTime.now());
        boleta.setSubtotal(subtotal);
        boleta.setIgv(igv);
        boleta.setTotal(total);
        boleta.setEstadoBoleta("ACTIVA");
        boleta.setIdcliente(idCliente);
        boleta.setIdempleado(empleado.getIdempleado());
        
        boolean exito = boletaDAO.agregar(boleta);
        
        if (exito) {
            session.setAttribute("mensaje", "Boleta generada correctamente");
            session.setAttribute("tipoMensaje", "success");
            session.setAttribute("boletaGenerada", boleta);
            response.sendRedirect("ControladorDetalle?accion=agregar&idBoleta=" + boleta.getIdboleta());
        } else {
            session.setAttribute("mensaje", "Error al generar boleta");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("ControladorBoleta?accion=listar");
        }
    }
    
    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        clsBoleta boleta = boletaDAO.listarPorId(id);
        List<clsCliente> listaClientes = clienteDAO.listar();
        
        if (boleta != null) {
            request.setAttribute("boleta", boleta);
            request.setAttribute("clientes", listaClientes);
            request.getRequestDispatcher("boleta/editar.jsp").forward(request, response);
        } else {
            response.sendRedirect("ControladorBoleta?accion=listar");
        }
    }
    
    private void actualizarBoleta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String numeroBoleta = request.getParameter("numeroBoleta");
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        BigDecimal subtotal = new BigDecimal(request.getParameter("subtotal"));
        BigDecimal igv = new BigDecimal(request.getParameter("igv"));
        BigDecimal total = new BigDecimal(request.getParameter("total"));
        String estadoBoleta = request.getParameter("estadoBoleta");
        
        clsBoleta boleta = new clsBoleta();
        boleta.setIdboleta(id);
        boleta.setNumeroBoleta(numeroBoleta);
        boleta.setSubtotal(subtotal);
        boleta.setIgv(igv);
        boleta.setTotal(total);
        boleta.setEstadoBoleta(estadoBoleta);
        boleta.setIdcliente(idCliente);
        
        boolean exito = boletaDAO.actualizar(boleta);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Boleta actualizada correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al actualizar boleta");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorBoleta?accion=listar");
    }
    
    private void anularBoleta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean exito = boletaDAO.anular(id);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Boleta anulada correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al anular boleta");
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ControladorBoleta?accion=listar");
    }
    
    private void verBoleta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        clsBoleta boleta = boletaDAO.listarPorId(id);
        
        if (boleta != null) {
            request.setAttribute("boleta", boleta);
            request.getRequestDispatcher("boleta/detalle.jsp").forward(request, response);
        } else {
            response.sendRedirect("ControladorBoleta?accion=listar");
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
        return "Controlador para gestión de boletas";
    }
}