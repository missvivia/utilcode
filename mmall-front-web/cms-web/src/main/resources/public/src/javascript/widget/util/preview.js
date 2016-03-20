/**
 * 预览封装（临时）
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  '{lib}base/element.js',
  'pro/extend/config',
  'pro/extend/request',
  'pro/components/notify/notify'
  ],
  function(e, config, request, notify){

  var form = e._$html2node(
  '<form action="'+config.MAINSITE+'/preview" ref=pform target="_blank" method="post" enctype="application/x-www-form-urlencoded">'+
    '<input type="hidden" name="product">' +
  '</form>'
  )
  // 标识是否被注入到页面中
  var injected = false;

  return {
    preview: function(_data){
      if(!injected) document.body.appendChild(form);
      injected = true;
      request("/detail",{
        sync: true,
        data: _data,
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
