/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MP4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Deus
 */

public class Atibula4{
}

class DFA {
    private static ArrayList<String> all_data;
    private static ArrayList<String> result;
    private static ArrayList<TestCase> test_case;
    private static ArrayList<String> reserved_words;
    private static int[][] varDeclaration;
    private static int[][] funcDeclaration;
    private static int[][] funcDefinition;
    
    public static void main(String[] args) {
        getInput();
        test_case = analyzeInput();
        result = new ArrayList();
        
        //populate state diagrams
        populateVarDecStateDiag();
        populateFuncDecStateDiag();
        populateFuncDefStateDiag();
        populateReservedWords();
        
        for (TestCase tc : test_case) {
            int type = tc.getType();
            String given = tc.getGiven();
            ArrayList arr = parseGiven(given);
            System.out.println("type: " + type);
            System.out.println("given: " + given);
            if(type == 1){
                if(runVarDecStateDiagram(arr)){
                    result.add("YES");
                }
                else{
                    result.add("NO");
                }
            }
            else if(type == 2){
                if(runFuncDecStateDiagram(arr)){
                    result.add("YES");
                }
                else{
                    result.add("NO");
                }
            }
            else if(type == 3){
                if(runFuncDefStateDiagram(arr)){
                    result.add("YES");
                }
                else{
                    result.add("NO");
                }
            }
            else{
                System.out.println("invalid input type");
            }
        }
        getOutput();
    }
    
    private static ArrayList<TestCase> analyzeInput(){
        ArrayList<TestCase> input = new ArrayList<>();
        
        if(all_data.size() > 0){
            TestCase.setNumOfTestCases(Integer.parseInt(all_data.get(0)));
            try{
                for (int i = 1; i < all_data.size(); i+=2){
                    int type = Integer.parseInt(all_data.get(i));
                    String given = all_data.get(i+1);
   
                    TestCase tc = new TestCase(type,given);
                    input.add(tc);
                }
            }
            catch(Exception e){
                System.out.println(e + ": Invalid input format!");
            }
        }
        else{
            System.out.println("Warning: Empty input file!");
        }
        
        return input;
    }
    
    private static void getInput(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter name of input file: ");
        String input = sc.nextLine();
        readFile(input);
    }
    
    private static void getOutput(){
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter name of output file: ");
        String output = sc.nextLine();
        writeFile(output);
    }
    
    private static void readFile(String input_filename){
        FileReader fr;
        BufferedReader br;
        String currentLine;
        all_data = new ArrayList<>();
        
        try {
            fr = new FileReader(input_filename);
            br = new BufferedReader(fr);
            
            while ((currentLine = br.readLine()) != null){ //reading file by line
                all_data.add(currentLine);            //combining lines into one arraylist
            }
        }catch (IOException e) {
            System.out.println(e);
            System.out.println("File not found");
        }
    }
    
