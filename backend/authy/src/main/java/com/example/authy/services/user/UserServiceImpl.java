package com.example.authy.services.user;

import com.example.authy.dto.UserDTO;
import com.example.authy.model.entity.User;
import com.example.authy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public UserDTO getUserDetails(String username){
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        return new UserDTO(user.getId(), user.getProfilePicture(),
                user.getFirstName(),user.getMiddleName(), user.getLastName(),
                user.getEmail(), user.getUsername(), user.getPassword(),
                user.getMobilePhone(), user.getHomePhone(),
                user.getDateOfBirth(), user.getNationalID(),
                user.getGender(), user.getMaritalStatus(), user.getRole(),
                user.getApartment(), user.getFloor(), user.getStreet(),
                user.getArea(), user.getCity(), user.getCountry(), user.getPostalCode(),
                user.getLinkedinUrl(), user.getGithubUrl(),user.getPortfolioUrl(),
                user.getFacebookUrl(), user.getInstagramUrl(), user.getXUrl(),
                user.getBio(), user.getInterests(),
                user.isMfaEnabled(), user.getMfaSecret(),
                user.is_email_verified(), user.getAuth_provider());

    }
}
