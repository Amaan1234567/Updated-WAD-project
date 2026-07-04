import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final AppUserRepository userRepository;

    public UserService(AppUserRepository userRepository) {
        this.repository = userRepository;
    }

    @Transactional
    public AppUser createOrUpdateUserFromClerk(String userId, String email, String firstName, String lastName) {
        // If they already exist, we might just want to update them
        AppUser user = userRepository.findByUserId(userId)
                .orElseGet(AppUser::new);
                
        user.setUserId(userId);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        
        return userRepository.save(user);
    }
}
