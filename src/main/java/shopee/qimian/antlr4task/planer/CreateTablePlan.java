package shopee.qimian.antlr4task.planer;

import shopee.qimian.antlr4task.db.metadata.Field;
import shopee.qimian.antlr4task.node.Node;

import java.util.List;

/**
 * @author qimian.huang 黄奇冕
 * @Description create语句执行计划
 */

public class CreateTablePlan extends Node {
    private String tableNm;
    private List<Field> fieldList;

    public CreateTablePlan(String tableNm, List<Field> fieldList) {
        this.tableNm = tableNm;
        this.fieldList = fieldList;
    }

    public String getTableNm() {
        return tableNm;
    }

    public void setTableNm(String tableNm) {
        this.tableNm = tableNm;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    @Override
    public String toString() {
        return "CreateTablePlan{" +
                "tableNm='" + tableNm + '\'' +
                ", fieldList=" + fieldList +
                '}';
    }
}
