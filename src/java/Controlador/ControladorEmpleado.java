package Controlador;

import ModeloDAO.clsEmpleadoDAO;
import ModeloDAO.clsCargoDAO;
import ModeloDAO.clsClienteDAO;
import Modelo.clsCliente;
import Modelo.clsEmpleado;
import Modelo.clsCargo;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorEmpleado")
public class ControladorEmpleado extends HttpServlet {
    
    private clsEmpleadoDAO empleadoDAO;
    private clsCargoDAO cargoDAO;
    
    @Override
    public void init() throws ServletException {
        empleadoDAO = new clsEmpleadoDAO();
        cargoDAO = new clsCargoDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        try {
            switch (accion) {
                case "login":
                    login(request, response);
                    break;
                case "logout":
                    logout(request, response);
                    break;
                case "listar":
                    listarEmpleados(request, response);
                    break;
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "agregar":
                    agregarEmpleado(request, response);
                    break;
                case "editar":
                    mostrarEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarEmpleado(request, response);
                    break;
                case "eliminar":
                    eliminarEmpleado(request, response);
                    break;
                case "buscar":
                    buscarEmpleados(request, response);
                    break;
                case "cambiarEstado":
                    cambiarEstadoEmpleado(request, response);
                    break;
                case "perfil":
                    mostrarPerfil(request, response);
                    break;
                case "actualizarPerfil":
                    actualizarPerfil(request, response);
                    break;
                case "verDetalle":
                    verDetalleEmpleado(request, response);
                    break;
                case "listarInactivos":
                    listarEmpleadosInactivos(request, response);
                    break;
                case "reactivar":
                    reactivarEmpleado(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorEmpleado?accion=listar");
        }
    }
    
    // Nuevos métodos:
    private void listarEmpleadosInactivos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<clsEmpleado> listaEmpleados = empleadoDAO.listar(); // ✅ AGREGAR ESTA LÍNEA
        List<clsEmpleado> listaEmpleadosInactivos = empleadoDAO.listarInactivos();

        request.setAttribute("empleados", listaEmpleados); // ✅ AGREGAR ESTA LÍNEA
        request.setAttribute("empleadosInactivos", listaEmpleadosInactivos);
        request.setAttribute("mostrarInactivos", true); // ✅ MOSTRAR INACTIVOS

        request.getRequestDispatcher("/empleado/listar.jsp").forward(request, response);
    }

    private void reactivarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            boolean exito = empleadoDAO.reactivar(id);

            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Empleado reactivado correctamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al reactivar empleado");
                session.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect(request.getContextPath() + "/ControladorEmpleado?accion=listarInactivos");
    }
    
    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");

