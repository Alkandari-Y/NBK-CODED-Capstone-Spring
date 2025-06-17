package com.project.authentication.services

import com.project.common.data.requests.authentication.RegisterCreateRequest
import com.project.authentication.entities.UserEntity
import com.project.authentication.mappers.toEntity
import com.project.common.exceptions.auth.UserExistsException
import com.project.common.exceptions.auth.UserNotFoundException
import com.project.authentication.repositories.UserRepository
import com.project.common.enums.ErrorCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleService: RoleService,
    private val passwordEncoder: PasswordEncoder,
    private val mailService: MailService
): UserService {
    override fun createUser(user: RegisterCreateRequest): UserEntity {
        val usernameExists = userRepository.existsByUsernameOrEmail(user.username, user.email)

        if (usernameExists) {
            throw UserExistsException("User already exists", code = ErrorCode.USER_ALREADY_EXISTS)
        }
        val defaultRole = roleService.getDefaultRole()

        val newUser = userRepository.save(user.toEntity(
            hashedPassword = passwordEncoder.encode(user.password),
            roles = setOf(defaultRole)
        ))
        mailService.sendHtmlEmail(
            to = newUser.email,
            subject = "Account Activation",
            username = user.username,
            bodyText = "Your account has been created, please update your account information or reach out to one of our representatives")
        return newUser
    }

    override fun findUserById(userId: Long): UserEntity? {
        return userRepository.findByIdOrNull(userId)
    }

    override fun findUserByUsername(username: String): UserEntity? {
        return userRepository.findByUsername(username)
    }

    override fun setActiveUser(userId: Long): Boolean {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()
        userRepository.save(user.copy(isActive = true))
        return true
    }
}