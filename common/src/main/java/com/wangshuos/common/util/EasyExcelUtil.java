package com.wangshuos.common.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.wangshuos.common.listener.DynamicExcelDataListener;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasyExcelUtil {

    /**
     * 读取 Excel 文件中的所有 Sheet，并返回每个 Sheet 的数据
     *
     * @param inputStream    Excel 文件输入流
     * @param sheetColumnMap 每个 Sheet 名称及其对应的列名数组
     * @param startRowIndex  开始行索引（默认0）
     * @return 包含每个 Sheet 数据的 Map，其中 key 是 Sheet 名称，value 是 Sheet 数据
     */
    public static <T> Map<String, List<T>> readExcel(InputStream inputStream, Map<String, String[]> sheetColumnMap, Class<T> clazz, int startRowIndex) {
        Map<String, List<T>> sheetDataMap = new HashMap<>();

        for (Map.Entry<String, String[]> entry : sheetColumnMap.entrySet()) {
            String sheetName = entry.getKey();
            String[] columns = entry.getValue();
            DynamicExcelDataListener<T> listener = new DynamicExcelDataListener<>(columns, clazz);
            ExcelReaderSheetBuilder sheetBuilder = EasyExcel.read(inputStream, listener).sheet(sheetName).headRowNumber(startRowIndex);
            sheetBuilder.doRead();
            sheetDataMap.put(sheetName, listener.getDataList());
        }

        return sheetDataMap;
    }

    /**
     * 读取单个文件
     *
     * @param file           文件
     * @param sheetColumnMap 每个 Sheet 名称及其对应的列名数组
     * @param startRowIndex  开始行索引
     * @return 包含每个 Sheet 数据的 Map，其中 key 是 Sheet 名称，value 是 Sheet 数据
     */
    public static <T> Map<String, List<T>> importExcel(File file, Map<String, String[]> sheetColumnMap, Class<T> clazz, int startRowIndex) throws IOException {
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            return readExcel(inputStream, sheetColumnMap, clazz, startRowIndex);
        }
    }

    /**
     * 读取文件输入流
     *
     * @param multipartFile  文件输入流
     * @param sheetColumnMap 每个 Sheet 名称及其对应的列名数组
     * @param startRowIndex  开始行索引
     * @return 包含每个 Sheet 数据的 Map，其中 key 是 Sheet 名称，value 是 Sheet 数据
     */
    public static <T> Map<String, List<T>> importExcel(MultipartFile multipartFile, Map<String, String[]> sheetColumnMap, Class<T> clazz, int startRowIndex) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return readExcel(inputStream, sheetColumnMap, clazz, startRowIndex);
        }
    }

    /**
     * 读取单个文件的第一个 sheet
     *
     * @param file          文件
     * @param columns       列名数组
     * @param startRowIndex 开始行索引
     * @return 读取的第一个 sheet 的数据
     */
    public static <T> List<T> importFirstSheet(File file, String[] columns, Class<T> clazz, int startRowIndex) throws IOException {
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            DynamicExcelDataListener<T> listener = new DynamicExcelDataListener<>(columns, clazz);
            ExcelReaderSheetBuilder sheetBuilder = EasyExcel.read(inputStream, listener).sheet().headRowNumber(startRowIndex);
            sheetBuilder.doRead();
            return listener.getDataList();
        }
    }

    /**
     * 读取文件输入流的第一个 sheet
     *
     * @param multipartFile 文件输入流
     * @param columns       列名数组
     * @param clazz         映射的目标对象的类
     * @param startRowIndex 开始行索引
     * @return 读取的第一个 sheet 的数据
     */
    public static <T> List<T> importFirstSheet(MultipartFile multipartFile, String[] columns, Class<T> clazz, int startRowIndex) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            DynamicExcelDataListener<T> listener = new DynamicExcelDataListener<>(columns, clazz);
            ExcelReaderSheetBuilder sheetBuilder = EasyExcel.read(inputStream, listener).sheet().headRowNumber(startRowIndex);
            sheetBuilder.doRead();
            return listener.getDataList();
        }
    }

    /**
     * 读取文件输入流的第一个 sheet
     *
     * @param multipartFile 文件输入流
     * @param columns       列名数组
     * @param clazz         映射的目标对象的类
     * @param startRowIndex 开始行索引
     * @param sheetNo       读取的 sheet 的编号（从 0 开始）
     * @return 读取的第一个 sheetNo 的数据
     */
    public static <T> List<T> importFromSheet(MultipartFile multipartFile, String[] columns, Class<T> clazz, int startRowIndex, int sheetNo) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            DynamicExcelDataListener<T> listener = new DynamicExcelDataListener<>(columns, clazz);
            ExcelReaderSheetBuilder sheetBuilder = EasyExcel.read(inputStream, listener).sheet(sheetNo).headRowNumber(startRowIndex);
            sheetBuilder.doRead();
            return listener.getDataList();
        }
    }

    /**
     * 读取文件输入流的第一个 sheet
     *
     * @param multipartFile 文件输入流
     * @param columns       列名数组
     * @param clazz         映射的目标对象的类
     * @param startRowIndex 开始行索引
     * @param sheetName     读取的 sheet 的名称
     * @return 读取的第一个 sheetName 的数据
     */
    public static <T> List<T> importFromSheet(MultipartFile multipartFile, String[] columns, Class<T> clazz, int startRowIndex, String sheetName) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            DynamicExcelDataListener<T> listener = new DynamicExcelDataListener<>(columns, clazz);
            ExcelReaderSheetBuilder sheetBuilder = EasyExcel.read(inputStream, listener).sheet(sheetName).headRowNumber(startRowIndex);
            sheetBuilder.doRead();
            return listener.getDataList();
        }
    }
}
