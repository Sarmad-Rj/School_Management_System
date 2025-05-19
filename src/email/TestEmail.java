package email;

public class TestEmail {
    public static void main(String[] args) {
        try {
            String token = java.util.UUID.randomUUID().toString();
            EmailService.sendResetEmail("sarmadrj01@gmail.com", token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
