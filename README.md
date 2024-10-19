[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/tOu9l-Id)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-7f7980b617ed060a017424585567c406b6ee15c891e84e1186181d67ecf80aa0.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=13075694)
# Assignment 4 (Interpreter) Documentation

Author: Cole Chiodo

## Scope of Work

| Requirement                           | Completed? | Comments from student |
| ------------------------------------- | ---------- | --------------------- |
| 1. Standard byte codes implemented    | [x]         |                       |
| 2. CodeTable implemented              | [x]         |                       |
| 3. ByteCodeLoader implemented         | [x]         |                       |
| 4. Program implemented                | [x]         |                       |
| 5. RuntimeStack implemented           | [x]         |                       |
| 6. New bytecode implemented (DMP)     | [x]         |                       |
| 7. Output correct (for byte codes)    | [x]         |                       |
| 8. Output correct (for runtime stack) | [x]         |                       |

## Results and Conclusions

### What I Learned

This Assignment I learned the full compilation process by implementing a bytecode interpreter, and the steps that step takes. It starts with the ByteCode files generated in the parser step. We take that file and read it line by line into the ByteCodeLoader. The ByteCodeLoader takes the string, and gets the type of code it is. Using that code, along with the CodeTable, we are able to use Java's reflection to make that an instance of that bytecode. We then pass the arguments to the bytecode, and add it to the Program object. The program object holds all the bytecode in a List to be executed later in the process. That program object is then passed to the VirtualMachine, which executes the bytecodes. The VirtualMachine also holds the RuntimeStack. While the program runs, variables are loaded into the RuntimeStack. Finally, the VirtualMachine holds a counter for the program. This counter is used to keep track of where we are in the program, and is used to jump to other parts of the program when Goto or Call bytecodes are executed.

### Challenges I Encountered

The biggest encounter I had was knowing where to start. I started in order of the steps, but that only left me not knowing if what I had done was correct or not until I complete the next step. It wasnt until after the first 5 steps were complete and was then starting to work on the VirtualMachine when I started gaining more of an understanding of the process. I was able to go back and make changes to the previous steps to make sure they were correct. I tested on the program used in the first ~40 slides, the program in the pdf, and the factorial program the professor generated. This was definitely the hardest assignment I have ever done in any computer science class, but I learned a lot from it.