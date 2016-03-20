/*
 * 日期选择
 * @author cheng-lin(cheng-lin@corp.netease.com)
 */
NEJ.define(['{lib}ui/datepick/datepick.js','{pro}widget/date/calendar.js'],function(){
	var _  = NEJ.P;
		_e = _('nej.e'),
		_p = _('du.ui'),
    _f = _('nej.f'),
		_ui= _('nej.ui');
    var _seed_css,_seed_html,_seed_date,
        _seed_action = _u._$uniqueID();
	  _p._$$DatePick = NEJ.C();
    _pro = _p._$$DatePick._$extend(_ui._$$DatePick);

    _pro.__reset = function(_options){
        this.__copt.range = _options.range;
        this.__copt.onselect = this.__onDateChange._$bind(this);
        this.__copt.ontimeselected = this.__onTimeSelected._$bind(this);
        this.__supReset(_options);
        if(this.__calendar) this.__calendar._$recycle();
        this.__calendar = _p._$$Calendar
                            ._$allocate(this.__copt);
        this.__calendar._$initTime();
    };

    /**
     * 动态构建控件节点模板
     * @protected
     * @method {__initNodeTemplate}
     * @return {Void}
     */
    _pro.__initNodeTemplate = function(){
        _seed_html = _e._$addNodeTemplate(
            '<div class="'+_seed_css+' zcard">'+
               _e._$getTextTemplate(_seed_action)+
               _e._$getHtmlTemplate(_seed_date)+
               _e._$getHtmlTemplate(_seed_date_ext)+
            '</div>'
        );
        this.__seed_html = _seed_html;
    };

    _pro.__initNode = function(){
      this.__supInitNode();
      var _list = _e._$getByClassName(this.__body,'ztime');
      this.__copt.hour = _list[0];
      this.__copt.minute = _list[1];
      this.__copt.second = _list[2];
      this.__copt.today = _list[3];
      this.__copt.ok = _list[4];
      this.__copt.pbody = this.__body;
    };

    _pro.__onTimeSelected = function(_options){
      try{
          this._$dispatchEvent('onchange',_options);
      }catch(e){
          // ignore
      }
      this._$hide();
    };

    _pro.__onDateChange = function(){

    };

    // ui css text
    _seed_css = _e._$pushCSSText('\
        .#<uispace>{width:210px;border:1px solid #aaa;font-size:14px;text-align:center;}\
        .#<uispace> .m-timeout{border-top:solid 1px #ccc;}\
        .#<uispace> .m-timeout .timebtn{margin:5px 0;}\
        .#<uispace> .m-date input{width:20px;}\
        .#<uispace> .m-timer{margin:5px 10px;}\
        .#<uispace> .m-timer input{border:0;text-align:center;}\
        .#<uispace> .m-timebox{position:relative;border:solid 1px #ccc;}\
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
        .#<uispace> .btn{margin:0 20px;}\
    ');
    _seed_date_ext = _e._$addHtmlTemplate('\
       <div class="f-cb m-timeout">\
        <div class="f-cb m-timer">\
          <label class="f-fl">时间：</label>\
          <div class="f-fl m-timebox">\
            <div class="m-date f-fl">\
              <input class="ztime" type="text" />:\
            </div>\
            <div class="m-date f-fl">\
              <input class="ztime" type="text" />:\
            </div>\
            <div class="m-date f-fl">\
              <input class="ztime" type="text" />\
            </div>\
          </div>\
        </div>\
        <div class="timebtn"><span class="ztime btn btn-default">今天</span><span class="ztime btn btn-primary">确定</span></div>\
       </div>\
    ');
    // ui date html
    _seed_date = _e._$addHtmlTemplate('\
        <table class="zday">\
          <tr>{list ["日","一","二","三","四","五","六"] as x}<th>${x}</th>{/list}</tr>\
          {list 1..6 as x}\
          <tr>{list 1..7 as y}<td><a href="#" class="js-ztag"></a></td>{/list}</tr>\
          {/list}\
        </table>\
    ');
    // button html
    _e._$addTextTemplate(_seed_action,'\
        <div class="zact">\
          <span class="zbtn zfl" title="上一年">&lt;&lt;</span>\
          <span class="zbtn zfl" title="上一月">&lt;</span>\
          <span class="zbtn zfr" title="下一年">&gt;&gt;</span>\
          <span class="zbtn zfr" title="下一月">&gt;</span>\
          <span class="ztxt"></span>年\
          <span class="ztxt"></span>月\
        </div>\
    ');
});