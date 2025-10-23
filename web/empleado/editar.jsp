<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.clsEmpleado"%>
<%@page import="Modelo.clsCargo"%>
<%
    // Verificar sesión
    Object empleadoObj = session.getAttribute("empleado");
    if (empleadoObj == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // Obtener datos del request
    clsEmpleado empleado = (clsEmpleado) request.getAttribute("empleado");
    List<clsCargo> listaCargos = (List<clsCargo>) request.getAttribute("cargos");
    
    if (empleado == null) {
        response.sendRedirect("ControladorEmpleado?accion=listar");
        return;
    }
    
    String iniciales = empleado.getNombre().charAt(0) + "" + empleado.getApellido().charAt(0);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Empleado - Sistema Tienda</title>
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
            border-bottom: 2px solid #667eea;
            padding-bottom: 0.5rem;
            margin-bottom: 1rem;
        }
        .user-avatar-large {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: bold;
            font-size: 1.5rem;
            margin: 0 auto 1rem;
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
                        <i class="fas fa-edit me-2 text-primary"></i>Editar Empleado
                    </h2>
                    <p class="text-muted mb-0">Modificar información del empleado</p>
                </div>
                <a href="ControladorEmpleado?accion=listar" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Volver a la lista
                </a>
            </div>
            
            <!-- === PEGA ESTE BLOQUE DEBUG AQUÍ === -->
            <div class="alert alert-info alert-dismissible fade show" role="alert">
                <strong>🔧 DEBUG FORMULARIO EDICIÓN:</strong> 
                <br>
                • Empleado ID: <strong><%= empleado != null ? empleado.getIdempleado() : "NULL"%></strong>
                <br>
                • Cargos disponibles: <strong><%= listaCargos != null ? listaCargos.size() : "NULL"%></strong>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <!-- === FIN DEBUG === -->

            <!-- Form -->
            <div class="card card-form">
                <div class="card-body">
                    <!-- Avatar y Info -->
                    <div class="text-center mb-4">
                        <div class="user-avatar-large">
                            <%= iniciales %>
                        </div>
                        <h5><%= empleado.getNombre() + " " + empleado.getApellido() %></h5>
                        <p class="text-muted"><%= empleado.getNombrecargo() != null ? empleado.getNombrecargo() : "Sin cargo" %> | ID: <%= empleado.getIdempleado() %></p>
                    </div>

                    <form action="ControladorEmpleado" method="POST" id="empleadoForm">
                        <input type="hidden" name="accion" value="actualizar">
                        <input type="hidden" name="id" value="<%= empleado.getIdempleado() %>">
                        
                        <!-- Información Personal -->
                        <div class="form-section">
                            <h6>
                                <i class="fas fa-user me-2"></i>Información Personal
                            </h6>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Nombre <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="nombre" required 
                                           value="<%= empleado.getNombre() %>" maxlength="50">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Apellido <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="apellido" required 
                                           value="<%= empleado.getApellido() %>" maxlength="50">
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">DNI <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="dni" maxlength="8" required 
                                           value="<%= empleado.getDni() %>" pattern="[0-9]{8}">
                                    <div class="form-text">Solo números, 8 dígitos</div>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Teléfono</label>
                                    <input type="tel" class="form-control" name="telefono" 
                                           value="<%= empleado.getTelefono() != null ? empleado.getTelefono() : "" %>" maxlength="15">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Email</label>
                                    <input type="email" class="form-control" name="email" 
                                           value="<%= empleado.getEmail() != null ? empleado.getEmail() : "" %>" maxlength="100">
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-semibold">Dirección</label>
                                <textarea class="form-control" name="direccion" rows="2" maxlength="200"><%= empleado.getDireccion() != null ? empleado.getDireccion() : "" %></textarea>
                            </div>
                        </div>

                        <!-- Información Laboral -->
                        <div class="form-section">
                            <h6>
                                <i class="fas fa-briefcase me-2"></i>Información Laboral
                            </h6>
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Fecha Contrato <span class="text-danger">*</span></label>
                                    <input type="date" class="form-control" name="fechaContrato" required 
                                           value="<%= empleado.getFechaContrato() %>">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Salario (S/) <span class="text-danger">*</span></label>
                                    <input type="number" step="0.01" class="form-control" name="salario" required 
                                           value="<%= empleado.getSalario() != null ? empleado.getSalario() : "0.00" %>" min="0">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Cargo <span class="text-danger">*</span></label>
                                    <select class="form-select" name="idCargo" required>
                                        <option value="">Seleccionar cargo</option>
                                        <% if (listaCargos != null) { 
                                            for (clsCargo cargo : listaCargos) { %>
                                                <option value="<%= cargo.getIdcargo() %>" <%= cargo.getIdcargo() == empleado.getIdcargo() ? "selected" : "" %>>
                                                    <%= cargo.getNombrecargo() %>
                                                </option>
                                            <% }
                                        } %>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <!-- Credenciales de Acceso -->
                        <div class="form-section">
                            <h6>
                                <i class="fas fa-key me-2"></i>Credenciales de Acceso
                            </h6>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Usuario <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="usuario" required 
                                           value="<%= empleado.getUsuario() %>" onkeypress="return event.key !== ' '" maxlength="50">
                                    <div class="form-text">Sin espacios</div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Contraseña</label>
                                    <input type="password" class="form-control" name="password" 
                                           placeholder="Dejar vacío para mantener la actual" 
                                           onkeypress="return event.key !== ' '" minlength="6">
                                    <div class="form-text">Dejar vacío para no cambiar la contraseña</div>
                                </div>
                            </div>
                        </div>

                        <!-- Botones -->
                        <div class="d-grid gap-2 d-md-flex justify-content-md-end border-top pt-4">
                            <a href="ControladorEmpleado?accion=listar" class="btn btn-secondary me-md-2">
                                <i class="fas fa-times me-2"></i>Cancelar
                            </a>
                            <button type="submit" class="btn btn-primary" id="btnGuardar">
                                <i class="fas fa-save me-2"></i>Actualizar Empleado
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
            const form = document.getElementById('empleadoForm');
            const btnGuardar = document.getElementById('btnGuardar');
            
            // Validación de DNI
            const dniInput = document.querySelector('input[name="dni"]');
            dniInput.addEventListener('input', function() {
                this.value = this.value.replace(/[^0-9]/g, '');
                if (this.value.length > 8) {
                    this.value = this.value.slice(0, 8);
                }
            });
            
            // Prevenir espacios en usuario y password
            const userInputs = document.querySelectorAll('input[name="usuario"], input[name="password"]');
            userInputs.forEach(input => {
                input.addEventListener('input', function() {
                    this.value = this.value.replace(/\s/g, '');
                });
            });
            
            // Loading state en el botón
            form.addEventListener('submit', function() {
                btnGuardar.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Actualizando...';
                btnGuardar.disabled = true;
            });
        });
    </script>
</body>
</html>