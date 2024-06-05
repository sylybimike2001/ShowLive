package com.ayb.uitls;

import com.ayb.entity.DTO.UserDTO;


public class UserHolder {
    private static ThreadLocal<UserDTO> threadLocalValue = new ThreadLocal<>();

    public static UserDTO getUser() {
        return threadLocalValue.get();
    }

    public static void setUser(UserDTO user) {
        threadLocalValue.set(user);
    }

    public static void removeUser() {
        threadLocalValue.remove();
    }
}
