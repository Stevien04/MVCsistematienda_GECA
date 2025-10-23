<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.clsEmpleado"%>
<%
    // Verificar sesión
    Object empleadoObj = session.getAttribute("empleado");
    if (empleadoObj == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Obtener datos del request
    List<clsEmpleado> listaEmpleados = (List<clsEmpleado>) request.getAttribute("empleados");
    List<clsEmpleado> listaEmpleadosInactivos = (List<clsEmpleado>) request.getAttribute("empleadosInactivos");
    Boolean mostrarInactivos = (Boolean) request.getAttribute("mostrarInactivos");
    if (mostrarInactivos == null) mostrarInactivos = false;
    
    // Obtener mensajes
    String mensaje = (String) session.getAttribute("mensaje");
    String tipoMensaje = (String) session.getAttribute("tipoMensaje");
    
    if (mensaje != null) {
        session.removeAttribute("mensaje");
        session.removeAttribute("tipoMensaje");
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Empleados - Sistema Tienda</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    
    <!-- Incluir estilos -->
    <jsp:include page="/includes/styles.jsp" />
    
    <style>
        .card-custom {
            border: none;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
        }
        .btn-action {
            width: 35px;
            height: 35px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            margin: 0 2px;
            border-radius: 8px;
        }
        .table th {
            border-top: none;
            font-weight: 600;
            color: #495057;
            background-color: #f8f9fa;
            padding: 1rem 0.75rem;
        }
        .table td {
            padding: 1rem 0.75rem;
            vertical-align: middle;
        }
        .avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: bold;
            font-size: 14px;
        }
        .avatar-inactive {
            background: linear-gradient(135deg, #6c757d 0%, #495057 100%);
            opacity: 0.7;
        }
        .badge-cargo {
            font-size: 0.75rem;
            padding: 0.35em 0.65em;
        }
        .search-box {
            max-width: 300px;
        }
        .nav-tabs .nav-link {
            border: none;
            color: #6c757d;
            font-weight: 500;
            padding: 0.75rem 1.5rem;
        }
        .nav-tabs .nav-link.active {
            color: #495057;
            border-bottom: 3px solid #667eea;
            background: transparent;
        }
        .tab-pane {
            padding-top: 1.5rem;
        }
        .text-strikethrough {
            text-decoration: line-through;
            opacity: 0.7;
        }
    </style>
</head>
<body>
    <!-- Incluir Sidebar -->
    <jsp:include page="/includes/sidebar.jsp" />

    <!-- Main Content -->
    <div class="main-content" id="mainContent">
        <!-- Incluir Header -->
        <jsp:include page="/includes/header.jsp" />

        <!-- Content -->
        <div class="container-fluid p-4">
            <!-- Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="h4 mb-1">
                        <i class="fas fa-users me-2 text-primary"></i>Gestión de Empleados
                    </h2>
                    <p class="text-muted mb-0">Administra los empleados del sistema</p>
                </div>
                <div class="d-flex gap-2">
                    <div class="search-box">
                        <form action="${pageContext.request.contextPath}/ControladorEmpleado" method="GET" class="d-flex">
                            <input type="hidden" name="accion" value="buscar">
                            <input type="text" class="form-control" name="valor" placeholder="Buscar empleado...">
                            <input type="hidden" name="criterio" value="nombre">
                            <button type="submit" class="btn btn-outline-primary ms-2">
                                <i class="fas fa-search"></i>
                            </button>
                        </form>
                    </div>
                    <a href="${pageContext.request.contextPath}/ControladorEmpleado?accion=nuevo" class="btn btn-primary">
                        <i class="fas fa-plus me-2"></i>Nuevo Empleado
                    </a>
                </div>
            </div>

            <!-- Mensajes -->
            <% if (mensaje != null) { %>
                <div class="alert alert-<%= tipoMensaje != null ? tipoMensaje : "info" %> alert-dismissible fade show" role="alert">
                    <i class="fas fa-<%= "success".equals(tipoMensaje) ? "check-circle" : "exclamation-triangle" %> me-2"></i>
                    <%= mensaje %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            <% } %>

            <!-- Card -->
            <div class="card card-custom">
                <div class="card-header bg-transparent">
                    <!-- Pestañas -->
                    <ul class="nav nav-tabs nav-tabs-custom" id="empleadosTab" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link <%= !mostrarInactivos ? "active" : "" %>" 
                                    id="activos-tab" 
                                    data-bs-toggle="tab" 
                                    data-bs-target="#activos" 
                                    type="button" 
                                    role="tab"
                                    onclick="cambiarTab('activos')">
                                <i class="fas fa-user-check me-2"></i>Empleados Activos
                                <span class="badge bg-success ms-2"><%= listaEmpleados != null ? listaEmpleados.size() : 0 %></span>
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link <%= mostrarInactivos ? "active" : "" %>" 
                                    id="inactivos-tab" 
                                    data-bs-toggle="tab" 
                                    data-bs-target="#inactivos" 
                                    type="button" 
                                    role="tab"
                                    onclick="cambiarTab('inactivos')">
                                <i class="fas fa-user-slash me-2"></i>Empleados Inactivos
                                <span class="badge bg-secondary ms-2"><%= listaEmpleadosInactivos != null ? listaEmpleadosInactivos.size() : 0 %></span>
                            </button>
                        </li>
                    </ul>
                </div>
                <div class="card-body">
                    <div class="tab-content" id="empleadosTabContent">
                        
                        <!-- Pestaña Activos -->
                        <div class="tab-pane fade <%= !mostrarInactivos ? "show active" : "" %>" id="activos" role="tabpanel">
                            <% if (listaEmpleados != null && !listaEmpleados.isEmpty()) { %>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Empleado</th>
                                                <th>DNI</th>
                                                <th>Contacto</th>
                                                <th>Cargo</th>
                                                <th>Salario</th>
                                                <th>Usuario</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (int i = 0; i < listaEmpleados.size(); i++) { 
                                                clsEmpleado empleado = listaEmpleados.get(i);
                                                String iniciales = empleado.getNombre().charAt(0) + "" + empleado.getApellido().charAt(0);
                                            %>
                                                <tr>
                                                    <td><%= i + 1 %></td>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <div class="avatar me-3">
                                                                <%= iniciales %>
                                                            </div>
                                                            <div>
                                                                <strong><%= empleado.getNombre() + " " + empleado.getApellido() %></strong>
                                                                <div class="text-muted small"><%= empleado.getEmail() != null ? empleado.getEmail() : "Sin email" %></div>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td><%= empleado.getDni() %></td>
                                                    <td>
                                                        <div class="small">
                                                            <div><i class="fas fa-phone me-1"></i> <%= empleado.getTelefono() != null ? empleado.getTelefono() : "N/A" %></div>
                                                            <div class="text-truncate" style="max-width: 150px;">
                                                                <i class="fas fa-map-marker-alt me-1"></i> <%= empleado.getDireccion() != null ? empleado.getDireccion() : "N/A" %>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-primary badge-cargo"><%= empleado.getNombrecargo() != null ? empleado.getNombrecargo() : "Sin cargo" %></span>
                                                    </td>
                                                    <td>
                                                        <span class="fw-bold text-success">S/ <%= empleado.getSalario() != null ? String.format("%.2f", empleado.getSalario()) : "0.00" %></span>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-secondary"><%= empleado.getUsuario() %></span>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group">
                                                            <a href="${pageContext.request.contextPath}/ControladorEmpleado?accion=editar&id=<%= empleado.getIdempleado() %>" 
                                                               class="btn btn-sm btn-outline-primary btn-action" 
                                                               title="Editar">
                                                                <i class="fas fa-edit"></i>
                                                            </a>
                                                            <button onclick="confirmarEliminacion(<%= empleado.getIdempleado() %>, '<%= empleado.getNombre() %> <%= empleado.getApellido() %>')" 
                                                                    class="btn btn-sm btn-outline-danger btn-action" 
                                                                    title="Desactivar">
                                                                <i class="fas fa-trash"></i>
                                                            </button>
                                                        </div>
                                                    </td>
                                                </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </div>
                            <% } else { %>
                                <div class="text-center py-5">
                                    <i class="fas fa-user-check fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No hay empleados activos</h5>
                                    <p class="text-muted">Todos los empleados están inactivos o no hay registros.</p>
                                    <a href="${pageContext.request.contextPath}/ControladorEmpleado?accion=nuevo" class="btn btn-primary">
                                        <i class="fas fa-plus me-2"></i>Agregar Empleado
                                    </a>
                                </div>
                            <% } %>
                        </div>
                        
                        <!-- Pestaña Inactivos -->
                        <div class="tab-pane fade <%= mostrarInactivos ? "show active" : "" %>" id="inactivos" role="tabpanel">
                            <% if (listaEmpleadosInactivos != null && !listaEmpleadosInactivos.isEmpty()) { %>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Empleado</th>
                                                <th>DNI</th>
                                                <th>Contacto</th>
                                                <th>Cargo</th>
                                                <th>Salario</th>
                                                <th>Usuario</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (int i = 0; i < listaEmpleadosInactivos.size(); i++) { 
                                                clsEmpleado empleado = listaEmpleadosInactivos.get(i);
                                                String iniciales = empleado.getNombre().charAt(0) + "" + empleado.getApellido().charAt(0);
                                            %>
                                                <tr class="text-muted">
                                                    <td><%= i + 1 %></td>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <div class="avatar avatar-inactive me-3">
                                                                <%= iniciales %>
                                                            </div>
                                                            <div>
                                                                <strong class="text-strikethrough"><%= empleado.getNombre() + " " + empleado.getApellido() %></strong>
                                                                <div class="small"><%= empleado.getEmail() != null ? empleado.getEmail() : "Sin email" %></div>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td><%= empleado.getDni() %></td>
                                                    <td>
                                                        <div class="small">
                                                            <div><i class="fas fa-phone me-1"></i> <%= empleado.getTelefono() != null ? empleado.getTelefono() : "N/A" %></div>
                                                            <div class="text-truncate" style="max-width: 150px;">
                                                                <i class="fas fa-map-marker-alt me-1"></i> <%= empleado.getDireccion() != null ? empleado.getDireccion() : "N/A" %>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-secondary badge-cargo"><%= empleado.getNombrecargo() != null ? empleado.getNombrecargo() : "Sin cargo" %></span>
                                                    </td>
                                                    <td>
                                                        <span class="fw-bold">S/ <%= empleado.getSalario() != null ? String.format("%.2f", empleado.getSalario()) : "0.00" %></span>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-light text-dark"><%= empleado.getUsuario() %></span>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group">
                                                            <button onclick="confirmarReactivacion(<%= empleado.getIdempleado() %>, '<%= empleado.getNombre() %> <%= empleado.getApellido() %>')" 
                                                                    class="btn btn-sm btn-outline-success btn-action" 
                                                                    title="Reactivar">
                                                                <i class="fas fa-undo"></i>
                                                            </button>
                                                        </div>
                                                    </td>
                                                </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </div>
                            <% } else { %>
                                <div class="text-center py-5">
                                    <i class="fas fa-user-slash fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No hay empleados inactivos</h5>
                                    <p class="text-muted">Todos los empleados están activos en el sistema.</p>
                                </div>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal de Confirmación Eliminación -->
    <div class="modal fade" id="confirmModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-exclamation-triangle text-warning me-2"></i>
                        Confirmar Desactivación
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>¿Está seguro de desactivar al empleado: <strong id="empleadoNombre"></strong>?</p>
                    <p class="text-muted small">
                        <i class="fas fa-info-circle me-1"></i>
                        El empleado ya no podrá acceder al sistema pero podrá ser reactivado después.
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-1"></i>Cancelar
                    </button>
                    <a id="btnEliminar" href="#" class="btn btn-warning">
                        <i class="fas fa-user-slash me-1"></i>Desactivar
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal de Confirmación Reactivación -->
    <div class="modal fade" id="reactivarModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-undo text-success me-2"></i>
                        Confirmar Reactivación
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>¿Está seguro de reactivar al empleado: <strong id="empleadoNombreReactivar"></strong>?</p>
                    <p class="text-muted small">
                        <i class="fas fa-info-circle me-1"></i>
                        El empleado podrá acceder al sistema nuevamente.
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-1"></i>Cancelar
                    </button>
                    <a id="btnReactivar" href="#" class="btn btn-success">
                        <i class="fas fa-user-check me-1"></i>Reactivar
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function cambiarTab(tab) {
          window.location.href = '${pageContext.request.contextPath}/ControladorEmpleado?accion=listar';
            }
        }         if (tab === 'inactivos') {
                window.location.href = '${pageContext.request.contextPath}/ControladorEmpleado?accion=listarInactivos';
            } else {
                window.location.href = '${pageContext.request.contextPath}/ControladorEmpleado?accion=listar';
            }
        }

        function confirmarEliminacion(id, nombre) {
            document.getElementById('empleadoNombre').textContent = nombre;
            document.getElementById('btnEliminar').href = '${pageContext.request.contextPath}/ControladorEmpleado?accion=eliminar&id=' + id;
            new bootstrap.Modal(document.getElementById('confirmModal')).show();
        }

        function confirmarReactivacion(id, nombre) {
            document.getElementById('empleadoNombreReactivar').textContent = nombre;
            document.getElementById('btnReactivar').href = '${pageContext.request.contextPath}/ControladorEmpleado?accion=reactivar&id=' + id;
            new bootstrap.Modal(document.getElementById('reactivarModal')).show();
        }

        // Auto-close alerts
        setTimeout(function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                if (alert.classList.contains('show')) {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }
            });
        }, 5000);
    </script>
</body>
</html>