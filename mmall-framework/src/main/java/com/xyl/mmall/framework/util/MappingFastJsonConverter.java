package com.xyl.mmall.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


public final class MappingFastJsonConverter extends
		AbstractHttpMessageConverter<Object> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	public boolean useGzip = false;
	
	private SerializerFeature[] serializerFeature;

	public boolean isUseGzip() {
		return useGzip;
	}

	public void setUseGzip(boolean useGzip) {
		this.useGzip = useGzip;
	}

	public SerializerFeature[] getSerializerFeature() {
		return serializerFeature;
	}

	public void setSerializerFeature(SerializerFeature[] serializerFeature) {
		this.serializerFeature = serializerFeature;
	}

	public MappingFastJsonConverter() {
		super(new MediaType("application", "json", DEFAULT_CHARSET));
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i;
		while ((i = inputMessage.getBody().read()) != -1) {
			baos.write(i);
		}
		return JSON.parseArray(baos.toString(), clazz);
	}

	@Override
	protected void writeInternal(Object o, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		String jsonString = JsonUtils.toJson(o);
		OutputStream out = outputMessage.getBody();
		byte[] b = jsonString.getBytes(DEFAULT_CHARSET);
		if(useGzip){
			b = GZIPCompressor.compress(b);
		}
		out.write(b);
		out.flush();
	}

}
