package visitor.images;

import java.util.HashMap;
import java.util.Map;

import ast.AST;

public class OffsetVisitor {
    class Coords{
        int x = 0;
        int y = 0;
        Coords(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    int currentLevel = 0;
    int[] nextAvailableOffset = new int[100];

    int max_height = 0;
    int max_width = 0;

    Map<AST, Coords> offsets = new HashMap<AST, Coords>(100);

    public void visit(AST ast) {
        if(ast.getChildren().isEmpty()){
            Coords coords = new Coords(nextAvailableOffset[currentLevel], currentLevel);
            offsets.put(ast, coords);
            nextAvailableOffset[currentLevel] += 2;
            if(nextAvailableOffset[currentLevel] > max_width) max_width = nextAvailableOffset[currentLevel];
            if(currentLevel+1 > max_height) max_height = currentLevel+1;
        }
        else{
            currentLevel++;
            for(int i = 0; i < ast.getChildCount(); i++){
                visit(ast.getChild(i));
            }
            currentLevel--;

            AST leftMostChild;
            AST rightMostChild;
            if(ast.getChildCount() == 1){
                leftMostChild = ast.getChild(0);
                rightMostChild = ast.getChild(0);
            }
            else{
                leftMostChild = ast.getChild(0);
                rightMostChild = ast.getChild(ast.getChildCount() - 1);
            }
            int calculatedXCoords = (offsets.get(rightMostChild).x + offsets.get(leftMostChild).x) / 2;

            if(calculatedXCoords < nextAvailableOffset[currentLevel]){
                nextAvailableOffset[currentLevel+1] = nextAvailableOffset[currentLevel] - 1;

                currentLevel++;
                for(int i = 0; i < ast.getChildCount(); i++){
                    visit(ast.getChild(i));
                }
                currentLevel--;
            }

            calculatedXCoords = (offsets.get(rightMostChild).x + offsets.get(leftMostChild).x) / 2;
            Coords coords = new Coords(calculatedXCoords, currentLevel);
            offsets.put(ast, coords);

            nextAvailableOffset[currentLevel] = calculatedXCoords + 2;
            if(nextAvailableOffset[currentLevel] > max_width) max_width = nextAvailableOffset[currentLevel];
            if(currentLevel+1 > max_height) max_height = currentLevel+1;
        }
    }

    public Map<AST, Coords> getOffsets() {
        return this.offsets;
    }

    public int getUnitWidth() {
        return this.max_width;
    }

    public int getUnitHeight() {
        return this.max_height;
    }
}
