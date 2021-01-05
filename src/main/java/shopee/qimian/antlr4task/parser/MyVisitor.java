package shopee.qimian.antlr4task.parser;

import org.antlr.v4.runtime.RuleContext;
import shopee.qimian.antlr4task.db.metadata.Field;
import shopee.qimian.antlr4task.node.ConditionNode;
import shopee.qimian.antlr4task.node.ExpressionNode;
import shopee.qimian.antlr4task.node.LogicalPlan;
import shopee.qimian.antlr4task.planer.CreateTablePlan;
import shopee.qimian.antlr4task.planer.QueryPlan;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author qimian.huang 黄奇冕
 * @Description
 */
public class MyVisitor extends SqlBaseBaseVisitor<LogicalPlan>{

    // 遍历 AST 生成执行计划树
    public LogicalPlan startVisit(SqlBaseParser parser){
        return visitSingleStatement(parser.singleStatement());
    }

    @Override
    public LogicalPlan visitSingleStatement(SqlBaseParser.SingleStatementContext ctx) {
        return visit(ctx.statement());
    }

    // create
    @Override
    public LogicalPlan visitCreateTable(SqlBaseParser.CreateTableContext ctx) {
        List<Field> fieldList = new LinkedList<>();
        String tableNm = ctx.qualifiedName().getText().trim().toUpperCase();
        List<SqlBaseParser.TableElementContext> tableElementContextList = ctx.tableElement();
        int i = 0;
        for(SqlBaseParser.TableElementContext tableElementContext : tableElementContextList){
            String name = tableElementContext.columnDefinition().identifier().getText().trim().toUpperCase();
            String type = tableElementContext.columnDefinition().type().getText().trim().toUpperCase();
            fieldList.add(new Field(name, type, i++));
        }
        return new CreateTablePlan(tableNm, fieldList);
    }

    // query
    @Override
    public LogicalPlan visitQuerySpecification(SqlBaseParser.QuerySpecificationContext ctx){
        QueryPlan from = (QueryPlan)visit(ctx.relation(0));                                                                   // select
        List<ExpressionNode> expressionNodeList = ctx.selectItem().stream().map(item -> (ExpressionNode)visit(item)).collect(Collectors.toCollection(LinkedList::new));   //  field
        // aggFlag
        boolean aggFlag = false;
        for(ExpressionNode expressionNode : expressionNodeList){
            if(null != expressionNode.getActionTyp()){
                aggFlag = true;
                break;
            }
        }

        List<String> grouByList = null;
        if(Objects.nonNull(ctx.groupBy())){
            grouByList = ctx.groupBy().groupingElement().stream().map(RuleContext::getText).collect(Collectors.toList());// groupby
        }
        ConditionNode conditionNode = null;
        QueryPlan res = from;
        if(Objects.nonNull(ctx.where)){
            conditionNode = (ConditionNode)visit(ctx.booleanExpression(0));
        }
        res = new QueryPlan(from.getTableNm(), expressionNodeList, conditionNode, grouByList, aggFlag, res.getChild());
        return res;
    }

    // 单表
    @Override
    public LogicalPlan visitUnquotedIdentifier(SqlBaseParser.UnquotedIdentifierContext ctx) {
        return new QueryPlan(ctx.IDENTIFIER().getText().trim().toUpperCase());
    }

    // 字段
    @Override
    public LogicalPlan visitColumnReference(SqlBaseParser.ColumnReferenceContext ctx){
        return new ExpressionNode(ctx.getText().trim().toUpperCase(), null);
    }
    // 聚合 + 字段
    @Override
    public LogicalPlan visitFunctionCall(SqlBaseParser.FunctionCallContext ctx){
        String field = ctx.expression(0).getText().trim().toUpperCase();
        String actionTyp = ctx.qualifiedName().getText().trim().toUpperCase();
        return new ExpressionNode(field, actionTyp);
    }
    // 嵌套单表
    @Override
    public LogicalPlan visitSingleTable(SqlBaseParser.SingleTableContext ctx){
        return new QueryPlan(ctx.getText());
    }
    // 嵌套查询返回
    @Override
    public LogicalPlan visitSubTable(SqlBaseParser.SubTableContext ctx){
        QueryPlan queryPlan = (QueryPlan) visit(ctx.relationPrimary());
        String tableNm = ((QueryPlan)visit(ctx.identifier())).getTableNm().trim().toUpperCase();
        return new QueryPlan(tableNm, null, null, null, false, queryPlan);
    }
    @Override
    public LogicalPlan visitSubqueryRelation(SqlBaseParser.SubqueryRelationContext ctx){
        return visit(ctx.query());
    }
    // where 条件
    @Override
    public LogicalPlan visitWhereExpression(SqlBaseParser.WhereExpressionContext ctx){
        String field = ctx.valueExpression().getText().trim().toUpperCase();
        ConditionNode conditionNode = (ConditionNode) visit(ctx.predicate());
        conditionNode.setField(field);
        return conditionNode;
    }
    @Override
    public LogicalPlan visitComparison(SqlBaseParser.ComparisonContext ctx){
        String value = ctx.valueExpression().getText().trim();
        if(value.startsWith("\"") || value.startsWith("'")) value = value.substring(1);
        if(value.endsWith("\"") || value.endsWith("'")) value = value.substring(0, value.length() - 1);
        return new ConditionNode(null, ctx.comparisonOperator().getText().trim(), value);
    }

}
