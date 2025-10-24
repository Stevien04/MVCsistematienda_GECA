package Controlador;

import ModeloDAO.clsProductoDAO;
import Modelo.clsProducto;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import Util.ExportUtil;

@WebServlet("/ControladorTienda")
public class ControladorTienda extends HttpServlet {

    private clsProductoDAO productoDAO;

    @Override
    public void init() throws ServletException {
        productoDAO = new clsProductoDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String accion = request.getParameter("accion");

        if (accion == null) {
            accion = "listarProductos";
        }

        try {
            switch (accion) {
                case "listarProductos":
                    listarProductos(request, response);
                    break;
                case "agregarAlCarrito":
                    agregarAlCarrito(request, response);
                    break;
                case "verCarrito":
                    verCarrito(request, response);
                    break;
                case "actualizarCarrito":
                    actualizarCarrito(request, response);
                    break;
                case "eliminarDelCarrito":
                    eliminarDelCarrito(request, response);
                    break;
                case "vaciarCarrito":
                    vaciarCarrito(request, response);
                    break;
                case "checkout":
                    mostrarCheckout(request, response);
                    break;
                case "procesarPago":
                    procesarPago(request, response);
                    break;
                case "exito":
                    mostrarExito(request, response);
                    break;
                case "exportarCarritoExcel":
                    exportarCarritoExcel(request, response);
                    break;
                case "exportarComprobantePdf":
                    exportarComprobantePdf(request, response);
                    break;
                default:
                    listarProductos(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=listarProductos");
        }
    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<clsProducto> productos = productoDAO.listarActivos();
        request.setAttribute("productos", productos);
        request.getRequestDispatcher("/tienda/tienda.jsp").forward(request, response);
    }

    // Métodos auxiliares para manejar el carrito en sesión
    private Map<Integer, Integer> obtenerCarrito(HttpSession session) {
        Map<Integer, Integer> carrito = (Map<Integer, Integer>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new HashMap<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    private BigDecimal calcularTotal(Map<Integer, Integer> carrito) {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
            clsProducto producto = productoDAO.listarPorId(entry.getKey());
            if (producto != null) {
                total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(entry.getValue())));
            }
        }
        return total;
    }

    private int getTotalItems(Map<Integer, Integer> carrito) {
        return carrito.values().stream().mapToInt(Integer::intValue).sum();
    }

    private void agregarAlCarrito(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> carrito = obtenerCarrito(session);

        int idProducto = Integer.parseInt(request.getParameter("id"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));

        clsProducto producto = productoDAO.listarPorId(idProducto);
        if (producto != null && producto.getStock() >= cantidad) {
            // Sumar a la cantidad existente
            carrito.put(idProducto, carrito.getOrDefault(idProducto, 0) + cantidad);
            session.setAttribute("mensaje", "✓ Producto agregado al carrito");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "✗ No hay suficiente stock disponible");
            session.setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=listarProductos");
    }

    private void verCarrito(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> carrito = obtenerCarrito(session);

        // Crear lista de productos del carrito con cantidades
        List<Map<String, Object>> itemsCarrito = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
            clsProducto producto = productoDAO.listarPorId(entry.getKey());
            if (producto != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("producto", producto);
                item.put("cantidad", entry.getValue());
                item.put("subtotal", producto.getPrecio().multiply(BigDecimal.valueOf(entry.getValue())));
                itemsCarrito.add(item);
            }
        }

        request.setAttribute("itemsCarrito", itemsCarrito);
        request.setAttribute("total", calcularTotal(carrito));
        request.getRequestDispatcher("/tienda/carrito.jsp").forward(request, response);
    }

    private void actualizarCarrito(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> carrito = obtenerCarrito(session);

        int idProducto = Integer.parseInt(request.getParameter("id"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));

        if (cantidad <= 0) {
            carrito.remove(idProducto);
        } else {
            carrito.put(idProducto, cantidad);
        }

        response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=verCarrito");
    }

    private void eliminarDelCarrito(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> carrito = obtenerCarrito(session);

        int idProducto = Integer.parseInt(request.getParameter("id"));
        carrito.remove(idProducto);

        session.setAttribute("mensaje", "✓ Producto eliminado del carrito");
        session.setAttribute("tipoMensaje", "success");

        response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=verCarrito");
    }