    private static void writeFile(String output_filename){
        try{
            File output = new File(output_filename);//create file
           
            if(!output.exists()){
                output.createNewFile();
            }
            //creates a filewritter object
            FileWriter writer = new FileWriter(output);
            
            for (String my_list1 : result) {
                writer.write(my_list1);
                writer.write(System.getProperty( "line.separator" ));
                System.out.println(my_list1);
            }
            writer.flush();
            writer.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    } 
    
    private static ArrayList<String> parseGiven(String given){
        ArrayList<String> parsed = new ArrayList<>();
        String str = new String();
        String[] arr = given.split(" ");
       
        for (String s : arr) {
            parsed.addAll(splitString(s));
        }
        
        return parsed;
    }
    
    private static ArrayList<String> splitString(String given){
        ArrayList<String> arr = new ArrayList<>();
        
        int start = 0;
        for (int i = 0; i < given.length(); i++) {
            char ch = given.charAt(i);
            if(isSpecialCharacter(ch)){ //substring based on special character
                String s = given.substring(start,i);
                if(!s.equals("")){
                    arr.add(s);
                }
                arr.add(Character.toString(ch));
                start = i+1;
            }
        }
        String s = given.substring(start,given.length());
        if(!s.equals("")){
            arr.add(s);
        }
        
        return arr;
    }
    
    private static boolean isSpecialCharacter(char ch){
        return ch == '=' || ch == ',' || ch == '(' || ch == ')' || ch == ';' || ch == 39 
                || ch == '[' || ch == ']' || ch == '{' || ch == '}' || ch == '*'
                || ch == '+' || ch == '/' || ch == '-';
    }
    
    private static void populateReservedWords(){
        String words[] = {"auto","const","int","short","struct","unsigned","doouble","float",
                          "break","continue","long","signed","switch","void","else","for",
                          "case","default","register","sizeof","typedef","volatile","enum","goto",
                          "char","do","return","static","union","while","extern","if"};
        reserved_words = new ArrayList<>(Arrays.asList(words));
    }
    
    private static void populateVarDecStateDiag(){
        varDeclaration = new int[][]{ 
            {1,18,18,18,18,18,18,18,18,18,18,18,18,18},
            {18,2,18,18,18,18,18,18,18,18,18,18,18,18},
            {18,18,3,18,18,18,17,1,7,18,18,18,18,18},
            {18,19,18,4,4,18,18,18,18,18,18,18,5,18},
            {18,18,18,18,18,18,17,1,18,18,18,18,18,18},
            {18,18,18,18,18,6,18,18,18,18,18,18,18,18},
            {18,18,18,18,18,18,18,18,18,18,18,18,4,18},
            {18,18,18,8,18,18,18,18,18,18,18,18,18,18},
            {18,18,18,18,18,18,18,18,18,9,18,18,18,18},
            {18,18,10,18,18,18,17,1,18,18,18,18,18,18},
            {18,18,18,18,18,18,18,18,18,18,11,18,18,18},
            {18,13,18,13,13,18,18,18,18,18,18,4,12,18},
            {18,18,18,18,18,14,18,16,18,18,18,18,18,18},
            {18,18,18,18,18,18,18,16,18,18,18,4,18,18},
            {18,18,18,18,18,18,18,18,18,18,18,18,15,18},
            {18,18,18,18,18,18,18,16,18,18,18,4,18,18},
            {18,13,18,13,13,18,18,18,18,18,18,18,12,18},
            {1,2,18,18,18,18,17,18,18,18,18,18,18,18},
            {18,18,18,18,18,18,18,18,18,18,18,18,18,18},
            {18,18,3,18,18,18,17,18,18,18,18,18,18,18}, 
        };
    }
    
    private static void populateFuncDecStateDiag(){
        funcDeclaration = new int[][]{ 
            {1,12,12,12,12,12,12,12,12,12,12,12},
            {12,2,12,12,12,12,12,12,12,12,12,12},
            {12,12,3,12,12,12,12,12,12,12,12,12},
            {12,12,12,4,5,12,12,12,12,12,12,12},
            {12,12,12,12,12,12,1,12,12,12,11,12},
            {12,12,12,4,12,7,6,12,12,12,12,12},
            {12,12,12,12,5,12,12,12,12,12,12,12},
            {12,12,12,4,12,12,6,8,12,12,12,12},
            {12,12,12,12,12,12,12,12,10,9,12,12},
            {12,12,12,12,12,12,12,12,10,12,12,12},
            {12,12,12,4,12,12,6,12,12,12,12,12},
            {1,12,12,12,12,12,12,12,12,12,12,12},
            {12,12,12,12,12,12,12,12,12,12,12,12},
            
        };
    }
    
    private static void populateFuncDefStateDiag(){
        funcDefinition = new int[][]{
            {1,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,2,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,44,3,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,4,8,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,5,44,44,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,14,15,44,44,6,44,44,44,44,44,44,44,44,5,36,44,44,44},
            {1,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,7,44,44,44,44},
            {1,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,7,44,44,44,44},
            {44,44,44,44,44,9,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,4,44,44,13,44,44,44,44,44,44,44,10,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,12,44,44,44,44,44,11,44,44,44,44,44,44},
            {44,44,44,4,44,44,13,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,11,44,44,44,44,44,44},
            {44,44,44,44,8,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,15,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,14,44,44,44,44,44,44,16,21,44,44,5,44,40,42,44},
            {44,44,44,44,44,20,44,44,44,19,19,44,17,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,18,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,19,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,14,44,44,44,44,44,44,44,44,44,32,5,44,32,32,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,44,16,44,44,32,5,44,32,32,44},
            {44,44,44,44,44,44,44,44,44,22,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,23,44,44,44,44,44,44},
            {44,44,44,44,44,44,14,44,44,44,44,44,44,24,44,44,44,5,44,44,44,44},
            {44,44,44,44,44,44,44,25,44,44,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,30,44,44,31,30,30,44,26,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,27,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,28,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,29,44,31,44,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,30,44,44,44,30,30,44,26,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,29,44,31,44,44,44,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,5,44,44,44,44},
            {44,44,44,44,44,33,44,44,44,33,33,44,34,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,32,5,44,32,32,44},
            {44,44,44,44,44,44,44,44,44,44,44,35,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,33,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,37,44,44,44,37,37,44,39,44,44,44,44,5,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,32,5,44,32,32,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,37,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,38,44,44,44,44,44,44,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,41,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,5,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,43,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,5,44,44,44,44},
            {44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44,44},
        };
    }
    
    private static boolean runVarDecStateDiagram(ArrayList<String> given){
        int final_state = 17;
        String prev= new String();
        String next= new String();
        String prev_prev = new String();
        String next_next = new String();
        boolean isArray = false;
        ArrayList<String> var_list = new ArrayList<>(); 
        
        int current = 0;
        for (int i = 0; i < given.size(); i++) {
            String s = given.get(i);
            if(i-1 > 0){
                prev_prev = given.get(i-2);
            }
            if(i > 0){
                prev = given.get(i-1);
            }
            if(i+1 < given.size()){
                next = given.get(i+1);
            }
            if(i+2 < given.size()){
                next_next = given.get(i+2);
            }
            if(isDataType(s)){
                current = varDeclaration[current][0];
                if(nextIsPointer(next,next_next)){
                    i++;
                }
//                System.out.println("datatype: " + current);
            }
            else if(isIdentifier(prev_prev,prev,s,var_list,isArray)){
                current = varDeclaration[current][1];
                if(isDataType(prev) || (prev.equals("*") && isDataType(prev_prev))
                        || (!isArray && prev.equals(","))){ //immediately add identifier only at the time when it was defined. 
                    var_list.add(s);
                }
//                System.out.println("identifier: " + current + " --> " + s);
            }
            else if(s.equals("=")){
                current = varDeclaration[current][2];
//                System.out.println("equals: " + current);
            }
            else if(isInteger(s,prev)){
                current = varDeclaration[current][3];
//                System.out.println("integer: " + current);
            }
            else if(isDouble(s,prev)){
                current = varDeclaration[current][4];
//                System.out.println("double: " + current);
            }
            else if(isCharacter(s,prev,next,isArray)){
                current = varDeclaration[current][5];
//                System.out.println("character: " + current);
            }
            else if(s.equals(";")){
                current = varDeclaration[current][6];
                isArray = false;
//                System.out.println("semi: " + current);
            }
            else if(s.equals(",")){
                current = varDeclaration[current][7];
//                System.out.println("comma: " + current);
            }
            else if(s.equals("[")){
                current = varDeclaration[current][8];
                isArray = true; //when it is an array; used for comma(',') not to read as a character when defining an array
//                System.out.println("open brack: " + current);
            }
            else if(s.equals("]")){
                current = varDeclaration[current][9];
//                System.out.println("close brack: " + current);
            }
            else if(s.equals("{")){
                current = varDeclaration[current][10];
//                System.out.println("open curly: " + current);
            }
            else if(s.equals("}")){
                current = varDeclaration[current][11];
//                System.out.println("close curly: " + current);
            }
            else if(s.equals("'")){
                current = varDeclaration[current][12];
//                System.out.println("quote: " + current);
            }
            else{ //others, for safety
                current = varDeclaration[current][13];
//                System.out.println("unknown input: " + current);
            }
        }
//        System.out.println("current: " + current);
        
        return current==final_state && isValidList(var_list);
    }
    
    private static boolean runFuncDecStateDiagram(ArrayList<String> given){
        int final_state = 11;
        String prev = new String();
        String next = new String();
        ArrayList<String> var_list = new ArrayList<>();
        ArrayList<String> func_list = new ArrayList<>();
        boolean hasParen = false;
        boolean valid_var_list = true;
        
        int current = 0;
        for (int i = 0; i < given.size(); i++) {
            String s = given.get(i);
            if(i > 0){
                prev = given.get(i-1);
            }
            if(i+1 < given.size()){
                next = given.get(i+1);
            }
            if(isReturnType(s,hasParen)){
                current = funcDeclaration[current][0];
//                System.out.println("Return Type: " + current);
            }
            else if(isFuncName(s,prev,next,hasParen)){
                current = funcDeclaration[current][1];
//                System.out.println("Function name: " + current);
                func_list.add(s);
            }
            else if(s.equals("(")){
                current = funcDeclaration[current][2];
//                System.out.println("open paren: " + current);
                hasParen = true;
            }
            else if(s.equals(")")){
                current = funcDeclaration[current][3];
//                System.out.println("close paren: " + current);
                if(!isValidList(var_list)){ //check if there is no duplicate of variable
                    valid_var_list = false;
                }
                var_list = new ArrayList<>(); //variable list will reset for every function declaration
            }
            else if(isDataType(s)){
                current = funcDeclaration[current][4];
//                System.out.println("datatype: " + current);
            }
            else if(isParamIdentifier(s,prev)){
                current = funcDeclaration[current][5];
//                System.out.println("identifier: " + current + " --> " + s);
                var_list.add(s);
            }
            else if(s.equals(",")){
                current = funcDeclaration[current][6];
//                System.out.println("comma: " + current);
            }
            else if(s.equals("[")){
                current = funcDeclaration[current][7];
//                System.out.println("open brack: " + current);
            }
            else if(s.equals("]")){
                current = funcDeclaration[current][8];
//                System.out.println("close brack: " + current);
            }
            else if(isInteger(s,prev)){
                current = funcDeclaration[current][9];
//                System.out.println("integer: " + current);
            }
            else if(s.equals(";")){
                current = funcDeclaration[current][10];
                hasParen = false;
//                System.out.println("semicolon: " + current);
            }
            else if(s.equals("*")){
                if (!isDataType(prev) && !prev.equals("void")){
                    current = funcDeclaration[current][11];
                }
            }
            else{
                current = funcDeclaration[current][11];
//                System.out.println("unknown input: " + current + " --> " + s);
            }
        }
//        System.out.println("current: " + current);
        return current==final_state && valid_var_list && isValidList(func_list);
    }
    
    private static boolean runFuncDefStateDiagram(ArrayList<String> given){
        int final_state = 6;
        int final_state2 = 7;
        String prev_prev = new String();
        String prev = new String();
        String next = new String();
        String next_next = new String();
        String return_type = new String();
        ArrayList<String> var_list = new ArrayList<>();
        ArrayList<String> func_list = new ArrayList<>();
        boolean hasParen = false;
        boolean valid_var_list = true;
        boolean isArray = false;
        boolean hasReturn = false;
        boolean returningVoid = false;
        int current = 0;
        
        for (int i = 0; i < given.size(); i++) {
            String s = given.get(i);
            if(i-1 > 0){
                prev_prev = given.get(i-2);
            }
            if(i > 0){
                prev = given.get(i-1);
            }
            if(i+1 < given.size()){
                next = given.get(i+1);
            }
            if(i+2 < given.size()){
                next_next = given.get(i+2);
            }
            if(isReturnType(s,hasParen)){
                current = funcDefinition[current][0];
                return_type = s;
//                System.out.println("Return Type: " + current);
            }
            else if(isFuncName(s,prev,next,hasParen)){
                current = funcDefinition[current][1];
//                System.out.println("Function name: " + current);
                func_list.add(s);
            }
            else if(s.equals("(")){
                current = funcDefinition[current][2];
//                System.out.println("open paren: " + current);
                hasParen = true;
            }
            else if(s.equals(")")){
                current = funcDefinition[current][3];
//                System.out.println("close paren: " + current);
            }
            else if(isDataType(s)){
                current = funcDefinition[current][4];
                if(nextIsPointer(next,next_next)){
                    i++;
                }
//                System.out.println("datatype: " + current);
            }
            else if(isIdentifier(prev_prev, prev,s,var_list,isArray)){
                current = funcDefinition[current][5];
                if(isDataType(prev) || (prev.equals("*") && isDataType(prev_prev))
                        || (!isArray && prev.equals(","))){  
                    var_list.add(s); //immediately add identifier only at the time when it is defined.
                }
//                System.out.println("identifier: " + current +" --> "+ s);
            }
            else if(s.equals(",")){
                current = funcDefinition[current][6];
//                System.out.println("comma: " + current);
            }
            else if(s.equals("{")){
                current = funcDefinition[current][7];
//                System.out.println("open brace: " + current);
            }
            else if(s.equals("}")){
                current = funcDefinition[current][8];
                if(!isArray){ //if the close brace is not part of the array, means its the end of the function
                    if(!isValidList(var_list)){ //check if there is no duplicate of variable
                        valid_var_list = false;    //if the variabe list is valid, then invalid function definition
                    }
                    if((return_type.equals("void") && hasReturn) || (!return_type.equals("void") && !hasReturn)){ 
//                        System.out.println("Returning void");  //if return type is void and has a return statement - invalid
                        returningVoid = true; 
                    }
                    var_list = new ArrayList<>(); //variable list will reset for every function definition
                    hasParen = false; //hasParen flag will also be reset
                    hasReturn = false;
                } 
//                System.out.println("close brace: " + current);
            }
            else if(isInteger(s,prev)){
                current = funcDefinition[current][9];
//                System.out.println("integer: " + current);
            }
            else if(isDouble(s,prev)){
                current = funcDefinition[current][10];
//                System.out.println("double: " + current);
            }
            else if(isCharacter(s,prev,next,isArray)){
                current = funcDefinition[current][11];
//                System.out.println("character: " + current);
            }
            else if(s.equals("'")){
                current = funcDefinition[current][12];
//                System.out.println("quote: " + current);
            }
            else if(s.equals("=")){
                current = funcDefinition[current][13];
//                System.out.println("equal sign: " + current);
            }
            else if(s.equals("[")){
                current = funcDefinition[current][14];
                isArray = true; //used for comma(',') not to read as a character when defining an array w/ elements separated by comma
//                System.out.println("open brack: " + current);
            }
            else if(s.equals("]")){
                current = funcDefinition[current][15];
//                System.out.println("close brack: " + current);
            }
            else if(isOperator(s)){
                current = funcDefinition[current][16];
//                System.out.println("operator: " + current);
            }
            else if(s.equals(";")){
                current = funcDefinition[current][17];
                isArray = false;
//                System.out.println("semicolon: " + current);
            }
            else if(s.equals("return")){
                current = funcDefinition[current][18];
                hasReturn = true;
//                System.out.println("return: " + current);
            }
            else if(s.equals("+")){
                current = funcDefinition[current][19];
//                System.out.println("plus sign: " + current + " --> " + s );
            }
            else if(s.equals("-")){
                current = funcDefinition[current][20];
//                System.out.println("minus sign: " + current + " --> " + s );
            }
            else{
                current = funcDefinition[current][21];
//                System.out.println("unknown input: " + current + " --> " + s );
            }
        }
        
        return (current == final_state || current == final_state2) &&  
                (valid_var_list && isValidList(func_list) && !returningVoid);
         
    }
    
    private static boolean nextIsPointer(String next, String next_next){
        return (next.equals("*") && isValidIdentifier(next_next));
    }
    
    private static boolean isOperator(String s){
        return (s.equals("*") || s.equals("/")); 
    }
    
    private static boolean isReturnType(String s,boolean hasParen){
        return (s.equals("int") || s.equals("double") || s.equals("char") || s.equals("float") || s.equals("void"))
                && (!hasParen);
    }
    
    private static boolean isDataType(String s){
        return (s.equals("int") || s.equals("double") || s.equals("char") || s.equals("float"));
    }
    
    private static boolean isFuncName(String s, String prev, String next, boolean hasParen){
        return ((isReturnType(prev,hasParen) || next.equals("(")) && isValidIdentifier(s));
    }
    
    private static boolean isIdentifier(String prev_prev, String prev, String s, ArrayList var_list, boolean isArray){
        if ((!isInteger(s,prev) && !isDouble(s,prev) && isValidIdentifier(s)) && !isDataType(s) && !isOperator(s)
                && !prev.equals("'")){
            if (!(isDataType(prev) || (prev.equals("*") && isDataType(prev_prev)) 
                    || (!isArray && prev.equals(",")))){ //if variable used as a value; check if the variable exist
                return var_list.contains(s); //returns false if variable is not defined
            }
            return true;
        }
        return false;
    }
    
    private static boolean isParamIdentifier(String s, String prev){
        return ((isDataType(prev) || prev.equals("*")) && (!isInteger(s,prev) && !isDouble(s,prev) && isValidIdentifier(s)));
    }
    
    private static boolean isValidIdentifier(String s){
        if(isReservedWord(s)){
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(i == 0 && !Character.isLetter(c) && c != '_'){
                return false;
            }
            if(i > 0){
                if(!Character.isLetterOrDigit(c) && c != '_'){
                    return false;
                }
            }
        }
        return true;
    }
    
    private static boolean isInteger(String s,String prev){
        if(prev.equals("'")){
            return false;
        }
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(i == 0 && c == '-') {
                if(prev.equals("[")){ //for array, integer must be positive
                    return false;
                }
                if(s.length() == 1) return false;
                else continue;
            }
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
    
    private static boolean isDouble(String s,String prev){
        if(prev.equals("'")){
            return false;
        }
        try{  
          double d = Double.parseDouble(s);  
        }  
        catch(NumberFormatException nfe)  {  
          return false;  
        }  
        return true;  
    }
    
    private static boolean isCharacter(String s,String prev,String next,boolean isArray){
        if(s.equals(",") && prev.equals("'") && next.equals("'") && isArray){ //comma should not be read as a character
            return false;                                                     //when defining an array
        }
        return s.length()==1 && prev.equals("'") && next.equals("'");
    }
    
    private static boolean isValidList(ArrayList<String> list){
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            for (int j = i+1; j < list.size(); j++) {
                if(s.equals(list.get(j))){
                    return false;
                }
            }
        }
        return true;
    }
    
    private static boolean isReservedWord(String s){
      return reserved_words.contains(s);
    }
}

class TestCase{
    private int type;
    private String given;
    private static int num_of_testcases;
    
    TestCase(int type, String given){
        this.type = type;
        this.given = given;
    }
    
    //setters
    public void setType(int type){
        this.type = type;
    }
    public void setGiven(String given){
        this.given = given;
    }
    public static void setNumOfTestCases(int num){
        num_of_testcases = num; 
    }
    
    //getters
    public int getType(){
        return this.type;
    }
    public String getGiven(){
        return this.given;
    }
    public static int getNumOfTestCases(){
        return num_of_testcases;
    }
}
