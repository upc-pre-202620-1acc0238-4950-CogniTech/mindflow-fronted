package com.cognitech.mindflow.data.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteConstraintException
import com.cognitech.mindflow.data.local.MindFlowDatabase
import com.cognitech.mindflow.data.local.MindFlowDatabase.Companion.TABLE_USERS
import com.cognitech.mindflow.data.local.SessionManager
import com.cognitech.mindflow.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.security.SecureRandom

class AuthRepository(
    private val database: MindFlowDatabase,
    private val session: SessionManager,
) {

    suspend fun signUp(name: String, email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            val now = System.currentTimeMillis()
            val values = ContentValues().apply {
                put("name", name.trim())
                put("email", email.trim().lowercase())
                put("password_hash", hashPassword(password))
                put("created_at", now)
            }
            try {
                val id = database.writableDatabase.insertOrThrow(TABLE_USERS, null, values)
                session.login(id)
                Result.success(User(id, email.trim().lowercase(), name.trim(), now))
            } catch (e: SQLiteConstraintException) {
                Result.failure(AuthException("Ya existe una cuenta con ese correo"))
            }
        }

    suspend fun signIn(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            val cursor = database.readableDatabase.query(
                TABLE_USERS,
                arrayOf("id", "email", "name", "password_hash", "created_at"),
                "email = ?",
                arrayOf(email.trim().lowercase()),
                null, null, null,
            )
            cursor.use {
                if (!it.moveToFirst() || !verifyPassword(password, it.getString(3))) {
                    return@withContext Result.failure(AuthException("Correo o contraseña incorrectos"))
                }
                val user = User(it.getLong(0), it.getString(1), it.getString(2), it.getLong(4))
                session.login(user.id)
                Result.success(user)
            }
        }

    suspend fun currentUser(): User? = withContext(Dispatchers.IO) {
        val id = session.currentUserId ?: return@withContext null
        database.readableDatabase.query(
            TABLE_USERS,
            arrayOf("id", "email", "name", "created_at"),
            "id = ?",
            arrayOf(id.toString()),
            null, null, null,
        ).use {
            if (it.moveToFirst()) User(it.getLong(0), it.getString(1), it.getString(2), it.getLong(3)) else null
        }
    }

    fun isLoggedIn(): Boolean = session.currentUserId != null

    fun logout() = session.logout()

    // salt:hash en hex con SHA-256. Suficiente para almacenamiento local;
    // el backend usa BCrypt para las cuentas reales.
    private fun hashPassword(password: String): String {
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        return salt.toHex() + ":" + sha256(salt + password.toByteArray()).toHex()
    }

    private fun verifyPassword(password: String, stored: String): Boolean {
        val (saltHex, hashHex) = stored.split(":").takeIf { it.size == 2 } ?: return false
        val salt = saltHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        return MessageDigest.isEqual(
            sha256(salt + password.toByteArray()).toHex().toByteArray(),
            hashHex.toByteArray(),
        )
    }

    private fun sha256(bytes: ByteArray): ByteArray =
        MessageDigest.getInstance("SHA-256").digest(bytes)

    private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
}

class AuthException(message: String) : Exception(message)
