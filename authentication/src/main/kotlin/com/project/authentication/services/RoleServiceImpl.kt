package com.project.authentication.services

import com.project.authentication.entities.RoleEntity
import com.project.common.exceptions.auth.UserNotFoundException
import com.project.authentication.repositories.RoleRepository
import com.project.authentication.repositories.UserRepository
import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository
): RoleService {
    override fun getDefaultRole(): RoleEntity {
        // TODO("ADD AS CONFIGURATION LATER")
        val defaultRoleName = "ROLE_USER"
        val defaultRole = roleRepository.findByName(defaultRoleName)
            ?: roleRepository.save(RoleEntity(null, defaultRoleName))

        return defaultRole
    }

    override fun createRole(role: RoleEntity): RoleEntity {
        return roleRepository.save(role)
    }

    override fun assignRolesToUser(userId: Long, roles: Collection<String>) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()

        val fetchedRoles = roleRepository.findAllByNameIn(roles)

        val updatedRoles = user.roles.toMutableSet()
        updatedRoles.addAll(fetchedRoles)
        userRepository.save(user.copy(roles = updatedRoles))

    }
    @Transactional
    override fun removeRoleFromUser(userId: Long, role: String) {
        val user = userRepository.findById(userId)
            .orElseThrow { APIException(
                "User not found",
                code = ErrorCode.USER_NOT_FOUND,
                httpStatus = HttpStatus.NOT_FOUND) }

        val roleToRemove = roleRepository.findByName(role)
            ?: throw APIException(
                "Role not found",
                code = ErrorCode.ROLE_NOT_FOUND,
                httpStatus = HttpStatus.NOT_FOUND
            )

        if (user.roles.contains(roleToRemove)) {
            user.roles.remove(roleToRemove)
            userRepository.save(user)
        }
    }
}


