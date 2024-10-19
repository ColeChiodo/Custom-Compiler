package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class CallCode extends ByteCode {
    private String label;
    private List<Integer> value;
    private int address;

    public CallCode(List<String> code) {
        super(code);
        this.label = code.get(1);
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getLabel() {
        return this.label;
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.pushReturnAddress();
        vm.setPC(this.address);
        this.value = vm.getArgs();
    }

    @Override
    public String toString() {
        String result;

        if(this.label.contains("<<")) {
            result = String.format("%-25s%s", "CALL " + this.label, this.label.substring(0, this.label.indexOf("<<")) + "(");
        } else {
            result = String.format("%-25s%s", "CALL " + this.label, this.label + "(");
        }

        for(int i = 0; i < this.value.size(); i++) {
            result += this.value.get(i);
            if(i != this.value.size() - 1) {
                result += ",";
            }
        }
        result += ")";
        return result;
    }
}
