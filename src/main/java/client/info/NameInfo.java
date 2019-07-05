package client.info;

/**
 * Info: send the name of the player to the server
 */
public class NameInfo implements Info{
    /**
     * Name of the player
     */
    private String name;

    public NameInfo(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
