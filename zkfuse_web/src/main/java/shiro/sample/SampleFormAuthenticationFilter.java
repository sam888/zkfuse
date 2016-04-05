/**
 * 
 */
package shiro.sample;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * @author ashish
 *
 */
public class SampleFormAuthenticationFilter extends FormAuthenticationFilter {

	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
		String message = ae.getMessage();
		request.setAttribute(getFailureKeyAttribute(), message);
	}

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        super.onLoginSuccess( token, subject, request, response );

        //TODO handle the role here for authorization
        return true;
    }
}
