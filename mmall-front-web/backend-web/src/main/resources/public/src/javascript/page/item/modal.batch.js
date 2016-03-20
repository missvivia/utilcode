/*
 * --------------------------------------------
 * 弹窗组件
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */
define([
  "{pro}components/modal/modal.js",
  "{pro}components/notify/notify.js",
  '{pro}extend/util.js',
  'text!./modal.batch.html'
  ], function(Modal, notify ,_, tpl){


  return Modal.extend({
    // 默认属性
    // 计算属性
    content: tpl,
    data:{
      width: 720
    },
    events: {
      confirm: function(ev){
        var data = this.data;
        if(!data.header && !data.tailer && !data.replace){
          notify.notify({
            "type":"error",
            "message": "请至少填写一个头部添加、尾部添加或批量替换"
          })
        }else{
          this.$emit("batch", data);
          this.destroy();
        }

      }
    },
    init: function(){
      this.supr();
      this.$on({
        "close": this.destroy.bind(this), 
      })
    }
  });

})