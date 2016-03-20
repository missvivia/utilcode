NEJ.define([
    'base/config',
    'base/element',
    'base/util',
    'util/color/color'
],function(_c,_e,_u,_x,_p){
    /**
     * 格式化颜色值
     * @param {String} 颜色值
     */
    _p._$getColor = (function(){
        var _reg1 = /rgb\(([\d]+?),\s*([\d]+?),\s*([\d]+?)\)/i;
        return function(_node,_name){
            var _color = _e._$getStyle(_node,_name);
            if (_reg1.test(_color)){
                return _x._$rgb2color({
                    r:parseInt(RegExp.$1),
                    g:parseInt(RegExp.$2),
                    b:parseInt(RegExp.$3)
                });
            }
        };
    })();
    /**
     * 取透明度
     * @param {Object} _node
     */
    _p._$getOpacity = function(_node){
        var _value = Math.ceil(_e._$getStyle(_node,'opacity')*100);
        return Math.max(0,Math.min(_value,100));
    };
    /**
     * 取数值型值
     * @return {Number}
     */
    _p._$getNumber = function(_node,_name,_default){
        var _value = parseInt(_e._$getStyle(_node,_name));
        return isNaN(_value)?(_default||0):_value;
    };
    /**
     * 根据序列排序列表
     * @param {Object} _list
     * @param {Object} _seq
     */
    _p._$sortListBySeq = function(_list,_seq){
        var _ret = [],
            _xap = {},
            _map = _u._$array2object(
                _list,function(_item){
                    return _item.id;
                }
            );
        // sort by seq
        _u._$forEach(
            (_seq||'').split(','),function(_id){
                if (!_xap[_id]){
                    _xap[_id] = !0;
                    _ret.push(_map[_id]);
                }
            }
        );
        // merge no sorted items
        if (_ret.length!=_list.length){
            _u._$forEach(
                _list,function(_item){
                    var _id = _item.id;
                    if (!_xap[_id]){
                        _xap[_id] = !0;
                        _ret.push(_item);
                    }
                }
            );
        }
        return _ret;
    };
    /**
     * 序列化限制条件
     * @return {String}
     */
    _p._$limit2str = function(_value){
        if (_u._$isArray(_value)){
            if (!_value[0]){
                return '小于'+_value[1]+'px';
            }else if(!_value[1]){
                return '大于'+_value[0]+'px';
            }
            return _value.join('~')+'px';
        }
        return !_value?'无限制':(_value+'px');
    };
    /**
     * 显示错误信息
     * @param {Object} _message
     */
    _p._$showMessage = (function(){
        var _node,_timer;
        var _doHideMessage = function(){
            _timer = window.clearTimeout(_timer);
            _e._$removeByEC(_node);
            _node.className = 'alert';
            _node.innerHTML = '&nbsp;';
        };
        return function(_message,_clazz){
            if (!_node){
                _node = _e._$create('div','alert');
            }
            _e._$addClassName(_node,_clazz||'alert-danger');
            _node.innerHTML = _message||'&nbsp;';
            document.body.appendChild(_node);
            if (!!_timer){
                _timer = window.clearTimeout(_timer);
            }
            _timer = window.setTimeout(_doHideMessage,10000);
        };
    })();
    
    return _p;
});
