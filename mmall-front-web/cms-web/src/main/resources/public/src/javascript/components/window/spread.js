/**
 * author hzwuyuedong@corp.netease.com
 */

define([
  "{pro}extend/util.js",
  "{pro}components/window/base.js",
  "{pro}components/datepicker/datepicker.js",
  "{pro}components/upload/imgUpload.js",
  '{pro}components/notify/notify.js',
  '{pro}widget/form.js',
  '{lib}base/util.js',
  '{lib}base/element.js'
], function(_, BaseModal, dp, imgUpload, notify, t, ut, e, p,o,f,r){

  var contentModal = BaseModal.extend({
    url: {
      'checkpo': '/content/checkpo',
      'upsert': '/content/spread/upsert',
      'update': '/content/spread/update'
    },
    watchedAttr: ['spread.scheduleId', 'spread.activityUrl', 'spread.imgUrl', 'spread.imgUrl2',  'spread.startTime', 'spread.endTime', 'spread.promotionType', 'poInvalidate'],
    rule: ['Require', 'Url', 'Url','Ignore', 'Require', 'Require', 'Require'],
    config: function(data){
      _.extend(data, {
        form: {}
      });
      if(data.spread.id == null){
        var nowDate = new Date(),
          date = new Date(nowDate.getFullYear(), nowDate.getMonth(), nowDate.getDate());
        _.extend(data, {
          spread: {
            startTime: +date,
            endTime: +date,
            promotionType: 0
          }
        }, true);
      }
      if(data.spread.scheduleId === 0){
        data.spread.scheduleId = '';
      }
      for(var i= 0, l=this.watchedAttr.length;i<l; i++){
        var name = this.watchedAttr[i].split('.')[1];
        if(!!name){
          data.form[name] = {};
        }
      }
      this.$watch(this.watchedAttr, function(){
        this.check(arguments);
      });
    },
    
    checkPo: function(arg){
      var data = this.data;
      this.$request(this.url['checkpo'], {
        data: {'poId': arg, 'areaId':data.provinceId},
        progress: true,
        onload: function(json){
          data.poInvalidate = false;
        }._$bind(this),
        onerror: function(json){
          data.poInvalidate = true;
          this.$update();
          notify.showError(json.result.msg || '无效的PO编号');
        }._$bind(this)
      })
    },

    //重写confirm
    confirm: function(){
      var data = this.data,
        spread = data.spread, type;
      if(!data.form.$inValidate){
        var webForm = t._$$WebForm._$allocate({
          form: e._$get('webform')
        });
        if(spread.id == null){
          //spread.sequence = data.account + 1;
          type = 'upsert';
        }else{
          type = 'update';
        }

        this.$request(this.url[type], {
          data: ut._$object2query(webForm._$data()),
          norest:true,
          method:'POST',
          type:'JSON',
          onload: function (json) {
            this.$emit('confirm', json.result);
            this.destroy();
          }._$bind(this),
          onerror: function (json) {
            notify.showError('操作推广位失败！');
          }
        });
      }
    },

    //上传图片
    handleUpload: function($event, type){
      // 根据upload传递的数据来判断处理逻辑
      switch($event.type){
        case "preview":
          this.$update(function(data){
            data.percent = 0.1;
            if ( $event.data[0]){
              data.preview = $event.data[0].imgUrl;
            };
          })
          break;
        case "upload":
          this.$update(function(data){
            data.spread[type] = $event.data[0].imgUrl;
            data.preview = null;
            data.percent = 1;
          })
          break;
        case "error":
          notify.showError('上传图片失败！');
          break;
      }
      console.log($event)
    },

    check: function(args){
      var fun = {
        isUrl: function(value){
          return /^(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?$/.test(value);
        },
        isRequire: function(value){
          if(value == null) return false;
          return (''+value).replace(/(^\s*)|(\s*$)/g,"");
        },
        isIgnore: function(value){
          return true;
        }
      };
      var i = 0, l = args.length,
        data = this.data,
        form = data.form,
        spread = data.spread;
      var name = null;
      for(i; i<l; i++){
        name = this.watchedAttr[i].split('.')[1];
        if(!!name){
          form[name].$inValidate = !fun['is' + this.rule[i]](args[i]);
        }
      }

      //结束日期<开始日期
      if(spread.endTime < spread.startTime){
        form['endTime'].$inValidate = true;
      }
      if(form['imgUrl'].$inValidate || form['startTime'].$inValidate || form['endTime'].$inValidate ||
        (spread.promotionType == 0)&&(form['scheduleId'].$inValidate || data.poInvalidate) ||  (spread.promotionType == 1)&&form['activityUrl'].$inValidate){
        form.$inValidate = true;
      }else{
        form.$inValidate = false;
      }
      return form.$inValidate;
    }
  });

  return contentModal;
});