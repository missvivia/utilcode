/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.ems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * @author hzzengchengyuan
 *
 */
@XmlRootElement(name = "RequestItem")
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(WMSSkuDetailDTO.class)
public class WmsOmsGoodsUpdate {
	/**
	 * 项目ID
	 */
	private long prjct_id;

	/**
	 * 货品编号
	 */
	@MapField(WMSSkuDetailDTO.field_skuId)
	private String goods_no;

	/**
	 * 货品名称
	 */
	@MapField(WMSSkuDetailDTO.field_name)
	private String goods_name;

	/**
	 * 货品条码
	 */
	@MapField(WMSSkuDetailDTO.field_artNo)
	private String bar_code;

	/**
	 * 货主编号
	 */
	private String consignorid;

	/**
	 * 货主名称
	 */
	private String consignorname;

	/**
	 * 版本号
	 */
	private long version;

	/**
	 * 货品英文名
	 */
	private String goods_en_name;

	/**
	 * 货品简称
	 */
	private String goods_abbr;

	/**
	 * 货品类别编号
	 */
	private String goods_type_id;

	/**
	 * 货品类别名称
	 */
	private String cargotypename;

	/**
	 * 货品基本单位
	 */
	@MapField(WMSSkuDetailDTO.field_unit)
	private String base_unit = "件";

	/**
	 * 货品SPU号
	 */
	private String spu;

	/**
	 * 检索号
	 */
	private String queryindex;

	/**
	 * 保质期
	 */
	private long keep_days;

	/**
	 * ABC分类
	 */
	private String abc_type;

	/**
	 * 颜色
	 */
	@MapField(WMSSkuDetailDTO.field_color)
	private String color;

	/**
	 * 规格
	 */
	@MapField(WMSSkuDetailDTO.field_size)
	private String specs;

	/**
	 * 大小 （1小件2中件 3大件）
	 */
	private String size_specs;

	/**
	 * 固液气属性 1固体 2液体 3气体 9其它
	 */
	private String slg_type;

	/**
	 * 长
	 */
	@MapField(WMSSkuDetailDTO.field_length)
	private double len;

	@MapField(WMSSkuDetailDTO.field_width)
	private double width;

	@MapField(WMSSkuDetailDTO.field_height)
	private double height;

	private double volume;

	/**
	 * 重量/毛重
	 */
	@MapField(WMSSkuDetailDTO.field_weight)
	private double weight;

	private double net;

	/**
	 * 最高库存预警数量
	 */
	private long max_alert_amount;

	/**
	 * 最低库存预警数量
	 */
	private long min_alert_amount;

	/**
	 * 是否序号管理
	 */
	private long serial_mgr;

	/**
	 * 是否为BOM组合品
	 */
	private String isbom;

	/**
	 * 是否IMEI号管理 1是 0否
	 */
	private String is_imei;

	/**
	 * 是否赠品 1是 0否
	 */
	private String isgift;

	/**
	 * 【是否】为新品 1是 0否
	 */
	private String is_new;

	/**
	 * @return the prjct_id
	 */
	public long getPrjct_id() {
		return prjct_id;
	}

	/**
	 * @param prjct_id
	 *            the prjct_id to set
	 */
	public void setPrjct_id(long prjct_id) {
		this.prjct_id = prjct_id;
	}

	/**
	 * @return the goods_no
	 */
	public String getGoods_no() {
		return goods_no;
	}

	/**
	 * @param goods_no
	 *            the goods_no to set
	 */
	public void setGoods_no(String goods_no) {
		this.goods_no = goods_no;
	}

	/**
	 * @return the goods_name
	 */
	public String getGoods_name() {
		return goods_name;
	}

	/**
	 * @param goods_name
	 *            the goods_name to set
	 */
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	/**
	 * @return the bar_code
	 */
	public String getBar_code() {
		return bar_code;
	}

