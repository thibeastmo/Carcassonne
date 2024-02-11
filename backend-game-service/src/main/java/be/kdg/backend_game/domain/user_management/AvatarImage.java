package be.kdg.backend_game.domain.user_management;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.net.URL;
import java.util.UUID;

@Entity
@Getter
@Setter
public class AvatarImage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    //TODO: check if this attribute is necessary
    private UUID avatarImageId;
    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;
    private String url;

    public AvatarImage() {
    }

    public AvatarImage(String url) {
        this.url = url;
    }

    public AvatarImage(Account account, String url) {
        this.account = account;
        this.url = url;
    }
}
