package shopee.qimian.antlr4task.node;

/**
 * @author qimian.huang 黄奇冕
 * @Description 查询条件节点
 */
public class ConditionNode extends Node{
    private String field;
    private String condition;
    private String value;

    public ConditionNode(String field, String condition, String value) {
        this.field = field;
        this.condition = condition;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConditionNode{" +
                "field='" + field + '\'' +
                ", condition='" + condition + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
