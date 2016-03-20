/*
 * --------------------------------------------
 * 全局配置参数
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */
define([
    '{lib}base/global.js'], function() {

  var config = {};

  // 默认上传地址
 // config.UPLOAD_URL = "http://10.242.233.55:8098/filemgr/upload";
  
  config.UPLOAD_URL = 'http://upload.paopao.163.com/filemgr/upload';
  // if(DEBUG){
	 //  config.MAINSITE = 'http://023.baiwandian.cn';
	 //  config.BACKEND = 'http://sj.baiwandian.cn';
	 //  config.DOMAIN_URL = 'http://back.baiwandian.cn';
  // } else{
	  config.DOMAIN_URL = 'http://back.baiwandian.cn';
	  config.BACKEND = 'http://sj.baiwandian.cn';
	  config.MAINSITE = 'http://023.baiwandian.cn';
  // }
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
    '@163.com',
    '@126.com',
    '@yeah.net',
    '@vip.163.com',
    '@vip.126.com',
    '@popo.163.com',
    '@188.com',
    '@qq.com',
    '@yahoo.com',
    '@sina.com'
  ];
  

  return config;

  
})