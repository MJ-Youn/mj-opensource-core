package io.github.mjyoun.core.utils.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;

import io.github.mjyoun.core.utils.excel.annotation.ExcelColumn;
import io.github.mjyoun.core.utils.excel.annotation.ExcelSheet;

/**
 * 엑셀 관련 유틸 정보
 * 
 * @author MJ Youn
 * @since 2024. 02. 06.
 */
public class ExcelUtils {

    /**
     * 엑셀 파일 생성
     * 
     * @param <T>
     *            저장할 class 정보
     * @param contents
     *            데이터 정보
     * @param clazz
     *            저장할 데이터의 클래스 정보
     * @param path
     *            저장할 위치
     * @throws FileNotFoundException
     * @throws IOException
     * 
     * @author MJ Youn
     * @since 2024. 02. 07.
     */
    public static <T> void create(@NotNull List<T> contents, @NotNull Class<T> clazz, @NotNull Path path) throws FileNotFoundException, IOException {
        SXSSFWorkbook workbook = create(contents, clazz);

        workbook.write(new FileOutputStream(path.normalize().toString()));
    }

    /**
     * 엑셀 파일 생성
     * 
     * @param headers
     *            헤더 정보
     * @param datas
     *            데이터 정보
     * @param sheetName
     *            시트 이름
     * @param path
     *            저장할 위치
     * @throws FileNotFoundException
     * @throws IOException
     * 
     * @author MJ Youn
     * @since 2024. 02. 07.
     */
    public static void create(@NotNull String[] headers, @NotNull Object[][] datas, @NotNull String sheetName, @NotNull Path path)
            throws FileNotFoundException, IOException {
        SXSSFWorkbook workbook = create(headers, datas, sheetName);

        workbook.write(new FileOutputStream(path.normalize().toString()));
    }

    /**
     * 엑셀 파일 생성
     * 
     * @param headers
     *            헤더 정보
     * @param datas
     *            데이터 정보
     * @param path
     *            저장할 위치
     * @throws FileNotFoundException
     * @throws IOException
     * 
     * @author MJ Youn
     * @since 2024. 02. 07.
     */
    public static void create(@NotNull String[] headers, @NotNull Object[][] datas, @NotNull Path path) throws FileNotFoundException, IOException {
        SXSSFWorkbook workbook = create(headers, datas);

        workbook.write(new FileOutputStream(path.normalize().toString()));
    }

    /**
     * Class로 부터 데이터 추출하여 엑셀 생성
     * 
     * @param <T>
     *            추출할 데이터 클래스
     * @param contents
     *            데이터
     * @param clazz
     *            클래스 정보
     * @return {@link SXSSFWorkbook}
     * 
     * @author MJ Youn
     * @since 2024. 02. 07.
     */
    public static <T> SXSSFWorkbook create(@NotNull List<T> contents, @NotNull Class<T> clazz) {
        // sheet 정보
        ExcelSheet excelSheet = clazz.getAnnotation(ExcelSheet.class);
        String sheetName = "sheet1";

        if (excelSheet != null && excelSheet.value().trim().length() != 0) {
            sheetName = excelSheet.value();
        }

        // header 정보 추출
        Field[] fields = clazz.getDeclaredFields();
        List<String> headers = new ArrayList<>();

        for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
            Field _field = fields[fieldIndex];
            ExcelColumn excelInfo = _field.getAnnotation(ExcelColumn.class);

            if (excelInfo != null) {
                if (!excelInfo.ignore()) {
                    if (excelInfo.value().trim().length() != 0) {
                        headers.add(excelInfo.value());
                    } else {
                        headers.add(_field.getName());
                    }
                }
            } else {
                headers.add(_field.getName());
            }
        }

        // data 정보 추출
        Object[][] datas = new Object[contents.size()][headers.size()];

