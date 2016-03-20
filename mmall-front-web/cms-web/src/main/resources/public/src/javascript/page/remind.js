/**
 *cms 手机/邮箱绑定
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
    '{lib}base/util.js',// 基础工具  - _ut
    '{lib}base/event.js', // 事件处理 - _v
    '{lib}base/element.js',// 节点处理 - _e
    'util/form/form',// 表单处理 - _f
    '{pro}widget/module.js', // module处理 - Module
    'util/encode/json', // json对象 - JSON
    '{pro}components/notify/notify.js',// 提示浮层 - notify
    '{lib}util/ajax/xdr.js',// ajax - _j
    'pro/extend/util'// cms 工具 - _
], function (_ut, _v, _e, _f, Module, JSON, notify, _j, _, _p) {

    var _pro;

    // 验证手机号码
    var __regExpPhone = /^1\d{10}$/;

    // 验证邮箱
    var __regExpEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;

    _p._$$RemindModule = NEJ.C();
    _pro = _p._$$RemindModule._$extend(Module);

    // 初始化
    _pro.__init = function (_options) {

        this.__supInit(_options);

        // 表单获取
        this.__form1 = _f._$$WebForm._$allocate({form: _e._$get('savePhone')});
        this.__form2 = _f._$$WebForm._$allocate({form: _e._$get('saveEmail')});

        this.__getNodes();
        this.__addEvent();

        // 插入用户
        _e._$get('username').innerHTML = _._$getFullUserName();
    };

    // 节点获取
    _pro.__getNodes = function () {

        var list = _e._$getByClassName(document.body, 'ztag'), i = 0;

        // 更改绑定手机参数相关
        this.__credential = list[i++];
        this.__expiredTime = list[i++];
        this.__sign = list[i++];
        this.__newphone = list[i++];
        this.__yzBtn = list[i++];
        this.__yzm = list[i++];
        this.__saveBtn = list[i++];
        this.__mInfo = list[i++];
        this.__cancel =  _e._$get('cancelPhone');

        // 更改绑定邮箱参数相关
        this.__originEmail = list[i++];
        this.__eInfo = list[i++];

        // 修改绑定手机按钮
        this.__changeBtn1 = _e._$get('changePhone');

        // 修改绑定邮箱
        this.__changeBtn2 = _e._$get('changeEmail');

        // 绑定手机模块切换相关
        this.__originPhone = _e._$get('phone');
        this.__showPhoneTip = _e._$get('showPhone');
        this.__savePhoneTip = _e._$get('savePhone');

    };

    // 事件获取
    _pro.__addEvent = function () {

        // 绑定手机模块切换
        _v._$addEvent(this.__changeBtn1, 'click', this.__changePhoneTip._$bind(this));

        // 修改绑定手机相关
        _v._$addEvent(this.__yzBtn, 'click', this.__getYzm._$bind(this));
        _v._$addEvent(this.__saveBtn, 'click', this.__savePhone._$bind(this));
        _v._$addEvent(this.__cancel, 'click', this.__cancelPhone._$bind(this));

        // 修改绑定邮箱
        _v._$addEvent(this.__changeBtn2, 'click', this.__saveEmail._$bind(this));

    };

    // 绑定手机模块切换
    _pro.__changePhoneTip = function () {

        if (_e._$hasClassName(this.__savePhoneTip, 'f-dn')) {

            _e._$replaceClassName(this.__savePhoneTip, 'f-dn', 'f-db');
            _e._$replaceClassName(this.__showPhoneTip, 'f-db', 'f-dn');
        } else {

            _e._$replaceClassName(this.__savePhoneTip, 'f-db', 'f-dn');
            _e._$replaceClassName(this.__showPhoneTip, 'f-dn', 'f-db');
        }
    }

    // 获取验证码
    _pro.__getYzm = function (_event) {

        if (this.__sending) return;

        _v._$stop(_event);

        this.__sending = true;

        var __newphone = this.__newphone.value.trim(), __hasError = false, __error = '';

        if (!__newphone) {

            __hasError = true;
            __error = '手机号码不能为空';

        } else if (!__regExpPhone.test(__newphone)) {

            __hasError = true;
            __error = '请填写正确的手机号码';
        }

        this.__showMsg(__error, this.__mInfo);

        if (__hasError) {
            this.__sending = false;
            return;
        }

        this.__requestYzm(__newphone);

        _e._$addClassName(this.__yzBtn, 'disabled');

        this.__time = 60;

        this.__timer = setInterval(function () {

            this.__showLefTime(this.__time);

        }._$bind(this), 1000);

    }

    // 验证码倒计时
    _pro.__showLefTime = function (_leftTime) {

        if (_leftTime >= 10) {
            this.__yzBtn.innerText = '获取中(' + _leftTime + ')';
        } else {
            this.__yzBtn.innerText = '获取中(0' + _leftTime + ')';
        }

        this.__time = --_leftTime;

        if (this.__time < 0) {

            clearInterval(this.__timer);

            _e._$delClassName(this.__yzBtn, 'disabled');

            this.__yzBtn.innerText = '获取验证码';

            this.__sending = false;
        }
    };

    // 提交修改手机号
    _pro.__savePhone = function (_event) {

        if (this.__saveing) return;

        _v._$stop(_event);

        this.__saveing = true;

        var __newphone = this.__newphone.value.trim(), __yzm = this.__yzm.value.trim(), __hasError = false, __error = '';

        if (!__newphone) {

            __hasError = true;
            __error = '手机号码不能为空';

        } else if (!__regExpPhone.test(__newphone)) {

            __hasError = true;
            __error = '请填写正确的手机号码';

        } else if (!__yzm) {

            __hasError = true;
            __error = '验证码不能为空';
        }


        this.__showMsg(__error, this.__mInfo);

        if (__hasError) {
            this.__saveing = false;
            return;
        }

        if (!this.__credential.value || !this.__expiredTime.value || !this.__sign.value) {
            notify.showError('请先获取验证码！');
            this.__saveing = false;
            return;
        }

        this.__postPhone(this.__form1._$data());

    }
    
    //取消修改手机
    _pro.__cancelPhone = function (_event) {
    	clearInterval(this.__timer);
        _e._$delClassName(this.__yzBtn, 'disabled');
        this.__yzBtn.innerText = '获取验证码';
        this.__sending = false;

        this.__newphone.value = '';
        this.__yzm.value = '';
    }
    
    // 提交修改邮箱
    _pro.__saveEmail = function (_event) {

        if (this.__changing) return;

        _v._$stop(_event);

        this.__changing = true;

        var __newemail = this.__originEmail.value.trim(), __hasError = false, __error = '';

        if (!__newemail) {

            __hasError = true;
            __error = '邮箱不能为空';

        } else if (!__regExpEmail.test(__newemail)) {

            __hasError = true;
            __error = '请填写正确的邮箱地址';
        }

        this.__showMsg(__error, this.__eInfo);

        if (__hasError) {
            this.__changing = false;
            return;
        }

        this.__postEmail(this.__form2._$data());

    }

    // 请求获取验证信息
    _pro.__requestYzm = function (_phonenum) {

        _j._$request('/index/remind/getVerifCode', {
            headers: {"Content-Type": "application/json;charset=UTF-8"},
            type: 'json',
            method: 'POST',
            data: JSON.stringify({phone: _phonenum}),
            onload: function (_json) {

                if (_json.code == 200) {

                    this.__credential.value = _json.result.credential;
                    this.__expiredTime.value = _json.result.expiredTime;
                    this.__sign.value = _json.result.sign;

                    notify.showError('验证码已发送！');
                } else {

                    notify.showError('验证码发送失败！');
                }
            }._$bind(this),

            onerror: function (_error) {

                notify.showError('验证码发送失败，请刷新页面重试！');
            }
        });
    };

    // 提交新的号码
    _pro.__postPhone = function (_data) {

        _j._$request('/index/remind/savePhonenum', {
            headers: {"Content-Type": "application/json;charset=UTF-8"},
            type: 'json',
            method: 'POST',
            data: JSON.stringify(_data),
            onload: function (_json) {

                if (_json.code == '200') {

                    notify.show('手机号码保存成功！');

                    this.__originPhone.innerHTML = '手机号：' + _data.newphone;
                    this.__changePhoneTip();

                    clearInterval(this.__timer);
                    _e._$delClassName(this.__yzBtn, 'disabled');
                    this.__yzBtn.innerText = '获取验证码';
                    this.__sending = false;

                    this.__newphone.value = '';
                    this.__yzm.value = '';

                } else {

                    notify.showError('手机号码保存失败！');
                }
            }._$bind(this),
            onerror: function (_error) {

                notify.showError('手机号码绑定失败，请刷新页面重试！');
            }
        });

        this.__saveing = false;
    }

    // 提交新的号码
    _pro.__postEmail = function (_data) {

        _j._$request('/index/remind/saveEmail', {
            headers: {"Content-Type": "application/json;charset=UTF-8"},
            type: 'json',
            method: 'POST',
            data: JSON.stringify(_data),
            onload: function (_json) {

                if (_json.code == '200') {

                    this.__showMsg('邮箱保存成功！', this.__eInfo);

                    this.__originEmail.value = '';
                    this.__originEmail.placeholder = _data.email;

                    setTimeout(function () {

                        this.__showMsg('', this.__eInfo);

                    }._$bind(this), 1000);

                } else {

                    notify.showError('邮箱保存失败！');
                }
            }._$bind(this),
            onerror: function (_error) {

                notify.showError('邮箱绑定失败，请刷新页面重试！');
            }
        });

        this.__changing = false;
    }

    // 信息提示
    _pro.__showMsg = function (_msg, _msgWrap) {
        var __html = '';

        __html = '<p class="w-msg">' + _msg + '</p>';

        _msgWrap.innerHTML = __html;

        if (!!__html) {

            _msgWrap.style.display = '';
        } else {

            _msgWrap.style.display = 'none';
        }
    };

    _p._$$RemindModule._$allocate();
});