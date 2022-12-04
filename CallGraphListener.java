import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.*;
import java.util.*;
import java.util.ArrayList;


public class CallGraphListener extends Java8BaseListener {
    static String method_name=null; //asıl methodun ismine karar verirken
    static String name=null; // çağırılan methodların ismine karar verirken

    static StringBuilder buf = new StringBuilder();;
    static String classname;
    static String packagename;
    static String methoddec;
    static ArrayList<String> methoddeclaratinarr = new ArrayList<String>();
    static ArrayList<String> methods  = new ArrayList<String>();
    static ArrayList<String> directions  = new ArrayList<String>();
    public static void main(String[] args) throws Exception {
        ANTLRInputStream input = new ANTLRInputStream(System.in);
        Java8Lexer lexer = new Java8Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        ParseTree tree = parser.compilationUnit();
        ParseTreeWalker walker = new ParseTreeWalker();
        CallGraphListener listener = new CallGraphListener();
        ArrayList<Boolean> bool  = new ArrayList<Boolean>();
        // This is where we trigger the walk of the tree using our listener.
        buf.append("digraph G {\n");
        walker.walk(listener, tree);
        for(int i=0;i<methods.size();i++){//bulunmayan ama çağırılan method varsa boolean arrayini false yap
                if(methoddeclaratinarr.contains(methods.get(i))){
                    bool.add(true);
            }
                else
                    bool.add(false);
        }
        for(int i=0; i<bool.size();i++){  //bulunmayan ama çağırılan metotu çıktıya vermek için
            if(bool.get(i)==false){
                int j=methods.get(i).indexOf("_");
                String str1=methods.get(i).substring(0,j);
                String str2=methods.get(i).substring(j+1);
                buf.append(methods.get(i)+"[label=\""+packagename+"/"+str1+"/"+str2+"\", fillcolor=white, style=filled, shape=circle];\n"  );
            }
        }
        for(int i=0;i<directions.size();i++){ //dşrectec graphın yönünü göstermek için
            buf.append(directions.get(i));
        }

        // ..
        buf.append("}");
        System.out.println(buf.toString());
    }
    //package ismini çekmek için
    @Override public void enterPackageDeclaration(Java8Parser.PackageDeclarationContext ctx) {
        packagename=ctx.Identifier(0).toString();//com.acme için Identifier(1) de gerekiyordu,package isimlerinin 1 uzunluğunda olduğunu varsaydım

    }
    //Class isimlerini çekmek için
    @Override public void enterNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
        classname=ctx.Identifier().toString();
    }

    @Override public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {

        method_name=classname+"_"+ctx.methodHeader().methodDeclarator().Identifier().toString();
        methoddec=(packagename+"/"+classname+"/"+ctx.methodHeader().methodDeclarator().Identifier().toString()); //hangi metholar var clasın icinde
        methoddeclaratinarr.add(method_name);
        buf.append(method_name+"[label=\""+methoddec+"\", fillcolor=green, style=filled, shape=circle];\n"  );


    }
    //çağırılan methodlar
    //enterA:kendi clasından çağırılan metotlar
    @Override public void enterA(Java8Parser.AContext ctx) {
        name=classname+"_"+ctx.methodName().Identifier().toString();
        String s=packagename+"/"+classname+"/"+ctx.methodName().Identifier().toString()+"\n";
        directions.add(method_name+" -> "+name+";\n");
        methods.add(name);
    }
    //enter B: başka classtan çağırılan metotlar
    @Override public void enterB(Java8Parser.BContext ctx) {
        name=ctx.typeName().Identifier().toString()+"_"+ctx.Identifier().toString();
        String s=packagename+"/"+ctx.typeName().Identifier().toString()+"/"+ctx.Identifier().toString();
        methods.add(name);
        directions.add(method_name+" -> "+name+";\n");
    }



}
