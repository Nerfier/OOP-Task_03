import java.util.Scanner;

import se.adlez.game.FirTree;
import se.adlez.game.Forest;
import se.adlez.game.Position;
import se.adlez.game.Rock;

public class Menu {
    private Scanner scanner;
    private Forest forest;

    public Menu() {
        scanner = new Scanner(System.in);
    } 

    public void run() {
        String choice = "";
        printMenu();

        do {
            System.out.print(">>> ");
            choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    forest = new Forest();
                    break;
                case "2":
                    System.out.println(forest.getGamePlan());
                    break;
                case "3":
                    addItem();
                    break;
                case "4":
                    System.out.println(forest.listItems());
                    break;
                case "5":
                    break;
                case "6":
                    break;
                case "7":
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
| 1) Create an empty forest
| 2) Print the forest
| 3) Add items (tree, rock) to the forest
| 4) List all items in the forest
| 5) Add 5 trees and 5 stones to the forest
| 6) Add player, hunter and the home
| 7) Play game
| m) Print menu
| qQ) Quit
 -----------------""";;
        System.out.println(s);
    }

    private void addItem() {
        String choice, xy;
        int x, y;

        while(true) {
            try {
                System.out.print("Add FirTree 🌲 (1) or Rock 🪨  (2) >>> ");
                choice = scanner.nextLine();
                if(!(choice.equals("1") || choice.equals("2"))) {
                    throw new NumberFormatException("You must enter a 1 or a 2");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        }

// TODO Error Handling for out of bounds coordinates

        while(true) {
            System.out.print("Set position x y (seperate by space): >>> ");
            xy = scanner.nextLine();
            try {
                x = Integer.parseInt(xy.split(" ")[0]);
                y = Integer.parseInt(xy.split(" ")[1]);

                break;
            } catch (NumberFormatException e) {
                System.out.println("The coordinates have to be two integers seperated by a space");
            }

        }
        

        forest.addItem((choice.equals("1")) ? new FirTree() : new Rock(), new Position(x, y));
        System.out.println(String.format("Added item to the forest: %s (%d, %d)",(choice.equals("1") ? "Firtree 🌲" : "Rock 🪨 "), x, y));
    }
}
