package com.icarus.jc.file.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class PoiUtils {

    /**
     * Read excel file.
     *
     * @param path           path of file
     * @param sheetIndex     index of sheet
     * @param startRowIndex  index of starting row
     * @param endRowIndex index of end row
     * @param startColumnIndex index of start column
     * @param endColumnIndex index of end column
     * @return
     * @throws IOException
     */
    public static List<Object[]> read(String path, int sheetIndex, int startRowIndex, int endRowIndex, int startColumnIndex,
                                      int endColumnIndex) throws IOException {

        // Open stream
        File excelFile = new File(path);
        FileInputStream fis = new FileInputStream(excelFile);

        // Create an XSSF Workbook object for our XLSX Excel File
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
//        HSSFWorkbook workbook = new HSSFWorkbook(fis);

        // we get first sheet
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
//        HSSFSheet sheet = workbook.getSheetAt(sheetIndex);

        // we iterate on rows
        Iterator<Row> rowIt = sheet.iterator();
        Row row;
        Iterator<Cell> cellIt;
        Cell cell;
        List<Object[]> listValues = new ArrayList<>();
        Object[] values;
        while (rowIt.hasNext()) {
            row = rowIt.next();
            if (row.getRowNum() >= startRowIndex && row.getRowNum() <= endRowIndex) {
                // iterate on cells for the current row
                cellIt = row.iterator();
                values = new Object[endColumnIndex + 1];
                while (cellIt.hasNext()) {
                    cell = cellIt.next();
                    if (cell.getColumnIndex() <= endColumnIndex && cell.getColumnIndex() >= startColumnIndex) {
                        if (cell.getCellType() == CellType.STRING) {
                            values[cell.getColumnIndex()- startColumnIndex] = cell.getStringCellValue();
                        } else {
                            values[cell.getColumnIndex()- startColumnIndex] = cell.getNumericCellValue();
                        }
                    }
                }
                listValues.add(values);
            }
        }
        workbook.close();
        fis.close();
        return listValues;
    }

}
