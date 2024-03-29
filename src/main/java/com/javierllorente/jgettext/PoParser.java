/*
 * Copyright (C) 2020-2021 Javier Llorente <javier@opensuse.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.javierllorente.jgettext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javier
 */
public class PoParser implements TranslationParser {

    private enum Type {
        UNKNOWN,
        COMMENT,
        MSGCTXT,
        MSGID,
        MSGID_PLURAL,
        MSGSTR_PLURAL,
        MSGSTR,
        OBSOLETE,
        BLANK
    }

    private final TranslationFile poFile;    

    public PoParser() {
        poFile = new PoFile();
    }
    
    @Override
    public TranslationFile parse(String str) throws IOException {
        
        try (BufferedReader reader = new BufferedReader(new StringReader(str))) {
            String line;
            Type type = Type.UNKNOWN;
            List<String> comments = null;
            List<String> msgCtxt = null;
            TranslationElement msgIdElement = null;
            TranslationElement msgIdPluralElement = null;
            List<TranslationElement> msgStrElements = null;
            TranslationElement msgStrElement = null;
            List<String> obsoleteEntries = null;
            boolean msgCtxtFound = false;
            boolean msgIdFound = false;
            boolean msgIdPluralFound = false;
            boolean msgStrFound = false;
            boolean obsoleteFound = false;
            boolean commentFound = false;
            boolean fuzzy = false;
            
            boolean header = true;
            boolean endOfFile = false;
            
//            System.out.println("line: ");
            // "\n" == null 
            while (!endOfFile) {

                // Do not skip last \n
                if ((line = reader.readLine()) == null) {
                    endOfFile = true;
                    line = "";
                }
                
//                System.out.println(line);
                if (line.startsWith("#~")) {
                    type = Type.OBSOLETE;
                    if (!obsoleteFound) {
                        obsoleteEntries = new ArrayList<>();
                        obsoleteFound = true;
                    }
                } else if(line.matches("^#(\\s*|\\.|\\:|\\,).*$")) {
                    type = Type.COMMENT;
                    if (!commentFound) {
                        comments = new ArrayList<>();
                        commentFound = true;
                    }
                    if (line.startsWith("#, fuzzy")) {
                        fuzzy = true;
                    }
                } else if (line.startsWith("msgctxt")) {
                    type = Type.MSGCTXT;
                    msgCtxtFound = true;
                    msgCtxt = new ArrayList<>();
                } else if (line.startsWith("msgid_plural")) {
                    type = Type.MSGID_PLURAL;
                    msgIdPluralFound = true;
                    msgIdPluralElement = new PoElement();
                    msgStrElements = new ArrayList<>();
                } else if (line.startsWith("msgid")) {
                    type = Type.MSGID;
                    msgIdFound = true;
                    msgIdElement = new PoElement();
                } else if (line.startsWith("msgstr[")) {
                    type = Type.MSGSTR_PLURAL;
                    msgStrFound = true;
                    msgStrElement = new PoElement();
                    msgStrElements.add(msgStrElement);
                } else if (line.startsWith("msgstr")) {
                    type = Type.MSGSTR;
                    msgStrFound = true;
                    msgStrElement = new PoElement();
                    
                } else if (line.isEmpty()) {
                    type = Type.BLANK;
                }
                
                if (type.toString().toLowerCase().startsWith("msg")) {
                    line = cleanDoubleQuotes(line);
                }

                switch (type) {
                    case COMMENT:
                        comments.add(line);
                        break;
                    case MSGCTXT:
                        addMsgLine(line, msgCtxt, type);
                        break;
                    case MSGID:
                        addMsgLine(line, msgIdElement, type);
                        break;
                    case MSGID_PLURAL:
                        addMsgLine(line, msgIdPluralElement, type);
                        break;
                    case MSGSTR_PLURAL:                        
                        addMsgLine(line, msgStrElement, type);                         
                        break;
                    case MSGSTR:
                        addMsgLine(line, msgStrElement, type);
                        break;
                    case OBSOLETE:
                        if (obsoleteFound) {
                            obsoleteEntries.add(line);
                        }
                        break;
                    case BLANK:
                        if (header) {
                            TranslationEntry entry = new PoEntry();
                            entry.setComments(comments);

                            cleanFirstLineBreak(msgIdElement);
                            cleanFirstLineBreak(msgStrElement);
                            cleanLastLineBreak(msgIdElement);
                            cleanLastLineBreak(msgStrElement);
                            
                            entry.setMsgIdElement(msgIdElement);
                            entry.setMsgStrElement(msgStrElement);                            
                            poFile.addHeader(entry);
                            
                            header = false;
                            commentFound = false;
                            msgIdFound = false;
                            msgStrFound = false;
                        } else if (msgIdFound && msgStrFound) {
                            TranslationEntry entry = new PoEntry();
                            if (commentFound) {
                                entry.setComments(comments); 
                                commentFound = false;
                            }
                            if (fuzzy) {
                                entry.setFuzzy(true);
                                fuzzy = false;
                            }
                            if (msgCtxtFound) {
                                cleanLastLineBreak(msgCtxt);
                                entry.setMsgCtxt(msgCtxt);
                                msgCtxtFound = false;
                            }
                            if (msgIdPluralFound) {
                                entry.setPlural(true);
                                msgIdPluralFound = false;
                            }                            
                            
                            cleanFirstLineBreak(msgIdElement);                            
                            cleanLastLineBreak(msgIdElement);                            
                            
                            if (entry.isPlural()) {
                                cleanFirstLineBreak(msgIdPluralElement);
                                cleanLastLineBreak(msgIdPluralElement);
                                entry.setMsgIdPluralElement(msgIdPluralElement);
                                for (TranslationElement element : msgStrElements) {
                                    cleanFirstLineBreak(element);
                                    cleanLastLineBreak(element);
                                }
                                entry.setMsgStrElements(msgStrElements);
                            } else {
                                cleanFirstLineBreak(msgStrElement);
                                cleanLastLineBreak(msgStrElement);
                                entry.setMsgStrElement(msgStrElement);
                            }
                            
                            entry.setMsgIdElement(msgIdElement);
                            poFile.addEntry(entry);                            
                            msgIdFound = false;
                            msgStrFound = false;
                        } else if (obsoleteFound) {
                            TranslationEntry entry = new PoEntry(true);
                            entry.setObsoleteEntries(obsoleteEntries);
                            poFile.addObsoleteEntry(entry);
                            obsoleteFound = false;
                            if (commentFound) {
                                entry.setComments(comments); 
                                commentFound = false;
                            }
                            if (fuzzy) {
                                entry.setFuzzy(true);
                                fuzzy = false;
                            }
                        }
                        break;
                }

            }
            System.out.println("end_line");
            System.out.println("POParser entries size: " + poFile.getEntries().size());
        }

        return poFile;
    }

