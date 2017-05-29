/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.representation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@XmlRootElement
public class LinkRepresentation
{
    private String rel    = "";
    private String href   = "";
    private String type   = "";
    private String method = "";
    private int nextStepDelay;

    public LinkRepresentation(final String rel, final String href, final String type, final String httpMethod, final int nextStepDelay)
    {
        this.rel = rel;
        this.type = type;
        this.href = href;
        this.method = httpMethod;
        this.nextStepDelay = nextStepDelay;
    }

    public static LinkRepresentation from(final Link link, final String httpMethod, final int nextStepDelay)
    {
        return new LinkRepresentation(link.getRel(), link.getUri().toASCIIString(), link.getType(), httpMethod, nextStepDelay);
    }

    public String getRel()
    {
        return rel;
    }

    public void setRel(final String rel)
    {
        this.rel = rel;
    }

    public String getHref()
    {
        return href;
    }

    public void setHref(final String href)
    {
        this.href = href;
    }

    public String getType()
    {
        return type;
    }

    public void setType(final String type)
    {
        this.type = type;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(final String method)
    {
        this.method = method;
    }

    public int getNextStepDelay()
    {
        return nextStepDelay;
    }

    public void setNextStepDelay(final int nextStepDelay)
    {
        this.nextStepDelay = nextStepDelay;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final LinkRepresentation that = (LinkRepresentation) o;

        return new EqualsBuilder().append(rel, that.rel).append(type, that.type).append(href, that.href).append(method, that.method).append(nextStepDelay, that.nextStepDelay)
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(rel).append(type).append(href).append(method).append(nextStepDelay).toHashCode();
    }
}
