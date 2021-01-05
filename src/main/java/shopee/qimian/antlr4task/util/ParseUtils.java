package shopee.qimian.antlr4task.util;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import shopee.qimian.antlr4task.parser.SqlBaseLexer;
import shopee.qimian.antlr4task.parser.SqlBaseParser;

/**
 * @author qimian.huang 黄奇冕
 * @Description
 */
public class ParseUtils {

    // 返回解析器
    public static SqlBaseParser getSqlParser(String sql){
        SqlBaseLexer lexer = new SqlBaseLexer(CharStreams.fromString(sql));  //词法分析
        return new SqlBaseParser(new CommonTokenStream(lexer)); // 语法分析
    }

}
