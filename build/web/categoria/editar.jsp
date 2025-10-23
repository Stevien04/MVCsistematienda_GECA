<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.clsCategoria"%>
<%
    // Verificar sesión
    Object empleadoObj = session.getAttribute("empleado");
    if (empleadoObj == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Obtener datos del request
    clsCategoria categoria = (clsCategoria) request.getAttribute("categoria");
    
    if (categoria == null) {
        response.sendRedirect(request.getContextPath() + "/ControladorCategoria?accion=listar");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Categoría - Sistema Tienda</title>
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
            border-bottom: 2px solid #17a2b8;
            padding-bottom: 0.5rem;
            margin-bottom: 1rem;
        }
        .categoria-badge {
            font-size: 0.9rem;
            padding: 0.5em 1em;
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
                        <i class="fas fa-edit me-2 text-primary"></i>Editar Categoría
                    </h2>
                    <p class="text-muted mb-0">Modificar información de la categoría</p>
                </div>
                <a href="${pageContext.request.contextPath}/ControladorCategoria?accion=listar" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Volver a la lista
                </a>
            </div>

            <!-- Form -->
            <div class="card card-form">
                <div class="card-body">
                    <!-- Info de la Categoría -->
                    <div class="text-center mb-4">
                        <div class="d-flex justify-content-center align-items-center mb-3">
                            <span class="categoria-badge bg-info text-white me-3">
                                <i class="fas fa-tag me-2"></i>ID: <%= categoria.getIdcategoria() %>
                            </span>
                            <div class="text-start">
                                <h5><%= categoria.getNombrecategoria() %></h5>
                                <p class="text-muted mb-0">
                                    Estado: 
                                    <span class="badge <%= categoria.getEstado() == 1 ? "bg-success" : "bg-secondary" %>">
                                        <%= categoria.getEstado() == 1 ? "Activo" : "Inactivo" %>
                                    </span>
                                </p>
                            </div>
                        </div>
                    </div>

                    <form action="${pageContext.request.contextPath}/ControladorCategoria" method="POST" id="categoriaForm">
                        <input type="hidden" name="accion" value="actualizar">
                        <input type="hidden" name="id" value="<%= categoria.getIdcategoria() %>">
                        
                        <!-- Información de la Categoría -->
                        <div class="form-section">
                            <h6>
                                <i class="fas fa-info-circle me-2"></i>Información de la Categoría
                            </h6>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Nombre de la Categoría <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="nombre" required 
                                           value="<%= categoria.getNombrecategoria() %>" maxlength="100">
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Descripción</label>
                                <textarea class="form-control" name="descripcion" rows="3" 
                                          maxlength="255"><%= categoria.getDescripcion() != null ? categoria.getDescripcion() : "" %></textarea>
                            </div>
                        </div>

                        <!-- Botones -->
                        <div class="d-grid gap-2 d-md-flex justify-content-md-end border-top pt-4">
                            <a href="${pageContext.request.contextPath}/ControladorCategoria?accion=listar" class="btn btn-secondary me-md-2">
                                <i class="fas fa-times me-2"></i>Cancelar
                            </a>
                            <button type="submit" class="btn btn-primary" id="btnGuardar">
                                <i class="fas fa-save me-2"></i>Actualizar Categoría
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
            const form = document.getElementById('categoriaForm');
            const btnGuardar = document.getElementById('btnGuardar');
            
            // Loading state en el botón
            form.addEventListener('submit', function() {
                btnGuardar.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Actualizando...';
                btnGuardar.disabled = true;
            });
        });
    </script>
</body>
</html>