<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.clsCargo"%>
<%
    // Verificar sesión
    Object empleadoObj = session.getAttribute("empleado");
    if (empleadoObj == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // Obtener lista de cargos del request
    List<clsCargo> listaCargos = (List<clsCargo>) request.getAttribute("cargos");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuevo Empleado - Sistema Tienda</title>
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
                        <i class="fas fa-user-plus me-2 text-primary"></i>Nuevo Empleado
                    </h2>
                    <p class="text-muted mb-0">Registrar nuevo empleado en el sistema</p>
                </div>
                <a href="ControladorEmpleado?accion=listar" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Volver a la lista
                </a>
            </div
            
            <!-- === PEGA ESTE BLOQUE DEBUG AQUÍ === -->
            <div class="alert alert-info alert-dismissible fade show" role="alert">
                <strong>🔧 DEBUG FORMULARIO NUEVO:</strong> 
                <br>
                • Cargos disponibles: <strong><%= listaCargos != null ? listaCargos.size() : "NULL"%></strong>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <!-- === FIN DEBUG === -->

            <!-- Form -->
            <div class="card card-form">
                <div class="card-body">
                    <form action="ControladorEmpleado" method="POST" id="empleadoForm">
                        <input type="hidden" name="accion" value="agregar">
                        
                        <!-- Información Personal -->
                        <div class="form-section">
                            <h6>
                                <i class="fas fa-user me-2"></i>Información Personal
                            </h6>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Nombre <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="nombre" required 
                                           placeholder="Ingrese el nombre" maxlength="50">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Apellido <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="apellido" required 
                                           placeholder="Ingrese el apellido" maxlength="50">
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">DNI <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="dni" maxlength="8" required 
                                           placeholder="8 dígitos" pattern="[0-9]{8}">
                                    <div class="form-text">Solo números, 8 dígitos</div>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Teléfono</label>
                                    <input type="tel" class="form-control" name="telefono" 
                                           placeholder="Ej: 987654321" maxlength="15">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Email</label>
                                    <input type="email" class="form-control" name="email" 
                                           placeholder="ejemplo@correo.com" maxlength="100">
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-semibold">Dirección</label>
                                <textarea class="form-control" name="direccion" rows="2" 
                                          placeholder="Dirección completa" maxlength="200"></textarea>
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
                                           value="<%= java.time.LocalDate.now() %>">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Salario (S/) <span class="text-danger">*</span></label>
                                    <input type="number" step="0.01" class="form-control" name="salario" required 
                                           placeholder="0.00" min="0" value="1500.00">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Cargo <span class="text-danger">*</span></label>
                                    <select class="form-select" name="idCargo" required>
                                        <option value="">Seleccionar cargo</option>
                                        <% if (listaCargos != null) { 
                                            for (clsCargo cargo : listaCargos) { %>
                                                <option value="<%= cargo.getIdcargo() %>"><%= cargo.getNombrecargo() %></option>
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
                                           placeholder="Nombre de usuario" onkeypress="return event.key !== ' '" maxlength="50">
                                    <div class="form-text">Sin espacios</div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Contraseña <span class="text-danger">*</span></label>
                                    <input type="password" class="form-control" name="password" required 
                                           placeholder="Contraseña segura" onkeypress="return event.key !== ' '" minlength="6">
                                    <div class="form-text">Mínimo 6 caracteres</div>
                                </div>
                            </div>
                        </div>

                        <!-- Botones -->
                        <div class="d-grid gap-2 d-md-flex justify-content-md-end border-top pt-4">
                            <a href="ControladorEmpleado?accion=listar" class="btn btn-secondary me-md-2">
                                <i class="fas fa-times me-2"></i>Cancelar
                            </a>
                            <button type="submit" class="btn btn-primary" id="btnGuardar">
                                <i class="fas fa-save me-2"></i>Guardar Empleado
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
                btnGuardar.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Guardando...';
                btnGuardar.disabled = true;
            });
        });
    </script>
</body>
</html>