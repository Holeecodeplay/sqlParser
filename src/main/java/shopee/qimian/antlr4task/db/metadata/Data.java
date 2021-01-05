package shopee.qimian.antlr4task.db.metadata;

import java.util.*;

/**
 * @author qimian.huang 黄奇冕
 * @Description 数据存储，全局静态单例
 */
public class Data {
    private static final Map<String, List<String[]>> dataMap = new HashMap<>();  // <表名，数据>

    public static String addData(String tableNm, List<String[]> dataList){
        if(Table.containsTable(tableNm.toUpperCase())){
            dataMap.put(tableNm.toUpperCase(), dataList);
            return String.format("insert data into [%s] successfully", tableNm);
        } else {
            return String.format("table [%s] it not existed", tableNm);
        }
    }

    public static List<String[]> getData(String tableNm){
        return dataMap.getOrDefault(tableNm.toUpperCase(), new ArrayList<>());
    }

    public static String showData(String tableNm){
        return dataMap.get(tableNm.toUpperCase()).toString();
    }
}
