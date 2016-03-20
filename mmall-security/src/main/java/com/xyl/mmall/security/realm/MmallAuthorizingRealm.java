package com.xyl.mmall.security.realm;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.util.StringUtils;

import com.netease.print.security.user.SimpleRole;
import com.netease.print.security.user.SimpleUser;
import com.netease.print.security.user.SimpleUserService;
import com.xyl.mmall.security.meta.MmallAccessUser;
import com.xyl.mmall.security.meta.MmallAuthenticationToken;
import com.xyl.mmall.security.meta.MmallUserType;

/**
 * MmallAuthorizingRealm.java created by skh at 2015年4月15日 上午10:34:38
 * 
 *
 * @author skh
 * @version 1.0
 */

public class MmallAuthorizingRealm extends AuthorizingRealm
{
    private SimpleUserService simpleUserService;
    
    public MmallAuthorizingRealm()
    {
        setCredentialsMatcher(new AllowAllCredentialsMatcher());
        setAuthenticationTokenClass(MmallAuthenticationToken.class);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.
     * shiro.subject.PrincipalCollection)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        MmallAccessUser mmallAccessUser = (MmallAccessUser) principals.getPrimaryPrincipal();
        String username = mmallAccessUser.getVistorName();
        List<SimpleRole> list = this.simpleUserService.getSimpleRoles(username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (list != null && (!list.isEmpty()))
        {
            for (SimpleRole role : list)
            {
                info.addRole(role.getName());
                info.addStringPermissions(role.getPermissionList());
            }
        }
        return info;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache
     * .shiro.authc.AuthenticationToken)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException
    {
        MmallAuthenticationToken mmallAuthenticationToken = (MmallAuthenticationToken) token;
        String username = mmallAuthenticationToken.getUsername();
        
        if (!StringUtils.hasText(username))
        {
            throw new UnknownAccountException("username is null");
        }
        
        SimpleUser simpleUser = this.simpleUserService.getSimpleUser(username);
        if (simpleUser == null)
        {
            throw new UnknownAccountException(username + " Not Found");
        }
        if (simpleUser.isLocked())
        {
            throw new LockedAccountException("The account for username " + username
                    + " is locked. Please contact your administrator to unlock.");
        }
        
        MmallAccessUser mmallAccessUser = new MmallAccessUser();
        mmallAccessUser.setVistorId(simpleUser.getId());
        mmallAccessUser.setVistorName(simpleUser.getUserName());
        MmallUserType userType = mmallAuthenticationToken.getUserType();
        if (userType != null)
        {
            mmallAccessUser.setUserType(userType);
        }
        mmallAccessUser.setCredential(mmallAuthenticationToken.getCredentials());
        
        //set the principal
        return new SimpleAuthenticationInfo(mmallAccessUser,
                mmallAuthenticationToken.getCredentials(), getName());
    }
    
    public void clearCacheAuthorizationInfo(String principal)
    {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(new MmallAccessUser(
                principal), getName());
        clearCachedAuthorizationInfo(principals);
    }
    
    public void setSimpleUserService(SimpleUserService simpleUserService)
    {
        this.simpleUserService = simpleUserService;
    }
}
