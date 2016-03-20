package com.xyl.mmall.oms.warehouse.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.dao.OmsOrderFormSkuDao;
import com.xyl.mmall.oms.dao.PickOrderDao;
import com.xyl.mmall.oms.dao.PickSkuDao;
import com.xyl.mmall.oms.dao.ShipOrderDao;
import com.xyl.mmall.oms.dao.ShipSkuDao;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.warehouse.adapter.EmsWarehouseAdapter;
import com.xyl.mmall.oms.warehouse.util.JsonLibUtils;

@Service("EmsCallSimulator")
public class EmsCallSimulator extends EmsWarehouseAdapter {
	@Autowired
	private OmsOrderFormDao omsOrderFormDao;

	@Autowired
	private OmsOrderFormSkuDao omsOrderFormSkuDao;

	@Autowired
	private PickOrderDao pickOrderDao;

	@Autowired
	private PickSkuDao pickOrderSkuDao;

	@Autowired
	private ShipOrderDao shipOrderDao;

	@Autowired
	private ShipSkuDao shipOrderSkuDao;

	private StringBuilder sb = new StringBuilder();

	private String title;

	private String url = "http://localhost:9094/oms/wms/ems";

	public EmsCallSimulator() {
		this.title = "模拟ems入库单确认回调";
	}

	public void initializeBody(String serviceName, Object data) {
		try {
			// 1. 将要发送的业务数据对象转换为xml
			String content = super.marshal(data);
			// 2. 组织发送数据
			Map<String, String> config = new HashMap<String, String>();
			config.put(KEY_CONSTANT_APPKEY, APPKEY);
			config.put(KEY_CONSTANT_SERVICE, serviceName);
			config.put(KEY_CONSTANT_INPUTCHARSET, getCharSet());

			config.put(KEY_CONSTANT_CONTENT, content);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			config.put(KEY_CONSTANT_NOTIFYTIME, format.format(new Date()));
			// 3. 计算签名，同时将签名添加到发送数据集合中
			String sign = genSignUTF8(content, KEYVALUE);
			config.put(KEY_CONSTANT_SIGN, sign);
			// 4. 发送post请求
			String result = super.doPost(config);
			String temp = JsonLibUtils.object2json(config);
			temp = replace('<' + "", "&lt;", temp);
			temp = replace('>' + "", "&gt;", temp);

			append("模拟发送内容：").append(temp).append("</br>");
			append("反馈结果：").append(result);
		} catch (Exception e) {
			append(ExceptionUtils.getFullStackTrace(e));
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	protected String getUrl() {
		return url;
	}

	public String toHTML() {
		return sb.toString();
	}

	private void reset() {
		sb = null;
		sb = new StringBuilder();
	}

	public final void doSimulator(String serviceName, Object data) {
		reset();
		initializeHead();
		initializeBody(serviceName, data);
		initializeFooter();
	}

	private void initializeHead() {
		append("<html><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /><head><title>").append(
				title).append("</title></head><body>");
	}

	private void initializeFooter() {
		append("</body></html>");
	}

	private EmsCallSimulator append(String str) {
		sb.append(str);
		return this;
	}
	
	@Override
	public WarehouseType getWarehouseType() {
		return WarehouseType.NULL;
	}

}
