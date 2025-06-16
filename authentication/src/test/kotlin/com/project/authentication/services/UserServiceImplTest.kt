package com.project.authentication.services

import com.project.common.data.requests.authentication.RegisterCreateRequest
import com.project.authentication.entities.RoleEntity
import com.project.authentication.entities.UserEntity
import com.project.authentication.repositories.UserRepository
import com.project.common.exceptions.auth.UserExistsException
import com.project.common.exceptions.auth.UserNotFoundException
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.data.repository.findByIdOrNull

class UserServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var roleService: RoleService
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var mailService: MailService
    private lateinit var userService: UserService

    private lateinit var testUser: UserEntity
    private lateinit var defaultRole: RoleEntity

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        roleService = mockk()
        passwordEncoder = mockk()
        mailService = mockk()
        userService = UserServiceImpl(userRepository, roleService, passwordEncoder, mailService)

        defaultRole = RoleEntity(id = 1L, name = "ROLE_USER")
        testUser = UserEntity(
            id = 123L,
            username = "testuser",
            password = "encodedPassword",
            email = "test@example.com",
            isActive = false,
            roles = mutableSetOf(defaultRole)
        )
    }

    @Test
    fun `createUser should save user and send email`() {
        val request = RegisterCreateRequest(
            username = testUser.username,
            password = "plainPassword",
            email = testUser.email
        )

        every { userRepository.existsByUsernameOrEmail(any(), any()) } returns false
        every { passwordEncoder.encode("plainPassword") } returns "encodedPassword"
        every { roleService.getDefaultRole() } returns defaultRole
        every { userRepository.save(any()) } returns testUser
        every { mailService.sendHtmlEmail(any(), any(), any(), any()) } just Runs

        val createdUser = userService.createUser(request)

        assertEquals(testUser.username, createdUser.username)
        assertEquals(testUser.email, createdUser.email)
        verify(exactly = 1) { userRepository.save(any()) }
        verify(exactly = 1) { mailService.sendHtmlEmail(testUser.email, any(), any(), any()) }
    }

    @Test
    fun `createUser should throw when user already exists`() {
        val request = RegisterCreateRequest(
            username = "existing",
            password = "Password1",
            email = "exist@example.com",
            civilId = "22222222"
        )

        every { userRepository.existsByUsernameOrEmail(any(), any()) } returns true

        assertThrows(UserExistsException::class.java) {
            userService.createUser(request)
        }
    }

    @Test
    fun `createUser should assign default role to user`() {
        val request = RegisterCreateRequest(
            username = testUser.username,
            password = "plainPassword",
            email = testUser.email,
        )

        every { userRepository.existsByUsernameOrEmail(any(), any()) } returns false
        every { passwordEncoder.encode("plainPassword") } returns "encodedPassword"
        every { roleService.getDefaultRole() } returns defaultRole
        every { userRepository.save(any()) } answers { firstArg() }
        every { mailService.sendHtmlEmail(any(), any(), any(), any()) } just Runs

        val createdUser = userService.createUser(request)

        assertTrue(createdUser.roles.contains(defaultRole))
    }


    @Test
    fun `findUserById should return user when found`() {
        every { userRepository.findByIdOrNull(123L) } returns testUser
        val result = userService.findUserById(123L)
        assertEquals(testUser, result)
    }

    @Test
    fun `findUserById should return null when not found`() {
        every { userRepository.findByIdOrNull(999L) } returns null
        val result = userService.findUserById(999L)
        assertNull(result)
    }

    @Test
    fun `findUserByUsername should return user`() {
        every { userRepository.findByUsername("testuser") } returns testUser
        val result = userService.findUserByUsername("testuser")
        assertEquals(testUser, result)
    }

    @Test
    fun `setActiveUser should activate user`() {
        val inactiveUser = testUser.copy(isActive = false)
        val activeUser = testUser.copy(isActive = true)

        every { userRepository.findByIdOrNull(123L) } returns inactiveUser
        every { userRepository.save(activeUser) } returns activeUser

        val result = userService.setActiveUser(123L)
        assertTrue(result)

        verify { userRepository.save(match { it.isActive }) }
    }

    @Test
    fun `setActiveUser should throw when user not found`() {
        every { userRepository.findByIdOrNull(999L) } returns null
        assertThrows(UserNotFoundException::class.java) {
            userService.setActiveUser(999L)
        }
    }

    @Test
    fun `createUser should encode password`() {
        val request = RegisterCreateRequest(
            username = testUser.username,
            password = "rawPassword",
            email = testUser.email,
        )

        every { userRepository.existsByUsernameOrEmail(any(), any()) } returns false
        every { passwordEncoder.encode("rawPassword") } returns "encodedPassword"
        every { roleService.getDefaultRole() } returns defaultRole
        every { userRepository.save(capture(slot())) } returns testUser
        every { mailService.sendHtmlEmail(any(), any(), any(), any()) } just Runs

        userService.createUser(request)

        verify { passwordEncoder.encode("rawPassword") }
    }


    @Test
    fun `createUser should send welcome email with correct content`() {
        val request = RegisterCreateRequest(
            username = testUser.username,
            password = "anyPassword",
            email = testUser.email,
        )

        every { userRepository.existsByUsernameOrEmail(any(), any()) } returns false
        every { passwordEncoder.encode(any()) } returns "encodedPassword"
        every { roleService.getDefaultRole() } returns defaultRole
        every { userRepository.save(any()) } returns testUser

        val emailSlot = slot<String>()
        val subjectSlot = slot<String>()
        val usernameSlot = slot<String>()
        val bodySlot = slot<String>()

        every {
            mailService.sendHtmlEmail(
                capture(emailSlot),
                capture(subjectSlot),
                capture(usernameSlot),
                capture(bodySlot)
            )
        } just Runs

        userService.createUser(request)

        assertEquals(testUser.email, emailSlot.captured)
        assertEquals("Account Activation", subjectSlot.captured)
        assertTrue(bodySlot.captured.contains("account has been created"))
    }

}
