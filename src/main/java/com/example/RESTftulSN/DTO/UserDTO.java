package com.example.RESTftulSN.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

public class UserDTO {


    private interface id {
        @NotNull
        Long getId();
    }
    private interface username {
        @Size(min = 3,message = "Username must be from 3 letters")
        @Size(max = 30,message = "Username must be less than 30 letters")
        String getUsername();
    }
    private interface password {
        @Size(min = 6, message = "Password must be from 6 symbols")
        @Size(max = 30, message = "Password must be less than 30 symbols")
        String getPassword();
    }
    private interface repeatPassword {
        @Size(min = 6, message = "Password must be from 6 symbols")
        @Size(max = 30, message = "Password must be less than 30 symbols")
        String getRepeatPassword();
    }
    private interface email {
        @Email(message = "Wrong email")
        @NotEmpty(message = "You must enter email")
        String getEmail();
    }

    public enum Request{;

        @Value
        public static class Register implements username, password, repeatPassword, email {
            String username;
            String password;
            String repeatPassword;
            String email;
        }
        @Value
        public static class Update implements username,password,email{
            Long id;
            String username;
            String password;
            String email;
        }
        @Value
        public static class Create implements username,password,email{
            String username;
            String password;
            String email;
        }
        @Value
        @NoArgsConstructor(force = true)
        @AllArgsConstructor
        public static class Id implements id{
            Long id;
        }
    }
    public enum Response{;
        @Value
        public static class Create implements username,password,email{
            String username;
            String password;
            String email;
        }
    }


}
