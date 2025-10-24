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
<!-- Sidebar -->
<div class="sidebar" id="sidebar">
    <div class="sidebar-brand">
        <h4 class="mb-0">
            <i class="fas fa-store me-2"></i>TIENDA GECA
        </h4>
        <small class="opacity-75">Sistema de Gesti&oacute;n</small>
    </div>
    
    <div class="sidebar-content p-3">
        <!-- User Info -->
        <div class="user-info bg-white bg-opacity-10 rounded-3 p-3 mb-4 text-center">
            <div class="user-avatar mx-auto mb-2">
                <%= iniciales %>
            </div>
            <h6 class="mb-1"><%= empleadoLogueado.getNombre() + " " + empleadoLogueado.getApellido() %></h6>
            <small class="opacity-75"><%= empleadoLogueado.getNombrecargo() != null ? empleadoLogueado.getNombrecargo() : "Empleado" %></small>
        </div>
        
        <!-- Navigation -->
        <div class="nav flex-column">
            <a class="nav-link" href="dashboard.jsp">
                <i class="fas fa-tachometer-alt me-3"></i><span>Dashboard</span>
            </a>
            <a class="nav-link" href="<%= request.getContextPath() %>/dashboard/Estadistica.jsp">
                <i class="fas fa-chart-bar me-3"></i><span>Estadísticas</span>
            </a>
            <a class="nav-link" href="ControladorEmpleado?accion=listar">
                <i class="fas fa-users me-3"></i><span>Empleados</span>
            </a>
            <a class="nav-link" href="ControladorCliente?accion=listar">
                <i class="fas fa-user-friends me-3"></i><span>Clientes</span>
            </a>
            <a class="nav-link" href="ControladorProducto?accion=listar">
                <i class="fas fa-box me-3"></i><span>Productos</span>
            </a>
            <a class="nav-link" href="ControladorCategoria?accion=listar">
                <i class="fas fa-tags me-3"></i><span>Categor&iacute;as</span>
            </a>
            <a class="nav-link" href="ControladorTienda?accion=listarProductos">
                <i class="fas fa-shop me-3"></i><span>Compras</span>
            </a>
            <a class="nav-link text-warning mt-4" href="ControladorEmpleado?accion=logout">
                <i class="fas fa-sign-out-alt me-3"></i><span>Cerrar Sesi&oacute;n</span>
            </a>
        </div>
    </div>
</div>