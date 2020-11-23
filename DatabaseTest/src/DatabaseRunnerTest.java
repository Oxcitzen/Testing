import java.io.*;
import java.util.*;

/**
 * This class is just for testing
 */

public class DatabaseRunnerTest {
    static Database database;
    static Scanner scanner;
    static String choice;
    private static User getPerson(Scanner scanner) {
        String name;
        String email;
        String gender;
        String birthday;
        User user;

        Objects.requireNonNull(scanner, "the specified Scanner is null");

        System.out.print("Enter the name of the user: ");

        if (!scanner.hasNextLine()) {
            return null;
        } //end if

        name = scanner.nextLine();

        System.out.print("Enter the email of the user: ");

        if (!scanner.hasNextLine()) {
            return null;
        } //end if

        email = scanner.nextLine();

        System.out.print("Enter the gender of the user: ");

        if (!scanner.hasNextLine()) {
            return null;
        } //end if

        gender = scanner.nextLine();

        System.out.print("Enter the birthday of the user: ");

        if (!scanner.hasNextLine()) {
            return null;
        } //end if

        birthday = scanner.nextLine();

        user = new User(name, email, gender, birthday);

        return user;
    } //getPerson

    private static Profile getProfile(Scanner scanner) {
        String profileId;
        String contactInfo;
        ArrayList<String> interests;
        ArrayList<String> friendList;
        String aboutMe;
        Profile profile;

        Objects.requireNonNull(scanner, "the specified Scanner is null");

        System.out.println("User Email: ");

        profileId = scanner.nextLine();

        System.out.println("Enter the contact info of the user: ");

        if (!scanner.hasNextLine()) {
            return null;
        } //end if

        contactInfo = scanner.nextLine();

        System.out.println("How many interests do you have?");

        int noInt = scanner.nextInt();

        scanner.nextLine();

        System.out.println("Enter the interests of the user (please separate by commas if more than 1): ");

        if (!scanner.hasNextLine()) {
            return null;
        } //end if

        String answer = scanner.nextLine();

        if (noInt > 1) {
            while (!answer.contains(",")) {
                System.out.println("Try again!");
                answer = scanner.nextLine();
            }
            String[] splitAnswer = answer.split(",");
            interests = new ArrayList<>(Arrays.asList(splitAnswer));
        } else {
            interests = new ArrayList<>();
            interests.add(answer);
        }

        System.out.println("Does this user have any friends? Y for yes, N for no");

        if (!scanner.hasNextLine()) {
            return null;
        } //end if

        String friendsAns = scanner.nextLine();
        String friends;

        if (friendsAns.equalsIgnoreCase("N")) {
            friendList = new ArrayList<>();
        } else {
            System.out.println("Please enter their profileId and separate by commas if there's more than one");
            friends = scanner.nextLine();
            while (!friends.contains(",")) {
                System.out.println("Try again!");
                friends = scanner.nextLine();
            }
            String[] splitFriends = friends.split(",");
            friendList = new ArrayList<>(Arrays.asList(splitFriends));
        }

        System.out.println("Enter the about me of the user");

        aboutMe = scanner.nextLine();

        profile = new Profile(profileId, contactInfo, interests, friendList, aboutMe);

        return profile;
    }

