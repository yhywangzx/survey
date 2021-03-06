<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!doctype html>
<html lang="zh-CN">
<%@ include file="../../base.jsp" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>test</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="layui/css/layui.css">
    <style>
        form {
            width: 97%;
        }

        select {
            width: 85%;
        }

        option {
            font-size: 16px;
            padding: 3px 5px;
        }
    </style>
</head>

<body oncontextmenu="return false" onselect="return false">
<form class="layui-form" id="adminUserForm">
    <div class="upload-headimg" id="headimg">
        <img src="uploadimage/${adminUser.headimage}" alt="个人头像">
        <button type="button" class="layui-btn" id="uploadheadimg">
            <i class="layui-icon">&#xe67c;</i>上传头像
        </button>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span style="color: #f00;">*</span>账号：</label>
        <div class="layui-input-block">
            <input type="text" name="account" lay-verify="required" placeholder="请输入账号" autocomplete="off"
                   class="layui-input" value="${adminUser.account}" disabled>
            <input type="hidden" name="id" value="${adminUser.id}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span style="color: #f00;">*</span>Email：</label>
        <div class="layui-input-block">
            <input type="email" name="email" placeholder="请输入Email" lay-verify="required|email" autocomplete="off"
                   class="layui-input" value="${adminUser.email}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">编号：</label>
        <div class="layui-input-block">
            <input type="text" name="identifier" placeholder="请输入编号" autocomplete="off" class="layui-input"
                   value="${adminUser.identifier}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span style="color: #f00;">*</span>角色：</label>
        <div class="layui-input-block">
            <select name="roles" multiple lay-ignore>
                <c:forEach var="r" items="${roles}">
                    <option value="${r.id}" ${!r.available ? 'disabled':''} ${r.checked ? 'selected':''}>${r.description}</option>
                </c:forEach>
            </select>
        </div>
        <span style=" display:block;float:right;">按住Shift多选</span>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">创建时间：</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" value="${adminUser.createdatetime}" disabled>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">最后一次登录时间：</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" value="${adminUser.lastlogintime}" disabled>
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">备注：</label>
        <div class="layui-input-block">
            <textarea name="remarks" placeholder="请输入内容" class="layui-textarea">${adminUser.remarks}</textarea>
        </div>
    </div>
    <shiro:hasPermission name="admin:update">
        <div class="layui-form-item">
            <div class="layui-input-block">
                <shiro:hasPermission name="admin:update">
                    <button class="layui-btn" lay-submit lay-filter="addAdmin" id="adminUserSubmit">立即修改</button>
                </shiro:hasPermission>
            </div>
        </div>
    </shiro:hasPermission>
</form>
</body>
<script>
    var layer = null;
    layui.use(['form', 'upload', 'layer'], function () {
        var form = layui.form;
        var upload = layui.upload;
        layer = layui.layer;
        //执行实例
        upload.render({
            elem: '#uploadheadimg', //绑定元素
            auto: true,
            url: 'system/updateAdminHeadImage',
            size: 500,
            method: 'post',
            data: {id:${adminUser.id}, image: '${adminUser.headimage}'},
            done: function (res, index, upload) {
                if (res.status == 0) {
                    window.parent.reflashAdminTable();
                    $("#headimg img").attr("src", res.data);
                }
                layer.msg(res.msg);

            }
        });
        form.on('submit(addAdmin)', function (data) {
            var loadIndex = layer.load();
            var fromData = new FormData($("#adminUserForm")[0]);
            $.ajax({
                type: "POST",
                dataType: "json",
                url: 'system/updateAdminUser',
                data: fromData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function (result) {
                    layer.close(loadIndex);
                    if (result.status == 0) {
                        window.parent.reflashAdminTable();
                        // window.parent.layer.closeAll();
                        window.parent.layer.msg(result.msg);
                    }
                    layer.msg(result.msg);
                },
                error: function (data) {
                    layer.close(loadIndex);
                    alert("出现异常！" + JSON.stringify(data));
                }
            });
            return false;
        });
    });
</script>
</html>
