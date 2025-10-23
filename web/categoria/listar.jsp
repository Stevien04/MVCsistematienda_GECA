<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.clsCategoria"%>
<%
    // Verificar sesión
    Object empleadoObj = session.getAttribute("empleado");
    if (empleadoObj == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Obtener listas del request
    List<clsCategoria> listaCategorias = (List<clsCategoria>) request.getAttribute("categorias");
    List<clsCategoria> listaCategoriasInactivas = (List<clsCategoria>) request.getAttribute("categoriasInactivas");
    Boolean mostrarInactivas = (Boolean) request.getAttribute("mostrarInactivas");
    if (mostrarInactivas == null) mostrarInactivas = false;
    
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
    <title>Categorías - Sistema Tienda</title>
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
        .categoria-icon {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: linear-gradient(135deg, #17a2b8 0%, #20c997 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 16px;
        }
        .categoria-icon-inactive {
            background: linear-gradient(135deg, #6c757d 0%, #495057 100%);
            opacity: 0.7;
        }
        .search-box {
            max-width: 300px;
        }
        .badge-custom {
            font-size: 0.75rem;
            padding: 0.35em 0.65em;
        }
        .nav-tabs .nav-link {
            border: none;
            color: #6c757d;
            font-weight: 500;
            padding: 0.75rem 1.5rem;
        }
        .nav-tabs .nav-link.active {
            color: #495057;
            border-bottom: 3px solid #17a2b8;
            background: transparent;
        }
        .tab-pane {
            padding-top: 1.5rem;
        }
        .text-strikethrough {
            text-decoration: line-through;
            opacity: 0.7;
        }
        .descripcion-text {
            color: #6c757d;
            font-size: 0.875rem;
            line-height: 1.4;
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
                        <i class="fas fa-tags me-2 text-primary"></i>Gestión de Categorías
                    </h2>
                    <p class="text-muted mb-0">Administra las categorías del sistema</p>
                </div>
                <div class="d-flex gap-2">
                    <div class="search-box">
                        <form action="${pageContext.request.contextPath}/ControladorCategoria" method="GET" class="d-flex">
                            <input type="hidden" name="accion" value="buscar">
                            <input type="text" class="form-control" name="valor" placeholder="Buscar categoría...">
                            <input type="hidden" name="criterio" value="nombre">
                            <button type="submit" class="btn btn-outline-primary ms-2">
                                <i class="fas fa-search"></i>
                            </button>
                        </form>
                    </div>
                    <a href="${pageContext.request.contextPath}/ControladorCategoria?accion=nuevo" class="btn btn-primary">
                        <i class="fas fa-plus me-2"></i>Nueva Categoría
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
                    <ul class="nav nav-tabs nav-tabs-custom" id="categoriasTab" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link <%= !mostrarInactivas ? "active" : "" %>" 
                                    id="activas-tab" 
                                    data-bs-toggle="tab" 
                                    data-bs-target="#activas" 
                                    type="button" 
                                    role="tab"
                                    onclick="cambiarTab('activas')">
                                <i class="fas fa-tag me-2"></i>Categorías Activas
                                <span class="badge bg-success ms-2"><%= listaCategorias != null ? listaCategorias.size() : 0 %></span>
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link <%= mostrarInactivas ? "active" : "" %>" 
                                    id="inactivas-tab" 
                                    data-bs-toggle="tab" 
                                    data-bs-target="#inactivas" 
                                    type="button" 
                                    role="tab"
                                    onclick="cambiarTab('inactivas')">
                                <i class="fas fa-tag me-2"></i>Categorías Inactivas
                                <span class="badge bg-secondary ms-2"><%= listaCategoriasInactivas != null ? listaCategoriasInactivas.size() : 0 %></span>
                            </button>
                        </li>
                    </ul>
                </div>
                <div class="card-body">
                    <div class="tab-content" id="categoriasTabContent">
                        
                        <!-- Pestaña Activas -->
                        <div class="tab-pane fade <%= !mostrarInactivas ? "show active" : "" %>" id="activas" role="tabpanel">
                            <% if (listaCategorias != null && !listaCategorias.isEmpty()) { %>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Categoría</th>
                                                <th>Descripción</th>
                                                <th>Estado</th>
                                                <th>Fecha Creación</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (int i = 0; i < listaCategorias.size(); i++) { 
                                                clsCategoria categoria = listaCategorias.get(i);
                                            %>
                                                <tr>
                                                    <td><%= i + 1 %></td>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <div class="categoria-icon me-3">
                                                                <i class="fas fa-tag"></i>
                                                            </div>
                                                            <div>
                                                                <strong><%= categoria.getNombrecategoria() %></strong>
                                                                <div class="text-muted small">ID: <%= categoria.getIdcategoria() %></div>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <% if (categoria.getDescripcion() != null && !categoria.getDescripcion().isEmpty()) { %>
                                                            <div class="descripcion-text"><%= categoria.getDescripcion() %></div>
                                                        <% } else { %>
                                                            <span class="text-muted">Sin descripción</span>
                                                        <% } %>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-success badge-custom">Activa</span>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-light text-dark">
                                                            <% if (categoria.getFechaCreacion() != null) { %>
                                                                <%= categoria.getFechaCreacion().toLocalDate() %>
                                                            <% } else { %>
                                                                N/A
                                                            <% } %>
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group">
                                                            <a href="${pageContext.request.contextPath}/ControladorCategoria?accion=editar&id=<%= categoria.getIdcategoria() %>" 
                                                               class="btn btn-sm btn-outline-primary btn-action" 
                                                               title="Editar">
                                                                <i class="fas fa-edit"></i>
                                                            </a>
                                                            <button onclick="confirmarEliminacion(<%= categoria.getIdcategoria() %>, '<%= categoria.getNombrecategoria() %>')" 
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
                                    <i class="fas fa-tags fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No hay categorías activas</h5>
                                    <p class="text-muted">Todas las categorías están inactivas o no hay registros.</p>
                                    <a href="${pageContext.request.contextPath}/ControladorCategoria?accion=nuevo" class="btn btn-primary">
                                        <i class="fas fa-plus me-2"></i>Agregar Categoría
                                    </a>
                                </div>
                            <% } %>
                        </div>
                        
                        <!-- Pestaña Inactivas -->
                        <div class="tab-pane fade <%= mostrarInactivas ? "show active" : "" %>" id="inactivas" role="tabpanel">
                            <% if (listaCategoriasInactivas != null && !listaCategoriasInactivas.isEmpty()) { %>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Categoría</th>
                                                <th>Descripción</th>
                                                <th>Estado</th>
                                                <th>Fecha Creación</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (int i = 0; i < listaCategoriasInactivas.size(); i++) { 
                                                clsCategoria categoria = listaCategoriasInactivas.get(i);
                                            %>
                                                <tr class="text-muted">
                                                    <td><%= i + 1 %></td>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <div class="categoria-icon categoria-icon-inactive me-3">
                                                                <i class="fas fa-tag"></i>
                                                            </div>
                                                            <div>
                                                                <strong class="text-strikethrough"><%= categoria.getNombrecategoria() %></strong>
                                                                <div class="small">ID: <%= categoria.getIdcategoria() %></div>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <% if (categoria.getDescripcion() != null && !categoria.getDescripcion().isEmpty()) { %>
                                                            <div class="descripcion-text"><%= categoria.getDescripcion() %></div>
                                                        <% } else { %>
                                                            <span>Sin descripción</span>
                                                        <% } %>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-secondary badge-custom">Inactiva</span>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-light text-dark">
                                                            <% if (categoria.getFechaCreacion() != null) { %>
                                                                <%= categoria.getFechaCreacion().toLocalDate() %>
                                                            <% } else { %>
                                                                N/A
                                                            <% } %>
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group">
                                                            <button onclick="confirmarReactivacion(<%= categoria.getIdcategoria() %>, '<%= categoria.getNombrecategoria() %>')" 
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
                                    <i class="fas fa-tags fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No hay categorías inactivas</h5>
                                    <p class="text-muted">Todas las categorías están activas en el sistema.</p>
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
                    <p>¿Está seguro de desactivar la categoría: <strong id="categoriaNombre"></strong>?</p>
                    <p class="text-muted small">
                        <i class="fas fa-info-circle me-1"></i>
                        La categoría será movida a la lista de inactivas y podrá ser reactivada después.
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-1"></i>Cancelar
                    </button>
                    <a id="btnEliminar" href="#" class="btn btn-warning">
                        <i class="fas fa-tag me-1"></i>Desactivar
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
                    <p>¿Está seguro de reactivar la categoría: <strong id="categoriaNombreReactivar"></strong>?</p>
                    <p class="text-muted small">
                        <i class="fas fa-info-circle me-1"></i>
                        La categoría volverá a estar activa en el sistema.
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-1"></i>Cancelar
                    </button>
                    <a id="btnReactivar" href="#" class="btn btn-success">
                        <i class="fas fa-tag me-1"></i>Reactivar
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function cambiarTab(tab) {
            if (tab === 'inactivas') {
                window.location.href = '${pageContext.request.contextPath}/ControladorCategoria?accion=listarInactivas';
            } else {
                window.location.href = '${pageContext.request.contextPath}/ControladorCategoria?accion=listar';
            }
        }

        function confirmarEliminacion(id, nombre) {
            document.getElementById('categoriaNombre').textContent = nombre;
            document.getElementById('btnEliminar').href = '${pageContext.request.contextPath}/ControladorCategoria?accion=eliminar&id=' + id;
            new bootstrap.Modal(document.getElementById('confirmModal')).show();
        }

        function confirmarReactivacion(id, nombre) {
            document.getElementById('categoriaNombreReactivar').textContent = nombre;
            document.getElementById('btnReactivar').href = '${pageContext.request.contextPath}/ControladorCategoria?accion=reactivar&id=' + id;
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

        // Tooltips
        document.addEventListener('DOMContentLoaded', function() {
            const tooltipTriggerList = [].slice.call(document.querySelectorAll('[title]'));
            const tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });
        });
    </script>
</body>
</html>