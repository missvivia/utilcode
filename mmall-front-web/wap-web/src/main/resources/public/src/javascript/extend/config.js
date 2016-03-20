/*
 * --------------------------------------------
 * 全局配置参数
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */
define([
    '{lib}base/global.js',
    'pro/extend/admaster'], function() {

  var config = {};

  // 默认上传地址
  config.UPLOAD_URL = "/upload";
  // 默认上传参数
  config.UPLOAD_PARAM = {

  }

  // mcss color config
  // $brand-success=         #5cb85c;
  // $brand-info=            #5bc0de;
  // $brand-warning=         #f0ad4e;
  // $brand-danger=          #d9534f;
  config.COLOR_SUCCESS = '#5cb85c';
  config.COLOR_INFO = '#5bc0de';
  config.COLOR_DANGER = '#d9534f';
  config.COLOR_WARNING = '#f0ad4e';

  config.EMAIL_SUFFIX = [
//    '@163.com',
//    '@126.com',
//    '@yeah.net',
//    '@vip.163.com',
//    '@vip.126.com',
//    '@popo.163.com',
//    '@188.com',
//    '@qq.com',
//    '@yahoo.com',
//    '@sina.com'
	'@st.xyl',
	'@op.xyl'
  ];
  if(DEBUG){
	  config.DOMAIN_URL = 'http://m.023.baiwandian.cn';
	  config.MAINSITE = 'http://m.023.baiwandian.cn';
	  config.PUSH_PORT = '6003';
	  config.PUSH_HOST = '123.58.180.233';
	  config.PUSH_PRODUCT_KEY = '7c48f34241f94364b84432a8794e94e7';
    config.IM_DOMAIN_URL = 'http://laike.yixin.im/user/init/2cd06fe49259d7d0?account=';
  } else{
	  config.DOMAIN_URL = 'http://m.023.baiwandian.cn';
	  config.MAINSITE = 'http://m.023.baiwandian.cn';
	  config.PUSH_PORT = '6003';
	  config.PUSH_HOST = '123.58.180.233';
	  config.PUSH_PRODUCT_KEY = '2f21a80299484573807cd4ce58a324a3';
      config.IM_DOMAIN_URL = 'http://laike.yixin.im/user/init/2cd06fe49259d7d0?account=';
  }
  // 尺码助手颜色配置
  config.helpColors = [
    "#fc8d8d", "#89d4fa", "#9ce593", "#ffc483",
    "#ffa6f0", "#8bf6f9", "#fff98c", "#b6a8fb",
    "#89a1fd"
    ]

  //最大加入购物车数量
  config.MAX_PICK = 2;

  config.PAYMETHOD_MAP = {'1':'货到付款', '2': '在线支付'};

  return config;


})