import java.util.Random;
import java.util.Scanner;

import se.adlez.game.AbstractMoveableItem;
import se.adlez.game.Castle;
import se.adlez.game.FirTree;
import se.adlez.game.Forest;
import se.adlez.game.Item;
import se.adlez.game.Monkey;
import se.adlez.game.Position;
import se.adlez.game.Robot;
import se.adlez.game.Rock;
import se.adlez.game.Wolf;
import se.adlez.game.ForestToFile;
import se.adlez.game.Fox;

public class Menu {
    private Scanner scanner;
    private Forest forest;
    private ForestToFile toFile;

    public Menu() {
        scanner = new Scanner(System.in);
        toFile = new ForestToFile();
    } 

    public void run() {
        String choice = "";
        printMenu();

        do {
            System.out.print(">>> ");
            choice = scanner.nextLine();
            if(!isInitialized() && !(choice.equals("1") || choice.equals("q") || choice.equals("Q") || choice.equals("m") || choice.equals("9"))) {
                System.out.println("Please first initialize the forest!");
                continue;
            }
            switch (choice) {
                case "1":
                    // initialize the Forest
                    forest = new Forest();
                    break;
                case "2":
                    // Print the game board
                    System.out.println(forest.getGamePlan());
                    break;
                case "3":
                    // Add an item
                    addItem();
                    break;
                case "4":
                    // Print all the items
                    System.out.println(forest.listItems());
                    break;
                case "5":
                    // Add 5 Rocks and Trees
                    add5Each();
                    break;
                case "6":
                    // Add player, hunter and the home
                    addMovables();
                    break;
                case "7":
                    // Play the game
                    playGame();
                    break;
                case "8":
                    // Save the forest state
                    toFile.save(forest, "forest.ser");
                    break;
                case "9":
                    // Load a forest state from a file
                    forest = toFile.load("forest.ser");
                    break;
                case "p":
                    // Print the forest as JSON
                    System.out.println(toFile.toJson(forest));
                    break;
                case "s":
                    // Save the forest as JSON
                    toFile.saveAsJson(forest, "forest.JSON");
                    break;
                case "m":
                    printMenu();
                    break;
                case "q":
                case "Q":
                    break;
                default:
                    System.out.println("Not a valid option. Try again.");
                    break;
            }
        } while (!(choice.equals("q") || choice.equals("Q")));
    }

    private void printMenu() {
        String s = """
 -----------------
| 1) Create an empty forest                     | 7) Play game
| 2) Print the forest                           | 8) Save game to file
| 3) Add items (tree, rock) to the forest       | 9) Load game from file
| 4) List all items in the forest               | p) print the forest as JSON
| 5) Add 5 trees and 5 stones to the forest     | s) save the forest as JSON
| 6) Add player, hunter and the home            | m) Print menu
| qQ) Quit
 -----------------""";;
        System.out.println(s);
    }

    private void addItem() {
        String choice, xy;
        int x, y;

        while(true) {
            System.out.print("Add FirTree 🌲 (1) or Rock 🪨  (2) >>> ");
            choice = scanner.nextLine();
            if(!(choice.equals("1") || choice.equals("2"))) {
                System.out.println("You must enter a 1 or a 2");
                continue;
            }
            break;
        }

        while(true) {
            System.out.print("Set position x y (seperate by space): >>> ");
            xy = scanner.nextLine();
            try {
                x = Integer.parseInt(xy.split(" ")[0]);
                y = Integer.parseInt(xy.split(" ")[1]);
                if(!(x >= 0 && x < forest.getWIDTH()) || !(y >= 0 && y < forest.getHEIGHT())) {
                    System.out.println(String.format("X ( 0 - %d )   Y ( 0 - %d )", forest.getWIDTH() - 1, forest.getHEIGHT() - 1));
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("The coordinates have to be two integers by a space");
            }

        }
        

        forest.addItem((choice.equals("1")) ? new FirTree() : new Rock(), new Position(x, y));
        System.out.println(String.format("Added item to the forest: %s (%d, %d)",(choice.equals("1") ? "Firtree 🌲" : "Rock 🪨 "), x, y));
    }

    private void add5Each() {
        Random rand = new Random();
        int x, y;
        Item item;
        for(int i = 0; i < 10; i++) { // Add 5 Rocks and 5 Firtrees at a random empty position
            do {
                item = i < 5 ? new Rock() : new FirTree();
                x = rand.nextInt(10);
                y = rand.nextInt(10);
            } while(!forest.tryAddItem(item, new Position(x, y)));
            System.out.println(String.format("%s (%d %d)", item.toString(), x, y));
        }
    }

    private void addMovables() {
        String skin = "";
        AbstractMoveableItem player;
        System.out.println("""
            Choose a skin for the player:
            1: Robot 🤖
            2: Monkey 🐒
            3: Fox 🦊
            Your choice: """);
         skin = scanner.nextLine();
        
        switch (skin) {
            case "1":
                player = new Robot(new Position(0, 0));
                break;
            case "2":
                player = new Monkey(new Position(0, 0));
                break;
            case "3":
                player = new Fox(new Position(0, 0));
                break;
            default:
                System.out.println("Not a valid choice. Using default skin (Robot).");
                player = new Robot(new Position(0, 0));
                break;
        }

        AbstractMoveableItem hunter = new Wolf(new Position(7, 4));
        AbstractMoveableItem home = new Castle(new Position(4, 7));

        forest.addPlayerItem(player);
        forest.addHunterItem(hunter);
        forest.addHomeItem(home);
    }

    private void playGame() {
        if(forest.isGameOver()) {System.out.println("Please first intialze a new forest as a new round has not been setup yet"); return;}
        if(forest.getHunter() == null) {System.out.println("Please first add a player, hunter and home to the forest"); return;}
        System.out.println(forest.getGamePlan());

        String choice = "";
        do {
            System.out.print("Move the player (w, a, s, d)     q : quit\nMove: ");
            choice = scanner.nextLine();

            switch (choice) {
                case "w": forest.movePlayer(new Position(0, 1)); break;
                case "a": forest.movePlayer(new Position(-1, 0)); break;
                case "s": forest.movePlayer(new Position(0,  -1)); break;
                case "d": forest.movePlayer(new Position(1, 0)); break;
                case "q": System.out.println("Thanks for playing!"); break;
                default: System.out.println("Not a valid option, Try again"); break;
            }
            System.out.println(forest.getGamePlan());
            if(forest.isGameOver()) {
                System.out.println("The game is over! Type 'm' for menu options"); 
                return;
            }
        } while(!choice.equals("q") && !forest.isGameOver());
    }

    private boolean isInitialized() {return forest != null;}
}
