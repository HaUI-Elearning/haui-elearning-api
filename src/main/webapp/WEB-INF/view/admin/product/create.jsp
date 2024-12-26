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
                <title>Create Product</title>
                <link href="/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <style>
                    .form-container {
                        background-color: #f8f9fa;
                        padding: 36px;
                        border-radius: 12px;
                        box-shadow: 0 0 24px rgba(0, 0, 0, 0.1);
                    }

                    .form-label {
                        font-weight: bold;
                        font-size: 1.1rem;
                    }

                    .form-control,
                    .form-select {
                        border-radius: 0.3rem;
                        font-size: 1rem;
                        padding: 8px;
                    }

                    .btn-primary {
                        background-color: #007bff;
                        border: none;
                        font-size: 1rem;
                        padding: 10px;
                    }

                    .btn-primary:hover {
                        background-color: #0056b3;
                    }

                    .invalid-feedback {
                        display: none;
                    }

                    .form-control.is-invalid+.invalid-feedback {
                        display: block;
                    }

                    .preview-img {
                        max-width: 100%;
                        height: auto;
                        margin-top: 15px;
                        border-radius: 8px;
                    }
                </style>
                <script>
                    $(document).ready(function () {
                        $("form").on("submit", function (event) {
                            let isValid = true;
                            $(".form-control").each(function () {
                                if ($(this).val() === "" && $(this).attr('type') !== 'file') {
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

                        $("#imageFile").change(function () {
                            const file = this.files[0];
                            if (file) {
                                const reader = new FileReader();
                                reader.onload = function (e) {
                                    $("#preview").attr("src", e.target.result).show();
                                }
                                reader.readAsDataURL(file);
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
                                    <li class="breadcrumb-item active">Product</li>
                                </ol>
                            </div>
                            <div class="row mt-5">
                                <div class="col-md-8 col-lg-6 mx-auto">
                                    <div class="form-container">
                                        <h3 class="mb-4">Create a Product</h3>
                                        <form:form method="post" enctype="multipart/form-data"
                                            action="/admin/product/create" modelAttribute="newProduct" class="row g-3">
                                            <div class="col-md-6">
                                                <label class="form-label">Course Name:</label>
                                                <form:input type="text" class="form-control" path="name" />
                                                <div class="invalid-feedback">Course name is required.</div>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="form-label">Author:</label>
                                                <form:input type="text" class="form-control" path="author" />
                                                <div class="invalid-feedback">Author is required.</div>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="form-label">Description:</label>
                                                <form:textarea class="form-control" path="description" />
                                                <div class="invalid-feedback">Description is required.</div>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="form-label">Content:</label>
                                                <form:textarea class="form-control" path="contents" />
                                                <div class="invalid-feedback">Content is required.</div>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="form-label">Hours:</label>
                                                <form:input type="text" class="form-control" path="hour" />
                                                <div class="invalid-feedback">Hours are required.</div>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="form-label">Price:</label>
                                                <form:input type="text" class="form-control" path="price" />
                                                <div class="invalid-feedback">Price is required.</div>
                                            </div>
                                            <div class="col-md-12">
                                                <label class="form-label">Upload Image:</label>
                                                <input type="file" name="imageFile" id="imageFile" class="form-control"
                                                    accept="image/*" />
                                                <div class="invalid-feedback">Image is required.</div>
                                                <img id="preview" class="preview-img" src="#" alt="Image preview"
                                                    style="display: none; width: 400; height: 300;" />
                                            </div>
                                            <div class="col-12">
                                                <button type="submit" class="btn btn-primary w-100">Create
                                                    Course</button>
                                            </div>
                                        </form:form>
                                    </div>
                                </div>
                            </div>
                        </main>
                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
            </body>

            </html>