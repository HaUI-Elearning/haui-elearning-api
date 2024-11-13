<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Table Courses</title>
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

        .card {
            border: 1px solid #007bff;
            border-radius: 0.25rem;
        }

        .card-header {
            background-color: #007bff;
            color: white;
        }

        .breadcrumb-item a {
            color: #007bff;
            text-decoration: none;
        }

        .breadcrumb-item a:hover {
            text-decoration: underline;
        }

        .btn-success {
            background-color: #28a745;
            border: none;
        }

        .btn-success:hover {
            background-color: #218838;
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
                <h1 class="mt-4">Manage Courses</h1>
                <ol class="breadcrumb mb-4">
                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                    <li class="breadcrumb-item active">Course Details</li>
                </ol>
                <div class="container mt-5">
                    <div class="row">
                        <div class="col-12 mx-auto" item="${course}">
                            <div class="d-flex justify-content-between">
                                <h3>Detail with Course ID = ${course.courseId}</h3>
                            </div>
                            <hr />
                            <div class="card">
                                <div class="card-header">
                                    Course Information
                                </div>
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item">Course ID: ${course.courseId}</li>
                                    <li class="list-group-item">Name: ${course.name}</li>
                                    <li class="list-group-item">Author: ${course.author}</li>
                                    <li class="list-group-item">Description: ${course.description}</li>
                                    <li class="list-group-item">Contents: ${course.contents}</li>
                                    <li class="list-group-item">Created At: ${course.createdAt}</li>
                                    <li class="list-group-item">Hours: ${course.hour}</li>
                                    <li class="list-group-item">Price: ${course.price}</li>
                                    <li class="list-group-item">Star Rating: ${course.star}</li>
                                    <li class="list-group-item">Thumbnail: <img src="${course.thumbnail}" alt="Thumbnail" style="max-width: 100px;" /></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <a href="/admin/product" class="btn btn-success mt-3">Back</a>
                </div>
            </div>
        </main>
        <jsp:include page="../layout/footer.jsp" />
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>

</html>