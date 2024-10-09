/**
 * 
 */
package com.education.api.config.interceptor;
import com.education.common.interceptor.BaseInterceptor;
import com.education.common.model.JwtToken;
import com.education.common.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户认证拦截器
 *   
 *   

 */
@Component
public class AdminAuthInterceptor extends BaseInterceptor {

	@Autowired
	private JwtToken adminJwtToken;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object)
			throws Exception {
		String targetUrl = RequestUtils.getRequestUrl(request);
		if (targetUrl.startsWith("/api/dict")) {
			return true;
		}

		else if (targetUrl.startsWith("/api/upload")) {
			/*String header = request.getHeader("httpType");
			if ("student-product".equals(header)) {
				return true;
			}*/
            return true;
		}
		return checkToken(adminJwtToken, request, response);
	}
}
