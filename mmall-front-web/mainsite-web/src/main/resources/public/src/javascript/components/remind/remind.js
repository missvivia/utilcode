/**
 *
 * 推送消息实现文件
 * @author cheng-lin(cheng-lin@corp.netease.com)
 *
 */
NEJ.define([
    'text!./remind.html',
    'pro/extend/util',
    'pro/extend/config',
    'util/effect/api',
    'base/element',
    'base/util',
    'pro/components/countdown/countdown'
],function(_html,_,config,_t,_e,_u,_countdown,_p,_o,_f,_r){
    var Remind = Regular.extend({
        template: _html,
        config: function(data){
          _.extend(data, {
            content: '',
            prop:['top','left'],
            start:[0,0],
            end:[100,100],
            delay:0,
            duration: 1,
            parent:document.body,
            timecnt:'{{dd}}天{{HH}}小时{{mm}}分钟{{ss}}秒'
          })
        },

        init: function(){
            this.$inject(this.data.parent);
            this.__refp = this.$refs.parent;
            if (!!this.data.time){
                var _node = _e._$getByClassName(this.__refp,'j-remind-time')[0];
                // 有倒计时的
                this.__ct = new _countdown({
                    data:{
                        content:this.data.timecnt,
                        time:this.data.time,
                        onchange:function(_opt){
                            if (_opt.isdown){
                                this.onclose();
                            }
                        }._$bind(this)
                    }
                });
                this.__ct.$inject(_node);
            }

            _u._$forEach(this.data.prop,function(_prop,_index){
                _e._$setStyle(this.__refp,_prop,this.data.start[_index] + 'px');
            }._$bind(this));
            setTimeout(this.onshow._$bind(this),100);
            this.$on('close', this.data.onclose||_f);
        },

        compareSE:function(){
            var _equal = null;
            _u._$forEach(this.data.start,function(_item,_index){
                if (_item == this.data.end[_index]){
                    _equal = _index
                }
            }._$bind(this));
            return _equal;
        },

        onshow:function(){
            _t._$stopEffect(this.__refp);
            var _equal = this.compareSE();
            if (_equal!=null){
                var _animp = (_equal == 0) ? this.data.prop[1] : this.data.prop[0];
                var _to = (_equal == 0) ? this.data.end[1] : this.data.end[0];
                _t._$slide(this.__refp,_animp+':+'+_to+'px',{
                    timing:'ease-out',
                    delay:this.data.delay,
                    duration:this.data.duration
                })
            }else{
                _t._$moveTo(this.__refp,this.getprop(1),{
                    timing:'ease-in-out',
                    delay:this.data.delay,
                    duration:[this.data.duration,this.data.duration]
                });
            }
        },

        getprop:function(_flag){
            var _opt = {},_map;
            if (_flag){
                _map = this.data.end;
            }else{
                _map = this.data.start;
            }
            _u._$forEach(this.data.prop,function(_prop,_index){
                _opt[_prop] = _map[_index];
            }._$bind(this))
            return _opt;
        },

        onclose:function(event){
            event.stopPropgation();
            if (!!this.__ct){
                this.__ct.destroy();
            }
            _t._$stopEffect(this.__refp);
            var _equal = this.compareSE();
            if (_equal!=null){
                var _animp = (_equal == 0) ? this.data.prop[1] : this.data.prop[0];
                var _to = (_equal == 0) ? this.data.start[1] : this.data.start[0];
                _t._$slide(this.__refp,_animp+':+'+_to+'px',{
                    timing:'ease-out',
                    delay:this.data.delay,
                    duration:this.data.duration
                })
            }else{
                _t._$moveTo(this.__refp,this.getprop(),{
                    timing:'ease-in-out',
                    delay:this.data.delay,
                    duration:[this.data.duration,this.data.duration]
                });
            }
            this.$emit('close');
        },

        destroy:function(){
            _t._$stopEffect(this.__refp);
            this.supr();
        }

    });

    return Remind;
});