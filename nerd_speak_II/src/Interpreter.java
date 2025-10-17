import java.util.ArrayList;
import java.util.Stack;

public class Interpreter {
    public static Stack<Token> the_stack = new Stack<Token>();
    public static ArrayList<Verbal> user_String_variables = new ArrayList<Verbal>();
    public static ArrayList<Material> user_int_variables = new ArrayList<Material>();
    public static ArrayList<BagOfHolding> user_arrays = new ArrayList<BagOfHolding>();
    public static Parser parse = new Parser();
    public static void main(String[] args){
        parse.parse_code();
        parse_stack(parse.the_stack_tm);
        /*for(int i = 0; i < user_arrays.size(); i++){
            for(int j = 0; j < user_arrays.get(i).value.size(); i++){
                System.out.println(user_arrays.get(i).value);
            }
        }*/
    }
    public static void parse_stack(Stack<Token> stack){
        the_stack = stack;
        //if(the_stack.size() > 0){
        Token working_token = the_stack.pop();
        //iterate through and parse each stack token
        while(the_stack.size() > 0){
            parse_token(working_token);
            if(the_stack.size() > 0){
                working_token = the_stack.pop();
            }   
        }
        /*for(int i = 0; i < user_arrays.size(); i++){
            for(int j = 0; j < user_arrays.get(i).value.size(); i++){
                System.out.println(user_arrays.get(i).value);
            }
        }*/
    }
    public static void parse_token(Token current_token){
        //store the value of the token being currently looked at 
         switch(current_token.type){
            //handle variable assignments
            case EQUAL:
                assign_variables();
                break;
            //handle function calls 
            case FUNCTION_CALL:
            //get the name of the function being called
                Token function_name = the_stack.pop();
                //print function
                if(function_name.value.equals("message")){
                    message();
                }
                break;
            case ARRAY_FUNCTION:
                do_array_functions(current_token.value);
                break;
            //set up arrays 
            case BAG_OF_HOLDING:
                Token array_name = the_stack.pop();
                ArrayList<String> create_array = new ArrayList<String>();
                BagOfHolding ary = new BagOfHolding(array_name.value, create_array);
                user_arrays.add(ary);
                break;
            case PLUS:
                do_addition();
                break;

            case MINUS:
                do_subtraction();
                break;

            case MULTIPLY:
                do_multiplication();
                break;
            
            case DIVIDE:
                do_division();
                break;
            
            case WOW:
                do_while();
                //iterate through while there is no second tilde 
                break;
                /*System.out.println("New Stack");
                while(temp_stack.size() > 0){
                    System.out.println(temp_stack.pop().value);
                }*/
                //break; 
            
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
    }
    //print function 
    public static void message(){
        Token verbal_or_material = the_stack.pop();
        Token next_token = the_stack.pop();
        //check to see if it is just a string or a variable
        switch(next_token.type){
            case LANGUAGE:
                System.out.println(next_token.value);
                break;
            //if the user is printing a variable find the name and get the value
            case IDENTIFIER:
                //check if the variable is an integer or a string 
                if((verbal_or_material.value).equals("verbal_component")){
                    for(int i = 0; i < user_String_variables.size(); i++){
                        if((next_token.value).equals((user_String_variables.get(i)).name)){
                            System.out.println(user_String_variables.get(i).value);
                        }
                    }
                }
                else if((verbal_or_material.value).equals("material_component")){
                    for(int i = 0; i < user_int_variables.size(); i++){
                        if(next_token.value.equals(user_int_variables.get(i).name)){
                            System.out.println(user_int_variables.get(i).value);
                        }
                    }
                }
                break;
        }
    }

    public static void assign_variables(){
        //get the name for the variable
        Token variable_name = the_stack.pop();
        Token variable_value = the_stack.pop();
        //check if the variable already exists, if it does, merely reassign the value
        switch(variable_value.type){
            case LANGUAGE:
                for(int i = 0; i < user_String_variables.size(); i++){
                    if(variable_name.value.equals(user_String_variables.get(i).name)){
                        user_String_variables.get(i).value = variable_value.value;
                    }
                    else{
                        make_new_variable(variable_name, variable_value);
                    }
                }
                if(user_String_variables.size() == 0){
                    make_new_variable(variable_name, variable_value);
                }
                break;
            case DICE:
                for(int i = 0; i < user_int_variables.size(); i++){
                    if(variable_name.value.equals(user_int_variables.get(i).name)){
                        int dice_value = get_dice_value(variable_value);
                        user_int_variables.get(i).value = dice_value;
                    }
                    else{
                        make_new_variable(variable_name, variable_value);
                    }
                }
                if(user_int_variables.size() == 0){
                    make_new_variable(variable_name, variable_value);
                }
                break;
            case BAG_OF_HOLDING:
                for(int i = 0; i < user_arrays.size(); i++){
                    if(variable_name.value.equals(user_arrays.get(i).name)){
                        ArrayList<String> temp = new ArrayList<String>();
                        user_arrays.get(i).value = temp;
                    }
                    else{
                        make_new_variable(variable_name, variable_value);
                    }
                }
                if(user_arrays.size() == 0){
                    make_new_variable(variable_name, variable_value);
                }
                break;
        }
    }

    public static void make_new_variable(Token variable_name, Token variable_value){
        switch(variable_value.type){
            case LANGUAGE:
                Verbal new_String_variable = new Verbal(variable_name.value, variable_value.value);
                user_String_variables.add(new_String_variable);
                break;
            case DICE:
                int dice_value = get_dice_value(variable_value);
                Material new_int_variable = new Material(variable_name.value, dice_value);
                user_int_variables.add(new_int_variable);
                break;
            case ARRAY_FUNCTION:
                Token trash = the_stack.pop();
                Token items = the_stack.pop();
                Token in = the_stack.pop();
                Token arr_name = the_stack.pop();
                ArrayList<String> temp = new ArrayList<String>();
                BagOfHolding user_array = new BagOfHolding(null, temp);
                int size = 0;
                for(int i = 0; i < user_arrays.size(); i++){
                    if(user_arrays.get(i).name.equals(arr_name.value)){
                        user_array = user_arrays.get(i);
                    }
                }
                for(int k = 0; k < user_array.value.size(); k++){
                    size += 1;
                }
                Material new_int_var = new Material(variable_name.value, size);
                user_int_variables.add(new_int_var);
                break;
            //create array variables and store them in an arraylist to reference at a later time
            /*case BAG_OF_HOLDING:
                BagOfHolding new_array_variable = new BagOfHolding(variable_name.value, null);
                user_arrays.add(new_array_variable);
                break;*/
        }
    }
    public static void do_addition(){
        //define variables for addition
        Token left_side = the_stack.pop();
        Token right_side = the_stack.pop();
        int left = 0;
        int right = 0;
        int sum = 0;

        //check to see what is being added to 
        switch(left_side.type){
            case IDENTIFIER: 
                right = get_dice_value(right_side);
                right = generate_number(right);
                for(int i = 0; i < user_int_variables.size(); i++){
                    if(left_side.value.equals(user_int_variables.get(i).name)){
                        left = user_int_variables.get(i).value;
                        sum  = left + right;
                        user_int_variables.get(i).value = sum;
                    }
                }
                break;
        }
    }
    public static void do_subtraction(){
        //define variables for addition
        Token left_side = the_stack.pop();
        Token right_side = the_stack.pop();
        int left = 0;
        int right = 0;
        int difference = 0;

        //check to see what is being added to 
        switch(left_side.type){
            case IDENTIFIER: 
                right = get_dice_value(right_side);
                right = generate_number(right);
                for(int i = 0; i < user_int_variables.size(); i++){
                    if(left_side.value.equals(user_int_variables.get(i).name)){
                        left = user_int_variables.get(i).value;
                        difference  = left - right;
                        user_int_variables.get(i).value = difference;
                    }
                }
                break;
        }
    }
    public static void do_multiplication(){
        //define variables for addition
        Token left_side = the_stack.pop();
        Token right_side = the_stack.pop();
        int left = 0;
        int right = 0;
        int product = 0;

        //check to see what is being added to 
        switch(left_side.type){
            case IDENTIFIER: 
                right = get_dice_value(right_side);
                right = generate_number(right);
                for(int i = 0; i < user_int_variables.size(); i++){
                    if(left_side.value.equals(user_int_variables.get(i).name)){
                        left = user_int_variables.get(i).value;
                        product  = left * right;
                        user_int_variables.get(i).value = product;
                    }
                }
                break;
        }
    }

    public static void do_division(){
        //define variables for addition
        Token left_side = the_stack.pop();
        Token right_side = the_stack.pop();
        int left = 0;
        int right = 0;
        int quotient = 0;

        //check to see what is being added to 
        switch(left_side.type){
            case IDENTIFIER: 
                right = get_dice_value(right_side);
                right = generate_number(right);
                for(int i = 0; i < user_int_variables.size(); i++){
                    if(left_side.value.equals(user_int_variables.get(i).name)){
                        left = user_int_variables.get(i).value;
                        quotient  = left / right;
                        user_int_variables.get(i).value = quotient;
                    }
                }
                break;
        }
    }

    public static void do_array_functions(String function_name){
        Token obj = the_stack.pop();
        Token op = the_stack.pop();
        Token array = the_stack.pop();
        //if you are adding to the array
        if(function_name.equals("put")){
            for(int i = 0; i < user_arrays.size(); i++){
                if(array.value.equals(user_arrays.get(i).name)){
                    switch(obj.type){
                        case DICE:
                            user_arrays.get(i).value.add(Integer.toString(get_dice_value(obj)));
                            break;
                        case LANGUAGE:
                            user_arrays.get(i).value.add(obj.value);
                            break;
                        case IDENTIFIER:
                            for(int j = 0; j < user_String_variables.size(); j++){
                                if((user_String_variables.get(i).name).equals(obj.value)){
                                    user_arrays.get(j).value.add(user_String_variables.get(j).value);
                                }
                            }
                            for(int k = 0; k < user_int_variables.size(); k++){
                                if((user_int_variables.get(i).name).equals(obj.value)){
                                    user_arrays.get(k).value.add(Integer.toString(user_int_variables.get(k).value));
                                }
                            }
                            break;
                    }
                }
            }
        }
        //check if you are removing from the array
        //TODO: check if this works
        else if(function_name.equals("take")){
            for(int i = 0; i < user_arrays.size(); i++){
                if(array.value.equals(user_arrays.get(i).name)){
                    user_arrays.get(i).value.remove(obj.value);
                }
            }
        }
        else if(function_name.equals("show")){
            ArrayList<String> temp = new ArrayList<String>();
            for(int i = 0; i < user_arrays.size(); i++){
                if(array.value.equals(user_arrays.get(i).name)){
                    temp = user_arrays.get(i).value;
                }
            }
            System.out.print("[");
            for(int j = 0; j < temp.size()-1; j++){
                System.out.print(temp.get(j) + ", ");
            }
            System.out.print(temp.get(temp.size()-1));
            System.out.println("]");
        }
    }

    public static void do_while(){
        Token duration_amount = the_stack.pop();
        Token start_tilde = the_stack.pop();
        //Token temp = the_stack.pop();
        boolean if_conditional = true;
        int duration_int = 0;
        Stack<Token> temp_stack = new Stack<Token>();
        ArrayList<Token> temp_array = new ArrayList<Token>();
        switch(duration_amount.type){
            //if the duration amount is a number calculate the number
            case DICE:
                duration_int = get_dice_value(duration_amount);
                break;

            case BOOLEAN:
                if(duration_amount.value.equals("on")){
                    if_conditional = true;
                }
                //If the boolean is false, do not run 
                else if(duration_amount.value.equals("off")){
                    if_conditional = false;
                }
            case IDENTIFIER:
                //get the integer variable and save the value in int 
                for(int i = 0; i < user_int_variables.size(); i++){
                    if(user_int_variables.get(i).name.equals(duration_amount.value)){
                        duration_int = user_int_variables.get(i).value;
                    }
                }
                break;
        }
        //iterate through while there is no second tilde 
        Token curr = the_stack.pop();
        while(!curr.value.equals("~")){
            //create a new temp array for the code that is being repeated, we are making it an array so it is easily reversable 
            temp_array.add(curr);
            curr = the_stack.pop();
        }
        //create the stack by reversing the arraylist 
        for(int i = temp_array.size() -1; i >= 0; i--){
            temp_stack.add(temp_array.get(i));
        }
        //if there is an integer instead of a boolean
        if(duration_int > 0){
            for(int j = 0; j < duration_int; j++){
                if(temp_stack.size() == 0 ){
                    for(int i = temp_array.size() -1; i >= 0; i--){
                        temp_stack.add(temp_array.get(i));
                    }
                }
                parse_stack(temp_stack);
            }
        }
        //If it is a while true loop
        else if(if_conditional){
            while(true){
                //if the temp stack is empty refill it 
                if(temp_stack.size() == 0 ){
                    for(int i = temp_array.size() -1; i >= 0; i--){
                        temp_stack.add(temp_array.get(i));
                    }
                }
                parse_stack(temp_stack);
            }
        }
        //If the main stack still has Tokens continue to run it 
        if(parse.the_stack_tm.size() > 0 ){
            parse_stack(parse.the_stack_tm);
        }
    }
}
