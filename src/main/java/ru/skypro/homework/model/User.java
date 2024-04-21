package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skypro.homework.constant.Role;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String imageUrl;

    @OneToMany(mappedBy = "author",fetch = FetchType.LAZY)
    private Collection<Ad> ads;

    @OneToMany(mappedBy = "author",fetch = FetchType.LAZY)
    private Collection<Ad> comments;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image id")
    private Image image;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(email, user.email)
                && Objects.equals(password, user.password)
                && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName)
                && Objects.equals(phoneNumber, user.phoneNumber)
                && role == user.role
                && Objects.equals(imageUrl, user.imageUrl)
                && Objects.equals(ads, user.ads)
                && Objects.equals(comments, user.comments)
                && Objects.equals(image, user.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstName, lastName, phoneNumber, role, imageUrl, ads, comments, image);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", imageUrl='" + imageUrl + '\'' +
                ", ads=" + ads +
                ", comments=" + comments +
                ", image=" + image +
                '}';
    }
}
