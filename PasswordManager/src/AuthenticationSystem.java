import com.avaya.smgr.tm.util.PasswordController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationSystem {
    public static void run() throws NoSuchAlgorithmException, InterruptedException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException {
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
        System.out.println(" Welcome to the Password Manager 1.0\n" +
                            " Before you are able to access the full features of the database you must pass the authentication system.\n" +
                            " Available commands:\n");
        help();
        main();

    }
    private static void help(){
        System.out.println(" login    | Usage: login username password    | Used to login into your account to access the password manager.\n" +
                           "----------------------------------------------------------------------------------------------------------------\n" +
                           " register | Usage: register username password | Used to register an account into the password manager.\n" +
                           "                                              | Ensure that the password length is at least of size 10 and includes 1 number.\n" +
                           "                                              | Example: register mike thisismylongpassword7\n" +
                           "----------------------------------------------------------------------------------------------------------------\n" +
                           " exit     | Usage: exit                       | Used to exit the application.\n" +
                           "----------------------------------------------------------------------------------------------------------------\n" +
                           " help     | Usage: help                       | Used to get description of commands.");
    }

    //registration process.
    private static void register(String username, String password) throws NoSuchAlgorithmException, InterruptedException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException {
        Scanner scanner = new Scanner(System.in);
        DatabaseManager dbmanager = new DatabaseManager();
        PasswordController passwordController = new PasswordController();
        KeyManagement keyManager = new KeyManagement();
        int userID = dbmanager.generateUserID();
        byte[] salt = passwordController.getSalt();
        String hashedPassword = passwordController.hashPassword(password, salt);
        System.out.println(" Please enter a USB pen drive. Ensure it is in the E:\\ drive. DO NOT LOSE THIS PEN: It will store the key that will allow you to access your stored passwords. \nOnce ready type anything and press enter.");
        scanner.next();
        System.out.println(" Creating storage...");
        keyManager.createStorageForKey();
        Thread.sleep(3000);
        System.out.println(" Generating key...");
        keyManager.generateKeyIntoStorage();
        Thread.sleep(2000);
        System.out.println(" Finalising registration.");
        Thread.sleep(3000);
        dbmanager.registerUser(username, hashedPassword, userID, salt);
        System.out.println(" Registration successful! Feel free to login!");
        main();
    }


    //main runnable while in the authentication system. If successful login is performed it will change to the actual user password manager environment.
    private static void main() throws NoSuchAlgorithmException, InterruptedException, InvalidAlgorithmParameterException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print(" PasswordManager> ");
            String command = scanner.nextLine();
            if (command.equals("exit")) System.exit(0);
            else if (command.equals("help")) help();
            else if (command.split(" ", 3)[0].equals("login")){
                String[] input = command.trim().split(" ", 3);
                if (input.length != 3) System.out.println("Error: Missing parameters\nUsage: login username password");
                else {
                    DatabaseManager databaseManager = new DatabaseManager();
                    String username = input[1];
                    String password = input[2];
                    if(databaseManager.verifyLogin(databaseManager.getUserID(username), password))
                    {
                        UserEnvironment.run(username);
                        scanner.close();
                        break;
                    }
                    else System.out.println("Error: Invalid credentials. Please check your username/password.");
                }
            }
            else if (command.split(" ", 3)[0].equals("register")) {
                String[] input = command.trim().split(" ");
                if (input.length != 3) System.out.println("Error: Missing parameters\nUsage: register username password");
                else {
                String username = input[1];
                String password = input[2];
                Pattern regex = Pattern.compile(".*\\d.*");
                Matcher matcher = regex.matcher(password);
                if (password.length() < 10 || !matcher.matches())
                    System.out.println("Error: Ensure your password has minimum length 10 and 1 numerical value.");
                else if (new DatabaseManager().checkUserInDB(username)) {
                    System.out.println("Username already exists in database. Please, try a different username.");
                }
                else {
                    register(username, password);
                    scanner.close();
                    break;
                }
                }
            }
            else System.out.println("Error: Invalid command. Please use help to list command.");
        }
    }
}
