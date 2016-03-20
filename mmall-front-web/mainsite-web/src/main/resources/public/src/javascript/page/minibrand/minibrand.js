/**
 * 边栏我的品牌和我的关注实现页面
 * @author cheng-lin(cheng-lin@corp.netease.com)
 *
 */
/* pro/page/minibrand/minibrand */
NEJ.define([
    'base/element',
    'base/util',
    'pro/extend/util',
    'pro/widget/BaseComponent',
    'text!./minibrand.html',
    'pro/components/countdown/countdown'
  ],
  function(_e,_u,_, BaseComponent, tpl,countdown, p, o, f, r, pro) {
    var brandModule = BaseComponent.extend({
      urlMap: {
        'brand': '/brand/favlist',
        'sales': '/schedule/favlist'
      },
      template: tpl,
      config: function (data) {
        _.extend(data, {
          "salesmap":{
            "1":"sales",
            "2":"sales sales2"
          },
          "overIndex": -1,
          "firstLoad" : true,
          "active" : "brand",
          "result" : [],
          "countdownList" : [],
          "schedulesize":'256x145',
          "schedulesize2":'140x60',
          "brandsize":'139x60'
        });
      },
      init: function (data) {
        this.fetch('brand');
        this.$on('updatewgt', this.fetch._$bind(this,'brand'));
      },

      onmouseover:function(_index){
        this.data.overIndex = _index;
      },

      change:function(_type,_flag){
        if (!_flag && _type == this.data.active){
          return;
        }
        this.data.active = _type;
        this.fetch(_type);
      },

      //根据内部componet返回的数据刷新
      refresh: function(result){
        var data = this.data;
        data.result = result;
        // mock scheduleId
        var _index = 0;
        _u._$forEach(data.result,function(x){
          x.scheduleId =_index++;
        });
        this.$update();
      },

      //获取数据
      fetch: function(_type){
        var data = this.data;
        // 清理倒计时
        if (!!this.data.countdownList){
          _u._$forEach(this.data.countdownList,function(_cd){
            _cd.destroy();
          }._$bind(this))
        }
        this.$request(this.urlMap[_type], {
          data:{limit:8,offset:0},
          onload: function(json){
            if (json.code == 200){
              data.firstLoad = false;
              var _list = json.result.list;
              this.refresh(_list);
              if (_type == 'sales'){
                _u._$forEach(_list,function(_item){
                  var _node = _e._$get('time-' + _item.scheduleId);
                  if (!!_node){
                    _cd = new countdown({
                        data:{
                          content:'{{dd}}天{{HH}}时{{mm}}分{{ss}}秒',
                          time:_item.endTime-new Date().getTime(),
                          onchange:function(_opt){
                            if (_opt.isdown){
                              this.fetch('sales');
                            }
                          }._$bind(this)
                        }
                      })
                    _cd.$inject(_node);
                    this.data.countdownList.push(_cd);
                  }
                }._$bind(this));
              }
            }
          }._$bind(this),
          onerror: function(json){
            this.data.result = [];
            // @TODO: remove
          }._$bind(this)
        });
      },

      close: function(){
        this.$emit('close');
      }

    });
    return brandModule;
  });