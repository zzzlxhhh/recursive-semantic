package SemanticAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SyntaxAnalysis {
    public ArrayList<FourElement> fourElemList=new ArrayList<FourElement>();
    public Word lookahead=new Word("","");
    public int count;
    public void  MatchToken(String expected) throws SyntaxException {

        if(!lookahead.value.equals(expected))
        {
            throw new SyntaxException("语法错误");
        }
        if(LexicalAnalysis.wt.size()>0) {
            lookahead.value = LexicalAnalysis.wt.peek().value;
            lookahead.type = LexicalAnalysis.wt.poll().type;//后续注意能否这样使用注意
        }
    }
    public void parseA() throws SyntaxException {
        String Eplace;
        String vplace;
        if(lookahead.type.equals(Word.VARIABLE)) {
            vplace=lookahead.value;
            MatchToken(lookahead.value);
            MatchToken("=");
            Eplace=parseE();
            MatchToken(";");
            System.out.println("="+'\t'+vplace+'\t'+'_'+'\t'+Eplace);
        }
    }
    public String parseE() throws SyntaxException {
        String Tplace="";
        String ehead="";
        String Eplace="";
        if(lookahead.value.equals("(")||lookahead.type.equals(Word.VARIABLE)||lookahead.type.equals(Word.CONST)){
            Tplace=parseT();
            ehead=Tplace;
            Eplace=parsee(ehead);
        }
        return Eplace;
    }
    public String parseT() throws SyntaxException {
        String Fplace="";
        String Tplace="";
        String thead="";
        if(lookahead.value.equals("(")||lookahead.type.equals(Word.VARIABLE)||lookahead.type.equals(Word.CONST)){
            Fplace=parseF();
            thead=Fplace;
            Tplace=Fplace;
            parset(thead);//返回Fplace的值
        }
        return Tplace;
    }
    public String parsee(String ehead) throws SyntaxException {
        String Tplace;
        String eplace;
        String e1head;
        switch (lookahead.value){
            case "+":
                MatchToken("+");
                Tplace=parseT();
                eplace="T"+(count++);
                e1head=eplace;
                parsee(e1head);
                System.out.println("+"+'\t'+ehead+'\t'+Tplace+'\t'+eplace);
                return eplace;
            case "-":
                MatchToken("-");
                Tplace=parseT();
                eplace="T"+(count++);
                e1head=eplace;
                parsee(e1head);
                System.out.println("-"+'\t'+ehead+'\t'+Tplace+'\t'+eplace);
                return eplace;
            case ")":
            case "#":
                break;
        }
    return  "";
    }
    public String parseF() throws SyntaxException {
        String Fplace;
       switch (lookahead.value){
           case "(":
               MatchToken("(");
               Fplace=parseE();
               MatchToken(")");
               return Fplace;
       }
        if(lookahead.type.equals(Word.VARIABLE)) {
            Fplace =lookahead.value;
            MatchToken(lookahead.value);
            return Fplace;
        }
        else if(lookahead.type.equals(Word.CONST)){
            Fplace =lookahead.value;
            MatchToken(lookahead.value);
            return Fplace;
        }
        return "_";
    }
    public String parset(String thead) throws SyntaxException {
        String Fplace="";
        String tplace="";
        String t1head="";
        switch (lookahead.value){
            case ")":
            case "#":
            case "+":
            case "-":
                break;
            case "*":
                MatchToken("*");
                Fplace=parseF();
                tplace="T"+(count++);
                t1head=Fplace;
                parset(t1head);
                System.out.println("*"+'\t'+thead+'\t'+Fplace+'\t'+tplace);
                break;
            case "/":
                MatchToken("/");
                Fplace=parseF();
                tplace="T"+(count++);
                t1head=Fplace;
                parset(t1head);
                System.out.println("/"+'\t'+thead+'\t'+Fplace+'\t'+tplace);
                break;
        }
        return "";
    }

    public static void main(String[] args) throws SyntaxException, IOException {
	// write your code here
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
        SyntaxAnalysis SA=new SyntaxAnalysis();
        //Word temp=new Word("","");
        SA.lookahead.value=LexicalAnalysis.wt.peek().value;
        SA.lookahead.type=LexicalAnalysis.wt.poll().type;
        SA.parseA();
    }
}
