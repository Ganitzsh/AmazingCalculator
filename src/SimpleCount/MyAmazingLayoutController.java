package SimpleCount;

import com.udojava.evalex.Expression;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * This class is the main controller for the JavaFX fxml view (MyAmazingLayout.fxml).
 * It handles every action performed by the program.
 *
 * The main functionalities are the mostly same as the ones included in the Microsoft Windows Calculator tool.
 * A local module called EvalEx available on GitHub (https://github.com/uklimaschewski/EvalEx) and created and
 * maintained by Udo Klimaschewski is used to solve mathematical expressions using the RPN system to allow a precise
 * and easy way to handle complex expressions containing multiple parenthesis and signs.
 *
 * This program is an open-source project under the MIT license.
 * Please refer to the README.md file in the root directory of the project.
 *
 * Author: Julien Ganichot
 * Purpose: Tek3 EPITECH's SimpleCount java project
 * Date: November, 21st 2014
 */
public class                MyAmazingLayoutController {
    public enum CalcMode    { NORMAL, SCIENTIFIC }
    @FXML private Label     label_result, label_detailed, label_heart;
    private  boolean        _dot = false, _operatorPressed = false, _specialOperatorPressed = false, _error = false;
    private String          _lastOperator = null, _lastNumber = null, _stored = null, _cache = null;
    private BigDecimal      _result = null;
    private CalcMode        _mode = CalcMode.NORMAL;
    private int             _precision = 12, _openingParenthesis = 0, _closingParenthesis = 0;

    /**
     * This method is an handler bond to numbers (0 - 9)
     * It appends the digit to the current displayed number
     *
     * @param  event  an event to access the source button
     */
    @FXML protected void handleNumber(ActionEvent event) {
        Button pressedButton = (Button) event.getSource();

        if (_operatorPressed)
        {
            label_result.setText("");
            _operatorPressed = false;
        }
        if (label_result.getText().length() < 12 || _error) {
            if (label_result.getText().equals("0") || _error) {
                label_result.setText(pressedButton.getText());
                _error = false;
            } else {
                label_result.setText(label_result.getText() + pressedButton.getText());
            }
        }
    }

    /**
     * This method is an handler bond to the backspace button
     * It removes the last digit of the current number.
     * If there is only one digit left, sets the current number to 0.
     */
    @FXML protected void backspaceHandler() {
        if (label_result.getText().length() == 1)
            label_result.setText("0");
        else if (!label_result.getText().equals("0"))
        {
            String current = label_result.getText();
            label_result.setText(current.substring(0, current.length() - 1));
        }
        int count = label_result.getText().length() - label_result.getText().replace(".", "").length();

        _dot = (count > 0);
    }

    /**
     * This method is an handler bond to the dot button.
     * It appends a dot to the current displayed number.
     * Only work if no occurrence of a number has been found.
     *
     * @param  event  an event to access the source button
     */
    @FXML protected void dotHandler(ActionEvent event) {
        Button pressedButton = (Button) event.getSource();

        if (!_dot)
            label_result.setText(label_result.getText() + pressedButton.getText());
        int count = label_result.getText().length() - label_result.getText().replace(".", "").length();

        _dot = (count > 0);
    }

    /**
     * This method is an handler bond to every operator's buttons.
     * It solves the current mathematical expression to display the result and append the pressed
     * operator button to it.
     *
     * @param  event  an event to access the source button
     */
    @FXML protected void operatorHandler(ActionEvent event) {
        Button pressedButton = (Button) event.getSource();
        String calc = null;

        if (!_operatorPressed) {
            if (label_detailed != null && label_detailed.getText().length() > 0)
                label_detailed.setText(label_detailed.getText() + " ");
            _lastOperator = pressedButton.getText();
            _operatorPressed = true;
            if (label_detailed != null && label_detailed.getText().length() == 0) {
                calc = label_result.getText();
            } else if (label_detailed != null) {
                calc = label_detailed.getText() + label_result.getText();
            }
            if (calc != null) {
                if (label_detailed != null && !_specialOperatorPressed)
                    label_detailed.setText(label_detailed.getText() + label_result.getText() + " " + pressedButton.getText());
                else if (label_detailed != null)
                    label_detailed.setText(label_detailed.getText() + " " + pressedButton.getText());
                try {
                    _result = calculateExpression(calc);
                    label_result.setText(_result.toString());
                } catch (Exception e) {
                    _cache = label_result.getText();
                    label_result.setText(e.getMessage());
                }
                _specialOperatorPressed = false;
            }
        }
    }

    /**
     * This method is an handler bond to the equal button.
     * It solve the entire expression and displays the result.
     */
    @FXML protected void equalHandler() {
        String calc = null;

        if (label_detailed.getText().length() > 0) {
            _lastNumber = label_result.getText();
            calc = label_detailed.getText() + " " + label_result.getText();
        } else if (_lastNumber != null && _lastOperator != null) {
            calc = label_result.getText() + _lastOperator + _lastNumber;
        }
        try {
            if (_mode == CalcMode.NORMAL) {
                _result = calculateExpression(calc);
            } else if (_mode == CalcMode.SCIENTIFIC) {
                _result = calculateExpression(calc);
            }
            label_result.setText(_result.toString());
            label_detailed.setText("");
        } catch (Exception e) {
            _error = true;
            _cache = label_result.getText();
            label_result.setText(e.getMessage());
        }
        _operatorPressed = false;
    }

    /**
     * This method is an handler bond to the C button.
     * It clears both the current result and the whole expression.
     */
    @FXML protected void clearEntireHandler() {
        label_detailed.setText("");
        label_result.setText("0");
        _dot = false;
        _operatorPressed = false;
    }

    /**
     * This method is an handler bond to the CE button.
     * It clears the current result.
     */
    @FXML protected void clearCurrentHandler() {
        label_result.setText("0");
    }

    @FXML protected void reciprocalHandler() {
        String calc;

        if (!label_result.getText().equals("0")) {
            if (label_detailed.getText().length() > 0)
                label_detailed.setText(label_detailed.getText() + " ");
            _lastNumber = "reciprocal(" + label_result.getText() + ")";
            label_detailed.setText(label_detailed.getText() + "reciprocal(" + label_result.getText() + ")");
            calc = label_detailed.getText();
            try {
                label_result.setText(calculateExpression(calc).toString());
            } catch (Exception e) {
                _cache = label_result.getText();
                label_result.setText(e.getMessage());
            }
            if (_operatorPressed)
                _operatorPressed = false;
            _specialOperatorPressed = true;
        }
    }

    @FXML protected void squareHandler() {
        String calc;

        if (!label_result.getText().equals("0")) {
            if (label_detailed.getText().length() > 0)
                label_detailed.setText(label_detailed.getText() + " ");
            _lastNumber = "sqr(" + label_result.getText() + ")";
            label_detailed.setText(label_detailed.getText() + "sqr(" + label_result.getText() + ")");
            calc = label_detailed.getText();
            try {
                label_result.setText(calculateExpression(calc).toString());
            } catch (Exception e) {
                _cache = label_result.getText();
                label_result.setText(e.getMessage());
            }
            if (_operatorPressed)
                _operatorPressed = false;
            _specialOperatorPressed = true;
        }
    }

    @FXML protected void scientificOperatorHandler(ActionEvent event) {
        Button pressedButton = (Button) event.getSource();
        String calc;

        if (!label_result.getText().equals("0")) {
            if (label_detailed.getText().length() > 0)
                label_detailed.setText(label_detailed.getText() + " ");
            _lastNumber = pressedButton.getText() + "(" + label_result.getText() + ")";
            label_detailed.setText(label_detailed.getText() + pressedButton.getText() + "(" + label_result.getText() + ")");
            calc = label_detailed.getText();
            try {
                label_result.setText(calculateExpression(calc).toString());
            } catch (Exception e) {
                _cache = label_result.getText();
                label_result.setText(e.getMessage());
            }
            if (_operatorPressed)
                _operatorPressed = false;
            _specialOperatorPressed = true;
        }
    }

    /**
     * This method is an handler bond to the MS button.
     * It stores the current display into the memory in a String variable called \"_stored\"  and sets the bottom-right
     * label to \"M\".
     */
    @FXML protected void storeHandler() {
        _stored = label_result.getText();
        label_heart.setText("M");
    }

    /**
     * This method is an handler bond to the MR button.
     * It sets the current display to the value of the memory.
     */
    @FXML protected void recallHandler() {
        label_result.setText(_stored);
    }

    /**
     * This method is an handler bond to the MR button.
     * It clears the number stored in the memory and sets the bottom-right label to \"NoMem\".
     */
    @FXML protected void cleanMemoryHandler() {
        _stored = null;
        label_heart.setText("NoMem");
    }

    /**
     * This method is an handler bond to the MR button.
     * It adds the current display to the number stored in the memory.
     */
    @FXML protected void memoryAddHandler() {
        try {
            _result = calculateExpression(_stored  + "+" + label_result.getText());
        } catch (Exception e) {
            _cache = label_result.getText();
            label_result.setText(e.getMessage());
        }
        _stored = _result.toString();
    }

    /**
     * This method is an handler bond to the MR button.
     * It subtracts the current display to the number stored in the memory.
     */
    @FXML protected void memorySubHandler() {
        try {
            _result = calculateExpression(_stored  + "-" + label_result.getText());
        } catch (Exception e) {
            _cache = label_result.getText();
            label_result.setText(e.getMessage());
        }
        _stored = _result.toString();
    }

    @FXML protected void parenthesisHandler(ActionEvent event) {
        Button pressedButton = (Button) event.getSource();
        String string = label_detailed.getText();

        if (pressedButton.getText().equals(")") && _closingParenthesis < _openingParenthesis) {
            if (string.substring(string.length() - 1).equals("("))
                label_detailed.setText(label_detailed.getText() + "0");
            label_detailed.setText(label_detailed.getText() + ")");
            _closingParenthesis++;
        } else if (pressedButton.getText().equals("(")) {
            label_detailed.setText(label_detailed.getText() + "(");
            _openingParenthesis++;
        }
    }

    @FXML protected void reverseHandler() {
        String  string = label_result.getText();

        if (string.substring(0, 1).equals("-"))
            label_result.setText(label_result.getText().substring(1, label_result.getText().length()));
        else
            label_result.setText("-" + label_result.getText());
    }

    @FXML protected void piHandler() {
        BigDecimal pi = new BigDecimal(Math.PI);

        label_result.setText(pi.toString().substring(0, _precision));
    }

    /**
     * Solve an expression given as a parameter.
     *
     * @param  calc  mathematical expression to solve
     * @see com.udojava.evalex.Expression.ExpressionException
     * @return BigDecimal
     */
    public BigDecimal calculateExpression(String calc) throws Exception {
        Expression _exp = new Expression(calc);
        _exp.addFunction(_exp.new Function("reciprocal", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                BigDecimal res = new BigDecimal(1);
                return (res.divide(parameters.get(0), _precision, RoundingMode.HALF_UP));
            }
        });
        _exp.addFunction(_exp.new Function("sqr", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                BigDecimal res = new BigDecimal(1);
                return (res.multiply(parameters.get(0).multiply(parameters.get(0))));
            }
        });
        _exp.setPrecision(_precision);
        String temporaryResultAsString = _exp.eval().toString();
        if (temporaryResultAsString.contains("E+")) {
            System.out.println("The result [" + temporaryResultAsString + "] needs to be split.");
            String[] temporaryArray = temporaryResultAsString.split("E\\+");
            System.out.println("Splitted the result in " + temporaryArray.length + " strings");
            for (int i = 0 ; i < temporaryArray.length ; ++i) {
                System.out.println("  " + i + ": " + temporaryArray[i]);
            }
            String resultWithZeroes = temporaryArray[0];
            int zeroes = Integer.parseInt(temporaryArray[1]);
            for (int i = 0 ; i < zeroes ; ++i) {
                resultWithZeroes += "0";
            }
            System.out.println("Final number: " + resultWithZeroes);
            return (new BigDecimal(resultWithZeroes));
        }
        return (_exp.eval());
    }
}
