/**
 * Created by raniamakki on 3/27/18.
 */
public class UserDetails {

    private String userUID;
    private int nb_followers;
    private int nb_following;
    private int nb_posts;
    private String bio;

    public UserDetails() {
    }

    public UserDetails(String userUID, int nb_followers, int nb_following, int nb_posts, String bio) {
        this.userUID = userUID;
        this.nb_followers = nb_followers;
        this.nb_following = nb_following;
        this.nb_posts = nb_posts;
        this.bio = bio;
    }


    public void setNb_followers(int nb_followers) {
        this.nb_followers = nb_followers;
    }

    public void setNb_following(int nb_following) {
        this.nb_following = nb_following;
    }

    public void setNb_posts(int nb_posts) {
        this.nb_posts = nb_posts;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUserUID() {
        return userUID;
    }

    public int getNb_followers() {
        return nb_followers;
    }

    public int getNb_following() {
        return nb_following;
    }

    public int getNb_posts() {
        return nb_posts;
    }

    public String getBio() {
        return bio;
    }
}
