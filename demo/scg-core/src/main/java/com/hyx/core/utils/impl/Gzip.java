package com.hyx.core.utils.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.hyx.core.utils.Zip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class Gzip implements Zip {

	private final static Logger LOGGER = LoggerFactory.getLogger(Gzip.class);

	@Override
	public byte[] z(byte[] data) {
		ByteArrayOutputStream bos = null;
		GZIPOutputStream gzip = null;
		try {
			bos = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(bos);
			gzip.write(data);
			gzip.finish();
			return bos.toByteArray();
		} catch (Exception ex) {
			LOGGER.warn("", ex);
			return null;
		} finally {
			if (gzip != null)
				try {
					gzip.close();
				} catch (IOException e) {
					LOGGER.warn("", e);
				}
			if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					LOGGER.warn("", e);
				}
		}
	}

	@Override
	public byte[] un(byte[] data) {
		ByteArrayInputStream bis = null;
		GZIPInputStream gzip = null;
		ByteArrayOutputStream baos = null;
		try {
			bis = new ByteArrayInputStream(data);
			gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			return baos.toByteArray();
		} catch (Exception ex) {
			LOGGER.warn("", ex);
			return null;
		} finally {
			if (gzip != null)
				try {
					gzip.close();
				} catch (IOException e) {
					LOGGER.warn("", e);
				}
			if (baos != null)
				try {
					baos.close();
				} catch (IOException e) {
					LOGGER.warn("", e);
				}
			if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					LOGGER.warn("", e);
				}
		}
	}

}
