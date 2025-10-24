<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String mensaje = (String) session.getAttribute("mensaje");
    String tipoMensaje = (String) session.getAttribute("tipoMensaje");
    
    // Limpiar mensajes después de mostrarlos
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
    <title>Login - Sistema Tienda GECA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            height: 100vh;
            display: flex;
            align-items: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .login-container {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.3);
            overflow: hidden;
            backdrop-filter: blur(10px);
        }
        .login-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2.5rem 2rem;
            text-align: center;
            position: relative;
            overflow: hidden;
        }
        .login-header::before {
            content: '';
            position: absolute;
            top: -50%;
            left: -50%;
            width: 200%;
            height: 200%;
            background: radial-gradient(circle, rgba(255,255,255,0.1) 1px, transparent 1px);
            background-size: 20px 20px;
            animation: float 20s linear infinite;
        }
        @keyframes float {
            0% { transform: translate(0, 0) rotate(0deg); }
            100% { transform: translate(-50px, -50px) rotate(360deg); }
        }
        .login-body {
            padding: 2.5rem;
        }
        .form-control {
            border: 2px solid #e9ecef;
            border-radius: 10px;
            padding: 0.75rem 1rem;
            transition: all 0.3s ease;
        }
        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.25rem rgba(102, 126, 234, 0.25);
            transform: translateY(-2px);
        }
        .btn-login {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            color: white;
            padding: 0.75rem 2rem;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s ease;
            width: 100%;
        }
        .btn-login:hover {
            background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        .logo {
            font-size: 2.5rem;
            margin-bottom: 1rem;
            display: block;
        }
        .credentials-hint {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 1.5rem;
            margin-top: 1.5rem;
            border-left: 4px solid #667eea;
        }
        .user-role {
            font-size: 0.75rem;
            opacity: 0.8;
        }
        .login-tabs {
            border-bottom: 1px solid #dee2e6;
            margin-bottom: 1.5rem;
        }
        .login-tabs .nav-link {
            border: none;
            color: #6c757d;
            font-weight: 500;
            padding: 0.75rem 1.5rem;
        }
        .login-tabs .nav-link.active {
            color: #495057;
            border-bottom: 3px solid #667eea;
            background: transparent;
        }
        .tab-content {
            padding-top: 1rem;
        }
        .client-info {
            background: linear-gradient(135deg, #d1ecf1 0%, #bee5eb 100%);
            border: 1px solid #b8daff;
            border-radius: 10px;
            padding: 1rem;
            margin-top: 1rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-7">
                <div class="login-container">
                    <div class="login-header">
                        <i class="fas fa-store logo"></i>
                        <h2 class="mb-2">SISTEMA TIENDA</h2>
                        <p class="mb-0 opacity-75">Gesti&oacute;n Integral GECA</p>
                    </div>
                    <div class="login-body">
                        <!-- Mensajes -->
                        <% if (mensaje != null) { %>
                            <div class="alert alert-<%= tipoMensaje != null ? tipoMensaje : "info" %> alert-dismissible fade show" role="alert">
                                <i class="fas fa-<%= "success".equals(tipoMensaje) ? "check-circle" : "exclamation-triangle" %> me-2"></i>
                                <%= mensaje %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        <% } %>
                        
                        <!-- Pestañas de Login -->
                        <ul class="nav nav-tabs login-tabs" id="loginTabs" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link active" id="empleado-tab" data-bs-toggle="tab" 
                                        data-bs-target="#empleado" type="button" role="tab">
                                    <i class="fas fa-user-tie me-2"></i>Empleado
                                </button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="cliente-tab" data-bs-toggle="tab" 
                                        data-bs-target="#cliente" type="button" role="tab">
                                    <i class="fas fa-user me-2"></i>Cliente
                                </button>
                            </li>
                        </ul>

                        <div class="tab-content" id="loginTabsContent">
                            <!-- Pestaña Empleado -->
                            <div class="tab-pane fade show active" id="empleado" role="tabpanel">
                                <form action="ControladorEmpleado" method="POST" id="loginFormEmpleado">
                                    <input type="hidden" name="accion" value="login">
                                    
                                    <div class="mb-4">
                                        <label for="usuario" class="form-label fw-semibold">
                                            <i class="fas fa-user me-2 text-primary"></i>Usuario
                                        </label>
                                        <input type="text" class="form-control" id="usuario" name="usuario" 
                                               placeholder="Ingrese su usuario" required autofocus
                                               onkeypress="return event.key !== ' '">
                                    </div>
                                    
                                    <div class="mb-4">
                                        <label for="password" class="form-label fw-semibold">
                                            <i class="fas fa-lock me-2 text-primary"></i>Contrase&ntilde;a
                                        </label>
                                        <input type="password" class="form-control" id="password" name="password" 
                                               placeholder="Ingrese su contraseña" required
                                               onkeypress="return event.key !== ' '">
                                    </div>
                                    
                                    <button type="submit" class="btn btn-login mb-3" id="btnLoginEmpleado">
                                        <i class="fas fa-sign-in-alt me-2"></i>Ingresar como Empleado
                                    </button>
                                </form>
                            </div>

                            <!-- Pestaña Cliente -->
                            <div class="tab-pane fade" id="cliente" role="tabpanel">
                                <form action="ControladorEmpleado" method="POST" id="loginFormCliente">
                                    <input type="hidden" name="accion" value="login">
                                    
                                    <div class="mb-4">
                                        <label for="dniCliente" class="form-label fw-semibold">
                                            <i class="fas fa-id-card me-2 text-primary"></i>DNI
                                        </label>
                                        <input type="text" class="form-control" id="dniCliente" name="usuario" 
                                               placeholder="Ingrese su DNI (8 dígitos)" required
                                               pattern="[0-9]{8}" maxlength="8"
                                               title="Ingrese su DNI de 8 dígitos"
                                               onkeypress="return event.key !== ' '">
                                        <div class="form-text">Use su número de DNI como usuario</div>
                                    </div>
                                    
                                    <div class="mb-4">
                                        <label for="passwordCliente" class="form-label fw-semibold">
                                            <i class="fas fa-lock me-2 text-primary"></i>Contrase&ntilde;a
                                        </label>
                                       <input type="password" class="form-control" id="passwordCliente" name="password"
                                               placeholder="Ingrese su contraseña" required
                                               onkeypress="return event.key !== ' '">
                                         <div class="form-text">Use la contraseña registrada</div>
                                    </div>
                                    
                                    <button type="submit" class="btn btn-login mb-3" id="btnLoginCliente">
                                        <i class="fas fa-shopping-cart me-2"></i>Ingresar como Cliente
                                    </button>
                                </form>

                                <div class="client-info">
                                    <h6 class="fw-bold mb-2">
                                        <i class="fas fa-info-circle me-2 text-info"></i>Información para Clientes
                                    </h6>
                                    <p class="mb-2 small">Si es cliente nuevo, regístrese primero en la tienda.</p>
                                    <a href="ControladorTienda?accion=listarProductos" class="btn btn-outline-primary btn-sm w-100">
                                        <i class="fas fa-store me-1"></i>Ir a la Tienda
                                    </a>
                                </div>
                            </div>
                        </div>

                        <!-- Credenciales de prueba -->
                        <div class="credentials-hint">
                            <h6 class="fw-bold mb-3 text-center">
                                <i class="fas fa-users me-2 text-warning"></i>Credenciales del Sistema
                            </h6>
                            <div class="row small g-3">
                                <div class="col-md-6">
                                    <div class="border rounded p-2 bg-white">
                                        <strong class="text-primary">Administradores:</strong>
                                        <div class="mt-1">
                                            <div><strong>admin</strong> / admin123</div>
                                            <div><strong>gcohaila</strong> / 1234</div>
                                            <div class="user-role">Gabriela Cohaila</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="border rounded p-2 bg-white">
                                        <strong class="text-success">Vendedores:</strong>
                                        <div class="mt-1">
                                            <div><strong>vendedor</strong> / vendedor123</div>
                                            <div><strong>mgomez</strong> / 1234</div>
                                            <div class="user-role">María Gómez</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12 mt-2">
                                    <div class="border rounded p-2 bg-white text-center">
                                        <strong class="text-info">Clientes (usar DNI):</strong>
                                        <div class="mt-1">
                                            <div><strong>44332211</strong> / <span class="text-muted">cliente123</span></div>
                                            <div class="user-role">Ana María Rodríguez</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="text-center mt-3">
                                <small class="text-muted">
                                    <i class="fas fa-info-circle me-1"></i>
                                    Seleccione el tipo de usuario y use las credenciales correspondientes
                                </small>
                            </div>
                        </div>

                        <div class="text-center mt-4">
                            <small class="text-muted">
                                <i class="fas fa-copyright me-1"></i> 2024 Sistema Tienda GECA v1.0
                            </small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const loginContainer = document.querySelector('.login-container');
            const loginFormEmpleado = document.getElementById('loginFormEmpleado');
            const loginFormCliente = document.getElementById('loginFormCliente');
            const btnLoginEmpleado = document.getElementById('btnLoginEmpleado');
            const btnLoginCliente = document.getElementById('btnLoginCliente');
            const dniCliente = document.getElementById('dniCliente');
            
            // Efectos de entrada
            loginContainer.style.opacity = '0';
            loginContainer.style.transform = 'translateY(30px)';
            
            setTimeout(() => {
                loginContainer.style.transition = 'all 0.5s ease';
                loginContainer.style.opacity = '1';
                loginContainer.style.transform = 'translateY(0)';
            }, 100);

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

            // Prevenir espacios en los inputs
            const inputs = document.querySelectorAll('input[type="text"], input[type="password"]');
            inputs.forEach(input => {
                input.addEventListener('input', function() {
                    this.value = this.value.replace(/\s/g, '');
                });
            });

            // Validación de DNI para clientes (solo números, máximo 8 dígitos)
            dniCliente.addEventListener('input', function() {
                this.value = this.value.replace(/[^0-9]/g, '');
                if (this.value.length > 8) {
                    this.value = this.value.slice(0, 8);
                }
            });

            // Loading state en los botones
            loginFormEmpleado.addEventListener('submit', function() {
                btnLoginEmpleado.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Verificando...';
                btnLoginEmpleado.disabled = true;
            });

            loginFormCliente.addEventListener('submit', function() {
                btnLoginCliente.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Verificando...';
                btnLoginCliente.disabled = true;
            });

            // Cambiar foco automáticamente al cambiar de pestaña
            document.getElementById('cliente-tab').addEventListener('click', function() {
                setTimeout(() => {
                    dniCliente.focus();
                }, 100);
            });

            document.getElementById('empleado-tab').addEventListener('click', function() {
                setTimeout(() => {
                    document.getElementById('usuario').focus();
                }, 100);
            });
        });
    </script>
</body>
</html>