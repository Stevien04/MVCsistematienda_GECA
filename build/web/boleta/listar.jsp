<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="Modelo.clsBoleta"%>
<%@page import="Modelo.clsDetalle"%>
<%
    Object empleadoObj = session.getAttribute("empleado");
    if (empleadoObj == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<clsBoleta> listaBoletas = (List<clsBoleta>) request.getAttribute("boletas");
    clsBoleta boletaSeleccionada = (clsBoleta) request.getAttribute("boletaSeleccionada");
    List<clsDetalle> detallesBoleta = (List<clsDetalle>) request.getAttribute("detallesBoleta");
    Integer idBoletaSeleccionada = (Integer) request.getAttribute("idBoletaSeleccionada");

    if (listaBoletas == null) {
        listaBoletas = Collections.emptyList();
    }
    if (detallesBoleta == null) {
        detallesBoleta = Collections.emptyList();
    }
    if (idBoletaSeleccionada == null) {
        idBoletaSeleccionada = boletaSeleccionada != null ? boletaSeleccionada.getIdboleta() : 0;
    }

    String mensaje = (String) session.getAttribute("mensaje");
    String tipoMensaje = (String) session.getAttribute("tipoMensaje");

    if (mensaje != null) {
        session.removeAttribute("mensaje");
        session.removeAttribute("tipoMensaje");
    }

    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
    DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");
%>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Boletas - Sistema Tienda</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

        <jsp:include page="../includes/styles.jsp" />

        <style>
            .card-custom {
                border: none;
                border-radius: 15px;
                box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            }
            .btn-action {
                width: 35px;
                height: 35px;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                margin: 0 2px;
                border-radius: 8px;
            }
            .table th {
                border-top: none;
                font-weight: 600;
                color: #495057;
                background-color: #f8f9fa;
                padding: 1rem 0.75rem;
            }
            .table td {
                padding: 1rem 0.75rem;
                vertical-align: middle;
            }
            .badge-estado {
                font-size: 0.75rem;
                padding: 0.35em 0.65em;
            }
            .resumen-boleta span {
                display: inline-block;
                margin-right: 1.5rem;
                color: #6c757d;
            }
        </style>
    </head>
    <body>
        <jsp:include page="../includes/sidebar.jsp" />

        <div class="main-content" id="mainContent">
            <jsp:include page="../includes/header.jsp" />

            <div class="container-fluid p-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h2 class="h4 mb-1">
                            <i class="fas fa-receipt me-2 text-primary"></i>Gestión de Boletas
                        </h2>
                        <p class="text-muted mb-0">Consulta y exporta la información de boletas</p>
                    </div>
                    <div class="d-flex gap-2">
                        <a href="${pageContext.request.contextPath}/ControladorBoleta?accion=exportarExcel" class="btn btn-outline-success">
                            <i class="fas fa-file-excel me-2"></i>Boletas Excel
                        </a>
                        <a href="${pageContext.request.contextPath}/ControladorBoleta?accion=exportarPdf" class="btn btn-outline-danger">
                            <i class="fas fa-file-pdf me-2"></i>Boletas PDF
                        </a>
                        <a href="${pageContext.request.contextPath}/ControladorBoleta?accion=nuevo" class="btn btn-primary">
                            <i class="fas fa-plus me-2"></i>Nueva Boleta
                        </a>
                    </div>
                </div>

                <% if (mensaje != null) {%>
                <div class="alert alert-<%= tipoMensaje != null ? tipoMensaje : "info"%> alert-dismissible fade show" role="alert">
                    <i class="fas fa-<%= "success".equals(tipoMensaje) ? "check-circle" : "exclamation-triangle"%> me-2"></i>
                    <%= mensaje%>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <% } %>

                <div class="card card-custom">
                    <div class="card-body">
                        <% if (!listaBoletas.isEmpty()) { %>
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Número</th>
                                        <th>Fecha</th>
                                        <th>Hora</th>
                                        <th>Cliente</th>
                                        <th>Empleado</th>
                                        <th class="text-end">Subtotal</th>
                                        <th class="text-end">IGV</th>
                                        <th class="text-end">Total</th>
                                        <th class="text-center">Estado</th>
                                        <th class="text-center">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (clsBoleta boleta : listaBoletas) {
                                            boolean seleccionada = boletaSeleccionada != null && boleta.getIdboleta() == boletaSeleccionada.getIdboleta();
                                    %>
                                    <tr class="<%= seleccionada ? "table-active" : ""%>">
                                        <td><%= boleta.getIdboleta()%></td>
                                        <td class="fw-semibold text-primary"><%= boleta.getNumeroBoleta() != null ? boleta.getNumeroBoleta() : "-"%></td>
                                        <td><%= boleta.getFechaEmision() != null ? boleta.getFechaEmision().format(formatoFecha) : "-"%></td>
                                        <td><%= boleta.getHoraEmision() != null ? boleta.getHoraEmision().format(formatoHora) : "-"%></td>
                                        <td>
                                            <div class="fw-semibold"><%= boleta.getNombreCliente() != null ? boleta.getNombreCliente() : "Cliente no registrado"%></div>
                                            <small class="text-muted">DNI: <%= boleta.getDniCliente() != null ? boleta.getDniCliente() : "-"%></small>
                                        </td>
                                        <td><%= boleta.getNombreEmpleado() != null ? boleta.getNombreEmpleado() : "-"%></td>
                                        <td class="text-end">S/ <%= boleta.getSubtotal() != null ? formatoMoneda.format(boleta.getSubtotal()) : "0.00"%></td>
                                        <td class="text-end">S/ <%= boleta.getIgv() != null ? formatoMoneda.format(boleta.getIgv()) : "0.00"%></td>
                                        <td class="text-end fw-semibold">S/ <%= boleta.getTotal() != null ? formatoMoneda.format(boleta.getTotal()) : "0.00"%></td>
                                        <td class="text-center">
                                            <% String estado = boleta.getEstadoBoleta() != null ? boleta.getEstadoBoleta() : "";%>
                                            <span class="badge badge-estado <%= "ACTIVA".equalsIgnoreCase(estado) ? "bg-success" : "bg-secondary"%>"><%= estado%></span>
                                        </td>
                                        <td class="text-center">
                                            <a href="${pageContext.request.contextPath}/ControladorBoleta?accion=listar&idBoleta=<%= boleta.getIdboleta()%>" class="btn btn-outline-secondary btn-action" title="Ver detalle">
                                                <i class="fas fa-list"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/ControladorBoleta?accion=ver&id=<%= boleta.getIdboleta()%>" class="btn btn-outline-primary btn-action" title="Ver boleta">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/ControladorBoleta?accion=editar&id=<%= boleta.getIdboleta()%>" class="btn btn-outline-warning btn-action" title="Editar">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/ControladorBoleta?accion=anular&id=<%= boleta.getIdboleta()%>" class="btn btn-outline-danger btn-action" title="Anular" onclick="return confirm('¿Desea anular la boleta <%= boleta.getNumeroBoleta()%>?');">
                                                <i class="fas fa-ban"></i>
                                            </a>
                                        </td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                        <% } else { %>
                        <div class="text-center py-5">
                            <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                            <h5 class="text-muted">No se encontraron boletas registradas</h5>
                            <p class="text-secondary mb-0">Empieza generando una nueva boleta</p>
                        </div>
                        <% } %>
                    </div>
                </div>

                <div class="card card-custom mt-4">
                    <div class="card-header bg-transparent d-flex flex-wrap justify-content-between align-items-center gap-3">
                        <div>
                            <h5 class="mb-1">Detalle de boleta</h5>
                            <p class="text-muted mb-0">Selecciona una boleta para revisar y exportar su detalle</p>
                        </div>
                        <div class="d-flex flex-wrap gap-2 align-items-center">
                            <form action="${pageContext.request.contextPath}/ControladorBoleta" method="get" class="d-flex gap-2 align-items-center">
                                <input type="hidden" name="accion" value="listar">
                                <select name="idBoleta" class="form-select" onchange="this.form.submit()">
                                    <option value="">Selecciona una boleta</option>
                                    <% for (clsBoleta boleta : listaBoletas) {%>
                                    <option value="<%= boleta.getIdboleta()%>" <%= boleta.getIdboleta() == idBoletaSeleccionada ? "selected" : ""%>>
                                        <%= boleta.getNumeroBoleta() != null ? boleta.getNumeroBoleta() : ("Boleta " + boleta.getIdboleta())%>
                                    </option>
                                    <% } %>
                                </select>
                            </form>
                            <% if (boletaSeleccionada != null) {%>
                            <div class="btn-group">
                                <a href="${pageContext.request.contextPath}/ControladorBoleta?accion=exportarDetalleExcel&idBoleta=<%= boletaSeleccionada.getIdboleta()%>" class="btn btn-outline-success">
                                    <i class="fas fa-file-excel me-2"></i>Detalle Excel
                                </a>
                                <a href="${pageContext.request.contextPath}/ControladorBoleta?accion=exportarDetallePdf&idBoleta=<%= boletaSeleccionada.getIdboleta()%>" class="btn btn-outline-danger">
                                    <i class="fas fa-file-pdf me-2"></i>Detalle PDF
                                </a>
                            </div>
                            <% } %>
                        </div>
                    </div>
                    <div class="card-body">
                        <% if (boletaSeleccionada != null) {%>
                        <div class="mb-4 resumen-boleta">
                            <span><strong>Número:</strong> <%= boletaSeleccionada.getNumeroBoleta() != null ? boletaSeleccionada.getNumeroBoleta() : "-"%></span>
                            <span><strong>Cliente:</strong> <%= boletaSeleccionada.getNombreCliente() != null ? boletaSeleccionada.getNombreCliente() : "-"%></span>
                            <span><strong>Total:</strong> S/ <%= boletaSeleccionada.getTotal() != null ? formatoMoneda.format(boletaSeleccionada.getTotal()) : "0.00"%></span>
                        </div>

                        <% if (!detallesBoleta.isEmpty()) { %>
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Producto</th>
                                        <th class="text-center">Cantidad</th>
                                        <th class="text-end">Precio Unitario</th>
                                        <th class="text-end">Importe</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (clsDetalle detalle : detallesBoleta) {%>
                                    <tr>
                                        <td><%= detalle.getIddetalle()%></td>
                                        <td><%= detalle.getNombreproducto() != null ? detalle.getNombreproducto() : "-"%></td>
                                        <td class="text-center"><%= detalle.getCantidad()%></td>
                                        <td class="text-end">S/ <%= detalle.getPrecioUnitario() != null ? formatoMoneda.format(detalle.getPrecioUnitario()) : "0.00"%></td>
                                        <td class="text-end fw-semibold">S/ <%= detalle.getImporte() != null ? formatoMoneda.format(detalle.getImporte()) : "0.00"%></td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                        <% } else { %>
                        <div class="text-center py-4">
                            <i class="fas fa-box-open fa-2x text-muted mb-3"></i>
                            <p class="text-muted mb-0">La boleta seleccionada no tiene detalles registrados.</p>
                        </div>
                        <% } %>
                        <% } else { %>
                        <div class="text-center py-5">
                            <i class="fas fa-hand-pointer fa-3x text-muted mb-3"></i>
                            <h5 class="text-muted">Selecciona una boleta para visualizar su detalle</h5>
                            <p class="text-secondary mb-0">Utiliza el selector superior para elegir una boleta</p>
                        </div>
                        <% }%>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>