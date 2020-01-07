import java.io.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Solves the Knapsack problem using a simple implementation of Hill Climbing algorithm.
 */

public class Version1{
    
    private static final int AMOUNT = 200; // amount of items to choose from
    private static final int MAX_VALUE = 20; // maximum value of the item
    private static final int MAX_WEIGHT = 50; // maximum value of the item
    private static final String VALUES_FILENAME = "values.txt";
    private static final String WEIGHTS_FILENAME = "weights.txt";
    
    public static void main(String[] args) {
        System.out.println("\n   Solving the Knapsack problem\n");
    
        // Can be used to create files with the test items
        generateTextFiles();
        
        System.out.println("Total number of items to choose from is " + AMOUNT);
        System.out.print("An item can cost up to " + MAX_VALUE);
        System.out.println("$ and weight maximum " + MAX_WEIGHT + " kg");
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter total capacity of the knapsack: ");
        int maxCapacity = sc.nextInt();
        
        // creating empty solution storage
        StringBuilder solution = new StringBuilder(AMOUNT);
        for (int i = 0; i < AMOUNT; i++) {
            solution.append("0");
        }
        
        // generating initial solution
        Random rand = new Random();
        for (int i = 0; i < maxCapacity / MAX_WEIGHT; i++) {
            binaryNotAtPosition(solution, rand.nextInt(AMOUNT));
        }
        System.out.println("\nAn initial solution: ");
        printOutSolution(solution.toString());
        System.out.println("\nInitial value: " + countFromFile(solution, VALUES_FILENAME));
        System.out.println("Initial weight: " + countFromFile(solution, WEIGHTS_FILENAME));
        System.out.println("\nGenerating an optimal solution...\n");
        
        String s = findSolution(solution.toString(), maxCapacity);
        solution = new StringBuilder(s);
        System.out.println("Optimal solution: ");
        printOutSolution(s);
        System.out.println();
        System.out.println("Optimal value: " + countFromFile(solution, VALUES_FILENAME));
        System.out.println("Optimal weight: " + countFromFile(solution, WEIGHTS_FILENAME));
    }
    
    /** Recursive function that finds best neighbour solution and compares it with a current one.
    Returns when no better neighbour solutions can be found */
    private static String findSolution(String currentSolution, int maxWeight) {
        String betterSolution = findBestNeighbour(currentSolution, maxWeight);
        if (betterSolution.equals(currentSolution)) return currentSolution;
        else return findSolution(betterSolution, maxWeight);
    }
    
    /** Iterates over all neighbour solutions and finds one with biggest value that fits into weight constraint */
    private static String findBestNeighbour(String currentSolution, int maxWeight) {
        StringBuilder sb = new StringBuilder(currentSolution);
        int currentBestValue = countFromFile(sb, VALUES_FILENAME);
        int currentBestWeight = countFromFile(sb, WEIGHTS_FILENAME);
        String bestNeighbour = currentSolution;
        
        for (int i = 0; i < AMOUNT; i++) {
            StringBuilder neighbour = binaryNotAtPosition(sb, i);
            int neighbourWeight = countFromFile(neighbour, WEIGHTS_FILENAME);
            int neighbourValue = countFromFile(neighbour, VALUES_FILENAME);
            
            if (neighbourValue > currentBestValue && neighbourWeight <= maxWeight) {
                currentBestValue = neighbourValue;
                currentBestWeight = neighbourWeight;
                bestNeighbour = neighbour.toString();
            } else if (neighbourValue == currentBestValue && neighbourWeight < currentBestWeight) {
                currentBestValue = neighbourValue;
                currentBestWeight = neighbourWeight;
                bestNeighbour = neighbour.toString();
            }
            sb = binaryNotAtPosition(neighbour, i);
        }
        return bestNeighbour;
    }
    
    /** Generates two text files filled with integers according to input parameters */
    private static void generateTextFiles() {
        System.out.print("Generating test files... ");
        randomGenerator(VALUES_FILENAME, MAX_VALUE);
        randomGenerator(WEIGHTS_FILENAME, MAX_WEIGHT);
        System.out.println("Done!\n");
    }
    
    /** Creates a text file with a certain amount of lines with one random integer that can be a max value */
    private static void randomGenerator(String pathname, int maxValue) {
        try {
            File file = new File(pathname);
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            Random rand = new Random();
            for (int i = 0; i < AMOUNT; i++) {
                bw.write((rand.nextInt(maxValue) + 1) + "\n");
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** Checks the StringBuilder for binary representation of item set ("1" for retrieve, "0" for skip),
    retrieves integers from corresponding lines of input file and returns their sum */
    private static int countFromFile(StringBuilder itemSet, String filename) {
        int sum = 0;
        for (int i = 0; i < AMOUNT; i++) {
            if (itemSet.charAt(i) == '1')
                sum += readIntFromFile(filename, i);
        }
        return sum;
    }

    /** Performs a binary NOT operation on a selected item in a binary representation of item set */
    private static StringBuilder binaryNotAtPosition(StringBuilder sb, int pos) {
        if (sb.charAt(pos) == '0')
            sb.setCharAt(pos, '1');
        else sb.setCharAt(pos, '0');
        return sb;
    }
    
    /** Reads a String from a particular line of a file */
    private static int readIntFromFile(String fileName, int lineNumber) {
        int value = 0;
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader rb = new BufferedReader(fr);
            for (int currentLine = 0; currentLine <= lineNumber; currentLine++) {
                if (currentLine == lineNumber) {
                    value = Integer.parseInt(rb.readLine());
                } else
                    rb.readLine();
            }
            rb.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
    
    /** Prints out String 20 characters at the time */
    private static void printOutSolution(String s) {
        boolean printFinished = false;
        int a = 0;
        int b = 10;
        while (!printFinished) {
            System.out.println(s.substring(a, b) + " " + s.substring(a + 10, b + 10));
            a += 20;
            b += 20;
            if (a >= AMOUNT) printFinished = true;
        }
    }}
