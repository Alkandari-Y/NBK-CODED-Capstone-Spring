package com.project.authentication.users

import com.project.authentication.entities.RoleEntity
import com.project.authentication.mappers.roles.toEntity
import com.project.authentication.services.RoleService
import com.project.authentication.services.UserService
import com.project.common.data.requests.users.RoleCreateRequest
import com.project.common.data.responses.users.RolesAssignmentRequest
import com.project.common.exceptions.auth.UserNotFoundException
import com.project.common.data.responses.authentication.UserInfoDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/users")
class UsersApiController(
    private val userService: UserService,
    private val roleService: RoleService
) {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/details/{userId}")
    fun getUserDetails(
        @PathVariable("userId") userId: Long,
    ): ResponseEntity<UserInfoDto> {
        val user = userService.findUserById(userId)
            ?: throw UserNotFoundException()
        return ResponseEntity(
            UserInfoDto(
                userId = user.id!!,
                username = user.username,
                email = user.email,
                isActive = user.isActive
            ),
            HttpStatus.OK
        )
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/details/{userId}/roles")
    fun addRole(
        @PathVariable("userId") userId: Long,
        @Valid @RequestBody newRoles: RolesAssignmentRequest,
    ): ResponseEntity<Unit> {
        roleService.assignRolesToUser(userId, newRoles.roles)
        return ResponseEntity(HttpStatus.OK)
    }

    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    @DeleteMapping("/details/{userId}/roles")
    fun removeRole(
        @PathVariable("userId") userId: Long,
        @Valid @RequestBody roleRequest: RolesAssignmentRequest,
    ): ResponseEntity<Unit> {
        roleRequest.roles.forEach { roleName ->
            roleService.removeRoleFromUser(userId, roleName)
        }
        return ResponseEntity(HttpStatus.OK)
    }

    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    @PostMapping("/roles")
    fun createNewRoles(
        @Valid @RequestBody newRole: RoleCreateRequest,
    ): ResponseEntity<RoleEntity> {
        return ResponseEntity(
            roleService.createRole(
                newRole.toEntity()
            ),
            HttpStatus.CREATED)
    }
}

