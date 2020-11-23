import java.util.*;

/**
 * This class is the database, so it's the one that stores, removes, and edits objects (such as profiles and accounts)
 */

public final class Database {
    /* This is a list of the objects (users) stored in the database */
    private final List<User> objects;

    /* Constructor */
    public Database(List<User> objects) {
        Objects.requireNonNull(objects, "The given List of object is null");
        // this checks if the given list is null, says the message if it is

        this.objects = new ArrayList<>(objects);
    }

    public Database() {
        this.objects = new ArrayList<>();
        // this is another constructor that has an empty list
    }


    /* Methods */

    /* Lists the current objects (accounts and profiles) in the database */
    public List<User> getObjects() {
        return Collections.unmodifiableList(this.objects);
        // returns the list of objects
    }

    /* This looks for a specific user using the identifier (email) */
    public User findObject(String identifier) {
        String currentIdentifier;
        User foundObject = null;

        Objects.requireNonNull(identifier, "the specified identifier is null");
        // Checks if the string is null, if it is, it sends the message

        for (User object : this.objects) { // You go through every object in the objects list
            currentIdentifier = object.getIdentifier();

            if (Objects.equals(currentIdentifier, identifier)) {
                foundObject = object;

                break;
            }
        } //end for

        return foundObject; // It returns null if the the object is not found
    }

    /* This adds a user account/profile to our database */
    public boolean addObject(User object) {
        String identifier;
        User foundObject;

        Objects.requireNonNull(object, "the specified object is null");
        // Checks if the object passed to it is null, if it is, it sends this message

        identifier = object.getIdentifier(); //this gets the identifier (in our case, it's the email)

        foundObject = this.findObject(identifier); //this looks if the object (user) passed is already in the database

        if (foundObject == null) { //this executes if the object (user) is NOT in the database
            this.objects.add(object);

            return true;
        } //end if

        return false; //if foundObject is not null, (object was already in database) return false.
    }

    /* This removes a user account/profile from our database */
    public User removeObject(String identifier) {
        User currentObject;
        String currentIdentifier;
        int objectIndex = -1;
        User foundObject = null;

        Objects.requireNonNull(identifier, "the specified identifier is null");
        // Checks if the String identifier passed to it is null, if it is, it sends this message

        for (int i = 0; i < this.objects.size(); i++) {
            currentObject = this.objects.get(i);

            currentIdentifier = currentObject.getIdentifier();

            if (Objects.equals(currentIdentifier, identifier)) {
                //if the identifier is equal to currentIdentifier, it means that the object is in the list
                objectIndex = i;

                break;
            }
        } //end for

        if (objectIndex != -1) { // if we found the object from the for loop
            foundObject = this.objects.get(objectIndex);

            this.objects.remove(objectIndex); //removes the object
        }

        return foundObject;
        //returns null if the identifier (user) is not found in the list
        //if not null, returns the object removed
    }

    /* This edits the user's account information */
    public boolean editObject(String identifier, String whatToEdit) {
        String currentIdentifier;
        User foundObject = null;
        boolean found = false;

        Objects.requireNonNull(identifier, "The specified identifier is null");
        // Checks if the string is null, if it is, it sends the message

        /* Looks for the object */
        for (User object : this.objects) {
            currentIdentifier = object.getIdentifier();

            if (Objects.equals(currentIdentifier, identifier)) {
                foundObject = object;
                found = true;
                break;
            }
        } //end for

        /* If object isn't found, return */
        if (!found) {
            System.out.println("Could not find the given identifier");
            return false;
        }

        return false; //returns false if failed to edit
    }
    // still need updates on how edit account and edit profile will be implemented before I proceed

    /* Adds/Creates a profile to a user */
    public boolean addProfile(Profile profile) {
        Profile initialProfile = new Profile(profile.getEmail());

        for (int i = 0; i < objects.size(); i++) {
            if (profile.getEmail().equals(objects.get(i).getIdentifier())) {
                if (objects.get(i).getProfile().equals(initialProfile)) {
                    objects.get(i).setProfile(profile);
                    return true;
                }
                return false;
            }
        } // end of for

        return false;
    }

    /* Removes/Deletes a profile to a user */
    public boolean removeProfile(String identifier) {

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getProfile().getIdentifier() == null) {
                continue;
            }

            if (objects.get(i).getProfile().getIdentifier().equals(identifier)) {

                Profile initialProfile = new Profile(objects.get(i).getIdentifier());

                objects.get(i).setProfile(initialProfile);
                return true;

            }
        }

        return false;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof Database) {
            return Objects.equals(this.objects, ((Database) object).objects);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String format = "Database%s";

        return String.format(format, this.objects);
    }


}
