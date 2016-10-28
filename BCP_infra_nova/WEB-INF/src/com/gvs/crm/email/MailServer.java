// FrontEnd Plus GUI for JAD
// DeCompiled : MailServer.class

package com.gvs.crm.email;


public class MailServer
{

    private String mailServer;
    private String username;
    private String password;
    private boolean needsAuth;

    public MailServer()
    {
        mailServer = null;
        username = null;
        password = null;
        needsAuth = true;
    }

    public MailServer(String mailServer, String username, String password, boolean needsAuth)
    {
        this.mailServer = null;
        this.username = null;
        this.password = null;
        this.needsAuth = true;
        this.mailServer = mailServer;
        this.username = username;
        this.password = password;
        this.needsAuth = needsAuth;
    }

    public String getMailServer()
    {
        return mailServer;
    }

    public void setMailServer(String mailServer)
    {
        this.mailServer = mailServer;
    }

    public boolean isNeedsAuth()
    {
        return needsAuth;
    }

    public void setNeedsAuth(boolean needsAuth)
    {
        this.needsAuth = needsAuth;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
