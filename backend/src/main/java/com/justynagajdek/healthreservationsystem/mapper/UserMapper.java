package com.justynagajdek.healthreservationsystem.mapper;

import com.justynagajdek.healthreservationsystem.dto.PendingUserDto;
import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.dto.UserDto;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity mapSignUpDtoToUserEntity(SignUpDto signUpDto) {
        if (signUpDto == null) {
            return null;
        }

        UserEntity user = new UserEntity();
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setEmail(signUpDto.getEmail());
        user.setPhoneNumber(signUpDto.getPhone());
        user.setPasswordHash(signUpDto.getPassword());
        user.setRole(Role.valueOf(signUpDto.getRole().toUpperCase()));
        user.setStatus(AccountStatus.PENDING);

        return user;
    }
    public SignUpDto mapToSignUpDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        SignUpDto dto = new SignUpDto();
        dto.setFirstName(userEntity.getFirstName());
        dto.setLastName(userEntity.getLastName());
        dto.setEmail(userEntity.getEmail());
        dto.setPhone(userEntity.getPhoneNumber());
        dto.setRole(userEntity.getRole().name());

        return dto;
    }

    public UserDto toDto(UserEntity entity) {
        if (entity == null) return null;

        return UserDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .phone(entity.getPhoneNumber())
                .role(entity.getRole())
                .status(entity.getStatus())
                .build();
    }

    public PendingUserDto toPendingUserDto(UserEntity user) {
        return new PendingUserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getPesel(),
                user.getAddress(),
                user.getRole().name()
        );
    }


}
