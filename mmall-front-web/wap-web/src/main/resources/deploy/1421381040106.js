D:/youhua/mmall/mmall-parent/mmall-front-web/wap-web/src/main/resources/public/src/javascript/lib/swiper/swiper.scrollbar.js
I$(279,"{{!商品详情页 详情参数加载!}} <div class=\"g-part-param\"> \t{{!商品参数!}} \t<div class=\"g-part-name\"> \t\t商品参数 \t</div> \t<div class=\"m-param\"> \t\t<span class=\"key\">商品名称</span>{{product.productName}}&emsp; \t</div> \t<div class=\"m-param\"> \t\t<span class=\"key\">商品货号</span>{{product.goodsNo}}&emsp; \t</div> \t<div class=\"m-param\"> \t\t<span class=\"key\">专柜同款</span>{{#if !!product.sameAsShop && product.sameAsShop==1}}是{{#else}}否{{/if}} \t</div> \t{{!由于UI排版需要放弃list!}} \t{{!长!}} \t{{#if !!product.length}} \t<div class=\"m-param\"> \t\t<span class=\"key\">长</span>{{product.length}}cm&emsp; \t</div> \t{{/if}} \t{{!宽!}} \t{{#if !!product.width}} \t<div class=\"m-param\"> \t\t<span class=\"key\">宽</span>{{product.width}}cm&emsp; \t</div> \t{{/if}} \t{{!高!}} \t{{#if !!product.height}} \t<div class=\"m-param\"> \t\t<span class=\"key\">高</span>{{product.height}}cm&emsp; \t</div> \t{{/if}} \t{{!风格!}} \t{{#if !!product.height}} \t<div class=\"m-param\"> \t\t<span class=\"key\">风格</span>{{product.height}}&emsp; \t</div> \t{{/if}} \t{{!产地!}} \t{{#if !!product.producing}} \t<div class=\"m-param\"> \t\t<span class=\"key\">产地</span>{{product.producing}}&emsp; \t</div> \t{{/if}} \t{{!重量!}} \t{{#if !!product.weight}} \t<div class=\"m-param\"> \t\t<span class=\"key\">重量</span>{{product.weight}}&emsp; \t</div> \t{{/if}} \t{{!商家编辑属性!}} \t{{#list product.productDetail as param}} \t{{#if param.name.length > 4}} \t<div>{{param.name}}：{{param.value}}&emsp;</div> \t{{#else}} \t<div class=\"m-param\"> \t\t<span class=\"key\">{{param.name}}</span>{{param.value}}&emsp; \t</div> \t{{/if}} \t{{/list}} \t{{!配件!}} \t{{#if !!product.accessory}} \t<div class=\"m-param\"> \t\t<span class=\"key\">配件</span>{{product.accessory}}&emsp; \t</div> \t{{/if}} \t{{!洗涤说明!}} \t{{#if !!product.careLabel}} \t<div class=\"m-param\"> \t\t<span class=\"key\">洗涤说明</span>{{product.careLabel}}&emsp; \t</div> \t{{/if}} \t{{!售后说明!}} \t{{#if !!product.afterMarket}} \t<div class=\"m-param\"> \t\t<span class=\"key\">售后说明</span>{{product.afterMarket}}&emsp; \t</div> \t{{/if}} \t{{!商品特点!}} \t{{#if !!product.productDescp}} \t<div class=\"m-param\"> \t\t<span class=\"key\">商品特点</span>{{product.productDescp}}&emsp; \t</div> \t{{/if}} </div> {{!尺码助手!}} {{#if !!product.productSize}} <div class=\"g-part-size\"> \t<div class=\"g-part-name\"> \t\t商品尺码 \t</div> \t{{!尺码对照表!}} \t<div class=\"m-table-name\">尺码对照表</div> \t<table class=\"g-size-table\"> \t\t{{!表头!}} \t\t<tr> \t\t\t{{#list product.productSize.header as hd}} \t\t\t<th>{{hd.name}}{{hd.unit || ''}}</th> \t\t\t{{/list}} \t\t</tr> \t\t{{!内容!}} \t\t{{#list product.productSize.body as bd}} \t\t<tr> \t\t\t{{#list bd as item}} \t\t\t<td>{{item}}</td> \t\t\t{{/list}} \t\t</tr> \t\t{{!温馨提示!}} \t\t{{#if (bd_index + 1) == bd_length}} \t\t<tr> \t\t\t<td colspan={{bd_length}} class=\"m-size-tip\">  \t\t\t{{#if !!product.productSize.tip}}  \t\t\t\t<span>温馨提示：</span>{{product.productSize.tip}} \t\t\t{{/if}}  \t\t\t</td> \t\t</tr> \t\t{{/if}} \t\t{{/list}} \t</table> \t{{!尺码助手!}} \t{{#if !!product.helper}}         <div class=\"m-table-name\">         \t尺码助手         </div>         <table class=\"g-helper-table\"> \t\t{{!表头!}} \t\t<tr> \t\t\t{{#list product.helper.vaxis.list as hd}} \t\t\t{{#if hd_index == 0}} \t\t\t<th class=\"m-helper-head\"> \t\t\t\t<div class=\"m-helper-corner\"></div> \t\t\t\t<span class=\"m-helper-info1\">身高cm</span> \t\t\t\t<span class=\"m-helper-info2\">体重kg</span> \t\t\t</th> \t\t\t{{/if}} \t\t\t<th>{{hd}}</th> \t\t\t{{/list}} \t\t</tr> \t\t{{#list product.helper.body as bd}} \t\t{{#if bd != 'empty'}} \t\t<tr> \t\t\t<td>{{product.helper.haxis.list[bd_index]}}</td> \t\t\t{{#list bd as item}} \t\t\t<td>{{item}}</td> \t\t\t{{/list}} \t\t</tr> \t\t{{/if}} \t\t{{/list}} \t</table>     {{/if}} </div> {{/if}} {{!详情图片!}} <div class=\"g-part-img\"> \t<div class=\"g-part-name\"> \t\t商品图片 \t</div> \t<div class=\"g-img-wrap\"> \t\t{{#list product.prodShowPicList as pic}}             <img src=\"{{pic}}?imageView&quality=100&thumbnail=640y640\" /> \t\t{{/list}} \t\t \t\t \t</div> </div> <div class=\"g-part-rmd\"> \t<div class=\"g-part-name\"> \t\t商品咨询 \t</div> \t<dl> \t\t购买前如有问题，请向mmall客服咨询。 \t\t<a href=\"tel:4008666163\">联系客服</a> \t</dl> \t<dl> \t\t<dt>商品都是正品吗？</dt> \t\t<dd>请你放心，mmall（）上所售卖的商品均经过品牌授权，确保正品，并由中华保险（CIC）为你购买的每一件商品进行承保。</dd> \t</dl> \t<dl> \t\t<dt>衣服图片上搭配的腰带、项链等配件，会连商品一同送货吗？</dt> \t\t<dd>您可参看商品详情提供的商品参数信息。如非在配件信息内特别说明，服装类商品图片中的腰带、饰品等配件均为拍摄搭配之用，是不包含在所售商品中的。</dd> \t</dl> \t<dl> \t\t<dt>尺码表上的尺码标准吗？</dt> \t\t<dd>mmall所售商品尺寸均为人工测量，可能会存在1-2cm的正常误差范围。</dd> \t</dl> \t<dl> \t\t<dt>图片颜色和实物颜色是否相同？</dt> \t\t<dd>mmall展示的商品图片是由mmall专业拍摄或采用品牌供应商提供的统一图片，力求将最真实的信息传达至你的视线。但由于个人显示器不同，可能导致实物与图片存在色差，最终颜色以实物为准。</dd> \t</dl> \t<dl> \t\t<dt>为什么我收到的商品包装和图片显示的不一致？</dt> \t\t<dd>由于部分商品生产批次不一，你收到货品的包装有可能与网站上图片不完全一致，但mmall保证所售商品均为正品，商品包装请以实物为准。</dd> \t</dl> \t<dl> \t\t<dt>网站上显示的参考价和实体店的售价一致吗？</dt> \t\t<dd>mmall的参考价采集自品牌官网标价、专柜标价或由品牌供应商提供。由于地区或时间的差异性，可能与你购物时的参考价不一致。mmall标注的参考价仅供你参考，不作为购物依据。</dd> \t</dl> \t<dl> \t\t<dt>如何退货？</dt> \t\t<dd>你签收商品之日起的7天之内，商品未经穿着不影响二次销售的情况下，mmall为你提供七天无理由放心退服务。</dd> \t</dl> </div>");
I$(277,function (tpl, p, o, f, r) {
//商品详情参数page
var detailPage = Regular.extend({
template : tpl
});
// change to NEJ module
return detailPage;
},279,77);
I$(278,function (Swiper, detailPage, timer, app, _wap, p, o, f, r) {
var productManager = {
// 页面初始化
_$init : function() {
/**************************************************
* 页面参数初始化
****************************************************/
this.showpage = 1; // 标记当前页码
this.hasShow = false; // 标记详情页是否初始化
this.touchTip = _wap._$('.m-touch-tip'); // 翻页提示区域
this.backTopButton = _wap._$('.backTop'); // 回顶按钮
this.loadingTip = _wap._$('.g-detail-loading'); // 页面加载提示
/**************************************************
* 唤醒App初始化
****************************************************/
app.IphoneOpenUrl = 'mmalliphone://page=2?itemId=' + PRODUCTDETAIL.product.productId; // 唤醒iPhone客户端对应详情页
app.AndroidOpenUrl = 'mmallandroid://page=2?itemId=' + PRODUCTDETAIL.product.productId; // 唤醒Android客户端对应详情页
/**************************************************
* 滑动组件初始化
****************************************************/
// page1 滑动封装
this.pageSlide1 = new Swiper('.sp1', { // 滑动容器
wrapperClass : "showpage1-wrapper", // 滑动模块
slideClass : "showpage1-slide", // 滑动内容
scrollContainer : true, // 开启内部滚动
mode : 'vertical', // 设定方向：竖直
scrollbar : {
container : '.showpage1-scrollbar' // 自定义滚动条容器
},
// Swiper 新增方法 touchEnd callback
scrollCallback : function(_this) {
// 到底 切换详情页
if (_this.positions.current < -_this.positions.maxPosition) {
this.showpage = 2;
this.changePage();
}
}._$bind(this)
});
// page2滑动封装
this.pageSlide2 = new Swiper('.sp2', { // 滑动容器
wrapperClass : "showpage2-wrapper", // 滑动模块
slideClass : "showpage2-slide", // 滑动内容
scrollContainer : true, // 开启内部滚动
mode : 'vertical', // 设定方向：竖直
scrollbar : {
container : '.showpage2-scrollbar' // 自定义滚动条容器
},
// Swiper 新增方法 touchEnd callback
scrollCallback : function(_this) {
// 到顶 切换详情页
if (_this.positions.current > 0) {
this.showpage = 1;
_wap._$setTransform(this.slide.node1, this.slide.data_transform2, 300);
_wap._$setTransform(this.slide.node2, this.slide.data_transform2);
}
// 回到顶部 按钮显示
if (_this.positions.end < -100) {
this.backTopButton.style.display = 'block';
} else {
this.backTopButton.style.display = 'none';
}
}._$bind(this)
});
// page1 轮播图
this.imgSwiper = new Swiper('.imgshow-container', {// 轮播容器
wrapperClass : "imgshow-wrapper", // 轮播模块
slideClass : "imgshow-slide", // 轮播内容
pagination : '.imgshow-pagination', // 数量提示
loop : true // 设置循环
});
// 开启倒计时
if (!!PRODUCTDETAIL.end) {
timer._$start(_wap._$('#m-tab-timer'), PRODUCTDETAIL.end);
}
},
// 页面渲染
_$draw : function() {
/**************************************************
* 页面参数设置
****************************************************/
this.viewH = _wap._$getWindowHeight(); // 主视角高度
this.picH = _wap._$('.g-container').offsetWidth; // 展示图高度(轮播图为正方形,且防止轮播图无效  用g-container获取)
/**************************************************
* 页面变换设置
****************************************************/
this.slide = {
node1 : _wap._$('.sp1'), // 下拉翻页 第一页
node2 : _wap._$('.sp2'), // 下拉翻页 第二页
data_transform1 : 'translate3d(0px, -' + this.viewH + 'px, 0px)', // 第一页顶部移动到-viewH
data_transform2 : 'translate3d(0px, 0px, 0px)' // 第二页顶部移动到0
};
/**************************************************
* 页面元素设置
****************************************************/
_wap._$('.sp2').style.top = this.viewH + 'px';
_wap._$('.g-container').style.height = this.viewH + 'px';
_wap._$('.imgshow-container').style.height = this.picH + 'px';
// 修正safari转屏操作时  显示断层问题
if (this.showpage == 2) {
_wap._$setTransform(this.slide.node1, this.slide.data_transform1);
_wap._$setTransform(this.slide.node2, this.slide.data_transform1);
}
/**************************************************
* 自定义滚动条 渲染更新
****************************************************/
this.imgSwiper.reInit();
this.imgSwiper.resizeFix();
this.pageSlide1.reInit();
this.pageSlide1.resizeFix();
this.pageSlide2.reInit();
this.pageSlide2.resizeFix();
},
// 交互事件
_$interactive : function() {
/**************************************************
* 交互函数
****************************************************/
// 尺码助手数组过滤
if(!!PRODUCTDETAIL.product.helper){
var
orginalArr = PRODUCTDETAIL.product.helper.body, // 标记原二维数组
rowlen = orginalArr.length, // 标记1维长度
collen = orginalArr[0].length; // 标记2维长度
for(var i = 0; i < rowlen; i++){
var flag = 0; // 标记行中空值数量
for(var j = 0; j < collen; j++){
if(orginalArr[i][j] == ""){
flag++;
}
}
// 标记空行
if(flag == collen){
orginalArr[i] = 'empty';
}
}
}
// 加载详情图片
this.showDetail = function() {
var
picList = PRODUCTDETAIL.product.customEditHTML, // 商品图片信息
picinner = document.createElement('div'), // 商家自定义内容占位div
regexp = new RegExp(/src="(.*?)"/g), // 匹配详情图片地址
info = new detailPage({
data : {
product : PRODUCTDETAIL.product
}
}).$inject(".g-detail-container"); // 参数注入
// 详情图片注入
if (!!picList) {
// 防止商家编辑部分纯文本  加载提示无法隐藏问题
if (picList.match(regexp) == null) {
// 加载提示隐藏
this.loadingTip.style.display = 'none';
}
var imgsrc; // 标记匹配出的图片地址
while (( imgsrc = regexp.exec(picList)) != null) {
var detailImg = new Image(); // 加载商家编辑图片
// 更新裁剪图片
detailImg.src = imgsrc[1] + '?imageView&thumbnail=640x0&quality=100';
// 裁剪后图片加载
detailImg.onload = function() {
// 自定义滚动条长度更新
this.pageSlide2.reInit();
// 加载提示隐藏
this.loadingTip.style.display = 'none';
}._$bind(this)
}
// 商家自定义部分注入
picList = picList.replace(regexp, 'src="$1?imageView&thumbnail=640x0&quality=100"');
picinner.innerHTML = picList;
_wap._$('.g-img-wrap').appendChild(picinner);
// 注入后滚动条长度更新 （防止商家自定义时出现大段文字介绍时  滚动长度不够的情况）
this.pageSlide2.reInit()
} else {
// 商家未编辑详情
this.loadingTip.style.display = 'none';
this.pageSlide2.reInit()
}
// 标记详情页已初始化
this.hasShow = true;
}._$bind(this)
// 查看详情页
this.changePage = function() {
// 判断是否初始化加载详情页
if (!this.hasShow) {
this.showDetail();
}
// 翻页
_wap._$setTransform(this.slide.node1, this.slide.data_transform1);
_wap._$setTransform(this.slide.node2, this.slide.data_transform1, 300);
}._$bind(this)
//回顶操作
this.backTop = function() {
this.backTopButton.style.display = 'none'; // 隐藏回顶按钮
this.pageSlide2.setWrapperTransition(300); // Swiper 内部函数 设定 动画时间
this.pageSlide2.setWrapperTranslate(0, 0, 0); // Swiper 内部函数 设定  translate3d属性
this.pageSlide2.allowMomentumBounce = false; // Swiper 新增属性   关闭回弹标记    防止惯性到底回弹过程中 用户点击回顶按钮  失效bug
//回顶缓动
setTimeout(function() {
// 页面标记更新
this.showpage = 1;
// 翻页
_wap._$setTransform(this.slide.node1, this.slide.data_transform2, 300);
_wap._$setTransform(this.slide.node2, this.slide.data_transform2);
// 第一页回顶缓动
this.pageSlide1.setWrapperTransition(300); // Swiper 内部函数 设定动画时间
this.pageSlide1.setWrapperTranslate(0, 0, 0);// Swiper 内部函数 设定  translate3d属性
this.pageSlide1.allowMomentumBounce = false; // Swiper 新增属性   关闭回弹标记    防止惯性到底回弹过程中 用户点击回顶按钮  失效bug
}._$bind(this), 300);
}._$bind(this)
/**************************************************
* 事件绑定
****************************************************/
_wap._$addEventListener(this.touchTip, 'click', this.changePage); // 点击 显示详情页 跳转
_wap._$addEventListener(this.backTopButton, 'click', this.backTop); // 点击 回顶按钮 回顶
window.onresize = _wap._$throttle(this._$draw._$bind(this), 100); // 检测屏幕变化渲染页面  (函数节流  delay 100ms)
}
}
// dom ready 加载 渲染页面
_wap._$domReady(function() {
productManager._$init();
productManager._$draw();
productManager._$interactive();
})
},276,277,273,269,263);