<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.clsProducto, java.util.List" %>
<%
    java.util.Map<Integer, Integer> carrito = (java.util.Map<Integer, Integer>) session.getAttribute("carrito");
    int totalItems = (carrito != null) ? carrito.values().stream().mapToInt(Integer::intValue).sum() : 0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tienda Online - Descubre lo Mejor</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary: #6366f1;
            --primary-dark: #4f46e5;
            --secondary: #f8fafc;
            --accent: #f59e0b;
            --text-dark: #1e293b;
            --text-light: #64748b;
            --border: #e2e8f0;
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
        
        .hero-section {
            background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
            color: white;
            padding: 4rem 0;
            margin-bottom: 3rem;
            border-radius: 0 0 2rem 2rem;
        }
        
        .product-card {
            background: white;
            border: none;
            border-radius: 1.5rem;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            overflow: hidden;
            margin-bottom: 2rem;
        }
        
        .product-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 20px 40px rgba(99, 102, 241, 0.15);
        }
        
        .card-img-top {
            height: 220px;
            object-fit: cover;
            transition: transform 0.3s ease;
        }
        
        .product-card:hover .card-img-top {
            transform: scale(1.05);
        }
        
        .product-badge {
            position: absolute;
            top: 1rem;
            left: 1rem;
            z-index: 2;
        }
        
        .color-indicator {
            width: 24px;
            height: 24px;
            border-radius: 50%;
            display: inline-block;
            border: 3px solid white;
            box-shadow: 0 2px 8px rgba(0,0,0,0.15);
        }
        
        .price-tag {
            font-weight: 700;
            font-size: 1.25rem;
            color: var(--primary);
        }
        
        .stock-badge {
            font-size: 0.75rem;
            padding: 0.35em 0.65em;
        }
        
        .btn-add-cart {
            background: linear-gradient(135deg, var(--primary), var(--primary-dark));
            border: none;
            border-radius: 0.75rem;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        
        .btn-add-cart:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(99, 102, 241, 0.3);
        }
        
        .cart-floating {
            position: fixed;
            bottom: 2rem;
            right: 2rem;
            z-index: 1000;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            border-radius: 50%;
            width: 60px;
            height: 60px;
        }
        
        .category-filter {
            background: white;
            border-radius: 1rem;
            padding: 1.5rem;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            margin-bottom: 2rem;
        }
        
        .search-box {
            position: relative;
        }
        
        .search-box input {
            border-radius: 2rem;
            padding-left: 3rem;
            border: 2px solid var(--border);
            transition: all 0.3s ease;
        }
        
        .search-box input:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
        }
        
        .search-icon {
            position: absolute;
            left: 1rem;
            top: 50%;
            transform: translateY(-50%);
            color: var(--text-light);
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white sticky-top shadow-sm">
        <div class="container">
            <a class="navbar-brand" href="#">
                <i class="fas fa-gem me-2"></i>GabrielaSHOP
            </a>
            
            <div class="navbar-nav ms-auto">
                <a href="ControladorTienda?accion=verCarrito" class="btn btn-primary position-relative px-4 py-2">
                    <i class="fas fa-shopping-cart"></i>
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-accent">
                        <%= totalItems %>
                    </span>
                </a>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero-section">
        <div class="container text-center">
            <h1 class="display-4 fw-bold mb-3">Descubre Productos Excepcionales</h1>
            <p class="lead mb-4 opacity-90">Calidad premium, precios increíbles y entrega rápida</p>
            
            <!-- Search Box -->
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="search-box">
                        <i class="fas fa-search search-icon"></i>
                        <input type="text" class="form-control form-control-lg" placeholder="Buscar productos...">
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Main Content -->
    <div class="container">
        <!-- Category Filters -->
        <div class="category-filter">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <div class="d-flex flex-wrap gap-2">
                        <button class="btn btn-outline-primary active">Todos</button>
                        <button class="btn btn-outline-primary">Electrónicos</button>
                        <button class="btn btn-outline-primary">Hogar</button>
                        <button class="btn btn-outline-primary">Moda</button>
                        <button class="btn btn-outline-primary">Deportes</button>
                    </div>
                </div>
                <div class="col-md-4 text-end">
                    <select class="form-select" style="max-width: 200px;">
                        <option>Ordenar por: Destacados</option>
                        <option>Precio: Menor a Mayor</option>
                        <option>Precio: Mayor a Menor</option>
                        <option>Mejor Valorados</option>
                    </select>
                </div>
            </div>
        </div>

        <!-- Mensajes -->
        <%
            String mensaje = (String) session.getAttribute("mensaje");
            String tipoMensaje = (String) session.getAttribute("tipoMensaje");
            if (mensaje != null) {
        %>
        <div class="alert alert-<%= "error".equals(tipoMensaje) ? "danger" : "success" %> alert-dismissible fade show shadow-sm" role="alert">
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

        <!-- Products Grid -->
        <div class="row">
            <%
                List<clsProducto> productos = (List<clsProducto>) request.getAttribute("productos");
                if (productos != null && !productos.isEmpty()) {
                    for (clsProducto producto : productos) {
            %>
            <div class="col-xl-3 col-lg-4 col-md-6">
                <div class="card product-card h-100">
                    <div class="position-relative">
                        <img src="https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=300&fit=crop" 
                             class="card-img-top" alt="<%= producto.getNombreproducto() %>">
                        <div class="product-badge">
                            <% if (producto.getStock() > 10) { %>
                            <span class="badge bg-success">Disponible</span>
                            <% } else if (producto.getStock() > 0) { %>
                            <span class="badge bg-warning">Últimas unidades</span>
                            <% } else { %>
                            <span class="badge bg-danger">Agotado</span>
                            <% } %>
                        </div>
                    </div>
                    
                    <div class="card-body d-flex flex-column">
                        <div class="mb-2">
                            <% if (producto.getNombrecategoria() != null) { %>
                            <span class="badge bg-light text-primary me-1"><%= producto.getNombrecategoria() %></span>
                            <% } %>
                            <% if (producto.getNombremarca() != null) { %>
                            <span class="badge bg-light text-secondary"><%= producto.getNombremarca() %></span>
                            <% } %>
                        </div>
                        
                        <h6 class="card-title fw-bold text-dark mb-2"><%= producto.getNombreproducto() %></h6>
                        
                        <p class="card-text flex-grow-1 small text-muted mb-3">
                            <%= producto.getDescripcion() != null && producto.getDescripcion().length() > 100 ? 
                                producto.getDescripcion().substring(0, 100) + "..." : 
                                (producto.getDescripcion() != null ? producto.getDescripcion() : "Producto de alta calidad") %>
                        </p>
                        
                        <% if (producto.getCodigoHex() != null) { %>
                        <div class="mb-3">
                            <small class="text-muted d-block mb-1">Color:</small>
                            <div class="d-flex align-items-center gap-2">
                                <span class="color-indicator" style="background-color: <%= producto.getCodigoHex() %>"></span>
                                <small class="text-dark fw-medium"><%= producto.getNombrecolor() %></small>
                            </div>
                        </div>
                        <% } %>
                        
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="price-tag">S/. <%= producto.getPrecio() %></span>
                            <span class="badge <%= producto.getStock() > 10 ? "bg-success" : producto.getStock() > 0 ? "bg-warning" : "bg-danger" %> stock-badge">
                                <%= producto.getStock() %> unidades
                            </span>
                        </div>
                        
                        <form action="ControladorTienda" method="post">
                            <input type="hidden" name="accion" value="agregarAlCarrito">
                            <input type="hidden" name="id" value="<%= producto.getIdproducto() %>">
                            <div class="input-group">
                                <input type="number" name="cantidad" value="1" min="1" max="<%= producto.getStock() %>" 
                                       class="form-control border-end-0" 
                                       style="border-radius: 0.75rem 0 0 0.75rem;"
                                       <%= producto.getStock() == 0 ? "disabled" : "" %>>
                                <button type="submit" class="btn btn-add-cart px-4" 
                                        <%= producto.getStock() == 0 ? "disabled" : "" %>
                                        style="border-radius: 0 0.75rem 0.75rem 0;">
                                    <i class="fas fa-cart-plus"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <%
                    }
                } else {
            %>
            <div class="col-12">
                <div class="text-center py-5">
                    <div class="empty-state">
                        <i class="fas fa-box-open fa-4x text-muted mb-4"></i>
                        <h3 class="text-muted">No hay productos disponibles</h3>
                        <p class="text-muted mb-4">Estamos trabajando para traerte los mejores productos.</p>
                        <a href="#" class="btn btn-primary">
                            <i class="fas fa-sync-alt me-2"></i>Recargar
                        </a>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    </div>

    <!-- Floating Cart Button -->
    <a href="ControladorTienda?accion=verCarrito" class="btn btn-primary cart-floating d-flex align-items-center justify-content-center">
        <i class="fas fa-shopping-cart text-white"></i>
    </a>

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
        // Filter buttons active state
        document.querySelectorAll('.category-filter .btn').forEach(btn => {
            btn.addEventListener('click', function() {
                document.querySelectorAll('.category-filter .btn').forEach(b => b.classList.remove('active'));
                this.classList.add('active');
            });
        });

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