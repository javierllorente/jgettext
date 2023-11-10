/*
 * Copyright (C) 2023 Javier Llorente <javier@opensuse.org>
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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class YamlParser implements TranslationParser {
    
    private final TranslationFile translationFile;
    boolean sourceLanguage;

    public YamlParser() {
        translationFile = new YamlFile();
        sourceLanguage = false;
    }

    public boolean isSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(boolean sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }
    
    @Override
    public TranslationFile parse(String str) throws IOException {
        // NOTE: experimental support for Yaml
        LoadSettings settings = LoadSettings.builder().build();
        Load load = new Load(settings);
        Map<String, Object> map = (HashMap<String, Object>) load.loadFromString(str);

        StringBuilder tag = new StringBuilder();
        return parse(map, tag, false);
    }
    
    private TranslationFile parse(Map<String, Object> map, StringBuilder tag, boolean child) {
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            TranslationElement translationElement;
            TranslationEntry translationEntry = new YamlEntry();
            
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object val = entry.getValue();
            
            if (val instanceof Map) {
                if (child) {
                    tag.append(":").append(key);
                } else {
                    tag.replace(0, tag.length(), key);
                }
                parse((Map<String, Object>) val, tag, true);
            } else {
                translationElement = new YamlElement();
                translationElement.setTag((child ? tag + ":" : "") + key);
                translationElement.add((String) val);

                if (sourceLanguage) {
                    translationEntry.setMsgIdElement(translationElement);

                    for (int i = 0; i < translationFile.getEntries().size(); i++) {
                        if (translationFile.getEntries().get(i).getMsgStrElement()
                                .getTag().equals(translationElement.getTag())) {
                            translationFile.getEntries().get(i).setMsgIdElement(translationElement);
                            break;
                        }
                    }

                    // Just in case if target language entries > source language entries
                    for (TranslationEntry te : translationFile.getEntries()) {
                        if (te.getMsgIdElement().get().isEmpty()) {
                            te.getMsgIdElement().set(Arrays.asList("???"));
                            te.getMsgIdElement().setTag(te.getMsgStrElement().getTag());
                        }
                    }
                    
                } else {
                    translationEntry.setMsgStrElement(translationElement);
                    translationFile.addEntry(translationEntry);
                }                
            }

            if (!iterator.hasNext()) {
                int start = tag.lastIndexOf(":");
                if (start != -1) {
                    tag.delete(start, tag.length());
                }
                child = false;
            }
        }

        return translationFile;
    }
    
}
