package shopee.qimian.antlr4task.db.metadata;

/**
 * @author qimian.huang 黄奇冕
 * @Description 表字段信息
 */
public class Field {
    private String name;
    private String type;
    private int pos;

    public Field(String name, String type, int pos) {
        this.name = name;
        this.type = type;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return name + ", " + type ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
