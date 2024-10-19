package interpreter;

import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class RuntimeStack {

  private Stack<Integer> framePointers;
  private Vector<Integer> runStack;

  public RuntimeStack() {
    this.framePointers = new Stack<>();
    this.runStack = new Vector<>();
    this.framePointers.push(0);
  }

  public RuntimeStack(Stack<Integer> framePointers, Vector<Integer> runStack) {
    this.framePointers = framePointers;
    this.runStack = runStack;
    if(this.framePointers.empty())
      this.framePointers.push(0);
  }

  public int getSize() {
    return this.runStack.size();
  }

  public int peek() {
    int size = runStack.size();
    if(size != 0){
      return runStack.get(size - 1);
    }
    return 0;
  }

  public int pop() {
    return runStack.remove(runStack.size() - 1);
  }

  public int push(int value) {
    this.runStack.add(value);
    return value;
  }

  public Integer push(Integer value) {
    this.runStack.add(value);
    return value;
  }

  public void newFrameAt(int offset) {
    this.framePointers.push(this.runStack.size() - offset);
  }

  public List<Integer> getCurrentFrame() {
    int top = this.framePointers.peek();
    return this.runStack.subList(top, this.runStack.size());
  }

  public void popFrame() {
    int value = this.peek();
    int offset = this.framePointers.pop();
    this.runStack.subList(offset, this.runStack.size()).clear();
    this.push(value);
  }

  public int store(int offset) {
    int top = this.pop();
    this.runStack.set(this.framePointers.peek() + offset, top);
    return top;
  }

  public int load(int offset) {
    int top = this.runStack.get(this.framePointers.peek() + offset);
    this.push(top);
    return top;
  }

  public void clear() {
    this.runStack.clear();
    this.framePointers.clear();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    Stack<Integer> temp = new Stack<>();
    while(!this.framePointers.empty() && this.framePointers.peek() != 0){
      temp.push(this.framePointers.pop());
    }

    sb.append("[");
    for (int i = 0; i < this.runStack.size(); i++) {
      sb.append(this.runStack.get(i));
      if (i != this.runStack.size() - 1) {
        boolean isFramePointer = false;
        for(int j = 0; j < temp.size(); j++){
          if(i+1 == temp.get(j)){
            sb.append("] [");
            isFramePointer = true;
          }
        }
        if(!isFramePointer)
          sb.append(",");
      }
    }
    sb.append("]");

    while(!temp.empty()){
      this.framePointers.push(temp.pop());
    }

    return sb.toString();
  }
}
