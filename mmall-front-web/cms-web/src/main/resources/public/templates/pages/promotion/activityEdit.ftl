<#assign pageName="promotion-activityEdit"/>
<#assign activityLabels = {
  "1": {"type":"1","name":"满减","place":"满减描述"},
  "2": {"type":"1","name":"包邮","place":"包邮描述"},
  "3": {"type":"1","name":"返券","place":"返券描述"},
  "4": {"type":"1","name":"红包","place":"红包描述"}
} />
<#if activityVO?? == false>
  <#assign activityVO = {} />
</#if>
<#assign allChecked=true/>
<#list activityVO.provinceList as province>
  <#if !(activityVO.provinceIds?? && activityVO.provinceIds?seq_contains(province.id))>
      <#assign allChecked=false/>
  </#if>
</#list>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl" />
  <style>
    .dropdown-menu-fix2{overflow:scroll;}
  </style>
<body>

  <@side />
  <#-- top and footer need match -->
  <@wrap>

  <@crumbs parent={"txt":"活动管理","url":'#'} sub={"txt":"活动新建"}/>

  <!-- card -->
  <div class="row">
    <div class="col-sm-12">
      <div class="m-card">
        <h2 class="card_b">
          <span class="glyphicon glyphicon-chevron-down pull-right"></span>
          基本信息
        </h2>
        <div class='card_c'>
          <form method="get" class="form-horizontal m-form-product j-baseform">
            <div class="form-group">
              <label class="col-sm-2 control-label">
                <span class="u-required">*</span>
                活动名称
              </label>
              <div class="col-sm-6">
                <input type="text" class="form-control" name="name" value="${activityVO.name!}" data-required=true data-max-length=70
                data-msg="活动名称格式错误"
                >
                <span class="help-block">35中文字符以内，显示在购物车等处</span>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label">
                <span class="u-required">*</span>
                活动描述
              </label>
              <div class="col-sm-6">
                <input type="text" 
                  data-required=true data-max-length=180
                  data-msg="活动描述格式错误"
                  class="form-control" name="description" value="${activityVO.description!}">
                <span class="help-block">90中文字符以内，显示在商品详情等处</span>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label"><span class="u-required">*</span>有效时间</label>
              <div class="col-sm-10 form-inline">
                <div style="display:inline;" id="form-startTime"></div>
                至
                <div style="display:inline;" id="form-endTime"></div>
              </div>
            </div>

            <div class="form-group">
              <label class="col-sm-2 control-label">标签描述</label>
              <#if activityVO.labelList??>
              <div class="col-sm-6 form-inline">
                <#list activityVO.labelList as label>
                <div class="row u-vmargin">
                  <div class='col-sm-12'>
                   <input type="checkbox" name="type-${label.type!}" 
                    <#if (label.select)!false == true>checked </#if>> ${label.name!} 
                   <input type="text" class='form-control' name="desc-${label.type!}" 
                     value="${label.desc!}"
                     placeholder="${(activityLabels[label.type?string].place)!}" 
                     data-max-length=25 
                     data-msg="${label.name!}不符合格式要求">
                    <input type="hidden" name="name-${label.type!}" value="${label.name!}">
                  </div>
                </div>
                </#list>
              </div>
              <#else>
              <div class="col-sm-6 form-inline">
                <#list 1..4 as num>
                	<#if num != 3>
		                <div class="row u-vmargin">
		                  <div class='col-sm-12'>
		                   <input type="checkbox" name="type-${num}"> ${(activityLabels[num?string].name)!} 
		                   <input type="text" class='form-control' name="desc-${num}" 
		                     placeholder="${(activityLabels[num?string].place)!}" 
		                     data-max-length=25 
		                     data-msg="${(activityLabels[num?string].name)!}不符合格式要求">
		                    <input type="hidden" name="name-${num}" value="${(activityLabels[num?string].name)!}">
		                  </div>
		                </div>
                	</#if>
                </#list>
              </div>
              </#if>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label"><span class="u-required">*</span>活动渠道</label>
                <div class="col-sm-3">
                  <label class="radio-inline">
                    <input type="radio" name="platformType" value="ALL" <#if (activityVO.platformType?? && activityVO.platformType =="ALL")|| !(activityVO.platformType??)>checked</#if>>全部</label>
                  <label class="radio-inline">
                    <input type="radio" name="platformType" value="MOBILE" <#if activityVO.platformType?? && activityVO.platformType =="MOBILE">checked</#if>>APP端</label>
                  <label class="radio-inline">
                    <input type="radio" name="platformType" value="WAP" <#if activityVO.platformType?? && activityVO.platformType =="WAP">checked</#if>>WAP端</label>
                  <label class="radio-inline">
                    <input type="radio" name="platformType" value="PC" <#if activityVO.platformType?? && activityVO.platformType =="PC">checked</#if>>WEB端</label>
                </div>
              </div>
              <div class="form-group" id="activitySite">
                <label class="col-sm-2 control-label"><span class="u-required">*</span>活动站点</label>
                <div class="col-sm-10">
                  <div class="col-sm-2" style="width:140px">
                    <div class="input-group input-group-sm">
                      <span class="input-group-addon">
                        <input type="checkbox" class="selectedAllPro" <#if allChecked>checked</#if>>
                      </span>
                      <span type="text" class="form-control" >全部站点</span>
                    </div><!-- /input-group -->
                  </div>
                  <#list activityVO.provinceList as province>
                  <div class="col-sm-2" style="width:140px">
                    <div class="input-group input-group-sm">
                      <span class="input-group-addon">
                        <input type="checkbox" value="${province.id!}" name="provinceIds"
                        <#if activityVO.provinceIds?? && activityVO.provinceIds?seq_contains(province.id)> checked </#if>
                        >
                      </span>
                      <span type="text" class="form-control" >${province.areaName!}</span>
                    </div><!-- /input-group -->
                  </div>
                  </#list>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label"><span class="u-required">*</span>活动类型</label>
                <div class="col-sm-3">
                  <label class="radio-inline">
                    <input type="radio" class="favorTypeRadio" name="favorType" value="0" <#if (activityVO.favorType?? && activityVO.favorType ==0)|| !(activityVO.favorType??)>checked</#if>>满xx金额优惠</label>
                  <label class="radio-inline">
                    <input type="radio" class="favorTypeRadio" name="favorType" value="1" <#if activityVO.favorType?? && activityVO.favorType ==1>checked</#if>>满xx件数优惠</label>
                </div>
              </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  <div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        条件和效果
      </h2>
      <div class='card_c j-items j-items-0<#if activityVO.favorType?? && activityVO.favorType ==1> f-dn</#if>'></div>
      <div class='card_c j-items j-items-1<#if (activityVO.favorType?? && activityVO.favorType ==0) || !(activityVO.favorType??)> f-dn</#if>'></div>
    </div>
  </div>
</div>

  <div class="row">
    <div class="col-sm-8 col-sm-offset-2">
      <div class="m-card-btn m-card">
        <#if editable==1><div class="cbtn_left"><a href="#" class="btn  btn-primary j-submit">保存</a></div></#if>
        <div class="cbtn_right" <#if editable!=1>style="width:100%;"</#if>><a href="/promotion/activity" class="btn  btn-default">返回</a></div>
      </div>
    </div>
  </div>
</@wrap>

<script>
  window.__data__ = ${JsonUtils.toJson(activityVO)}
</script>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/promotion/activityEdit.js?v=1.0.0.0"></script>

</body>
</html>