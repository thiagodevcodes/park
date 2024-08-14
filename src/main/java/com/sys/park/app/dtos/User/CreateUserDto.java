package com.sys.park.app.dtos.User;

public record CreateUserDto(String name, String username, String password, Long role) {
}