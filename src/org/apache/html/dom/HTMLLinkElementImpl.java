/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.html.dom;

import org.w3c.dom.html.HTMLLinkElement;

/**
 * @xerces.internal
 * @version $Revision: 1029426 $ $Date: 2010-10-31 22:55:00 +0530 (Sun, 31 Oct 2010) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLLinkElement
 * @see org.apache.xerces.dom.ElementImpl
 */
public class HTMLLinkElementImpl
    extends HTMLElementImpl
    implements HTMLLinkElement
{

    private static final long serialVersionUID = 874345520063418879L;

    public boolean getDisabled()
    {
        return getBinary( "disabled" );
    }
    
    
    public void setDisabled( boolean disabled )
    {
        setAttribute( "disabled", disabled );
    }

    
    public String getCharset()
    {
        return getAttribute( "charset" );
    }
    
    
    public void setCharset( String charset )
    {
        setAttribute( "charset", charset );
    }
    
    
    public String getHref()
    {
        return getAttribute( "href" );
    }
    
    
    public void setHref( String href )
    {
        setAttribute( "href", href );
    }
    
    
    public String getHreflang()
    {
        return getAttribute( "hreflang" );
    }
    
    
    public void setHreflang( String hreflang )
    {
        setAttribute( "hreflang", hreflang );
    }

    
    public String getMedia()
    {
        return getAttribute( "media" );
    }
    
    
    public void setMedia( String media )
    {
        setAttribute( "media", media );
    }

  
    public String getRel()
    {
        return getAttribute( "rel" );
    }
    
    
    public void setRel( String rel )
    {
        setAttribute( "rel", rel );
    }
    
    
    public String getRev()
    {
        return getAttribute( "rev" );
    }
    
    
    public void setRev( String rev )
    {
        setAttribute( "rev", rev );
    }

    
    public String getTarget()
    {
        return getAttribute( "target" );
    }
    
    
    public void setTarget( String target )
    {
        setAttribute( "target", target );
    }
  
  
    public String getType()
    {
        return getAttribute( "type" );
    }
    
    
    public void setType( String type )
    {
        setAttribute( "type", type );
    }
    
    
    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLLinkElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }


}

