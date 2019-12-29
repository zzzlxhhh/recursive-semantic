package SemanticAnalysis;

public class Word {
    public final static String KEY = "关键字";
    public final static String OPERATOR = "运算符";
    public final static String CONST = "常量";
    public final static String VARIABLE = "变量";
    public final static String BOOL_CONST = "布尔常量";
    public final static String IDENTIFIER = "标志符";
    public final static String BOUNDARYSIGN = "界符";
    public final static String END = "结束符";
    public String value;
    public String type;
    public Word( String value, String type) {
        this.value = value;
        this.type = type;
    }
}
