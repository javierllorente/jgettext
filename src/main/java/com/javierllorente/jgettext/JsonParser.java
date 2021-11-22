/*
 * Copyright (C) 2021 Javier Llorente <javier@opensuse.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
