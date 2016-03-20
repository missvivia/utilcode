/*
 * --------------------------------------------
 * 快哭了，竟然翻出了两年前写的gesture demo.
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */

define(['pro/extend/util'], function(_){
  var 
    toString = ({}).toString,
    slice = [].slice,
    sqrt = Math.sqrt,
    pow = Math.pow,
    doc = document,
    $ =  function(sl){return doc.querySelector(sl)},
    $$ = function(sl){return doc.querySelectorAll(sl)},
    fallbackMaps = {
      touchstart:"mousedown",
      touchend : "mouseup",
      touchmove : "mousemove"
    },
    css = function(el , name ,value){
      if(_.typeOf(name) == "object"){
        for(var i in name){
          css(el, i , name[i])
        }
      }else{
        el.style[name] = value
      }
    },
    useFallback = true, // 是否用mouse事件做fallback
    on = function(el, type, fn){
      var fallback,types = type.split(" ")
      if(types.length > 1){return types.forEach(function(item){on(el, item ,fn) }) }
      el.addEventListener(type, fn, false)
    },
    off = function(el, type, fn){
      var fallback,types = type.split(" ")
      if(types.length > 1){types.forEach(function(item){off(el, item ,fn) }) }
      else el.removeEventListener(type, fn, false)
    },
    trigger = function(el, event, data){
      var event = createEvent(event),data = data || {}
      _.extend(event, data)
      el.dispatchEvent(event)
    },
    createEvent = function(type, props){
      var event = document.createEvent("Event")
      props && _.extend(event, props)
      event.initEvent(type, true, true)
      return event
    }


  function gesturify(el, option){
    option = option || {};
    if(typeof (el) === "string") el = $(el)
    if(!el || el.gesturify === true) return el;

    el.gesturify = true;


    var touch = {}, //信息容器 
      timer = {
        hold : null,
        swip :null
      },
      delay = {
        tap:150,//单击
        dbTap : 220, //双击
        hold :600,
        swip: 80
      },
      clearTimer = function(name){
        if(!name){
          for(var i in timer){clearTimer(i)}
        }else{
          if(timer[name]) clearTimeout(timer[name])
        }
      },
      // prepareFakeTransform = function(){
      //   touch.fakeTransform = true
      //   css(touch.point,{
      //     left:(touch.startPosition[0].x)-20,
      //     top:(touch.startPosition[0].y)-20,
      //     display:"block"
      //   }) 
      //   doc.body.appendChild(point)
      // },
      // endFakeTransform = function(){
      //   touch.fakeTransform = false
      // },
      getPosition=function(event){
        if(~event.type.indexOf("touch")){
          var pos =[],touches = event.touches,len = touches.length
          for(var i = 0;i < len; i++){
            pos.push({
              x: touches[i].pageX,
              y: touches[i].pageY
            })
          }
          return pos
        }else{
          return [{x: event.pageX, y : event.pageY}]
        }
      },
      onStart = function(event){
        // if(option.stop) event.preventDefault();
        touch.touches = event.touches || [event]
        if(touch.touches&&(touch.touches.length > 1)) touch.mult = true
        else touch.mult = false
        touch.startPosition = getPosition(event)
        var now = Date.now()
        // 处理tap
        if(!touch.lastTouchStart) touch.lastTouchStart = now
        timer.hold = setTimeout(function(){
          if(!touch.mult){
            if(!touch.position || compute(touch.position[0]||touch.position,touch.startPosition[0]||touch.startPosition).distance < 10)
            trigger(el, "hold", touch.startPosition[0]||touch.startPosition)
          }
        },delay.hold)
        if(timer.swip){
          clearTimer('swip');
        }
        timer.drag = setTimeout(function(){
          if(!touch.mult){
             trigger(el, "dragstart" , {x: touch.startPosition[0].x, y:touch.startPosition[0].y})
             touch.drag = true;
          }
        }, 100)
        timer.swip = setTimeout(function(){
          if(!touch.position) return;
          var distance = compute(touch.position[0]||touch.position,touch.startPosition[0]||touch.startPosition).distance
          if(distance > 8 && !touch.mult){
             trigger(el, "swip" ,{end:touch.position[0]||touch.position,start:touch.startPosition[0]||touch.startPosition})
          }
           touch.position = null;
        },delay.swip)
      },
      onEnd = function(event){
        if(option.stop) event.preventDefault();

        var now = Date.now()
        clearTimer("hold") // 取消hold的Timer

        // 重新判断 mult
        if(touch.touches&&(touch.touches.length > 1)) touch.mult = true
        else touch.mult = false

        touch.position = null;

        clearTimer("drag");
        if(touch.drag){
          trigger(el, "dragend");
        }
        //侦测tap 
        if( touch.lastTouchStart && now - touch.lastTouchStart < delay.tap ){
          trigger( el, "tap" , {event: event})
          if( touch.lastTap ){
            if(now - touch.lastTap <  delay.dbTap){
               trigger(el,"dbtap") // db tap
               touch.lastTap = null
            }else{
              touch.lastTap = now
            }
            //无论是否判断成功都清空lastTap
          }else{
            touch.lastTap = now
          }

          
        } 
        touch.lastTouchStart = null
      },
      onMove = function(event){
        if(option.stop) event.preventDefault();
        if(!touch.touches||!touch.touches.length) return
        touch.position = getPosition(event)

        if(touch.drag && !touch.mult){
          trigger(el, "dragmove", {
            x: touch.position[0].x - touch.startPosition[0].x,
            y: touch.position[0].y - touch.startPosition[0].y
          })
        }

        if(touch.mult){
          var data2 = compute(touch.position[0],touch.position[1])
          var data1 = compute(touch.startPosition[0],touch.startPosition[1])
          trigger(el, "transform", {
            scale: data2.distance / data1.distance,
            rotate :data2.angle - data1.angle
          })
        }
      },
      onCancel = function(event){
        touch.position = null;

        console.log("onCancel了")
      },
      compute = function(pos1, pos2){
        var v1 = {x:pos2.x-pos1.x,y:pos2.y-pos1.x},
          v2 = {x:1,y:0}
        var result = {
          distance: sqrt(pow(pos1.x-pos2.x,2)+pow(pos1.y-pos2.y,2)),
          angle: Math.atan2(pos2.y - pos1.y, pos2.x - pos1.x) * 180 / Math.PI
        } 
        return result
      },
      // 只是Demo简单起见暂时都绑在一个handle中
      handle = function(event){

        switch(event.type){
          case "touchstart":
            onStart(event)
            break
          case "touchend":
            onEnd(event)
            break
          case "touchmove":
            onMove(event)
            break
          case "touchcancel":
            onCancel(event)
        }
      }
    on(el, "touchstart touchmove touchend touchcancel", handle)
    return el
  }

  return gesturify;
})


