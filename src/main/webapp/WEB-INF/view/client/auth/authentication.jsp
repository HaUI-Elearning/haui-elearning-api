<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>HaUI Elearning</title>
                <link rel="stylesheet" href="/css/style_authentication.css">
            </head>

            <body>
                <div id="container" class="container">
                    <!-- FORM SECTION -->
                    <div class="row">
                        <!-- SIGN UP -->
                        <div class="col align-items-center flex-col sign-up">
                            <div class="form-wrapper align-items-center">
                                <form:form method="post" action="/register" modelAttribute="registerUser">
                                    <div class="form sign-up">
                                        <div class="input-group">
                                            <i class='bx bxs-user'></i>
                                            <form:input type="text" class="form-control" path="username"
                                                placeholder="Username" />
                                            <form:errors path="username" cssClass="error" />
                                        </div>
                                        <div class="input-group">
                                            <i class='bx bxs-user'></i>
                                            <form:input type="text" id="name" class="form-control" path="name"
                                                placeholder="Fullname" />
                                            <form:errors path="name" cssClass="error" />
                                        </div>
                                        <div class="input-group">
                                            <i class='bx bx-mail-send'></i>
                                            <form:input type="email" class="form-control" path="email"
                                                placeholder="Email" />
                                            <form:errors path="email" cssClass="error" />
                                        </div>
                                        <div class="input-group">
                                            <i class='bx bxs-lock-alt'></i>
                                            <form:input type="password" class="form-control" path="password"
                                                placeholder="Password" />
                                            <form:errors path="password" cssClass="error" />
                                        </div>
                                        <div class="input-group">
                                            <i class='bx bxs-lock-alt'></i>
                                            <form:input type="password" class="form-control" path="confirmPassword"
                                                placeholder="Confirm password" />
                                            <form:errors path="confirmPassword" cssClass="error" />
                                        </div>
                                        <button>
                                            Sign up
                                        </button>
                                        <p>
                                            <span>
                                                Already have an account?
                                            </span>
                                            <a href="\login" class="pointer">
                                                Sign in here
                                            </a>
                                        </p>
                                    </div>
                                </form:form>
                            </div>

                        </div>
                        <!-- END SIGN UP -->
                        <!-- SIGN IN -->
                        <div class="col align-items-center flex-col sign-in">
                            <div class="form-wrapper align-items-center">
                                <form method="post" action="/login">
                                    <div class="form sign-in">
                                        <c:if test="${param.error != null}">
                                            <div class="my-2" style="color: red;">Invalid email or password.</div>
                                        </c:if>
                                        <div class="input-group">
                                            <i class='bx bxs-user'></i>
                                            <input type="text" placeholder="Username" name="username" />
                                        </div>
                                        <div class="input-group">
                                            <i class='bx bxs-lock-alt'></i>
                                            <input type="password" placeholder="Password" name="password" />
                                        </div>
                                        <div>
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        </div>
                                        <button>
                                            Sign in
                                        </button>
                                        <p>
                                            <b>
                                                Forgot password?
                                            </b>
                                        </p>
                                        <p>
                                            <span>
                                                Don't have an account?
                                            </span>
                                            <b onclick="toggle()" class="pointer">
                                                Sign up here
                                            </b>
                                        </p>
                                    </div>
                                </form>
                            </div>
                            <div class="form-wrapper">

                            </div>
                        </div>
                        <!-- END SIGN IN -->
                    </div>
                    <!-- END FORM SECTION -->
                    <!-- CONTENT SECTION -->
                    <div class="row content-row">
                        <!-- SIGN IN CONTENT -->
                        <div class="col align-items-center flex-col">
                            <div class="text sign-in">
                                <h2>
                                    Welcome
                                </h2>

                            </div>
                            <div class="img sign-in">

                            </div>
                        </div>
                        <!-- END SIGN IN CONTENT -->
                        <!-- SIGN UP CONTENT -->
                        <div class="col align-items-center flex-col">
                            <div class="img sign-up">

                            </div>
                            <div class="text sign-up">
                                <h2>
                                    Join with us
                                </h2>

                            </div>
                        </div>
                        <!-- END SIGN UP CONTENT -->
                    </div>
                    <!-- END CONTENT SECTION -->
                </div>
                <script src="/js/authentication.js"></script>
            </body>

            </html>