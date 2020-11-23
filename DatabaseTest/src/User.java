import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * Creates a user object (for the user's account)
 */

public class User implements Identifiable {
    /* This SerializationProxy class is so that we can store the data input */
    private static final class SerializationProxy implements Serializable {

        static final long serialVersionUID;
        final String name;
        final String email; //the identifier
        final String gender;
        final String birthday;
        final Profile profile;

        static {
            serialVersionUID = 0xCAFEBABEL;
        } //static constructor

        SerializationProxy(User user) {
            Objects.requireNonNull(user, "The specified user is null");

            this.name = user.name;
            this.email = user.email;
            this.gender = user.gender;
            this.birthday = user.birthday;
            this.profile = user.profile;
        }

        Object readResolve() {
            return new User(this.name, this.email, this.gender, this.birthday);
        }
        // this reads a new user
    }

    //user fields
    private static final long serialVersionUID;
    private final String email;
    private String name;
    private String gender;
    private String birthday;
    private Profile profile;

    //constructors
    static {
        serialVersionUID = 0xCAFEBABEL;
    } //static constructor

    public User(String name, String email, String gender, String birthday) {
        Objects.requireNonNull(name, "The specified name is null");
        Objects.requireNonNull(email, "The specified email is null");
        Objects.requireNonNull(gender, "The specified gender is null");
        Objects.requireNonNull(birthday, "The specified birthday is null");

        Profile initialProfile = new Profile(email);

        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.profile = initialProfile;
    } // user constructor without a profile

    //methods

    @Override
    public String getIdentifier() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getGender() {
        return this.gender;
    }

    public String getBirthday() {
        return this.birthday;
    }

    //profile stuff

    public Profile getProfile() {
        return profile;
    }

    //setters
    /* There's no setEmail because those are final, you can't change them */
    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    //other methods

    private void readObject(ObjectInputStream inputStream) throws InvalidObjectException {
        throw new InvalidObjectException("a User object must be deserialized using a proxy");
    } // Not necessary, just here to make sure that they deserialize using a proxy

    private Object writeReplace() {
        return new SerializationProxy(this);
    } // This takes our user object, converts it to a serialization proxy object

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof User) {
            boolean equal;

            equal = Objects.equals(this.name, ((User) object).name);

            equal &= Objects.equals(this.email, ((User) object).email);

            equal &= Objects.equals(this.gender, ((User) object).gender);

            equal &= Objects.equals(this.birthday, ((User) object).birthday);

            return equal;
        } else {
            return false;
        } //end if
    } //equals

    @Override
    public int hashCode() {
        return Objects.hash(name, email, gender, birthday);
    }

    @Override
    public String toString() {
        String format = "User[email=%s, name=%s, gender=%s, birthday=%s, profile=%s]";

        return String.format(format, this.email, this.name, this.gender, this.birthday, this.profile);
    } //toString
}
