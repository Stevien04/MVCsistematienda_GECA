<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, java.util.Map, Modelo.clsProducto, java.math.BigDecimal" %>
<%
    List<Map<String, Object>> itemsCarrito = (List<Map<String, Object>>) request.getAttribute("itemsCarrito");
    BigDecimal total = (BigDecimal) request.getAttribute("total");
    boolean carritoVacio = itemsCarrito == null || itemsCarrito.isEmpty();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Carrito - GabrielaSHOP</title>
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
        
        .cart-header {
            background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
            color: white;
            padding: 3rem 0;
            margin-bottom: 2rem;
            border-radius: 0 0 2rem 2rem;
        }
        
        .cart-card {
            background: white;
            border: none;
            border-radius: 1.5rem;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            margin-bottom: 2rem;
            overflow: hidden;
        }
        
        .cart-item {
            padding: 1.5rem;
            border-bottom: 1px solid var(--border);
            transition: background-color 0.3s ease;
        }
        
        .cart-item:hover {
            background-color: var(--bg-light);
        }
        
        .cart-item:last-child {
            border-bottom: none;
        }
        
        .product-image {
            width: 100px;
            height: 100px;
            object-fit: cover;
            border-radius: 1rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        
        .quantity-control {
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        
        .quantity-btn {
            width: 40px;
            height: 40px;
            border: 2px solid var(--border);
            background: white;
            border-radius: 0.75rem;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .quantity-btn:hover {
            border-color: var(--primary);
            color: var(--primary);
        }
        
        .quantity-input {
            width: 70px;
            text-align: center;
            border: 2px solid var(--border);
            border-radius: 0.75rem;
            padding: 0.5rem;
            font-weight: 600;
        }
        
        .summary-card {
            background: white;
            border: none;
            border-radius: 1.5rem;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            position: sticky;
            top: 2rem;
        }
        
        .btn-checkout {
            background: linear-gradient(135deg, var(--success), #059669);
            border: none;
            border-radius: 1rem;
            padding: 1rem 2rem;
            font-weight: 600;
            font-size: 1.1rem;
            transition: all 0.3s ease;
        }
        
        .btn-checkout:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(16, 185, 129, 0.3);
        }
        
        .empty-cart {
            padding: 4rem 2rem;
            text-align: center;
        }
        
        .empty-cart-icon {
            font-size: 5rem;
            color: var(--text-light);
            margin-bottom: 1.5rem;
        }
        
        .price-highlight {
            font-size: 1.25rem;
            font-weight: 700;
            color: var(--primary);
        }
        
        .delete-btn {
            color: var(--danger);
            background: none;
            border: none;
            padding: 0.5rem;
            border-radius: 0.5rem;
            transition: all 0.3s ease;
            cursor: pointer;
        }
        
        .delete-btn:hover {
            background-color: rgba(239, 68, 68, 0.1);
            transform: scale(1.1);
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white sticky-top shadow-sm">
        <div class="container">
            <a class="navbar-brand" href="ControladorTienda?accion=listarProductos">
                <i class="fas fa-gem me-2"></i>GabrielaSHOP
            </a>
            <div class="navbar-nav ms-auto">
                <a href="ControladorTienda?accion=listarProductos" class="btn btn-outline-primary">
                    <i class="fas fa-arrow-left me-2"></i>Seguir Comprando
                </a>
            </div>
        </div>
    </nav>

    <!-- Cart Header -->
    <section class="cart-header">
        <div class="container text-center">
            <h1 class="display-5 fw-bold mb-3">
                <i class="fas fa-shopping-cart me-3"></i>Mi Carrito
            </h1>
            <p class="lead opacity-90 mb-0">
                <% if (!carritoVacio) { %>
                    Tienes <%= itemsCarrito.size() %> producto(s) en tu carrito
                <% } else { %>
                    Tu carrito está esperando por productos increíbles
                <% } %>
            </p>
        </div>
    </section>

    <div class="container">
        <!-- Mensajes -->
        <%
            String mensaje = (String) session.getAttribute("mensaje");
            String tipoMensaje = (String) session.getAttribute("tipoMensaje");
            if (mensaje != null) {
        %>
        <div class="alert alert-<%= "error".equals(tipoMensaje) ? "danger" : "success" %> alert-dismissible fade show shadow-sm mb-4" role="alert">
            <div class="d-flex align-items-center">
                <i class="fas fa-<%= "error".equals(tipoMensaje) ? "exclamation-triangle" : "check-circle" %> me-2"></i>
                <span><%= mensaje %></span>
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <%
                session.removeAttribute("mensaje");
                session.removeAttribute("tipoMensaje");
            }
        %>

        <% if (carritoVacio) { %>
        <!-- Empty Cart State -->
        <div class="cart-card">
            <div class="empty-cart">
                <div class="empty-cart-icon">
                    <i class="fas fa-shopping-cart"></i>
                </div>
                <h3 class="text-muted mb-3">Tu carrito está vacío</h3>
                <p class="text-muted mb-4">¡Descubre nuestros productos exclusivos y encuentra algo especial!</p>
                <a href="ControladorTienda?accion=listarProductos" class="btn btn-primary btn-lg">
                    <i class="fas fa-store me-2"></i>Explorar Productos
                </a>
            </div>
        </div>
        <% } else { %>
        <div class="row">
            <!-- Cart Items -->
            <div class="col-lg-8">
                <div class="cart-card">
                    <div class="card-header bg-transparent border-0 py-3">
                        <h5 class="mb-0 text-dark fw-bold">
                            <i class="fas fa-list me-2"></i>Productos en el Carrito
                            <span class="badge bg-primary ms-2 fs-6"><%= itemsCarrito.size() %></span>
                        </h5>
                    </div>
                    <div class="card-body p-0">
                        <%
                            for (Map<String, Object> item : itemsCarrito) {
                                clsProducto producto = (clsProducto) item.get("producto");
                                int cantidad = (Integer) item.get("cantidad");
                                BigDecimal subtotal = (BigDecimal) item.get("subtotal");
                        %>
                        <div class="cart-item">
                            <div class="row align-items-center">
                                <div class="col-md-2">
                                    <img src="https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=200&h=200&fit=crop" 
                                         class="product-image" alt="<%= producto.getNombreproducto() %>">
                                </div>
                                <div class="col-md-4">
                                    <h6 class="fw-bold text-dark mb-2"><%= producto.getNombreproducto() %></h6>
                                    <p class="text-muted small mb-2">
                                        <%= producto.getDescripcion() != null && producto.getDescripcion().length() > 80 ? 
                                            producto.getDescripcion().substring(0, 80) + "..." : 
                                            (producto.getDescripcion() != null ? producto.getDescripcion() : "Producto de alta calidad") %>
                                    </p>
                                    <% if (producto.getNombremarca() != null) { %>
                                    <span class="badge bg-light text-dark small">
                                        <i class="fas fa-tag me-1"></i><%= producto.getNombremarca() %>
                                    </span>
                                    <% } %>
                                </div>
                                <div class="col-md-2 text-center">
                                    <span class="price-highlight">S/. <%= producto.getPrecio() %></span>
                                </div>
                                <div class="col-md-2">
                                    <form action="ControladorTienda" method="post" class="quantity-control">
                                        <input type="hidden" name="accion" value="actualizarCarrito">
                                        <input type="hidden" name="id" value="<%= producto.getIdproducto() %>">
                                        <div class="d-flex align-items-center justify-content-center gap-2">
                                            <button type="button" class="quantity-btn" onclick="updateQuantity(<%= producto.getIdproducto() %>, -1)">
                                                <i class="fas fa-minus"></i>
                                            </button>
                                            <input type="number" name="cantidad" value="<%= cantidad %>" 
                                                   min="1" max="<%= producto.getStock() %>" 
                                                   class="quantity-input" 
                                                   onchange="this.form.submit()">
                                            <button type="button" class="quantity-btn" onclick="updateQuantity(<%= producto.getIdproducto() %>, 1)">
                                                <i class="fas fa-plus"></i>
                                            </button>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-md-2 text-center">
                                    <div class="d-flex flex-column align-items-center gap-2">
                                        <strong class="text-success fs-5">S/. <%= subtotal %></strong>
                                        <form action="ControladorTienda" method="post" class="d-inline">
                                            <input type="hidden" name="accion" value="eliminarDelCarrito">
                                            <input type="hidden" name="id" value="<%= producto.getIdproducto() %>">
                                            <button type="submit" class="delete-btn" title="Eliminar producto">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <% } %>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="d-flex gap-3 mb-4">
                    <a href="ControladorTienda?accion=listarProductos" class="btn btn-outline-primary flex-fill">
                        <i class="fas fa-arrow-left me-2"></i>Seguir Comprando
                    </a>
                    <form action="ControladorTienda" method="post" class="flex-fill">
                        <input type="hidden" name="accion" value="vaciarCarrito">
                        <button type="submit" class="btn btn-outline-danger w-100" onclick="return confirm('¿Estás seguro de vaciar el carrito?')">
                            <i class="fas fa-trash me-2"></i>Vaciar Carrito
                        </button>
                    </form>
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
                        
                        <a href="ControladorTienda?accion=checkout" class="btn btn-checkout w-100 mb-3">
                            <i class="fas fa-credit-card me-2"></i>Proceder al Pago
                        </a>
                        
                        <div class="text-center">
                            <small class="text-muted">
                                <i class="fas fa-lock me-1"></i>Pago 100% seguro y encriptado
                            </small>
                        </div>
                        
                        <!-- Benefits -->
                        <div class="mt-4 pt-3 border-top">
                            <div class="d-flex align-items-center mb-2">
                                <i class="fas fa-shipping-fast text-success me-2"></i>
                                <small class="text-muted">Envío gratis en pedidos mayores a S/. 100</small>
                            </div>
                            <div class="d-flex align-items-center mb-2">
                                <i class="fas fa-undo text-primary me-2"></i>
                                <small class="text-muted">Devoluciones gratis en 30 días</small>
                            </div>
                            <div class="d-flex align-items-center">
                                <i class="fas fa-shield-alt text-warning me-2"></i>
                                <small class="text-muted">Garantía del producto incluida</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-light mt-5 py-5">
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
                <p class="mb-0 text-light opacity-75">&copy; 2024 GabrielaSHOP. Todos los derechos reservados.</p>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function updateQuantity(productId, change) {
            const input = document.querySelector(`input[name="cantidad"][value="${productId}"]`);
            if (!input) return;
            
            let newValue = parseInt(input.value) + change;
            const max = parseInt(input.getAttribute('max'));
            const min = parseInt(input.getAttribute('min'));
            
            if (newValue > max) newValue = max;
            if (newValue < min) newValue = min;
            
            input.value = newValue;
            input.form.submit();
        }

        // Auto-hide alerts
        setTimeout(() => {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            });
        }, 5000);
    </script>
</body>
</html>