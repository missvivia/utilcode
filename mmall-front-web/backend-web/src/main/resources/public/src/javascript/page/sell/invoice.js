/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/sell/invoice.js'
    ],
    function(ut,v,e, Module,Invoice, p) {
        var pro;

        p._$$SizeModule = NEJ.C();
        pro = p._$$SizeModule._$extend(Module);
        
        pro.__init = function(_options) {
            _options.tpl = 'jstTemplate';
            this.__supInit(_options);
            var invoice = new Invoice();
            invoice.$inject('#invoice');
            invoice.search();
        };
        
        
        // pro.__addEvent = function(){
        //     v._$addEvent(this.searchtmp,'click',this.__onSearchBtnClick._$bind(this)); 
        //     v._$addEvent(this.notadd,'click',this.__onNotaddBtnClick._$bind(this));
        //     v._$addEvent(this.alreadyadd,'click',this.__onAlreadyaddBtnClick._$bind(this));

        // };
        // pro.__onSearchBtnClick = function(event){
        //     this.__dataPack();
        // }
        // pro.__dataPack = function(){
        //     var _list = e._$getByClassName(document,'j-save');
        //         _data = {};
        //     for(var i=0,len=_list.length;i<len;i++){
        //         _data[_list[i].name] = _list[i].value;
        //     }
        //     console.log(_data);
        // }
        // pro.__onNotaddBtnClick = function(){
        //     e._$addClassName(this.notadd,'addclick');
        //     e._$delClassName(this.alreadyadd,'addclick');
        //     e._$replaceClassName(this.table1,'f-dn','f-db');
        //     e._$replaceClassName(this.table2,'f-db','f-dn');
        // }
        // pro.__onAlreadyaddBtnClick = function(){
        //     e._$delClassName(this.notadd,'addclick');
        //     e._$addClassName(this.alreadyadd,'addclick');
        //     e._$replaceClassName(this.table2,'f-dn','f-db');
        //     e._$replaceClassName(this.table1,'f-db','f-dn');
        // }

        
        p._$$SizeModule._$allocate();
    });