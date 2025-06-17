package com.project.banking.permissions

import com.project.banking.entities.AccountEntity
import com.project.common.exceptions.AccessDeniedException
import com.project.common.security.RemoteUserPrincipal

fun hasAccountPermission(account: AccountEntity, user: RemoteUserPrincipal): Boolean {
    val isOwner = account.ownerId == user.getUserId()
    val isAdmin = user.authorities.any { it.authority == "ROLE_ADMIN" }

    if (!isOwner && !isAdmin) {
        throw AccessDeniedException()
    }
    return true
}