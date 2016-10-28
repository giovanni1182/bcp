// FrontEnd Plus GUI for JAD
// DeCompiled : MailMessage.class

package com.gvs.crm.email;


public class MailMessage
{

    private String to;
    private String cc;
    private String bcc;
    private String from;
    private String subject;
    private String text;

    public MailMessage()
    {
    }

    public MailMessage(String to, String cc, String bcc, String from, String subject, String text)
    {
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.from = from;
        this.subject = subject;
        this.text = text;
    }

    public String getBcc()
    {
        return bcc;
    }

    public void setBcc(String bcc)
    {
        this.bcc = bcc;
    }

    public String getCc()
    {
        return cc;
    }

    public void setCc(String cc)
    {
        this.cc = cc;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }
}