    private void vaciarCarrito(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> carrito = obtenerCarrito(session);
        carrito.clear();

        session.setAttribute("mensaje", "✓ Carrito vaciado");
        session.setAttribute("tipoMensaje", "success");

        response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=listarProductos");
    }

    private void exportarCarritoExcel(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> carrito = obtenerCarrito(session);

        if (carrito.isEmpty()) {
            session.setAttribute("mensaje", "✗ No hay productos en el carrito para exportar");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=verCarrito");
            return;
        }

        List<Map<String, Object>> itemsExportacion = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
            clsProducto producto = productoDAO.listarPorId(entry.getKey());
            if (producto == null) {
                continue;
            }

            int cantidad = entry.getValue();
            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
            Map<String, Object> item = new HashMap<>();
            item.put("producto", producto);
            item.put("cantidad", cantidad);
            item.put("subtotal", subtotal);
            itemsExportacion.add(item);
            total = total.add(subtotal);
        }

        if (itemsExportacion.isEmpty()) {
            session.setAttribute("mensaje", "✗ No se encontraron productos válidos para exportar");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=verCarrito");
            return;
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        response.setHeader("Content-Disposition", "attachment; filename=\"carrito_" + timestamp + ".xls\"");

        try (PrintWriter out = response.getWriter()) {
            out.println("<table border='1'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th colspan='6'>Resumen del carrito</th>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Producto</th>");
            out.println("<th>Marca</th>");
            out.println("<th>Cantidad</th>");
            out.println("<th>Precio Unitario (S/.)</th>");
            out.println("<th>Subtotal (S/.)</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");

            for (Map<String, Object> item : itemsExportacion) {
                clsProducto producto = (clsProducto) item.get("producto");
                int cantidad = (int) item.get("cantidad");
                BigDecimal subtotal = (BigDecimal) item.get("subtotal");
                out.println("<tr>");
                out.println("<td>" + producto.getIdproducto() + "</td>");
                out.println("<td>" + escapeHtml(producto.getNombreproducto()) + "</td>");
                out.println("<td>" + escapeHtml(Optional.ofNullable(producto.getNombremarca()).orElse("-")) + "</td>");
                out.println("<td>" + cantidad + "</td>");
                out.println("<td>" + formatCurrency(producto.getPrecio()) + "</td>");
                out.println("<td>" + formatCurrency(subtotal) + "</td>");
                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("<tfoot>");
            out.println("<tr>");
            out.println("<th colspan='5' style='text-align:right;'>Total</th>");
            out.println("<th>" + formatCurrency(total) + "</th>");
            out.println("</tr>");
            out.println("</tfoot>");
            out.println("</table>");
        }
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String formatCurrency(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private void mostrarCheckout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> carrito = obtenerCarrito(session);

        if (carrito.isEmpty()) {
            session.setAttribute("mensaje", "El carrito está vacío");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=listarProductos");
            return;
        }

        request.setAttribute("total", calcularTotal(carrito));
        request.getRequestDispatcher("/tienda/checkout.jsp").forward(request, response);
    }

    private void procesarPago(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> carrito = obtenerCarrito(session);

        // 1. Verificar stock antes del pago
        for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
            clsProducto producto = productoDAO.listarPorId(entry.getKey());
            if (producto == null || producto.getStock() < entry.getValue()) {
                session.setAttribute("mensaje", "✗ El producto '" + producto.getNombreproducto() + "' ya no tiene stock suficiente");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=verCarrito");
                return;
            }
        }

        // 2. Simular pago PayPal
        String idTransaccion = "PAYPAL_TXN_" + System.currentTimeMillis();

        // 3. Actualizar stock en BD
        for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
            productoDAO.actualizarStock(entry.getKey(), entry.getValue());
        }

        // 4. Guardar datos para comprobante
        session.setAttribute("idTransaccion", idTransaccion);
        session.setAttribute("totalPedido", calcularTotal(carrito));
        session.setAttribute("itemsPedido", new HashMap<>(carrito)); // Copia del carrito

        // 5. Vaciar carrito
        carrito.clear();

        response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=exito");
    }

