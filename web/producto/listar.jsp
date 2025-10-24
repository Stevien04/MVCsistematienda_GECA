<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.clsProducto"%>
<%
    // Verificar sesión
    Object empleadoObj = session.getAttribute("empleado");
    if (empleadoObj == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Obtener listas del request
    List<clsProducto> listaProductos = (List<clsProducto>) request.getAttribute("productos");
    List<clsProducto> listaProductosInactivos = (List<clsProducto>) request.getAttribute("productosInactivos");
    Boolean mostrarInactivos = (Boolean) request.getAttribute("mostrarInactivos");
    if (mostrarInactivos == null) {
        mostrarInactivos = false;
    }

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
        <title>Productos - Sistema Tienda</title>
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
            .color-badge {
                width: 20px;
                height: 20px;
                border-radius: 50%;
                display: inline-block;
                margin-right: 8px;
                border: 2px solid #dee2e6;
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
                border-bottom: 3px solid #28a745;
                background: transparent;
            }
            .tab-pane {
                padding-top: 1.5rem;
            }
            .text-strikethrough {
                text-decoration: line-through;
                opacity: 0.7;
            }
            .stock-low {
                color: #dc3545;
                font-weight: bold;
            }
            .stock-ok {
                color: #28a745;
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
                            <i class="fas fa-cubes me-2 text-primary"></i>Gestión de Productos
                        </h2>
                        <p class="text-muted mb-0">Administra los productos del sistema</p>
                    </div>
                    <div class="d-flex gap-2">
                        <div class="search-box">
                            <form action="${pageContext.request.contextPath}/ControladorProducto" method="GET" class="d-flex">
                                <input type="hidden" name="accion" value="buscar">
                                <input type="text" class="form-control" name="valor" placeholder="Buscar producto...">
                                <input type="hidden" name="criterio" value="nombre">
                                <button type="submit" class="btn btn-outline-primary ms-2">
                                    <i class="fas fa-search"></i>
                                </button>
                            </form>
                        </div>
                        <a href="${pageContext.request.contextPath}/ControladorProducto?accion=exportarExcel" class="btn btn-outline-success">
                            <i class="fas fa-file-excel me-2"></i>Excel
                        </a>
                        <a href="${pageContext.request.contextPath}/ControladorProducto?accion=exportarPdf" class="btn btn-outline-danger">
                            <i class="fas fa-file-pdf me-2"></i>PDF
                        </a>
                        <a href="${pageContext.request.contextPath}/ControladorProducto?accion=nuevo" class="btn btn-primary">
                            <i class="fas fa-plus me-2"></i>Nuevo Producto
                        </a>
                    </div>
                </div>

                <!-- Mensajes -->
                <% if (mensaje != null) {%>
                <div class="alert alert-<%= tipoMensaje != null ? tipoMensaje : "info"%> alert-dismissible fade show" role="alert">
                    <i class="fas fa-<%= "success".equals(tipoMensaje) ? "check-circle" : "exclamation-triangle"%> me-2"></i>
                    <%= mensaje%>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <% }%>

                <!-- Card -->
                <div class="card card-custom">
                    <div class="card-header bg-transparent">
                        <!-- Pestañas -->
                        <ul class="nav nav-tabs nav-tabs-custom" id="productosTab" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link <%= !mostrarInactivos ? "active" : ""%>" 
                                        id="activos-tab" 
                                        data-bs-toggle="tab" 
                                        data-bs-target="#activos" 
                                        type="button" 
                                        role="tab"
                                        onclick="cambiarTab('activos')">
                                    <i class="fas fa-cube me-2"></i>Productos Activos
                                    <span class="badge bg-success ms-2"><%= listaProductos != null ? listaProductos.size() : 0%></span>
                                </button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link <%= mostrarInactivos ? "active" : ""%>" 
                                        id="inactivos-tab" 
                                        data-bs-toggle="tab" 
                                        data-bs-target="#inactivos" 
                                        type="button" 
                                        role="tab"
                                        onclick="cambiarTab('inactivos')">
                                    <i class="fas fa-cube me-2"></i>Productos Inactivos
                                    <span class="badge bg-secondary ms-2"><%= listaProductosInactivos != null ? listaProductosInactivos.size() : 0%></span>
                                </button>
                            </li>
                        </ul>
                    </div>
                    <div class="card-body">
                        <div class="tab-content" id="productosTabContent">

                            <!-- Pestaña Activos -->
                            <div class="tab-pane fade <%= !mostrarInactivos ? "show active" : ""%>" id="activos" role="tabpanel">
                                <% if (listaProductos != null && !listaProductos.isEmpty()) { %>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Producto</th>
                                                <th>Categoría</th>
                                                <th>Modelo - Marca</th>
                                                <th>Color</th>
                                                <th>Precio</th>
                                                <th>Stock</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (int i = 0; i < listaProductos.size(); i++) {
                                                    clsProducto producto = listaProductos.get(i);
                                            %>
                                            <tr>
                                                <td><%= i + 1%></td>
                                                <td>
                                                    <div>
                                                        <strong><%= producto.getNombreproducto()%></strong>
                                                        <% if (producto.getDescripcion() != null && !producto.getDescripcion().isEmpty()) {%>
                                                        <div class="text-muted small"><%= producto.getDescripcion()%></div>
                                                        <% }%>
                                                    </div>
                                                </td>
                                                <td>
                                                    <span class="badge bg-primary badge-custom"><%= producto.getNombrecategoria()%></span>
                                                </td>
                                                <td>
                                                    <div class="small">
                                                        <div><strong><%= producto.getNombremodelo()%></strong></div>
                                                        <div class="text-muted"><%= producto.getNombremarca()%></div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <span class="color-badge" style="background-color: <%= producto.getCodigoHex() != null ? producto.getCodigoHex() : "#6c757d"%>"></span>
                                                        <span><%= producto.getNombrecolor()%></span>
                                                    </div>
                                                </td>
                                                <td>
                                                    <span class="fw-bold text-success">S/ <%= producto.getPrecio() != null ? String.format("%.2f", producto.getPrecio()) : "0.00"%></span>
                                                </td>
                                                <td>
                                                    <span class="<%= producto.getStock() <= 5 ? "stock-low" : "stock-ok"%>">
                                                        <%= producto.getStock()%>
                                                    </span>
                                                </td>
                                                <td>
                                                    <div class="btn-group">
                                                        <a href="${pageContext.request.contextPath}/ControladorProducto?accion=editar&id=<%= producto.getIdproducto()%>" 
                                                           class="btn btn-sm btn-outline-primary btn-action" 
                                                           title="Editar">
                                                            <i class="fas fa-edit"></i>
                                                        </a>
                                                        <button onclick="confirmarEliminacion(<%= producto.getIdproducto()%>, '<%= producto.getNombreproducto()%>')" 
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
                                    <i class="fas fa-cube fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No hay productos activos</h5>
                                    <p class="text-muted">Todos los productos están inactivos o no hay registros.</p>
                                    <a href="${pageContext.request.contextPath}/ControladorProducto?accion=nuevo" class="btn btn-primary">
                                        <i class="fas fa-plus me-2"></i>Agregar Producto
                                    </a>
                                </div>
                                <% }%>
                            </div>

                            <!-- Pestaña Inactivos -->
                            <div class="tab-pane fade <%= mostrarInactivos ? "show active" : ""%>" id="inactivos" role="tabpanel">
                                <% if (listaProductosInactivos != null && !listaProductosInactivos.isEmpty()) { %>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Producto</th>
                                                <th>Categoría</th>
                                                <th>Modelo - Marca</th>
                                                <th>Color</th>
                                                <th>Precio</th>
                                                <th>Stock</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (int i = 0; i < listaProductosInactivos.size(); i++) {
                                                    clsProducto producto = listaProductosInactivos.get(i);
                                            %>
                                            <tr class="text-muted">
                                                <td><%= i + 1%></td>
                                                <td>
                                                    <div>
                                                        <strong class="text-strikethrough"><%= producto.getNombreproducto()%></strong>
                                                        <% if (producto.getDescripcion() != null && !producto.getDescripcion().isEmpty()) {%>
                                                        <div class="small"><%= producto.getDescripcion()%></div>
                                                        <% }%>
                                                    </div>
                                                </td>
                                                <td>
                                                    <span class="badge bg-secondary badge-custom"><%= producto.getNombrecategoria()%></span>
                                                </td>
                                                <td>
                                                    <div class="small">
                                                        <div><strong><%= producto.getNombremodelo()%></strong></div>
                                                        <div><%= producto.getNombremarca()%></div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <span class="color-badge" style="background-color: <%= producto.getCodigoHex() != null ? producto.getCodigoHex() : "#6c757d"%>; opacity: 0.7;"></span>
                                                        <span><%= producto.getNombrecolor()%></span>
                                                    </div>
                                                </td>
                                                <td>
                                                    <span class="fw-bold">S/ <%= producto.getPrecio() != null ? String.format("%.2f", producto.getPrecio()) : "0.00"%></span>
                                                </td>
                                                <td>
                                                    <span><%= producto.getStock()%></span>
                                                </td>
                                                <td>
                                                    <div class="btn-group">
                                                        <button onclick="confirmarReactivacion(<%= producto.getIdproducto()%>, '<%= producto.getNombreproducto()%>')" 
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
                                    <i class="fas fa-cube fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No hay productos inactivos</h5>
                                    <p class="text-muted">Todos los productos están activos en el sistema.</p>
                                </div>
                                <% }%>
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
                        <p>¿Está seguro de desactivar el producto: <strong id="productoNombre"></strong>?</p>
                        <p class="text-muted small">
                            <i class="fas fa-info-circle me-1"></i>
                            El producto será movido a la lista de inactivos y podrá ser reactivado después.
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-1"></i>Cancelar
                        </button>
                        <a id="btnEliminar" href="#" class="btn btn-warning">
                            <i class="fas fa-cube me-1"></i>Desactivar
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
                        <p>¿Está seguro de reactivar el producto: <strong id="productoNombreReactivar"></strong>?</p>
                        <p class="text-muted small">
                            <i class="fas fa-info-circle me-1"></i>
                            El producto volverá a estar activo en el sistema.
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-1"></i>Cancelar
                        </button>
                        <a id="btnReactivar" href="#" class="btn btn-success">
                            <i class="fas fa-cube me-1"></i>Reactivar
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                                                    function cambiarTab(tab) {
                                                                        if (tab === 'inactivos') {
                                                                            window.location.href = '${pageContext.request.contextPath}/ControladorProducto?accion=listarInactivos';
                                                                        } else {
                                                                            window.location.href = '${pageContext.request.contextPath}/ControladorProducto?accion=listar';
                                                                        }
                                                                    }

                                                                    function confirmarEliminacion(id, nombre) {
                                                                        document.getElementById('productoNombre').textContent = nombre;
                                                                        document.getElementById('btnEliminar').href = '${pageContext.request.contextPath}/ControladorProducto?accion=eliminar&id=' + id;
                                                                        new bootstrap.Modal(document.getElementById('confirmModal')).show();
                                                                    }

                                                                    function confirmarReactivacion(id, nombre) {
                                                                        document.getElementById('productoNombreReactivar').textContent = nombre;
                                                                        document.getElementById('btnReactivar').href = '${pageContext.request.contextPath}/ControladorProducto?accion=reactivar&id=' + id;
                                                                        new bootstrap.Modal(document.getElementById('reactivarModal')).show();
                                                                    }

                                                                    // Auto-close alerts
                                                                    setTimeout(function () {
                                                                        const alerts = document.querySelectorAll('.alert');
                                                                        alerts.forEach(alert => {
                                                                            if (alert.classList.contains('show')) {
                                                                                const bsAlert = new bootstrap.Alert(alert);
                                                                                bsAlert.close();
                                                                            }
                                                                        });
                                                                    }, 5000);

                                                                    // Tooltips
                                                                    document.addEventListener('DOMContentLoaded', function () {
                                                                        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[title]'));
                                                                        const tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                                                                            return new bootstrap.Tooltip(tooltipTriggerEl);
                                                                        });
                                                                    });
        </script>
    </body>
</html>