/*

 * SiteMaster_JS - v0.0.1 - 2014-10-08

 * Copyright (c) 2014 Admaster.inc; Licensed

 */





(function() {

    var SM_VERSION, sm,

        __slice = [].slice;



    // 版本号

    SM_VERSION = '0.0.1';

    // 初始化变量

    sm = {};



    // 延迟0秒执行

    setTimeout(function() {

        var clickDoing, isReady, lastScrollPosition, packer, send, _;

        _ = sm.cookieInit({});

        _ = sm.toolInit(_);

        packer = sm.packerInit(_);

        isReady = false;

        clickDoing = false;

        lastScrollPosition = null;

        // smq 处理方法

        send = function(item) {

            var fn, fnName, pac;

            // 数组的第一项是函数的名字, 也可能直接就是个函数

            fnName = item.shift();

            // 允许用户push一个方法，监控准备好了以后会调用之

            // 监控准备好之前这个函数的执行是异步的，监控准备好之后将是同步的返回

            if (_.isFunction(fnName)) {

                return fnName.apply(null, item);

            }

            // _setAccount 未执行前其他的都不能执行

            // _setAccount 执行后 isReady 为 true

            if (fnName === '_setAccount') {

                isReady = true;

            }

            if (!isReady) {

                return;

            }

            // 从packer获取方法，如果不存在就直接退出，以免接下来的执行出错

            if (!(fn = packer[fnName])) {

                return;

            }

            // 执行packer对应的函数，收集返回值，返回值有以下几种情况

            // 1.如果返回为对象，且有send属性，且等于 yes 则调用tool.request将数据发送到采集服务器

            // 2.除情况1之外, 直接返回给用户,这种情况是packer暴露出来的功能性函数，比如获取版本号之类的

            pac = fn.apply(null, item);

            if (_.isObject(pac) && pac.send === true) {

                return _.request(pac);

            }

            return pac;

        };

        // 将用户初始添加到smq上的数据全部处理一遍
        if(typeof _smq==="undefined")return;
        while (_smq.length) {

            send(_smq.shift());

        }

        // 改写_smq的push方法，外部调用_sqm.push可以直接对应到packer暴露出来的方法上

        _smq.push = send;

        // 注册页面关闭事件

        return _.event.add(window, 'unload', function(e) {

            return send(['pageClose']);

        });

    }, 0);



    // cookie工具，读取写入cookie

    sm.cookieInit = function($) {

        var config, dc, decode, ec, encode, extend, parseCookieValue, pluses, read, stringifyCookieValue;

        if ($ == null) {

            $ = {};

        }

        ec = encodeURIComponent;

        dc = decodeURIComponent;

        pluses = /\+/g;

        // 编码函数

        encode = function(s) {

            if (config.raw) {

                return s;

            } else {

                return ec(s);

            }

        };

        // 解码函数

        decode = function(s) {

            if (config.raw) {

                return s;

            } else {

                try {

                    return dc(s);

                } catch (_error) {}

            }

        };

        stringifyCookieValue = function(value) {

            return encode(String(value));

        };

        parseCookieValue = function(s) {

            if (s.indexOf('"') === 0) {

                s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');

            }

            try {

                s = dc(s.replace(pluses, ' '));

            } catch (_error) {}

            return s;

        };

        read = function(s, converter) {

            var value;

            value = config.raw ? s : parseCookieValue(s);

            return (typeof converter === "function" ? converter(value) : void 0) || value;

        };

        extend = function() {

            var k, obj, objs, ret, v, _i, _len;

            objs = 1 <= arguments.length ? __slice.call(arguments, 0) : [];

            ret = objs.shift();

            for (_i = 0, _len = objs.length; _i < _len; _i++) {

                obj = objs[_i];

                for (k in obj) {

                    v = obj[k];

                    ret[k] = v;

                }

            }

            return ret;

        };

        config = $.cookie = function(key, value, options) {

            var cookie, cookies, days, item, name, parts, result, t, _i, _len;

            if (value !== void 0) {

                options = extend({}, config.defaults, options);

                if (typeof options.expires === 'number') {

                    days = options.expires;

                    t = options.expires = new Date();

                    t.setDate(t.getDate() + days);

                }

                return document.cookie = [encode(key), '=', stringifyCookieValue(value), options.expires ? '; expires=' + options.expires.toUTCString() : '', options.path ? '; path=' + options.path : '', options.domain ? '; domain=' + options.domain.split(':')[0] : '', options.secure ? '; secure' : ''].join('');

            }

            result = key ? void 0 : {};

            cookies = document.cookie ? document.cookie.split('; ') : [];

            for (_i = 0, _len = cookies.length; _i < _len; _i++) {

                item = cookies[_i];

                parts = item.split('=');

                name = decode(parts.shift());

                cookie = parts.join('=');

                if (key === name) {

                    result = read(cookie, value);

                    break;

                }

                if (!key && (cookie = read(cookie)) !== void 0) {

                    result[name] = cookie;

                }

            }

            return result;

        };

        config.defaults = {

            expires: 720,

            path: '/'

        };

        $.removeCookie = function(key, options) {

            if ($.cookie(key) === void 0) {

                return false;

            }

            $.cookie(key, '', extend({}, options, {

                expires: -1

            }));

            return true;

        };

        return $;

    };

    // 初始化工具函数

    sm.toolInit = function(_, undef) {

        var COOKIE_NAME, ObjProto, createUID, doc, docEle, ec, nativeIsArray, nav, toString, win;

        if (_ == null) {

            _ = {};

        }

        // 初始化变量，方便后面调用

        win = window;

        doc = document;

        ec = encodeURIComponent;

        docEle = doc.documentElement;

        nav = navigator;

        ObjProto = Object.prototype;

        nativeIsArray = Array.isArray;

        toString = ObjProto.toString;

        COOKIE_NAME = '_smt_uid';

        // 创建user id函数

        createUID = function() {

            var random, timestamp;

            timestamp = _.time();

            random = _.intval(Math.random() * timestamp);

            if (random < 100000000) {

                random += 100000000;

            }

            return [timestamp.toString(16), random.toString(16)].join('.');

        };

        // 返回秒级时间函数

        _.time = function() {

            return _.intval((new Date()).valueOf() / 1000);

        };

        // 设置或获取uid函数，如果不存在uid则调用createUID函数，如果有，则直接获取。

        _.uid = function(domain) {

            var uid;

            if (domain == null) {

                domain = '';

            }

            uid = _.cookie(COOKIE_NAME);

            if (uid) {

                return uid;

            }

            uid = createUID();

            _.cookie(COOKIE_NAME, uid, {

                domain: domain

            });

            return uid;

        };

        // 判断obj是不是function函数

        _.isFunction = function(obj) {

            return toString.call(obj) === '[object Function]';

        };

        // 判断obj是不是array函数

        _.isArray = nativeIsArray || function(obj) {

            return toString.call(obj) === '[object Array]';

        };

        // 判断obj是不是undefined函数

        _.isUndefined = function(obj) {

            return obj === undef;

        };

        // 判断obj是不是number函数

        _.isNumber = function(obj) {

            return toString.call(obj) === '[object Number]';

        };

        // 判断obj是不是obj函数

        _.isObject = function(obj) {

            return obj === Object(obj);

        };

        // map函数

        _.map = _.collect = function(obj, iterator, context) {

            var index, results, value, _i, _len;

            results = [];

            if (!obj) {

                return results;

            }

            for (index = _i = 0, _len = obj.length; _i < _len; index = ++_i) {

                value = obj[index];

                results[results.length] = iterator.call(context, value, index, obj);

            }

            return results;

        };

        // 解析integer函数

        _.intval = function(value, mod) {

            if (mod == null) {

                mod = 10;

            }

            return parseInt(value, mod) || 0;

        };

        // 格式化url，返回uri，params,hash,querystring

        _.parseUrl = function(url) {

            var expRet, hash, params, q, uri, urlArr;

            if (!url) {

                return {};

            }

            expRet = (new RegExp('#.*$')).exec(url);

            hash = _.isArray(expRet) ? expRet[0] : '';

            urlArr = url.replace(hash, '').split('?');

            uri = urlArr.shift();

            q = urlArr.join('?') || "";

            params = q.length > 0 ? _.map(q.split('&'), function(x) {

                return x.split('=');

            }) : [];

            return {

                uri: uri,

                params: params,

                hash: hash,

                qs: q

            };

        };

        // 查找url中指定key的value

        _.findUrl = function(url, key) {

            var params, value;

            params = (_.parseUrl(url)).params;

            value = '';

            _.map(params, function(x) {

                if (x[0] === key) {

                    return value = x[1];

                }

            });

            return value;

        };

        // 解析url中smtb参数

        _.decodeSmtb = function(s) {

            var c, i, k, num, p, ret, t, tmp;

            tmp = [];

            ret = [];

            s = s.split('').reverse();

            if (s.length === 23) {

                s.push(0);

            }

            i = 0;

            while (i < s.length) {

                tmp = [];

                tmp.push(s[i + 1]);

                tmp.push(s[i]);

                t = tmp.join("");

                num = parseInt(t, 16);

                if (num < (i / 2) + 1) {

                    num += 256;

                }

                num -= (i / 2) + 1;

                ret.push(num);

                i += 2;

            }

            p = (ret[0] << 24) + (ret[1] << 16) + (ret[2] << 8) + ret[3];

            c = (ret[4] << 24) + (ret[5] << 16) + (ret[6] << 8) + ret[7];

            k = (ret[8] << 24) + (ret[9] << 16) + (ret[10] << 8) + ret[11];

            if (!isNaN(p) && !isNaN(c) && !isNaN(k)) {

                return [p, c, k];

            }

            return void 0;

        };

        // 加密smtb参数

        _.encodeSmtb = function(p, c, k, cb) {

            var error, i, num, ret, right, s, _i, _j, _len, _len1, _ref, _ref1;

            s = [];

            i = 1;

            ret = "";

            try {

                _ref = [p, c, k];

                for (_i = 0, _len = _ref.length; _i < _len; _i++) {

                    num = _ref[_i];

                    _ref1 = [24, 16, 8, 0];

                    for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {

                        right = _ref1[_j];

                        ret = (num >> right & 255) + i;

                        ret = ret >= 256 ? ret - 256 : ret;

                        ret = (ret << 4 & 240) + (ret >> 4);

                        ret = ret.toString(16);

                        if (ret.length === 1) {

                            ret = "0" + ret;

                        }

                        s.push(ret);

                        i += 1;

                    }

                }

                ret = s.join('').split('').reverse().join('').replace(/^0+/, '').toUpperCase();

                if (cb) {

                    return cb(null, ret);

                }

                return ret;

            } catch (_error) {

                error = _error;

                if (error) {

                    return cb(error);

                }

            }

        };

        // 页面载入时间，从fetch到contentLoad

        _.pageLoadTime = function() {

            var perf, timing;

            if ((!(perf = win.performance)) || (!(timing = perf.timing))) {

                return 0;

            }

            if (timing.domContentLoadedEventEnd < timing.fetchStart) {

                return 0;

            }

            return _.intval(timing.domContentLoadedEventEnd - timing.fetchStart);

        };

        // 获取flash版本

        _.flashVersion = function() {

            var fl, plugin, v, _i, _j, _len, _ref;

            if (nav.plugins && nav.plugins.length) {

                _ref = nav.plugins;

                for (_i = 0, _len = _ref.length; _i < _len; _i++) {

                    plugin = _ref[_i];

                    if (plugin.name.indexOf('Shockwave Flash') === -1) {

                        continue;

                    }

                    return plugin.description.split('Shockwave Flash ')[1] || '';

                }

            }

            if (win.ActiveXObject) {

                for (v = _j = 10; _j >= 2; v = --_j) {

                    try {

                        fl = new ActiveXObject("ShockwaveFlash.ShockwaveFlash." + v);

                        if (fl) {

                            return "" + v + ".0";

                        }

                    } catch (_error) {}

                }

            }

            return '';

        };

        _.viewHeight = function() {

            var e;

            try {

                return docEle.clientHeight || doc.body.clientHeight;

            } catch (_error) {

                e = _error;

                return 0;

            }

        };

        _.scrollTop = function() {

            try {

                return _.intval(win.pageYOffset || docEle.scrollTop);

            } catch (_error) {}

            return 0;

        };

        _.scrollLeft = function() {

            try {

                return _.intval(win.pageXOffset || docEle.scrollLeft);

            } catch (_error) {}

            return 0;

        };

        _.throttle = function(func, wait) {

            var doing;

            doing = false;

            return function() {

                if (doing) {

                    return;

                }

                doing = true;

                setTimeout(function() {

                    return doing = false;

                }, wait);

                return func.apply(null, arguments);

            };

        };

        _.debounce = function(func, wait, immediate) {

            var timeout;

            timeout = null;

            return function() {

                var args;

                args = arguments;

                if (timeout) {

                    clearTimeout(timeout);

                }

                return timeout = setTimeout(function() {

                    return func.apply(null, args);

                }, wait);

            };

        };

        _.request = function(obj) {

            var url, urls, _i, _len, _results;

            urls = _.urlFormat(obj);

            _results = [];

            for (_i = 0, _len = urls.length; _i < _len; _i++) {

                url = urls[_i];

                _results.push(_.send(url, new Image(0, 0)));

            }

            return _results;

        };

        _.urlFormat = function(obj) {

            var k, qs, urls, v;

            qs = ((function() {

                var _ref, _results;

                _ref = obj.params;

                _results = [];

                for (k in _ref) {

                    v = _ref[k];

                    _results.push("" + k + "=" + (ec(v)));

                }

                return _results;

            })()).join('&');

            urls = ["" + obj.protocol + "//" + obj.host + obj.path + "?" + qs];

            if (obj.localPath) {

                urls.push("" + obj.localPath + obj.path + "?" + qs);

            }

            return urls;

        };

        _.send = function(url, mediator) {

            var _retry;

            mediator.src = url;

            _retry = function() {

                _.event.remove(mediator, 'error', _retry);

                return setTimeout(function() {

                    return mediator.src = "" + url + "&retry=yes";

                }, 2000);

            };

            return _.event.add(mediator, 'error', _retry);

        };

        _.random = function(len) {

            if (len == null) {

                len = 8;

            }

            return Math.random().toString().substr(2, len);

        };

        _.event = {

            add: function(elem, type, handler) {

                var _add, _attach, _on;

                _add = "addEventListener";

                _attach = "attachEvent";

                _on = "on" + type;

                if (elem[_add]) {

                    return elem[_add](type, handler, false);

                } else if (elem[_attach]) {

                    return elem[_attach](_on, handler);

                } else {

                    return elem[_on] = handler;

                }

            },

            remove: function(elem, type, handler) {

                var _detach, _on, _rm;

                _rm = "removeEventListener";

                _detach = "detachEvent";

                _on = "on" + type;

                if (elem[_rm]) {

                    return elem[_rm](type, handler, false);

                } else if (elem[_detach]) {

                    return elem[_detach](_on, handler);

                } else {

                    return elem[_on] = null;

                }

            },

            event: function(e) {

                return e || window.event;

            },

            target: function(e) {

                return e && (e.target || e.srcElement || null);

            }

        };

        _.customVars = function(vars, onlyCookie) {

            var ret, x, _i, _len;

            if (onlyCookie == null) {

                onlyCookie = false;

            }

            if (!onlyCookie) {

                return _.map(vars, function(x) {

                    return "v" + x[0] + "=" + x[1] + "&s" + x[0] + "=" + x[2];

                }).join('&');

            }

            ret = [];

            vars.sort(function(left, right) {

                if (left[0] > right[0]) {

                    return 1;

                }

                if (left[0] < right[0]) {

                    return -1;

                }

            });

            for (_i = 0, _len = vars.length; _i < _len; _i++) {

                x = vars[_i];

                if (+x[2] === 1) {

                    ret.push("" + x[0] + "=" + x[1]);

                }

            }

            return ret.join('&');

        };

        _.pageWidth = function() {

            var body, bodyWidth, childWidth, children, d, i, maxHeight, maxHeightElem, ret, width, _i;

            d = document;

            body = d.body;

            doc = d.documentElement;

            children = body.children;

            childWidth = 0;

            maxHeightElem = null;

            bodyWidth = doc.clientWidth || body.clientWidth;

            width = function(nodes) {

                var node, ret, _i, _len;

                ret = 0;

                for (_i = 0, _len = nodes.length; _i < _len; _i++) {

                    node = nodes[_i];

                    childWidth = node.offsetWidth;

                    if (childWidth > 600 && (childWidth < bodyWidth - 10 || childWidth > bodyWidth)) {

                        if (childWidth > ret) {

                            ret = childWidth;

                        }

                    }

                }

                return ret;

            };

            maxHeight = function(nodes) {

                var h, l, node, ret, _i, _len;

                l = -1;

                for (_i = 0, _len = nodes.length; _i < _len; _i++) {

                    node = nodes[_i];

                    h = node.offsetHeight;

                    if (h > l) {

                        ret = nodes[i];

                        l = h;

                    }

                }

                return ret;

            };

            if (ret = width(children)) {

                return ret;

            }

            for (i = _i = 0; _i < 3; i = ++_i) {

                maxHeightElem = maxHeight(children);

                if (maxHeightElem) {

                    children = maxHeightElem.children;

                    ret = width(children);

                    if (ret) {

                        break;

                    }

                }

            }

            return ret || body.offsetWidth;

        };

        return _;

    };



    sm.packerInit = function(_, undef) {

        var SMTBCOOKIENAME, VARSCOOKIENAME, clickEvent, custom, customVars, doc, ec, getOpt, host, loc, na, options, pageClose, pageview, params, pick, version, win, _decodeSmt_b, _encodeSmt_b, _setAccount, _setClickTimeOut, _setCustomVar, _setDomainName, _setHeatmapEnabled, _setLocalPath, _setReferrerSmtEnabled, _setSSL;

        win = window;

        na = navigator;

        doc = document;

        loc = doc.location;

        ec = encodeURIComponent;

        VARSCOOKIENAME = '_smtv';

        SMTBCOOKIENAME = '_smtb';

        customVars = [];

        options = {

            isSSL: loc.protocol === 'http:' ? 'no' : 'yes',

            allowLinker: false,

            referrerSmtEnabled: false,

            siteId: '',

            domain: loc.host || loc.hostname,

            heatmapEnabled: 'no',

            clickTimeout: 0,

            host: loc.host || loc.hostname,

            smtb: (function() {

                var smtb;

                smtb = _.findUrl(loc.href, "smt_b");

                if (smtb) {

                    return _.cookie(SMTBCOOKIENAME, smtb);

                }

            })(),

            params: {

                sid: '',

                uid: (function() {

                    var uid;

                    uid = null;

                    return function() {

                        if (uid) {

                            return uid;

                        }

                        return uid = _.uid(options.domain);

                    };

                })(),

                url: loc.href,

                tl: doc.title,

                cs: (doc.charset || doc.characterSet || '').toLowerCase(),

                rl: function() {

                    return doc.referrer;

                },

                fv: _.flashVersion(),

                sr: [win.screen.width, win.screen.height].join('x'),

                sc: screen.colorDepth,

                tz: (new Date()).getTimezoneOffset() / -60,

                je: (function() {

                    try {

                        if ("javaEnabled" in na && na.javaEnabled()) {

                            return 1;

                        } else {

                            return 0;

                        }

                    } catch (_error) {}

                    return 0;

                })(),

                sp: _.scrollTop,

                vh: _.viewHeight,

                pw: _.pageWidth,

                pt: _.pageLoadTime,

                vars: (function() {

                    var pos, ret, vars, x, _i, _len, _ref;

                    vars = _.cookie(VARSCOOKIENAME);

                    if (!vars) {

                        return undef;

                    }

                    _ref = vars.split('&');

                    for (_i = 0, _len = _ref.length; _i < _len; _i++) {

                        x = _ref[_i];

                        ret = x.split('=');

                        pos = _.intval(ret[0]);

                        if (pos < 1 || pos > 5) {

                            continue;

                        }

                        ret.push(1);

                        customVars.push(ret);

                    }

                    return _.customVars(customVars);

                })()

            }

        };

        params = options.params;

        pageview = function(path, title) {

            var keys, ret;

            keys = 'sid,uid,url,tl,cs,rl,fv,sr,sc,tz,je,sp,vh,pt,vars';

            if (path) {

                params.url = "" + loc.protocol + "//" + options.host + path;

            }

            if (title) {

                params.tl = title;

            }

            ret = pick('pageview', keys.split(','));

            return ret;

        };

        custom = function(category, action, label, value, brf) {

            var ret;

            if (brf == null) {

                brf = 0;

            }

            if (!category) {

                return;

            }

            ret = pick('event', ['sid', 'uid', 'url']);

            ret.params.cat = category;

            if (action) {

                ret.params.act = action;

            }

            if (label) {

                ret.params.lab = label;

            }

            if (value) {

                ret.params.val = value;

            }

            if (value) {

                ret.params.val = value;

            }

            ret.params.brf = brf ? 1 : 0;

            return ret;

        };

        clickEvent = function(mx, my) {

            var keys, ret;

            if (!options.heatmapEnabled) {

                return;

            }

            keys = 'sid,uid,url,pw';

            ret = pick('click', keys.split(','));

            ret.params.x = mx;

            ret.params.y = my;

            return ret;

        };

        pageClose = function(mx, my) {

            var keys, ret;

            keys = 'sid,uid,url,sp,vh';

            ret = pick('close', keys.split(','));

            return ret;

        };

        pick = function(type, keys) {

            return {

                send: true,

                protocol: options.isSSL === 'yes' ? 'https:' : 'http:',

                localPath: options.localPath,

                host: host(),

                path: '/p.gif',

                params: (function() {

                    var key, ret, val, value, _i, _len;

                    ret = {

                        type: type

                    };

                    for (_i = 0, _len = keys.length; _i < _len; _i++) {

                        key = keys[_i];

                        if (!(value = options.params[key])) {

                            continue;

                        }

                        val = _.isFunction(value) ? value() : value;

                        if (!_.isUndefined(val)) {

                            ret[key] = val;

                        }

                    }

                    ret._ = _.random();

                    return ret;

                })()

            };

        };

        host = function() {

            return "smtvip.admaster.com.cn";

        };

        version = function(fn) {

            if (typeof fn === "function") {

                fn(SM_VERSION);

            }

            return SM_VERSION;

        };

        _setSSL = function(value) {

            return options.isSSL = value === 'yes' ? 'yes' : 'no';

        };

        _setAccount = function(sid, startTime) {

            return options.siteId = options.params.sid = sid.toLowerCase();

        };

        _setDomainName = function(value) {

            if (value !== options.domain.substr(-value.length)) {

                return;

            }

            return options.domain = value;

        };

        _setHeatmapEnabled = function(value) {

            return options.heatmapEnabled = value === 'yes';

        };

        _setClickTimeOut = function(value) {

            return options.clickTimeout = Math.max(0, _.intval(value));

        };

        _setLocalPath = function(value) {

            return options.localPath = value;

        };

        _setCustomVar = function(pos, val, scope) {

            var onlyCookie, x, _has, _i, _len, _rewriteCookie;

            _rewriteCookie = _has = false;

            for (_i = 0, _len = customVars.length; _i < _len; _i++) {

                x = customVars[_i];

                if (+x[0] === +pos) {

                    _has = true;

                    _rewriteCookie = x[1] !== ec(val) || +x[2] !== +scope;

                    x[1] = ec(val);

                    x[2] = scope;

                }

            }

            if (_has === false) {

                customVars.push([pos, val, scope]);

            }

            _rewriteCookie = _has === false && +scope === 1;

            _.cookie.apply(null, [

                VARSCOOKIENAME, _.customVars(customVars, onlyCookie = true), {

                    domain: options.domain

                }

            ]);

            return options.params.vars = _.customVars(customVars);

        };

        _decodeSmt_b = function(cb) {

            var arr, smtb;

            if (!cb) {

                return console.log('decode smtb need cb');

            }

            smtb = _.cookie(SMTBCOOKIENAME);

            if (!smtb) {

                return cb(Error('Smtb not found'));

            }

            arr = _.decodeSmtb(smtb);

            if (!arr) {

                return cb(Error('Smtb is invalid'));

            }

            return cb(null, arr);

        };

        _encodeSmt_b = _.encodeSmtb;

        _setReferrerSmtEnabled = function(state) {

            var smtb;

            if (state === 'yes') {

                options.referrerSmtEnabled = true;

            } else {

                options.referrerSmtEnabled = false;

            }

            if (options.referrerSmtEnabled) {

                smtb = _.findUrl(loc.referrer, "smt_b");

                if (smtb) {

                    return _.cookie(SMTBCOOKIENAME, smtb, options.domain);

                }

            }

        };

        getOpt = function(key, cb) {

            if (cb) {

                cb(options[key]);

            }

            return options[key];

        };

        return {

            pageview: pageview,

            custom: custom,

            pageClose: pageClose,

            version: version,

            _setAccount: _setAccount,

            _setCustomVar: _setCustomVar,

            _setDomainName: _setDomainName,

            _setClickTimeOut: _setClickTimeOut,

            _setSSL: _setSSL,

            _setHeatmapEnabled: _setHeatmapEnabled,

            _setLocalPath: _setLocalPath,

            _decodeSmt_b: _decodeSmt_b,

            _encodeSmt_b: _encodeSmt_b,

            _setReferrerSmtEnabled: _setReferrerSmtEnabled,

            options: getOpt

        };

    };



}).call(this);


