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

package org.apache.xerces.impl.xpath.regex;

import org.apache.xerces.impl.Constants;

/**
 * @xerces.internal
 * 
 * @version $Id: RangeTokenMapFactory.java 1175917 2011-09-26 15:59:08Z mrglavas $
 */
public final class RangeTokenMapFactory {
    
    private static RangeTokenMap xmlMap = null;
    private static RangeTokenMap xml11Map = null;
    
    static synchronized RangeTokenMap getXMLTokenMap(short xmlVersion) {
        if (xmlVersion == Constants.XML_VERSION_1_0) {
            if (xmlMap == null) {
                xmlMap = XMLTokenMap.instance();
            }
            
            return xmlMap;
        }
        
        if (xml11Map == null) {
            xml11Map = XML11TokenMap.instance();
        }
        return xml11Map;
    }
}
