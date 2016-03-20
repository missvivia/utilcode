package com.xyl.mmall.framework.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * @author jiangww
 *
 */
public class GZIPCompressor {
	private static Logger logger = LoggerFactory.getLogger(GZIPCompressor.class);
	
    public static byte[] compress(byte[] content) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(content);
            gzipOutputStream.close();
        } catch (IOException e) {
        	logger.error("gzip compress error",e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] decompress(byte[] contentBytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            IOUtils.copy(new GZIPInputStream(new ByteArrayInputStream(contentBytes)), out);
        } catch (IOException e) {
        	logger.error("gzip decompress error",e);
        }
        return out.toByteArray();
    }
}