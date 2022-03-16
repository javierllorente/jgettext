/*
 * Copyright (C) 2021 Javier Llorente <javier@opensuse.org>
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

import jakarta.json.Json;
import jakarta.json.JsonString;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 *
 * @author javier
 */
public class JsonParser implements TranslationParser {

    private final TranslationFile translationFile;
    boolean sourceLanguage;
    
    public JsonParser() {
        translationFile = new JsonFile();
        sourceLanguage = false;
    }

    public boolean isSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(boolean sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }
    
    @Override
    public TranslationFile parse(String str) {
        try (jakarta.json.stream.JsonParser jsonParser = Json.createParser(
                new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)))) {
            jsonParser.next();

            jsonParser.getObjectStream().forEach((entry) -> {
                TranslationElement translationElement = new JsonElement();
                translationElement.add(((JsonString) entry.getValue()).getString());
                translationElement.setTag(entry.getKey());

                TranslationEntry translationEntry = new JsonEntry();

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

                System.out.println(entry.getKey() + " = " + ((JsonString) entry.getValue()).getString());

            });
        }
        
        return translationFile;
    }
    
}
