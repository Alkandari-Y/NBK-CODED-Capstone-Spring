package com.project.authentication.users

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.project.authentication.entities.RoleEntity
import com.project.authentication.entities.UserEntity
import com.project.authentication.services.RoleService
import com.project.authentication.services.UserService
import com.project.common.data.requests.users.RoleCreateRequest
import com.project.common.data.requests.users.RolesAssignmentRequest
import com.project.common.exceptions.auth.UserNotFoundException
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = ["jwt-secret=supersecretkeythatshouldbe32byteslong123456"])
class UsersApiControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var userService: UserService

    @MockkBean
    lateinit var roleService: RoleService

    private lateinit var testUser: UserEntity
    private lateinit var testRole: RoleEntity

    @BeforeEach
    fun setup() {
        testRole = RoleEntity(id = 1L, name = "ROLE_USER")
        testUser = UserEntity(
            id = 1L,
            username = "testuser",
            email = "test@example.com",
            password = "password",
            isActive = false,
            roles = mutableSetOf(testRole)
        )
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `getUserDetails should return user info`() {
        every { userService.findUserById(1L) } returns testUser

        mockMvc.perform(get("/api/v1/users/details/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.userId").value(testUser.id!!))
            .andExpect(jsonPath("$.username").value(testUser.username))
            .andExpect(jsonPath("$.email").value(testUser.email))
            .andExpect(jsonPath("$.isActive").value(testUser.isActive))

        verify { userService.findUserById(1L) }
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `getUserDetails should return 404 if user not found`() {
        every { userService.findUserById(999L) } returns null

        mockMvc.perform(get("/api/v1/users/details/999"))
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `addRole should call roleService to assign roles`() {
        val request = RolesAssignmentRequest(roles = listOf("ROLE_ADMIN"))

        every { roleService.assignRolesToUser(1L, request.roles) } just Runs

        mockMvc.perform(
            post("/api/v1/users/add-role/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)

        verify { roleService.assignRolesToUser(1L, request.roles) }
    }


    @Test
    fun `setActiveUser should activate user`() {
        every { userService.setActiveUser(1L) } returns true

        mockMvc.perform(post("/api/v1/users/set-active/1"))
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }

    @Test
    @WithMockUser(roles = ["DEVELOPER"])
    fun `createNewRoles should create and return role`() {
        val request = RoleCreateRequest(name = "ROLE_NEW")
        val roleEntity = RoleEntity(id = 99L, name = request.name)

        every { roleService.createRole(any()) } returns roleEntity

        mockMvc.perform(
            post("/api/v1/users/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(99))
            .andExpect(jsonPath("$.name").value("ROLE_NEW"))

        verify { roleService.createRole(match { it.name == request.name }) }
    }

    @Test
    @WithMockUser(roles = ["DEVELOPER"])
    fun `removeRole should remove each role`() {
        val request = RolesAssignmentRequest(roles = listOf("ROLE_ADMIN", "ROLE_USER"))

        every { roleService.removeRoleFromUser(1L, "ROLE_ADMIN") } just Runs
        every { roleService.removeRoleFromUser(1L, "ROLE_USER") } just Runs

        mockMvc.perform(
            post("/api/v1/users/remove-role/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)

        verify { roleService.removeRoleFromUser(1L, "ROLE_ADMIN") }
        verify { roleService.removeRoleFromUser(1L, "ROLE_USER") }
    }

    @Test
    fun `setActiveUser should return false if activation fails`() {
        every { userService.setActiveUser(1L) } returns false

        mockMvc.perform(post("/api/v1/users/set-active/1"))
            .andExpect(status().isOk)
            .andExpect(content().string("false"))

        verify { userService.setActiveUser(1L) }
    }

    @Test
    @WithMockUser(roles = ["DEVELOPER"])
    fun `createNewRoles should return 400 for invalid input`() {
        val invalidRequest = RoleCreateRequest(name = "") // assume validation requires non-empty

        mockMvc.perform(
            post("/api/v1/users/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `addRole should return 403 if unauthorized`() {
        val request = RolesAssignmentRequest(roles = listOf("ROLE_ADMIN"))

        mockMvc.perform(
            post("/api/v1/users/add-role/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(roles = ["DEVELOPER"])
    fun `removeRole should return 400 for invalid payload`() {
        val invalidJson = """{"roles": []}""" // assume at least one role is required

        mockMvc.perform(
            post("/api/v1/users/remove-role/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `getUserDetails should return 403 if unauthorized`() {
        mockMvc.perform(get("/api/v1/users/details/1"))
            .andExpect(status().isForbidden)
    }

    @Test
    fun `removeRole should return 403 if not authorized`() {
        val request = RolesAssignmentRequest(roles = listOf("ROLE_ADMIN"))

        mockMvc.perform(
            post("/api/v1/users/remove-role/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `createNewRoles should return 403 if user is not DEVELOPER`() {
        val request = RoleCreateRequest(name = "ROLE_CUSTOM")

        mockMvc.perform(
            post("/api/v1/users/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `addRole should return 400 if request body is malformed`() {
        val malformedJson = """{ "roles": null }""" // violates @Valid constraint

        mockMvc.perform(
            post("/api/v1/users/add-role/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson)
        ).andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(roles = ["DEVELOPER"])
    fun `removeRole should return 400 when roles field is missing`() {
        val invalidJson = """{}""" // roles field required

        mockMvc.perform(
            post("/api/v1/users/remove-role/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        ).andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(roles = ["DEVELOPER"])
    fun `createNewRoles should return 400 when role already exists`() {
        val request = RoleCreateRequest(name = "ROLE_USER")
        every { roleService.createRole(any()) } throws IllegalArgumentException("Role already exists")

        mockMvc.perform(
            post("/api/v1/users/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest)
    }


    @Test
    fun `setActiveUser should return 404 if user not found`() {
        every { userService.setActiveUser(999L) } throws UserNotFoundException()

        mockMvc.perform(post("/api/v1/users/set-active/999"))
            .andExpect(status().isNotFound)
    }

}