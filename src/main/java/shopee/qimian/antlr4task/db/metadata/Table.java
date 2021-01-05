package shopee.qimian.antlr4task.db.metadata;

import shopee.qimian.antlr4task.node.LogicalPlan;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author qimian.huang 黄奇冕
 * @Description 表结构信息，全局静态单例
 */
public class Table implements LogicalPlan {
    private static final Map<String, Map<String, Field>> tableMap = new ConcurrentHashMap<>();  // <表名，<字段名，字段属性>>

    public static void addTable(String tableNm, List<Field> fieldList){
        Map<String, Field> fieldMap = new LinkedHashMap<>();
        for(Field field : fieldList){
            fieldMap.put(field.getName(), field);
        }
        tableMap.put(tableNm, fieldMap);
    }

    public static String showTable(String tableNm){
        return tableMap.getOrDefault(tableNm.toUpperCase(), new HashMap<>()).values().stream()
                .sorted(Comparator.comparing(Field::getPos))
                .map(Field::toString).collect(Collectors.joining("\n"));
    }

    // 获取表中某字段的位置，不存在则返回 -1
    public static int getFieldPos(String tableNm, String field){
        return tableMap.getOrDefault(tableNm.toUpperCase(), new HashMap<>())
                .getOrDefault(field.toUpperCase(), new Field(null,null,-1))
                .getPos();
    }

    public static String getFieldTyp(String tableNm, String field){
        return tableMap.get(tableNm.toUpperCase()).get(field.toUpperCase()).getType();
    }

    public static Boolean containsTable(String tableNm){
        return tableMap.containsKey(tableNm.toUpperCase());
    }


}
