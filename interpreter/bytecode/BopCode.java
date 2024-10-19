package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class BopCode extends ByteCode{
    private String code;
    private String operator;

    public BopCode(List<String> code) {
        super(code);
        this.code = code.get(0);
        this.operator = code.get(1);
    }

    @Override
    public String toString() {
        return this.code + " " + this.operator;
    }

    @Override
    public void execute(VirtualMachine vm) {
        int operand1 = vm.getRuntimeStack().pop();
        int operand2 = vm.getRuntimeStack().pop();
        int result = 0;
        switch(this.operator){
            case "+":
                result = operand2 + operand1;
                break;
            case "-":
                result = operand2 - operand1;
                break;
            case "*":
                result = operand2 * operand1;
                break;
            case "/":
                result = operand2 / operand1;
                break;
            case "==":
                if(operand1 == operand2)
                    result = 1;
                else
                    result = 0;
                break;
            case "!=":
                if(operand1 != operand2)
                    result = 1;
                else
                    result = 0;
                break;
            case "<=":
                if(operand2 <= operand1)
                    result = 1;
                else
                    result = 0;
                break;
            case ">=":
                if(operand2 >= operand1)
                    result = 1;
                else
                    result = 0;
                break;
            case "<":
                if(operand2 < operand1)
                    result = 1;
                else 
                    result = 0;
                break;
            case ">":
                if(operand2 > operand1)
                    result = 1;
                else 
                    result = 0;
                break;  
            case "&":
                if(operand2 == 1 && operand1 == 1)
                    result = 1;
                else
                    result = 0;
                break;
            case "|":
                if(operand2 == 1 || operand1 == 1)
                    result = 1;
                else
                    result = 0;
                break;  
        }
        vm.getRuntimeStack().push(result);
    }
}
