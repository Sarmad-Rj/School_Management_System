// email/EmailService.java
package email;

import com.sendgrid.*;
import java.io.IOException;

public class EmailService {
    private static final String FROM_EMAIL = "sarmadrajpoot291@gmail.com";

    public static void sendResetEmail(String toEmail, String resetCode) throws IOException {
        Email from = new Email(FROM_EMAIL);
        String subject = "Your Password Reset Code";
        Email to = new Email(toEmail);

        String message = "Hello,\n\n"
                + "You requested to reset your password.\n"
                + "Your 6-digit verification code is:\n\n"
                + "     " + resetCode + "\n\n"
                + "Please enter this code in the School Management app to complete your password reset.\n"
                + "This code will expire in 1 hour.\n\n"
                + "If you didn’t request this, you can safely ignore this email.\n\n"
                + "Best regards,\nSchool Management Team";

        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Email sent: " + response.getStatusCode());
        } catch (IOException ex) {
            throw ex;
        }
    }
}
