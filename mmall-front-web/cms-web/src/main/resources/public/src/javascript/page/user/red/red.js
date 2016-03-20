NEJ.define([
    'pro/extend/util',
    'base/util',
    'pro/widget/BaseComponent',
    'text!./red.html'
],function(_,_u,BaseComponent,_html0){
    var format = function(_time){
        if (!_time) return '';
        _time = parseInt(_time);
        return _u._$format(new Date(_time),'yyyy.MM.dd');
    };
    var red = BaseComponent.extend({
        url:'/user/red',
        name: 'm-user-info-red',
        template: _html0,
        config: function(data){
          _.extend(data, {
            currentState:0,//active
            list:[],//list of current state
            result: []
          });
        },
        init: function(){
            this.getList();
        },
        setCurrentState : function(state){
            this.data.currentState = state;
            this.data.list = this.getListByState();
        },
        getListByState: function(){
            var list = this.data.result,
                group = [],
                state = this.data.currentState;
            _u._$forEach(list,function(t){
                if (t.state==state){
                    group.push(t);
                }
            }._$bind(this));
            return group;
        },
        //获取数据
        getList: function(){
          var data = this.data;
          this.$request(this.url, {
            data: {userId:this.data.userid},
            onload: function(json){
                data.result = json.result;
                data.list = this.getListByState();
            },
            // test
            onerror: function(json){
              // @TODO: remove
            }
          });
        }
    }).filter('format',format);
    return red;
});