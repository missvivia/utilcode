package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.List;

/**
 * poSku搜索类
 * 
 * @author hzzhaozhenzuo
 *
 */
public class PoProductSo implements Serializable {

	private static final long serialVersionUID = 1L;

	//用户选择的站点条件
	private String siteFlagsUserSelect; // 站点id，目前只能与档期表联表查

	private Long poId;// poSku表中有

	private Long supplierId;

	private Long brandId;

	private String goodsNo;// poSku中存在

	private String barCode;// 可以去除

	private Long stime;// poSku表中存在

	private Long etime; // poSku表中存在

	private Integer status; // poSku表中存在，待审核状态为2

	private Integer offset;// 起始位置

	private Integer limit;// 结束位置
	
	private List<Long> poIdList;
	
	private List<Long> productIdList;
	
	private List<Long> productIdListFromGoodsNo;
	
	//用户拥有的权限条件，二进制表示
	private String siteFlagsUserAuth;
	
	private Long productId;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getSiteFlagsUserSelect() {
		return siteFlagsUserSelect;
	}

	public void setSiteFlagsUserSelect(String siteFlagsUserSelect) {
		this.siteFlagsUserSelect = siteFlagsUserSelect;
	}

	public Long getPoId() {
		return poId;
	}

	public void setPoId(Long poId) {
		this.poId = poId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Long getStime() {
		return stime;
	}

	public void setStime(Long stime) {
		this.stime = stime;
	}

	public Long getEtime() {
		return etime;
	}

	public void setEtime(Long etime) {
		this.etime = etime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public List<Long> getPoIdList() {
		return poIdList;
	}

	public void setPoIdList(List<Long> poIdList) {
		this.poIdList = poIdList;
	}

	public String getSiteFlagsUserAuth() {
		return siteFlagsUserAuth;
	}

	public void setSiteFlagsUserAuth(String siteFlagsUserAuth) {
		this.siteFlagsUserAuth = siteFlagsUserAuth;
	}

	public List<Long> getProductIdList() {
		return productIdList;
	}

	public void setProductIdList(List<Long> productIdList) {
		this.productIdList = productIdList;
	}

	public List<Long> getProductIdListFromGoodsNo() {
		return productIdListFromGoodsNo;
	}

	public void setProductIdListFromGoodsNo(List<Long> productIdListFromGoodsNo) {
		this.productIdListFromGoodsNo = productIdListFromGoodsNo;
	}
	
}