/**
 * Created by raniamakki on 3/27/18.
 */
public class UserDetails {

    private String userUID;
    private int nbFollowers;
    private int nbFollowing;
    private int nbPosts;
    private String bio;

    public UserDetails() {
    }

    public UserDetails(String userUID, int nbFollowers, int nbFollowing, int nbPosts, String bio) {
        this.userUID = userUID;
        this.nbFollowers = nbFollowers;
        this.nbFollowing = nbFollowing;
        this.nbPosts = nbPosts;
        this.bio = bio;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public int getNbFollowers() {
        return nbFollowers;
    }

    public void setNbFollowers(int nbFollowers) {
        this.nbFollowers = nbFollowers;
    }

    public int getNbFollowing() {
        return nbFollowing;
    }

    public void setNbFollowing(int nbFollowing) {
        this.nbFollowing = nbFollowing;
    }

    public int getNbPosts() {
        return nbPosts;
    }

    public void setNbPosts(int nbPosts) {
        this.nbPosts = nbPosts;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
