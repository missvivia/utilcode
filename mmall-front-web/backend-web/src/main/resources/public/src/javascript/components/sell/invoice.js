/**
 * 发票编辑页
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "{lib}base/util.js",
  "pro/extend/util",
  "text!./invoice.html",
  "pro/components/notify/notify",
  "pro/widget/BaseComponent",
  "pro/components/sell/intable",
  '{lib}util/file/select.js',
  'util/ajax/xdr',
  "pro/components/datepicker/datepicker"
  ], function(u, _, tpl, notify ,BaseComponent, intable , s, j){
  var InvoiceTable = BaseComponent.extend({
    name: "invoice",
    template: tpl,
    config: function(data){
      _.extend(data, {
        stateInt: 0,
        // 最近30天
        sdate: +new Date - 1000 * 60 * 60 * 24 * 30 
      })
    },
    init: function(){
      this.intable = this.$refs.intable;
      this.$on("upload_done", function(){
        location.reload();
      });
    },
    search: function(exports, $event){
      var formdata = this.gatherData();
      if(formdata){
        //导出
        if(exports){
          _.extend(formdata, {
            isExport: true
          })
          var query = u._$object2query(formdata);
          $event.target.href= "/sell/invoiceSearch?" + query;
        }else{
          this.intable.search(formdata);
        }
         
      }
    },
    gatherData: function(){
      var data = this.data;
      var qvalue = (data.queryValue||"").trim();
      var error;

//      if((data.sdate >= data.edate)){
//        error = "结束时间必须大于开始时间"
//      }
      if(data.queryType != "0" && !qvalue){
        error = "查询条件不能为空"
      }
      if(error){
        notify.notify({
          type: "error",
          message: error
        })
        return;
      } 
      return {
        sdate: data.sdate,
        edate: data.edate,        
        queryType: data.queryType,
        queryValue: data.queryValue||"",
        stateInt: data.stateInt
      }
    }

  }).directive("label-upload", function(elem, url){
      if(!url) return;
      s._$bind(elem, {
          parent: elem.parentNode,
          name: 'myfile',
          multiple: false,
          onchange: onUpload
      });

    var self = this;

    function onUpload(_event){
      var form = _event.form;
      form.action =  url;
      j._$upload(form,{
        onload:function(result){
          if(result.code==200){
            form.reset();
            //notify.show('操作成功');
            notify.notify("批量导入成功")
            self.$emit("upload_done")
          } else{
            form.reset();
            notify.notify({
              type: "error",
              message: "导入错误，请确认导入格式"
            });
          }
        }._$bind(this),
        onerror:function(){
          notify.notify({
            type: "error",
            message: '请求错误!'
          });
        }
      })
    }
  })
  return InvoiceTable;
})