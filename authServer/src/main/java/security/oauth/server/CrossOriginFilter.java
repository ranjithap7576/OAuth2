package security.oauth.server;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Allows cross origin for testing swagger docs using swagger-ui from local file
 * system
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CrossOriginFilter extends OncePerRequestFilter {

	static final String ORIGIN = "Origin";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String origin = request.getHeader(ORIGIN);

		response.setHeader("Access-Control-Allow-Origin", "*");// * or origin as u prefer
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "PUT, POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "content-type, authorization");

		if (request.getMethod().equals("OPTIONS"))
			response.setStatus(HttpServletResponse.SC_OK);
		else
			filterChain.doFilter(request, response);

	}

}