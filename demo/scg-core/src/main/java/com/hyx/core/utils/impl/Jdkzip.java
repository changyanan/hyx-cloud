package com.hyx.core.utils.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.hyx.core.utils.Zip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Jdkzip implements Zip {

	private final static Logger LOGGER = LoggerFactory.getLogger(Jdkzip.class);

	@Override
	public byte[] z(byte[] data) {
		ByteArrayOutputStream bos = null;
		ZipOutputStream zip = null;
		try {
			bos = new ByteArrayOutputStream();
			zip = new ZipOutputStream(bos);
			ZipEntry entry = new ZipEntry("zip");
			entry.setSize(data.length);
			zip.putNextEntry(entry);
			zip.write(data);
			zip.closeEntry();
			return bos.toByteArray();
		} catch (Exception ex) {
			LOGGER.warn("", ex);
			return null;
		} finally {
			if (zip != null)
				try {
					zip.close();
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
		ZipInputStream zip = null;
		ByteArrayOutputStream baos = null;
		try {
			bis = new ByteArrayInputStream(data);
			zip = new ZipInputStream(bis);
			baos = new ByteArrayOutputStream();
			while (zip.getNextEntry() != null) {
				byte[] buf = new byte[1024];
				int num = -1;
				while ((num = zip.read(buf, 0, buf.length)) != -1) {
					baos.write(buf, 0, num);
				}
				return baos.toByteArray();
			}
			return null;
		} catch (Exception ex) {
			LOGGER.warn("", ex);
			return null;
		} finally {
			if (zip != null)
				try {
					zip.close();
				} catch (IOException e) {
					LOGGER.warn("", e);
				}
			if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					LOGGER.warn("", e);
				}
			if (baos != null)
				try {
					baos.close();
				} catch (IOException e) {
					LOGGER.warn("", e);
				}

		}
	}

}
