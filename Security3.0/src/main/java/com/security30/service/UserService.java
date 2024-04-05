package com.security30.service;

import com.security30.model.UserProperty;
import com.security30.payload.LoginDto;
import com.security30.payload.UserPropertyDto;
import com.security30.repository.UserPropertyRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private UserPropertyRepository userRepository;
    private JwtService jwtService;

    public UserService(UserPropertyRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }
    public UserPropertyDto addUserProperty(UserPropertyDto userProperty){
        UserProperty user = new UserProperty();
        user.setUsername(userProperty.getUserName());
        user.setEmail(userProperty.getEmail());
        //Password is saved as Hashing technique first hash the pw then gensalt
        // method to how many rounds are needed for decrypt
        user.setPassword(BCrypt.hashpw(userProperty.getPassword(),BCrypt.gensalt(4)));

        user.setFirstName(userProperty.getFirstName());
        user.setLastName(userProperty.getLastName());
        user.setUserRole(userProperty.getUserRole());

        UserProperty saved = userRepository.save(user);


        UserPropertyDto dto = new UserPropertyDto();
        dto.setUserName(saved.getUsername());
        dto.setEmail(saved.getEmail());
        dto.setUserName(saved.getUsername());
        dto.setFirstName(saved.getFirstName());
        dto.setLastName(saved.getLastName());
        dto.setUserRole(saved.getUserRole());

        return dto;
    }

    public String verifyLogin(LoginDto loginDto) {
        Optional<UserProperty> byUserName = userRepository.findByUsername(loginDto.getUsername());
        if (byUserName.isPresent()){
            UserProperty user = byUserName.get();
            if(BCrypt.checkpw(loginDto.getPassword(), user.getPassword())){
                return jwtService.generateToken(user.getUsername());
            }
        }
        return null;

    }

    public String getOauth2Access(OAuth2User principal) {
        String username = principal.getName();
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String[] arr = new String[0];
        if(name!=null) {
            arr = name.split(" ");
        }
        String fName = arr[0];
        String lName = arr[1];


        Optional<UserProperty> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()){
          return jwtService.generateToken(byEmail.get().getUsername());
        }
        else{
            UserProperty user = new UserProperty();
            String password = java.util.UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);


            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(BCrypt.hashpw(password,BCrypt.gensalt(4)));
            user.setFirstName(fName);
            user.setLastName(lName);
            user.setUserRole("ROLE_USER");
            UserProperty saved = userRepository.save(user);

            return jwtService.generateToken(saved.getUsername());
        }

    }
}
