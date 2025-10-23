<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.clsCategoria"%>
<%@page import="Modelo.clsModelo"%>
<%@page import="Modelo.clsColor"%>
<%
    // Verificar sesión
    Object empleadoObj = session.getAttribute("empleado");
    if (empleadoObj == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Obtener listas del request
    List<clsCategoria> listaCategorias = (List<clsCategoria>) request.getAttribute("categorias");
    List<clsModelo> listaModelos = (List<clsModelo>) request.getAttribute("modelos");
    List<clsColor> listaColores = (List<clsColor>) request.getAttribute("colores");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuevo Producto - Sistema Tienda</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    
    <!-- Incluir estilos -->
    <jsp:include page="/includes/styles.jsp" />
    
    <style>
        .card-form {
            border: none;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
        }
        .form-section {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
        }
        .form-section h6 {
            color: #495057;
            border-bottom: 2px solid #28a745;
            padding-bottom: 0.5rem;
            margin-bottom: 1rem;
        }
        .color-preview {
            width: 20px;
            height: 20px;
            border-radius: 50%;
            display: inline-block;
            margin-right: 8px;
            border: 2px solid #dee2e6;
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
                        <i class="fas fa-cube me-2 text-primary"></i>Nuevo Producto
                    </h2>
                    <p class="text-muted mb-0">Registrar nuevo producto en el sistema</p>
                </div>
                <a href="${pageContext.request.contextPath}/ControladorProducto?accion=listar" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Volver a la lista
                </a>
            </div>

            <!-- Form -->
            <div class="card card-form">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/ControladorProducto" method="POST" id="productoForm">
                        <input type="hidden" name="accion" value="agregar">
                        
                        <!-- Información Básica -->
                        <div class="form-section">
                            <h6>
                                <i class="fas fa-info-circle me-2"></i>Información Básica
                            </h6>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Nombre del Producto <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="nombre" required 
                                           placeholder="Ingrese el nombre del producto" maxlength="100">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Descripción</label>
                                    <textarea class="form-control" name="descripcion" rows="2" 
                                              placeholder="Descripción del producto" maxlength="255"></textarea>
                                </div>
                            </div>
                        </div>

                        <!-- Categorización -->
                        <div class="form-section">
                            <h6>
                                <i class="fas fa-tags me-2"></i>Categorización
                            </h6>
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Categoría <span class="text-danger">*</span></label>
                                    <select class="form-select" name="idCategoria" required>
                                        <option value="">Seleccionar categoría</option>
                                        <% if (listaCategorias != null) { 
                                            for (clsCategoria categoria : listaCategorias) { %>
                                                <option value="<%= categoria.getIdcategoria() %>">
                                                    <%= categoria.getNombrecategoria() %>
                                                </option>
                                            <% }
                                        } %>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Modelo <span class="text-danger">*</span></label>
                                    <select class="form-select" name="idModelo" required>
                                        <option value="">Seleccionar modelo</option>
                                        <% if (listaModelos != null) { 
                                            for (clsModelo modelo : listaModelos) { %>
                                                <option value="<%= modelo.getIdmodelo() %>">
                                                    <%= modelo.getNombremodelo() %> - <%= modelo.getNombremarca() %>
                                                </option>
                                            <% }
                                        } %>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Color <span class="text-danger">*</span></label>
                                    <select class="form-select" name="idColor" required>
                                        <option value="">Seleccionar color</option>
                                        <% if (listaColores != null) { 
                                            for (clsColor color : listaColores) { %>
                                                <option value="<%= color.getIdcolor() %>" data-hex="<%= color.getCodigoHex() != null ? color.getCodigoHex() : "#6c757d" %>">
                                                    <span class="color-preview" style="background-color: <%= color.getCodigoHex() != null ? color.getCodigoHex() : "#6c757d" %>"></span>
                                                    <%= color.getNombrecolor() %>
                                                </option>
                                            <% }
                                        } %>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <!-- Precio y Stock -->
                        <div class="form-section">
                            <h6>
                                <i class="fas fa-chart-line me-2"></i>Precio y Stock
                            </h6>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Precio (S/) <span class="text-danger">*</span></label>
                                    <input type="number" step="0.01" class="form-control" name="precio" required 
                                           placeholder="0.00" min="0" value="0.00">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Stock <span class="text-danger">*</span></label>
                                    <input type="number" class="form-control" name="stock" required 
                                           placeholder="0" min="0" value="0">
                                </div>
                            </div>
                        </div>

                        <!-- Botones -->
                        <div class="d-grid gap-2 d-md-flex justify-content-md-end border-top pt-4">
                            <a href="${pageContext.request.contextPath}/ControladorProducto?accion=listar" class="btn btn-secondary me-md-2">
                                <i class="fas fa-times me-2"></i>Cancelar
                            </a>
                            <button type="submit" class="btn btn-primary" id="btnGuardar">
                                <i class="fas fa-save me-2"></i>Guardar Producto
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('productoForm');
            const btnGuardar = document.getElementById('btnGuardar');
            
            // Validación de precios negativos
            const precioInput = document.querySelector('input[name="precio"]');
            precioInput.addEventListener('input', function() {
                if (this.value < 0) {
                    this.value = 0;
                }
            });
            
            // Validación de stock negativo
            const stockInput = document.querySelector('input[name="stock"]');
            stockInput.addEventListener('input', function() {
                if (this.value < 0) {
                    this.value = 0;
                }
            });
            
            // Loading state en el botón
            form.addEventListener('submit', function() {
                btnGuardar.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Guardando...';
                btnGuardar.disabled = true;
            });
        });
    </script>
</body>
</html>