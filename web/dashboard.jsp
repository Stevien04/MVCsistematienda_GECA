<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ModeloDAO.clsEmpleadoDAO"%>
<%@page import="Modelo.clsEmpleado"%>
<%
   
    Object empleadoObj = session.getAttribute("empleado");
    if (empleadoObj == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    String mensaje = (String) session.getAttribute("mensaje");
    String tipoMensaje = (String) session.getAttribute("tipoMensaje");
    
    
    if (mensaje != null) {
        session.removeAttribute("mensaje");
        session.removeAttribute("tipoMensaje");
    }
    
    String nombreUsuario = "Usuario";
    String apellidoUsuario = "Sistema";
    String cargoUsuario = "Empleado";
    String iniciales = "US";
    int totalEmpleados = 0;
    int totalClientes = 0;
    int totalProductos = 0;
    double ventasHoy = 0.0;
    
   
    if (empleadoObj instanceof clsEmpleado) {
        clsEmpleado empleadoLogueado = (clsEmpleado) empleadoObj;
        nombreUsuario = empleadoLogueado.getNombre() != null ? empleadoLogueado.getNombre() : "Usuario";
        apellidoUsuario = empleadoLogueado.getApellido() != null ? empleadoLogueado.getApellido() : "Sistema";
        cargoUsuario = empleadoLogueado.getNombrecargo() != null ? empleadoLogueado.getNombrecargo() : "Empleado";
        iniciales = (nombreUsuario.charAt(0) + "" + apellidoUsuario.charAt(0)).toUpperCase();
        
        try {
            clsEmpleadoDAO empleadoDAO = new clsEmpleadoDAO();
            totalEmpleados = empleadoDAO.obtenerTotalEmpleados();
            
            totalClientes = 15;  
            totalProductos = 28;  
            ventasHoy = 2150.75; 
            
        } catch (Exception e) {
            // Usar valores por defecto si hay error
            totalEmpleados = 5;
            totalClientes = 15;
            totalProductos = 28;
            ventasHoy = 2150.75;
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Sistema Tienda GECA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #667eea;
            --secondary-color: #764ba2;
            --sidebar-width: 280px;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
            overflow-x: hidden;
        }
        
        .sidebar {
            background: linear-gradient(180deg, var(--primary-color) 0%, var(--secondary-color) 100%);
            color: white;
            width: var(--sidebar-width);
            height: 100vh;
            position: fixed;
            transition: all 0.3s;
            box-shadow: 3px 0 15px rgba(0,0,0,0.1);
            z-index: 1000;
            left: 0;
            top: 0;
        }
        
        .sidebar-brand {
            padding: 1.5rem 1rem;
            border-bottom: 1px solid rgba(255,255,255,0.1);
            text-align: center;
        }
        
        .sidebar .nav-link {
            color: rgba(255,255,255,0.9);
            padding: 0.8rem 1.5rem;
            margin: 0.1rem 1rem;
            border-radius: 10px;
            transition: all 0.3s;
            font-weight: 500;
            text-decoration: none;
            display: block;
        }
        
        .sidebar .nav-link:hover {
            background: rgba(255,255,255,0.15);
            color: white;
            transform: translateX(5px);
        }
        
        .sidebar .nav-link.active {
            background: rgba(255,255,255,0.2);
            color: white;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        
        .main-content {
            margin-left: var(--sidebar-width);
            transition: all 0.3s;
            min-height: 100vh;
            background-color: #f8f9fa;
        }
        
        .navbar-custom {
            background: white;
            box-shadow: 0 2px 15px rgba(0,0,0,0.1);
            padding: 1rem 1.5rem;
            position: sticky;
            top: 0;
            z-index: 999;
        }
        
        .card-dashboard {
            border: none;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
            transition: transform 0.3s, box-shadow 0.3s;
            overflow: hidden;
            margin-bottom: 1.5rem;
            background: white;
        }
        
        .card-dashboard:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }
        
        .stat-card {
            color: white;
            position: relative;
            overflow: hidden;
            border: none;
        }
        
        .stat-card::before {
            content: '';
            position: absolute;
            top: -50%;
            right: -50%;
            width: 100%;
            height: 200%;
            background: rgba(255,255,255,0.1);
            transform: rotate(45deg);
        }
        
        .welcome-section {
            background: linear-gradient(135deg, var(--primary-color) 0%, var(--secondary-color) 100%);
            color: white;
            border-radius: 20px;
            padding: 2.5rem;
            margin-bottom: 2rem;
            position: relative;
            overflow: hidden;
        }
        
        .welcome-section::before {
            content: '';
            position: absolute;
            top: -50%;
            right: -50%;
            width: 100%;
            height: 200%;
            background: radial-gradient(circle, rgba(255,255,255,0.1) 1px, transparent 1px);
            background-size: 20px 20px;
            opacity: 0.3;
        }
        
        .user-avatar {
            width: 45px;
            height: 45px;
            background: linear-gradient(135deg, var(--primary-color) 0%, var(--secondary-color) 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: bold;
            font-size: 1.1rem;
        }
        
        .sidebar-collapsed {
            width: 80px;
            transform: translateX(0);
        }
        
        .sidebar-collapsed .sidebar-brand h4,
        .sidebar-collapsed .sidebar-brand small,
        .sidebar-collapsed .nav-link span {
            display: none;
        }
        
        .sidebar-collapsed .nav-link {
            text-align: center;
            padding: 0.8rem 0.5rem;
            margin: 0.2rem 0.5rem;
        }
        
        .sidebar-collapsed .nav-link i {
            margin-right: 0;
            font-size: 1.2rem;
        }
        
        .main-content-expanded {
            margin-left: 80px;
        }
        
        .quick-action-btn {
            border: none;
            border-radius: 12px;
            padding: 1.5rem;
            text-align: center;
            color: white;
            text-decoration: none;
            display: block;
            transition: all 0.3s;
            height: 100%;
        }
        
        .quick-action-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
            color: white;
            text-decoration: none;
        }
        
        @media (max-width: 768px) {
            .sidebar {
                width: var(--sidebar-width);
                transform: translateX(-100%);
            }
            
            .sidebar.show {
                transform: translateX(0);
            }
            
            .main-content {
                margin-left: 0;
            }
            
            .sidebar-collapsed {
                transform: translateX(-100%);
            }
            
            .main-content-expanded {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>
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
                <h6 class="mb-1"><%= nombreUsuario + " " + apellidoUsuario %></h6>
                <small class="opacity-75"><%= cargoUsuario %></small>
            </div>
            
            <!-- Navigation -->
            <div class="nav flex-column">
                <a class="nav-link active" href="dashboard.jsp">
                    <i class="fas fa-tachometer-alt me-3"></i><span>Dashboard</span>
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

    <!-- Main Content -->
    <div class="main-content" id="mainContent">
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
                            <%= nombreUsuario %>
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

        <!-- Content -->
        <div class="container-fluid p-4">
            <!-- Mensajes -->
            <% if (mensaje != null) { %>
                <div class="alert alert-<%= tipoMensaje != null ? tipoMensaje : "info" %> alert-dismissible fade show" role="alert">
                    <i class="fas fa-<%= "success".equals(tipoMensaje) ? "check-circle" : "exclamation-triangle" %> me-2"></i>
                    <%= mensaje %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            <% } %>

            <!-- Welcome Section -->
            <div class="welcome-section">
                <div class="row align-items-center">
                    <div class="col-md-8">
                        <h2 class="mb-3">¡Bienvenido, <%= nombreUsuario %>!</h2>
                        <p class="mb-0 lead opacity-90">
                            Sistema de gesti&oacute;n integral para Tienda GECA. 
                            Gestiona empleados, clientes, productos y ventas de manera eficiente.
                        </p>
                    </div>
                    <div class="col-md-4 text-end d-none d-md-block">
                        <i class="fas fa-store fa-5x opacity-25"></i>
                    </div>
                </div>
            </div>

            <!-- Stats Cards -->
            <div class="row g-4">
                <div class="col-xl-3 col-md-6">
                    <div class="card card-dashboard stat-card" 
                         style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                        <div class="card-body position-relative">
                            <div class="row align-items-center">
                                <div class="col-8">
                                    <h5 class="card-title opacity-90">Empleados</h5>
                                    <h2 class="mb-0"><%= totalEmpleados %></h2>
                                    <small class="opacity-75">Activos en sistema</small>
                                </div>
                                <div class="col-4 text-end">
                                    <i class="fas fa-users fa-3x opacity-50"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-xl-3 col-md-6">
                    <div class="card card-dashboard stat-card" 
                         style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                        <div class="card-body position-relative">
                            <div class="row align-items-center">
                                <div class="col-8">
                                    <h5 class="card-title opacity-90">Clientes</h5>
                                    <h2 class="mb-0"><%= totalClientes %></h2>
                                    <small class="opacity-75">Registrados</small>
                                </div>
                                <div class="col-4 text-end">
                                    <i class="fas fa-user-friends fa-3x opacity-50"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-xl-3 col-md-6">
                    <div class="card card-dashboard stat-card" 
                         style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                        <div class="card-body position-relative">
                            <div class="row align-items-center">
                                <div class="col-8">
                                    <h5 class="card-title opacity-90">Productos</h5>
                                    <h2 class="mb-0"><%= totalProductos %></h2>
                                    <small class="opacity-75">En inventario</small>
                                </div>
                                <div class="col-4 text-end">
                                    <i class="fas fa-box fa-3x opacity-50"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-xl-3 col-md-6">
                    <div class="card card-dashboard stat-card" 
                         style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                        <div class="card-body position-relative">
                            <div class="row align-items-center">
                                <div class="col-8">
                                    <h5 class="card-title opacity-90">Ventas Hoy</h5>
                                    <h2 class="mb-0">S/ <%= String.format("%.2f", ventasHoy) %></h2>
                                    <small class="opacity-75">Total del d&iacute;a</small>
                                </div>
                                <div class="col-4 text-end">
                                    <i class="fas fa-shopping-cart fa-3x opacity-50"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Quick Actions & Recent Activity -->
            <div class="row g-4 mt-2">
                <div class="col-lg-8">
                    <div class="card card-dashboard">
                        <div class="card-header bg-transparent border-0 pb-0">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-bolt me-2 text-warning"></i>Acciones R&aacute;pidas
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-md-6 col-lg-4">
                                    <a href="ControladorEmpleado?accion=nuevo" class="quick-action-btn" 
                                       style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                                        <i class="fas fa-user-tie fa-2x mb-2"></i>
                                        <div>Nuevo Empleado</div>
                                    </a>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <a href="ControladorCliente?accion=nuevo" class="quick-action-btn" 
                                       style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                                        <i class="fas fa-user-plus fa-2x mb-2"></i>
                                        <div>Nuevo Cliente</div>
                                    </a>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <a href="ControladorProducto?accion=nuevo" class="quick-action-btn" 
                                       style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                                        <i class="fas fa-box fa-2x mb-2"></i>
                                        <div>Nuevo Producto</div>
                                    </a>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <a href="ControladorCategoria?accion=nuevo" class="quick-action-btn" 
                                       style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                                        <i class="fas fa-tags fa-2x mb-2"></i>
                                        <div>Nueva Categor&iacute;a</div>
                                    </a>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <a href="ControladorTienda?accion=listarProductos" class="quick-action-btn" 
                                       style="background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);">
                                        <i class="fas fa-cash-register fa-2x mb-2"></i>
                                        <div>Compras</div>
                                    </a>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <a href="ControladorEmpleado?accion=listar" class="quick-action-btn" 
                                       style="background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); color: #333 !important;">
                                        <i class="fas fa-list fa-2x mb-2"></i>
                                        <div>Ver Empleados</div>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-lg-4">
                    <div class="card card-dashboard">
                        <div class="card-header bg-transparent border-0 pb-0">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-history me-2 text-info"></i>Actividad Reciente
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="list-group list-group-flush">
                                <div class="list-group-item d-flex justify-content-between align-items-center border-0 px-0">
                                    <div class="d-flex align-items-center">
                                        <i class="fas fa-sign-in-alt text-primary me-3"></i>
                                        <div>
                                            <small class="fw-bold">Inicio de sesión</small><br>
                                            <small class="text-muted"><%= nombreUsuario %> se conectó</small>
                                        </div>
                                    </div>
                                    <small class="text-muted">Ahora</small>
                                </div>
                                <div class="list-group-item d-flex justify-content-between align-items-center border-0 px-0">
                                    <div class="d-flex align-items-center">
                                        <i class="fas fa-users text-success me-3"></i>
                                        <div>
                                            <small class="fw-bold">Gestión empleados</small><br>
                                            <small class="text-muted"><%= totalEmpleados %> empleados activos</small>
                                        </div>
                                    </div>
                                    <small class="text-muted">Hoy</small>
                                </div>
                                <div class="list-group-item d-flex justify-content-between align-items-center border-0 px-0">
                                    <div class="d-flex align-items-center">
                                        <i class="fas fa-chart-line text-warning me-3"></i>
                                        <div>
                                            <small class="fw-bold">Sistema activo</small><br>
                                            <small class="text-muted">Todos los módulos funcionando</small>
                                        </div>
                                    </div>
                                    <small class="text-muted">Reciente</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Toggle Sidebar
        document.getElementById('sidebarToggle').addEventListener('click', function() {
            const sidebar = document.getElementById('sidebar');
            const mainContent = document.getElementById('mainContent');
            
            sidebar.classList.toggle('sidebar-collapsed');
            mainContent.classList.toggle('main-content-expanded');
            
            // Cambiar icono
            const icon = this.querySelector('i');
            if (sidebar.classList.contains('sidebar-collapsed')) {
                icon.className = 'fas fa-bars';
            } else {
                icon.className = 'fas fa-bars';
            }
        });

        // Update DateTime
        function updateDateTime() {
            const now = new Date();
            const options = { 
                weekday: 'long', 
                year: 'numeric', 
                month: 'long', 
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            };
            const dateTimeString = now.toLocaleDateString('es-ES', options);
            document.getElementById('currentDateTime').textContent = dateTimeString;
        }
        
        // Inicializar fecha/hora
        updateDateTime();
        setInterval(updateDateTime, 1000);

        // Auto-close alerts después de 5 segundos
        setTimeout(function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                if (alert.classList.contains('show')) {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }
            });
        }, 5000);

        // Mobile sidebar toggle para pantallas pequeñas
        function toggleMobileSidebar() {
            const sidebar = document.getElementById('sidebar');
            sidebar.classList.toggle('show');
        }

        // Inicializar tooltips
        document.addEventListener('DOMContentLoaded', function() {
            var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
            var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });
        });

        // Efectos hover en tarjetas
        document.addEventListener('DOMContentLoaded', function() {
            const cards = document.querySelectorAll('.card-dashboard');
            cards.forEach(card => {
                card.addEventListener('mouseenter', function() {
                    this.style.transform = 'translateY(-5px)';
                });
                card.addEventListener('mouseleave', function() {
                    this.style.transform = 'translateY(0)';
                });
            });
        });
    </script>
</body>
</html>