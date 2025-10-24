package Controlador;

import ModeloDAO.clsClienteDAO;
import Modelo.clsCliente;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Util.ExportUtil;
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet("/ControladorCliente")
public class ControladorCliente extends HttpServlet {

    private clsClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        clienteDAO = new clsClienteDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String accion = request.getParameter("accion");
        System.out.println("=== DEBUG ControladorCliente ===");
        System.out.println("Acción: " + accion);
        System.out.println("Parámetros: " + request.getParameterMap().toString());

        if (accion == null) {
            response.sendRedirect(request.getContextPath() + "/ControladorCliente?accion=listar");
            return;
        }

        try {
            switch (accion) {
                case "listar":
                    listarClientes(request, response);
                    break;
                case "listarInactivos":
                    listarClientesInactivos(request, response);
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarCliente(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarCliente(request, response);
                    break;
                case "eliminar":
                    eliminarCliente(request, response);
                    break;
                case "reactivar":
                    reactivarCliente(request, response);
                    break;  // ✅ ¡AGREGA ESTE BREAK!
                case "buscarPorDni":
                    buscarPorDni(request, response);
                    break;
                case "exportarExcel":
                    exportarClientesExcel(response);
                    break;
                case "exportarPdf":
                    exportarClientesPdf(response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/ControladorCliente?accion=listar");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error en el sistema: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorCliente?accion=listar");
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/cliente/formulario.jsp").forward(request, response); // ✅ Ruta absoluta
    }

    private void reactivarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            System.out.println("=== REACTIVANDO CLIENTE ID: " + id + " ===");

            boolean exito = clienteDAO.reactivar(id);

            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Cliente reactivado correctamente");
                session.setAttribute("tipoMensaje", "success");
                System.out.println("Cliente reactivado exitosamente: " + id);
            } else {
                session.setAttribute("mensaje", "Error al reactivar cliente");
                session.setAttribute("tipoMensaje", "error");
                System.out.println("Error al reactivar cliente: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            System.out.println("Excepción al reactivar cliente: " + e.getMessage());
        }

        // ✅ REDIRIGIR A LA LISTA DE INACTIVOS
        response.sendRedirect(request.getContextPath() + "/ControladorCliente?accion=listarInactivos");
    }

    private void listarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<clsCliente> listaClientes = clienteDAO.listar();
        List<clsCliente> listaClientesInactivos = clienteDAO.listarInactivos(); // Cargar ambos

        request.setAttribute("clientes", listaClientes);
        request.setAttribute("clientesInactivos", listaClientesInactivos);
        request.setAttribute("mostrarInactivos", false);
        request.getRequestDispatcher("/cliente/listar.jsp").forward(request, response); // ✅ Usar ruta absoluta
    }

    private void listarClientesInactivos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<clsCliente> listaClientes = clienteDAO.listar(); // Cargar ambos
        List<clsCliente> listaClientesInactivos = clienteDAO.listarInactivos();

        request.setAttribute("clientes", listaClientes);
        request.setAttribute("clientesInactivos", listaClientesInactivos);
        request.setAttribute("mostrarInactivos", true);
        request.getRequestDispatcher("/cliente/listar.jsp").forward(request, response); // ✅ Usar ruta absoluta
    }

    private void agregarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String dni = request.getParameter("dni");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String direccion = request.getParameter("direccion");

        clsCliente cliente = new clsCliente();
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setDni(dni);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);
        cliente.setDireccion(direccion);
        cliente.setFechaRegistro(LocalDate.now());

        boolean exito = clienteDAO.agregar(cliente);

        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Cliente agregado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al agregar cliente");
            session.setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect(request.getContextPath() + "/ControladorCliente?accion=listar");
    }

    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        clsCliente cliente = clienteDAO.listarPorId(id);

        if (cliente != null) {
            request.setAttribute("cliente", cliente);
            request.getRequestDispatcher("/cliente/editar.jsp").forward(request, response); // ✅ Ruta absoluta
        } else {
            response.sendRedirect(request.getContextPath() + "/ControladorCliente?accion=listar"); // ✅ Ruta absoluta
        }
    }

    private void actualizarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String dni = request.getParameter("dni");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String direccion = request.getParameter("direccion");

        clsCliente cliente = new clsCliente();
        cliente.setIdcliente(id);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setDni(dni);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);
        cliente.setDireccion(direccion);

        boolean exito = clienteDAO.actualizar(cliente);

        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Cliente actualizado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al actualizar cliente");
            session.setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect(request.getContextPath() + "/ControladorCliente?accion=listar");
    }

    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        boolean exito = clienteDAO.eliminar(id);

        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "Cliente eliminado correctamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "Error al eliminar cliente");
            session.setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect(request.getContextPath() + "/ControladorCliente?accion=listar");
    }

    private void buscarPorDni(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String dni = request.getParameter("dni");
            clsCliente cliente = clienteDAO.listarPorDni(dni);

            if (cliente != null) {
                request.setAttribute("cliente", cliente);
            } else {
                request.setAttribute("mensaje", "Cliente no encontrado");
            }

            request.getRequestDispatcher("/cliente/buscar.jsp").forward(request, response);

        } catch (Exception e) {
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error en la búsqueda: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorCliente?accion=listar");
        }
    }
    
     private void exportarClientesExcel(HttpServletResponse response) throws IOException {
        List<clsCliente> clientes = clienteDAO.listar();
        if (clientes == null) {
            clientes = new ArrayList<>();
        }
        List<String> cabeceras = Arrays.asList("ID", "Nombre", "Documento", "Teléfono",
                "Email", "Dirección", "Registro", "Estado");

        List<List<String>> filas = new ArrayList<>();
        for (clsCliente cliente : clientes) {
            filas.add(Arrays.asList(
                    String.valueOf(cliente.getIdcliente()),
                    safeString(cliente.getNombreCompleto()),
                    safeString(cliente.getDocumentoCompleto()),
                    safeString(cliente.getTelefono()),
                    safeString(cliente.getEmail()),
                    safeString(cliente.getDireccion()),
                    cliente.getFechaRegistro() != null ? cliente.getFechaRegistro().toString() : "",
                    formatearEstado(cliente.getEstado())));
        }

        ExportUtil.exportToExcel(response, "clientes.xls", cabeceras, filas);
    }

    private void exportarClientesPdf(HttpServletResponse response) throws IOException {
        List<clsCliente> clientes = clienteDAO.listar();
        if (clientes == null) {
            clientes = new ArrayList<>();
        }
        List<String> cabeceras = Arrays.asList("ID", "Nombre", "Documento", "Contacto", "Estado");

        List<List<String>> filas = new ArrayList<>();
        for (clsCliente cliente : clientes) {
            String contacto = concatNonEmpty(safeString(cliente.getTelefono()), safeString(cliente.getEmail()));
            filas.add(Arrays.asList(
                    String.valueOf(cliente.getIdcliente()),
                    safeString(cliente.getNombreCompleto()),
                    safeString(cliente.getDocumentoCompleto()),
                    contacto,
                    formatearEstado(cliente.getEstado())));
        }

        ExportUtil.exportToPdf(response, "clientes.pdf", "Listado de clientes", cabeceras, filas);
    }

    private String safeString(String value) {
        return value != null ? value : "";
    }

    private String concatNonEmpty(String... values) {
        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                if (sb.length() > 0) {
                    sb.append(" / ");
                }
                sb.append(value);
            }
        }
        return sb.toString();
    }

    private String formatearEstado(int estado) {
        return estado == 1 ? "Activo" : "Inactivo";
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
        return "Controlador para gestión de clientes";
    }
}