        // Validar campos vacíos
        if (usuario == null || usuario.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {

            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Usuario y contraseña son obligatorios");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        System.out.println("=== INTENTO DE LOGIN ===");
        System.out.println("Usuario: " + usuario);
        System.out.println("Password: " + password);

        // PRIMERO: Intentar login como EMPLEADO
        clsEmpleado empleado = empleadoDAO.validarLogin(usuario.trim(), password.trim());

        HttpSession session = request.getSession();
        if (empleado != null) {
            System.out.println("=== LOGIN EXITOSO COMO EMPLEADO ===");
            System.out.println("Empleado: " + empleado.getNombreCompleto());
            System.out.println("Cargo: " + empleado.getNombrecargo());

            session.setAttribute("empleado", empleado);
            session.setAttribute("mensaje", "¡Bienvenido " + empleado.getNombreCompleto() + "!");
            session.setAttribute("tipoMensaje", "success");
            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");

        } else {
            // SEGUNDO: Intentar login como CLIENTE (usando usuario/contraseña real)
            System.out.println("=== INTENTANDO LOGIN COMO CLIENTE ===");

            // Crear DAO de cliente
            clsClienteDAO clienteDAO = new clsClienteDAO();

            // Buscar cliente por usuario y contraseña
            clsCliente cliente = clienteDAO.login(usuario.trim(), password.trim());

            if (cliente != null) {
                System.out.println("=== LOGIN EXITOSO COMO CLIENTE ===");
                System.out.println("Cliente: " + cliente.getNombreCompleto());
                System.out.println("Documento: " + cliente.getDocumentoCompleto());

                session.setAttribute("cliente", cliente);
                session.setAttribute("mensaje", "¡Bienvenido " + cliente.getNombreCompleto() + "!");
                session.setAttribute("tipoMensaje", "success");

                // Redirigir a la tienda en lugar del dashboard
                response.sendRedirect(request.getContextPath() + "/ControladorTienda?accion=listarProductos");

            } else {
                System.out.println("=== LOGIN FALLIDO ===");
                session.setAttribute("mensaje", "Usuario/contraseña incorrectos");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }
    
    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        if (session != null) {
            clsEmpleado empleado = (clsEmpleado) session.getAttribute("empleado");
            if (empleado != null) {
                System.out.println("Logout: " + empleado.getNombreCompleto());
            }
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
    
    private void listarEmpleados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<clsEmpleado> listaEmpleados = empleadoDAO.listar();
        List<clsEmpleado> listaEmpleadosInactivos = empleadoDAO.listarInactivos(); // ✅ AGREGAR ESTA LÍNEA

        request.setAttribute("empleados", listaEmpleados);
        request.setAttribute("empleadosInactivos", listaEmpleadosInactivos); // ✅ AGREGAR ESTA LÍNEA
        request.setAttribute("mostrarInactivos", false); // ✅ POR DEFECTO MOSTRAR ACTIVOS

        request.getRequestDispatcher("/empleado/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<clsCargo> listaCargos = cargoDAO.listar();
        request.setAttribute("cargos", listaCargos);
        request.getRequestDispatcher("/empleado/formulario.jsp").forward(request, response);
    }
    
    private void agregarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Validar campos obligatorios
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String dni = request.getParameter("dni");
            String usuario = request.getParameter("usuario");
            String password = request.getParameter("password");
            String idCargoStr = request.getParameter("idCargo");
            
            if (nombre == null || nombre.trim().isEmpty() ||
                apellido == null || apellido.trim().isEmpty() ||
                dni == null || dni.trim().isEmpty() ||
                usuario == null || usuario.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                idCargoStr == null || idCargoStr.trim().isEmpty()) {
                
                throw new Exception("Los campos nombre, apellido, DNI, usuario, password y cargo son obligatorios");
            }
            
            int idCargo = Integer.parseInt(idCargoStr);
            
            // Crear objeto empleado
            clsEmpleado empleado = new clsEmpleado();
            empleado.setNombre(nombre.trim());
            empleado.setApellido(apellido.trim());
            empleado.setDni(dni.trim());
            empleado.setUsuario(usuario.trim());
            empleado.setPassword(password.trim());
            empleado.setIdcargo(idCargo);
            
            // Campos opcionales
            String telefono = request.getParameter("telefono");
            if (telefono != null && !telefono.trim().isEmpty()) {
                empleado.setTelefono(telefono.trim());
            }
            
            String email = request.getParameter("email");
            if (email != null && !email.trim().isEmpty()) {
                empleado.setEmail(email.trim());
            }
            
            String direccion = request.getParameter("direccion");
            if (direccion != null && !direccion.trim().isEmpty()) {
                empleado.setDireccion(direccion.trim());
            }
            
            String fechaContratoStr = request.getParameter("fechaContrato");
            if (fechaContratoStr != null && !fechaContratoStr.trim().isEmpty()) {
                try {
                    LocalDate fechaContrato = LocalDate.parse(fechaContratoStr);
                    empleado.setFechaContrato(fechaContrato);
                } catch (DateTimeParseException e) {
                    throw new Exception("Formato de fecha inválido. Use YYYY-MM-DD");
                }
            } else {
                empleado.setFechaContrato(LocalDate.now());
            }
            
            String salarioStr = request.getParameter("salario");
            if (salarioStr != null && !salarioStr.trim().isEmpty()) {
                try {
                    BigDecimal salario = new BigDecimal(salarioStr);
                    empleado.setSalario(salario);
                } catch (NumberFormatException e) {
                    throw new Exception("Formato de salario inválido");
                }
            } else {
                empleado.setSalario(BigDecimal.ZERO);
            }
            
            // Verificar si el usuario o DNI ya existen
            if (existeUsuario(usuario.trim())) {
                throw new Exception("El usuario ya existe en el sistema");
            }
            
            if (existeDni(dni.trim())) {
                throw new Exception("El DNI ya está registrado en el sistema");
            }
            
            boolean exito = empleadoDAO.agregar(empleado);
            
            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Empleado agregado correctamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al agregar empleado");
                session.setAttribute("tipoMensaje", "error");
            }
        } catch (NumberFormatException e) {
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "ID de cargo inválido");
            session.setAttribute("tipoMensaje", "error");
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorEmpleado?accion=listar");
    }
    
    private void mostrarEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            clsEmpleado empleado = empleadoDAO.listarPorId(id);
            List<clsCargo> listaCargos = cargoDAO.listar();
            
            if (empleado != null) {
                request.setAttribute("empleado", empleado);
                request.setAttribute("cargos", listaCargos);
                request.getRequestDispatcher("/empleado/editar.jsp").forward(request, response);
            } else {
                throw new Exception("Empleado no encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorEmpleado?accion=listar");
        }
    }
    
