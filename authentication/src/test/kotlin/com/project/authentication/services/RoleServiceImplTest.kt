package com.project.authentication.services

import com.project.authentication.entities.RoleEntity
import com.project.authentication.entities.UserEntity
import com.project.authentication.repositories.RoleRepository
import com.project.authentication.repositories.UserRepository
import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import com.project.common.exceptions.auth.UserNotFoundException
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import java.util.*
import org.springframework.data.repository.findByIdOrNull

class RoleServiceImplTest {

    private lateinit var roleRepository: RoleRepository
    private lateinit var userRepository: UserRepository
    private lateinit var roleService: RoleServiceImpl

    private val defaultRole = RoleEntity(id = 1L, name = "ROLE_USER")
    private val adminRole = RoleEntity(id = 2L, name = "ROLE_ADMIN")

    private val user = UserEntity(
        id = 100L,
        username = "testuser",
        password = "hashed",
        email = "test@example.com",
        civilId = "12345678",
        roles = mutableSetOf(defaultRole),
        isActive = true
    )

    @BeforeEach
    fun setup() {
        roleRepository = mockk()
        userRepository = mockk()
        roleService = RoleServiceImpl(roleRepository, userRepository)
    }

    @Test
    fun `getDefaultRole should return existing role`() {
        every { roleRepository.findByName("ROLE_USER") } returns defaultRole

        val result = roleService.getDefaultRole()
        assert(result == defaultRole)
    }

    @Test
    fun `getDefaultRole should create and return role if not exists`() {
        every { roleRepository.findByName("ROLE_USER") } returns null
        every { roleRepository.save(any()) } returns defaultRole

        val result = roleService.getDefaultRole()

        assert(result.name == "ROLE_USER")
        verify { roleRepository.save(match { it.name == "ROLE_USER" }) }
    }

    @Test
    fun `createRole should save role`() {
        every { roleRepository.save(adminRole) } returns adminRole

        val result = roleService.createRole(adminRole)
        assert(result == adminRole)
    }

    @Test
    fun `assignRolesToUser should assign roles to user`() {
        val rolesToAssign = listOf("ROLE_ADMIN")
        val updatedUser = user.copy(roles = mutableSetOf(defaultRole, adminRole))

        every { userRepository.findByIdOrNull(user.id!!) } returns user
        every { roleRepository.findAllByNameIn(rolesToAssign) } returns listOf(adminRole)
        every { userRepository.save(any()) } returns updatedUser

        roleService.assignRolesToUser(user.id!!, rolesToAssign)

        verify { userRepository.save(match { it.roles.contains(adminRole) }) }
    }

    @Test
    fun `assignRolesToUser should throw if user not found`() {
        every { userRepository.findByIdOrNull(999L) } returns null

        assertThrows<UserNotFoundException> {
            roleService.assignRolesToUser(999L, listOf("ROLE_ADMIN"))
        }
    }

    @Test
    fun `removeRoleFromUser should remove role and save`() {
        val userWithAdmin = user.copy(roles = mutableSetOf(defaultRole, adminRole))

        every { userRepository.findById(user.id!!) } returns Optional.of(userWithAdmin)
        every { roleRepository.findByName("ROLE_ADMIN") } returns adminRole
        every { userRepository.save(any()) } returns userWithAdmin

        roleService.removeRoleFromUser(user.id!!, "ROLE_ADMIN")

        verify { userRepository.save(match { !it.roles.contains(adminRole) }) }
    }

    @Test
    fun `removeRoleFromUser should throw if user not found`() {
        every { userRepository.findById(999L) } returns Optional.empty()

        val exception = assertThrows<APIException> {
            roleService.removeRoleFromUser(999L, "ROLE_ADMIN")
        }

        assert(exception.code == ErrorCode.USER_NOT_FOUND)
        assert(exception.httpStatus == HttpStatus.NOT_FOUND)
    }

    @Test
    fun `removeRoleFromUser should throw if role not found`() {
        every { userRepository.findById(user.id!!) } returns Optional.of(user)
        every { roleRepository.findByName("ROLE_UNKNOWN") } returns null

        val exception = assertThrows<APIException> {
            roleService.removeRoleFromUser(user.id!!, "ROLE_UNKNOWN")
        }

        assert(exception.code == ErrorCode.ROLE_NOT_FOUND)
        assert(exception.httpStatus == HttpStatus.NOT_FOUND)
    }
    @Test
    fun `assignRolesToUser should do nothing if roles list is empty`() {
        every { userRepository.findByIdOrNull(user.id!!) } returns user
        every { roleRepository.findAllByNameIn(emptyList()) } returns emptyList()
        every { userRepository.save(any()) } returns user

        roleService.assignRolesToUser(user.id!!, emptyList())

        verify(exactly = 1) { userRepository.save(user) }
    }

    @Test
    fun `assignRolesToUser should not add duplicate roles`() {
        val userWithAdmin = user.copy(roles = mutableSetOf(defaultRole, adminRole))

        every { userRepository.findByIdOrNull(user.id!!) } returns userWithAdmin
        every { roleRepository.findAllByNameIn(listOf("ROLE_ADMIN")) } returns listOf(adminRole)
        every { userRepository.save(any()) } returns userWithAdmin

        roleService.assignRolesToUser(user.id!!, listOf("ROLE_ADMIN"))

        verify {
            userRepository.save(match {
                it.roles.count { role -> role.name == "ROLE_ADMIN" } == 1
            })
        }
    }

    @Test
    fun `removeRoleFromUser should not fail if role exists but not assigned to user`() {
        val userWithoutAdmin = user.copy(roles = mutableSetOf(defaultRole))

        every { userRepository.findById(user.id!!) } returns Optional.of(userWithoutAdmin)
        every { roleRepository.findByName("ROLE_ADMIN") } returns adminRole

        roleService.removeRoleFromUser(user.id!!, "ROLE_ADMIN")

        verify(exactly = 0) { userRepository.save(any()) }
    }


    @Test
    fun `removeRoleFromUser should result in empty roles if last role is removed`() {
        val userWithOneRole = user.copy(roles = mutableSetOf(defaultRole))

        every { userRepository.findById(user.id!!) } returns Optional.of(userWithOneRole)
        every { roleRepository.findByName("ROLE_USER") } returns defaultRole
        every { userRepository.save(any()) } returns userWithOneRole.copy(roles = mutableSetOf())

        roleService.removeRoleFromUser(user.id!!, "ROLE_USER")

        verify {
            userRepository.save(match { it.roles.isEmpty() })
        }
    }

}
