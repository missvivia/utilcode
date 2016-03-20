/**
 * xx平台活动编辑——商品添加页
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
    '{pro}widget/module.js', 
    '{pro}components/activity/list.js'
    ],
    function(Module, ActList, e, c) {
        var pro, 
          $$CustomModule = NEJ.C(),
          pro = $$CustomModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__areaList = window["g_areaList"]||[];
            this.__audit = window["g_audit"];
            this.__areaList.unshift({areaName:'全部',id:0});
            this.__addEvent();
        };
        
        pro.__addEvent = function(){
        	this.__list = new ActList({
                data: {
                	areaList:this.__areaList,
                	audit:this.__audit
                }
             }).$inject(".j-it");
        	
        };

        // var list = [];
        // for(var i =0; i < 100; i++){
        //   list.push({
            
        //       name: 'name' + i,
        //       tags:[
        //         {name: 'tag1', desc: 'tag1 desc'}
        //       ],
        //       startTime: +new Date,
        //       endTime: +new Date,
        //       submitTime: +new Date,
        //       status: i % 5,
        //       author: {name: "leeluolee"},
        //       reason: "无论如何请走开"
        //   })
        // }

        var module = $$CustomModule._$allocate({
          data: {},
          //事件传入
          events: {
            "remove1": function(index){
                console.log("remove in events " + index)
            }
          },
          // 在compile之前
          config: function(){
            this.$on('remove1', function(){
                console.log("remove in config")
            })

          },
          remove1: function(index){
            console.log(index)
          },
          // 在compile之后
          init: function(){
            this.supr();
            this.$on('remove1', function(){
                console.log("remove in init")
            })
          }
        });

        module.__hub;

        return $$CustomModule;
    });