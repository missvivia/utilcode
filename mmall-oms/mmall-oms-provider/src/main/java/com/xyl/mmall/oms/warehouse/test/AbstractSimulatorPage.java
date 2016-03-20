package com.xyl.mmall.oms.warehouse.test;

public abstract class AbstractSimulatorPage implements SimulatorPage {

	private StringBuilder sb = new StringBuilder(2046);

	private String title;

	private String baseUrl = "/omsutil/ems";

	public AbstractSimulatorPage(String title) {
		this.title = title;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getServiceUrl() {
		return baseUrl + (serviceName().startsWith("/") ? "" : "/") + serviceName();
	}

	public String getTitle() {
		return this.title;
	}

	@Override
	public final SimulatorPage initialize(Object data) {
		reset();
		initializeHead();
		initializeBody(data);
		initializeFooter();
		return this;
	}

	private void initializeHead() {
		append("<html><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /><head><title>").append(
				getTitle()).append("</title></head><body>");
	}

	protected abstract void initializeBody(Object data);

	private void initializeFooter() {
		append("</body></html>");
	}

	private void reset() {
		sb = null;
		sb = new StringBuilder();
	}

	public String toHTML() {
		return sb.toString();
	}

	protected AbstractSimulatorPage appendRowInput(String... inputDetail) {
		openTr();
		String text = null, inputName = null, defaultValue = null;
		for (int i = 0; i < inputDetail.length; i++) {
			if ((i + 1) % 3 == 0) {
				defaultValue = inputDetail[i];
				appendOneInputTd(text, inputName, defaultValue);
			} else if ((i + 1) % 2 == 0) {
				inputName = inputDetail[i];
			} else {
				text = inputDetail[i];
			}
		}
		closeTr();
		return this;
	}

	protected AbstractSimulatorPage appendOption(String selectName, String... options) {
		append("<select ").append("name=\"").append(selectName).append("\">");
		if (options != null) {
			String value = "", label = "";
			for (int i = 0; i < options.length; i++) {
				if ((i + 1) % 2 == 0) {
					label = options[i];
					append("<option value =\"").append(value).append("\">").append(label).append("</option>");
					value = "";
					label = "";
				} else {
					value = options[i];
				}
			}
		}
		append("</select>");
		return this;
	}

	protected AbstractSimulatorPage appendRow(String... texts) {
		openTr();
		for (String text : texts) {
			openTd().append(text).closeTd();
		}
		closeTr();
		return this;
	}

	protected AbstractSimulatorPage appendRow(String text, int num) {
		openTr();
		appendTd(text, num);
		closeTr();
		return this;
	}

	private AbstractSimulatorPage appendOneInputTd(String text, String inputName, String defaultValue) {
		appendTd(text);
		appendTd("<input type=\"text\" name=\"".concat(inputName).concat(
				"\" value=\"".concat(defaultValue).concat("\"/>")));
		return this;
	}

	protected AbstractSimulatorPage appendTd(String text) {
		openTd();
		append(text);
		closeTd();
		return this;
	}

	protected AbstractSimulatorPage appendTd(String text, int num) {
		openTd(num);
		append(text);
		closeTd();
		return this;
	}

	protected AbstractSimulatorPage openTr() {
		append("<tr>");
		return this;
	}

	protected AbstractSimulatorPage closeTr() {
		append("</tr>");
		return this;
	}

	protected AbstractSimulatorPage openTd() {
		append("<td>");
		return this;
	}

	protected AbstractSimulatorPage openTd(int num) {
		append("<td colspan=\"2\">");
		return this;
	}

	protected AbstractSimulatorPage closeTd() {
		append("</td>");
		return this;
	}

	protected AbstractSimulatorPage openForm(String action) {
		append("<form method=\"post\" target=\"_blank\" action=\"").append(action).append("\">");
		append("<button type=\"submit\" >OK</button>");
		return this;
	}

	protected AbstractSimulatorPage openForm(String action, String method) {
		append("<form method=\"").append(method).append("\" target=\"_self\" action=\"").append(action).append("\">");
		append("<button type=\"submit\" >OK</button>");
		return this;
	}

	protected AbstractSimulatorPage closeForm() {
		append("</form>");
		return this;
	}

	protected AbstractSimulatorPage openTable(String... tableHeads) {
		append("<table style=\"padding:5px;\" >");
		if (tableHeads != null && tableHeads.length > 0) {
			append("<tr>");
			for (String head : tableHeads) {
				append("<th>").append(head).append("</th>");
			}
			append("</tr>");
		}
		return this;
	}

	/**
	 * @param style
	 *            0:无框, 1:有框
	 * @param tableHeads
	 */
	protected AbstractSimulatorPage openTable(int style, String... tableHeads) {
		append("<table ");
		if (style == 0) {
			append("border=\"0\" ");
		} else {
			append("border=\"1\" ");
		}
		append("cellpadding=\"0\" cellspacing=\"0\" style=\"margin:5px;\">");
		if (tableHeads != null && tableHeads.length > 0) {
			append("<tr>");
			for (String head : tableHeads) {
				append("<th>").append(head).append("</th>");
			}
			append("</tr>");
		}
		return this;
	}

	protected AbstractSimulatorPage closeTable() {
		append("</table></br>");
		return this;
	}

	protected AbstractSimulatorPage append(String str) {
		sb.append(str);
		return this;
	}
}
