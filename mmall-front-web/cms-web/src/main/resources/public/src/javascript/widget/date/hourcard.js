/*
 * 小时控件
 * @author cheng-lin(cheng-lin@corp.netease.com)
 */
NEJ.define(['{lib}ui/layer/card.wrapper.js',
        '{lib}util/calendar/calendar.js'],function(){
	var _  = NEJ.P;
		  _e = _('nej.e'),
		  _p = _('du.ui'),
		  _ui= _('nej.ui');
  var _seed_css,_seed_html,_seed_date;
	  _p._$$HourCard = NEJ.C();
    _pro = _p._$$HourCard._$extend(_ui._$$CardWrapper);
    _sup = _p._$$HourCard._$supro;

    /**
     * 动态构建控件节点模板
     * @protected
     * @method {__initNodeTemplate}
     * @return {Void}
     */
    _pro.__initNodeTemplate = function(){
        _seed_html = _e._$addNodeTemplate(
            '<div class="'+_seed_css+' zcard">'+
               _e._$getHtmlTemplate(_seed_date)+
            '</div>'
        );
        this.__seed_html = _seed_html;
    };

    _pro.__initNode = function(){
      this.__supInitNode();
      this.__zday = _e._$getByClassName(this.__body,'zday')[0];
    };

    _pro.__reset = function(_options){
      this.__supReset(_options);
      this.__doInitDomEvent([
            [this.__zday,'click',this.__onChangeHour._$bind(this)]
        ]);
    };

    _pro.__onChangeHour = function(_event){
      var _target = _v._$getElement(_event);
      if(_target.nodeName!='A') return;
      var _value = _target.innerText;
      this._$dispatchEvent('onhourchange',_value);
      this._$hide();
    };

    // ui css text
    _seed_css = _e._$pushCSSText('\
        .m-time{z-index:9999;}\
        .#<uispace>{width:210px;border:1px solid #aaa;font-size:14px;text-align:center;}\
        .#<uispace> .zact{line-height:30px;overflow:hidden;zoom:1;}\
        .#<uispace> .zact .zfl{float:left;}\
        .#<uispace> .zact .zfr{float:right;}\
        .#<uispace> .zact .zbtn{padding:0 5px;cursor:pointer;}\
        .#<uispace> .zact .ztxt{margin-left:10px;}\
        .#<uispace> .zday{table-layout:fixed;border-collapse:collapse;width:100%;}\
        .#<uispace> .zday th{font-weight:normal;}\
        .#<uispace> .zday a{display:block;height:22px;line-height:22px;color:#333;text-decoration:none;}\
        .#<uispace> .zday a:hover{background:#eee;}\
        .#<uispace> .zday a.js-extended{color:#aaa;}\
        .#<uispace> .zday a.js-selected,\
        .#<uispace> .zday a.js-selected:hover{background:#DAE4E7;}\
        .#<uispace> .zday a.js-disabled,\
        .#<uispace> .zday a.js-disabled:hover{background:#fff;color:#eee;cursor:default;}\
    ');
    // ui date html
    _seed_date = _e._$addHtmlTemplate('\
        <table class="zday">\
          {list 0..3 as x}\
          <tr>{list 1..6 as y}<td><a href="#" class="js-ztag">${x*6+y-1}</a></td>{/list}</tr>\
          {/list}\
        </table>\
    ');
});