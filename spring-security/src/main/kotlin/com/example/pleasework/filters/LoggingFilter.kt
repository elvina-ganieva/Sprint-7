package com.example.pleasework.filters

import java.time.Instant
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest


@WebFilter(urlPatterns = ["/app/*", "/api/*"], servletNames = ["AuthServlet"])
class LoggingFilter: Filter {
    override fun doFilter(req: ServletRequest?, resp: ServletResponse?, fc: FilterChain?) {
        val request = req as HttpServletRequest

        println()
        println("protocol: ${request.protocol}")
        println("method: ${request.method}")
        println("date: ${Instant.now()}")
        println("servletPath: ${request.servletPath}")
        println()

        fc?.doFilter(req, resp)
    }
}
