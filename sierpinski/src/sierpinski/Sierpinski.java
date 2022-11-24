package sierpinski;

import acm.graphics.GPoint;
import acm.graphics.GPolygon;
import acm.program.GraphicsProgram;
import acm.program.ProgramMenuBar;
import acm.util.RandomGenerator;
import stack.ArrayStack;

public class Sierpinski extends GraphicsProgram {

    public static final int APPLICATION_WIDTH = 800;
    public static final int APPLICATION_HEIGHT = 600;

    private static final RandomGenerator gen = RandomGenerator.getInstance();
    public static final double PADDING = 0.1;

    /**
     * Adds a triangle to the canvas
     *
     * @param x      horizontal coordinate of the bottom left corner
     * @param y      vertical coordinate of the bottom left corner
     * @param base   base of the triangle
     * @param height height of the triangle
     */
    private void addTriangle(double x, double y, double base, double height) {
        var triangle = new GPolygon(new GPoint[]{
                new GPoint(x, y),
                new GPoint(x + base, y),
                new GPoint(x + base / 2, y - height)
        });
        triangle.setFilled(true);
        add(triangle);
    }

    /**
     * Draws the sierpinski triangle
     *
     * @param x      horizontal coordinate of the bottom left corner
     * @param y      vertical coordinate of the bottom left corner
     * @param base   base of the triangle
     * @param height height of the triangle
     * @param depth  number of recursive sierpinkski triangle we draw
     */

    private void sierpinskiRec(double x, double y, double base, double height, int depth) {

        if (depth == 1) {
            addTriangle(x, y, base, height);
        } else {
            double newSide = base / 2;
            double newHeight = height / 2;
            sierpinskiRec(x, y, newSide, newHeight, depth - 1);
            sierpinskiRec(x + newSide, y, newSide, newHeight, depth - 1);
            double xTop = x + newSide / 2;
            double yTop = y - newHeight;
            sierpinskiRec(xTop, yTop, newSide, newHeight, depth - 1);
        }
    }

    private static class Context {
        double x;
        double y;
        double base;
        double height;
        int depth;
        EntryPoint entryPoint;

        public Context(double x, double y, double base, double height, int depth) {
            this.x = x;
            this.y = y;
            this.base = base;
            this.height = height;
            this.depth = depth;
            this.entryPoint = EntryPoint.CALL;
        }
    }

    enum EntryPoint {
        CALL, RESUME1, RESUME2, RESUME3
    }

    private void sierpinskiIter(double x, double y, double base, double height, int depth) {
        double xTop;
        double yTop;
        double newSide;
        double newHeight;
        var stack = new ArrayStack<Context>();
        stack.push(new Context(x, y, base, height, depth));
        while (!stack.isEmpty()) {
            var context = stack.top();
            switch (context.entryPoint) {
                case CALL -> {
                    if (context.depth == 1) {
                        addTriangle(context.x, context.y, context.base, context.height);
                        stack.pop();
                    } else {
                        newSide = context.base / 2;
                        newHeight = context.height / 2;
                        context.entryPoint = EntryPoint.RESUME1;
                        stack.push(new Context(context.x, context.y, newSide, newHeight, context.depth - 1));
                    }
                }
                case RESUME1 -> {
                    newSide = context.base / 2;
                    xTop = context.x + newSide;
                    newHeight = context.height / 2;
                    context.entryPoint = EntryPoint.RESUME2;
                    stack.push(new Context(xTop, context.y, newSide, newHeight, context.depth - 1));

                }
                case RESUME2 -> {

                    newSide = context.base / 2;
                    newHeight = context.height / 2;
                    xTop = context.x + newSide / 2;
                    yTop = context.y - newHeight;
                    context.entryPoint = EntryPoint.RESUME3;
                    stack.push(new Context(xTop, yTop, newSide, newHeight, context.depth - 1));


                }
                case RESUME3 -> {
                    stack.pop();
                }
            }
        }
    }


    private static class ContextOptimized {
        double x;
        double y;
        int depth;
        EntryPoint entryPoint;

        public ContextOptimized(double x, double y, int depth) {
            this.x = x;
            this.y = y;
            this.depth = depth;
            this.entryPoint = EntryPoint.CALL;
        }
    }


    private void sierpinskiIterOptimized(double x, double y, double base, double height, int depth) {
        double xTop;
        double yTop;
        double newSide;
        double newHeight;
        var stack = new ArrayStack<ContextOptimized>();
        stack.push(new ContextOptimized(x, y, depth));
        while (!stack.isEmpty()) {
            var context = stack.top();
            switch (context.entryPoint) {
                case CALL -> {
                    if (context.depth == 1) {
                        newSide = base / Math.pow(2, depth - context.depth);
                        newHeight = height / Math.pow(2, depth - context.depth);
                        addTriangle(context.x, context.y, newSide, newHeight);
                        stack.pop();
                    } else {
                        context.entryPoint = EntryPoint.RESUME1;
                        stack.push(new ContextOptimized(context.x, context.y, context.depth - 1));
                    }
                }
                case RESUME1 -> {
                    newSide = base / Math.pow(2, depth - context.depth + 1);
                    xTop = context.x + newSide;
                    context.entryPoint = EntryPoint.RESUME2;
                    stack.push(new ContextOptimized(xTop, context.y, context.depth - 1));

                }
                case RESUME2 -> {
                    stack.pop();
                    newSide = base / Math.pow(2, depth - context.depth + 1);
                    newHeight = height / Math.pow(2, depth - context.depth + 1);
                    xTop = context.x + newSide / 2;
                    yTop = context.y - newHeight;
                    stack.push(new ContextOptimized(xTop, yTop, context.depth - 1));


                }
            }
        }
    }

    public void run() {
        double base = (1 - PADDING) * getWidth();
        double height = (1 - PADDING) * getHeight();
        double x = (getWidth() - base) / 2;
        double y = (getHeight() + height) / 2;
        //sierpinskiRec(x, y, base, height, 6);
        sierpinskiIter(x, y, base, height, 6);
        sierpinskiIterOptimized(x, y, base, height, 6);
        waitForClick();
        movieRecord();
    }

    private void movieRecord() {
        while (true) {
            var figureIt = getGCanvas().iterator();
            while (figureIt.hasNext()) {
                var color = gen.nextColor();
                figureIt.next().setColor(color);
            }
            pause(100);
        }
    }

    public static void main(String[] args) {
        new Sierpinski().start(args);
    }

    /**
     * Removes the top menu bar of the application
     *
     * @return null (to remove)
     */
    @Override
    protected ProgramMenuBar createMenuBar() {
        return null;
    }
}


