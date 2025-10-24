<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Object empleadoObj = session.getAttribute("empleado");
    if (empleadoObj == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar Cliente - Sistema Tienda</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

    <jsp:include page="includes/styles.jsp" />

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
            border-bottom: 2px solid #f5576c;
            padding-bottom: 0.5rem;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <jsp:include page="includes/sidebar.jsp" />

    <div class="main-content" id="mainContent">
        <jsp:include page="includes/header.jsp" />

        <div class="container-fluid p-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="h4 mb-1">
                        <i class="fas fa-user-plus me-2 text-primary"></i>Registrar Cliente
                    </h2>
                    <p class="text-muted mb-0">Complete el formulario para guardar un nuevo cliente</p>
                </div>
                <a href="ControladorCliente?accion=listar" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Volver
                </a>
            </div>

            <div class="card card-form">
                <div class="card-body">
                    <form action="ControladorCliente" method="POST" id="registroClienteForm">
                        <input type="hidden" name="accion" value="agregar">

                        <div class="form-section">
                            <h6>
                                <i class="fas fa-user me-2"></i>Datos del Cliente
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

                        <div class="d-grid gap-2 d-md-flex justify-content-md-end border-top pt-4">
                            <a href="ControladorCliente?accion=listar" class="btn btn-secondary me-md-2">
                                <i class="fas fa-times me-2"></i>Cancelar
                            </a>
                            <button type="submit" class="btn btn-primary" id="btnGuardarCliente">
                                <i class="fas fa-save me-2"></i>Guardar Cliente
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
            const form = document.getElementById('registroClienteForm');
            const btnGuardar = document.getElementById('btnGuardarCliente');
            const dniInput = document.querySelector('input[name="dni"]');

            dniInput.addEventListener('input', function() {
                this.value = this.value.replace(/[^0-9]/g, '');
                if (this.value.length > 8) {
                    this.value = this.value.slice(0, 8);
                }
            });

            form.addEventListener('submit', function() {
                btnGuardar.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Guardando...';
                btnGuardar.disabled = true;
            });
        });
    </script>
</body>
</html>