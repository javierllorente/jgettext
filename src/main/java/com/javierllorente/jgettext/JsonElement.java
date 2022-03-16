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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javier
 */
public class JsonElement implements TranslationElement {

    private List<String> lines;
    private String tag;

    public JsonElement() {
        lines = new ArrayList<>();
    }

    @Override
    public List<String> get() {
        return lines;
    }

    @Override
    public void set(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public void add(String line) {
        lines.add(line);
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        String str = "";
        String line;

        for (int i = 0; i < lines.size(); i++) {
            line = lines.get(i);

            if (i > 0 && i != lines.size() - 1) {
                line += "\\n";
            }

            str += line;
        }

        return str;
    }
    
}
