/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights
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

package org.apache.xerces.parsers;

import org.apache.xerces.dom3.ls.DOMInputSource;
import org.apache.xerces.dom3.as.ASModel;
import org.apache.xerces.dom3.as.DOMASBuilder;
import org.apache.xerces.dom3.as.DOMASException;

import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.grammars.Grammar;

import org.apache.xerces.dom.ASModelImpl;

import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.validation.XMLGrammarPoolImpl;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.impl.xs.XSDDescription;
import org.apache.xerces.impl.xs.XSGrammarBucket;
import org.apache.xerces.impl.xs.SubstitutionGroupHandler;
import org.apache.xerces.impl.xs.models.CMBuilder;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSDeclarationPool;

import java.util.Vector;
import org.apache.xerces.util.SymbolTable;

/**
 * This is Abstract Schema DOM Builder class. It extends the DOMBuilderImpl
 * class. Provides support for preparsing schemas.
 *
 * @author Pavani Mukthipudi, Sun Microsystems Inc.
 * @author Neil Graham, IBM
 * @version $Id$
 *
 */

public class DOMASBuilderImpl
    extends DOMBuilderImpl implements DOMASBuilder {

    //
    // Constants
    //

    // Feature ids

    protected static final String SCHEMA_FULL_CHECKING =
        Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_FULL_CHECKING;

    // Property ids

    protected static final String ERROR_REPORTER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;

    protected static final String SYMBOL_TABLE =
        Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;

    protected static final String ENTITY_MANAGER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_MANAGER_PROPERTY;


    //
    // Data
    //

    protected XSGrammarBucket fGrammarBucket;
    protected SubstitutionGroupHandler fSubGroupHandler;
    protected CMBuilder fCMBuilder;
    protected XSDHandler fSchemaHandler;
    protected XMLErrorReporter fErrorReporter;
    protected XMLEntityResolver fEntityResolver;
    protected SymbolTable fSymbolTable;
    protected final XSDDescription fXSDDescription = new XSDDescription() ;
    protected XMLGrammarPool fGrammarPool = null;

    protected ASModelImpl fAbstractSchema;

    //
    // Constructors
    //

    /**
     * Constructs a DOM Builder using the dtd/xml schema parser configuration.
     */
    public DOMASBuilderImpl() {
        super(new XMLGrammarCachingConfiguration());
    } // <init>

    /**
     * Constructs a DOM Builder using the specified parser configuration.
     */
    public DOMASBuilderImpl(XMLParserConfiguration config) {
        super(config);
    } // <init>(XMLParserConfiguration)

    /**
     * Constructs a DOM Builder using the specified symbol table.
     */
    public DOMASBuilderImpl(SymbolTable symbolTable) {
        super(new XMLGrammarCachingConfiguration(symbolTable));
    } // <init>(SymbolTable)


    /**
     * Constructs a DOM Builder using the specified symbol table and
     * grammar pool.
     */
    public DOMASBuilderImpl(SymbolTable symbolTable, XMLGrammarPool grammarPool) {
        super(new XMLGrammarCachingConfiguration(symbolTable, grammarPool));
        fGrammarPool = grammarPool;
    }

    //
    // DOMASBuilder methods
    //

    /**
     * Associate an <code>ASModel</code> with a document instance. This
     * <code>ASModel</code> will be used by the "
     * <code>validate-if-schema</code>" and "
     * <code>datatype-normalization</code>" options during the load of a new
     * <code>Document</code>.
     */
    public ASModel getAbstractSchema() {
        return fAbstractSchema;
    }

    /**
     * Associate an <code>ASModel</code> with a document instance. This
     * <code>ASModel</code> will be used by the "
     * <code>validate-if-schema</code>" and "
     * <code>datatype-normalization</code>" options during the load of a new
     * <code>Document</code>.
     */
    public void setAbstractSchema(ASModel abstractSchema) {

        // since the ASModel associated with this object is an attribute
        // according to the DOM IDL, we must obliterate anything
        // that was set before, rather than adding to it.
        fAbstractSchema = (ASModelImpl)abstractSchema;

        // make sure the GrammarPool is properly initialized.
        if (fGrammarPool == null) {
            fGrammarPool = (XMLGrammarPool)fConfiguration.getProperty(StandardParserConfiguration.XMLGRAMMAR_POOL);
        }
        // if there is no grammar pool, create one
        // REVISIT: ASBuilder should always create one.
        if (fGrammarPool == null) {
            fGrammarPool = new XMLGrammarPoolImpl();
            fConfiguration.setProperty(StandardParserConfiguration.XMLGRAMMAR_POOL,
                                       fGrammarPool);
        }
        if (fAbstractSchema != null) {
            initGrammarPool(fAbstractSchema);
        }
    }

    /**
     * Parse a Abstract Schema from a location identified by an URI.
     *
     * @param uri The location of the Abstract Schema to be read.
     * @return The newly created <code>Abstract Schema</code>.
     * @exception DOMASException
     *   Exceptions raised by <code>parseASURI()</code> originate with the
     *   installed ErrorHandler, and thus depend on the implementation of
     *   the <code>DOMErrorHandler</code> interfaces. The default error
     *   handlers will raise a <code>DOMASException</code> if any form of
     *   Abstract Schema inconsistencies or warning occurs during the parse,
     *   but application defined errorHandlers are not required to do so.
     *   <br> WRONG_MIME_TYPE_ERR: Raised when <code>mimeTypeCheck</code> is
     *   <code>true</code> and the inputsource has an incorrect MIME Type.
     *   See attribute <code>mimeTypeCheck</code>.
     * @exception DOMSystemException
     *   Exceptions raised by <code>parseURI()</code> originate with the
     *   installed ErrorHandler, and thus depend on the implementation of
     *   the <code>DOMErrorHandler</code> interfaces. The default error
     *   handlers will raise a DOMSystemException if any form I/O or other
     *   system error occurs during the parse, but application defined error
     *   handlers are not required to do so.
     */
    public ASModel parseASURI(String uri)
                              throws DOMASException, Exception {
        XMLInputSource source = new XMLInputSource(null, uri, null);
        try {
            return parseASInputSource(source);
        }

        catch (XNIException e) {
            Exception ex = e.getException();
            throw ex;
        }

    }

    /**
     * Parse a Abstract Schema from a location identified by an
     * <code>DOMInputSource</code>.
     *
     * @param is The <code>DOMInputSource</code> from which the source
     *   Abstract Schema is to be read.
     * @return The newly created <code>ASModel</code>.
     * @exception DOMASException
     *   Exceptions raised by <code>parseASURI()</code> originate with the
     *   installed ErrorHandler, and thus depend on the implementation of
     *   the <code>DOMErrorHandler</code> interfaces. The default error
     *   handlers will raise a <code>DOMASException</code> if any form of
     *   Abstract Schema inconsistencies or warning occurs during the parse,
     *   but application defined errorHandlers are not required to do so.
     *   <br> WRONG_MIME_TYPE_ERR: Raised when <code>mimeTypeCheck</code> is
     *   true and the inputsource has an incorrect MIME Type. See attribute
     *   <code>mimeTypeCheck</code>.
     * @exception DOMSystemException
     *   Exceptions raised by <code>parseURI()</code> originate with the
     *   installed ErrorHandler, and thus depend on the implementation of
     *   the <code>DOMErrorHandler</code> interfaces. The default error
     *   handlers will raise a DOMSystemException if any form I/O or other
     *   system error occurs during the parse, but application defined error
     *   handlers are not required to do so.
     */
    public ASModel parseASInputSource(DOMInputSource is)
                                      throws DOMASException, Exception {
                                      
        // need to wrap the DOMInputSource with an XMLInputSource
        XMLInputSource xis = this.dom2xmlInputSource(is);
        try {
            return parseASInputSource(xis);
        }
        catch (XNIException e) {
            Exception ex = e.getException();
            throw ex;
        }
    }

    ASModel parseASInputSource(XMLInputSource is) throws Exception {
                                      
       if (fSchemaHandler == null) {
           fGrammarBucket = new XSGrammarBucket();
           fSubGroupHandler = new SubstitutionGroupHandler(fGrammarBucket);
           fSchemaHandler = new XSDHandler(fGrammarBucket);
           fCMBuilder = new CMBuilder(new XSDeclarationPool());
       }

       fErrorReporter = (XMLErrorReporter)fConfiguration.getProperty(ERROR_REPORTER);
       fEntityResolver = (XMLEntityResolver)fConfiguration.getProperty(ENTITY_MANAGER);

       fSymbolTable = (SymbolTable)fConfiguration.getProperty(SYMBOL_TABLE);
       String externalSchemas =
            (String)(fConfiguration.getProperty(Constants.XERCES_PROPERTY_PREFIX+Constants.SCHEMA_LOCATION));
       String noNamespaceExternalSchemas =
            (String)(fConfiguration.getProperty(Constants.XERCES_PROPERTY_PREFIX+Constants.SCHEMA_NONS_LOCATION));

       initGrammarBucket();
       fSubGroupHandler.reset();
       fSchemaHandler.reset(fErrorReporter, fEntityResolver, fSymbolTable, externalSchemas, noNamespaceExternalSchemas, fGrammarPool);

       // Should check whether the grammar with this namespace is already in
       // the grammar resolver. But since we don't know the target namespace
       // of the document here, we leave such check to XSDHandler
        fXSDDescription.reset();
        fXSDDescription.setContextType(XSDDescription.CONTEXT_PREPARSE);
        String sid = is.getSystemId();
        if (sid != null) {
            fXSDDescription.setLiteralSystemId(sid);
            fXSDDescription.setExpandedSystemId(sid);
            fXSDDescription.setLocationHints(new String[]{sid});
        }
        SchemaGrammar grammar = fSchemaHandler.parseSchema(is, fXSDDescription);

        if (getFeature(SCHEMA_FULL_CHECKING)) {
            XSConstraints.fullSchemaChecking(fGrammarBucket, fSubGroupHandler, fCMBuilder, fErrorReporter);
        }
 
        ASModelImpl newAsModel = new ASModelImpl();
        addGrammars(newAsModel, fGrammarBucket);
        return newAsModel;
    }

    // put all the grammars we have access to in the GrammarBucket
    private void initGrammarBucket() {
        fGrammarBucket.reset();
        if (fAbstractSchema != null)
            initGrammarBucketRecurse(fAbstractSchema);
    }
    private void initGrammarBucketRecurse(ASModelImpl currModel) {
        if(currModel.getGrammar() != null) {
            fGrammarBucket.putGrammar(currModel.getGrammar());
        }
        for(int i = 0; i < currModel.getInternalASModels().size(); i++) {
            ASModelImpl nextModel = (ASModelImpl)(currModel.getInternalASModels().elementAt(i));
            initGrammarBucketRecurse(nextModel);
        }
    }

    private void addGrammars(ASModelImpl model, XSGrammarBucket grammarBucket) {
        SchemaGrammar [] grammarList = grammarBucket.getGrammars();
        for(int i=0; i<grammarList.length; i++) {
            ASModelImpl newModel = new ASModelImpl();
            newModel.setGrammar(grammarList[i]);
            model.addASModel(newModel);
        }
    } // addGrammars

    private void initGrammarPool(ASModelImpl currModel) {
        // put all the grammars in fAbstractSchema into the grammar pool.
        if (fGrammarPool != null) {
            Grammar[] grammars = new Grammar[1];
            if ((grammars[0] = (Grammar)currModel.getGrammar()) != null) {
                fGrammarPool.cacheGrammars(grammars[0].getGrammarDescription().getGrammarType(), grammars);
            }
            Vector modelStore = currModel.getInternalASModels();
            for (int i = 0; i < modelStore.size(); i++) {
                initGrammarPool((ASModelImpl)modelStore.elementAt(i));
            }
        }
    }
} // class DOMASBuilderImpl
