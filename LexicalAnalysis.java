package SemanticAnalysis;

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class LexicalAnalysis {
    private String keyWord[] = {"const","var","procedure","begin","end","odd","if",
            "then","call","while","do","read","write","else"};
    public static Queue<Word> wt=new LinkedList<>();
    public static Queue<Word> buffert=new LinkedList<>();
    private char ch;
    //判断是否是关键字
    boolean isKey(String str)
    {
        for(int i = 0;i < keyWord.length;i++)
        {
            if(keyWord[i].equals(str))
                return true;
        }
        return false;
    }
    //判断是否是字母
    boolean isLetter(char ch)
    {
        if(ch >= 'a' && ch<= 'z')
            return true;
        else
            return false;
    }
    //判断是否是数字
    boolean isNum(char num)
    {
        if(num >= '0' && num <= '9')
            return true;
        else
            return false;
    }
    //词法分析
    void getSym_wordlist(char[] chars) throws IOException
    {

        for(int i = 0;i< chars.length;i++) {
            Word temp =new Word("","");
            String arr = "";
            ch = chars[i];
            while(ch == ' '||ch == '\t'||ch == '\n'||ch == '\r')//忽略空格、换行、回车和Tab
            {ch = chars[++i];}
            if(isLetter(ch)){//变量名以字母开头 其中可能包含数字
                while(isLetter(ch)||isNum(ch)){
                    arr += ch; //arr的拓展要放在前面
                    ch = chars[++i];
                }
                //注意此处回退一个字符 对应之前++i操作
                i--;
                if(isKey(arr)){
                    //关键字 置保留字的种别至sym
                    temp.type=Word.KEY;
                    temp.value=arr;
                    wt.offer(temp);
                    buffert.offer(temp);
                }
                else{
                    //标识符
                    temp.type=Word.VARIABLE;
                    temp.value=arr;
                    wt.offer(temp);
                    buffert.offer(temp);
                }
            }
            else
            { 	 //无符号数的读取
                if(isNum(ch))
                {   double d=0;
                    double N=0;
                    double P=0;int e=1;
                    double t=0;//存储最终的结果
                    while(isNum(ch)) {
                        arr += ch;
                        d=ch-48;
                        N=10*N+d;
                        ch=chars[++i];
                    }
                    if(ch=='.') {
                        arr += ch;
                        ch=chars[++i];
                        int j=0;
                        while(isNum(ch)) {
                            j++;arr += ch;
                            d=ch-48;
                            for(int i1=j;i1>0;i1--)
                                d*=0.1;
                            N=N+d;
                            ch=chars[++i];
                        }
                        if(ch=='e') {
                            arr += ch;
                            ch=chars[++i];
                            if(ch=='-') e=-1;
                            arr += ch;
                            ch=chars[++i];
                            while(isNum(ch)) {
                                arr += ch;
                                d=ch-48;
                                P=10*P+d;
                                ch=chars[++i];
                            }
                        }

                    }
                    t=N*Math.pow(10,P*e);
                    temp.type=Word.CONST;
                    String str=String.valueOf(t);
                    temp.value=str;
                    wt.offer(temp);
                    buffert.offer(temp);
                    i--;//回退 非常重要
                }

                else switch(ch){
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case '#':
                        temp.type=Word.OPERATOR;
                        temp.value=ch+"";
                        wt.offer(temp);
                        buffert.offer(temp);
                        break;
                        //writer.append(ch+"\t"+4+"\t运算符"+"\n");break;
                    //分界符
                    case '(':
                    case ')':
                    case '[':
                    case ']':
                    case ';':
                    case '{':
                    case '}':
                        temp.type=Word.BOUNDARYSIGN;
                        temp.value=ch+"";
                        wt.offer(temp);
                        buffert.offer(temp);
                        break;

                    //运算符
                    case '=':{
                        ch = chars[++i];
                        if(ch == '=') {
                            temp.type=Word.OPERATOR;
                            temp.value="==";
                            wt.offer(temp);
                            buffert.offer(temp);
                        }
                        else {
                            temp.type=Word.OPERATOR;
                            temp.value="=";
                            wt.offer(temp);
                            buffert.offer(temp);
                            i--;
                        }
                    }break;
                    case ':':{
                        ch = chars[++i];
                        if(ch == '=') {
                            temp.type=Word.OPERATOR;
                            temp.value=":=";
                            wt.offer(temp);
                            buffert.offer(temp);
                        }
                        else {
                            temp.type=Word.OPERATOR;
                            temp.value=":";
                            wt.offer(temp);
                            buffert.offer(temp);
                            i--;
                        }
                    }break;
                    case '>':{
                        ch = chars[++i];
                        if(ch == '=') {
                            temp.type=Word.OPERATOR;
                            temp.value=">=";
                            wt.offer(temp);
                            buffert.offer(temp);
                        }
                        else {
                            temp.type=Word.OPERATOR;
                            temp.value=">";
                            wt.offer(temp);
                            buffert.offer(temp);
                            i--;
                        }
                    }break;
                    case '<':{
                        ch = chars[++i];
                        if(ch == '=') {
                            temp.type=Word.OPERATOR;
                            temp.value="<=";
                            wt.offer(temp);
                            buffert.offer(temp);
                        }
                        else {
                            temp.type=Word.OPERATOR;
                            temp.value="<";
                            wt.offer(temp);
                            buffert.offer(temp);
                            i--;
                        }
                    }break;
                    //无识别
                    // default:  writer.append(ch+"\t6"+"\t无识别符");
                }
            }
        }
    }
    void getSym(char[] chars) throws IOException
    {
        File f = new File("src/L.txt");
        FileOutputStream fop = new FileOutputStream(f);
        // 构建FileOutputStream对象,文件不存在会自动新建

        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
        for(int i = 0;i< chars.length;i++) {
            String arr = "";
            ch = chars[i];
            while(ch == ' '||ch == '\t'||ch == '\n'||ch == '\r')//忽略空格、换行、回车和Tab
            {ch = chars[++i];}
            if(isLetter(ch)){//变量名以字母开头 其中可能包含数字
                while(isLetter(ch)||isNum(ch)){
                    arr += ch; //arr的拓展要放在前面
                    ch = chars[++i];
                }
                //注意此处回退一个字符 对应之前++i操作
                i--;
                if(isKey(arr)){
                    //关键字 置保留字的种别至sym
                    writer.append(arr+"\t"+3+"\t关键字"+"\n");
                }
                else{
                    //标识符
                    writer.append(arr+"\t"+1+"\t标识符"+"\n");
                }
            }
            else
            { 	 //无符号数的读取
                if(isNum(ch))
                {   double d=0;
                    double N=0;
                    double P=0;int e=1;
                    double t=0;//存储最终的结果
                    while(isNum(ch)) {
                        arr += ch;
                        d=ch-48;
                        N=10*N+d;
                        ch=chars[++i];
                    }
                    if(ch=='.') {
                        arr += ch;
                        ch=chars[++i];
                        int j=0;
                        while(isNum(ch)) {
                            j++;arr += ch;
                            d=ch-48;
                            for(int i1=j;i1>0;i1--)
                                d*=0.1;
                            N=N+d;
                            ch=chars[++i];
                        }
                        if(ch=='e') {
                            arr += ch;
                            ch=chars[++i];
                            if(ch=='-') e=-1;
                            arr += ch;
                            ch=chars[++i];
                            while(isNum(ch)) {
                                arr += ch;
                                d=ch-48;
                                P=10*P+d;
                                ch=chars[++i];
                            }
                        }

                    }
                    t=N*Math.pow(10,P*e);
                    writer.append(arr+"\t"+2+"\t常数"+t+"\n");
                    i--;//回退 非常重要
                }

                else switch(ch){
                    //运算符
                    case '+': writer.append(ch+"\t"+4+"\t运算符"+"\n");break;
                    case '-': writer.append(ch+"\t"+4+"\t运算符"+"\n");break;
                    case '*': writer.append(ch+"\t"+4+"\t运算符"+"\n");break;
                    case '/': writer.append(ch+"\t"+4+"\t运算符"+"\n");break;
                    case '#': writer.append(ch+"\t"+4+"\t运算符"+"\n");break;
                    //分界符
                    case '(': writer.append(ch+"\t"+5+"\t分界符"+"\n");break;
                    case ')': writer.append(ch+"\t"+5+"\t分界符"+"\n");break;
                    case '[': writer.append(ch+"\t"+5+"\t分界符"+"\n");break;
                    case ']': writer.append(ch+"\t"+5+"\t分界符"+"\n");break;
                    case ';': writer.append(ch+"\t"+5+"\t分界符"+"\n");break;
//                case '{': writer.append(ch+" "+5+"\t分界符"+"\n");break;
//                case '}': writer.append(ch+" "+5+"\t分界符"+"\n");break;
                    //运算符
                    case '=':{
                        ch = chars[++i];
                        if(ch == '=') writer.append("=="+"\t"+4+"\t运算符"+"\n");
                        else {
                            writer.append("="+"\t"+4+"\t运算符"+"\n");
                            i--;
                        }
                    }break;
                    case ':':{
                        ch = chars[++i];
                        if(ch == '=') writer.append(":="+"\t"+4+"\t运算符"+"\n");
                        else {
                            writer.append(":"+"\t"+4+"\t运算符"+"\n");
                            i--;
                        }
                    }break;
                    case '>':{
                        ch = chars[++i];
                        if(ch == '=') writer.append(">=\t"+4+"\t运算符"+"\n");
                        else {
                            writer.append(">\t"+4+"\t运算符"+"\n");
                            i--;
                        }
                    }break;
                    case '<':{
                        ch = chars[++i];
                        if(ch == '=') writer.append("<="+"\t"+4+"\t运算符"+"\n");
                        else {
                            writer.append("<"+"\t"+4+"\t运算符"+"\n");
                            i--;
                        }
                    }break;
                    //无识别
                    // default:  writer.append(ch+"\t6"+"\t无识别符");
                }
            }
        }
        writer.close();
        fop.close();
    }
    public static void main(String[] args) throws IOException {
        File file = new File("src/input_code.txt");//定义一个file对象，用来初始化FileReader
        FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
        int length = (int) file.length();
        //这里定义字符数组的时候需要多定义一个,因为词法分析器会遇到超前读取一个字符的时候，如果是最后一个
        //字符被读取，如果在读取下一个字符就会出现越界的异常
        char buf[] = new char[length+1];
        reader.read(buf);
        reader.close();
        LexicalAnalysis LA=new LexicalAnalysis();
        LA.getSym(buf);
        LA.getSym_wordlist(buf);
    }
}
