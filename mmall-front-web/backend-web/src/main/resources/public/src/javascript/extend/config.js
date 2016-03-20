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
  //config.UPLOAD_URL = "http://10.165.124.8:8181/filemgr/upload";
  
  config.UPLOAD_URL = 'http://upload.paopao.163.com/filemgr/upload';
  // if(DEBUG){
	 //  config.MAINSITE = 'http://miss.163.com:8010';
	 //  config.BACKEND = 'http://back.miss.163.com:8020';
	 //  config.DOMAIN_URL = 'http://miss.163.com:8020';
  // } else{
	  config.DOMAIN_URL = 'http://sj.baiwandian.cn';
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


  config.FORM_CODE = {
    "-1":  "请输入必填项",
    "-2":  "类型不匹配",
    "-3":  "错误的输入类型",
    "-4":  "超过最大长度限制",
    "-5":  "未达到最小长度",
    "-6":  "未达到给定范围的最小值",
    "-7":  "超出给定范围的最大值"
  }
  
 

  config.helpColors = [
    "#fc8d8d", "#89d4fa", "#9ce593", "#ffc483",
    "#ffa6f0", "#8bf6f9", "#fff98c", "#b6a8fb",
    "#89a1fd"
    ]

  config.resource = {
    HELP: "/rest/helpers"
  }



  return config;

  
})