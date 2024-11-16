<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
                <html lang="en">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Order Details</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
                        rel="stylesheet">
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                    <link href="/css/styles.css" rel="stylesheet" />
                    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
                        crossorigin="anonymous"></script>
                </head>

                <body class="sb-nav-fixed">
                    <jsp:include page="../layout/header.jsp" />
                    <div id="layoutSidenav">
                        <jsp:include page="../layout/sidebar.jsp" />
                        <div id="layoutSidenav_content">
                            <main>
                                <div class="container-fluid px-4">
                                    <h1 class="mt-4">Manage Orders</h1>
                                    <ol class="breadcrumb mb-4">
                                        <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                        <li class="breadcrumb-item active">Order Details</li>
                                    </ol>
                                    <div class="container mt-5">
                                        <div class="row">
                                            <div class="col-12 mx-auto">
                                                <div class="d-flex justify-content-between">
                                                    <h3>Details of Order ID = ${order.orderId}</h3>
                                                </div>
                                                <hr />
                                                <div class="card">
                                                    <div class="card-header">Order Information</div>
                                                    <ul class="list-group list-group-flush">
                                                        <li class="list-group-item">Order ID: ${order.orderId}</li>
                                                        <li class="list-group-item">User ID: ${order.user.userId}</li>
                                                        <li class="list-group-item">User Name: ${order.user.name}</li>
                                                        <li class="list-group-item">Order Date:
                                                            <fmt:formatDate value="${order.orderDate}"
                                                                pattern="yyyy-MM-dd HH:mm:ss" />
                                                        </li>
                                                        <li class="list-group-item">Status: ${order.status}</li>
                                                        <li class="list-group-item">Total Amount:
                                                            <fmt:formatNumber value="${order.totalAmount}"
                                                                type="number" /> VNĐ
                                                        </li>
                                                        <li class="list-group-item">Payment Method:
                                                            ${order.paymentMethod != null ? order.paymentMethod : 'N/A'}
                                                        </li>
                                                        <li class="list-group-item">Payment ID:
                                                            ${order.payment.paymentId}</li>
                                                    </ul>
                                                </div>
                                                <h3 class="mt-4">Enrolled Courses</h3>
                                                <div class="table-responsive">
                                                    <table class="table table-striped mt-3">
                                                        <thead>
                                                            <tr>
                                                                <th>Course ID</th>
                                                                <th>Name</th>
                                                                <th>Description</th>
                                                                <th>Author</th>
                                                                <th>Hours</th>
                                                                <th>Price</th>
                                                                <th>Star Rating</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach var="orderDetail" items="${orderDetails}">
                                                                <tr>
                                                                    <td>${orderDetail.course.courseId}</td>
                                                                    <td>${orderDetail.course.name}</td>
                                                                    <td>${orderDetail.course.description}</td>
                                                                    <td>${orderDetail.course.author}</td>
                                                                    <td>${orderDetail.course.hour}</td>
                                                                    <td>
                                                                        <fmt:formatNumber
                                                                            value="${orderDetail.course.price}"
                                                                            type="number" />
                                                                    </td>
                                                                    <td>${orderDetail.course.star}</td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                        <a href="/admin/order" class="btn btn-success mt-3">Back</a>
                                    </div>
                                </div>
                            </main>
                            <jsp:include page="../layout/footer.jsp" />
                        </div>
                    </div>
                </body>

                </html>