        for (int contentIndex = 0; contentIndex < contents.size(); contentIndex++) {
            int dataIndex = 0;
            T content = contents.get(contentIndex);

            for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
                Field _field = fields[fieldIndex];
                ExcelColumn excelInfo = _field.getAnnotation(ExcelColumn.class);

                if (excelInfo == null || (excelInfo != null && !excelInfo.ignore())) {
                    boolean accessible = _field.isAccessible();
                    _field.setAccessible(true);

                    try {
                        datas[contentIndex][dataIndex++] = _field.get(content);
                    } catch (IllegalArgumentException | IllegalAccessException ignore) {
                    } finally {
                        _field.setAccessible(accessible);
                    }
                }
            }
        }

        return create(headers.toArray(new String[headers.size()]), datas, sheetName);
    }

    /**
     * 엑셀 객체 정보 생성
     * 
     * @param headers
     *            헤더 정보
     * @param datas
     *            데이터 정보
     * @return 엑셀 객체
     * 
     * @author MJ Youn
     * @since 2024. 02. 07.
     */
    public static SXSSFWorkbook create(@NotNull String[] headers, @NotNull Object[][] datas) {
        return create(headers, datas, "sheet01");
    }

    /**
     * 엑셀 객체 정보 생성
     * 
     * @param headers
     *            헤더 정보
     * @param datas
     *            데이터 정보
     * @param sheetName
     *            시트 이름
     * @return 액셀 객체
     * 
     * @author MJ Youn
     * @since 2024. 02. 07.
     */
    public static SXSSFWorkbook create(@NotNull String[] headers, @NotNull Object[][] datas, @NotNull String sheetName) {
        // excel 파일 생성
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        // new sheet
        SXSSFSheet sheet = workbook.createSheet(sheetName);
        // Row Number
        int rowNum = 0;

        // header 
        Row headerRow = sheet.createRow(rowNum++);
        for (int headerIndex = 0; headerIndex < headers.length; headerIndex++) {
            Cell cell = headerRow.createCell(headerIndex);
            cell.setCellValue(headers[headerIndex]);

            CellStyle style = workbook.createCellStyle();
            style.setBorderTop(BorderStyle.THICK);
            style.setBorderBottom(BorderStyle.DOUBLE);
            style.setAlignment(HorizontalAlignment.CENTER);

            XSSFFont font = new XSSFFont();
            font.setBold(true);
            style.setFont(font);

            if (headerIndex == 0) {
                style.setBorderLeft(BorderStyle.THICK);
                style.setBorderRight(BorderStyle.THIN);

            } else if (headerIndex == headers.length - 1) {
                style.setBorderLeft(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THICK);
            } else {
                style.setBorderLeft(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
            }

            cell.setCellStyle(style);
        }

        // data
        for (int i = 0; i < datas.length; i++) {
            Row row = sheet.createRow(rowNum++);

            for (int j = 0; j < datas[i].length; j++) {
                Cell cell = row.createCell(j);

                // 데이터 타입에 따른 설정
                Object _data = datas[i][j];

                if (_data instanceof Integer) {
                    cell.setCellValue((Integer) _data);
                } else if (_data instanceof Long) {
                    cell.setCellValue((Long) _data);
                } else if (_data instanceof Double) {
                    cell.setCellValue((Double) _data);
                } else if (_data instanceof Float) {
                    cell.setCellValue((Float) _data);
                } else if (_data instanceof Boolean) {
                    cell.setCellValue((Boolean) _data);
                } else if (_data instanceof LocalDate) {
                    LocalDate date = (LocalDate) _data;
                    cell.setCellValue(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else if (_data instanceof LocalDateTime) {
                    LocalDateTime dateTime = (LocalDateTime) _data;
                    cell.setCellValue(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } else if (_data instanceof LocalTime) {
                    LocalTime time = (LocalTime) _data;
                    cell.setCellValue(time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                } else {
                    if (_data == null || _data.toString().trim().length() == 0) {
                        cell.setCellValue("-");
                    } else {
                        cell.setCellValue(_data.toString());
                    }
                }

                CellStyle style = workbook.createCellStyle();
                style.setBorderLeft(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderBottom(BorderStyle.THIN);

                if (i == datas.length - 1) {
                    style.setBorderBottom(BorderStyle.THICK);
                }

                if (j == 0) {
                    style.setBorderLeft(BorderStyle.THICK);
                } else if (j == datas[i].length - 1) {
                    style.setBorderRight(BorderStyle.THICK);
                }

                cell.setCellStyle(style);
            }
        }

        // 컬럼 width 자동 조절
        sheet.trackAllColumnsForAutoSizing();
        // 필터 적용
        sheet.setAutoFilter(new CellRangeAddress(0, datas.length, 0, headers.length - 1));

        return workbook;
    }

}
