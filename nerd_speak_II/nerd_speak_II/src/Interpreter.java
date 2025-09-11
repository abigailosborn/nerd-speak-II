import java.util.ArrayList;
import java.util.Stack;

public class Interpreter {
    public static Stack<Token> the_stack = new Stack<Token>();
    public static ArrayList<Verbal> user_String_variables = new ArrayList<Verbal>();
    public static ArrayList<Material> user_int_variables = new ArrayList<Material>();
    public static void main(String[] args){
        Parser parse = new Parser();
        the_stack = parse.the_stack_tm;
        parse.parse_code();

        //iterate through and parse each stack token
        while(the_stack.size() > 0){
            parse_token(the_stack.pop());
        }

    }
    public static void parse_token(Token current_token){
        //store the value of the token being currently looked at 
         switch(current_token.type){
            //handle variable assignments
            case EQUAL:
                //get the name and the value for the variable
                Token variable_name = the_stack.pop();
                Token variable_value = the_stack.pop();
                //make variable to store the assignment 
                switch(variable_value.type){
                    case LANGUAGE:
                        Verbal new_String_variable = new Verbal(variable_name.value, variable_value.value);
                        user_String_variables.add(new_String_variable);
                        break;
                    case DICE:
                        int dice_value = get_dice_value(variable_value);
                        Material new_int_variable = new Material(variable_name.value, Integer.toString(dice_value));
                        user_int_variables.add(new_int_variable);
                        break;
                }
                break;
         }
    }

    public static int generate_number(int dice_sides){
        int roll = (int) (Math.random() * dice_sides + 1);
        return roll;
    }

    public static int get_dice_value(Token die){
        //create variables to be used later
        int dice_sides = 0;
        int roll = 0;
        switch(die.value){
            //determine maximum for random number generation
            case "d4":
                dice_sides = 4;
                break;
            case "d6":
                dice_sides = 6;
                break;
            case "d8":
                dice_sides = 8;
                break;
            case "d10":
                dice_sides = 10;
                break;
            case "d12":
                dice_sides = 12;
                break;
            case "d20":
                dice_sides = 20;
                break;
            case "d100":
                dice_sides = 100;
                break;
            default:
                System.out.println("Invalid Dice type");
                break;
        }
        roll = generate_number(dice_sides);
        return roll;
        //debug statement
        //System.out.println("roll: " + roll);
        //TODO: my code is broken :( it's only rolling one die
    }
}
