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
