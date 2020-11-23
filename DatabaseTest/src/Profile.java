import java.util.ArrayList;
import java.util.Objects;

/**
 * Creates a profile object (for the user's profile)
 */

public class Profile implements Identifiable{

    //Profile fields
    private final String email;
    private final String profileId;
    private String contactInfo;
    private ArrayList<String> interests;
    private ArrayList<String> friendList;
    private String aboutMe;

    //constructors

    public Profile(String email, String contactInfo,
                   ArrayList<String> interests, ArrayList<String> friendList, String aboutMe) {
        Objects.requireNonNull(email, "The specified email is null");
        Objects.requireNonNull(contactInfo, "The specified contactInfo is null");
        Objects.requireNonNull(interests, "The specified interests is null");
        Objects.requireNonNull(friendList, "The specified friendList is null");
        Objects.requireNonNull(aboutMe, "The specified aboutMe is null");

        this.email = email;
        this.profileId = createProfileId(email);
        this.contactInfo = contactInfo;
        this.interests = new ArrayList<>(interests);
        this.friendList = new ArrayList<>(friendList);
        this.aboutMe = aboutMe;
    } //Profile constructor

    public Profile(String email) {
        this.email = email;
        this.profileId = null;
        this.contactInfo = null;
        this.interests = null;
        this.friendList = null;
        this.aboutMe = null;
    } //Null profile constructor

    //getters

    public String getEmail() {
        return this.email;
    }

    @Override
    public String getIdentifier() {
        return this.profileId;
    }

    public String getContactInfo() {
        return this.contactInfo;
    }

    public ArrayList<String> getInterests() {
        return this.interests;
    }

    public ArrayList<String> getFriendList() {
        return this.friendList;
    }

    public String getAboutMe() {
        return this.aboutMe;
    }

    //setters (no setEmail and setProfileId because those are final)
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public void setFriendList(ArrayList<String> friendList) {
        this.friendList = friendList;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    //additional methods

    public String createProfileId(String email) {
        int max = 5000;
        int min = 10;
        StringBuilder sb = new StringBuilder();
        sb.append(email.charAt(0));

        int number = (int) (Math.random() * (max - min + 1) + min);
        String numberString = String.valueOf(number);

        sb.append(numberString.charAt(0));
        sb.append(email.charAt(1));
        sb.append(numberString.substring(1));

        return sb.toString();
    } //generate unique profileId per profile

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof Profile) {
            boolean equal;

            equal = Objects.equals(this.contactInfo, ((Profile) object).contactInfo);

            equal &= Objects.equals(this.interests, ((Profile) object).interests);

            equal &= Objects.equals(this.friendList, ((Profile) object).friendList);

            equal &= Objects.equals(this.aboutMe, ((Profile) object).aboutMe);

            return equal;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Profile{" +
                "contactInfo='" + contactInfo + '\'' +
                ", interests=" + interests +
                ", friendList=" + friendList +
                ", aboutMe='" + aboutMe + '\'' +
                ", profileId='" + profileId + '\'' +
                '}';
    }
}
