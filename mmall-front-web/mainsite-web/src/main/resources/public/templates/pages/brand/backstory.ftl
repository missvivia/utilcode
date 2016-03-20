<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
    <html>
    <head>
        <#include "../../wrap/common.ftl">
        <@title content="品牌故事"/>
        <meta charset="utf-8"/>
        <@css/>
        <!-- @STYLE -->
        <link rel="stylesheet" type="text/css" href="/src/css/page/brandIntro.css">
    </head>
    <body id="index-netease-com">

        <@topbar/>
        <@sidebar/>
        <@navbar index=0/>
        <@module>
        <div class="m-crumbs">
            <a href="/index">首页</a><span>&gt;</span><a href="/brand/settle">入驻品牌</a><span>&gt;</span><span
                class="selected">${data.basic.brandName}</span>
        </div>
        <div class="g-bd">

            <div class="m-box">
                <div class="m-story padbox">
                    <div class="partl">
        <span class="u-brandlogo">
          <img src="${data.basic.logoUrl}" width="153px" height="70px"/>
          <span class="name">${data.basic.brandName}</span>
        </span>
                    </div>
                    <div class="partr">
                        <h3>品 牌 故 事</h3>

                        <p>
                        ${data.basic.intro}
                        </p>
                    </div>
                </div>

                <div id="maxImages"></div>


                <div class="m-winshow padbox">
                    <div class="u-tt">
                        <h2>品牌橱窗</h2>
                    </div>
                    <div class="list">
                        <ul class="f-cb">
                            <#list data.basic.fixImages as item>
                                <li class="item">
                                    <img src="${item.src}" width="300px" height="300px"/>

                                    <h3>${item.desc}</h3>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </div>



            </div>
            <div class="m-box m-box-1" id="m-box-map">
                <!-- <div>此处为百度地图部分</div> -->
                <div class="m-map" id="map">
                </div>
            </div>
            <!--end of box-->

        </div>



        <#-- Page Content Here -->

        </@module>
        <@footer/>


    <!-- @NOPARSE -->
    <script type="text/javascript">
            <#noescape>
            var g_return = ${JsonUtils.toJson(data)};
            </#noescape>
    </script>
    <!-- /@NOPARSE -->

        <@baiduMap/>

        <#noparse>
        <!-- @SCRIPT -->
        </#noparse>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/brand/backstory.js"></script>
    </body>
    </html>
    </@compress>
</#escape>