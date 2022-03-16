/*
 * Copyright (C) 2020-2022 Javier Llorente <javier@opensuse.org>
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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javier
 */
public class PoFile implements TranslationFile {
    private final List<TranslationEntry> header;
    private final List<TranslationEntry> entries;
    private List<TranslationEntry> obsoleteEntries;

    public PoFile() {
        header = new ArrayList<>();
        entries = new ArrayList<>();
        obsoleteEntries = null;
    }
    
    private String getHeader(String key) {
        String value = "";
        for (TranslationEntry entry : header) {
            for (int i = 0; i < entry.getMsgStr().size(); i++) {
                if (entry.getMsgStr().get(i).contains(key + ": ")) {
                    value = entry.getMsgStr().get(i).split(key + ": ")[1];
                }
            }
        }
        return value;
    }

    private void setHeader(String key, String value) {
        for (TranslationEntry entry : header) {
            for (int i = 0; i < entry.getMsgStr().size(); i++) {
                if (entry.getMsgStr().get(i).contains(key)) {
                    entry.getMsgStr().set(i, key + ": " + value + "\\n\n");
                    return;
                }
            }
        }
    }
    
    @Override
    public String getRevisionDate() {
        return getHeader("PO-Revision-Date");
    }
    
    @Override
    public void setRevisionDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mmZ");
        setHeader("PO-Revision-Date", ZonedDateTime.now().format(dateTimeFormatter));
    }
    
    @Override
    public String getTranslator() {
        return getHeader("Last-Translator");
    }
    
    @Override
    public void setTranslator(String name, String email) {
        setHeader("Last-Translator", name + " <" + email + ">");
    }
    
    @Override
    public String getGenerator() {
        return getHeader("X-Generator");
    }
    
    @Override
    public void setGenerator(String generator) {
        setHeader("X-Generator", generator);
    }
    
    @Override
    public void addHeader(TranslationEntry headerEntry) {
        header.add(headerEntry);
    }
    
    @Override
    public List<TranslationEntry> getEntries() {
        return entries;
    }
    
    @Override
    public void addEntry(TranslationEntry entry) {
        entries.add(entry);
    }
    
    @Override
    public void addObsoleteEntry(TranslationEntry obsoleteEntry) {
        if (obsoleteEntries == null) {
            obsoleteEntries = new ArrayList<>();
        }
        obsoleteEntries.add(obsoleteEntry);
    }
    
    private void updateMsgStr(List<String> originalLines, List<String> revisedLines) {
        
      if (revisedLines.size() < originalLines.size()) {

            for (int k = originalLines.size() - 1; k > revisedLines.size(); k--) {
                originalLines.remove(k);
            }
            
            if ((originalLines.size() == 2) && (originalLines.get(0).isEmpty())) {                
                originalLines.set(0, originalLines.get(1));                
                originalLines.remove(1);
            }
            
        } else if ((revisedLines.size() != 1) && revisedLines.size() > originalLines.size()) {

            String lineBreak = "\n";
            
            if (originalLines.size() == 1) {
                originalLines.add(0, "");
            }
            
            for (int i = originalLines.size(); i < revisedLines.size(); i++) {
                if (i == revisedLines.size() - 1) {
                    lineBreak = "";
                }                

                originalLines.add(revisedLines.get(i) + lineBreak);
            }
            
        }
      
        String lineBreak = "\n";
        int j = 1;
        if ((revisedLines.size() == 1) 
                || !(originalLines.size() == 2 
                && originalLines.get(0).isEmpty())) {
            j = 0;
        }

        for (int i = 0; i < revisedLines.size(); i++, j++) {
            
            if (i == revisedLines.size() - 1) {
                lineBreak = "";
            }

            if ((revisedLines.size() == 1)
                    || (j <= originalLines.size() - 1) 
                    && !(revisedLines.get(i) + lineBreak)
                            .equals(originalLines.get(j))) {

                if ((j == 0 && originalLines.size() == 1)
                        || (j != 0 && originalLines.size() > 1)) {

                    originalLines.set(j, revisedLines.get(i) + lineBreak);
                }
            }
        }
               
    }    
    
    @Override
    public void updateEntry(int index, List<TranslationElement> elements) {
        
        boolean plural = elements.size() > 1;
        
        if (plural) {
            for (int i = 0; i < elements.size(); i++) {
                updateMsgStr(entries.get(index).getMsgStrElements().get(i).get(), 
                        elements.get(i).get());
            }
        } else {
            updateMsgStr(entries.get(index).getMsgStrElement().get(), 
                        elements.get(0).get());
        }        
        
    }
    
    @Override
    public String toString() {
        String str = "";

        int lastHeaderIndex = header.get(0).getMsgStr().size() - 1;
        String lastHeader = header.get(0).getMsgStr().get(lastHeaderIndex);
        if (!lastHeader.endsWith("\\n\n")) {
            header.get(0).getMsgStr().set(lastHeaderIndex, lastHeader + "\n");
        }
        str += entriesToString(header);
        str = str.replaceFirst("\n$", "");

        str += entriesToString(entries);

        if (obsoleteEntries != null) {
            str += entriesToString(obsoleteEntries);
        }
        str = str.replaceFirst("\n$", "");

        return str;
    }

    private String entriesToString(List<TranslationEntry> entries) {
        String str = "";
        for (TranslationEntry entry : entries) {
            str += entry.toString() + "\n";
        }
        return str;
    }    
}
