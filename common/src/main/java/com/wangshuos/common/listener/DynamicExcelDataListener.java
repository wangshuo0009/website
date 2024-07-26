package com.sg.bjftviewprotect.system.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author wangshuo
 * @Description excel监听器
 * @Date 2024/5/30 15:43
 **/

public class DynamicExcelDataListener<T> extends AnalysisEventListener<Map<Integer, String>> {
    @Getter
    private final List<T> dataList = new ArrayList<>();
    private final String[] columns;
    private final Class<T> clazz;

    public DynamicExcelDataListener(String[] columns, Class<T> clazz) {
        this.columns = columns;
        this.clazz = clazz;
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            for (int i = 0; i < columns.length; i++) {
                try {
                    Field field = clazz.getDeclaredField(columns[i]);
                    field.setAccessible(true);
                    field.set(instance, data.get(i));
                } catch (Exception e) {
                    // 异常处理跳过未对应的属性
                    System.err.println("Field not found: " + columns[i] + ", skipping...");
                }
            }
            dataList.add(instance);
        } catch (Exception e) {
            // 不处理
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // No implementation needed for this example
    }
}
