/*
 * ------------------------------------------
 * 消息管理模块
 * @version  1.0
 * @author   xiangwenbin(xiangwenbin@corp.netease.com)
 * ------------------------------------------
 */

NEJ.define([
  'base/util',
  'pro/extend/util',
  'pro/widget/BaseComponent',
  "pro/components/pager/pager",
  'pro/components/notify/notify',
  'pro/components/window/base',
  'pro/page/app/pmessage/edit',
  'pro/page/app/pmessage/window/base',
  'text!./module.html',
  'text!./win.html'
], function(_u,_,BaseComponent,_pager,notify,win0,win1,win2,_html0,_html1){

    return BaseComponent.extend({
      url:'/app/pmessage/list',
      baseurl:'/app/pmessage/',
      template: _html0,
      config: function(data){
        _.extend(data, {
          result: {}
        });
      },
      init: function(){
        // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
        //this.$on('updatelist', this.getList.bind(this));
        // this.getList();
      },
      edit: function(meta,isView){
        var _o = _u._$merge({
        	
        },meta);
        var opt = {
          data:{
        	'provinceList':window["g_provinceList"],
            'os':window["g_os"],
            'content':_html1,
            'clazz':'bg-model',
            'title': !_o.id ? '新建通知' : '编辑消息',
            'message': _o
          }
        };
        
        var modal = isView? new win2(opt): new win1(opt);
       
        modal.$on('confirm',function(_data){
          if(_data.id){
        	  this.$request(this.baseurl + 'update', {
                  data: _data,
                  method:'GET',
                  onload: function(json){
                	  if (json.code == 200){
                		  notify.showError('修改成功');
                      }
                  }._$bind(this),
                  // test
                  onerror: function(_event){
                    notify.showError('修改失败');
                  }
                });
          }else{
        	  this.$request(this.baseurl + 'add', {
                  data: _data,
                  method:'GET',
                  onload: function(json){
                    if (json.code == 200){
                    	 notify.showError('新建成功');
                    }
                  }._$bind(this),
                  // test
                  onerror: function(_event){
                    notify.showError('新建失败');
                  }
                });
          }
          
        }._$bind(this));
        modal.$inject('body');
        if(modal.$bindForm)
        	modal.$bindForm();
      },

      del:function(index){
          var modal = new win0({
            data:{
              'content': '是否删除该条记录',
              'title': '删除记录'
            }
          });

          modal.$on('confirm', function(){
            //确认删除
            this.$request(this.baseurl + 'delete', {
              data: {id:this.data.list[index].id},
              onload: function(json){
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
      onPageChange:function(_page){
    	  this.condition.offset=10*(_page-1);
    	  this.$request(this.url, {
              data: this.condition,
              onload: function(json){
            	  var _data={};
            	  if(json.result){
                      _data.list =json.result.list;
                      _data.total=json.result.total;
                      _data.current=_page;
            	  }else{
//            		  _data.result=null;
            		  _data.list=[];
            	  }
            	  this.data=_data;
              },
              onerror: function(json){
                // @TODO: remove
              }
            });
      },
      //获取数据
      getList: function(_condition){
    	  _condition=_.merge(_condition,{limit:10,offset:0});
    	  this.condition=_condition;
          this.$request(this.url, {
            data: this.condition,
            onload: function(json){
              var _data={};
          	  if(json.result){
                  _data.list = json.result.list;
                  _data.total=json.result.total;
                  _data.current=1;
          	  }else{
          		  _data.result=null;
          		  _data.list=[];
          	  }
          	  this.data=_data;
            },
            onerror: function(json){
              // @TODO: remove
            }
          });
      }
  });
});