    public static void main(String[] args) {

        try (var inputStream = new ObjectInputStream(new FileInputStream("objects.ser"))) {
            List<User> objects;

            objects = (List<User>) inputStream.readObject();

            database = new Database(objects);
        } catch (IOException | ClassNotFoundException e) {
            database = new Database();
        } //end try catch

        scanner = new Scanner(System.in);

        System.out.print("--------------------\n" +
                "(A) Add User\n" +
                "(B) Remove Object\n" +
                "(C) Find Object\n" +
                "(D) Print Objects\n" +
                "(E) Exit\n" +
                "(F) Add Profile\n" +
                "(G) Remove Profile\n" +
                "--------------------\n" +
                "Enter your choice: ");

        if (!scanner.hasNextLine()) {
            return;
        } //end if

        choice = scanner.nextLine();

        choice = choice.toUpperCase();

        while (!choice.equals("E")) {
            System.out.println();

            switch (choice) {
                case "A" -> {
                    User user;
                    boolean added;

                    user = getPerson(scanner);

                    if (user == null) {
                        return;
                    } //end if

                    added = database.addObject(user);

                    System.out.println();

                    if (added) {
                        System.out.println("The user was successfully added to the database.");
                    } else {
                        System.out.println("The user could not be added to the database, as it already contains a " +
                                "user with the specified email.");
                    } //end if
                } //case "A"
                case "B" -> {
                    String identifier;
                    User object;

                    System.out.println();

                    System.out.print("Enter the identifier: ");

                    if (!scanner.hasNextLine()) {
                        return;
                    } //end if

                    identifier = scanner.nextLine();

                    object = database.removeObject(identifier);

                    System.out.println();

                    if (object == null) {
                        System.out.println("An object with the specified identifier could not be found in the " +
                                "database.");
                    } else {
                        System.out.printf("%s was removed from the database.%n", object);
                    } //end if
                } //case "C"
                case "C" -> {
                    String identifier;
                    User object;

                    System.out.println();

                    System.out.print("Enter the identifier: ");

                    if (!scanner.hasNextLine()) {
                        return;
                    } //end if

                    identifier = scanner.nextLine();

                    object = database.findObject(identifier);

                    System.out.println();

                    if (object == null) {
                        System.out.println("An object with the specified identifier could not be found in the " +
                                "database.");
                    } else {
                        System.out.printf("%s was found in the database.%n", object);
                    } //end if
                } //case "D"
                case "D" -> {
                    List<User> objects;

                    objects = database.getObjects();

                    System.out.println();

                    if (objects.isEmpty()) {
                        System.out.println("There are no objects in the database.");
                    } else {
                        for (User object : objects) {
                            System.out.println(object);
                        } //end for
                    } //end if
                } //case "D"
                case "F" -> {
                    Profile profile;
                    boolean added;

                    profile = getProfile(scanner);

                    if (profile == null) {
                        return;
                    } //end if

                    added = database.addProfile(profile);

                    System.out.println();

                    if (added) {
                        System.out.println("Profile added");
                        System.out.printf("This Profile ID is: %s\n", profile.getIdentifier());
                    } else {
                        System.out.println("Profile not added");
                    }
                }
                case "G" -> {
                    boolean removed;
                    System.out.println("Profile ID: ");
                    String identifier = scanner.nextLine();

                    if (identifier == null) {
                        return;
                    } //end if

                    removed = database.removeProfile(identifier);

                    System.out.println();

                    if (removed) {
                        System.out.println("Profile removed");
                    } else {
                        System.out.println("Profile not removed");
                    }
                }
                default -> {
                    System.out.println();

                    System.out.println("Error: Invalid choice!");
                } //default
            } //end switch

            System.out.println();

            System.out.print("--------------------\n" +
                    "(A) Add User\n" +
                    "(B) Remove Object\n" +
                    "(C) Find Object\n" +
                    "(D) Print Objects\n" +
                    "(E) Exit\n" +
                    "(F) Add Profile\n" +
                    "(G) Remove Profile\n" +
                    "--------------------\n" +
                    "Enter your choice: ");

            if (!scanner.hasNextLine()) {
                return;
            } //end if

            choice = scanner.nextLine();

            choice = choice.toUpperCase();
        } //end while

        try (var outputStream = new ObjectOutputStream(new FileOutputStream("objects.ser"))) {
            List<User> objects;

            objects = database.getObjects();

            outputStream.writeObject(objects);
        } catch (IOException e) {
            e.printStackTrace();
        } //end try catch
    }
}
