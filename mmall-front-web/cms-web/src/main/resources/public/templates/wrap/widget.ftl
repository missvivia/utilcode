<div style="display:none" id="wgt-tpl">
<#noparse>
	<textarea name="ntp" id="ntp-sure-window">
		<div class="m-movewin">
			<div class="wincnt f-tac">
				<div class="j-flag"></div>
			</div>
			<div class="f-tac winbnts"><span class="btn btn-primary j-flag">确定</span><span class="f-mgl btn btn-primary j-flag">取消</span></div>
		</div>
	</textarea>
  <textarea name="ntp" id="ntp-pass-win">
    <div class="m-winform">
      <div class="line">
        <div>确定执行所选操作</div>
        <div class="f-fc1">一旦确定，将无法恢复！</div>
      </div>
    </div>
    <div class="m-winbot">
      <div class="btns">
        <span class="btn btn-primary j-flag">确定</span>
        <span class="f-mgl btn btn-primary j-flag">取消</span>
      </div>
    </div>
  </textarea>
	<textarea name="ntp" id="ntp-cancel-order-win">
    <div class="m-winform">
      <form>
        <div class="line">
          <div>请选择订单退款方式，并确认取消订单</div>
          <div class="f-fc1">一旦取消，用户订单将无法恢复！</div>
        </div>
        <div class="line f-cb">
          <input type="radio" name="return" checked="checked" id="original" class="f-fl">
          <label class="f-mgl" for="original">订单金额原路退回</label>
        </div>
        <div class="line f-cb">
          <input type="radio" name="return" id="epay" class="f-fl">
          <label class="f-mgl" for="epay">退回网易余额宝</label>
        </div>
      </form>
    </div>
    <div class="m-winbot">
      <div class="btns">
        <span class="btn btn-primary j-flag">确定</span>
        <span class="f-mgl btn btn-primary j-flag">取消</span>
      </div>
    </div>
  </textarea>
  <textarea name="ntp" id="ntp-order-reject-win">
    <div class="m-winform">
      <form id="rejectForm">
        <div class="line">
          <div>拒绝以后将结束用户的申请，建议你与用户沟通以后再执行拒绝操作。</div>
          <div class="f-fc1">你确定要拒绝该申请吗？</div>
        </div>
        <div class="line f-cb">
          <label class="">操作说明</label>
          <div>
            <textarea class="u-textarea" data-required="true" name="extInfo"><&#47;textarea>
          </div>
        </div>
      </form>
    </div>
    <div class="m-winbot">
      <div class="btns">
        <span class="btn btn-primary j-flag">确定</span>
        <span class="f-mgl btn btn-primary j-flag">取消</span>
      </div>
    </div>
  </textarea>
</#noparse>
<div>