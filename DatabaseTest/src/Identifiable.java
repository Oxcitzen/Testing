import java.io.Serializable;

/**
 * Used for the database
 */

public interface Identifiable extends Serializable {
    String getIdentifier();
}
