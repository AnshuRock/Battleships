package battleship;

import java.util.Locale;
import java.util.Scanner;

public class Main {

    static String[][] grid = new String[10][10];
    static String[][] grid2 = new String[10][10];
    static String[][] grid3 = new String[10][10];
    static String[][] grid4 = new String[10][10];
    static int counter1 = 0;
    static int counter2 = 0;
    static Scanner read = new Scanner(System.in);
    static boolean isValid = false;

    enum Tanks {
        Aircraft("Aircraft Carrier", 5),
        Battleship("Battleship", 4),
        Submarine("Submarine", 3),
        Cruiser("Cruiser", 3),
        Destroyer("Destroyer", 2);

        private final String name;
        private final int numberOfCells;

        Tanks(String name, int numberOfCells) {
            this.name = name;
            this.numberOfCells = numberOfCells;
        }

        public String getName() {
            return name;
        }

        public int getNumberOfCells() {
            return numberOfCells;
        }
    }

    public static void main(String[] args) {
        // write your code here
        createGrid(grid);
        createGrid(grid2);
        createGrid(grid3);
        createGrid(grid4);

        String cord1, cord2, cord3;
        for(int i = 1; i <= 2; i++) {
            System.out.println("Player " + i + ", place your ships to the game field\n");
            if (i == 1){
                displayField(grid);
            } else {
                displayField(grid3);
            }
            System.out.println();

            for (Tanks tank : Tanks.values()) {
                System.out.printf("\nEnter the coordinates of the %s (%d cells):", tank.getName(), tank.getNumberOfCells());
                System.out.println();
                while (!isValid) {
                    cord1 = read.next();
                    cord2 = read.next();
                    cord1 = cord1.toUpperCase(Locale.ROOT);
                    cord2 = cord2.toUpperCase(Locale.ROOT);
                    if (i == 1) {
                        putShips(cord1, cord2, tank.getNumberOfCells(), grid);
                        //displayField(grid);
                    } else {
                        putShips(cord1, cord2, tank.getNumberOfCells(), grid3);
                        //displayField(grid3);
                    }
                }
                if (i == 1) {
                    displayField(grid);
                } else {
                    displayField(grid3);
                }

                System.out.println();
                isValid = false;
            }
               pressEnterKeyToContinue();
        }

        do {
            isValid = false;
            while (!isValid) {

                for (int i = 1; i < 3; i++) {
                    if (i == 1) {
                        displayField(grid4);
                        System.out.println("---------------------");
                        displayField(grid);
                    } else {
                        displayField(grid2);
                        System.out.println("---------------------");
                        displayField(grid3);
                    }
                    System.out.println("Player " + i + ", it is your turn:");
                    cord3 = read.next();
                    if(i == 1) {
                        System.out.println(attack(cord3, grid3, grid4));
                    } else {
                        System.out.println(attack(cord3, grid, grid2));
                    }
                    if (counter1 == 17 || counter2 == 17) {
                        isValid = true;
                        return;
                    }
                    pressEnterKeyToContinue();
                }
            }
        } while (counter1 != 17 || counter2 != 17);
        }
        public static String attack (String myShot, String[][] warGround, String[][] fogGround){
           myShot = myShot.toUpperCase(Locale.ROOT);
            int row = myShot.charAt(0) - 'A';
            int column = Integer.parseInt(myShot.substring(1)) - 1;

            String msgToConsole;

            if (row > 9 || column > 9) {
                msgToConsole = "\nError! You entered the wrong coordinates! Try again:\n";
                isValid = false;
            } else {
                if (warGround[row][column].equals("O") || warGround[row][column].equals("X")) {
                    if (!warGround[row][column].equals("X")) {
                        if (warGround == grid) {
                            counter1++;
                        } else {
                            counter2++;
                        }
                    }

                    warGround[row][column] = "X";
                    fogGround[row][column] = "X";

                    isValid = true;
                    System.out.println();
                   // fogOfWar(fogGround);

                    msgToConsole = "\nYou hit a ship!";

                    try {
                        if (row == 0) {
                            if (!warGround[row + 1][column].equals("O") && !warGround[row + 1][column - 1].equals("O")
                                    && !warGround[row][column + 1].equals("O")) {
                                msgToConsole = "\"You sank a ship! Specify a new target:\"";
                            }
                        } else if (row == 9) {
                            if (!warGround[row - 1][column].equals("O") && !warGround[row][column - 1].equals("O")
                                     && !warGround[row][column + 1].equals("O")) {
                                msgToConsole = "You sank a ship! Specify a new target:";
                            }
                        } else {
                            if (!warGround[row - 1][column].equals("O")  && !warGround[row + 1][column].equals("O")
                                    && !warGround[row][column + 1].equals("O") && !warGround[row][column - 1].equals("O")) {
                                msgToConsole = "You sank a ship! Specify a new target:";
                            }
                        }
                    } catch (IndexOutOfBoundsException ignored) {
                        msgToConsole = "You sank a ship! Specify a new target:";
                    }

                } else {
                    warGround[row][column] = "M";
                    fogGround[row][column] = "M";

                    isValid = true;
                    System.out.println();
                   // fogOfWar(fogGround);

                    msgToConsole = "\nYou missed!";

                }
            }
            if (counter2 == 17) {
                msgToConsole = "\nPlayer 1, You sank the last ship. You won. Congratulations!\n";
            } else if (counter1 == 17){
                msgToConsole ="\nPlayer 2, You sank the last ship. You won. Congratulations!\n";
            }
            return msgToConsole;
        }
        public static boolean putShips (String startNode, String endNode,int validLength, String [][] gridPlayer1Or2){
            int startNodeRow = startNode.charAt(0) - 'A';
            int startNodeColumn = Integer.parseInt(startNode.substring(1)) - 1;

            int endNodeRow = endNode.charAt(0) - 'A';
            int endNodeColumn = Integer.parseInt(endNode.substring(1)) - 1;

            int startRow = Math.min(startNodeRow, endNodeRow);
            int endRow = Math.max(startNodeRow, endNodeRow);

            int startColumn = Math.min(startNodeColumn, endNodeColumn);
            int endColumn = Math.max(startNodeColumn, endNodeColumn);

            if (startRow == endRow) {
                if ((endColumn - startColumn) + 1 == validLength) {

                    for (int i = startColumn; i <= endColumn; i++) {
                        if (isTooClose(startRow, i, gridPlayer1Or2)) {
                            System.out.println("Error! You placed it too close to another one. Try again:");
                            return false;
                            //isValid = false;
                        }
                    }
                    isValid = true;
                    for (int i = startColumn; i <= endColumn; i++) {
                        gridPlayer1Or2[startRow][i] = "O";
                        //isValid = false;
                    }

                } else {
                    System.out.println("Error! Wrong length of the Submarine! Try again:");
                }
            } else if (startColumn == endColumn) {
                if ((endRow - startRow) + 1 == validLength) {

                        for (int i = startRow; i <= endRow; i++){
                            if (isTooClose(i, startColumn, gridPlayer1Or2)){
                                System.out.println("Error! You placed it too close to another one. Try again:");
                                return false;
                                //isValid = false;
                            }
                        }
                    isValid = true;
                    for (int i = startRow; i <= endRow; i++) {
                        gridPlayer1Or2[i][startColumn] = "O";
                            //isValid = false;
                        }
                    } else {
                    System.out.println("Error! Wrong length of the Submarine! Try again:");
                }
        } else {
                System.out.println("Error! Wrong ship location! try again:");
            }
            return false;
        }
        public static void displayField (String[][] player) {
            System.out.print("  ");
            for (int i = 1; i <= 10; i++) {
                System.out.print(i + " ");
            }
            System.out.println();
            for (int j = 0; j < 10; j++) {
                System.out.print((char) (j + 65));
                for (int k = 0; k < 10; k++) {
                    System.out.print(" " + player[j][k]);
                }
                System.out.println();
            }
        }
        public static void createGrid (String[][]grid){
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    grid[i][j] = "~";
                }
            }
        }
       public static void pressEnterKeyToContinue() {
        System.out.println("Press Enter and pass the move to another player");
        Scanner s = new Scanner(System.in);
        s.nextLine();
        }
    public static boolean isTooClose(int x, int y, String[][] warGround) {
        int num = 0;
        //check above
        int nx = x - 1;
        int ny = y;
        if (isValid(nx, ny)){
            if (!warGround[nx][ny].equals("~")){
                num++;
            }
        }
        //check right
        nx = x;
        ny = y + 1;
        if (isValid(nx, ny)){
            if (!warGround[nx][ny].equals("~")){
                num++;
            }
        }
        //check left
        nx = x;
        ny = y - 1;
        if (isValid(nx, ny)){
            if (!warGround[nx][ny].equals("~")){
                num++;
            }
        }
        //check bottom
        nx = x + 1;
        ny = y;
        if (isValid(nx, ny)){
            if (!warGround[nx][ny].equals("~")){
                num++;
            }
        }

        return num >= 1;
    }

    public static boolean isValid(int x, int y){
        return x >= 0 && y >= 0 && x < 10 && y < 10;
    }
    }
