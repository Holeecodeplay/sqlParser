package shopee.qimian.antlr4task.node;

/**
 * @author qimian.huang 黄奇冕
 * @Description 查询字段节点
 */
public class ExpressionNode extends Node{
    private String field;
    private String actionTyp;

    public ExpressionNode(String field, String actionTyp) {
        this.field = field;
        this.actionTyp = actionTyp;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getActionTyp() {
        return actionTyp;
    }

    public void setActionTyp(String actionTyp) {
        this.actionTyp = actionTyp;
    }

    @Override
    public String toString() {
        return "ExpressionNode{" +
                "field='" + field + '\'' +
                ", actionTyp='" + actionTyp + '\'' +
                '}';
    }
}
