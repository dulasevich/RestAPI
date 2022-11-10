import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Request request = new Request();
        Response response = new Response();
        try {
            System.out.println(response.getAuthResponse(request.postAuthRequest("read")).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(response.getAuthResponse(request.postAuthRequest("write")).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
