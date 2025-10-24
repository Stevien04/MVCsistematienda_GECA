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
import ModeloDAO.clsDetalleDAO;
import Modelo.clsDetalle;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import Util.ExportUtil;

@WebServlet("/ControladorBoleta")
public class ControladorBoleta extends HttpServlet {

    private clsBoletaDAO boletaDAO;
    private clsClienteDAO clienteDAO;
    private clsEmpleadoDAO empleadoDAO;
    private clsDetalleDAO detalleDAO;

    @Override
    public void init() throws ServletException {
        boletaDAO = new clsBoletaDAO();
        clienteDAO = new clsClienteDAO();
        empleadoDAO = new clsEmpleadoDAO();
        detalleDAO = new clsDetalleDAO();
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
                case "exportarExcel":
                    exportarBoletasExcel(response);
                    break;
                case "exportarPdf":
                    exportarBoletasPdf(response);
                    break;
                case "exportarDetalleExcel":
                    exportarDetalleBoletaExcel(request, response);
                    break;
                case "exportarDetallePdf":
                    exportarDetalleBoletaPdf(request, response);
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
         if (listaBoletas == null) {
            listaBoletas = new ArrayList<>();
        }

        String idBoletaParam = request.getParameter("idBoleta");
        int idBoletaSeleccionada = 0;

        if (idBoletaParam != null && !idBoletaParam.trim().isEmpty()) {
            try {
                idBoletaSeleccionada = Integer.parseInt(idBoletaParam);
            } catch (NumberFormatException e) {
                idBoletaSeleccionada = 0;
            }
        }

        if (idBoletaSeleccionada == 0 && !listaBoletas.isEmpty()) {
            idBoletaSeleccionada = listaBoletas.get(0).getIdboleta();
        }

        clsBoleta boletaSeleccionada = null;
        List<clsDetalle> detallesBoleta = new ArrayList<>();

        if (idBoletaSeleccionada > 0) {
            boletaSeleccionada = boletaDAO.listarPorId(idBoletaSeleccionada);
            if (boletaSeleccionada != null) {
                List<clsDetalle> detalles = detalleDAO.listarPorBoleta(idBoletaSeleccionada);
                if (detalles != null) {
                    detallesBoleta = detalles;
                }
            }
        }

        request.setAttribute("boletas", listaBoletas);
        request.setAttribute("boletaSeleccionada", boletaSeleccionada);
        request.setAttribute("detallesBoleta", detallesBoleta);
        request.setAttribute("idBoletaSeleccionada", idBoletaSeleccionada);
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
    
    private void exportarBoletasPdf(HttpServletResponse response) throws IOException {
        List<clsBoleta> boletas = boletaDAO.listar();
        if (boletas == null) {
            boletas = new ArrayList<>();
        }

        List<String> cabeceras = Arrays.asList("ID", "Número", "Fecha", "Hora",
                "Cliente", "DNI", "Empleado", "Subtotal", "IGV", "Total", "Estado");

        DateTimeFormatter formateadorFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formateadorHora = DateTimeFormatter.ofPattern("HH:mm");
        DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

        List<List<String>> filas = new ArrayList<>();
        for (clsBoleta boleta : boletas) {
            filas.add(Arrays.asList(
                    String.valueOf(boleta.getIdboleta()),
                    safeString(boleta.getNumeroBoleta()),
                    formatFecha(boleta.getFechaEmision(), formateadorFecha),
                    formatHora(boleta.getHoraEmision(), formateadorHora),
                    safeString(boleta.getNombreCliente()),
                    safeString(boleta.getDniCliente()),
                    safeString(boleta.getNombreEmpleado()),
                    formatMoneda(boleta.getSubtotal(), formatoMoneda),
                    formatMoneda(boleta.getIgv(), formatoMoneda),
                    formatMoneda(boleta.getTotal(), formatoMoneda),
                    safeString(boleta.getEstadoBoleta())));
        }

        ExportUtil.exportToPdf(response, "boletas.pdf", "Listado de boletas", cabeceras, filas);
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

    private void exportarBoletasExcel(HttpServletResponse response) throws IOException {
        List<clsBoleta> boletas = boletaDAO.listar();
        if (boletas == null) {
            boletas = new ArrayList<>();
        }

        List<String> cabeceras = Arrays.asList("ID", "Número", "Fecha", "Hora",
                "Cliente", "DNI", "Empleado", "Subtotal", "IGV", "Total", "Estado");

        DateTimeFormatter formateadorFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formateadorHora = DateTimeFormatter.ofPattern("HH:mm");
        DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

        List<List<String>> filas = new ArrayList<>();
        for (clsBoleta boleta : boletas) {
            filas.add(Arrays.asList(
                    String.valueOf(boleta.getIdboleta()),
                    safeString(boleta.getNumeroBoleta()),
                    formatFecha(boleta.getFechaEmision(), formateadorFecha),
                    formatHora(boleta.getHoraEmision(), formateadorHora),
                    safeString(boleta.getNombreCliente()),
                    safeString(boleta.getDniCliente()),
                    safeString(boleta.getNombreEmpleado()),
                    formatMoneda(boleta.getSubtotal(), formatoMoneda),
                    formatMoneda(boleta.getIgv(), formatoMoneda),
                    formatMoneda(boleta.getTotal(), formatoMoneda),
                    safeString(boleta.getEstadoBoleta())));
        }

        ExportUtil.exportToExcel(response, "boletas.xls", cabeceras, filas);
    }
     private void exportarDetalleBoletaPdf(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        String idBoletaParam = request.getParameter("idBoleta");

        if (idBoletaParam == null || idBoletaParam.trim().isEmpty()) {
            session.setAttribute("mensaje", "Debe seleccionar una boleta para exportar su detalle");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("ControladorBoleta?accion=listar");
            return;
        }

        int idBoleta;
        try {
            idBoleta = Integer.parseInt(idBoletaParam);
        } catch (NumberFormatException e) {
            session.setAttribute("mensaje", "El identificador de la boleta no es válido");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("ControladorBoleta?accion=listar");
            return;
        }

        clsBoleta boleta = boletaDAO.listarPorId(idBoleta);
        if (boleta == null) {
            session.setAttribute("mensaje", "No se encontró la boleta seleccionada");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("ControladorBoleta?accion=listar");
            return;
        }

        List<clsDetalle> detalles = detalleDAO.listarPorBoleta(idBoleta);
        if (detalles == null) {
            detalles = new ArrayList<>();
        }

        List<String> cabeceras = Arrays.asList("ID Detalle", "Número Boleta", "Producto",
                "Cantidad", "Precio Unitario", "Importe");
        DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

        List<List<String>> filas = new ArrayList<>();
        for (clsDetalle detalle : detalles) {
            filas.add(Arrays.asList(
                    String.valueOf(detalle.getIddetalle()),
                    safeString(detalle.getNumeroBoleta()),
                    safeString(detalle.getNombreproducto()),
                    String.valueOf(detalle.getCantidad()),
                    formatMoneda(detalle.getPrecioUnitario(), formatoMoneda),
                    formatMoneda(detalle.getImporte(), formatoMoneda)));
        }

        String numeroBoleta = boleta.getNumeroBoleta() != null ? boleta.getNumeroBoleta() : "boleta";
        String titulo = "Detalle de boleta " + numeroBoleta;
        String nombreArchivo = "detalles_" + numeroBoleta.replace('-', '_') + ".pdf";

        ExportUtil.exportToPdf(response, nombreArchivo, titulo, cabeceras, filas);
    }

    private void exportarDetalleBoletaExcel(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        String idBoletaParam = request.getParameter("idBoleta");

        if (idBoletaParam == null || idBoletaParam.trim().isEmpty()) {
            session.setAttribute("mensaje", "Debe seleccionar una boleta para exportar su detalle");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("ControladorBoleta?accion=listar");
            return;
        }

        int idBoleta;
        try {
            idBoleta = Integer.parseInt(idBoletaParam);
        } catch (NumberFormatException e) {
            session.setAttribute("mensaje", "El identificador de la boleta no es válido");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("ControladorBoleta?accion=listar");
            return;
        }

        clsBoleta boleta = boletaDAO.listarPorId(idBoleta);
        if (boleta == null) {
            session.setAttribute("mensaje", "No se encontró la boleta seleccionada");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("ControladorBoleta?accion=listar");
            return;
        }

        List<clsDetalle> detalles = detalleDAO.listarPorBoleta(idBoleta);
        if (detalles == null) {
            detalles = new ArrayList<>();
        }

        List<String> cabeceras = Arrays.asList("ID Detalle", "Número Boleta", "Producto",
                "Cantidad", "Precio Unitario", "Importe");
        DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

        List<List<String>> filas = new ArrayList<>();
        for (clsDetalle detalle : detalles) {
            filas.add(Arrays.asList(
                    String.valueOf(detalle.getIddetalle()),
                    safeString(detalle.getNumeroBoleta()),
                    safeString(detalle.getNombreproducto()),
                    String.valueOf(detalle.getCantidad()),
                    formatMoneda(detalle.getPrecioUnitario(), formatoMoneda),
                    formatMoneda(detalle.getImporte(), formatoMoneda)));
        }

        String numeroBoleta = boleta.getNumeroBoleta() != null ? boleta.getNumeroBoleta().replace('-', '_') : "boleta";
        String nombreArchivo = "detalles_" + numeroBoleta + ".xls";

        ExportUtil.exportToExcel(response, nombreArchivo, cabeceras, filas);
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

    private String safeString(String value) {
        return value != null ? value : "";
    }

    private String formatFecha(LocalDate fecha, DateTimeFormatter formatter) {
        if (fecha == null) {
            return "";
        }
        return formatter.format(fecha);
    }

    private String formatHora(LocalTime hora, DateTimeFormatter formatter) {
        if (hora == null) {
            return "";
        }
        return formatter.format(hora);
    }

    private String formatMoneda(BigDecimal valor, DecimalFormat formato) {
        if (valor == null) {
            return "";
        }
        return formato.format(valor);
    }
}
