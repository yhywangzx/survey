<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!doctype html>
<html lang="zh-CN">
<%@ include file="../../base.jsp" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>用户编辑</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="layui/css/layui.css">
    <style>
        form {
            width: 97%;
        }
    </style>
</head>

<body oncontextmenu="return false" onselect="return false">
<form class="layui-form" id="userForm"> <!-- 提示：如果你不想用form，你可以换成div等任何一个普通元素 -->
    <input type="hidden" name="id" value="${user.id}">
    <div style="margin-top: 25px;" class="layui-form-item">
        <label class="layui-form-label"><span style="color: #f00;">*</span>学校名称：</label>
        <div class="layui-input-block">
            <input type="text" name="account" lay-verify="required" placeholder="请输入学校名称" autocomplete="off"
                   class="layui-input" value="${user.account}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">重置密码：</label>
        <div class="layui-input-block">
            <input type="text" name="password" placeholder="请输入要修改的密码" autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span style="color: #f00;">*</span>校长：</label>
        <div class="layui-input-block">
            <input type="text" name="headmaster" placeholder="请输入校长名称" lay-verify="required" autocomplete="off"
                   class="layui-input" value="${user.headmaster}">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label"><span style="color: #f00;">*</span>学校地址：</label>
        <div class="layui-input-block">
            <input type="text" name="address" value="${user.address}" lay-verify="required" placeholder="请输入学校地址"
                   autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span style="color: #f00;">*</span>留守儿童之家名称：</label>
        <div class="layui-input-block">
            <input type="text" name="playhousename" value="${user.playhousename}" lay-verify="required"
                   placeholder="请输入留守儿童之家名称" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span style="color: #f00;">*</span>图书数量：</label>
        <div class="layui-input-block">
            <input type="number" name="booknumber" value="${user.booknumber}" lay-verify="required"
                   placeholder="请输入图书数量" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">备注</label>
        <div class="layui-input-block">
            <textarea name="remarks" placeholder="请输入内容" class="layui-textarea">${user.remarks}</textarea>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <shiro:hasPermission name="user:update">
                <button class="layui-btn" lay-submit lay-filter="addUser" id="userSubmit">立即修改</button>
            </shiro:hasPermission>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
    <!-- 更多表单结构排版请移步文档左侧【页面元素-表单】一项阅览 -->
</form>
</body>
<script>
    var layer = null;
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        layer = layui.layer;
        form.on('submit(addUser)', function (data) {
            var loadIndex = layer.load();
            var fromData = new FormData($("#userForm")[0]);
            $.ajax({
                type: "POST",
                dataType: "json",
                url: 'system/editorUser',
                data: fromData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function (result) {
                    layer.close(loadIndex);
                    if (result.status == 0) {
                        window.parent.reflashUserOnSuccessTable();
                    }
                    layer.msg(result.msg);
                },
                error: function (data) {
                    layer.close(loadIndex);
                    alert("出现异常！");
                }
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
    });
</script>
</html>
