
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="./js/jquery.js"></script>
<script type="text/javascript">
        $(function ()
        {
            $("#btn").click(function ()
            {
                var model = [];
                model.push($("#username").val(), $("#password").val());

                $.ajax({
                    url: './user/login',
                    data: JSON.stringify(model),//传给服务器的数据(即后台AddUsers()方法的参数,参数类型要一致才可以)
                    type: 'POST',
                    contentType: 'application/json;charset=utf-8',//数据类型必须有
                   // async: true,//异步
                    success: function (data) //成功后的回调方法
                    {
                        alert(data)//弹出框                     
                    }
                });
         });
            });
  
</script>
</head>
<body>
	<form action="./user/login"  method="post">
		用户：<input id="username" type="text" name="username">
		密码：<input id="password" type="password" name="password">
		<input type="submit" value="登录">
	</form>
	<input id="btn" type="button" value="提交">
</body>
</html>