    private void actualizarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String dni = request.getParameter("dni");
            String telefono = request.getParameter("telefono");
            String email = request.getParameter("email");
            String direccion = request.getParameter("direccion");
            String fechaContratoStr = request.getParameter("fechaContrato");
            String salarioStr = request.getParameter("salario");
            int idCargo = Integer.parseInt(request.getParameter("idCargo"));
            String usuario = request.getParameter("usuario");
            String password = request.getParameter("password");
            
            // Validar campos obligatorios
            if (nombre == null || nombre.trim().isEmpty() ||
                apellido == null || apellido.trim().isEmpty() ||
                dni == null || dni.trim().isEmpty() ||
                usuario == null || usuario.trim().isEmpty()) {
                
                throw new Exception("Los campos nombre, apellido, DNI, usuario y cargo son obligatorios");
            }
            
            clsEmpleado empleado = new clsEmpleado();
            empleado.setIdempleado(id);
            empleado.setNombre(nombre.trim());
            empleado.setApellido(apellido.trim());
            empleado.setDni(dni.trim());
            empleado.setTelefono(telefono != null ? telefono.trim() : null);
            empleado.setEmail(email != null ? email.trim() : null);
            empleado.setDireccion(direccion != null ? direccion.trim() : null);
            empleado.setIdcargo(idCargo);
            empleado.setUsuario(usuario.trim());
            
            // Procesar fecha de contrato
            if (fechaContratoStr != null && !fechaContratoStr.trim().isEmpty()) {
                try {
                    LocalDate fechaContrato = LocalDate.parse(fechaContratoStr);
                    empleado.setFechaContrato(fechaContrato);
                } catch (DateTimeParseException e) {
                    throw new Exception("Formato de fecha inválido. Use YYYY-MM-DD");
                }
            }
            
            // Procesar salario
            if (salarioStr != null && !salarioStr.trim().isEmpty()) {
                try {
                    BigDecimal salario = new BigDecimal(salarioStr);
                    empleado.setSalario(salario);
                } catch (NumberFormatException e) {
                    throw new Exception("Formato de salario inválido");
                }
            }
            
            // Solo actualizar password si se proporcionó uno nuevo
            if (password != null && !password.trim().isEmpty()) {
                empleado.setPassword(password.trim());
            } else {
                // Mantener la contraseña actual
                clsEmpleado empleadoActual = empleadoDAO.listarPorId(id);
                if (empleadoActual != null) {
                    empleado.setPassword(empleadoActual.getPassword());
                }
            }
            
            // Verificar si el usuario ya existe (excluyendo el actual)
            if (existeUsuario(usuario.trim(), id)) {
                throw new Exception("El usuario ya existe en el sistema");
            }
            
            // Verificar si el DNI ya existe (excluyendo el actual)
            if (existeDni(dni.trim(), id)) {
                throw new Exception("El DNI ya está registrado en el sistema");
            }
            
