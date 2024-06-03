package com.daelim.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerErrorException
import java.security.Key

@Service
class UserService(
    @Value("\${jwt.secret}") secretKey: String,
) {
    private var key: Key? = null

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }


    fun verifyToken(token: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun parseClaims(accessToken: String?): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }

    fun getUserId(): Long {
        val requestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val request = requestAttributes.request
        println("request: ${request.headerNames.toList()}   ${request.getHeader("Authorization")}   ")
        val authorization = request.getHeader("Authorization") ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is required")
        val token = authorization.substring(7)
        val claims = try {
            parseClaims(token)
        }catch (e: Exception){
            throw throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token")
        }
        return (claims["userId"] as Int).toLong()
    }
}