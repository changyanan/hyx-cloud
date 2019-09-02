package com.hyx.core.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public abstract class ExcelPoi {

	private static final Logger log = LoggerFactory.getLogger(ExcelPoi.class);

	@FunctionalInterface
	public interface ReadDataCell<E> {
		void xlh(final Row row, final int rownum, final String sheetName);
	}

	@FunctionalInterface
	public interface DataCell<E> {
		void xlh(final Row row, final E e, final int index, final int sheetid);
	}

	@FunctionalInterface
	public interface DataList<E> {
		Iterable<E> getList(final int index, final int sheetid);
	}

	private final static int maxrow = 8000;

	public static <T> void exPort(final File outputfile, final String sheetname, final int startRow, final DataList<T> dataList, final DataCell<T> dataCell) throws IOException {
		File parentFile = outputfile.getParentFile();
		if(parentFile!=null&&!parentFile.exists()){
			parentFile.mkdirs();
		}
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(outputfile);
			boolean isExecl2003 = outputfile.getName().endsWith(".xls");
			exPort(outputStream, sheetname, startRow, isExecl2003, dataList, dataCell);
		} finally {
			if (outputStream != null)
				outputStream.close();
		}
	}

	public static <T> void exPort(final OutputStream outputStream, final String sheetname, final int startRow, final boolean isExecl2003, final DataList<T> dataList, final DataCell<T> dataCell) throws IOException {
		Workbook wb = null;
		try {
			wb = exPort(sheetname, startRow, isExecl2003, dataList, dataCell);
			ExcelPoi.save(wb, outputStream);
		} finally {
			if (wb != null)
				wb.close();
		}
	}

	public static <T> Workbook exPort(final String sheetname, final int startRow, final boolean isExecl2003, final DataList<T> dataList, final DataCell<T> dataCell) throws IOException {
		Workbook workbook;
		if (isExecl2003)
			workbook = new HSSFWorkbook();
		else
			workbook = new SXSSFWorkbook(40960);
		int pageid = 0;
		int index = 0;
		while (true) {
			Integer rowid;
			if ((rowid = nextPage(index, startRow, pageid++, sheetname, workbook, dataList, dataCell)) == null) {
				break;
			}
			index += rowid;
		}
		return workbook;
	}

	private static <T> Integer nextPage(final int index, final int startRow, final int pageid, final String sheetname, final Workbook workbook, final DataList<T> dataList, final DataCell<T> dataCell) {
		Sheet sheet = null;
		int rowid = startRow;
		while (true) {
			Iterable<T> it = dataList.getList(index + rowid - startRow, pageid);
			if (it == null||!it.iterator().hasNext()){
				log.debug("导出数据完毕....");
				return null;
			}
			if (sheet == null)
				sheet = workbook.createSheet(sheetname + " (" + pageid + ")");
			for (T t : it) {
				Row row = sheet.createRow(rowid);
				dataCell.xlh(row, t, index - startRow + rowid++, pageid);
			}
			if (maxrow <= rowid)
				return rowid - startRow;
		}
	}

	public static int writeRow(final Row row, final int startCell, final Map<String, Object> data, final String... cellNames) {
		int num = 0;
		for (int i = 0; i < cellNames.length; i++) {
			Cell cell = row.getCell(i + startCell);
			if (cell == null)
				cell = row.createCell(i + startCell);
			boolean bool = ExcelPoi.writeCell(cell, data.get(cellNames[i]));
			if (bool)
				num++;
		}
		return num;
	}

	public static boolean writeCell(final Cell cell, final Object data) {
		if (data == null)
			return false;
		else if (data instanceof Date)
			cell.setCellValue((Date) data);
		else if (data instanceof Number)
			cell.setCellValue(((Number) data).doubleValue());
		else if (data instanceof Calendar)
			cell.setCellValue((Calendar) data);
		else if (data instanceof Byte)
			cell.setCellValue((Byte) data);
		else if (data instanceof String)
			cell.setCellValue((String) data);
		else if (data instanceof RichTextString)
			cell.setCellValue((RichTextString) data);
		else
			return false;
		return true;
	}

	public static void save(final Workbook workbook, final OutputStream outputStream) throws IOException {
		workbook.write(outputStream);
		outputStream.flush();
	}

	public static <E> void imPort(final File inputFile, final int startRow, final ReadDataCell<E> row) throws IOException {
		FileInputStream input = null;
		try {
			input = new FileInputStream(inputFile);
			boolean isExecl2003 = inputFile.getName().endsWith(".xls");
			imPort(input, isExecl2003, startRow, row);
		} finally {
			if (input != null)
				input.close();
		}
	}

	public static <E> void imPort(final InputStream input, final boolean isExecl2003, final int startRow, final ReadDataCell<E> row) throws IOException {
		Workbook workbook = null;
		try {
			if (isExecl2003) {
				workbook = new HSSFWorkbook(input);
			} else {
				workbook = new XSSFWorkbook(input);
			}
			imPort(workbook, startRow, row);
		} finally {
			if (workbook != null)
				workbook.close();
		}
	}

	public static <E> void imPort(final Workbook workbook, final int startRow, final ReadDataCell<E> row) {
		for (Sheet sheet : workbook) {
			readSheet(sheet, startRow, row);
		}
	}

	private static <E> void readSheet(Sheet sheet, int startRow, ReadDataCell<E> row) {
		String sheetName = sheet.getSheetName();
		if (startRow < sheet.getFirstRowNum())
			startRow = sheet.getFirstRowNum();
		for (int rownum = startRow; rownum <= sheet.getLastRowNum(); rownum++) {
			row.xlh(sheet.getRow(rownum), rownum, sheetName);
		}
	}

	public static Map<String, Object> readRow(final Row row, final int startCell, final String... cellNames) {
		Map<String, Object> d = new HashMap<>();
		for (int i = 0; i < cellNames.length; i++) {
			Cell cell = row.getCell(i + startCell);
			Object val = ExcelPoi.readCell(cell);
			for (String name : cellNames[i].split("/")) {
				if (StringUtils.hasText(name))
					d.put(name, val);
			}
		}
		return d;
	}

	@SuppressWarnings("deprecation")
	public static Object readCell(final Cell cell) {
		CellType type;
		if (cell == null || (type = cell.getCellTypeEnum()) == null)
			return null;
		switch (type) {
			case NUMERIC :
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue();
				} else {
					return cell.getNumericCellValue();
				}
			case STRING :
				return cell.getStringCellValue();
			case FORMULA :
				return cell.getNumericCellValue();// 单元格中包含公式如果要返回公式可以用getCellFormula
			case BOOLEAN :
				return cell.getBooleanCellValue();
			case ERROR :
				return cell.getErrorCellValue();
			case BLANK :
			case _NONE :
			default :
				return null;
		}
	}
}