            boolean exito = empleadoDAO.actualizar(empleado);
            
            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Empleado actualizado correctamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al actualizar empleado");
                session.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error en los datos: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorEmpleado?accion=listar");
    }
    
    private void eliminarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            // Verificar si el empleado a eliminar es el mismo que está logueado
            HttpSession session = request.getSession();
            clsEmpleado empleadoLogueado = (clsEmpleado) session.getAttribute("empleado");
            
            if (empleadoLogueado != null && empleadoLogueado.getIdempleado() == id) {
                session.setAttribute("mensaje", "No puede eliminar su propio usuario");
                session.setAttribute("tipoMensaje", "error");
            } else {
                boolean exito = empleadoDAO.eliminar(id);
                
                if (exito) {
                    session.setAttribute("mensaje", "Empleado eliminado correctamente");
                    session.setAttribute("tipoMensaje", "success");
                } else {
                    session.setAttribute("mensaje", "Error al eliminar empleado");
                    session.setAttribute("tipoMensaje", "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorEmpleado?accion=listar");
    }
    
    // ========== NUEVOS MÉTODOS AGREGADOS ==========
    
    private void buscarEmpleados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String criterio = request.getParameter("criterio");
            String valor = request.getParameter("valor");
            
            if (criterio == null || valor == null || valor.trim().isEmpty()) {
                listarEmpleados(request, response);
                return;
            }
            
            List<clsEmpleado> listaEmpleados = buscarEmpleados(criterio, valor.trim());
            request.setAttribute("empleados", listaEmpleados);
            request.setAttribute("criterio", criterio);
            request.setAttribute("valor", valor);
            request.getRequestDispatcher("/empleado/listar.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error en la búsqueda: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            listarEmpleados(request, response);
        }
    }
    
    private void cambiarEstadoEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String estado = request.getParameter("estado");
            
            boolean exito = cambiarEstado(id, estado);
            
            HttpSession session = request.getSession();
            if (exito) {
                session.setAttribute("mensaje", "Estado del empleado actualizado correctamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al cambiar estado del empleado");
                session.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorEmpleado?accion=listar");
    }
    
    private void mostrarPerfil(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession();
            clsEmpleado empleado = (clsEmpleado) session.getAttribute("empleado");
            
            if (empleado != null) {
                // Actualizar datos del empleado desde la base de datos
                clsEmpleado empleadoActualizado = empleadoDAO.listarPorId(empleado.getIdempleado());
                request.setAttribute("empleado", empleadoActualizado);
                request.getRequestDispatcher("/empleado/perfil.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error al cargar perfil: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
        }
    }
    
    private void actualizarPerfil(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession();
            clsEmpleado empleadoLogueado = (clsEmpleado) session.getAttribute("empleado");
            
            if (empleadoLogueado == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }
            
            int id = empleadoLogueado.getIdempleado();
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String telefono = request.getParameter("telefono");
            String email = request.getParameter("email");
            String direccion = request.getParameter("direccion");
            String passwordActual = request.getParameter("passwordActual");
            String passwordNuevo = request.getParameter("passwordNuevo");
            
            // Validar campos obligatorios
            if (nombre == null || nombre.trim().isEmpty() ||
                apellido == null || apellido.trim().isEmpty()) {
                
                throw new Exception("Los campos nombre y apellido son obligatorios");
            }
            
            clsEmpleado empleado = new clsEmpleado();
            empleado.setIdempleado(id);
            empleado.setNombre(nombre.trim());
            empleado.setApellido(apellido.trim());
            empleado.setTelefono(telefono != null ? telefono.trim() : null);
            empleado.setEmail(email != null ? email.trim() : null);
            empleado.setDireccion(direccion != null ? direccion.trim() : null);
            
            // Verificar y actualizar contraseña si se proporciona
            if (passwordNuevo != null && !passwordNuevo.trim().isEmpty()) {
                if (passwordActual == null || passwordActual.trim().isEmpty()) {
                    throw new Exception("Debe ingresar la contraseña actual para cambiarla");
                }
                
                // Verificar contraseña actual
                clsEmpleado empleadoActual = empleadoDAO.validarLogin(
                    empleadoLogueado.getUsuario(), passwordActual.trim());
                
                if (empleadoActual == null) {
                    throw new Exception("La contraseña actual es incorrecta");
                }
                
                empleado.setPassword(passwordNuevo.trim());
            } else {
                // Mantener la contraseña actual
                empleado.setPassword(empleadoLogueado.getPassword());
            }
            
            boolean exito = empleadoDAO.actualizar(empleado);
            
            if (exito) {
                // Actualizar datos en sesión
                clsEmpleado empleadoActualizado = empleadoDAO.listarPorId(id);
                session.setAttribute("empleado", empleadoActualizado);
                session.setAttribute("mensaje", "Perfil actualizado correctamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al actualizar perfil");
                session.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/ControladorEmpleado?accion=perfil");
    }
    
    private void verDetalleEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            clsEmpleado empleado = empleadoDAO.listarPorId(id);
            
            if (empleado != null) {
                request.setAttribute("empleado", empleado);
                request.getRequestDispatcher("/empleado/detalle.jsp").forward(request, response);
            } else {
                throw new Exception("Empleado no encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/ControladorEmpleado?accion=listar");
        }
    }
    
    // ========== MÉTODOS AUXILIARES ==========
    
    private boolean existeUsuario(String usuario) {
        clsEmpleado empleado = empleadoDAO.listarPorUsuario(usuario);
        return empleado != null;
    }
    
    private boolean existeUsuario(String usuario, int idExcluir) {
        clsEmpleado empleado = empleadoDAO.listarPorUsuario(usuario);
        return empleado != null && empleado.getIdempleado() != idExcluir;
    }
    
    private boolean existeDni(String dni) {
        // Implementar lógica para verificar DNI
        // Por ahora, asumimos que no existe
        return false;
    }
    
    private boolean existeDni(String dni, int idExcluir) {
        // Implementar lógica para verificar DNI excluyendo un ID
        // Por ahora, asumimos que no existe
        return false;
    }
    
    private List<clsEmpleado> buscarEmpleados(String criterio, String valor) {
        // Implementar lógica de búsqueda
        // Por ahora, retornamos todos los empleados
        return empleadoDAO.listar();
    }
    
    private boolean cambiarEstado(int id, String estado) {
        // Implementar lógica para cambiar estado
        // Por ahora, asumimos éxito
        return true;
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
        return "Controlador para gestión de empleados";
    }
}