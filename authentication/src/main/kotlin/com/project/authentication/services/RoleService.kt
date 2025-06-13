package com.project.authentication.services

import com.project.authentication.entities.RoleEntity

interface RoleService {
    fun getDefaultRole(): RoleEntity
    fun createRole(role: RoleEntity): RoleEntity
    fun assignRolesToUser(userId: Long, roles: Collection<String>)
    fun removeRoleFromUser(userId: Long, role: String)
}