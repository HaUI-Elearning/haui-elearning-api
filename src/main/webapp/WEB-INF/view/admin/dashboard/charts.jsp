<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
                <html lang="en">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <meta name="description" content="" />
                    <meta name="author" content="" />
                    <title>Charts - SB Admin</title>
                    <link href="/css/styles.css" rel="stylesheet" />
                    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
                        crossorigin="anonymous"></script>
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
                                <div class="container my-5">
                                    <h1 class="text-center mb-4">Dashboard</h1>

                                    <!-- Thông tin tổng quan -->
                                    <div class="row mb-4">
                                        <div class="col-md-4">
                                            <div class="info-box">
                                                <h3>Total User Account</h3>
                                                <p id="user-count">${userCount}</p>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="info-box">
                                                <h3>Total Course</h3>
                                                <p id="course-count">${courseCount}</p>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="info-box">
                                                <h3>Total Revenue</h3>
                                                <p id="total-revenue">
                                                    <fmt:formatNumber value="${totalRevenue}" pattern="#,###" /> VND
                                                </p>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Biểu đồ -->
                                    <div class="row">
                                        <div class="col-md-8 chart-container">
                                            <canvas id="revenueChart"></canvas>
                                        </div>
                                        <div class="col-md-4 chart-container">
                                            <canvas id="categoryChart"></canvas>
                                        </div>
                                    </div>
                                    <!-- Thông tin sản phẩm bán chạy nhất -->
                                    <div class="mt-5">
                                        <h3 class="text-center">Sản phẩm bán chạy nhất</h3>
                                        <div class="table-responsive">
                                            <table class="table table-striped table-bordered mt-3">
                                                <thead class="thead-dark">
                                                    <tr>
                                                        <th scope="col">#</th>
                                                        <th scope="col">Tên sản phẩm</th>
                                                        <th scope="col">Thể loại</th>
                                                        <th scope="col">Giá</th>
                                                        <th scope="col">Tác giả</th>
                                                        <th scope="col">Số sản phẩm đã bán</th>
                                                        <th scope="col">Tổng doanh thu</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <!-- Lặp qua danh sách sản phẩm bán chạy nhất -->
                                                    <<c:forEach var="course" items="${topSellingCourses}">
                                                        <tr>
                                                            <th scope="row">${course.courseId}</th>
                                                            <td>${course.courseName}</td>
                                                            <td>${course.categoryName}</td>
                                                            <!-- Định dạng giá -->
                                                            <td>
                                                                <fmt:formatNumber value="${course.price}"
                                                                    type="currency" currencySymbol="₫" />
                                                            </td>
                                                            <!-- Định dạng tác giả -->
                                                            <td>${course.author}</td>
                                                            <!-- Định dạng số lượng bán -->
                                                            <td>
                                                                <fmt:formatNumber value="${course.quantitySold}"
                                                                    type="number" />
                                                            </td>
                                                            <!-- Định dạng doanh thu -->
                                                            <td>
                                                                <fmt:formatNumber value="${course.totalSales}"
                                                                    type="currency" currencySymbol="₫" />
                                                            </td>
                                                        </tr>
                                                        </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>

                                <canvas id="revenueChart"></canvas>
                                <canvas id="categoryChart"></canvas>

                                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                                <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels"></script>

                                <script>
                                    // Biểu đồ doanh thu theo ngày
                                    const revenueCtx = document.getElementById('revenueChart').getContext('2d');
                                    const revenueChart = new Chart(revenueCtx, {
                                        type: 'bar', // loại biểu đồ cột chính
                                        data: {
                                            labels: ['Thứ 2', 'Thứ 3', 'Thứ 4', 'Thứ 5', 'Thứ 6', 'Thứ 7', 'CN'],
                                            datasets: [
                                                {
                                                    type: 'bar', // Dữ liệu dạng cột cho doanh thu
                                                    label: 'Doanh thu (VND)',
                                                    data: [10000000, 20000000, 15000000, 25000000, 22000000, 30000000, 27000000],
                                                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                                    borderColor: 'rgba(75, 192, 192, 1)',
                                                    borderWidth: 1
                                                },
                                                {
                                                    type: 'line', // Thêm đường biểu diễn
                                                    label: 'Đường biểu diễn doanh thu',
                                                    data: [10000000, 20000000, 15000000, 25000000, 22000000, 30000000, 27000000],
                                                    borderColor: 'rgba(255, 99, 132, 1)',
                                                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                                                    fill: false,
                                                    tension: 0.4, // Độ cong của đường biểu diễn (0.4 để đường cong nhẹ)
                                                    borderWidth: 2
                                                }
                                            ]
                                        },
                                        options: {
                                            responsive: true,
                                            scales: {
                                                y: {
                                                    beginAtZero: true,
                                                    title: {
                                                        display: true,
                                                        text: 'Doanh thu (VND)',
                                                        font: {
                                                            size: 14
                                                        }
                                                    }
                                                }
                                            },
                                            plugins: {
                                                legend: {
                                                    position: 'top'
                                                }
                                            }
                                        }
                                    });

                                    // Biểu đồ phần trăm khóa học theo thể loại
                                    // Lấy dữ liệu JSON từ controller
                                    const categoryStatsJson = '${categoryStatsJson}';

                                    // Chuyển đổi chuỗi JSON thành đối tượng JavaScript
                                    const categoryStats = JSON.parse(categoryStatsJson);

                                    // Chuyển đổi Map từ Java thành mảng JavaScript
                                    const labels = Object.keys(categoryStats);  // Các thể loại khóa học
                                    const values = Object.values(categoryStats);  // Số lượng khóa học trong mỗi thể loại

                                    // Tính tổng số khóa học
                                    const totalCourses = values.reduce((sum, value) => sum + value, 0);

                                    // Biểu đồ phần trăm khóa học theo thể loại
                                    const categoryCtx = document.getElementById('categoryChart').getContext('2d');
                                    const categoryChart = new Chart(categoryCtx, {
                                        type: 'pie',
                                        data: {
                                            labels: labels,  // Các thể loại khóa học
                                            datasets: [{
                                                data: values,  // Số lượng khóa học trong mỗi thể loại
                                                backgroundColor: [
                                                    'rgba(255, 99, 132, 0.2)',
                                                    'rgba(54, 162, 235, 0.2)',
                                                    'rgba(255, 206, 86, 0.2)',
                                                    'rgba(75, 192, 192, 0.2)',
                                                    'rgba(153, 102, 255, 0.2)'
                                                ],
                                                borderColor: [
                                                    'rgba(255, 99, 132, 1)',
                                                    'rgba(54, 162, 235, 1)',
                                                    'rgba(255, 206, 86, 1)',
                                                    'rgba(75, 192, 192, 1)',
                                                    'rgba(153, 102, 255, 1)'
                                                ],
                                                borderWidth: 1
                                            }]
                                        },
                                        options: {
                                            responsive: true,
                                            plugins: {
                                                legend: {
                                                    position: 'top',
                                                },
                                                datalabels: {
                                                    formatter: (value, context) => {
                                                        let sum = 0;
                                                        let dataArr = context.chart.data.datasets[0].data;
                                                        dataArr.forEach(data => {
                                                            sum += data;
                                                        });
                                                        let percentage = (value * 100 / sum).toFixed(1) + "%";
                                                        return percentage;
                                                    },
                                                    color: 'black',
                                                    font: {
                                                        weight: 'bold'
                                                    }
                                                }
                                            }
                                        },
                                        plugins: [ChartDataLabels]
                                    });
                                </script>
                                <script
                                    src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                                    crossorigin="anonymous"></script>
                                <script src="/js/scripts.js"></script>
                                <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"
                                    crossorigin="anonymous"></script>
                                <script src="/js/chart-area-demo.js"></script>
                                <script src="/js/chart-bar-demo.js"></script>
                                <script src="/js/chart-pie-demo.js"></script>
                </body>

                </html>