    private String cleanDoubleQuotes(String line) {
        if (line.startsWith("\"")) {
            line = line.substring(1);
        } else if (line.startsWith("msg")) {
            line = line.replaceFirst("\"", "");
        }
        if (line.endsWith("\"")) {
            line = line.substring(0, line.length() - 1) + "";
        }
        return line;
    }

    private void addMsgLine(String line, List<String> msg, Type type) {  
        msg.add(line.replace(type.toString().toLowerCase() + " ", "") + "\n");
    }
    
    private void addMsgLine(String line, TranslationElement element, Type type) {

        if (line.startsWith("msg")) {           
            String[] split = line.split("\\s", 2);
            String tag = split[0];
            String splitLine = "";
            String newLine = "";
            if (split.length > 1) {
                splitLine = split[1];
                newLine = "\n";
            }
            element.setTag(tag);
            element.add(splitLine + newLine);
        } else {
            element.add(line + "\n");
        }

    }
    
    private void cleanLastLineBreak(List<String> msg) {
        if (msg.size() > 0) {
            msg.set(msg.size() - 1, msg.get(msg.size() - 1).replaceFirst("\n$", ""));
        }
    }   
    
    private void cleanFirstLineBreak(TranslationElement element) {
        if (element.get().size() > 1 && element.get().get(0).equals("\n")) {
            element.get().set(0, "");
        }
    }
    
    private void cleanLastLineBreak(TranslationElement element) {
            if (element.get().size() > 0) {
                element.get().set(element.get().size() - 1, element.get().get(
                        element.get().size() - 1).replaceFirst("\n$", ""));
            }
        }
    }
