package shopee.qimian.antlr4task.node;

/**
 * @author qimian.huang 黄奇冕
 * @Description 执行计划抽象类，增加 child 方便子查询嵌套等执行计划
 */

public abstract class Node implements LogicalPlan{
    protected LogicalPlan child;

    public LogicalPlan getChild() {
        return child;
    }

    public void setChild(LogicalPlan child) {
        this.child = child;
    }

    public boolean hasChild(){
        return null != child;
    }
}
