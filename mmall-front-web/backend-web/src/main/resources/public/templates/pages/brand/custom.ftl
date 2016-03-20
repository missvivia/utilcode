<#escape x as x?html>
<@compress single_line=true>
<!DOCTYPE html>
<html>
<head>
<#include "../../wrap/common.ftl">
<meta charset="utf-8"/>
<title>品购页</title>
<#include "../../wrap/css.ftl">
<!-- @STYLE -->
<link rel="stylesheet" href="/src/css/page/decorate.css"/>
</head>
<body id="index-netease-com">
<#include "../../wrap/top.ftl">
<div class="g-bd f-cb m-decorate" id="body">
	<div class="m-savact" id="saveact">
		<input type="button" value="保存" class="m-save z-flag f-fr btn btn-primary"/>
		<div class="f-fr m-bgset"><label class="lbl f-fl">背景色设置:</label><div class="z-flag  f-fl"></div></div>
		<input type="button" class="z-flag f-fr btn btn-primary" value="添加倒计时" id="countdown"/>
	</div>
	<div class="m-imgchs j-flag j-min">
		<a class="f-fr act j-flag">隐藏</a>
		<ul class="nav nav-tabs m-tab j-flag" role="tablist">
		   <li class="active"><a href="#">图片空间</a></li>
		   <li><a href="#">商品管理</a></li>
		 </ul>
		<div class="j-flag imgs">
			<label class="j-flag">上传图片</label>
			<div>
				<select class="j-flag"><option>默认分类</option><option>女装</option></select>
				<select class="j-flag"><option>选择尺寸</option></select>
				<div></div>
				<ul class="m-imgs j-flag">
					<li>
						<img src="/res/images/cut.jpg" data-height="4381" data-width="1440"/>
						<div>1440*4381</div>
					</li>
					<li>
						<img src="/res/images/cut1.jpg" data-height="2264" data-width="1440"/>
						<div>1440*2264</div>
					</li>
				</ul>
			</div>
		</div>
		<div class="j-flag f-dn goods">
			<ul class="m-imgs d-flag">
				<!--<li>
					<img src="/res/images/cut.jpg" data-height="4381" data-width="1440"/>
					<div>1440*4381</div>
				</li>-->
			</ul>
			<div class="pager d-flag"></div>
		</div>
	</div>
    <div class="j-flag m-cut cutbg">
    </div>
</div>
<#noparse>
<div style="display:none" id="jst-template">
	<textarea name="ntp" id="ntp-product-item">
		<li>
			<img class="j-flag" src="/res/images/cut.jpg" data-height="4381" data-width="1440"/>
			<div class="j-flag">1440*4381</div>
		</li>
	</textarea>
	<textarea name="txt" id="txt-countdown">
		<div><span class="coundown">剩余 xx天 xx时 xx分</span><input class="m-shortbtn btn btn-primary j-editcountdown" value="编辑"><input class="m-shortbtn btn btn-primary j-drag" value="拖动"></div>
	</textarea>
</div>
</#noparse>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/brand/custom.js"></script>
</body>
</html>
</@compress>
</#escape>