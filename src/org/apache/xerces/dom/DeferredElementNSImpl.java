/* $Id$ */
/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xerces" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, International
 * Business Machines, Inc., http://www.apache.org.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

/*
 * WARNING: because java doesn't support multi-inheritance some code is
 * duplicated. If you're changing this file you probably want to change
 * DeferredElementImpl.java at the same time.
 */

package org.apache.xerces.dom;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.xerces.utils.StringPool;

import org.w3c.dom.*;

/**
 * DeferredElementNSImpl is to ElementNSImpl, what DeferredElementImpl is to
 * ElementImpl. 
 * @see DeferredElementImpl
 */
public class DeferredElementNSImpl
    extends ElementNSImpl
    implements DeferredNode {

    //
    // Constants
    //

    /** Serialization version. */
    static final long serialVersionUID = -5001885145370927385L;

    //
    // Data
    //

    /** Node index. */
    protected transient int fNodeIndex;

    //
    // Constructors
    //

    /**
     * This is the deferred constructor. Only the fNodeIndex is given here. All
     * other data, can be requested from the ownerDocument via the index.
     */
    DeferredElementNSImpl(DeferredDocumentImpl ownerDoc, int nodeIndex) {
        super(ownerDoc, null);

        fNodeIndex = nodeIndex;
        syncChildren(true);

    } // <init>(DocumentImpl,int)

    //
    // DeferredNode methods
    //

    /** Returns the node index. */
    public final int getNodeIndex() {
        return fNodeIndex;
    }

    //
    // Protected methods
    //

    /** Synchronizes the data (name and value) for fast nodes. */
    protected final void synchronizeData() {

        // no need to sync in the future
        syncData(false);

        // fluff data
        DeferredDocumentImpl ownerDocument =
            (DeferredDocumentImpl) this.ownerDocument;
        int elementQName = ownerDocument.getNodeName(fNodeIndex);
        StringPool pool = ownerDocument.getStringPool();
        name = pool.toString(elementQName);

        // extract local part from QName
        int index = name.indexOf(':');
        if (index < 0) {
            localName = name;
        } 
        else {
            localName = name.substring(index + 1);
        }

	namespaceURI = pool.toString(ownerDocument.getNodeURI(fNodeIndex));

        // attributes
        setupDefaultAttributes();
        int attrIndex = ownerDocument.getNodeValue(fNodeIndex);
        if (attrIndex != -1) {
            NamedNodeMap attrs = getAttributes();
            do {
                NodeImpl attr =
                    (NodeImpl)ownerDocument.getNodeObject(attrIndex);
                attrs.setNamedItem(attr);
                attrIndex = ownerDocument.getPrevSibling(attrIndex);
            } while (attrIndex != -1);
        }

    } // synchronizeData()

    /**
     * Synchronizes the node's children with the internal structure.
     * Fluffing the children at once solves a lot of work to keep
     * the two structures in sync. The problem gets worse when
     * editing the tree -- this makes it a lot easier.
     */
    protected final void synchronizeChildren() {

        // no need to sync in the future
        syncChildren(false);

        // create children and link them as siblings
        DeferredDocumentImpl ownerDocument =
            (DeferredDocumentImpl)this.ownerDocument;
        ChildNode first = null;
        ChildNode last = null;
        for (int index = ownerDocument.getLastChild(fNodeIndex);
             index != -1;
             index = ownerDocument.getPrevSibling(index)) {

            ChildNode node = (ChildNode)ownerDocument.getNodeObject(index);
            if (last == null) {
                last = node;
            }
            else {
                first.previousSibling = node;
            }
            node.ownerNode = this;
            node.owned(true);
            node.nextSibling = first;
            first = node;
        }
        if (last != null) {
            firstChild = first;
            first.firstChild(true);
            lastChild(last);
        }

    } // synchronizeChildren()

} // class DeferredElementImpl
