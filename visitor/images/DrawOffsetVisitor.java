package visitor.images;

import ast.*;
import ast.ISymbolTree;
import visitor.images.OffsetVisitor.Coords;

import java.awt.image.*;
import java.awt.*;
import java.util.Map;

public class DrawOffsetVisitor {
    Map<AST, Coords> offsets;
    int unitWidth;
    int unitHeight;

    BufferedImage image;
    Graphics2D graphics;
    Font font;
    FontMetrics metrics;

    private static final int NODE_WIDTH = 125;
    private static final int NODE_HEIGHT = 125;

    private static final int VERTICAL_PADDING = 80;
    private static final int HORIZONTAL_PADDING = 5;

    public DrawOffsetVisitor(Map<AST, OffsetVisitor.Coords> offsets, int unitWidth, int unitHeight) {
        this.offsets = offsets;
        this.unitWidth = (NODE_WIDTH * unitWidth) + ((unitWidth + 1) * HORIZONTAL_PADDING);
        this.unitHeight = (NODE_HEIGHT * unitHeight) + ((unitHeight + 1) * VERTICAL_PADDING);
    }

    public void visit(AST ast) {
        this.CreateImage();
    }

    private void CreateImage() {
        this.image = new BufferedImage(unitWidth, unitHeight, BufferedImage.TYPE_INT_RGB);
        
        this.graphics = this.image.createGraphics();
        this.graphics.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING, 
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.graphics.setRenderingHint(
            RenderingHints.KEY_RENDERING, 
            RenderingHints.VALUE_RENDER_QUALITY);

        this.graphics.setBackground(Color.GRAY);
        this.graphics.clearRect(0, 0, unitWidth, unitHeight);

        this.font = new Font("Arial", Font.PLAIN, 20);
        this.graphics.setFont(font);

        this.metrics = this.graphics.getFontMetrics(font);

        this.graphics.setStroke(new BasicStroke(2));

        for (Map.Entry<AST, Coords> entry : offsets.entrySet()) {
            AST node = entry.getKey();
            Coords offset = entry.getValue();
            int x = (offset.x * (NODE_WIDTH + HORIZONTAL_PADDING)) + HORIZONTAL_PADDING;
            int y = (offset.y * (NODE_HEIGHT + VERTICAL_PADDING)) + VERTICAL_PADDING;
            
            this.graphics.setColor(Color.BLACK);
            this.graphics.drawOval(x, y, NODE_WIDTH*2, NODE_HEIGHT);
            this.graphics.setColor(Color.WHITE);
            this.graphics.fillOval(x, y, NODE_WIDTH*2, NODE_HEIGHT);
            this.graphics.setColor(Color.BLACK);

            //fix string placement
            String nodeString = node.toString();
            nodeString = nodeString.substring(nodeString.indexOf(".") + 7, nodeString.indexOf("Tree@"));
            String printString = node.getNodeNumber() + ": " + nodeString;
            int stringWidth = metrics.stringWidth(printString);
            int stringX = x + (NODE_WIDTH) - (stringWidth / 2 + 2);
            int stringY = y + (NODE_HEIGHT / 2) + (metrics.getHeight() / 2);
            String nodeSymbol = "";
            if(node instanceof ISymbolTree){
                nodeSymbol = " (" + ((ISymbolTree)node).getSymbol().getLexeme() + ")";
            }
            this.graphics.drawString(printString + nodeSymbol, stringX, stringY);
            
            for(int i = 0; i < node.getChildCount(); i++){
                AST child = node.getChild(i);
                Coords childOffset = offsets.get(child);
                int childX = (childOffset.x * (NODE_WIDTH + HORIZONTAL_PADDING)) + HORIZONTAL_PADDING;
                int childY = (childOffset.y * (NODE_HEIGHT + VERTICAL_PADDING)) + VERTICAL_PADDING;
                this.graphics.drawLine(x+NODE_WIDTH, y + NODE_HEIGHT, childX + NODE_WIDTH, childY);
            }
        }
    }

    public RenderedImage getImage() {
        return this.image;
    }
}
