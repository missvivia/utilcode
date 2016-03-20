/*
 * --------------------------------------------
 * 项目内工具函数集合，此页面尽量写注释
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * --------------------------------------------
 */
define([
    '{lib}util/query/query.js'
    ], function(e) {

  var data = {
    attrs: [
      {type: "text", required: "true", name: "place", "default": "hello", "label" :"产地", "description": "默认产地" },
      {type: "textarea", required: "true", name: "attatch", "label" : "配件备注", "description": "默认" },
      {type: "select", name: "season", "default": "hello", "label": "季节", "description": "默认" , 
        options: [
          { value: "1" , label: "春节"},
          { value: "2" , label: "秋季"}
        ]
      }
    ]
  };



  return data;

  
})