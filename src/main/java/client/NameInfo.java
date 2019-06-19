package client;

public class NameInfo implements Info{
    private String name;

    public NameInfo(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
