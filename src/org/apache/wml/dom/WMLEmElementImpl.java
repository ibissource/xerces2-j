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
package org.apache.wml.dom;

import org.apache.wml.WMLEmElement;

/**
 * @xerces.internal
 * @version $Id: WMLEmElementImpl.java 665720 2008-06-09 15:28:43Z mrglavas $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */
public class WMLEmElementImpl extends WMLElementImpl implements WMLEmElement {

    private static final long serialVersionUID = 7962278391619830801L;
    
    public WMLEmElementImpl (WMLDocumentImpl owner, String tagName) {
        super( owner, tagName);
    }
    
    public void setClassName(String newValue) {
        setAttribute("class", newValue);
    }
    
    public String getClassName() {
        return getAttribute("class");
    }
    
    public void setXmlLang(String newValue) {
        setAttribute("xml:lang", newValue);
    }
    
    public String getXmlLang() {
        return getAttribute("xml:lang");
    }
    
    public void setId(String newValue) {
        setAttribute("id", newValue);
    }
    
    public String getId() {
        return getAttribute("id");
    }
    
}
