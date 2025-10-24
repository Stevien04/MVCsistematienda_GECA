<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="Modelo.clsResumenVenta"%>
<%@page import="ModeloDAO.clsEstadisticaDAO"%>
<%
    Object empleadoObj = session.getAttribute("empleado");
    if (empleadoObj == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    int totalClientes = 0;
    int totalProductos = 0;
    int totalVentas = 0;
    int totalUnidadesVendidas = 0;
    double totalIngresos = 0.0;
    String categoriaMasVendida = "Sin datos";
    String productoMasVendido = "Sin datos";
    List<clsResumenVenta> categoriasTop = new ArrayList<>();
    List<clsResumenVenta> productosTop = new ArrayList<>();
    clsEstadisticaDAO estadisticaDAO = null;

    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "PE"));
    NumberFormat integerFormatter = NumberFormat.getIntegerInstance(new Locale("es", "PE"));

    try {
        estadisticaDAO = new clsEstadisticaDAO();
        totalClientes = estadisticaDAO.obtenerTotalClientesActivos();
        totalProductos = estadisticaDAO.obtenerTotalProductosActivos();
        totalVentas = estadisticaDAO.obtenerTotalVentas();
        totalUnidadesVendidas = estadisticaDAO.obtenerTotalProductosVendidos();
        totalIngresos = estadisticaDAO.obtenerTotalIngresos();
        categoriasTop = estadisticaDAO.obtenerTopCategorias(5);
        productosTop = estadisticaDAO.obtenerTopProductos(5);

        if (!categoriasTop.isEmpty()) {
            clsResumenVenta topCategoria = categoriasTop.get(0);
            categoriaMasVendida = topCategoria.getNombre() + " (" + integerFormatter.format(topCategoria.getTotal()) + " uds)";
        }

        if (!productosTop.isEmpty()) {
            clsResumenVenta topProducto = productosTop.get(0);
            productoMasVendido = topProducto.getNombre() + " (" + integerFormatter.format(topProducto.getTotal()) + " uds)";
        }
    } catch (Exception e) {
        System.out.println("Error al cargar estadísticas: " + e.getMessage());
    } finally {
        if (estadisticaDAO != null) {
            estadisticaDAO.cerrarConexion();
        }
    }

    StringBuilder categoriasLabels = new StringBuilder();
    StringBuilder categoriasValues = new StringBuilder();
    for (int i = 0; i < categoriasTop.size(); i++) {
        clsResumenVenta resumen = categoriasTop.get(i);
        if (i > 0) {
            categoriasLabels.append(",");
            categoriasValues.append(",");
        }
        categoriasLabels.append("\"").append(resumen.getNombre().replace("\"", "\\\"")).append("\"");
        categoriasValues.append(resumen.getTotal());
    }

    StringBuilder productosLabels = new StringBuilder();
    StringBuilder productosValues = new StringBuilder();
    for (int i = 0; i < productosTop.size(); i++) {
        clsResumenVenta resumen = productosTop.get(i);
        if (i > 0) {
            productosLabels.append(",");
            productosValues.append(",");
        }
        productosLabels.append("\"").append(resumen.getNombre().replace("\"", "\\\"")).append("\"");
        productosValues.append(resumen.getTotal());
    }

    int maxCategorias = categoriasTop.isEmpty() ? 1 : Math.max(1, categoriasTop.get(0).getTotal());
    int maxProductos = productosTop.isEmpty() ? 1 : Math.max(1, productosTop.get(0).getTotal());
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Estadísticas - Sistema Tienda GECA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <jsp:include page="/includes/styles.jsp" />
    <style>
        .stat-card {
            border: none;
            border-radius: 18px;
            padding: 1.75rem;
            color: #fff;
            position: relative;
            overflow: hidden;
            box-shadow: 0 12px 25px rgba(0,0,0,0.08);
            height: 100%;
        }
        .stat-card .icon-wrapper {
            width: 60px;
            height: 60px;
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            background: rgba(255,255,255,0.2);
            margin-bottom: 1.25rem;
        }
        .stat-card .stat-value {
            font-size: 2rem;
            font-weight: 700;
        }
        .stat-card .stat-label {
            font-size: 0.95rem;
            text-transform: uppercase;
            letter-spacing: 1px;
            opacity: 0.85;
        }
        .card-dashboard {
            border: none;
            border-radius: 18px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.08);
            overflow: hidden;
        }
        .card-dashboard .card-header {
            background: transparent;
            border-bottom: none;
            padding: 1.5rem 1.5rem 0;
        }
        .card-dashboard .card-body {
            padding: 1.5rem;
        }
        .bg-gradient-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        .bg-gradient-success { background: linear-gradient(135deg, #28a745 0%, #58d68d 100%); }
        .bg-gradient-warning { background: linear-gradient(135deg, #f39c12 0%, #f1c40f 100%); }
        .bg-gradient-danger { background: linear-gradient(135deg, #e74c3c 0%, #ff7675 100%); }
        .bg-gradient-info { background: linear-gradient(135deg, #17a2b8 0%, #5dade2 100%); }
        .bg-gradient-secondary { background: linear-gradient(135deg, #8e44ad 0%, #a569bd 100%); }
        .list-group-transparent .list-group-item {
            background-color: transparent;
            border: none;
            padding-left: 0;
            padding-right: 0;
        }
        .list-group-transparent .badge {
            font-size: 0.85rem;
        }
        /* Corrección: limitar altura del gráfico de productos */
        #chartProductos {
            max-height: 250px !important;
        }
        .card-dashboard .card-body {
            overflow-y: auto;
        }
    </style>
</head>
<body>
    <jsp:include page="/includes/sidebar.jsp" />
    <div class="main-content" id="mainContent">
        <jsp:include page="/includes/header.jsp" />

        <div class="container-fluid py-4">
            <div class="row g-4">
                <div class="col-xxl-3 col-md-6">
                    <div class="stat-card bg-gradient-primary">
                        <div class="icon-wrapper"><i class="fas fa-users"></i></div>
                        <div class="stat-value"><%= integerFormatter.format(totalClientes) %></div>
                        <div class="stat-label">Clientes activos</div>
                        <small class="opacity-75">Clientes registrados con estado activo.</small>
                    </div>
                </div>
                <div class="col-xxl-3 col-md-6">
                    <div class="stat-card bg-gradient-success">
                        <div class="icon-wrapper"><i class="fas fa-boxes"></i></div>
                        <div class="stat-value"><%= integerFormatter.format(totalProductos) %></div>
                        <div class="stat-label">Productos disponibles</div>
                        <small class="opacity-75">Inventario activo listo para la venta.</small>
                    </div>
                </div>
                <div class="col-xxl-3 col-md-6">
                    <div class="stat-card bg-gradient-warning">
                        <div class="icon-wrapper"><i class="fas fa-receipt"></i></div>
                        <div class="stat-value"><%= integerFormatter.format(totalVentas) %></div>
                        <div class="stat-label">Ventas registradas</div>
                        <small class="opacity-75">Comprobantes emitidos en el sistema.</small>
                    </div>
                </div>
                <div class="col-xxl-3 col-md-6">
                    <div class="stat-card bg-gradient-danger">
                        <div class="icon-wrapper"><i class="fas fa-shopping-basket"></i></div>
                        <div class="stat-value"><%= integerFormatter.format(totalUnidadesVendidas) %></div>
                        <div class="stat-label">Unidades vendidas</div>
                        <small class="opacity-75">Cantidad total de productos vendidos.</small>
                    </div>
                </div>
            </div>

            <div class="row g-4 mt-1">
                <div class="col-xxl-3 col-md-6">
                    <div class="stat-card bg-gradient-info">
                        <div class="icon-wrapper"><i class="fas fa-sack-dollar"></i></div>
                        <div class="stat-value"><%= currencyFormatter.format(totalIngresos) %></div>
                        <div class="stat-label">Ingresos totales</div>
                        <small class="opacity-75">Monto generado por las ventas registradas.</small>
                    </div>
                </div>
                <div class="col-xxl-3 col-md-6">
                    <div class="stat-card bg-gradient-secondary">
                        <div class="icon-wrapper"><i class="fas fa-crown"></i></div>
                        <div class="stat-value" style="font-size: 1.2rem; line-height: 1.4;"><%= categoriaMasVendida %></div>
                        <div class="stat-label">Catálogo más vendido</div>
                        <small class="opacity-75">Categoría con mayor rotación en ventas.</small>
                    </div>
                </div>
                <div class="col-xxl-6 col-md-12">
                    <div class="stat-card bg-gradient-primary" style="background: linear-gradient(135deg, rgba(102,126,234,0.95) 0%, rgba(118,75,162,0.95) 100%);">
                        <div class="icon-wrapper"><i class="fas fa-star"></i></div>
                        <div class="stat-value" style="font-size: 1.2rem; line-height: 1.4;"><%= productoMasVendido %></div>
                        <div class="stat-label">Producto destacado</div>
                        <small class="opacity-75">El artículo con mayor demanda en el periodo.</small>
                    </div>
                </div>
            </div>

            <div class="row g-4 mt-1">
                <div class="col-xl-7">
                    <div class="card card-dashboard h-100">
                        <div class="card-header">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-chart-pie text-primary me-2"></i>Distribución de ventas por categoría
                            </h5>
                        </div>
                        <div class="card-body">
                            <% if (categoriasTop.isEmpty()) { %>
                                <div class="text-center text-muted py-5">
                                    <i class="fas fa-info-circle fa-2x mb-3 text-primary"></i>
                                    <p class="mb-0">Aún no hay ventas registradas para mostrar estadísticas de categorías.</p>
                                </div>
                            <% } else { %>
                                <canvas id="chartCategorias" height="220"></canvas>
                                <div class="mt-4">
                                    <ul class="list-group list-group-transparent">
                                        <% for (clsResumenVenta resumen : categoriasTop) { 
                                            int porcentaje = (int) Math.round((resumen.getTotal() * 100.0) / maxCategorias);
                                        %>
                                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                                <div><i class="fas fa-tag text-primary me-2"></i><strong><%= resumen.getNombre() %></strong></div>
                                                <span class="badge bg-light text-dark">
                                                    <%= integerFormatter.format(resumen.getTotal()) %> uds · <%= porcentaje %>%
                                                </span>
                                            </li>
                                        <% } %>
                                    </ul>
                                </div>
                            <% } %>
                        </div>
                    </div>
                </div>
                <div class="col-xl-5">
                    <div class="card card-dashboard h-100">
                        <div class="card-header">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-chart-line text-success me-2"></i>Productos con mayor demanda
                            </h5>
                        </div>
                        <div class="card-body">
                            <% if (productosTop.isEmpty()) { %>
                                <div class="text-center text-muted py-5">
                                    <i class="fas fa-box-open fa-2x mb-3 text-success"></i>
                                    <p class="mb-0">No hay productos vendidos para analizar.</p>
                                </div>
                            <% } else { %>
                                <div style="height:250px;">
                                    <canvas id="chartProductos"></canvas>
                                </div>
                                <div class="mt-4">
                                    <% for (clsResumenVenta resumen : productosTop) { 
                                        int porcentaje = (int) Math.round((resumen.getTotal() * 100.0) / maxProductos);
                                    %>
                                        <div class="mb-3">
                                            <div class="d-flex justify-content-between align-items-center">
                                                <strong><i class="fas fa-box text-success me-2"></i><%= resumen.getNombre() %></strong>
                                                <span class="text-muted"><%= integerFormatter.format(resumen.getTotal()) %> uds</span>
                                            </div>
                                            <div class="progress" style="height: 6px;">
                                                <div class="progress-bar bg-success" role="progressbar"
                                                     style="width: <%= porcentaje %>%" aria-valuenow="<%= porcentaje %>"
                                                     aria-valuemin="0" aria-valuemax="100"></div>
                                            </div>
                                        </div>
                                    <% } %>
                                </div>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
        const chartCategoriasEl = document.getElementById('chartCategorias');
        if (chartCategoriasEl) {
            new Chart(chartCategoriasEl.getContext('2d'), {
                type: 'doughnut',
                data: {
                    labels: [<%= categoriasLabels.toString() %>],
                    datasets: [{
                        data: [<%= categoriasValues.toString() %>],
                        backgroundColor: ['#667eea', '#764ba2', '#28a745', '#f39c12', '#17a2b8'],
                        borderWidth: 0
                    }]
                },
                options: {
                    plugins: { legend: { position: 'bottom', labels: { usePointStyle: true } } }
                }
            });
        }

        const chartProductosEl = document.getElementById('chartProductos');
        if (chartProductosEl) {
            new Chart(chartProductosEl.getContext('2d'), {
                type: 'bar',
                data: {
                    labels: [<%= productosLabels.toString() %>],
                    datasets: [{
                        label: 'Unidades vendidas',
                        data: [<%= productosValues.toString() %>],
                        backgroundColor: '#28a745',
                        borderRadius: 6,
                        maxBarThickness: 28
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: true,
                    aspectRatio: 2,
                    scales: { y: { beginAtZero: true, ticks: { precision: 0 } } },
                    plugins: { legend: { display: false } }
                }
            });
        }
    </script>
</body>
</html>
