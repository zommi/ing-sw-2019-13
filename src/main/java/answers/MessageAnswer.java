package answers;

public class MessageAnswer implements ServerAnswer {
    private String message;

    public MessageAnswer(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
