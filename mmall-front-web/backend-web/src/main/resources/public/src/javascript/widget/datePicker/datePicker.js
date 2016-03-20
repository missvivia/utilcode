/**
 * 渲染日历控件
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define(['base/util'
        ,'base/event'
        ,'base/element'
        ,'util/event'
        ,'ui/datepick/datepick'],
    function(ut,v,e,t,dp,p) {
        var _seed_css = e._$pushCSSText('\
                .m-card{overflow:visible;}\
                .m-datePicker{z-index:1;}\
                .m-datePicker .zact{background:#68449e;color:#fff;}\
                .m-datePicker .zcard{border: 1px solid #ddd;}\
        ');
        e._$dumpCSSText()
        var pro;
        p._$$datePickerModule = NEJ.C();
        pro = p._$$datePickerModule._$extend(t._$$EventTarget);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
        };
        pro.__reset = function(_options) {
            this.__supReset(_options);
            var _dateCon = e._$getByClassName(e._$get(_options.pcon), 'datePicker');
            for(var i=0;i<_dateCon.length;i++){
                v._$addEvent(_dateCon[i],'click',onInputClick._$bind(this,_dateCon[i]));
            }
        };

        function onInputClick(_node,_event){
            v._$stop(_event);//因为日历控件是卡片，在document上接收到click 事件都会回调到卡片，所以阻止掉事件
            var _datepick = dp._$$DatePick._$allocate({
                        parent: _node.parentNode,
                        clazz: 'm-datePicker',
                        onchange: onChange._$bind(this,_node)
                    }); 
        }

        //选中时回调，把值设到input中
        function onChange(_node,_value){
            _node.value = ut._$format(_value,'yyyy-MM-dd');
        }
        
        return p;
        
    });