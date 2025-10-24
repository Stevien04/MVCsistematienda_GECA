package Controlador;

import ModeloDAO.clsClienteDAO;
import ModeloDAO.clsTipoDocumentoDAO;
import Modelo.clsCliente;
import Modelo.clsTipoDocumento;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorRegistroCliente")
public class ControladorRegistroCliente extends HttpServlet {
    
    private clsClienteDAO clienteDAO;
    private clsTipoDocumentoDAO tipoDocumentoDAO;
    
    @Override
    public void init() throws ServletException {
        clienteDAO = new clsClienteDAO();
        tipoDocumentoDAO = new clsTipoDocumentoDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            mostrarFormularioRegistro(request, response);
            return;
        }

        try {
            switch (accion) {
                case "registrar":
                    registrarCliente(request, response);
                    break;
                case "mostrarFormulario":
                    mostrarFormularioRegistro(request, response);
                    break;
                default:
                    mostrarFormularioRegistro(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/registroCliente.jsp");
        }
    }
    
    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener tipos de documento para el formulario
        List<clsTipoDocumento> tiposDocumento = tipoDocumentoDAO.listar();
        request.setAttribute("tiposDocumento", tiposDocumento);
        request.getRequestDispatcher("/registroCliente.jsp").forward(request, response);
    }
    
    private void registrarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idTipoDocumentoStr = request.getParameter("idtipodocumento");
        String numeroDocumento = request.getParameter("numero_documento");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String direccion = request.getParameter("direccion");
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");
        String fechaNacimientoStr = request.getParameter("fecha_nacimiento");
        
        // Validaciones básicas
        if (idTipoDocumentoStr == null || idTipoDocumentoStr.trim().isEmpty() ||
            numeroDocumento == null || numeroDocumento.trim().isEmpty() ||
            nombre == null || nombre.trim().isEmpty() ||
            apellido == null || apellido.trim().isEmpty() ||
            usuario == null || usuario.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Todos los campos marcados con * son obligatorios");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorRegistroCliente?accion=mostrarFormulario");
            return;
        }
        
        // Validar que las contraseñas coincidan
        if (password.length() < 6) { 
            HttpSession session = request.getSession();
           session.setAttribute("mensaje", "La contraseña debe tener al menos 6 caracteres");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorRegistroCliente?accion=mostrarFormulario");
            return;
        }
        
        int idTipoDocumento = Integer.parseInt(idTipoDocumentoStr);
        
        // Verificar si el usuario ya existe
        if (clienteDAO.existeUsuario(usuario.trim())) {
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "El usuario '" + usuario + "' ya está registrado");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorRegistroCliente?accion=mostrarFormulario");
            return;
        }
        
        // Verificar si el número de documento ya existe
        if (clienteDAO.existeNumeroDocumento(numeroDocumento.trim())) {
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "El número de documento '" + numeroDocumento + "' ya está registrado");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorRegistroCliente?accion=mostrarFormulario");
            return;
        }
        
        clsCliente cliente = new clsCliente();
        cliente.setIdtipodocumento(idTipoDocumento);
        cliente.setNumero_documento(numeroDocumento.trim());
        cliente.setNombre(nombre.trim());
        cliente.setApellido(apellido.trim());
        cliente.setTelefono(telefono != null ? telefono.trim() : null);
        cliente.setEmail(email != null ? email.trim() : null);
        cliente.setDireccion(direccion != null ? direccion.trim() : null);
        cliente.setUsuario(usuario.trim());
        cliente.setPassword(password.trim()); // En producción, hashear la contraseña
        cliente.setFechaRegistro(LocalDate.now());
        
        // Procesar fecha de nacimiento si se proporciona
        if (fechaNacimientoStr != null && !fechaNacimientoStr.trim().isEmpty()) {
            try {
                LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
                cliente.setFechaNacimiento(fechaNacimiento);
            } catch (Exception e) {
                System.out.println("Formato de fecha de nacimiento inválido: " + fechaNacimientoStr);
            }
        }
        
        boolean exito = clienteDAO.Registrarcliente(cliente);
        
        HttpSession session = request.getSession();
        if (exito) {
            session.setAttribute("mensaje", "¡Registro exitoso! Ahora puede iniciar sesión con su usuario y contraseña");
            session.setAttribute("tipoMensaje", "success");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            session.setAttribute("mensaje", "Error al registrar cliente");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorRegistroCliente?accion=mostrarFormulario");
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
}