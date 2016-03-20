兼容性： 
唤醒App支持：
手机自带浏览器 Android(webkit内核), Iphone(Safari)及Chrome,Opera,Firefox,360,百度,QQ,UC浏览器;
页面浏览支持：
Wphone(IE 10+),微信,微博,易信内置浏览器

JS结构：
NEJ依赖管理 +regualr页面注入+wap工具函数

插件：
swiper：自定义滚动条+滑动封装+轮播图

样式结构：
混合布局,屏幕自适应   响应范围  320~640

图片请求裁剪规格：
商品详情等大图 width:640 height:自定义    
例如 XXX?imageView&thumbnail=640x0
列表等2列布局 width:320 height:自定义
例如 XXX?imageView&thumbnail=320x0

代码规范：
缩进：4个空格或一个tab
注释规范：参考jquery注释规范

测试环境下 预览

web view 页面:
1.商品详情： http://023.baiwandian.cn/m/product?id=xxx (测试id：51772)
2.快递查询： http://023.baiwandian.cn/m/logistics?expressCompany=xxx&expressNO=xxx (请保证浏览器中有token)
3.帮助中心： http://023.baiwandian.cn/m/help (测试环境 常见问题可测)
4.用户协议： http://023.baiwandian.cn/m/agreement
5.尺码助手： http://023.baiwandian.cn/m/sizetable?productId=xxx (测试id：51772)

分享页面：
1.单品页： http://023.baiwandian.cn/m/share/product?id=xxx (测试id：51772 页面操作：鼠标拖动浏览或手机触屏滑动)
2.PO列表：http://023.baiwandian.cn/m/share/po/page?scheduleId=xxx&limit=20&lastId=0 (测试id: 96781 预览请更改地区设定 http://023.baiwandian.cn/province/s?p=浙江)
3.红包：http://023.baiwandian.cn/m/share/red/apply?id=xxx (id未定 先可为任意值)
4.登录：http://023.baiwandian.cn/m/login
5.主页：http://023.baiwandian.cn/m/share/home

