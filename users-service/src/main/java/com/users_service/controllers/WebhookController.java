import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final UserService userService;

    public WebhookController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/clerk")
    public ResponseEntity<String> handleClerkWebhook(@RequestBody String payload, 
                                                     @RequestHeader("svix-id") String svixId,
                                                     @RequestHeader("svix-timestamp") String svixTimestamp,
                                                     @RequestHeader("svix-signature") String svixSignature) {
        
        // TODO: 1. Verify the Svix signature (Crucial for security!)
        // TODO: 2. Parse the JSON payload
        // TODO: 3. Pass data to userService.createOrUpdateUserFromClerk(...)
        
        System.out.println("Webhook received! Signatures attached.");
        
        return ResponseEntity.ok("Webhook processed");
    }
}