    private void mostrarExito(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String idTransaccion = (String) session.getAttribute("idTransaccion");
        BigDecimal totalPedido = (BigDecimal) session.getAttribute("totalPedido");

        if (idTransaccion == null) {
            response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=listarProductos");
            return;
        }

        // Recuperar items del pedido para el comprobante
        Map<Integer, Integer> itemsPedido = (Map<Integer, Integer>) session.getAttribute("itemsPedido");
        List<Map<String, Object>> itemsComprobante = new ArrayList<>();

        if (itemsPedido != null) {
            for (Map.Entry<Integer, Integer> entry : itemsPedido.entrySet()) {
                clsProducto producto = productoDAO.listarPorId(entry.getKey());
                if (producto != null) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("producto", producto);
                    item.put("cantidad", entry.getValue());
                    item.put("subtotal", producto.getPrecio().multiply(BigDecimal.valueOf(entry.getValue())));
                    itemsComprobante.add(item);
                }
            }
        }

        request.setAttribute("idTransaccion", idTransaccion);
        request.setAttribute("totalPedido", totalPedido);
        request.setAttribute("itemsComprobante", itemsComprobante);
        
         Map<String, Object> comprobanteData = new HashMap<>();
        comprobanteData.put("idTransaccion", idTransaccion);
        comprobanteData.put("totalPedido", totalPedido);
        comprobanteData.put("items", new ArrayList<>(itemsComprobante));
        comprobanteData.put("fecha", new Date());
        session.setAttribute("comprobantePago", comprobanteData);

        request.getRequestDispatcher("/tienda/exito.jsp").forward(request, response);

        // Limpiar session
        session.removeAttribute("idTransaccion");
        session.removeAttribute("totalPedido");
        session.removeAttribute("itemsPedido");
    }
    
    private void exportarComprobantePdf(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=listarProductos");
            return;
        }

        Map<String, Object> comprobanteData = (Map<String, Object>) session.getAttribute("comprobantePago");
        if (comprobanteData == null) {
            session.setAttribute("mensaje", "No hay un comprobante disponible para descargar.");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=listarProductos");
            return;
        }

        String idTransaccion = (String) comprobanteData.get("idTransaccion");
        BigDecimal totalPedido = (BigDecimal) comprobanteData.get("totalPedido");
        Date fecha = (Date) comprobanteData.get("fecha");
        List<Map<String, Object>> itemsComprobante = (List<Map<String, Object>>) comprobanteData.get("items");

        if (totalPedido == null) {
            totalPedido = BigDecimal.ZERO;
        }
        if (fecha == null) {
            fecha = new Date();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fechaTexto = dateFormat.format(fecha);

        List<String> headers = Arrays.asList("Producto", "Cantidad", "Precio Unit.", "Subtotal");
        List<List<String>> rows = new ArrayList<>();
        rows.add(Arrays.asList("ID Transacción: " + (idTransaccion != null ? idTransaccion : "-"), "", "Fecha:", fechaTexto));
        rows.add(Arrays.asList("Método de Pago: PayPal", "", "Total Pedido:", "S/. " + totalPedido));
        rows.add(Arrays.asList("", "", "", ""));

        if (itemsComprobante != null) {
            for (Map<String, Object> item : itemsComprobante) {
                clsProducto producto = (clsProducto) item.get("producto");
                Integer cantidad = (Integer) item.get("cantidad");
                BigDecimal subtotal = (BigDecimal) item.get("subtotal");

                String nombreProducto = producto != null ? producto.getNombreproducto() : "Producto";
                String precioUnitario = producto != null && producto.getPrecio() != null
                        ? "S/. " + producto.getPrecio()
                        : "";

                rows.add(Arrays.asList(
                        nombreProducto,
                        cantidad != null ? cantidad.toString() : "",
                        precioUnitario,
                        subtotal != null ? "S/. " + subtotal : ""
                ));
            }
        }

        rows.add(Arrays.asList("", "", "TOTAL:", "S/. " + totalPedido));

        String fileName = "comprobante_" + (idTransaccion != null ? idTransaccion : "pedido") + ".pdf";
        String title = "Comprobante de Pago - " + (idTransaccion != null ? idTransaccion : "Pedido");

        ExportUtil.exportToPdf(response, fileName, title, headers, rows);
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
}
