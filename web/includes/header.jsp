<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.clsEmpleado"%>
<%
    // Verificar sesión
    Object empleadoObj = session.getAttribute("empleado");
    if (empleadoObj == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    clsEmpleado empleadoLogueado = (clsEmpleado) empleadoObj;
    String iniciales = empleadoLogueado.getNombre().charAt(0) + "" + empleadoLogueado.getApellido().charAt(0);
%>
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-custom">
    <div class="container-fluid">
        <button class="btn btn-primary me-3" id="sidebarToggle">
            <i class="fas fa-bars"></i>
        </button>
        
        <div class="navbar-nav ms-auto align-items-center">
            <span class="navbar-text me-4 d-none d-md-block">
                <i class="fas fa-calendar me-2 text-primary"></i>
                <span id="currentDateTime"></span>
            </span>
            <div class="dropdown">
                <button class="btn btn-outline-primary dropdown-toggle" type="button" 
                        data-bs-toggle="dropdown">
                    <i class="fas fa-user me-2"></i>
                    <%= empleadoLogueado.getNombre() %>
                </button>
                <ul class="dropdown-menu">
                    <li>
                        <a class="dropdown-item" href="#">
                            <i class="fas fa-user-edit me-2"></i>Mi Perfil
                        </a>
                    </li>
                    <li><hr class="dropdown-divider"></li>
                    <li>
                        <a class="dropdown-item text-danger" href="ControladorEmpleado?accion=logout">
                            <i class="fas fa-sign-out-alt me-2"></i>Cerrar Sesi&oacute;n
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</nav>