import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "users", schema = "user_svc")
public class AppUser {

    @Id
    @Column(name = "user_id") // This is your Primary Key (the Clerk String)
    private String userId;

    @Column(name = "auth_id", unique = true, nullable = false)
    private UUID authId; // Matches your UUID in SQL

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    private String phone; // Changed to String to handle leading zeros/formats

    private String address;

    private String city;

    private String pincode;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM) // Necessary for Postgres custom ENUM types
    private AppRole userType;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // --- GETTERS AND SETTERS ---

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public UUID getAuthId() { return authId; }
    public void setAuthId(UUID authId) { this.authId = authId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public AppRole getUserType() { return userType; }
    public void setUserType(AppRole userType) { this.userType = userType; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}