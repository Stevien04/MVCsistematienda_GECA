<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.clsTipoDocumento, java.util.List" %>
<%
    // Obtener tipos de documento del request
    List<clsTipoDocumento> tiposDocumento = (List<clsTipoDocumento>) request.getAttribute("tiposDocumento");
    
    // Mensajes
    String mensaje = (String) session.getAttribute("mensaje");
    String tipoMensaje = (String) session.getAttribute("tipoMensaje");
    
    if (mensaje != null) {
        session.removeAttribute("mensaje");
        session.removeAttribute("tipoMensaje");
    }
    
    // Si no hay tipos de documento, redirigir al controlador
    if (tiposDocumento == null) {
        response.sendRedirect("ControladorRegistroCliente?accion=mostrarFormulario");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Cliente - Sistema Tienda</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .register-container {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.3);
            backdrop-filter: blur(10px);
            margin: 2rem 0;
        }
        .register-header {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            color: white;
            padding: 2rem;
            text-align: center;
            border-radius: 20px 20px 0 0;
        }
        .register-body {
            padding: 2rem;
        }
        .btn-register {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            border: none;
            color: white;
            padding: 0.75rem 2rem;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s ease;
            width: 100%;
        }
        .btn-register:hover {
            background: linear-gradient(135deg, #218838 0%, #1e9e8a 100%);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(40, 167, 69, 0.4);
        }
        .form-control {
            border: 2px solid #e9ecef;
            border-radius: 10px;
            padding: 0.75rem 1rem;
            transition: all 0.3s ease;
        }
        .form-control:focus {
            border-color: #28a745;
            box-shadow: 0 0 0 0.25rem rgba(40, 167, 69, 0.25);
        }
        .required-field::after {
            content: " *";
            color: #dc3545;
        }
        .password-strength {
            height: 4px;
            border-radius: 2px;
            margin-top: 0.25rem;
            transition: all 0.3s ease;
        }
        .strength-weak { background-color: #dc3545; width: 25%; }
        .strength-medium { background-color: #ffc107; width: 50%; }
        .strength-strong { background-color: #28a745; width: 100%; }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="register-container">
                    <div class="register-header">
                        <i class="fas fa-user-plus fa-2x mb-3"></i>
                        <h3 class="mb-2">Registro de Cliente</h3>
                        <p class="mb-0">Complete sus datos para comenzar a comprar</p>
                    </div>
                    <div class="register-body">
                        <!-- Mensajes -->
                        <% if (mensaje != null) { %>
                            <div class="alert alert-<%= tipoMensaje != null ? tipoMensaje : "info" %> alert-dismissible fade show" role="alert">
                                <i class="fas fa-<%= "success".equals(tipoMensaje) ? "check-circle" : "exclamation-triangle" %> me-2"></i>
                                <%= mensaje %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        <% } %>

                        <form action="ControladorRegistroCliente" method="POST" id="registroForm">
                            <input type="hidden" name="accion" value="registrar">
                            
                            <!-- Información de Documento -->
                            <h5 class="mb-3 text-primary">
                                <i class="fas fa-id-card me-2"></i>Información de Documento
                            </h5>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="idtipodocumento" class="form-label fw-semibold required-field">
                                        <i class="fas fa-passport me-2"></i>Tipo de Documento
                                    </label>
                                    <select class="form-select" id="idtipodocumento" name="idtipodocumento" required>
                                        <option value="">Seleccione tipo de documento</option>
                                        <% for (clsTipoDocumento tipoDoc : tiposDocumento) { %>
                                        <option value="<%= tipoDoc.getIdtipodocumento() %>">
                                            <%= tipoDoc.getNombretipodocumento() %> (<%= tipoDoc.getAbreviatura() %>)
                                        </option>
                                        <% } %>
                                    </select>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="numero_documento" class="form-label fw-semibold required-field">
                                        <i class="fas fa-hashtag me-2"></i>Número de Documento
                                    </label>
                                    <input type="text" class="form-control" id="numero_documento" name="numero_documento" 
                                           placeholder="Ingrese su número de documento" required
                                           onkeypress="return event.key !== ' '">
                                </div>
                            </div>

                            <!-- Información Personal -->
                            <h5 class="mb-3 mt-4 text-primary">
                                <i class="fas fa-user me-2"></i>Información Personal
                            </h5>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="nombre" class="form-label fw-semibold required-field">
                                        <i class="fas fa-signature me-2"></i>Nombres
                                    </label>
                                    <input type="text" class="form-control" id="nombre" name="nombre" 
                                           placeholder="Ingrese sus nombres" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="apellido" class="form-label fw-semibold required-field">
                                        <i class="fas fa-signature me-2"></i>Apellidos
                                    </label>
                                    <input type="text" class="form-control" id="apellido" name="apellido" 
                                           placeholder="Ingrese sus apellidos" required>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="telefono" class="form-label fw-semibold">
                                        <i class="fas fa-phone me-2"></i>Teléfono
                                    </label>
                                    <input type="tel" class="form-control" id="telefono" name="telefono" 
                                           placeholder="987654321">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="fecha_nacimiento" class="form-label fw-semibold">
                                        <i class="fas fa-birthday-cake me-2"></i>Fecha de Nacimiento
                                    </label>
                                    <input type="date" class="form-control" id="fecha_nacimiento" name="fecha_nacimiento">
                                </div>
                            </div>

                            <!-- Información de Contacto -->
                            <h5 class="mb-3 mt-4 text-primary">
                                <i class="fas fa-address-book me-2"></i>Información de Contacto
                            </h5>
                            <div class="mb-3">
                                <label for="email" class="form-label fw-semibold">
                                    <i class="fas fa-envelope me-2"></i>Email
                                </label>
                                <input type="email" class="form-control" id="email" name="email" 
                                       placeholder="juan@ejemplo.com">
                            </div>

                            <div class="mb-4">
                                <label for="direccion" class="form-label fw-semibold">
                                    <i class="fas fa-map-marker-alt me-2"></i>Dirección
                                </label>
                                <textarea class="form-control" id="direccion" name="direccion" 
                                          rows="2" placeholder="Av. Ejemplo 123, Lima"></textarea>
                            </div>

                            <!-- Credenciales de Acceso -->
                            <h5 class="mb-3 mt-4 text-primary">
                                <i class="fas fa-key me-2"></i>Credenciales de Acceso
                            </h5>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="usuario" class="form-label fw-semibold required-field">
                                        <i class="fas fa-user-circle me-2"></i>Usuario
                                    </label>
                                    <input type="text" class="form-control" id="usuario" name="usuario" 
                                           placeholder="Elija un nombre de usuario" required
                                           onkeypress="return event.key !== ' '">
                                    <div class="form-text">Este será su usuario para ingresar al sistema</div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="fecha_nacimiento" class="form-label fw-semibold">
                                        <i class="fas fa-birthday-cake me-2"></i>Fecha de Nacimiento
                                    </label>
                                    <input type="date" class="form-control" id="fecha_nacimiento" name="fecha_nacimiento"
                                           max="<%= java.time.LocalDate.now().minusYears(18) %>">
                                    <div class="form-text">Debe ser mayor de 18 años</div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="password" class="form-label fw-semibold required-field">
                                        <i class="fas fa-lock me-2"></i>Contraseña
                                    </label>
                                    <input type="password" class="form-control" id="password" name="password" 
                                           placeholder="Ingrese su contraseña" required
                                           onkeyup="checkPasswordStrength(this.value)">
                                    <div class="password-strength" id="passwordStrength"></div>
                                    <div class="form-text">Mínimo 6 caracteres</div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="confirmar_password" class="form-label fw-semibold required-field">
                                        <i class="fas fa-lock me-2"></i>Confirmar Contraseña
                                    </label>
                                    <input type="password" class="form-control" id="confirmar_password" name="confirmar_password" 
                                           placeholder="Confirme su contraseña" required
                                           onkeyup="checkPasswordMatch()">
                                    <div class="form-text" id="passwordMatchText"></div>
                                </div>
                            </div>

                            <!-- Términos y Condiciones -->
                            <div class="form-check mb-4">
                                <input class="form-check-input" type="checkbox" id="terminos" required>
                                <label class="form-check-label" for="terminos">
                                    Acepto los <a href="#" class="text-primary">términos y condiciones</a> 
                                    y la <a href="#" class="text-primary">política de privacidad</a>
                                </label>
                            </div>

                            <!-- Botones -->
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-register" id="btnRegistrar">
                                    <i class="fas fa-user-plus me-2"></i>Crear Cuenta
                                </button>
                                <div class="text-center">
                                    <a href="login.jsp" class="btn btn-outline-primary me-2">
                                        <i class="fas fa-sign-in-alt me-2"></i>Ya tengo cuenta
                                    </a>
                                    <a href="ControladorTienda?accion=listarProductos" class="btn btn-outline-secondary">
                                        <i class="fas fa-store me-2"></i>Continuar como invitado
                                    </a>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const registroForm = document.getElementById('registroForm');
            const btnRegistrar = document.getElementById('btnRegistrar');
            
            // Validación de número de documento (solo números)
            document.getElementById('numero_documento').addEventListener('input', function(e) {
                this.value = this.value.replace(/[^0-9]/g, '');
            });
            
            // Validación de teléfono (solo números)
            document.getElementById('telefono').addEventListener('input', function(e) {
                this.value = this.value.replace(/[^0-9]/g, '');
            });
            
            // Validación de usuario (sin espacios)
            document.getElementById('usuario').addEventListener('input', function(e) {
                this.value = this.value.replace(/\s/g, '');
            });
            
            // Prevenir espacios en campos de texto
            const textInputs = document.querySelectorAll('input[type="text"]');
            textInputs.forEach(input => {
                input.addEventListener('input', function() {
                    this.value = this.value.replace(/\s/g, '');
                });
            });
            
            // Loading state en el botón
            registroForm.addEventListener('submit', function() {
                btnRegistrar.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Registrando...';
                btnRegistrar.disabled = true;
            });
            
            // Auto-close alerts after 5 seconds
            setTimeout(function() {
                const alerts = document.querySelectorAll('.alert');
                alerts.forEach(alert => {
                    if (alert.classList.contains('show')) {
                        const bsAlert = new bootstrap.Alert(alert);
                        bsAlert.close();
                    }
                });
            }, 5000);
        });
        
        // Función para verificar fortaleza de contraseña
        function checkPasswordStrength(password) {
            const strengthBar = document.getElementById('passwordStrength');
            let strength = 0;
            
            if (password.length >= 6) strength++;
            if (password.length >= 8) strength++;
            if (/[A-Z]/.test(password)) strength++;
            if (/[0-9]/.test(password)) strength++;
            if (/[^A-Za-z0-9]/.test(password)) strength++;
            
            strengthBar.className = 'password-strength';
            if (password.length === 0) {
                strengthBar.style.width = '0%';
            } else if (strength <= 2) {
                strengthBar.className += ' strength-weak';
            } else if (strength <= 4) {
                strengthBar.className += ' strength-medium';
            } else {
                strengthBar.className += ' strength-strong';
            }
        }
        
        // Función para verificar coincidencia de contraseñas
        function checkPasswordMatch() {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmar_password').value;
            const matchText = document.getElementById('passwordMatchText');
            
            if (confirmPassword.length === 0) {
                matchText.textContent = '';
                matchText.className = 'form-text';
            } else if (password === confirmPassword) {
                matchText.textContent = '✓ Las contraseñas coinciden';
                matchText.className = 'form-text text-success';
            } else {
                matchText.textContent = '✗ Las contraseñas no coinciden';
                matchText.className = 'form-text text-danger';
            }
        }
        
        // Validación de formulario antes de enviar
        document.getElementById('registroForm').addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmar_password').value;
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Las contraseñas no coinciden. Por favor, verifique.');
                document.getElementById('confirmar_password').focus();
                return false;
            }
            
            if (password.length < 6) {
                e.preventDefault();
                alert('La contraseña debe tener al menos 6 caracteres.');
                document.getElementById('password').focus();
                return false;
            }
            
            return true;
        });
    </script>
</body>
</html>