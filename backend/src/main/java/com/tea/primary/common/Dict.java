package com.tea.primary.common;

import java.util.List;
/** 茶叶初制加工厂业务字典。 */
public final class Dict {

    private Dict() {
    }

    public static final List<String> PICKING_SEASONS = List.of("春季", "夏季", "秋季");
    public static final List<String> PROCESS_STEPS = List.of("萎凋", "杀青", "揉捻", "烘干");
    public static final List<String> BATCH_STATUS = List.of("待加工", "加工中", "已完成");
    public static final List<String> PRODUCT_GRADES = List.of("特级", "一级", "二级");
    public static final List<String> EQUIPMENT_STATUS = List.of("运行", "停机", "检修");

    public static boolean contains(List<String> values, String value) {
        return value != null && values.contains(value);
    }
}
