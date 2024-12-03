<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <meta name="description" content="Dự án HaUI Elearning" />
                <meta name="author" content="HaHuy" />
                <title>Create User</title>
                <link href="/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
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
                        padding: 36px;
                        border-radius: 12px;
                        box-shadow: 0 0 24px rgba(0, 0, 0, 0.1);
                    }

                    .form-label {
                        font-weight: bold;
                        font-size: 1.2rem;
                    }

                    .form-control,
                    .form-select {
                        border-radius: 0.3rem;
                        font-size: 1.2rem;
                        padding: 12px;
                    }

                    .btn-primary {
                        background-color: #007bff;
                        border: none;
                        font-size: 1.2rem;
                        padding: 12px;
                    }

                    .btn-primary:hover {
                        background-color: #0056b3;
                    }

                    .breadcrumb-item a {
                        color: #007bff;
                        text-decoration: none;
                        font-size: 1.2rem;
                    }

                    .breadcrumb-item a:hover {
                        text-decoration: underline;
                    }
                </style>
                <script>
                    $(document).ready(function () {
                        $("form").on("submit", function (event) {
                            let isValid = true;
                            $(".form-control").each(function () {
                                if ($(this).val() === "") {
                                    isValid = false;
                                    $(this).addClass("is-invalid");
                                } else {
                                    $(this).removeClass("is-invalid");
                                }
                            });
                            if (!isValid) {
                                event.preventDefault();
                            }
                        });
                    });
                </script>
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
                                <div class="row mt-5">
                                    <div class="col-md-8 col-lg-6 mx-auto">
                                        <div class="form-container">
                                            <h3 class="mb-4">Create a User</h3>

                                            <!-- Alert Section -->
                                            <c:if test="${not empty error}">
                                                <div class="alert alert-danger" role="alert">
                                                    ${error}
                                                </div>
                                            </c:if>

                                            <form:form method="post" enctype="multipart/form-data"
                                                action="/admin/user/create" modelAttribute="newUser" class="row g-3">
                                                <div class="col-md-6">
                                                    <label class="form-label">Username:</label>
                                                    <form:input type="text" class="form-control" path="username" />
                                                    <div class="invalid-feedback">Username is required.</div>
                                                </div>
                                                <div class="col-md-6">
                                                    <label class="form-label">Email:</label>
                                                    <form:input type="email" class="form-control" path="email" />
                                                    <div class="invalid-feedback">Valid email is required.</div>
                                                </div>
                                                <div class="col-md-6">
                                                    <label class="form-label">Password:</label>
                                                    <form:input type="password" class="form-control" path="password" />
                                                    <div class="invalid-feedback">Password is required.</div>
                                                </div>
                                                <div class="col-md-6">
                                                    <label class="form-label">Full Name:</label>
                                                    <form:input type="text" class="form-control" path="name" />
                                                    <div class="invalid-feedback">Full name is required.</div>
                                                </div>
                                                <div class="col-md-6">
                                                    <label class="form-label">Introduce:</label>
                                                    <form:input type="text" class="form-control" path="introduce" />
                                                    <div class="invalid-feedback">Introduction is required.</div>
                                                </div>
                                                <div class="col-md-6">
                                                    <label class="form-label">Role:</label>
                                                    <form:select class="form-select" path="role.name">
                                                        <form:option value="ADMIN">ADMIN</form:option>
                                                        <form:option value="USER">USER</form:option>
                                                    </form:select>
                                                </div>
                                                <div class="col-12">
                                                    <button type="submit" class="btn btn-primary w-100">Create</button>
                                                </div>
                                            </form:form>
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