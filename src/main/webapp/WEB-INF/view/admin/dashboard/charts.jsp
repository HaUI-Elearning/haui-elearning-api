<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>Charts - SB Admin</title>
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
    </style>
</head>

<body class="sb-nav-fixed">
    <jsp:include page="../layout/header.jsp" />
    <div id="layoutSidenav">
        <jsp:include page="../layout/sidebar.jsp" />
        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Charts</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                    </ol>
                    <div class="card mb-4">
                        <div class="card-body">
                            Chart.js is a third party plugin that is used to generate the charts in this template. The
                            charts below have been customized - for further customization options, please visit the
                            official
                            <a target="_blank" href="https://www.chartjs.org/docs/latest/">Chart.js documentation</a>
                            .
                        </div>
                    </div>
                    <div class="card mb-4">
                        <div class="card-header">
                            <i class="fas fa-chart-area me-1"></i>
                            Area Chart Example
                        </div>
                        <div class="card-body"><canvas id="myAreaChart" width="100%" height="30"></canvas></div>
                        <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
                    </div>
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="card mb-4">
                                <div class="card-header">
                                    <i class="fas fa-chart-bar me-1"></i>
                                    Bar Chart Example
                                </div>
                                <div class="card-body"><canvas id="myBarChart" width="100%" height="50"></canvas></div>
                                <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="card mb-4">
                                <div class="card-header">
                                    <i class="fas fa-chart-pie me-1"></i>
                                    Pie Chart Example
                                </div>
                                <div class="card-body"><canvas id="myPieChart" width="100%" height="50"></canvas></div>
                                <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js" crossorigin="anonymous"></script>
    <script src="/js/chart-area-demo.js"></script>
    <script src="/js/chart-bar-demo.js"></script>
    <script src="/js/chart-pie-demo.js"></script>
</body>

</html>