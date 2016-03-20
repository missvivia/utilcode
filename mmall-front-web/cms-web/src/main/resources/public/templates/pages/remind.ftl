<#assign pageName="index-remind"/>

<#escape x as x?html>
<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/wrap/common.ftl" />
    <meta charset="UTF-8">
    <#include "/wrap/css.ftl">
    <style>
        .form-group .control-label {text-align:left;margin-bottom:0;padding-top:7px;}

    </style>
</head>
<body>
    <@side />
    <#-- top and footer need match -->
    <@wrap>
        <@crumbs parent={"txt":"首页","url":'#'} sub={"txt":"提醒设置"}/>
        <#-- card -->
        <div class="row">
            <div class="col-sm-12">
                <div class="m-card">
                    <h2 class="card_b">
                        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
                        手机/邮箱绑定
                    </h2>

                    <div class="card_c">

                        <#-- info -->
                        <div class="form-group f-cb">
                            <label class="col-sm-10 control-label"><span id="username"></span>，你好！</label>
                            <div class="col-sm-10">
                            <#if phone==''>
                                <p class="form-control-static">你还未绑定手机，如未收到任务提醒请修改绑定信息或联系客服~</p>
                            <#else>
                                <p class="form-control-static">你已绑定手机和邮箱，如未收到任务提醒请修改绑定信息或联系客服~</p>
                            </#if>
                            </div>
                        </div>

                        <#-- phone -->
                        <h3>提醒手机</h3>

                        <#-- 有绑定手机 -->
                        <div class="form-group f-cb <#if phone==''>f-dn<#else>f-db</#if>" id="showPhone">
                            <label class="col-sm-3 control-label" id="phone">手机号：${phone}</label>
                            <a class="f-fl btn btn-primary" href="#" id="changePhone">修改</a>
                        </div>

                        <#-- 无绑定手机/修改绑定手机 -->
                        <form method="POST" class="form-group f-cb <#if phone==''>f-db<#else>f-dn</#if>" id="savePhone">
                            <label class="col-sm-1 control-label">手机号：</label>
                            <div class="col-sm-2">
                                <input type="hidden" value="" name="credential" class="ztag"/>
                                <input type="hidden" value="" name="expiredTime" class="ztag"/>
                                <input type="hidden" value="" name="sign" class="ztag"/>

                                <input type="text" class="form-control ztag" placeholder="请输入手机号" maxlength="11" name="newphone"/>
                            </div>
                            <div class="col-sm-1">
                                <a href="#" class="btn btn-default ztag" role="button">获取验证码</a>
                            </div>
                            <div class="col-sm-2">
                                <input type="text" class="form-control ztag" placeholder="请输入验证码" name="yzm"/>
                            </div>
                            <div class="col-sm-1">
                                <button class="btn btn-primary btn-block ztag" type="button">保存</button>
                            </div>
                            <div class="col-sm-1">
                           		<button class="btn btn-primary btn-block" id="cancelPhone" type="button">取消</button>
                           	</div>
                            <div class="col-sm-3">
                                <div class="m-info ztag"></div>
                            </div>
                        </form>

                        <#-- 分割线 -->
                        <div class="hr-dashed"></div>

                        <#-- email -->
                        <h3>提醒邮箱</h3>

                        <form method="POST" class="form-group f-cb" id="saveEmail">
                            <label class="col-sm-1 control-label">邮箱：</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control ztag" placeholder=${email} name="email"/>
                            </div>
                            <div class="col-sm-1">
                                <button class="btn btn-primary btn-block" type="button" id="changeEmail">修改</button>
                            </div>
                            <div class="col-sm-3">
                                <div class="m-info ztag"></div>
                            </div>
                        </form>


                    </div>
                </div>
            </div>
        </div>
    </@wrap>

<#-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/remind.js?v=1.0.0.0"></script>
</body>
</html>
</#escape>