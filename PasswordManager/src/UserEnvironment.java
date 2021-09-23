import com.avaya.smgr.tm.util.PasswordController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

//requires successful login.
public class UserEnvironment {
    public static void run(String username) throws InterruptedException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        System.out.println("\n" +
                "\n" +
                "█████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████\n" +
                "█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░█░░░░░░██████████░░░░░░█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░░░███░░░░░░░░░░░░███\n" +
                "█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░██████████░░▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀▄▀░░███░░▄▀▄▀▄▀▄▀░░░░█\n" +
                "█░░▄▀░░░░░░▄▀░░█░░▄▀░░░░░░▄▀░░█░░▄▀░░░░░░░░░░█░░▄▀░░░░░░░░░░█░░▄▀░░██████████░░▄▀░░█░░▄▀░░░░░░▄▀░░█░░▄▀░░░░░░░░▄▀░░███░░▄▀░░░░▄▀▄▀░░█\n" +
                "█░░▄▀░░██░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░█████████░░▄▀░░█████████░░▄▀░░██████████░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░████░░▄▀░░███░░▄▀░░██░░▄▀░░█\n" +
                "█░░▄▀░░░░░░▄▀░░█░░▄▀░░░░░░▄▀░░█░░▄▀░░░░░░░░░░█░░▄▀░░░░░░░░░░█░░▄▀░░██░░░░░░██░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░░░░░░░▄▀░░███░░▄▀░░██░░▄▀░░█\n" +
                "█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░██░░▄▀░░██░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀▄▀▄▀▄▀▄▀▄▀░░███░░▄▀░░██░░▄▀░░█\n" +
                "█░░▄▀░░░░░░░░░░█░░▄▀░░░░░░▄▀░░█░░░░░░░░░░▄▀░░█░░░░░░░░░░▄▀░░█░░▄▀░░██░░▄▀░░██░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░░░░░▄▀░░░░███░░▄▀░░██░░▄▀░░█\n" +
                "█░░▄▀░░█████████░░▄▀░░██░░▄▀░░█████████░░▄▀░░█████████░░▄▀░░█░░▄▀░░░░░░▄▀░░░░░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░██░░▄▀░░█████░░▄▀░░██░░▄▀░░█\n" +
                "█░░▄▀░░█████████░░▄▀░░██░░▄▀░░█░░░░░░░░░░▄▀░░█░░░░░░░░░░▄▀░░█░░▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░░░░░▄▀░░█░░▄▀░░██░░▄▀░░░░░░█░░▄▀░░░░▄▀▄▀░░█\n" +
                "█░░▄▀░░█████████░░▄▀░░██░░▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░░░░░▄▀░░░░░░▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░██░░▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀░░░░█\n" +
                "█░░░░░░█████████░░░░░░██░░░░░░█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░█░░░░░░██░░░░░░██░░░░░░█░░░░░░░░░░░░░░█░░░░░░██░░░░░░░░░░█░░░░░░░░░░░░███\n" +
                "█████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████\n" +
                "██████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████\n" +
                "█░░░░░░██████████░░░░░░█░░░░░░░░░░░░░░█░░░░░░██████████░░░░░░█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░░░███\n" +
                "█░░▄▀░░░░░░░░░░░░░░▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░░░░░░░░░██░░▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀▄▀░░███\n" +
                "█░░▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░░░░░▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░██░░▄▀░░█░░▄▀░░░░░░▄▀░░█░░▄▀░░░░░░░░░░█░░▄▀░░░░░░░░░░█░░▄▀░░░░░░░░▄▀░░███\n" +
                "█░░▄▀░░░░░░▄▀░░░░░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░░░░░▄▀░░██░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░█████████░░▄▀░░█████████░░▄▀░░████░░▄▀░░███\n" +
                "█░░▄▀░░██░░▄▀░░██░░▄▀░░█░░▄▀░░░░░░▄▀░░█░░▄▀░░██░░▄▀░░██░░▄▀░░█░░▄▀░░░░░░▄▀░░█░░▄▀░░█████████░░▄▀░░░░░░░░░░█░░▄▀░░░░░░░░▄▀░░███\n" +
                "█░░▄▀░░██░░▄▀░░██░░▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░██░░▄▀░░██░░▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░██░░░░░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀▄▀░░███\n" +
                "█░░▄▀░░██░░░░░░██░░▄▀░░█░░▄▀░░░░░░▄▀░░█░░▄▀░░██░░▄▀░░██░░▄▀░░█░░▄▀░░░░░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░░░░░░░░░█░░▄▀░░░░░░▄▀░░░░███\n" +
                "█░░▄▀░░██████████░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░██░░▄▀░░░░░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░█████████░░▄▀░░██░░▄▀░░█████\n" +
                "█░░▄▀░░██████████░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░██░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░░░░░▄▀░░█░░▄▀░░░░░░░░░░█░░▄▀░░██░░▄▀░░░░░░█\n" +
                "█░░▄▀░░██████████░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀░░██░░░░░░░░░░▄▀░░█░░▄▀░░██░░▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░██░░▄▀▄▀▄▀░░█\n" +
                "█░░░░░░██████████░░░░░░█░░░░░░██░░░░░░█░░░░░░██████████░░░░░░█░░░░░░██░░░░░░█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░█░░░░░░██░░░░░░░░░░█\n" +
                "██████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████");
        System.out.println(" Login successful!\n" + " Logged in as user: " + username);
        System.out.println(" Enter help to display commands.");
        main(username);
    }

    private static void main(String username) throws NoSuchAlgorithmException, InterruptedException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException {
        while (true) {
            System.out.print(" " + username + "@PasswordManager>");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.trim().split(" ", 2)[0].equals("gen")) {
                if (input.trim().split(" ").length != 2)
                    System.out.println("Error: Missing parameters or too many parameters were given. Check help for guidance.");
                else {
                    try {
                        int length = Integer.parseInt(input.trim().split(" ")[1]);
                        if (length > 0) System.out.println(new PasswordController().generatePassword(length));
                        else System.out.println("Error: Invalid length.");

                    } catch (NumberFormatException e) {
                        System.out.println("Error: Ensure you are using gen command correctly. Check help for guidance.");
                    }
                }
            } else if (input.equals("help")) help();
            else if (input.equals("logout")) logout();
            else if (input.equals("exit")) System.exit(0);
            else if (input.trim().split(" ", 2)[0].equals("goto")) {
                if (input.trim().split(" ", 0).length != 2)
                    System.out.println("Error: Missing parameters or too many parameters were given. Check help for guidance.");
                else openWebsite(input.trim().split(" ")[1]);


            }
            else if (input.equals("add")) add(new DatabaseManager().getUserID(username));
            else if (input.equals("showall")) showAll(username);
            else if (input.trim().split(" ", 2)[0].equals("showith")) {
                if (input.trim().split(" ").length != 3) System.out.println("Error: Missing parameters or too many parameters were given. Check help for guidance.");
                if (input.trim().split(" ")[1].equals("-u")) showWith(new DatabaseManager().getUserID(username),
                                                                    input.trim().split(" ")[2],
                                                                    "username");
                else if (input.trim().split(" ")[1].equals("-w")) showWith(new DatabaseManager().getUserID(username),
                        input.trim().split(" ")[2],
                        "website");
                else System.out.println("Error: Missing parameters or too many parameters were given. Check help for guidance.");
            }
            else if (input.trim().split(" ", 2)[0].equals("del")) {
                try {
                    int id = Integer.parseInt(input.trim().split(" ", 2)[1]);
                    new DatabaseManager().deleteEntry(id, username);
                    System.out.println("Deletion complete.");
                }
                catch (NumberFormatException e){
                    System.out.println("Error: Ensure your parameters are correct.");
                }

            }
        }
    }

    private static void help(){
        System.out.println(" gen        | Usage: gen length              | Example: gen 9                   | Generates a random password of desired length.\n" +
                           "-------------------------------------------------------------------------------------------------------------------------\n" +
                           " logout     | Usage: logout                  |                                  | Logs the user out.                            \n" +
                           "-------------------------------------------------------------------------------------------------------------------------\n" +
                           " exit       | Usage: exit                    |                                  | Exit the application.                         \n" +
                           "-------------------------------------------------------------------------------------------------------------------------\n" +
                           " goto       | Usage: goto website            | Example: goto www.facebook.com   | Opens the website in default browser          \n" +
                           "-------------------------------------------------------------------------------------------------------------------------\n" +
                           " add        | Usage: add                     |                                  | Use to add to the database. It will open a prompt\n" +
                           "            |                                |                                  | with different parameters that need to be added \n" +
                           "            |                                |                                  | such as website/username/password.\n" +
                           "-------------------------------------------------------------------------------------------------------------------------\n" +
                           " showall    | Usage: showall                 |                                  | Shows all the stored website/username/passwords.\n" +
                           "--------------------------------------------------------------------------------------------------------------------------\n" +
                           " showith    | Usage: showith -flag username  | Example: showith -u username123 | Find the credentials for a specific username or\n" +
                           "            |                                |                                  | website.\n" +
                           "            |                                |                                  | Flags:\n" +
                           "            |                                |                                  | -u for username\n" +
                           "            |                                |                                  | -w for website\n"        +
                           "--------------------------------------------------------------------------------------------------------------------------\n" +
                           " del        | Usage: del ID                  | Example: del 2                   | Deletes a row from database with ID.\n" +
                           "---------------------------------------------------------------------------------------------------------------------------\n");
    }

    private static void logout() throws InterruptedException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {
        AuthenticationSystem.run();
    }

    private static void openWebsite(String link)  {

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(link));
            }
        }
        catch (URISyntaxException | IOException a) {
            System.out.println("Unable to open browser.");
        }
    }

    private static void add(int userID) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(" Website: ");
            String website = scanner.nextLine();
            System.out.println(" Username:");
            String username = scanner.nextLine();
            System.out.println(" Password:");
            String password = scanner.nextLine();
            System.out.println(" Are you sure you want to add these?(yes/no)\nWebsite: " + website + "\nUsername: " + username + "\nPassword: " + password);
            String response = scanner.nextLine().toLowerCase();
            if (response.equals("yes")) {
                KeyManagement keyManager = new KeyManagement();
                new DatabaseManager().addToManager(userID, website, username, password, keyManager.retrieveKey(), keyManager.retrieveInitialisationVector());
                System.out.println(" Added.");
                break;
            }
        }
    }
    private static void showAll(String username) throws NoSuchAlgorithmException {
        KeyManagement keyManager = new KeyManagement();
        SecretKey secretKey = keyManager.retrieveKey();
        IvParameterSpec initialisationVector = keyManager.retrieveInitialisationVector();
        new DatabaseManager().readAll(username, secretKey, initialisationVector);
    }
    private static void showWith(int userID, String input, String parameter) throws NoSuchAlgorithmException {
        KeyManagement keyManager = new KeyManagement();
        SecretKey secretKey = keyManager.retrieveKey();
        IvParameterSpec initialisationVector = keyManager.retrieveInitialisationVector();
        new DatabaseManager().readWithInput(userID, input, secretKey, initialisationVector, parameter);
    }
}
