<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Table Orders</title>
                <!-- Latest compiled and minified CSS -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
                <!-- Latest compiled JavaScript -->
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <link href="/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"></script>
                <style>
                    #customNavbar {
                        background-color: #343a40;
                        transition: background-color 0.5s ease, box-shadow 0.5s ease;
                    }

                    .sb-topnav {
                        transition: background-color 0.5s ease, box-shadow 0.5s ease;
                    }

                    .sb-topnav:hover {
                        background-color: rgba(0, 0, 0, 0.9);
                        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
                    }

                    .dropdown-menu {
                        transition: opacity 0.5s ease-in-out, transform 0.5s ease;
                        transform: translateY(10px);
                        opacity: 0;
                    }

                    .dropdown-menu.show {
                        transform: translateY(0);
                        opacity: 1;
                    }

                    .dropdown-item:hover {
                        background-color: #007bff;
                        color: #fff;
                    }

                    .nav-link {
                        color: #ffffff;
                    }

                    .nav-link:hover {
                        color: #efe707;
                    }

                    .navbar-brand {
                        font-weight: bold;
                        color: #f8f9fa;
                    }

                    .navbar-brand:hover {
                        color: #ffc107;
                    }

                    .btn-link {
                        color: #f8f9fa;
                    }

                    .btn-link:hover {
                        color: #ffc107;
                    }

                    .nav-item {
                        transition: background-color 0.3s, transform 0.3s;
                    }

                    .nav-item:hover {
                        background-color: rgba(237, 245, 10, 0.1);
                        transform: scale(1.05);
                    }

                    .navbar-text {
                        color: #f8f9fa;
                        font-weight: 500;
                    }

                    .sb-sidenav {
                        background-color: #343a40;
                        transition: width 0.3s, background-color 0.3s;
                    }

                    .sb-sidenav-menu {
                        overflow: hidden;
                    }

                    .sb-sidenav a.nav-link {
                        color: #ffffff;
                        padding: 10px 15px;
                        border-radius: 0.25rem;
                        transition: background-color 0.3s, transform 0.2s;
                    }

                    .sb-sidenav a.nav-link:hover {
                        background-color: #99bbee;
                        transform: translateX(5px);
                        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                    }

                    .sb-nav-link-icon {
                        margin-right: 10px;
                        transition: transform 0.2s;
                    }

                    .sb-nav-link-icon:hover {
                        margin-right: 10px;
                        transition: transform 0.2s;
                    }

                    .sb-sidenav a.nav-link:hover .sb-nav-link-icon {
                        transform: scale(1.1);
                    }

                    .sb-sidenav-footer {
                        background-color: #23272b;
                        color: #ffffff;
                        text-align: center;
                        padding: 15px 0;
                    }

                    .sb-sidenav-footer {
                        background-color: #6c757d;
                        color: #ffffff;
                        text-align: center;
                        padding: 10px 0;
                        position: relative;
                        border-top: 2px solid #fff;
                        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
                    }

                    .sb-sidenav-footer .small {
                        font-size: 0.85rem;
                        opacity: 0.9;
                    }

                    .sb-sidenav-footer h5 {
                        margin: 5px 0;
                        font-size: 1rem;
                        font-weight: bold;
                        color: #ffffff;
                        text-transform: uppercase;
                        letter-spacing: 0.5px;
                    }

                    .sb-sidenav-footer p {
                        margin: 0;
                        font-size: 0.85rem;
                    }

                    .pagination .page-link {
                        color: #007bff;
                        font-size: 1.1rem;
                        border: none;
                        margin: 0 2px;
                        transition: background-color 0.3s;
                    }

                    .pagination .page-link:hover {
                        background-color: #007bff;
                        color: white;
                    }

                    .pagination .active .page-link {
                        background-color: #007bff;
                        color: white;
                        border: none;
                    }

                    /* Màu sắc cho các trạng thái đơn hàng */
                    .badge.bg-warning {
                        background-color: #ffc107;
                        /* Màu vàng cho trạng thái PENDING */
                    }

                    .badge.bg-primary {
                        background-color: #007bff;
                        /* Màu xanh dương cho trạng thái PROCESSING */
                    }

                    .badge.bg-success {
                        background-color: #28a745;
                        /* Màu xanh lá cho trạng thái COMPLETED */
                    }

                    .badge.bg-danger {
                        background-color: #dc3545;
                        /* Màu đỏ cho trạng thái CANCELLED */
                    }

                    .badge.bg-secondary {
                        background-color: #6c757d;
                        /* Màu xám cho trạng thái UNKNOWN */
                    }
                </style>
            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">
                                <h1 class="mt-4">Manage Order</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                </ol>
                                <div class="container mt-5">
                                    <div class="row">
                                        <div class="col-12 mx-auto">
                                            <div class="d-flex justify-content-between align-items-center mb-3">
                                                <h3>Table Orders</h3>
                                            </div>
                                            <hr />
                                            <table class="table table-striped table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>User</th>
                                                        <th>Order Date</th>
                                                        <th>Status</th>
                                                        <th>Total Amount</th>
                                                        <th>Payment Method</th>
                                                        <th>Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="order" items="${orders}">
                                                        <tr>
                                                            <td>${order.orderId}</td>
                                                            <td>${order.user.name}</td>
                                                            <td>${order.orderDate}</td>
                                                            <td>
                                                                <!-- Dropdown for status selection -->
                                                                <div class="dropdown">
                                                                    <button
                                                                        class="btn btn-sm 
                                                                        ${order.status == 'PENDING' ? 'bg-warning' : 
                                                                        (order.status == 'PROCESSING' ? 'bg-primary' : 
                                                                        (order.status == 'COMPLETED' ? 'bg-success' : 
                                                                        (order.status == 'CANCELLED' ? 'bg-danger' : 'bg-secondary')))} dropdown-toggle"
                                                                        type="button"
                                                                        id="statusDropdown${order.orderId}"
                                                                        data-bs-toggle="dropdown" aria-expanded="false">
                                                                        ${order.status}
                                                                        <!-- Hiển thị trạng thái hiện tại -->
                                                                    </button>
                                                                    <ul class="dropdown-menu"
                                                                        aria-labelledby="statusDropdown${order.orderId}">
                                                                        <!-- Check if the current status is 'PENDING', 'PROCESSING', etc., and hide the corresponding option -->
                                                                        <c:if test="${order.status != 'PENDING'}">
                                                                            <li><a class="dropdown-item"
                                                                                    href="/admin/order/updateStatus/${order.orderId}?status=PENDING">PENDING</a>
                                                                            </li>
                                                                        </c:if>
                                                                        <c:if test="${order.status != 'PROCESSING'}">
                                                                            <li><a class="dropdown-item"
                                                                                    href="/admin/order/updateStatus/${order.orderId}?status=PROCESSING">PROCESSING</a>
                                                                            </li>
                                                                        </c:if>
                                                                        <c:if test="${order.status != 'COMPLETED'}">
                                                                            <li><a class="dropdown-item"
                                                                                    href="/admin/order/updateStatus/${order.orderId}?status=COMPLETED">COMPLETED</a>
                                                                            </li>
                                                                        </c:if>
                                                                        <c:if test="${order.status != 'CANCELLED'}">
                                                                            <li><a class="dropdown-item"
                                                                                    href="/admin/order/updateStatus/${order.orderId}?status=CANCELLED">CANCELLED</a>
                                                                            </li>
                                                                        </c:if>
                                                                    </ul>
                                                                </div>
                                                            </td>
                                                            <td>${order.totalAmount}</td>
                                                            <td>${order.paymentMethod}</td>
                                                            <td>
                                                                <a href="/admin/order/${order.orderId}"
                                                                    class="btn btn-success btn-sm">View</a>
                                                                <a href="/admin/product/update/${order.orderId}"
                                                                    class="btn btn-warning btn-sm">Update</a>
                                                                <a href="/admin/product/delete/${order.orderId}"
                                                                    class="btn btn-danger btn-sm">Delete</a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                            <!-- Pagination -->
                                            <nav aria-label="Page navigation example">
                                                <ul class="pagination justify-content-center">
                                                    <!-- Previous button -->
                                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                                        <a class="page-link" href="?page=${currentPage - 1}"
                                                            aria-label="Previous">
                                                            <span aria-hidden="true">&laquo;</span>
                                                        </a>
                                                    </li>

                                                    <!-- If there are pages before the displayed range, show '1' and an ellipsis -->
                                                    <c:if test="${totalPages > 5 && currentPage > 3}">
                                                        <li class="page-item"><a class="page-link" href="?page=1">1</a>
                                                        </li>
                                                        <li class="page-item disabled"><span
                                                                class="page-link">...</span></li>
                                                    </c:if>

                                                    <!-- Loop through a maximum of 5 pages around the current page -->
                                                    <c:forEach var="i" begin="${currentPage > 3 ? currentPage - 2 : 1}"
                                                        end="${(currentPage > totalPages - 3) ? totalPages : (currentPage > 3 ? currentPage + 2 : 5)}">
                                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                            <a class="page-link" href="?page=${i}">${i}</a>
                                                        </li>
                                                    </c:forEach>

                                                    <!-- If there are pages after the displayed range, show an ellipsis and the last page -->
                                                    <c:if test="${totalPages > 5 && currentPage < totalPages - 2}">
                                                        <li class="page-item disabled"><span
                                                                class="page-link">...</span></li>
                                                        <li class="page-item"><a class="page-link"
                                                                href="?page=${totalPages}">${totalPages}</a></li>
                                                    </c:if>

                                                    <!-- Next button -->
                                                    <li
                                                        class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                                        <a class="page-link" href="?page=${currentPage + 1}"
                                                            aria-label="Next">
                                                            <span aria-hidden="true">&raquo;</span>
                                                        </a>
                                                    </li>
                                                </ul>
                                            </nav>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </main>
                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>
                <script>
                    document.addEventListener('DOMContentLoaded', function () {
                        var dropdowns = document.querySelectorAll('.dropdown-toggle');

                        dropdowns.forEach(function (dropdown) {
                            dropdown.addEventListener('click', function () {
                                var menu = this.nextElementSibling;
                                var isVisible = menu.classList.contains('show');
                                var allMenus = document.querySelectorAll('.dropdown-menu');

                                // Đóng tất cả các menu
                                allMenus.forEach(function (m) {
                                    m.classList.remove('show');
                                });

                                // Nếu menu hiện tại không mở, mở nó
                                if (!isVisible) {
                                    menu.classList.add('show');
                                }
                            });
                        });

                        // Đóng dropdown nếu click ra ngoài
                        document.addEventListener('click', function (event) {
                            if (!event.target.closest('.dropdown')) {
                                document.querySelectorAll('.dropdown-menu').forEach(function (menu) {
                                    menu.classList.remove('show');
                                });
                            }
                        });

                        // Thêm sự kiện cho các lựa chọn trong dropdown để cập nhật trạng thái
                        var menuItems = document.querySelectorAll('.dropdown-item');
                        menuItems.forEach(function (item) {
                            item.addEventListener('click', function () {
                                var status = this.innerText;
                                var button = this.closest('.dropdown').querySelector('.dropdown-toggle');
                                button.innerText = status; // Cập nhật trạng thái trên nút
                            });
                        });
                    });
                </script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
            </body>

            </html>