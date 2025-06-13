package com.project.authentication.mappers.users

import com.project.authentication.entities.RoleEntity
import com.project.authentication.entities.UserEntity
import com.project.common.data.requests.authentication.RegisterCreateRequest


fun RegisterCreateRequest.toEntity(
    hashedPassword: String,
    roles: Set<RoleEntity>
) = UserEntity(
    username = this.username,
    password = hashedPassword,
    civilId = this.civilId,
    email = this.email,
    isActive = false,
    roles = roles.toMutableSet()
)