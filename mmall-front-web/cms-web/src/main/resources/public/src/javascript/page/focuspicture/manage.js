/*
 * ------------------------------------------
 * 焦点图管理页
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */

NEJ.define([
  'base/util',
  'pro/extend/util',
  'pro/widget/BaseComponent',
  'pro/components/notify/notify',
  'pro/components/window/base',
  'pro/page/focuspicture/edit',
  'text!./manage.html',
  'text!./edit.html',
  'base/element'
], function(_u,_,BaseComponent,notify,win0,win1,_html0,_html1,_e){

    var format = function(_time){
        if (!_time) return '';
        _time = parseInt(_time);
        return _u._$format(new Date(_time),'yyyy-MM-dd HH:mm:ss');
    };
    return BaseComponent.extend({
      url:'/focuspicture/list',
      baseurl:'/focuspicture/',
      template: _html0,
      config: function(data){
        _.extend(data, {
          list:[]
        });
      },
      init: function(){
        // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
        //this.$on('updatelist', this.getList.bind(this));
        // this.getList();
      },

      online: function(index,id,num){
        this.$request('/focuspicture/online',{
          data:{'id':id,'action':num},
          onload:function(json){
            if (json.code == 200){
              this.data.list[index].online = num;
            }
          }._$bind(this),
          onerror:function(err){

          }
        })
      },

      // 添加焦点图
      edit: function(meta,index){
        var _o = _u._$merge({},meta);
        var opt = {
          data:{
            'content':_html1,
            'clazz':'bg-model',
            'title': !meta ? '添加焦点图' : '编辑焦点图',
            'spread': _o
          }
        }
        var modal = new win1(opt);
        modal.$on('confirm',function(meta,spread){
          var action = !meta ? 'add' : 'update';
          if (action == 'update'){
            spread.id = meta.id;
          }else{
            // 添加需要传当前有几个
            spread.sequence = this.data.list.length + 1;
          }
          spread.provinceId = parseInt(_e._$get('city').value);
          spread.promotionType = parseInt(spread.promotionType);
          this.$request(this.baseurl + action, {
            data: spread,
            method:'POST',
            onload: function(json){
              if (json.code == 200){
                var _index = index!=undefined ? index : spread.sequence-1
                this.data.list[_index] = action == 'update' ? spread : json.result;
              }else{
                notify.showError('操作失败');
              }
            }._$bind(this),
            // test
            onerror: function(_event){
              notify.showError('操作失败');
            }
          });
        }._$bind(this,meta));
        modal.$inject('body');
      },

      delete:function(index){
        var modal = new win0({
          data:{
            'content': '是否删除焦点图',
            'title': '删除焦点图'
          }
        });

        modal.$on('confirm', function(){
          //确认删除
          this.$request(this.baseurl + 'delete', {
            data: {id:this.data.list[index].id},
            onload: function(json){
              var result = json.result;
              if (json.code == 200){
                this.data.list.splice(index, 1);
                this.$update();
              }
            }._$bind(this),
            // test
            onerror: function(json){
              notify.showError('删除失败');
            }
          });
        }._$bind(this));
        modal.$inject('body');
      },

      cbmove: function(index,num){
          var tmp = this.data.list[index],
              length = this.data.list.length-1,
              newIndex = index + num;
          this.data.list.splice(index,1);
          this.data.list.splice(newIndex,0,tmp);
      },

      move: function(index,num,x){
          this.$request(this.baseurl + 'order', {
            data: {id:x.id,provinceId:x.provinceId,move:num,searchTime:this.__saveQtime||0},
            onload: function(json){
              var result = json.result;
              if (json.code == 200){
                this.cbmove(index,num);
              }
            }._$bind(this),
            // test
            onerror: function(json){
              notify.showError('移动失败');
            }
          });
      },

      //获取数据
      getList: function(cityId,qtime,event){
        this.__saveQtime = qtime;
        var data = this.data;
        console.log(data);
        this.$request(this.url, {
          data: {'provinceId':cityId,'searchTime':qtime},
          onload: function(json){
            if (json.code == 200){
              data.list = json.result.list;
            }
          }._$bind(this),
          // test
          onerror: function(json){
            // @TODO: remove
          }
        });
      }
  }).filter('format',format);
});