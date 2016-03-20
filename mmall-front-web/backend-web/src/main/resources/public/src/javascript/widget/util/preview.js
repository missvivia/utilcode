/**
 * 预览封装（临时）
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  '{lib}base/element.js',
  'pro/extend/util',
  'pro/extend/config',
  'pro/extend/request',
  'pro/components/notify/notify'
  ],
  function(e, _ ,config, request, notify){

  var form = e._$html2node(
  '<form action="'+config.MAINSITE+'/preview" ref=pform target="_blank" method="post" enctype="application/x-www-form-urlencoded">'+
    '<input type="hidden" name="product">' +
  '</form>'
  )
  // 标识是否被注入到页面中
  var injected = false;

  return {
    preview: function(id, options){
      options = options || {};
      options.url = options.url || "/detail";
      if(!injected) document.body.appendChild(form);
      injected = true;
      request(options.url,{
        sync: true,
        data: _.extend({id: id}, options.data||{}),
        onload: function(json){
          if(json.code === 200){
            form.product.value = JSON.stringify(json.result);
            form.submit();
          }
        },
        onerror: function(){
          notify.notify({
            "type": "error",
            "message": "尝试预览失败，请稍后再试"
          })
        }
      } )
    }
  }

})
