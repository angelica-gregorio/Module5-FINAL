package exercise2;
import acm.graphics.GCompound;
import acm.graphics.GLabel;

public class CalculatorLayout extends GCompound {

    private static final int NROWS = 7;     /* Number of rows    */
    private static final int NCOLS = 5;     /* Number of columns */
    private static final int MARGIN = 10;
    private static final String[] labels = {
            "CE", "C", "⌫", " ", " ",
            "hyp", "sin", "cos", "tan", "DEG",
            "1/x", "π", "e", "!", "÷",
            "√", "7", "8", "9", "x",
            "^", "4", "5", "6", "-",
            "log", "1", "2", "3", "+",
            "ln", "±", ".", "0", "="};

    private GLabel memoDisplay;
    private GLabel mainDisplay;
    private StringBuilder memoBuffer;
    private StringBuilder numBuffer;
    private static final String MAIN_FONT = "SansSerif-bold-48";
    private static final String MEMO_FONT = "SansSerif-bold-28";
    private static final String SPECIFIC_BUTTON_FONT = "SansSerif-bold-20";

    private MyButton degButton; // Reference to the "DEG" button

    public CalculatorLayout(double height) {
        double sqSize = height / (NROWS + 1);
        numBuffer = new StringBuilder();
        clearNumBuffer();
        mainDisplay = new GLabel(numBuffer.toString(), MARGIN, MARGIN + 70);
        mainDisplay.setFont(MAIN_FONT);

        memoBuffer = new StringBuilder();
        memoDisplay = new GLabel("", MARGIN, 2 * MARGIN);
        memoDisplay.setFont(MEMO_FONT);

        add(mainDisplay);
        add(memoDisplay);

        int count = 0;
        for (int i = 1; i <= NROWS; i++) {
            for (int j = 0; j < NCOLS; j++) {
                double x = MARGIN + j * sqSize;
                double y = i * sqSize - MARGIN;
                String label = labels[count++];
                MyButton myButton = new MyButton(x, y, sqSize, sqSize, label);

                // Save reference to the "DEG" button
                if (label.equals("DEG")) {
                    degButton = myButton;
                }

                // Apply specific font to certain buttons

                add(myButton);
            }
        }
    }

    /* Sample Polymorphic Methods */
    public void setMemoDisplay(char symbol) {
        memoBuffer.append(symbol);
        memoDisplay.setLabel(memoBuffer.toString());
    }

    public void setMemoDisplay(String input) {
        memoBuffer.append(input);
        memoDisplay.setLabel(memoBuffer.toString());
    }

    /* Sample Polymorphic Methods */
    public void setMainDisplay(char symbol) {
        if (numBuffer.length() == 1 && numBuffer.charAt(0) == '0') {
            numBuffer.setCharAt(0, symbol);
        } else {
            numBuffer.append(symbol);
        }
        mainDisplay.setLabel(numBuffer.toString());
    }

    public void setMainDisplay(String input) {
        numBuffer.setLength(0);
        numBuffer.append(input);
        mainDisplay.setLabel(input);
    }

    public String getMainDisplay() {
        return numBuffer.toString();
    }

    public void clearMainDisplay() {
        clearNumBuffer();
        mainDisplay.setLabel("0");
    }

    public void clearMemoDisplay() {
        clearMemoBuffer();
        memoDisplay.setLabel("");
    }

    public void clearNumBuffer() {
        numBuffer.setLength(1);
        numBuffer.setCharAt(0, '0');
    }

    public void clearMemoBuffer() {
        memoBuffer.setLength(0);
    }

    public void clearMemoElement(char operation) {
        int position = memoBuffer.lastIndexOf("" + operation);
        if (position != -1) {
            memoBuffer.setLength(position + 1);
            memoDisplay.setLabel(memoBuffer.toString());
        }
    }

    public void negateElement(char operation) {
        int position = memoBuffer.lastIndexOf("" + operation);
        if (position != -1) {
            if (memoBuffer.charAt(position + 1) != '-') {
                memoBuffer.insert(position + 1, '-');
            } else {
                memoBuffer.deleteCharAt(position + 1);
            }
            if (numBuffer.charAt(0) != '-') {
                numBuffer.insert(0, '-');
            } else {
                numBuffer.deleteCharAt(0);
            }
            memoDisplay.setLabel(memoBuffer.toString());
            mainDisplay.setLabel(numBuffer.toString());
        }
    }

    public void deleteOneCharacter() {
        if (numBuffer.length() > 1) {
            numBuffer.setLength(numBuffer.length() - 1);
        } else {
            numBuffer.setLength(1);
            numBuffer.setCharAt(0, '0');
        }
        if (memoBuffer.length() > 0) {
            memoBuffer.setLength(memoBuffer.length() - 1);
        }
        memoDisplay.setLabel(memoBuffer.toString());
        mainDisplay.setLabel(numBuffer.toString());
    }

    public GLabel getMemoDisplay() {
        return  memoDisplay; // Assuming memoDisplay is a String field that holds the content of the memo display
    }

    // Add method to get the DEG button
    public MyButton getDegButton() {
        return degButton;
    }

    public void appendMainDisplay(String s) {
        String currentDisplay = getMainDisplay(); // Get the current content of the main display
        currentDisplay += s; // Append the new string s to the current display content
        setMainDisplay(currentDisplay); // Set the updated display content back to the main display
    }

}
