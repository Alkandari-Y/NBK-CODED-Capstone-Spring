package com.project.authentication.mappers

import com.project.authentication.entities.RoleEntity
import com.project.common.data.requests.users.RoleCreateRequest

fun RoleCreateRequest.toEntity() = RoleEntity(name = name)