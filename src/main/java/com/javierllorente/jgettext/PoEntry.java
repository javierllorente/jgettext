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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javier
 */
public class PoEntry implements TranslationEntry {
    private List<String> comments;
    private List<String> msgCtxt;
    private TranslationElement msgIdElement;
    private TranslationElement msgIdPluralElement;
    private List<TranslationElement> msgStrElements;
    private TranslationElement msgStrElement;  
    private List<String> obsoleteEntries;
    private boolean plural;
    private boolean fuzzy;
    private static final String FUZZY_FLAG = "#, fuzzy";
    private enum MsgType {
        MSGCTXT,
        MSGID,
        MSGID_PLURAL,
        MSGSTR
    }

    public PoEntry() {
        init();
    }
    
    public PoEntry(boolean obsolete) {
        if (obsolete) {
            obsoleteEntries = new ArrayList<>();
        } else {
            init();
        }
    }
    
    private void init() {
        comments = new ArrayList<>();
        msgIdElement = new PoElement();
        msgStrElement = new PoElement();
        plural = false;
    }
    
    @Override
    public List<String> getComments() {
        return comments;
    }

    @Override
    public void setComments(List<String> comments) {
        this.comments = comments;
    }
    
    @Override
    public void addComment(String comment) {
        comments.add(comment);
    }    

    @Override
    public List<String> getMsgCtxt() {
        return msgCtxt;
    }

    @Override
    public void setMsgCtxt(List<String> msgCtxt) {
        this.msgCtxt = msgCtxt;
    }

    @Override
    public TranslationElement getMsgIdElement() {
        return msgIdElement;
    }
    
    @Override
    public void setMsgIdElement(TranslationElement msgIdElement) {
        this.msgIdElement = msgIdElement;
    }
    
    @Override
    public List<String> getMsgId() {
        return msgIdElement.get();
    }

    @Override
    public void setMsgId(List<String> msgId) {
        msgIdElement.set(msgId);
    }
    
    @Override
    public void addMsgIdEntry(String msgIdEntry) {
        msgIdElement.add(msgIdEntry);
    }

    @Override
    public TranslationElement getMsgIdPluralElement() {
        return msgIdPluralElement;
    }

    @Override
    public void setMsgIdPluralElement(TranslationElement msgIdPluralElement) {
        this.msgIdPluralElement = msgIdPluralElement;
    }    

    @Override
    public List<TranslationElement> getMsgStrElements() {
        return msgStrElements;
    }

    @Override
    public void setMsgStrElements(List<TranslationElement> msgStrElements) {
        this.msgStrElements = msgStrElements;
    }
    
    @Override
    public TranslationElement getMsgStrElement() {
        return msgStrElement;
    }
    
    @Override
    public void setMsgStrElement(TranslationElement msgStrElement) {
        this.msgStrElement = msgStrElement;
    }

    @Override
    public List<String> getMsgStr() {
        return msgStrElement.get();
    }

    @Override
    public void setMsgStr(List<String> msgStr) {
        msgStrElement.set(msgStr);
    }
    
    @Override
    public void addMsgStrEntry(String msgStrEntry) {
        msgStrElement.add(msgStrEntry);
    }

    @Override
    public List<String> getObsoleteEntries() {
        return obsoleteEntries;
    }

    @Override
    public void setObsoleteEntries(List<String> obsoleteEntries) {
        this.obsoleteEntries = obsoleteEntries;
    }
    
    @Override
    public void addObsoleteEntry(String obsoleteEntry) {
        obsoleteEntries.add(obsoleteEntry);
    }
    
    @Override
    public boolean isPlural() {
        return plural;
    }

    @Override
    public void setPlural(boolean plural) {
        this.plural = plural;
    }

    @Override
    public boolean isFuzzy() {
        return fuzzy;
    }

    @Override
    public void setFuzzy(boolean fuzzy) {        
        this.fuzzy = fuzzy;
    }
    
    @Override
    public void addFuzzyFlag() {
        if (!comments.contains(FUZZY_FLAG)) {
            for (int i = 0; i < comments.size(); i++) {
                if (comments.get(i).startsWith("msgid")) {
                    comments.add(i, FUZZY_FLAG);
                    fuzzy = true;
                    break;
                }
            }
        }
    }
    
    @Override
    public void removeFuzzyFlag() {
        comments.removeIf((String s) -> (s.startsWith(FUZZY_FLAG)
                || s.startsWith("#| msgid")));
        fuzzy = false;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        if (comments != null && !comments.isEmpty()) {
            sb.append(String.join("\n", comments)).append("\n");
        }

        if (msgCtxt != null) {
            appendMsg(sb, msgCtxt, MsgType.MSGCTXT);
        }
        
        if (msgIdElement != null) {
            sb.append(msgIdElement.toString());
        }
        
        if (msgIdPluralElement != null) {
            sb.append(msgIdPluralElement.toString());
        }
        
        if (msgStrElements != null) {
            for (TranslationElement element : msgStrElements) {
                sb.append(element.toString());
            }
        }
        
        if (msgStrElement != null) {
            sb.append(msgStrElement.toString());
        }
        
        if (obsoleteEntries != null && !obsoleteEntries.isEmpty()) {
            sb.append(String.join("\n", obsoleteEntries)).append("\n");
        }
        
        return sb.toString();
    }
    
    private void appendMsg(StringBuilder sb, List<String> msg, MsgType type) {
        String typeStr = type.toString().toLowerCase();
        
        if (!(plural && typeStr.startsWith("msgstr"))) {
            sb.append(typeStr).append(" ");
        }
        
        if (msg.size() == 1) {
            sb.append("\"").append(msg.get(0)).append("\"\n");
        } else if (msg.isEmpty() || msg.size() > 1) {           
         
            if (!(plural && typeStr.startsWith("msgstr"))) {
                sb.append("\"\"\n");
            }
            
            for (int i=0; i<msg.size(); i++) {                
                sb.append("\"");
                
                if (msg.get(i).contains("\\n")) {
                    sb.append(msg.get(i).replace("\\n", "\\n\""));
                } else {
                    if (i == msg.size() - 1) {
                        sb.append(msg.get(i)).append("\"");
                    } else {
                        sb.append(msg.get(i).replace("\n", "\"\n"));
                    }
                }
                
                // Check last header
                if (!plural && (i == msg.size() - 1) && !msg.get(i).endsWith("\\n\n")) {
                    sb.append("\n");
                }
                
            }
        }
    }    
}
