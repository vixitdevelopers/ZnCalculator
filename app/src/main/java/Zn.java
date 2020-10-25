import org.apache.commons.lang3.StringUtils;

import java.io.LineNumberInputStream;
import java.util.ArrayList;

public class Zn {

    private int n;
    private int[] set;
    private String left, right;
    private char x;
    private int type;

    public static final int TYPE_REGULAR_EQUATION = 0;
    public static final int TYPE_ONE_SIDE_NO_VAR = 1;
    public static final int TYPE_TWO_SIDE_NO_VAR = 2;
    public static final int TYPE_ONE_SIDE_WITH_VAR = 3;


    public Zn(int n, String expression) {
        this.n = n;
        this.set = new int[n - 1];
        for (int i = 0; i < set.length; i++)
            set[i] = i + 1;
        this.x = varFinder(expression);
        splitByEquals(expression); //defines left , right
        defineType();
    }

    private void defineType() {
        boolean oneSide = (right == null || right == "");
        if (x == '?' && oneSide)
            type = TYPE_ONE_SIDE_NO_VAR;
        else if (x == '?' && !oneSide)
            type = TYPE_TWO_SIDE_NO_VAR;
        else if (x != '?' && oneSide)
            type = TYPE_ONE_SIDE_WITH_VAR;
        else if (x != '?' && !oneSide)
            type = TYPE_REGULAR_EQUATION;
    }

    //find the var
    //check if var is connected , and take care of that.
    //return var
    private char varFinder(String expression) {
        char alpha = 'a';
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == alpha)
                return alpha;
            else
                alpha++;
        }
        return '?';
    }

    //split expression to two by = sign
    private void splitByEquals(String expression) {
        String[] parts = expression.split("=");
        left = parts[0];
        right = parts[1];
    }


    //replace var with each int, and return
    private boolean checkIfEquals(String left, String right) {
        double solutionRight = Calculater.eval(right);
        double solutionLeft = Calculater.eval(left);
        int modRight = (int) (solutionRight % n);
        int modLeft = (int) (solutionLeft % n);
        return (modLeft == modRight);
    }

    private double solveNoEquals(String left) {
        double solution = Calculater.eval(left);
        return (solution % n);
    }

    public String solve() {
        switch (type) {

            case TYPE_REGULAR_EQUATION:
                for (int a : set) {
                    if (checkIfEquals(replace(left, a), replace(right, a)))
                        return x + "=" + a;
                }
                return "No Solution";
            break;
            case TYPE_ONE_SIDE_NO_VAR:
                return "" + solveNoEquals(left);
            break;
            case TYPE_ONE_SIDE_WITH_VAR:
                return "error 304: can't solve equation";
                break;
            case TYPE_TWO_SIDE_NO_VAR:
                return "Equation is "+checkIfEquals(left,right);
                break;
        }
        return "error 205. can't solve";
    }

    private String replace(String equation, int a) {
        //args e.g:
        ArrayList<String>appearancesOfX = getListBySplit(equation);
        for (String part : appearancesOfX) {

            for (int i = 0; i < part.length() - 1; i++) {
                char current = part.charAt(i);
                char next = part.charAt(i + 1);
                if (current == x)
                   part = part.replace(x, (char) a);
                else if(next==x) {
                    if (StringUtils.isAlphanumeric(String.valueOf(current))) // e.g is 3x
                        part = part.replace("" + x, "*" + a);
                    else
                        part = part.replace(x, (char) a);
                }
            }

        }
    }

    private ArrayList<String> getListBySplit(String equation){
        ArrayList<String>result = new ArrayList<>();
        while (true){
            if(equation==""||equation==null)
                return result;
            else{
                result.add(StringUtils.substringBefore(equation, String.valueOf(x))+x);
                equation = StringUtils.substringAfter(equation, String.valueOf(x));
            }
        }
    }
}
