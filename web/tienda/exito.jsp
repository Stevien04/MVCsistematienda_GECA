<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, java.util.Map, Modelo.clsProducto, java.math.BigDecimal" %>
<%
    String idTransaccion = (String) request.getAttribute("idTransaccion");
    BigDecimal totalPedido = (BigDecimal) request.getAttribute("totalPedido");
    List<Map<String, Object>> itemsComprobante = (List<Map<String, Object>>) request.getAttribute("itemsComprobante");
    
    if (idTransaccion == null) {
        response.sendRedirect("ControladorTienda?accion=listarProductos");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>¡Compra Exitosa! - GabrielaSHOP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary: #6366f1;
            --primary-dark: #4f46e5;
            --success: #10b981;
            --success-light: #d1fae5;
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
        
        .success-hero {
            background: linear-gradient(135deg, var(--success) 0%, #059669 100%);
            color: white;
            padding: 4rem 0;
            margin-bottom: 3rem;
            border-radius: 0 0 2rem 2rem;
            position: relative;
            overflow: hidden;
        }
        
        .success-hero::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1000 100" fill="rgba(255,255,255,0.1)"><path d="M500 50Q600 0 700 50T900 50T1000 0V100H0V0Q100 50 300 50T500 50Z"/></svg>');
            background-size: cover;
            background-position: bottom;
        }
        
        .success-icon {
            font-size: 5rem;
            margin-bottom: 1.5rem;
            animation: bounce 1s ease-in-out;
        }
        
        @keyframes bounce {
            0%, 20%, 50%, 80%, 100% {transform: translateY(0);}
            40% {transform: translateY(-20px);}
            60% {transform: translateY(-10px);}
        }
        
        .confetti {
            position: absolute;
            width: 10px;
            height: 10px;
            background: #fff;
            border-radius: 50%;
            animation: confetti-fall 5s linear infinite;
        }
        
        @keyframes confetti-fall {
            0% {transform: translateY(-100px) rotate(0deg); opacity: 1;}
            100% {transform: translateY(500px) rotate(360deg); opacity: 0;}
        }
        
        .comprobante-card {
            background: white;
            border: none;
            border-radius: 1.5rem;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            margin-bottom: 2rem;
            overflow: hidden;
            border: 2px solid var(--success);
        }
        
        .comprobante-header {
            background: linear-gradient(135deg, var(--success), #059669);
            color: white;
            padding: 2rem;
            text-align: center;
        }
        
        .comprobante-body {
            padding: 2rem;
        }
        
        .info-badge {
            background: var(--success-light);
            color: #065f46;
            padding: 0.5rem 1rem;
            border-radius: 2rem;
            font-size: 0.875rem;
            font-weight: 500;
            border: 1px solid var(--success);
        }
        
        .order-timeline {
            position: relative;
            padding-left: 2rem;
            margin: 2rem 0;
        }
        
        .order-timeline::before {
            content: '';
            position: absolute;
            left: 0;
            top: 0;
            bottom: 0;
            width: 2px;
            background: var(--success);
        }
        
        .timeline-item {
            position: relative;
            margin-bottom: 2rem;
        }
        
        .timeline-item::before {
            content: '';
            position: absolute;
            left: -2rem;
            top: 0.25rem;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            background: var(--success);
            border: 3px solid white;
            box-shadow: 0 0 0 2px var(--success);
        }
        
        .timeline-item.completed::before {
            background: var(--success);
        }
        
        .timeline-item.current::before {
            background: white;
            animation: pulse 2s infinite;
        }
        
        @keyframes pulse {
            0% {box-shadow: 0 0 0 0 rgba(16, 185, 129, 0.7);}
            70% {box-shadow: 0 0 0 10px rgba(16, 185, 129, 0);}
            100% {box-shadow: 0 0 0 0 rgba(16, 185, 129, 0);}
        }
        
        .product-image-sm {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 0.75rem;
        }
        
        .action-buttons {
            display: flex;
            gap: 1rem;
            justify-content: center;
            flex-wrap: wrap;
        }
        
        .btn-success-outline {
            border: 2px solid var(--success);
            color: var(--success);
            background: white;
            border-radius: 1rem;
            padding: 0.75rem 1.5rem;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        
        .btn-success-outline:hover {
            background: var(--success);
            color: white;
            transform: translateY(-2px);
        }
        
        .print-only {
            display: none;
        }
        
        @media print {
            .no-print {
                display: none !important;
            }
            .print-only {
                display: block !important;
            }
            .comprobante-card {
                border: 2px solid #000;
                box-shadow: none;
            }
            .success-hero {
                background: #000 !important;
                color: #000 !important;
            }
        }
        
        .share-buttons {
            display: flex;
            gap: 0.5rem;
            justify-content: center;
            margin-top: 1rem;
        }
        
        .share-btn {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            text-decoration: none;
            transition: all 0.3s ease;
        }
        
        .share-btn:hover {
            transform: scale(1.1);
        }
        
        .share-facebook { background: #3b5998; }
        .share-twitter { background: #1da1f2; }
        .share-whatsapp { background: #25d366; }
    </style>
</head>
<body>
    <!-- Success Hero Section -->
    <section class="success-hero">
        <div class="container text-center position-relative">
            <div class="success-icon">
                <i class="fas fa-check-circle"></i>
            </div>
            <h1 class="display-4 fw-bold mb-3">¡Pago Exitoso!</h1>
            <p class="lead mb-4 opacity-90">Tu compra ha sido procesada correctamente</p>
            <div class="info-badge d-inline-flex align-items-center">
                <i class="fas fa-receipt me-2"></i>
                ID de Transacción: <strong class="ms-2"><%= idTransaccion %></strong>
            </div>
        </div>
        
        <!-- Confetti Animation -->
        <div id="confetti-container"></div>
    </section>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <!-- Comprobante de Pago -->
                <div class="comprobante-card">
                    <div class="comprobante-header">
                        <div class="row align-items-center">
                            <div class="col-md-8 text-md-start text-center">
                                <h3 class="mb-2">
                                    <i class="fas fa-receipt me-2"></i>Comprobante de Pago
                                </h3>
                                <p class="mb-0 opacity-90">Gracias por tu compra en GabrielaSHOP</p>
                            </div>
                            <div class="col-md-4 text-md-end text-center mt-md-0 mt-3">
                                <span class="badge bg-light text-success fs-6 p-3">
                                    <i class="fas fa-check-circle me-1"></i>Pagado
                                </span>
                            </div>
                        </div>
                    </div>
                    
                    <div class="comprobante-body">
                        <!-- Información de la Transacción -->
                        <div class="row mb-4">
                            <div class="col-md-6 mb-3">
                                <h6 class="fw-bold text-dark mb-3">
                                    <i class="fas fa-info-circle me-2 text-primary"></i>Información de la Transacción
                                </h6>
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">ID Transacción:</span>
                                    <span class="fw-bold"><%= idTransaccion %></span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Fecha:</span>
                                    <span class="fw-bold"><%= new java.util.Date() %></span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Método de Pago:</span>
                                    <span class="fw-bold">PayPal</span>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <span class="text-muted">Estado:</span>
                                    <span class="badge bg-success">Completado</span>
                                </div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <h6 class="fw-bold text-dark mb-3">
                                    <i class="fas fa-user me-2 text-primary"></i>Información del Cliente
                                </h6>
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Email:</span>
                                    <span class="fw-bold">cliente@ejemplo.com</span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Teléfono:</span>
                                    <span class="fw-bold">+51 XXX XXX XXX</span>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <span class="text-muted">Envío a:</span>
                                    <span class="fw-bold text-end">Dirección proporcionada</span>
                                </div>
                            </div>
                        </div>

                        <!-- Detalles del Pedido -->
                        <h6 class="fw-bold text-dark mb-3">
                            <i class="fas fa-shopping-bag me-2 text-primary"></i>Detalles del Pedido
                        </h6>
                        <div class="table-responsive">
                            <table class="table table-borderless">
                                <thead class="table-light">
                                    <tr>
                                        <th>Producto</th>
                                        <th class="text-center">Cantidad</th>
                                        <th class="text-end">Precio Unit.</th>
                                        <th class="text-end">Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        if (itemsComprobante != null) {
                                            for (Map<String, Object> item : itemsComprobante) {
                                                clsProducto producto = (clsProducto) item.get("producto");
                                                int cantidad = (Integer) item.get("cantidad");
                                                BigDecimal subtotal = (BigDecimal) item.get("subtotal");
                                    %>
                                    <tr>
                                        <td>
                                            <div class="d-flex align-items-center">
                                                <img src="https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=120&h=120&fit=crop" 
                                                     class="product-image-sm me-3" alt="<%= producto.getNombreproducto() %>">
                                                <div>
                                                    <strong class="text-dark"><%= producto.getNombreproducto() %></strong>
                                                    <% if (producto.getNombremarca() != null) { %>
                                                    <br><small class="text-muted">Marca: <%= producto.getNombremarca() %></small>
                                                    <% } %>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="text-center align-middle">
                                            <span class="badge bg-primary fs-6"><%= cantidad %></span>
                                        </td>
                                        <td class="text-end align-middle">
                                            <strong>S/. <%= producto.getPrecio() %></strong>
                                        </td>
                                        <td class="text-end align-middle">
                                            <strong class="text-success">S/. <%= subtotal %></strong>
                                        </td>
                                    </tr>
                                    <%
                                            }
                                        }
                                    %>
                                </tbody>
                                <tfoot class="table-light">
                                    <tr>
                                        <td colspan="3" class="text-end"><strong>Total:</strong></td>
                                        <td class="text-end"><strong class="text-primary fs-5">S/. <%= totalPedido %></strong></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>

                        <!-- Order Timeline -->
                        <div class="order-timeline">
                            <h6 class="fw-bold text-dark mb-3">
                                <i class="fas fa-shipping-fast me-2 text-primary"></i>Seguimiento del Pedido
                            </h6>
                            <div class="timeline-item completed">
                                <h6 class="fw-bold text-success mb-1">Pedido Confirmado</h6>
                                <p class="text-muted mb-0">Tu pago ha sido procesado exitosamente</p>
                                <small class="text-muted">Hace unos momentos</small>
                            </div>
                            <div class="timeline-item current">
                                <h6 class="fw-bold text-primary mb-1">Preparando Envío</h6>
                                <p class="text-muted mb-0">Estamos preparando tu pedido para el envío</p>
                                <small class="text-muted">Próximamente</small>
                            </div>
                            <div class="timeline-item">
                                <h6 class="fw-bold text-muted mb-1">En Camino</h6>
                                <p class="text-muted mb-0">Tu pedido será enviado pronto</p>
                            </div>
                            <div class="timeline-item">
                                <h6 class="fw-bold text-muted mb-1">Entregado</h6>
                                <p class="text-muted mb-0">Espera la entrega en tu domicilio</p>
                            </div>
                        </div>

                        <!-- Información Adicional -->
                        <div class="alert alert-info border-0 mt-4">
                            <div class="d-flex align-items-start">
                                <i class="fas fa-info-circle text-primary mt-1 me-3 fs-5"></i>
                                <div>
                                    <h6 class="fw-bold mb-2">Información Importante</h6>
                                    <ul class="mb-0 ps-3">
                                        <li>Guarda este comprobante para cualquier consulta</li>
                                        <li>El tiempo de envío estimado es de 3-5 días hábiles</li>
                                        <li>Recibirás un email con el número de seguimiento</li>
                                        <li>Para consultas contacta a: <strong>soporte@luxuryshop.com</strong></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="text-center no-print mb-5">
                    <div class="action-buttons">
                        <button onclick="window.print()" class="btn btn-success-outline">
                            <i class="fas fa-print me-2"></i>Imprimir Comprobante
                        </button>
                        <a href="ControladorTienda?accion=listarProductos" class="btn btn-primary px-4 py-2">
                            <i class="fas fa-shopping-bag me-2"></i>Seguir Comprando
                        </a>
                        <a href="#" class="btn btn-success-outline">
                            <i class="fas fa-download me-2"></i>Descargar PDF
                        </a>
                    </div>
                    
                    <!-- Share Buttons -->
                    <div class="share-buttons">
                        <p class="text-muted mb-2 small">Compartir compra:</p>
                        <a href="#" class="share-btn share-facebook">
                            <i class="fab fa-facebook-f"></i>
                        </a>
                        <a href="#" class="share-btn share-twitter">
                            <i class="fab fa-twitter"></i>
                        </a>
                        <a href="#" class="share-btn share-whatsapp">
                            <i class="fab fa-whatsapp"></i>
                        </a>
                    </div>
                </div>

                <!-- Mensaje para Impresión -->
                <div class="print-only text-center mt-4">
                    <p class="mb-1">Gracias por tu compra - GabrielaSHOP</p>
                    <p class="mb-0 text-muted">www.GabrielaSHOP.com - contacto@GabrielaSHOP.com</p>
                    <p class="text-muted small">ID Transacción: <%= idTransaccion %></p>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-light mt-5 py-5 no-print">
        <div class="container">
            <div class="row">
                <div class="col-lg-4 mb-4">
                    <h5 class="fw-bold mb-3">
                        <i class="fas fa-gem me-2"></i>GabrielaSHOP
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
        // Confetti Animation
        function createConfetti() {
            const container = document.getElementById('confetti-container');
            for (let i = 0; i < 50; i++) {
                const confetti = document.createElement('div');
                confetti.className = 'confetti';
                confetti.style.left = Math.random() * 100 + 'vw';
                confetti.style.animationDelay = Math.random() * 5 + 's';
                confetti.style.background = getRandomColor();
                container.appendChild(confetti);
            }
        }
        
        function getRandomColor() {
            const colors = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#feca57', '#ff9ff3', '#54a0ff'];
            return colors[Math.floor(Math.random() * colors.length)];
        }
        
        // Auto-create confetti on load
        document.addEventListener('DOMContentLoaded', function() {
            createConfetti();
            
            // Auto-print option (descomenta si quieres imprimir automáticamente)
            // setTimeout(() => window.print(), 2000);
        });
        
        // Download PDF simulation
        document.querySelector('.btn-success-outline:nth-child(3)').addEventListener('click', function(e) {
            e.preventDefault();
            alert('La función de descarga PDF estará disponible próximamente');
        });
        
        // Share buttons
        document.querySelectorAll('.share-btn').forEach(btn => {
            btn.addEventListener('click', function(e) {
                e.preventDefault();
                const platform = this.classList[1].replace('share-', '');
                alert(`Compartiendo en ${platform} (función simulada)`);
            });
        });
    </script>
</body>
</html>