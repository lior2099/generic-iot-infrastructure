package simple_rps;

public interface Message<T> {
    public T getMessage();
    public void sendResponse(String response);
}