	/**
	 * @param bar_code
	 *            the bar_code to set
	 */
	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}

	/**
	 * @return the consignorid
	 */
	public String getConsignorid() {
		return consignorid;
	}

	/**
	 * @param consignorid
	 *            the consignorid to set
	 */
	public void setConsignorid(String consignorid) {
		this.consignorid = consignorid;
	}

	/**
	 * @return the consignorname
	 */
	public String getConsignorname() {
		return consignorname;
	}

	/**
	 * @param consignorname
	 *            the consignorname to set
	 */
	public void setConsignorname(String consignorname) {
		this.consignorname = consignorname;
	}

	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * @return the goods_en_name
	 */
	public String getGoods_en_name() {
		return goods_en_name;
	}

	/**
	 * @param goods_en_name
	 *            the goods_en_name to set
	 */
	public void setGoods_en_name(String goods_en_name) {
		this.goods_en_name = goods_en_name;
	}

	/**
	 * @return the goods_abbr
	 */
	public String getGoods_abbr() {
		return goods_abbr;
	}

	/**
	 * @param goods_abbr
	 *            the goods_abbr to set
	 */
	public void setGoods_abbr(String goods_abbr) {
		this.goods_abbr = goods_abbr;
	}

	/**
	 * @return the goods_type_id
	 */
	public String getGoods_type_id() {
		return goods_type_id;
	}

	/**
	 * @param goods_type_id
	 *            the goods_type_id to set
	 */
	public void setGoods_type_id(String goods_type_id) {
		this.goods_type_id = goods_type_id;
	}

	/**
	 * @return the cargotypename
	 */
	public String getCargotypename() {
		return cargotypename;
	}

	/**
	 * @param cargotypename
	 *            the cargotypename to set
	 */
	public void setCargotypename(String cargotypename) {
		this.cargotypename = cargotypename;
	}

	/**
	 * @return the base_unit
	 */
	public String getBase_unit() {
		return base_unit;
	}

	/**
	 * @param base_unit
	 *            the base_unit to set
	 */
	public void setBase_unit(String base_unit) {
		this.base_unit = base_unit;
	}

	/**
	 * @return the spu
	 */
	public String getSpu() {
		return spu;
	}

	/**
	 * @param spu
	 *            the spu to set
	 */
	public void setSpu(String spu) {
		this.spu = spu;
	}

	/**
	 * @return the queryindex
	 */
	public String getQueryindex() {
		return queryindex;
	}

	/**
	 * @param queryindex
	 *            the queryindex to set
	 */
	public void setQueryindex(String queryindex) {
		this.queryindex = queryindex;
	}

	/**
	 * @return the keep_days
	 */
	public long getKeep_days() {
		return keep_days;
	}

	/**
	 * @param keep_days
	 *            the keep_days to set
	 */
	public void setKeep_days(long keep_days) {
		this.keep_days = keep_days;
	}

	/**
	 * @return the abc_type
	 */
	public String getAbc_type() {
		return abc_type;
	}

	/**
	 * @param abc_type
	 *            the abc_type to set
	 */
	public void setAbc_type(String abc_type) {
		this.abc_type = abc_type;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the specs
	 */
	public String getSpecs() {
		return specs;
	}

	/**
	 * @param specs
	 *            the specs to set
	 */
	public void setSpecs(String specs) {
		this.specs = specs;
	}

	/**
	 * @return the size_specs
	 */
	public String getSize_specs() {
		return size_specs;
	}

	/**
	 * @param size_specs
	 *            the size_specs to set
	 */
	public void setSize_specs(String size_specs) {
		this.size_specs = size_specs;
	}

	/**
	 * @return the slg_type
	 */
	public String getSlg_type() {
		return slg_type;
	}

	/**
	 * @param slg_type
	 *            the slg_type to set
	 */
	public void setSlg_type(String slg_type) {
		this.slg_type = slg_type;
	}

	/**
	 * @return the len
	 */
	public double getLen() {
		return len;
	}

	/**
	 * @param len
	 *            the len to set
	 */
	public void setLen(double len) {
		this.len = len;
	}

	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * @return the volume
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @return the net
	 */
	public double getNet() {
		return net;
	}

	/**
	 * @param net
	 *            the net to set
	 */
	public void setNet(double net) {
		this.net = net;
	}

	/**
	 * @return the max_alert_amount
	 */
	public long getMax_alert_amount() {
		return max_alert_amount;
	}

	/**
	 * @param max_alert_amount
	 *            the max_alert_amount to set
	 */
	public void setMax_alert_amount(long max_alert_amount) {
		this.max_alert_amount = max_alert_amount;
	}

	/**
	 * @return the min_alert_amount
	 */
	public long getMin_alert_amount() {
		return min_alert_amount;
	}

	/**
	 * @param min_alert_amount
	 *            the min_alert_amount to set
	 */
	public void setMin_alert_amount(long min_alert_amount) {
		this.min_alert_amount = min_alert_amount;
	}

	/**
	 * @return the serial_mgr
	 */
	public long getSerial_mgr() {
		return serial_mgr;
	}

	/**
	 * @param serial_mgr
	 *            the serial_mgr to set
	 */
	public void setSerial_mgr(long serial_mgr) {
		this.serial_mgr = serial_mgr;
	}

	/**
	 * @return the isbom
	 */
	public String getIsbom() {
		return isbom;
	}

	/**
	 * @param isbom
	 *            the isbom to set
	 */
	public void setIsbom(String isbom) {
		this.isbom = isbom;
	}

	/**
	 * @return the is_imei
	 */
	public String getIs_imei() {
		return is_imei;
	}

	/**
	 * @param is_imei
	 *            the is_imei to set
	 */
	public void setIs_imei(String is_imei) {
		this.is_imei = is_imei;
	}

	/**
	 * @return the isgift
	 */
	public String getIsgift() {
		return isgift;
	}

	/**
	 * @param isgift
	 *            the isgift to set
	 */
	public void setIsgift(String isgift) {
		this.isgift = isgift;
	}

	/**
	 * @return the is_new
	 */
	public String getIs_new() {
		return is_new;
	}

	/**
	 * @param is_new
	 *            the is_new to set
	 */
	public void setIs_new(String is_new) {
		this.is_new = is_new;
	}

}
