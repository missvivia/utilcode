/*
 * --------------------------------------------
 * xx控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define([ 'base/klass', 
         'base/util', 
         'base/event', 
         'base/element', 
         'util/event',
		 'ui/base', 
		 'util/template/tpl',  
		 'text!./explosion.html',
		 'text!./explosion.css',
		 'util/animation/easein',
		 'util/animation/linear',
		 '../layer/explosion.win.r/explosion.win.js',
		 'util/cache/cookie',
		 'pro/extend/util'
		 ],
		function(k, ut, v, e, t, i, e1,  html,_css,_t1,_t2,ExplosionWin ,_j,_,p, o, f, r) {
			var pro, sup, 
			_seed_html = e1._$addNodeTemplate(html),
			_seed_css = e._$pushCSSText(_css),
			_BROKENHEIGHT = 409,
			_RADIUS = 183;
			_SPEED = 35;

			/**
			 * 全局状态控件
			 * @class   {nm.i._$$Explosion}
			 * @extends {nej.ui._$$Abstract}
			 */
			p._$$Explosion = k._$klass();
			pro = p._$$Explosion._$extend(i._$$Abstract);
			sup = p._$$Explosion._$supro;

			/**
			 * 重置控件
			 * @param  {[type]} options [description]
			 *
			 */
			pro.__reset = function(options) {
				options.parent = options.parent || document.body;
				this.__super(options);
				this.__initBalls();
			};
			pro.__initBalls = function(){
				var ballsRandom =[
				                  {clazz:'eball0',radius:5},{clazz:'eball1',radius:13},{clazz:'eball2',radius:20},{clazz:'eball3',radius:35},
				                	  {clazz:'eball4',radius:5},{clazz:'eball5',radius:13},{clazz:'eball6',radius:20},{clazz:'eball7',radius:35}
				                 ];
				e._$clearChildren(this.__box);
				e._$clearChildren(this.__flowbox);
				this.__ballList =[];
				this.__flowBallList =[];
				for(var i=0,l=100;i<l;i++){
					var ball = e._$create('div','eball'+parseInt(Math.random()*10000)%7);
					this.__ballList.push({ball:ball,deg:3.6*i,accelate:ut._$randNumber(4,10)});
					this.__box.appendChild(ball);
				}
				var sep =60;
				var degList =[{deg:10,radius:55+sep},{deg:130,radius:36+sep},{deg:165,radius:65+sep},{deg:210,radius:45+sep},{deg:320,radius:38+sep}];
				for(var i=0,l=5;i<l;i++){
					var ball = e._$create('div','eball'  +' eballflow-'+i);
					this.__flowbox.appendChild(ball);
					this.__flowBallList.push({ball:ball,deg:degList[i].deg,accelate:_RADIUS+degList[i].radius,radius:degList[i].radius});
					console.log('top:'+parseInt(_RADIUS-this.__flowBallList[i].accelate*Math.sin(degList[i].deg*Math.PI/180)-degList[i].radius)+'px;left:'+parseInt(_RADIUS+this.__flowBallList[i].accelate*Math.cos(degList[i].deg*Math.PI/180)-degList[i].radius)+'px;')
					
				}
			};
			/**
			 * 控件销毁
			 *
			 */
			pro.__destroy = function() {
				this.__super();
			};

			/**
			 * 初使化UI
			 */
			pro.__initXGui = function() {
				//在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
				//this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
				//这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
				this.__seed_html = _seed_html;
				this.__seed_css = _seed_css;
			};
			/**
			 * 节点初使化
			 *
			 */
			pro.__initNode = function() {
				this.__super();
				var list = e._$getByClassName(this.__body,'j-flag');
				this.__ball = list[0];
				this.__r1 = list[1];
				this.__r2 = list[2];
				this.__bubble = list[3];
				this.__box = list[4];
				this.__flowbox = list[5];
				this.__text = list[6];
				var date = +_.setDate('2015-1-17');
				var now = +new Date();
				if((now>date)){
					this.__text.style.background ='url(/res/images/activity/0117/text2.png) no-repeat'
				}
				v._$addEvent(this.__ball,'click',this.__onBallBroken._$bind(this));
				v._$addEvent(this.__text,'mouseOver',this.__onMouseOver._$bind(this,true));
				v._$addEvent(this.__text,'mouseOut',this.__onMouseOver._$bind(this,false));
			};
			pro.__onMouseOver = function(_ishover){
				if(_ishover){
					if(!e._$hasClassName(this.__flowbox,'hover')){
						e._$addClassName(this.__flowbox,'hover')
					}
				} else{
					e._$delClassName(this.__flowbox,'hover')
				}
			};
			pro.__onBallBroken = function(){
				e._$addClassName(this.__ball,'broken broken-1')
				var _easein0  = _t1._$$AnimEaseIn._$allocate({
				              from:{
				                  offset:0
				              },
				              to:{
				                  offset:8
				              },
				              duration:1100,
				              onupdate:function(_event){
				            	  console.log(_event.offset);
				            	  var offset = _event.offset;
				            	  for(var i=0,l=this.__ballList.length;i<l;i++){
				            		  var deg = this.__ballList[i].deg;
				            		  var top = (_RADIUS-this.__ballList[i].accelate*offset*_SPEED*Math.sin(deg*Math.PI/180));
				            		  var left = (_RADIUS+this.__ballList[i].accelate*offset*_SPEED*Math.cos(deg*Math.PI/180));
				            		  this.__ballList[i].ball.style.top = top+'px'
				            		  this.__ballList[i].ball.style.left = left+'px'
				            		  this.__ballList[i].ball.style.opacity = (1- offset/10)
				            		  this.__ballList[i].ball.style.filter = 'alpha(opacity='+offset*10+')';
				            	  }
				            	  for(var i=0,l=this.__flowBallList.length;i<l;i++){
				            		  var deg = this.__flowBallList[i].deg;
				            		  var top = (_RADIUS-this.__flowBallList[i].accelate*(1.2+offset)*Math.sin(deg*Math.PI/180)-this.__flowBallList[i].radius);
				            		  var left = (_RADIUS+this.__flowBallList[i].accelate*(1.2+offset)*Math.cos(deg*Math.PI/180)-this.__flowBallList[i].radius);
				            		  this.__flowBallList[i].ball.style.top = top+'px'
				            		  this.__flowBallList[i].ball.style.left = left+'px'
				            		  this.__flowBallList[i].ball.style.opacity = (1- offset/10)
				            		  this.__flowBallList[i].ball.style.filter = 'alpha(opacity='+offset*10+')';
				            		  console.log(top+'px;'+left+'px;')
				            	  }
				              }._$bind(this),
				              onstop:function(){
				            	  this._$dispatchEvent('onanimationend');
				            	  _j._$cookie('isshowbuble',{value:1});
				            	  this.__ball.style.backgroundPosition = '0 0';
				            	  //this.__initBalls();
				            	  //e._$delClassName(this.__ball,'broken broken-1')
				            	  _easein0._$recycle();
				            	  e._$remove(this.__ball);
				            	  
				              }._$bind(this)
				          });
				var _easein1  = _t2._$$AnimLinear._$allocate({
				              from:{
				                  offset:0
				              },
				              to:{
				                  offset:10
				              },
				              duration:400,
				              onupdate:function(_event){
				            	  var offset = _event.offset;
				            	  this.__ball.style.backgroundPosition = '0 '+ (-_BROKENHEIGHT*parseInt(offset)) +'px';
				              }._$bind(this),
				              onstop:function(){
				            	  _easein1._$recycle();
				            	  if(window.ay0126End){
				            		  this._$recycle();
				            	  }
				            	  else{
				            	  var win =new ExplosionWin({
			            		  data:{mask:false}
				            	  });
				            	  win.$on('close',function(){
				            		  this._$recycle();
				            	  }._$bind(this));
				            	  win.$on('confirm',function(){
				            		  this._$recycle();
				            	  }._$bind(this));
				            	  win.$show();  
				            	  }

				              }._$bind(this)
				          });
				_easein1._$play();
				_easein0._$play();
			};
			
			return p._$$Explosion;
		})