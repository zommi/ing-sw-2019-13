package client.info;

public class AsynchronousInfo implements Info{
    private Info info;

    public AsynchronousInfo(Info info){
        this.info = info;
    }

    public Info getInfo() {
        return info;
    }
}
