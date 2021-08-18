package com.example.epokemon.data;

import com.example.epokemon.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication

            //resultLogin(username, password);

            LoggedInUser fakeUser = new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Cassio Ribeiro");


            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    public void resultLogin(String username, String password){
        if(!username.equals("cassio@gmail.com") && !password.equals("123456")){
            throw new RuntimeException("Login ou senha incorretos");
        }
    }
}