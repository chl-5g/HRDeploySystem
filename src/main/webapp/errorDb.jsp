<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>数据库连接异常</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>css/default.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>css/private.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.1/css/materialize.min.css">
    <style type="text/css">
        html, body { height: 100%; }
        html { display: table; margin: auto; }
        body {
            display: table-cell;
            vertical-align: middle;
            background: url("<%=basePath%>images/hello/ban3.jpg") top center no-repeat;
            background-size: cover;
        }
    </style>
    <link rel="shortcut icon" href="<%=basePath%>images/hello/logo.png">
</head>
<body>
<div id="login-page" class="row">
    <div class="col s12 z-depth-6 card-panel">
        <div class="row">
            <div class="input-field col s12 center">
                <img src="<%=basePath%>images/hello/logo.png" alt="" class="responsive-img valign profile-image-login">
                <h5 class="center login-form-text">数据库暂不可用，请稍后重试</h5>
                <p class="center">系统已快速返回错误，避免页面长时间卡住。</p>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <a href="<%=basePath%>login.jsp" class="btn waves-effect waves-light col s12">返回登录页</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
