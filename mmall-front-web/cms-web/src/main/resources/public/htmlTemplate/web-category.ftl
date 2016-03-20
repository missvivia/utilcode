<div class="category-wrap">
	<div class="category-side">
		<ul>
			<#if categoryList?? >
				<#list categoryList as category>
					<#--class="itme${category_index + 1}"-->
			        <li>
				        <i style="background: url('${category.iconUrl}') no-repeat center center;"></i>
				        <span>
				        	<a href="${category.url}">${category.name}</a>
				        </span>
				        <em></em>
			        </li>
			    </#list>
			</#if>
		</ul>
	</div>
	<div class="category-nav">
		<ul>
			<#if categoryList?? >
				<#list categoryList as category>
				   <li>
						<#if category.subCategoryContentVOs?? >
							
								<#list category.subCategoryContentVOs as secondCategory>
									<dl>
										<dt>
											<a href="${secondCategory.url}">${secondCategory.name}</a>
										</dt>
										<#if secondCategory.subCategoryContentVOs?? >
											<#list secondCategory.subCategoryContentVOs as thirdCategory>
												<dd>
													<a href="${thirdCategory.url}" >${thirdCategory.name}</a>
												</dd>
										   </#list>
									   </#if>
									</dl>
								</#list>
						</#if>
					</li>
				 </#list>
			</#if>
		</ul>
	</div>
</div>
