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
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author javier
 */
public class JsonFile implements TranslationFile {
    
    private final List<TranslationEntry> entries;

    public JsonFile() {
        entries = new ArrayList<>();
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
    public void updateEntry(int index, List<TranslationElement> elements) {        
        entries.get(index).getMsgStrElement().set(elements.get(0).get());
    }

    @Override
    public String toString() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (TranslationEntry entry : entries) {
            builder.add(entry.getMsgIdElement().getTag(), 
                    entry.getMsgStrElement().toString());
        }

        Map<String, Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory jwf = Json.createWriterFactory(config);
        
        StringWriter sw = new StringWriter();
        JsonObject jsonObject = builder.build();

        try (JsonWriter jsonWriter = jwf.createWriter(sw)) {
            jsonWriter.writeObject(jsonObject);
        }

        return sw.toString();
    }
    
}
