package com.users_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UsersService {

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Sets the Supabase auth UUID into the current transaction so RLS policies
     * fire.
     * Must be called inside a @Transactional method.
     */
    private void setRlsContext() {
        String supabaseAuthUuid = UserContextInterceptor.getUserUuid();
        String userRole = UserContextInterceptor.getUserRole(); // you'll need this
        if (supabaseAuthUuid != null) {
            String jwtClaims = String.format(
                    "{\"sub\": \"%s\", \"user_role\": \"%s\"}",
                    supabaseAuthUuid,
                    userRole != null ? userRole : "user");
            entityManager.createNativeQuery(
                    "SELECT set_config('request.jwt.claims', :claims, true)")
                    .setParameter("claims", jwtClaims)
                    .getSingleResult();
        }
    }

    public void createUserApi(NewUserApi newUserApi) {
        setRlsContext();
        User user = User.newUserApiToUser(newUserApi);
        userRepository.save(user);
    }

    public void createUser(NewUser newUser) {
        setRlsContext();
        User user = User.newUserToUser(newUser);
        userRepository.save(user);
    }

    public User getUser(Long userId) {
        setRlsContext();
        return userRepository.findById(userId).get();
    }

    public void updateUser(UserUpdate userUpdate) {
        setRlsContext();
        User user = User.userUpdateToUser(userUpdate);
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        setRlsContext();
        userRepository.deleteById(userId);
    }
}
