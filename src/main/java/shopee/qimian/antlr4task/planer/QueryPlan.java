package shopee.qimian.antlr4task.planer;

import shopee.qimian.antlr4task.node.*;

import java.util.List;

/**
 * @author qimian.huang 黄奇冕
 * @Description 查询计划，只考虑 where xx = xx 的单条件，其他不做兼容
 */

public class QueryPlan extends Node {
    private String tableNm;                                 // table
    private List<ExpressionNode> expressionNodeList;        // query field
    private ConditionNode conditionNode;                    // where
    private List<String> groupBy;                           // groupBy
    private Boolean aggFlag;                                // 聚合标识

    public QueryPlan(String tableNm, List<ExpressionNode> expressionNodeList, ConditionNode conditionNode, List<String> groupBy, Boolean aggFlag, LogicalPlan child) {
        this.tableNm = tableNm;
        this.expressionNodeList = expressionNodeList;
        this.conditionNode = conditionNode;
        this.groupBy = groupBy;
        this.aggFlag = aggFlag;
        this.child = child;
    }

    public QueryPlan(String tableNm) {
        this.tableNm = tableNm;
        this.expressionNodeList = null;
        this.conditionNode = null;
        this.groupBy = null;
        this.child = null;
        this.aggFlag = false;
    }

    public String getTableNm() {
        return tableNm;
    }

    public void setTableNm(String tableNm) {
        this.tableNm = tableNm;
    }

    public List<ExpressionNode> getExpressionNodeList() {
        return expressionNodeList;
    }

    public void setExpressionNodeList(List<ExpressionNode> expressionNodeList) {
        this.expressionNodeList = expressionNodeList;
    }

    public ConditionNode getConditionNode() {
        return conditionNode;
    }

    public void setConditionNode(ConditionNode conditionNode) {
        this.conditionNode = conditionNode;
    }

    public List<String> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(List<String> groupBy) {
        this.groupBy = groupBy;
    }

    public Boolean getAggFlag() {
        return aggFlag;
    }

    public void setAggFlag(Boolean aggFlag) {
        this.aggFlag = aggFlag;
    }

    @Override
    public String toString() {
        return "QueryPlan{" +
                "tableNm='" + tableNm + '\'' +
                ", expressionNodeList=" + expressionNodeList +
                ", conditionNode=" + conditionNode +
                ", groupBy=" + groupBy +
                ", aggFlag=" + aggFlag +
                ", child=" + child +
                '}';
    }
}
