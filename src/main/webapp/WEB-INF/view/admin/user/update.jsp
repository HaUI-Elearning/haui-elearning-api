<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Update User</title>
                <!-- Latest compiled and minified CSS -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
                <!-- Latest compiled JavaScript -->
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <link href="/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
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

                    .form-container {
                        background-color: #f8f9fa;
                        padding: 30px;
                        border-radius: 10px;
                        box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
                    }

                    .form-label {
                        font-weight: bold;
                    }

                    .form-control {
                        border-radius: 0.25rem;
                    }

                    .btn-warning {
                        background-color: #ffc107;
                        border: none;
                    }

                    .btn-warning:hover {
                        background-color: #e0a800;
                    }

                    .btn-back {
                        background-color: #007bff;
                        color: white;
                        border: none;
                    }

                    .btn-back:hover {
                        background-color: #0056b3;
                    }

                    .breadcrumb-item a {
                        color: #007bff;
                        text-decoration: none;
                    }

                    .breadcrumb-item a:hover {
                        text-decoration: underline;
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
                                <h1 class="mt-4">Manage Users</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Users</li>
                                </ol>
                                <div class="mt-5">
                                    <div class="row">
                                        <div class="col-md-8 col-lg-6 mx-auto">
                                            <div class="form-container">
                                                <h3 class="mb-4">Update a User</h3>
                                                <form:form method="post" action="/admin/user/update"
                                                    modelAttribute="updateUser">
                                                    <div class="mb-3" style="display: none;">
                                                        <label class="form-label">Id:</label>
                                                        <form:input type="text" class="form-control" path="userId" />
                                                    </div>
                                                    <div class="mb-3">
                                                        <label class="form-label">Username:</label>
                                                        <form:input type="text" class="form-control" path="username"
                                                            disabled="true" />
                                                    </div>
                                                    <div class="mb-3">
                                                        <label class="form-label">Email:</label>
                                                        <form:input type="text" class="form-control" path="email"
                                                            disabled="true" />
                                                    </div>
                                                    <div class="mb-3">
                                                        <label class="form-label">Full Name:</label>
                                                        <form:input type="text" class="form-control" path="name" />
                                                    </div>
                                                    <div class="mb-3">
                                                        <label class="form-label">Introduce:</label>
                                                        <form:input type="text" class="form-control" path="introduce" />
                                                    </div>
                                                    <div class="d-flex justify-content-between">
                                                        <button type="submit" class="btn btn-warning">Update</button>
                                                        <a href="/admin/user" class="btn btn-back">Back</a>
                                                    </div>
                                                </form:form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </main>
                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
                <script src="/js/scripts.js"></script>
            </body>

            </html>