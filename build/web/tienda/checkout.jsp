<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.math.BigDecimal" %>
<%
    BigDecimal total = (BigDecimal) request.getAttribute("total");
    if (total == null) {
        response.sendRedirect("ControladorTienda?accion=listarProductos");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Finalizar Compra - LuxuryShop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary: #6366f1;
            --primary-dark: #4f46e5;
            --success: #10b981;
            --warning: #f59e0b;
            --danger: #ef4444;
            --text-dark: #1e293b;
            --text-light: #64748b;
            --border: #e2e8f0;
            --bg-light: #f8fafc;
        }
        
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
            min-height: 100vh;
        }
        
        .navbar-brand {
            font-weight: 700;
            font-size: 1.5rem;
            background: linear-gradient(135deg, var(--primary), var(--primary-dark));
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        
        .checkout-header {
            background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
            color: white;
            padding: 3rem 0;
            margin-bottom: 2rem;
            border-radius: 0 0 2rem 2rem;
        }
        
        .checkout-card {
            background: white;
            border: none;
            border-radius: 1.5rem;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            margin-bottom: 2rem;
            overflow: hidden;
        }
        
        .step-indicator {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 3rem;
            position: relative;
        }
        
        .step-indicator::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 0;
            right: 0;
            height: 2px;
            background: var(--border);
            z-index: 1;
        }
        
        .step {
            display: flex;
            flex-direction: column;
            align-items: center;
            position: relative;
            z-index: 2;
        }
        
        .step-circle {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            background: white;
            border: 3px solid var(--border);
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
            margin-bottom: 0.5rem;
            transition: all 0.3s ease;
        }
        
        .step.active .step-circle {
            background: var(--primary);
            border-color: var(--primary);
            color: white;
        }
        
        .step.completed .step-circle {
            background: var(--success);
            border-color: var(--success);
            color: white;
        }
        
        .step-label {
            font-size: 0.875rem;
            font-weight: 500;
            color: var(--text-light);
        }
        
        .step.active .step-label {
            color: var(--primary);
            font-weight: 600;
        }
        
        .form-section {
            padding: 2rem;
            border-bottom: 1px solid var(--border);
        }
        
        .form-section:last-child {
            border-bottom: none;
        }
        
        .form-control {
            border: 2px solid var(--border);
            border-radius: 0.75rem;
            padding: 0.75rem 1rem;
            transition: all 0.3s ease;
        }
        
        .form-control:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
        }
        
        .payment-method {
            border: 2px solid var(--border);
            border-radius: 1rem;
            padding: 1.5rem;
            margin-bottom: 1rem;
            cursor: pointer;
            transition: all 0.3s ease;
            background: white;
        }
        
        .payment-method:hover {
            border-color: var(--primary);
            transform: translateY(-2px);
        }
        
        .payment-method.selected {
            border-color: var(--success);
            background: rgba(16, 185, 129, 0.05);
        }
        
        .payment-icon {
            font-size: 2rem;
            margin-bottom: 1rem;
        }
        
        .summary-card {
            background: white;
            border: none;
            border-radius: 1.5rem;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            position: sticky;
            top: 2rem;
        }
        
        .btn-pay {
            background: linear-gradient(135deg, var(--success), #059669);
            border: none;
            border-radius: 1rem;
            padding: 1rem 2rem;
            font-weight: 600;
            font-size: 1.1rem;
            transition: all 0.3s ease;
            width: 100%;
        }
        
        .btn-pay:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(16, 185, 129, 0.3);
        }
        
        .security-badge {
            background: linear-gradient(135deg, #fef3c7, #f59e0b);
            color: #92400e;
            padding: 0.5rem 1rem;
            border-radius: 2rem;
            font-size: 0.875rem;
            font-weight: 500;
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white sticky-top shadow-sm">
        <div class="container">
            <a class="navbar-brand" href="ControladorTienda?accion=listarProductos">
                <i class="fas fa-gem me-2"></i>LuxuryShop
            </a>
            <div class="navbar-nav ms-auto">
                <a href="ControladorTienda?accion=verCarrito" class="btn btn-outline-primary">
                    <i class="fas fa-arrow-left me-2"></i>Volver al Carrito
                </a>
            </div>
        </div>
    </nav>

    <!-- Checkout Header -->
    <section class="checkout-header">
        <div class="container text-center">
            <h1 class="display-5 fw-bold mb-3">
                <i class="fas fa-credit-card me-3"></i>Finalizar Compra
            </h1>
            <p class="lead opacity-90 mb-0">Completa tu información para procesar el pago</p>
        </div>
    </section>

    <div class="container">
        <!-- Step Indicator -->
        <div class="step-indicator">
            <div class="step completed">
                <div class="step-circle">
                    <i class="fas fa-shopping-cart"></i>
                </div>
                <span class="step-label">Carrito</span>
            </div>
            <div class="step active">
                <div class="step-circle">2</div>
                <span class="step-label">Información</span>
            </div>
            <div class="step">
                <div class="step-circle">3</div>
                <span class="step-label">Pago</span>
            </div>
            <div class="step">
                <div class="step-circle">
                    <i class="fas fa-check"></i>
                </div>
                <span class="step-label">Confirmación</span>
            </div>
        </div>

        <div class="row">
            <!-- Checkout Form -->
            <div class="col-lg-8">
                <div class="checkout-card">
                    <!-- Información de Envío -->
                    <div class="form-section">
                        <h5 class="fw-bold mb-4">
                            <i class="fas fa-truck me-2 text-primary"></i>Información de Envío
                        </h5>
                        <form id="shippingForm">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="nombre" class="form-label fw-medium">Nombres *</label>
                                    <input type="text" class="form-control" id="nombre" required placeholder="Ingresa tus nombres">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="apellido" class="form-label fw-medium">Apellidos *</label>
                                    <input type="text" class="form-control" id="apellido" required placeholder="Ingresa tus apellidos">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="email" class="form-label fw-medium">Email *</label>
                                    <input type="email" class="form-control" id="email" required placeholder="tu@email.com">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="telefono" class="form-label fw-medium">Teléfono *</label>
                                    <input type="tel" class="form-control" id="telefono" required placeholder="+51 XXX XXX XXX">
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="direccion" class="form-label fw-medium">Dirección Completa *</label>
                                <textarea class="form-control" id="direccion" rows="3" required placeholder="Ingresa tu dirección completa para el envío"></textarea>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="ciudad" class="form-label fw-medium">Ciudad *</label>
                                    <input type="text" class="form-control" id="ciudad" required placeholder="Ej: Lima">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="codigoPostal" class="form-label fw-medium">Código Postal</label>
                                    <input type="text" class="form-control" id="codigoPostal" placeholder="Ej: 15001">
                                </div>
                            </div>
                        </form>
                    </div>

                    <!-- Método de Pago -->
                    <div class="form-section">
                        <h5 class="fw-bold mb-4">
                            <i class="fas fa-credit-card me-2 text-primary"></i>Método de Pago
                        </h5>
                        
                        <div class="payment-method selected" onclick="selectPaymentMethod('paypal')">
                            <div class="row align-items-center">
                                <div class="col-auto">
                                    <div class="payment-icon text-primary">
                                        <i class="fab fa-paypal"></i>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="form-check mb-0">
                                        <input class="form-check-input" type="radio" name="paymentMethod" 
                                               id="paypal" value="paypal" checked>
                                        <label class="form-check-label fw-medium" for="paypal">
                                            PayPal
                                        </label>
                                        <p class="text-muted small mb-0 mt-1">
                                            Paga de forma segura con tu cuenta PayPal
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="payment-method" onclick="selectPaymentMethod('tarjeta')">
                            <div class="row align-items-center">
                                <div class="col-auto">
                                    <div class="payment-icon text-primary">
                                        <i class="fas fa-credit-card"></i>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="form-check mb-0">
                                        <input class="form-check-input" type="radio" name="paymentMethod" 
                                               id="tarjeta" value="tarjeta">
                                        <label class="form-check-label fw-medium" for="tarjeta">
                                            Tarjeta de Crédito/Débito
                                        </label>
                                        <p class="text-muted small mb-0 mt-1">
                                            Paga con Visa, MasterCard o American Express
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Términos y Condiciones -->
                    <div class="form-section">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="terminos" required>
                            <label class="form-check-label text-muted" for="terminos">
                                He leído y acepto los <a href="#" class="text-primary">términos y condiciones</a> 
                                y la <a href="#" class="text-primary">política de privacidad</a>
                            </label>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Order Summary -->
            <div class="col-lg-4">
                <div class="summary-card">
                    <div class="card-header bg-primary text-white py-3">
                        <h5 class="mb-0">
                            <i class="fas fa-receipt me-2"></i>Resumen del Pedido
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="text-muted">Subtotal:</span>
                            <span class="fw-bold">S/. <%= total %></span>
                        </div>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="text-muted">Envío:</span>
                            <span class="text-success fw-bold">Gratis</span>
                        </div>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="text-muted">Impuestos:</span>
                            <span class="fw-bold">S/. 0.00</span>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <strong class="fs-5">Total:</strong>
                            <strong class="text-primary fs-4">S/. <%= total %></strong>
                        </div>
                        
                        <form action="ControladorTienda" method="post">
                            <input type="hidden" name="accion" value="procesarPago">
                            <button type="submit" class="btn btn-pay mb-3" onclick="return validateForm()">
                                <i class="fab fa-paypal me-2"></i>Pagar con PayPal
                            </button>
                        </form>
                        
                        <div class="text-center mb-4">
                            <div class="security-badge d-inline-flex align-items-center">
                                <i class="fas fa-lock me-2"></i>
                                Pago 100% seguro
                            </div>
                        </div>
                        
                        <!-- Benefits -->
                        <div class="border-top pt-3">
                            <div class="d-flex align-items-center mb-2">
                                <i class="fas fa-shipping-fast text-success me-2"></i>
                                <small class="text-muted">Envío gratis</small>
                            </div>
                            <div class="d-flex align-items-center mb-2">
                                <i class="fas fa-undo text-primary me-2"></i>
                                <small class="text-muted">Devoluciones en 30 días</small>
                            </div>
                            <div class="d-flex align-items-center">
                                <i class="fas fa-shield-alt text-warning me-2"></i>
                                <small class="text-muted">Garantía incluida</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-light mt-5 py-5">
        <div class="container">
            <div class="row">
                <div class="col-lg-4 mb-4">
                    <h5 class="fw-bold mb-3">
                        <i class="fas fa-gem me-2"></i>LuxuryShop
                    </h5>
                    <p class="text-light opacity-75">Tu destino para productos de calidad premium con la mejor experiencia de compra online.</p>
                </div>
                <div class="col-lg-2 col-md-6 mb-4">
                    <h6 class="fw-bold mb-3">Comprar</h6>
                    <ul class="list-unstyled">
                        <li><a href="#" class="text-light opacity-75 text-decoration-none">Productos</a></li>
                        <li><a href="#" class="text-light opacity-75 text-decoration-none">Ofertas</a></li>
                        <li><a href="#" class="text-light opacity-75 text-decoration-none">Nuevos</a></li>
                    </ul>
                </div>
                <div class="col-lg-2 col-md-6 mb-4">
                    <h6 class="fw-bold mb-3">Soporte</h6>
                    <ul class="list-unstyled">
                        <li><a href="#" class="text-light opacity-75 text-decoration-none">Contacto</a></li>
                        <li><a href="#" class="text-light opacity-75 text-decoration-none">FAQ</a></li>
                        <li><a href="#" class="text-light opacity-75 text-decoration-none">Envíos</a></li>
                    </ul>
                </div>
                <div class="col-lg-4 mb-4">
                    <h6 class="fw-bold mb-3">Newsletter</h6>
                    <p class="text-light opacity-75 mb-2">Suscríbete para ofertas exclusivas</p>
                    <div class="input-group">
                        <input type="email" class="form-control" placeholder="tu@email.com">
                        <button class="btn btn-primary">Suscribir</button>
                    </div>
                </div>
            </div>
            <hr class="bg-light opacity-25">
            <div class="text-center pt-3">
                <p class="mb-0 text-light opacity-75">&copy; 2024 LuxuryShop. Todos los derechos reservados.</p>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function selectPaymentMethod(method) {
            // Remover selección de todos los métodos
            document.querySelectorAll('.payment-method').forEach(el => {
                el.classList.remove('selected');
            });
            
            // Seleccionar el método clickeado
            event.currentTarget.classList.add('selected');
            document.getElementById(method).checked = true;
        }
        
        function validateForm() {
            const terminos = document.getElementById('terminos');
            if (!terminos.checked) {
                alert('Debes aceptar los términos y condiciones para continuar.');
                terminos.focus();
                return false;
            }
            
            // Validar campos requeridos
            const requiredFields = ['nombre', 'apellido', 'email', 'direccion', 'telefono', 'ciudad'];
            for (const fieldId of requiredFields) {
                const field = document.getElementById(fieldId);
                if (!field.value.trim()) {
                    alert('Por favor, completa todos los campos requeridos marcados con *.');
                    field.focus();
                    return false;
                }
            }
            
            // Validar email
            const email = document.getElementById('email');
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email.value)) {
                alert('Por favor, ingresa un email válido.');
                email.focus();
                return false;
            }
            
            return confirm('¿Estás seguro de que deseas proceder con el pago de S/. <%= total %>?');
        }

        // Auto-fill demo data (solo para desarrollo)
        document.addEventListener('DOMContentLoaded', function() {
            // Descomenta la siguiente línea si quieres datos de prueba
            // fillDemoData();
        });

        function fillDemoData() {
            document.getElementById('nombre').value = 'Juan';
            document.getElementById('apellido').value = 'Pérez';
            document.getElementById('email').value = 'juan@ejemplo.com';
            document.getElementById('telefono').value = '+51 999 888 777';
            document.getElementById('direccion').value = 'Av. Ejemplo 123, Miraflores';
            document.getElementById('ciudad').value = 'Lima';
            document.getElementById('codigoPostal').value = '15001';
        }
    </script>
</body>
</html>