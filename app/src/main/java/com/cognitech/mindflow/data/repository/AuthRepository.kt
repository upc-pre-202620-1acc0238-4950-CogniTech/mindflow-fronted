package com.cognitech.mindflow.data.repository

import android.content.ContentValues
import android.database.Cursor
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
    val session: SessionManager,
    private val habitRepository: HabitRepository,
) {

    suspend fun signUp(name: String, email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put("name", name.trim())
                put("email", email.trim().lowercase())
                put("password_hash", hashPassword(password))
                put("created_at", System.currentTimeMillis())
            }
            try {
                val id = database.writableDatabase.insertOrThrow(TABLE_USERS, null, values)
                habitRepository.seedDefaults(id)
                session.login(id)
                Result.success(findById(id)!!)
            } catch (e: SQLiteConstraintException) {
                Result.failure(AuthException("Ya existe una cuenta con ese correo"))
            }
        }

    suspend fun signIn(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            val cursor = database.readableDatabase.query(
                TABLE_USERS, arrayOf("id", "password_hash"), "email = ?",
                arrayOf(email.trim().lowercase()), null, null, null,
            )
            val id = cursor.use {
                if (!it.moveToFirst() || !verifyPassword(password, it.getString(1))) null else it.getLong(0)
            } ?: return@withContext Result.failure(AuthException("Correo o contraseña incorrectos"))
            session.login(id)
            Result.success(findById(id)!!)
        }

    suspend fun currentUser(): User? = withContext(Dispatchers.IO) {
        session.currentUserId?.let(::findById)
    }

    suspend fun updateProfile(userId: Long, name: String, occupation: String, timezone: String) =
        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put("name", name.trim())
                put("occupation", occupation.trim())
                put("timezone", timezone.trim())
            }
            database.writableDatabase.update(TABLE_USERS, values, "id = ?", arrayOf(userId.toString()))
        }

    suspend fun setPlan(userId: Long, plan: String) = withContext(Dispatchers.IO) {
        val values = ContentValues().apply { put("plan", plan) }
        database.writableDatabase.update(TABLE_USERS, values, "id = ?", arrayOf(userId.toString()))
    }

    suspend fun deleteAccount(userId: Long) = withContext(Dispatchers.IO) {
        database.writableDatabase.delete(TABLE_USERS, "id = ?", arrayOf(userId.toString()))
        session.logout()
    }

    fun isLoggedIn(): Boolean = session.currentUserId != null

    fun logout() = session.logout()

    private fun findById(id: Long): User? = database.readableDatabase.query(
        TABLE_USERS,
        arrayOf("id", "email", "name", "occupation", "timezone", "plan", "created_at"),
        "id = ?", arrayOf(id.toString()), null, null, null,
    ).use { if (it.moveToFirst()) it.toUser() else null }

    private fun Cursor.toUser() = User(
        id = getLong(0),
        email = getString(1),
        name = getString(2),
        occupation = getString(3),
        timezone = getString(4),
        plan = getString(5),
        createdAt = getLong(6),
    )

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
